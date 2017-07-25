package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.passenger.WaitForVehicleAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.query.AgentPositionQuery;

/**
 * Action implements methods for passenger, which wants to using vehicle. E.g.
 * waiting registration, get in vehicle atd.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class PassengerAction {

    private final GetInVehicleAction getInVehicleAction;
    private final GetOffVehicleAction getOffVehicleAction;
    private final AgentPositionQuery positionSensorAgent;
    private final WaitForVehicleAction passengerWaitingAction;

    @Inject
    public PassengerAction(GetInVehicleAction getInVehicleAction,
            GetOffVehicleAction getOffVehicleAction, AgentPositionQuery positionSensorAgent,
            WaitForVehicleAction passengerWaitingAction) {
        super();
        this.getInVehicleAction = getInVehicleAction;
        this.getOffVehicleAction = getOffVehicleAction;
        this.positionSensorAgent = positionSensorAgent;
        this.passengerWaitingAction = passengerWaitingAction;

    }

    /**
     * Waiting for specific vehicle on current place
     * 
     * @param vehicleId
     * @param vehiclePlanCallback
     */
    public void waitToVehicle(String agentId, String vehicleId,
            VehicleArrivedCallback vehiclePlanCallback) {
        long agentPosition = positionSensorAgent.getCurrentPositionByNodeId(agentId);
        passengerWaitingAction
                .waitToVehicle(agentId, vehicleId, agentPosition, vehiclePlanCallback);

    }

    /**
     * Waiting for first arrived vehicle from some group of vehicles (e.g. tram
     * line)
     * 
     * @param groupId
     * @param vehiclePlanCallback
     */
    public void waitToVehicleFromGroup(String agentId, String groupId,
            VehicleArrivedCallback vehiclePlanCallback) {
        long agentPosition = positionSensorAgent.getCurrentPositionByNodeId(agentId);
        passengerWaitingAction.waitToVehicleFromGroup(agentId, groupId, agentPosition,
                vehiclePlanCallback);

    }

    /**
     * Through this method - get in vehicle
     * 
     * 
     */
    public boolean getInVehicle(final String agentId, String vehicleId,
            VehicleArrivedCallback vehiclePlanCallback,
            LinkedEntitySensor linkedEntityCallback) {

        return getInVehicleAction.getInVehicle(agentId, vehicleId, vehiclePlanCallback,
                linkedEntityCallback);

    }

    /**
     * Passenger can get off vehicle
     * 
     * @param vehicleId
     */
    public void getOffVehicleAndUnLink(final String agentId, final String vehicleId) {
        getOffVehicleAction.getOffVehicleAndUnLink(agentId, vehicleId);
    }

    /**
     * Through this method agent can get off a vehicle.
     */
    public void getOffVehicle(final String agentId) {
        getOffVehicleAction.getOffVehicle(agentId);
    }
}
