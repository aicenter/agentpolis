package cz.agents.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.PassengerActivityLogger;
import cz.agents.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerAction;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerTripAction;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;

/**
 * Represents passenger activity - from waiting for vehicle, getting in vehicle,
 * traveling by vehicle get of vehicle
 * 
 * @author Zbynek Moler
 * 
 * @param <TTrip>
 */
public class RideAsPassengerActivity<TTrip extends GraphTrip<TripItem>> implements
        VehicleArrivedCallback, LinkedEntitySensor {

    private static final Logger LOGGER = Logger.getLogger(RideAsPassengerActivity.class);

    private final PassengerAction useVehicleAction;
    private final PassengerTripAction passengerTripAction;
    private WaitingForVehicle waitingForVehicle;

    // ---------- LOG ---------------------
    private final PassengerActivityLogger passengerActivityLogger;
    // ---------- LOG ---------------------

    private String passengerId;
    private TTrip trip;

    @Inject
    public RideAsPassengerActivity(PassengerAction useVehicleAction, PassengerTripAction passengerTripAction, 
            PassengerActivityLogger passengerActivityLogger) {
        super();

        this.useVehicleAction = useVehicleAction;
        this.passengerTripAction = passengerTripAction;

        // ---------- LOG ---------------------
        this.passengerActivityLogger = passengerActivityLogger;
        // ---------- LOG ---------------------

    }

    /**
     * Waits for the arrival of a particular vehicle and uses the vehicle as
     * passenger
     * 
     * @param vehicleId
     */
    public void usingVehicleAsPassenger(final String agentId, final String vehicleId, TTrip trip,
            PassengerActivityCallback<TTrip> passengerActivityCallback) {

        setUpPassenger(agentId, trip, passengerActivityCallback);

        // --- LOG ----
        passengerActivityLogger.logStartWaitingOnPosition(agentId, trip);
        // --- LOG ----

        waitingForVehicle = new WaitingForVehicle() {

            @Override
            public void waitingForVehicle(VehicleArrivedCallback passengerVehiclePlanCallback) {
                useVehicleAction.waitToVehicle(agentId, vehicleId, passengerVehiclePlanCallback);

            }
        };

        waitingForVehicle.waitingForVehicle(this);

    }

    /**
     * 
     * Waits for the arrival of a vehicle from a group and uses the vehicle as
     * passenger
     * 
     * Travel with first arrived vehicle from group - waiting for the vehicle
     * and travel
     * 
     */
    public void usingVehicleFromGroupAsPassenger(final String agentId, final String groupId,
            TTrip trip, PassengerActivityCallback<TTrip> passengerActivityCallback) {

        // ---------- LOG ---------------------
        passengerActivityLogger.logStartWaitingOnPosition(agentId, trip);
        // ---------- LOG ---------------------

        setUpPassenger(agentId, trip, passengerActivityCallback);

        waitingForVehicle = new WaitingForVehicle() {

            @Override
            public void waitingForVehicle(VehicleArrivedCallback passengerVehiclePlanCallback) {
                useVehicleAction.waitToVehicleFromGroup(agentId, groupId,
                        passengerVehiclePlanCallback);

            }
        };

        waitingForVehicle.waitingForVehicle(this);
    }

    private void setUpPassenger(String passengerId, TTrip trip,
            PassengerActivityCallback<TTrip> passengerActivityCallback) {
        this.passengerId = passengerId;
        this.trip = trip;
        passengerTripAction.addPassengerTripCallback(passengerId, passengerActivityCallback);
    }

    /**
     * Getting in vehicle if it is possible - vehicle can be full
     * 
     * @param vehicleId
     * @return
     */
    private boolean getInVehicleIfCan(String vehicleId) {
        if (useVehicleAction.getInVehicle(passengerId, vehicleId, this, this)) {
            return true;
        }

        // ---------- LOG ---------------------
        passengerActivityLogger.logMissVehicle(passengerId, trip.clone());
        // ---------- LOG ---------------------
        passengerTripAction.tripFail(passengerId, trip);
        return false;

    }

    /**
     * Method is usually invoked, if vehicle finished its trip, but passenger
     * want to next.
     */
    @Override
    public void entityWasUnlinked(String vehicleId) {

        useVehicleAction.getOffVehicle(passengerId);

        // TODO add check if trip last it == current position
        if (trip.numOfCurrentTripItems() > 1) {

            // ---------- LOG ---------------------
            passengerActivityLogger.logPassengerMoveAcrosseNode(passengerId, trip.clone()
                    .getAndRemoveFirstTripItem());
            passengerActivityLogger.logPassengerGotOffToVehiclePartTrip(passengerId, trip.clone());
            // ---------- LOG ---------------------

            passengerTripAction.donePartTrip(passengerId, trip);
        } else {

            // ---------- LOG ---------------------
            passengerActivityLogger.logPassengerMoveAcrosseNode(passengerId, trip.clone()
                    .getAndRemoveFirstTripItem());
            passengerActivityLogger.logPassengerGotOffToVehicleDoneFullTrip(passengerId,
                    trip.clone(), trip.clone().getAndRemoveFirstTripItem());
            // ---------- LOG ---------------------

            trip.removeFirstTripItem();
            passengerTripAction.doneFullTrip(passengerId);
        }

    }

    /**
     * Through this method is passenger notify about vehicle next destination
     * position. Passenger make decision about next using vehicle based on that
     * information.
     * 
     * 
     */
    @Override
    public void notifyPassengerAboutVehiclePlan(int fromNodeId, int toNodeId, String vehicleId) {

        TripItem fromtripItem = new TripItem(fromNodeId);
        TripItem totripItem = new TripItem(toNodeId);

        if (trip.isEqualWithFirstTripItem(fromtripItem) == false) {
            throw new RuntimeException("Is not possible");
        }

        TripItem lastPositionByNodeId = trip.getAndRemoveFirstTripItem();

        // Does full plan trip
        if (trip.hasNextTripItem() == false) {

            // ---------- LOG ---------------------
            passengerActivityLogger.logPassengerMoveAcrosseNode(passengerId, fromNodeId);
            passengerActivityLogger.logPassengerGotOffToVehicleDoneFullTrip(passengerId,
                    trip.clone(), lastPositionByNodeId);
            // ---------- LOG ---------------------

            useVehicleAction.getOffVehicleAndUnLink(passengerId, vehicleId);
            passengerTripAction.doneFullTrip(passengerId);
            return;
        }

        // Vehicle has different direction
        if (trip.isEqualWithFirstTripItem(totripItem) == false) {
            trip.addTripItemBeforeCurrentFirst(lastPositionByNodeId);

            // ---------- LOG ---------------------
            passengerActivityLogger.logPassengerMoveAcrosseNode(passengerId, fromNodeId);
            passengerActivityLogger.logPassengerGotOffToVehiclePartTrip(passengerId, trip.clone());
            // ---------- LOG ---------------------

            useVehicleAction.getOffVehicleAndUnLink(passengerId, vehicleId);
            passengerTripAction.donePartTrip(passengerId, trip);
            return;
        }

        // ---------- LOG ---------------------
        passengerActivityLogger.logPassengerMoveAcrosseNode(passengerId, fromNodeId);
        // ---------- LOG ---------------------

    }

    /**
     * Passenger is inform about arrived vehicle and about next vehicle
     * movement.
     */
    @Override
    public void notifyWaitingPassengerAboutVehiclePlan(int fromNodeId, int toNodeId,
                                                       String vehicleId) {

        TripItem fromtripItem = new TripItem(fromNodeId);
        TripItem totripItem = new TripItem(toNodeId);

        if (trip.isEqualWithFirstTripItem(fromtripItem) == false
                || trip.numOfCurrentTripItems() < 2) {
            LOGGER.warn("The agent with id" + passengerId
                    + " is terminted. It is not allowed state for"
                    + fromtripItem.tripPositionByNodeId + "and " + trip);
            return;
            // throw new RuntimeException("It is not possible");
        }

        TripItem lastPositionByNodeId = trip.getAndRemoveFirstTripItem();

        // Try if vehicle go to same direction, where passenger needs
        if (trip.isEqualWithFirstTripItem(totripItem)) {
            if (getInVehicleIfCan(vehicleId) == false) {
                trip.addTripItemBeforeCurrentFirst(lastPositionByNodeId);

                // ---------- LOG ---------------------
                passengerActivityLogger.logOvercrowdedVehicle(vehicleId, trip);
                // ---------- LOG ---------------------

                return;
            }

            // --- Log ----
            passengerActivityLogger.logEndWaitingOnPosition(passengerId, trip);
            // --- Log ----

            // ---------- LOG ---------------------
            passengerActivityLogger.logPassengerGotInToVehicle(passengerId, trip.clone(),
                    lastPositionByNodeId);
            // ---------- LOG ---------------------
        } else {
            // Passenger needs to go different direction
            trip.addTripItemBeforeCurrentFirst(lastPositionByNodeId);
            waitingForVehicle.waitingForVehicle(this);
        }

    }

    private interface WaitingForVehicle {

        public void waitingForVehicle(VehicleArrivedCallback passengerVehiclePlanCallback);
    }

}
