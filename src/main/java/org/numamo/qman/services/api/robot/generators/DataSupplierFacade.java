package org.numamo.qman.services.api.robot.generators;

import org.numamo.qman.web.dto.data.BodySupplierDto;
import org.numamo.qman.web.dto.data.HeaderSupplierDto;


public interface DataSupplierFacade {

    Object makeBy (BodySupplierDto bodySupplier);

    Object makeBy (HeaderSupplierDto headerSupplier);

}
