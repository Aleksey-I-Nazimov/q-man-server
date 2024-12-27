package org.numamo.qman.services.manual;

import org.numamo.qman.services.api.manual.HeadersFilter;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;


/**
 *  The default implementation of the header filters interface service.
 *
 * @author Nazimov Aleksey I.
 */
@Service
public final class HeadersFilterImpl implements HeadersFilter {

    private static final Logger LOGGER = getLogger(HeadersFilterImpl.class);

    private static final String KAFKA_PREFIX = "QMAN_KAFKA_";
    private static final String IBM_MQ_PREFIX = "QMAN_IBM_MQ_";


    @Override
    public Map<String, String> makeForKafka(
            final Map<String, String> requestHeaders
    ) {
        LOGGER.debug("Filtering headers for kafka: {}", requestHeaders);
        final Map<String,String> newHeaders = filter(KAFKA_PREFIX,requestHeaders);
        LOGGER.debug("Filtered headers for kafka: {}",newHeaders);
        return newHeaders;
    }

    @Override
    public Map<String, String> makeForIbm(
            final Map<String, String> requestHeaders
    ) {
        LOGGER.debug("Filtering headers for ibm: {}", requestHeaders);
        final Map<String,String> newHeaders = filter(IBM_MQ_PREFIX,requestHeaders);
        LOGGER.debug("Filtered headers for ibm: {}",newHeaders);
        return newHeaders;
    }

    private Map<String, String> filter(
            final String prefix,
            final Map<String, String> headers
    ) {
        final Map<String, String> newHeaders = new HashMap<>();
        final Map<String, String> originalHeaders = isNull(headers) ? emptyMap() : headers;

        for (final Map.Entry<String, String> entry : originalHeaders.entrySet()) {
            final String key = entry.getKey();
            if (nonNull(key)
                    && key.toLowerCase().startsWith(prefix.toLowerCase())
                    && nonNull(originalHeaders.get(key))
            ) {
                final String newKey = key.toLowerCase().replace(prefix.toLowerCase(), "");
                newHeaders.put(newKey, originalHeaders.get(key));
            }
        }

        return newHeaders;
    }

}
