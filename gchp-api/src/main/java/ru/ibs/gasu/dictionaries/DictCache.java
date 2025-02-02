package ru.ibs.gasu.dictionaries;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import ru.ibs.gasu.soap.generated.dictionary.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.ibs.gasu.gchp.util.Utils.logFormat;

@Component
public class DictCache implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DictCache.class);
    private static final long DURATION_MIN = 10L;
    private static final long DURATION_DAY = 1L;

    @Resource
    private IDictionaryDataWebService dictDataService;

    @Resource
    private IDictionaryMetadataWebService dictMetadataService;

    private final Cache<Dictionary, Map<Long, DictionaryDataRecordDescriptor>> shortTimeCache = Caffeine.newBuilder()
            .refreshAfterWrite(DURATION_MIN, TimeUnit.MINUTES)
            .initialCapacity(50)
            .maximumSize(100)
            .build(this::getDictFromService);

    private final Cache<Dictionary, Map<Long, DictionaryDataRecordDescriptor>> longTimeCache = Caffeine.newBuilder()
            .refreshAfterWrite(DURATION_DAY, TimeUnit.DAYS)
            .initialCapacity(5)
            .maximumSize(10)
            .build(this::getDictFromService);

    @Override
    public void afterPropertiesSet() {
        loadDictionaries();
    }

    private Map<Long, DictionaryDataRecordDescriptor> getDictFromService(Dictionary dictionary) {
        logger.info(logFormat("... Loading dictionary from NSI service: %s", dictionary.name()));

        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(dictionary.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + dictionary.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();
        loadConfig.setLimit(Integer.MAX_VALUE);

        DataPage dataPage = dictDataService.getDataRecords(domainDescriptor, false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + dictionary.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        logger.info(logFormat("==> Dictionary loaded from NSI service: %s, ID: %d, size: %d",
                dictionary.name(), domainDescriptor.getId(), dataList.size()));

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));
        return res;
    }

    public Collection<DictionaryDataRecordDescriptor> getDictFromCache(Dictionary dictionary) {
        Cache<Dictionary, Map<Long, DictionaryDataRecordDescriptor>> cache =
                EvictDuration.ShortEvict.equals(dictionary.getEvictDuration()) ? shortTimeCache : longTimeCache;

        Map<Long, DictionaryDataRecordDescriptor> dictIndex = cache.get(dictionary, this::getDictFromService);

        return dictIndex == null ? null : dictIndex.values();
    }

    public DictionaryDataRecordDescriptor getDictDataRecordFromCache(Dictionary dictionary, Object recordId) {
        if (recordId == null) return null;

        Long longRecordId = String.class.isAssignableFrom(recordId.getClass()) ? Long.parseLong((String) recordId) : (Long) recordId;
        Optional<DictionaryDataRecordDescriptor> descriptor = getDictFromCache(dictionary).stream()
                .filter(o -> longRecordId.equals(o.getId()))
                .findFirst();

        return descriptor.orElse(null);
    }

    public List<DictionaryDataRecordDescriptor> getDictDataRecordsFromCache(Dictionary dictionary, List<Long> recordIds) {
        if (recordIds == null) return null;

        return getDictFromCache(dictionary).stream()
                .filter(o -> recordIds.contains(o.getId()))
                .collect(Collectors.toList());
    }

    private void loadDictionaries() {
        Stream.of(Dictionary.values()).forEach(_dict -> {
            if (_dict.isLoadOnStart()) {
                getDictFromCache(_dict);
            }
        });
    }
}
