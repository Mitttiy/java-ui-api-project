package ru.ibs.gasu.configuation;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ibs.gasu.gchp.ws.*;
import ru.ibs.gasu.soap.generated.dictionary.IDictionaryDataWebService;
import ru.ibs.gasu.soap.generated.dictionary.IDictionaryMetadataWebService;
import ru.ibs.gasu.soap.generated.user.PublicUserWebService;
import ru.ibs.gasu.soap.generated.userinterface.PublicUserInterfaceWebService;

import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WsConfig {

    @Value("${soapws.endpoint.user}")
    private String userEndpoint;

    @Value("${soapws.endpoint.user-interface}")
    private String userInterfaceEndpoint;

    @Value("${soapws.endpoint.dict-data}")
    private String dictionaryDataEndpoint;

    @Value("${soapws.endpoint.dict-meta}")
    private String dictionaryMetadataEndpoint;

    private Integer limit = 204800;
    private boolean prettyLogging = false;

    @Bean
    public GchpDictionariesWebService gchpDictionariesWs() {
        return new GchpDictionariesWebServiceImpl();
    }

    @Bean
    public GerbWebService gerbWebService() {
        return new GerbWebServiceImpl();
    }


    @Bean
    public Endpoint gerbWsEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), gerbWebService());
        endpoint.publish("/gerb");
        return endpoint;
    }

    @Bean
    public Endpoint gchpDictionariesWsEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), gchpDictionariesWs());
        endpoint.publish("/dictionary");
        return endpoint;
    }

    @Bean
    public GchpDocumentsWebService gchpDocumentsWs() {
        return new GchpDocumentsWebServiceImpl();
    }

    @Bean
    public Endpoint faqWsEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), gchpDocumentsWs());
        endpoint.publish("/documents");
        return endpoint;
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        SpringBus springBus = new SpringBus();
        springBus.getFeatures().add(loggingFeature());
        return springBus;
    }

    @Bean
    public PublicUserWebService userWebService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(PublicUserWebService.class);
        factoryBean.setAddress(userEndpoint);
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        return (PublicUserWebService) factoryBean.create();
    }

    @Bean
    public PublicUserInterfaceWebService userInterfaceWebService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(PublicUserInterfaceWebService.class);
        factoryBean.setAddress(userInterfaceEndpoint);
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        return (PublicUserInterfaceWebService) factoryBean.create();
    }

    @Bean
    public IDictionaryDataWebService getDictionaryDataService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(IDictionaryDataWebService.class);
        factoryBean.setAddress(dictionaryDataEndpoint);
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        return (IDictionaryDataWebService) factoryBean.create();
    }

    @Bean
    public IDictionaryMetadataWebService getDictionaryMetadataService() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(IDictionaryMetadataWebService.class);
        factoryBean.setAddress(dictionaryMetadataEndpoint);
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("set-jaxb-validation-event-handler", false);
        propMap.put("schema-validation-enabled", false);
        propMap.put("mtom-enabled", false);
        factoryBean.setProperties(propMap);
        return (IDictionaryMetadataWebService) factoryBean.create();
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(prettyLogging);
        loggingFeature.setLimit(limit);
        return loggingFeature;
    }
}
