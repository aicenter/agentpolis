package cz.agents.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;

import cz.agents.agentpolis.siminfrastructure.description.Description;
import cz.agents.agentpolis.siminfrastructure.description.DescriptionImpl;
import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.VehicleDrivingActivityLogger;
import cz.agents.agentpolis.siminfrastructure.planner.trip.DepartureTripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.DrivingFinishedActivityCallback;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.MovementActivityCallback;
import cz.agents.agentpolis.simmodel.agent.activity.movement.util.MoveTimeNormalizer;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DriveMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DriveWithDepartureMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.MoveVehicleAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehicleBeforePlanNotifyAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehiclePlanNotifyAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.WaitingVehicleAction;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.query.VehicleInfoQuery;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.action.LinkEntityAction;

/**
 * Represents agent driving activity. Through this activity agent can drive a
 * specific vehicle. Agent can drive vehicle based on time table or classical
 * 
 * @author Zbynek Moler
 * 
 */
public class DriveVehicleActivity implements MovementActivityCallback, Description {

    private String driverId;

    private final MoveVehicleAction driveAction;
    private final VehiclePlanNotifyAction vehiclePlanNotifyAction;
    private final WaitingVehicleAction waitingVehicleAction;
    private final VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction;
    private final LinkEntityAction linkEntityAction;
    private final VehicleInfoQuery vehicleSensor;
    private final VehicleDrivingActivityLogger vehicleDrivingActivityLogger;

    private final MovementActivityDepartureTripItem departureMovementActivity;
    private final MovementActivityTripItem movementActivity;

    private PhysicalVehicle vehicle;
    private DrivingFinishedActivityCallback drivingActivityCallback;

    @Inject
    public DriveVehicleActivity(MoveVehicleAction driveAction,
            VehiclePlanNotifyAction vehiclePlanNotifyAction,
            WaitingVehicleAction waitingVehicleAction,
            VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction,
            LinkEntityAction linkEntityAction, VehicleInfoQuery vehicleSensor,
            VehicleDrivingActivityLogger vehicleDrivingActivityLogger,
            MovementActivityDepartureTripItem departureMovementActivity,
            MovementActivityTripItem movementActivity) {
        super();

        this.driveAction = driveAction;
        this.vehiclePlanNotifyAction = vehiclePlanNotifyAction;
        this.waitingVehicleAction = waitingVehicleAction;
        this.vehicleBeforePlanNotifyAction = vehicleBeforePlanNotifyAction;
        this.linkEntityAction = linkEntityAction;
        this.vehicleSensor = vehicleSensor;
        this.vehicleDrivingActivityLogger = vehicleDrivingActivityLogger;
        this.departureMovementActivity = departureMovementActivity;
        this.movementActivity = movementActivity;
    }

    /**
     * Driving vehicle base on some time table - drive waiting with vehicle to
     * departure time if arrived early
     * 
     */
    public void driveBaseOnDepartureTime(String agentId, PhysicalVehicle vehicle,
            GraphTrip<DepartureTripItem> trip, DrivingFinishedActivityCallback drivingActivityCallback) {

        drive(agentId,
                vehicle,
                trip,
                new DriveWithDepartureMovingAction(driveAction, vehiclePlanNotifyAction,
                        computeMoveTime(vehicle), waitingVehicleAction, vehicle.getId(), vehicle
                                .getLength(), vehicleBeforePlanNotifyAction),
                drivingActivityCallback, departureMovementActivity);

    }

    /**
     * Driving vehicle - agent do not wait on some position (station) if arrive
     * early
     * 
     * @param trip
     */
    public void drive(String agentId, PhysicalVehicle vehicle, GraphTrip<TripItem> trip,
            DrivingFinishedActivityCallback drivingActivityCallback) {

        drive(agentId, vehicle, trip, new DriveMovingAction(driveAction, vehiclePlanNotifyAction,
                computeMoveTime(vehicle), vehicle.getId(), vehicle.getLength(),
                vehicleBeforePlanNotifyAction), drivingActivityCallback, movementActivity);
    }

    /**
     * Computes time based on vehicle length and velocity
     * 
     * @return
     */
    private long computeMoveTime(PhysicalVehicle vehicle) {
        double vehicleVelocityInMeterPerMillis = vehicleSensor.getCurrrentVehicleVelocity(vehicle
                .getId()) / 1000;
        long moveTime = (long) (vehicle.getLength() / vehicleVelocityInMeterPerMillis);

        return MoveTimeNormalizer.normalizeMoveTimeForQueue(moveTime);
    }

    private <TTripItem extends TripItem> void drive(String driverId, PhysicalVehicle vehicle,
            GraphTrip<TTripItem> trip, MovingAction<TTripItem> movingAction,
            DrivingFinishedActivityCallback drivingActivityCallback,
            MovementActivity<TTripItem> movementActivity) {

        assert this.vehicle == null && this.driverId == null
                && this.drivingActivityCallback == null : "VehicleDrivingActivity is currently in progress by agent with id: "
                + driverId;

        this.vehicle = vehicle;
        this.driverId = driverId;
        this.drivingActivityCallback = drivingActivityCallback;

        linkEntityAction.linkEnities(vehicle.getId(), driverId);

        driveAction.addDriverEmptyNotify(driverId);

        // --- log -----
        vehicleDrivingActivityLogger.logStartDriving(driverId, vehicle.getId());
        // --- log -----

        movementActivity.move(driverId, this, movingAction, trip);

    }

    /**
     * Finished driving a vehicle - unlinked all passengers and driver
     */
    public void finishedMovement() {
        driveAction.removeDriverNotify(driverId);
        driveAction.finishedDriving(vehicle.getId(), drivingActivityCallback);

        // --- log -----
        vehicleDrivingActivityLogger.logEndDriving(driverId, vehicle.getId());
        // --- log -----

        this.driverId = null;
        this.vehicle = null;
        this.drivingActivityCallback = null;
    }

    @Override
    public DescriptionImpl getDescription() {
        DescriptionImpl description = new DescriptionImpl();
        description.addDescriptionLine("Driver with id - ", driverId, "  is driving vehicle");
        description.addDescriptionLine("Driven vehicle has id - ", vehicle.getId());
        return description;
    }

}
