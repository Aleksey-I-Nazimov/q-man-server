package org.numamo.qman.services.api.manual;

import java.util.Map;


public interface HeadersFilter {

    Map<String,String> makeForKafka(Map<String,String> requestHeaders);

    Map<String,String> makeForIbm(Map<String,String> requestHeaders);

}
