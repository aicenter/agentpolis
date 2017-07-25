package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * Vehicle position action - vehicle from agent position environment
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehiclePositionAction extends APositionAction {

    @Inject
    public VehiclePositionAction(EventProcessor eventProcessor,
            VehiclePositionModel entityPositionStorage) {
        super(eventProcessor, entityPositionStorage);
        // TODO Auto-generated constructor stub
    }

    
    
    

}
