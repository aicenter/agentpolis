package cz.cvut.fel.aic.agentpolis.simmodel.environment;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

/**
 * Serves for saving flag, which represents day when vehicle departures from
 * some specific place.
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleTimeModel {

    private final Map<String, Long> vehicleDepartureDayFlag = new HashMap<String, Long>();

    public void addVehicleDepartureDayFlag(String vehicleId, long dayFlag) {
        vehicleDepartureDayFlag.put(vehicleId, dayFlag);
    }

    public void removeVehicleDepartureDayFlag(String vehicleId) {
        vehicleDepartureDayFlag.remove(vehicleId);
    }

    public Long getVehicleDepartureDayFlag(String vehicleId) {
        return vehicleDepartureDayFlag.get(vehicleId);
    }

}
