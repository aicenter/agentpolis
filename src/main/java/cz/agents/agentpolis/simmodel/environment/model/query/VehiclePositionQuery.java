package cz.agents.agentpolis.simmodel.environment.model.query;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TransportNetworks;

/**
 * Vehicle position sensor - vehicle - vehicle position storage
 * 
 * @author Zbynek Moler
 *
 */
@Singleton
public class VehiclePositionQuery extends APositionQuery {

    @Inject
    public VehiclePositionQuery(VehiclePositionModel vehiclePositionModel,
            TransportNetworks transportNetworks) {
        super(vehiclePositionModel, transportNetworks);
    }

}
