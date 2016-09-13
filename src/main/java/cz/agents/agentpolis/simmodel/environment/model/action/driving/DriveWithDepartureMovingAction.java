package cz.agents.agentpolis.simmodel.environment.model.action.driving;

import cz.agents.agentpolis.siminfrastructure.planner.trip.DepartureTripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.DepartureTripItemVisitor;
import cz.agents.agentpolis.siminfrastructure.planner.trip.NotWaitingDepartureTripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.WaitingDepartureTripItem;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehicleBeforePlanNotifyAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehiclePlanNotifyAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.WaitingVehicleAction;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.publictransport.TimetableItem;

/**
 * This class is implementation of @ MovingAction} interface. It provides logic
 * for driving (moving) vehicle with using departure time.
 * 
 * @author Zbynek Moler
 * 
 */
public class DriveWithDepartureMovingAction implements MovingAction<DepartureTripItem> {

    private final MoveVehicleAction drivenAction;
    private final VehiclePlanNotifyAction vehiclePlanNotifyAction;
    private final WaitingVehicleAction waitingVehicleAction;

    private final VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction;

    private final long moveTime;
    private final String vehicleId;
    private final double vehicleLength;

    public DriveWithDepartureMovingAction(MoveVehicleAction drivenAction,
            final VehiclePlanNotifyAction vehiclePlanNotifyAction, long moveTime,
            WaitingVehicleAction waitingVehicleAction, final String vehicleId,
            final double vehicleLength,
            final VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction) {
        super();
        this.drivenAction = drivenAction;
        this.moveTime = moveTime;
        this.vehiclePlanNotifyAction = vehiclePlanNotifyAction;
        this.waitingVehicleAction = waitingVehicleAction;

        this.vehicleId = vehicleId;
        this.vehicleLength = vehicleLength;

        this.vehicleBeforePlanNotifyAction = vehicleBeforePlanNotifyAction;

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
    public void waitToDepartureTime(DepartureTripItem fromTripItem, DepartureTripItem toTripItem,
            MovingActionCallback movingActionCallback) {

        fromTripItem.visitDepartureTripItem(new WaitToDepartureTime(
                toTripItem.tripPositionByNodeId, waitingVehicleAction, movingActionCallback));

    }

    private class WaitToDepartureTime implements DepartureTripItemVisitor {

        private final int toNodeId;
        private final WaitingVehicleAction waitingVehicleAction;
        private final MovingActionCallback movingActionCallback;

        public WaitToDepartureTime(int toNodeId, WaitingVehicleAction waitingVehicleAction,
                                   MovingActionCallback movingActionCallback) {
            super();
            this.toNodeId = toNodeId;
            this.waitingVehicleAction = waitingVehicleAction;
            this.movingActionCallback = movingActionCallback;
        }

        @Override
        public void visitDepartureTripItem(NotWaitingDepartureTripItem notWaitingDepartureTripItem) {
            movingActionCallback.endWaitingToDepartureTime(
                    notWaitingDepartureTripItem.tripPositionByNodeId, toNodeId);
        }

        @Override
        public void visitDepartureTripItem(WaitingDepartureTripItem waitingDepartureTripItem) {
            TimetableItem timeTableItem = new TimetableItem(waitingDepartureTripItem.departureTime,
                    waitingDepartureTripItem.overMidnight);
            waitingVehicleAction.waitToDepartureTime(vehicleId,
                    waitingDepartureTripItem.tripPositionByNodeId, toNodeId, timeTableItem,
                    movingActionCallback);

        }

    }

}
