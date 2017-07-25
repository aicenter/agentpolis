package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.driving;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.vehicle.VehicleBeforePlanNotifyAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.vehicle.VehiclePlanNotifyAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * This class is implementation of {@code MoveAction} interface. It provides
 * logic for driving (moving) vehicle.
 * 
 * @author Zbynek Moler
 * 
 */
public class DriveMovingAction implements MovingAction<TripItem> {

    private final MoveVehicleAction drivenAction;
    private final VehiclePlanNotifyAction vehiclePlanNotifyAction;
    private final VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction;

    private final long moveTime;
    private final String vehicleId;
    private final double vehicleLength;

    public DriveMovingAction(final MoveVehicleAction drivenAction,
            final VehiclePlanNotifyAction vehiclePlanNotifyAction, final long moveTime,
            final String vehicleId, final double vehicleLength,
            final VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction) {
        this.drivenAction = drivenAction;
        this.vehiclePlanNotifyAction = vehiclePlanNotifyAction;
        this.vehicleBeforePlanNotifyAction = vehicleBeforePlanNotifyAction;

        this.moveTime = moveTime;
        this.vehicleId = vehicleId;
        this.vehicleLength = vehicleLength;
    }

    @Override
    public void moveToNextNode(int startNode, int destinationByNodeId,
                               GraphType typeOfGraphForMoving) {
        drivenAction.driven(vehicleId, startNode, destinationByNodeId, typeOfGraphForMoving);

    }

    @Override
    public long moveTime() {
        return moveTime;
    }

    @Override
    public void notifyPlanForNextMove(int startNode, int destinationByNodeId,
                                      MovingActionCallback movingActionCallback) {

        vehiclePlanNotifyAction.notifyPassengerAndWatingPassenger(vehicleId, startNode,
                destinationByNodeId, movingActionCallback);

    }

    @Override
    public String movingEntityId() {
        return vehicleId;
    }

    @Override
    public double takenCapacity() {
        return vehicleLength;
    }

    @Override
    public void beforeNotifyingAboutPlan(MovingActionCallback movingActionCallback,
            long fromPositionByNodeId, long toPositionByNodeId) {

        vehicleBeforePlanNotifyAction.callBeforePlanNotify(vehicleId, movingActionCallback,
                fromPositionByNodeId, toPositionByNodeId);

    }

    @Override
    public void waitToDepartureTime(TripItem fromTripItem, TripItem toTripItem,
            MovingActionCallback movingActionCallback) {
        movingActionCallback.endWaitingToDepartureTime(fromTripItem.tripPositionByNodeId,
                toTripItem.tripPositionByNodeId);

    }

}
