package org.numamo.qman.services;

import org.numamo.qman.services.api.BasicService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class BasicServiceImpl implements BasicService {

    private final Logger LOGGER = getLogger(BasicServiceImpl.class);

    @Override
    public String makeId() {
        final String id = UUID.randomUUID().toString();
        LOGGER.trace("Requested ID={}",id);
        return id;
    }

}
