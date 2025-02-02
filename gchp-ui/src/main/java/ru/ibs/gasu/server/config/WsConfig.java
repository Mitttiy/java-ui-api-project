package ru.ibs.gasu.server.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ibs.gasu.common.soap.generated.fileapi.FileWebService;
import ru.ibs.gasu.common.soap.generated.gchpdicts.GchpDictionariesWebService;
import ru.ibs.gasu.common.soap.generated.gchpdocs.GchpDocumentsWebService;
import ru.ibs.gasu.server.soap.interceptors.UserContextHeaderInterceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WsConfig {

    @Value("${soapws.baseurl.gchpapi}")
    private String gchpApiBaseUrl;

    @Value("${soapws.baseurl.fileapi}")
    private String fileServiceEndpointUrl;

    @Bean
    public GchpDictionariesWebService gchpDictionariesWebService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(GchpDictionariesWebService.class);
        factoryBean.setAddress(gchpApiBaseUrl + "/soap/dictionary");
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        factoryBean.setOutInterceptors(Collections.singletonList(new UserContextHeaderInterceptor()));
        return (GchpDictionariesWebService) factoryBean.create();
    }

    @Bean
    public GchpDocumentsWebService gchpDocumentsWebService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(GchpDocumentsWebService.class);
        factoryBean.setAddress(gchpApiBaseUrl + "/soap/documents");
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        factoryBean.setOutInterceptors(Collections.singletonList(new UserContextHeaderInterceptor()));
        return (GchpDocumentsWebService) factoryBean.create();
    }

    @Bean
    public FileWebService fileWebService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(FileWebService.class);
        factoryBean.setAddress(fileServiceEndpointUrl + "/soap/file");
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        return (FileWebService) factoryBean.create();
    }


    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        SpringBus springBus = new SpringBus();
        springBus.getInInterceptors().add(loggingInInterceptor());
        springBus.getInFaultInterceptors().add(loggingInInterceptor());
        springBus.getOutInterceptors().add(loggingOutInterceptor());
        springBus.getOutFaultInterceptors().add(loggingOutInterceptor());
        springBus.getFeatures().add(loggingFeature());
        return springBus;
    }

    private Integer limit = 204800;
    private boolean prettyLogging = true;

    @Bean
    public LoggingInInterceptor loggingInInterceptor() {
        LoggingInInterceptor interceptor = new LoggingInInterceptor();
        interceptor.setPrettyLogging(prettyLogging);
        interceptor.setLimit(limit);
        return interceptor;
    }

    @Bean
    public LoggingOutInterceptor loggingOutInterceptor() {
        LoggingOutInterceptor interceptor = new LoggingOutInterceptor();
        interceptor.setPrettyLogging(prettyLogging);
        interceptor.setLimit(limit);
        return interceptor;
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(prettyLogging);
        return loggingFeature;
    }
}
