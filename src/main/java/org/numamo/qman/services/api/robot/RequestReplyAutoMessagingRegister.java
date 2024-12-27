package org.numamo.qman.services.api.robot;

import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;
import org.numamo.qman.web.dto.jms.JmsRqRsMultiConfigDto;

import java.util.Map;

public interface RequestReplyAutoMessagingRegister {

    String deployConfigsForIbmMq(JmsRqRsConfigDto config);

    Map<String,JmsRqRsConfigDto> deployMultiConfigsForIbmMq(JmsRqRsMultiConfigDto multiConfig);

    Map<String,JmsRqRsConfigDto> deployedConfigs();

}
