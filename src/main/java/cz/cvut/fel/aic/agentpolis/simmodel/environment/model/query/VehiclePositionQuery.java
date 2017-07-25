package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.query;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;

import javax.inject.Singleton;

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
