package org.numamo.qman.configuration.jms;


import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
import com.ibm.mq.spring.boot.MQConnectionFactoryFactory;
import jakarta.jms.JMSException;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;

import static java.lang.Integer.parseInt;
import static org.slf4j.LoggerFactory.getLogger;

/*
 * https://github.com/ibm-messaging/mq-jms-spring/blob/master/mq-jms-spring-boot-starter/src/main/java/com/ibm/mq/spring/boot/MQConnectionFactoryConfiguration.java
 * https://spring.io/guides/gs/messaging-jms
 * https://developer.ibm.com/tutorials/mq-jms-application-development-with-spring-boot/
 * https://levioconsulting.com/insights/a-simple-guide-to-using-ibm-mq-with-java-messaging-service/
 */

@Configuration
@Import({MQConfigurationProperties.class,})
@EnableConfigurationProperties
public class IbmMqConfiguration {

    private static final Logger LOGGER = getLogger(IbmMqConfiguration.class);

    public static final String IBM_CONNECTION_FACTORY = "IBM_CONNECTION_FACTORY";
    public static final String IBM_CACHING_CONNECTION_FACTORY = "IBM_CACHING_CONNECTION_FACTORY";
    public static final String IBM_JMS_LISTENER_CONTAINER_FACTORY = "IBM_JMS_LISTENER_CONTAINER_FACTORY";
    public static final String IBM_JMS_TEMPLATE = "IBM_JMS_TEMPLATE";
    public static final String IBM_JMS_LISTENER_ENDPOINT_REGISTRY = "IBM_JMS_LISTENER_ENDPOINT_REGISTRY";
    public static final String IBM_JMS_LISTENER_ENDPOINT_REGISTRAR = "IBM_JMS_LISTENER_ENDPOINT_REGISTRAR";


    @Bean(IBM_CONNECTION_FACTORY)
    public jakarta.jms.ConnectionFactory createConnectionFactory(
            MQConfigurationProperties properties,
            ObjectProvider<SslBundles> sslBundles,
            ObjectProvider<List<MQConnectionFactoryCustomizer>> factoryCustomizers
    ) throws JMSException {
        /*
        The default IBM starter reads the respective configs from yaml-file and creates
        properties. The default starter creates the specific IBM connection factory we just
        auto wire that and cast to the standard interface
         */
        properties.traceProperties();
        LOGGER.debug("IBM-MQ: {}",properties);
        return new MQConnectionFactoryFactory(properties, sslBundles.getIfAvailable(),
                factoryCustomizers.getIfAvailable()).createConnectionFactory(MQConnectionFactory.class);
    }


    @Bean(IBM_CACHING_CONNECTION_FACTORY)
    public CachingConnectionFactory cachingConnectionFactory(
            @Value("${spring.application.jms-session-cache-size: 10}")
            String jmsSessionCacheSize,
            @Qualifier(IBM_CONNECTION_FACTORY)
            jakarta.jms.ConnectionFactory jmsConnectionFactory
    ) {
        LOGGER.debug("Setting jms session cache size {}",jmsSessionCacheSize);
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setSessionCacheSize( parseInt(jmsSessionCacheSize) );
        factory.setTargetConnectionFactory( jmsConnectionFactory );
        factory.setReconnectOnException( true );
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean(IBM_JMS_LISTENER_CONTAINER_FACTORY)
    public DefaultJmsListenerContainerFactory createDefaultJmsListenerContainerFactory(
            @Qualifier(IBM_CACHING_CONNECTION_FACTORY)
            CachingConnectionFactory cachingConnectionFactory
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory( cachingConnectionFactory );
        return factory;
    }

    @Bean(IBM_JMS_TEMPLATE)
    public JmsTemplate jmsTemplate(
            @Qualifier(IBM_CACHING_CONNECTION_FACTORY)
            CachingConnectionFactory cachingConnectionFactory
    ) {
        final JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory( cachingConnectionFactory );
        return jmsTemplate;
    }

    @Bean(IBM_JMS_LISTENER_ENDPOINT_REGISTRY)
    public JmsListenerEndpointRegistry createRegistry(){
        return new JmsListenerEndpointRegistry();
    }


    @Bean(IBM_JMS_LISTENER_ENDPOINT_REGISTRAR)
    public JmsListenerEndpointRegistrar createRegistrar(
            @Qualifier(IBM_JMS_LISTENER_ENDPOINT_REGISTRY)
            JmsListenerEndpointRegistry registry,
            @Qualifier(IBM_JMS_LISTENER_CONTAINER_FACTORY)
            DefaultJmsListenerContainerFactory containerFactory
    ) {
        final JmsListenerEndpointRegistrar registrar = new JmsListenerEndpointRegistrar();
        registrar.setEndpointRegistry( registry );
        registrar.setContainerFactory( containerFactory );
        return registrar;
    }

}
