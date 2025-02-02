package ru.ibs.gasu.gchp.service.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.Converter;
import ru.ibs.gasu.dictionaries.DictCache;
import ru.ibs.gasu.dictionaries.Dictionary;
import ru.ibs.gasu.gchp.domain.DocumentsFilterPaginateCriteria;
import ru.ibs.gasu.gchp.domain.ExportTask;
import ru.ibs.gasu.gchp.entities.ProjectExcelProjection;
import ru.ibs.gasu.gchp.repositories.ProjectExcelProjectionRepository;
import ru.ibs.gasu.gchp.util.OdsConverter;
import ru.ibs.gasu.gchp.util.Utils;
import ru.ibs.gasu.soap.generated.dictionary.DictionaryDataRecordDescriptor;

import javax.transaction.Transactional;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.specifications.ProjectSpecification.filterByCriteria;

@Service
public class ExcelService {

    @Autowired
    private DictCache dictCache;

    @Autowired
    private ProjectExcelProjectionRepository projectExcelProjectionRepository;

    @Autowired
    private ApplicationContext ac;

    private final ConcurrentHashMap<Long, List<ExportTask>> tasks = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, List<ExportTask>> getTasks() {
        return tasks;
    }

    private static class Row {
        public List<Pair> cells = new ArrayList<>();
    }

    @AllArgsConstructor
    @Getter
    private static class Pair {
        private final String header;
        private final Object value;
        private final SectionColor color;
    }

    @Scheduled(fixedDelay = 180_000)
    public void clearOldTasks() {
        Instant now = Instant.now();
        for (List<ExportTask> userTasks : tasks.values()) {
            userTasks.removeIf(task -> {
                boolean isDoneTaskExpired = task.isDone() &&
                        Duration.between(task.getDoneTime().toInstant(), now).toMinutes() > 20;
                boolean isTaskBroken = !task.isDone() &&
                        Duration.between(task.getStartTime().toInstant(), now).toHours() > 2;
                return isDoneTaskExpired || isTaskBroken;
            });
        }
    }

    private void processField(ExcelField meta, Field field, Object project, Row accumulator)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try {
            String name = meta.name();
            String converterMethod = meta.converterMethod();
            Dictionary dictionary = meta.dictionary();
            Class<?> aClass = meta.converterClass();
            String type = meta.type();
            SectionColor color = meta.color();
            field.setAccessible(true);

            // notImplemented converter
            if ("notImplemented".equals(converterMethod)) {
                accumulator.cells.add(new Pair(name, "НЕ РЕАЛИЗОВАНО", color));
                return;
            }

            Object fieldValue = field.get(project);

            // primitive field
            if (converterMethod.isEmpty()) {
                accumulator.cells.add(new Pair(name, fieldValue, color));
                return;
            }

            boolean isStringCollection = false;
            Object data;
            // List of dictionary values field
            if (!(fieldValue instanceof List || fieldValue instanceof Set)) {
                data = Arrays.asList(fieldValue);
            } else {
                isStringCollection = true;
                data = fieldValue;
            }

            List<Object> ress = new ArrayList<>();
            for (Object o : ((Collection) data)) {
                // get dict DTO
                Object dictObject;
                if (type.equals("DICT")) {
                    Method methodD = Converter.class.getMethod(converterMethod, Object.class);
                    DictionaryDataRecordDescriptor dictDataRecordFromCache = dictCache.getDictDataRecordFromCache(dictionary, o);
                    dictObject = methodD.invoke(null, dictDataRecordFromCache);
                } else {
                    Object bean = ac.getBean(aClass);
                    Method methodB = aClass.getMethod(converterMethod, Object.class);
                    dictObject = methodB.invoke(bean, o);
                }

                if (dictObject == null) continue;

                // get dict DTO name
                Method getName = dictObject.getClass().getMethod("getName");
                Object invoke = getName.invoke(dictObject);

                ress.add(invoke);
                ress.removeIf(Objects::isNull);
            }

            Object toPut;
            if (isStringCollection && ress.size() > 1) {
                toPut = ress.stream().map(String::valueOf).collect(Collectors.joining("; "));
            } else {
                toPut = ress.size() > 0 ? ress.get(0) : null;
            }

            accumulator.cells.add(new Pair(name, toPut, color));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private Row getVals(ProjectExcelProjection project) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Row m = new Row();
        for (Field field : ProjectExcelProjection.class.getDeclaredFields()) {
            ExcelFields excelFields = field.getAnnotation(ExcelFields.class);
            if (excelFields != null) {
                for (ExcelField excelField : excelFields.fields()) {
                    processField(excelField, field, project, m);
                }
            }

            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField == null) continue;
            processField(excelField, field, project, m);
        }
        return m;
    }

    private ByteArrayOutputStream createExcelFile(List<Row> rows, ExportTask currentTask) {
        // create excel file
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("Export");
        CellStyle commonStyle = createCellStyle(book, false, false, CellStyle.ALIGN_CENTER, true);
        CellStyle dateStyle = createCellStyle(book, false, false, CellStyle.ALIGN_CENTER, true);

//        CellStyle boldStyle = createCellStyle(book, false, true, CellStyle.ALIGN_CENTER, true);
//        XSSFFont font = book.createFont();
//        font.setBold(true);
//        boldStyle.setFont(font);

        CreationHelper createHelper = book.getCreationHelper();
        dateStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("m/d/yy"));
        PredefinedStyles styles = new PredefinedStyles(dateStyle, commonStyle);

        // generate header row
        XSSFRow rowHeader = sheet.createRow(0);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (rows.size() == 0)
            return out;

        List<String> headers = rows.get(0).cells.stream().map(Pair::getHeader).collect(Collectors.toList());
        List<Pair> pairs = rows.get(0).cells;
        for (int i = 0; i < pairs.size(); i++) {
            XSSFCell cellInRow = createCellInRow(rowHeader, i, styles, pairs.get(i).getHeader());
            XSSFCellStyle cellStyle = createCellStyle(book, false, true, CellStyle.ALIGN_CENTER, true);
            XSSFFont font = book.createFont();
            font.setBold(true);
            cellStyle.setFont(font);
            SectionColor sectionColor = pairs.get(i).getColor();
            cellStyle.setFillForegroundColor(new XSSFColor(sectionColor.getColor()));
            cellInRow.setCellStyle(cellStyle);
        }

        // generate other rows
        int total = rows.size();
        double inc = 20D / total;

        int j = 0;
        for (Row row : rows) {
            XSSFRow xssfRow = sheet.createRow(j++ + 1);
            int k = 0;
            for (Pair pair : row.cells) {
                createCellInRow(xssfRow, k++, styles, pair.getValue());
            }
            currentTask.setProgress(currentTask.getProgress() + inc);
        }

        setCustomSheetColumnsWidth(sheet, headers.size());

        try {
            book.write(out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Async
    @Transactional
    public Future<String> export(DocumentsFilterPaginateCriteria criteria,
                                 ExportTask thisTask)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        thisTask.setStatus("Получение списка документов...");

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getLimit(), Utils.createSort(criteria.getSortInfo()));
        Page<ProjectExcelProjection> res = projectExcelProjectionRepository.findAll(filterByCriteria(criteria), pageable);

        thisTask.setStatus("Генерация данных для полей...");
        thisTask.setProgress(40);

        List<Row> vals = new ArrayList<>();

        List<ProjectExcelProjection> projects = res.getContent();
        long total = projects.size();
        double inc = 40D / total;
        for (ProjectExcelProjection project : projects) {
            Row m = getVals(project);
            vals.add(m);
            thisTask.setProgress(thisTask.getProgress() + inc);
        }

        thisTask.setStatus("Генерация файлов...");

        ByteArrayOutputStream out = createExcelFile(vals, thisTask);
        byte[] bytes = out.toByteArray();

        // конвертация в ods
        thisTask.setOds(OdsConverter.convertXlsxToOds(bytes));

        thisTask.setDone(true);
        thisTask.setStatus("Завершено");
        thisTask.setDoneTime(new Date());

        return new AsyncResult<>(
                Base64.getEncoder().encodeToString(bytes)
        );
    }

    @AllArgsConstructor
    private static class PredefinedStyles {
        public CellStyle date;
        public CellStyle common;
    }

    private void setCustomSheetColumnsWidth(XSSFSheet sheet, int finishColNum) {
        for (int i = 0; i < finishColNum; i++) {
            int colWidth = 30 * 256;
            sheet.setColumnWidth(i, colWidth);
        }
    }

    private static XSSFCell createCellInRow(XSSFRow row, int colIndex, PredefinedStyles cellStyles, Object value) {
        XSSFCell cell = row.createCell(colIndex);
        cell.setCellStyle(cellStyles.common);
        if (value != null) {
            if (value instanceof String) {
                String val = (String) value;
                if (val.length() > 32000)
                    val = val.substring(0, 32000);
                cell.setCellValue(val);
            } else if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Date) {
                cell.setCellStyle(cellStyles.date);
                cell.setCellValue((Date) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue(((Boolean) value) ? "Да" : "Нет");
            } else {
                cell.setCellValue(value.toString());
            }
        }
        return cell;
    }

    private static XSSFCellStyle createCellStyle(XSSFWorkbook wb, boolean locked, boolean color, short align, boolean border) {
        XSSFCellStyle style = wb.createCellStyle();
        if (border) {
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        }
        if (color) {
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//            style.setFillForegroundColor(new XSSFColor(new Color(244, 246, 246)));//Color.LIGHT_GRAY));
        }

        style.setAlignment(align);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setLocked(locked);
        return style;
    }

    public enum SectionColor {
        GENERAL_INFO(new Color(197, 217, 241)),
        DESCRIPTION(new Color(230, 184, 183)),
        CREATION(new Color(204, 192, 218)),
        PREPARATION(new Color(216, 228, 188)),
        CBC(new Color(226, 239, 218)),
        EXPLOITATION(new Color(183, 222, 232)),
        TERMINATE(new Color(252, 213, 180)),
        CHANGE_CONDITIONS(new Color(184, 204, 228)),
        DEFAULT(new Color(255, 255, 255));

        private Color color;

        SectionColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
