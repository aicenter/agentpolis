package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.Logger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.PublishSubscribeLogger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.EndWaitingOnStationLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.OvercrowdedPTVehicleLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.StartWaitingOnStationLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerEntryToPTVehicleLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerEntryToVehicleLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerExitPTVehicleFullTripLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerExitPTVehiclePartTripLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerExitVehicleFullTripLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerExitVehiclePartTripLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerMissPTVehicleLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerMissVehicleLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerMoveAcrossNodeLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.PTTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The specific implementation of {@code Logger} relating to logging information
 * from {@code PassengerActivity}
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class PassengerActivityLogger extends Logger {

    @Inject
    public PassengerActivityLogger(PublishSubscribeLogger publishSubscribeLogger,
            EventProcessor eventProcessor) {
        super(publishSubscribeLogger, eventProcessor);

    }

    /**
     * 
     * Logs that a particular agent got in a vehicle and on a position
     * 
     * @param publisherId
     * @param tripClone
     * @param positionByNodeId
     */
    public <TTrip extends GraphTrip<TripItem>> void logPassengerGotInToVehicle(String publisherId,
            TTrip tripClone, long positionByNodeId) {

        if (tripClone instanceof PTTrip) {
            log(new PassengerEntryToPTVehicleLogItem(publisherId, getCurrentSimulationTime(),
                    positionByNodeId, ((PTTrip) tripClone).getLineId()));
        } else {
            log(new PassengerEntryToVehicleLogItem(publisherId, getCurrentSimulationTime(),
                    positionByNodeId));
        }

    }

    /**
     * 
     * Logs that a particular agent got in a vehicle and on a position
     * 
     * @param relatedEntityId
     * @param tripClone
     * @param place
     */
    public <TTrip extends GraphTrip<TripItem>> void logPassengerGotInToVehicle(String relatedEntityId,
            TTrip tripClone, TripItem place) {
        logPassengerGotInToVehicle(relatedEntityId, tripClone, place.tripPositionByNodeId);
    }

    /**
     * 
     * Logs that a particular agent missed
     * 
     * @param publisherId
     * @param tripClone
     */
    public <TTrip extends GraphTrip<TripItem>> void logMissVehicle(String publisherId, TTrip tripClone) {
        if (tripClone instanceof PTTrip) {
            log(new PassengerMissPTVehicleLogItem(publisherId, getCurrentSimulationTime(),
                    getLineId(tripClone)));
        } else {
            log(new PassengerMissVehicleLogItem(publisherId, getCurrentSimulationTime()));
        }

    }

    private <TTrip> String getLineId(TTrip tripClone) {
        return ((PTTrip) tripClone).getLineId();
    }

    /**
     * 
     * Logs that a particular agent got off a vehicle and on a position
     * 
     * @param passengerId
     * @param tripClone
     */
    public <TTrip extends GraphTrip<TripItem>> void logPassengerGotOffToVehiclePartTrip(
            String passengerId, TTrip tripClone) {
        long exitPositionByNodeId = tripClone.getAndRemoveFirstTripItem().tripPositionByNodeId;

        if (tripClone instanceof PTTrip) {
            log(new PassengerExitPTVehiclePartTripLogItem(passengerId, getCurrentSimulationTime(),
                    exitPositionByNodeId, getLineId(tripClone)));
        } else {
            log(new PassengerExitVehiclePartTripLogItem(passengerId, getCurrentSimulationTime(),
                    exitPositionByNodeId));
        }
    }

    /**
     * 
     * Logs that a particular agent moved across a position
     * 
     * @param relatedEntityId
     * @param tripItem
     */
    public void logPassengerMoveAcrosseNode(String relatedEntityId, TripItem tripItem) {
        logPassengerMoveAcrosseNode(relatedEntityId, tripItem.tripPositionByNodeId);
    }

    /**
     * 
     * Logs that a particular agent moved across a position
     * 
     * @param passengerId
     * @param positionByNodeId
     */
    public void logPassengerMoveAcrosseNode(String passengerId, long positionByNodeId) {

        log(new PassengerMoveAcrossNodeLogItem(passengerId, getCurrentSimulationTime(),
                positionByNodeId));

    }

    /**
     * 
     * Logs that a particular agent got off a vehicle and on a position
     * 
     * @param passengerId
     * @param tripClone
     * @param tripItem
     */
    public <TTrip extends GraphTrip<TripItem>> void logPassengerGotOffToVehicleDoneFullTrip(
            String passengerId, TTrip tripClone, TripItem tripItem) {
        long exitPositionByNodeId = tripItem.tripPositionByNodeId;

        if (tripClone instanceof PTTrip) {
            log(new PassengerExitPTVehicleFullTripLogItem(passengerId, getCurrentSimulationTime(),
                    exitPositionByNodeId, getLineId(tripClone)));
        } else {
            log(new PassengerExitVehicleFullTripLogItem(passengerId, getCurrentSimulationTime(),
                    exitPositionByNodeId));
        }

    }

    /**
     * 
     * Logs that a particular agent was not able to enter into vehicle because
     * it was overcrowd
     * 
     * @param vehicleId
     * @param trip
     */
    public <TTrip extends GraphTrip<TripItem>> void logOvercrowdedVehicle(String vehicleId, TTrip trip) {
        if (trip instanceof PTTrip) {
            log(new OvercrowdedPTVehicleLogItem(getCurrentSimulationTime(), vehicleId,
                    ((PTTrip) trip).showCurrentStationId()));
        }
    }

    /**
     * 
     * Logs that a particular agent started waiting on a given position
     * 
     * @param agentId
     * @param trip
     */
    public <TTrip extends GraphTrip<TripItem>> void logStartWaitingOnPosition(String agentId, TTrip trip) {
        if (trip instanceof PTTrip) {
            log(new StartWaitingOnStationLogItem(getCurrentSimulationTime(), agentId,
                    ((PTTrip) trip).showCurrentStationId()));
        }
    }

    /**
     * 
     * Logs that a particular agent ended waiting on a given position
     * 
     * @param agentId
     * @param trip
     */
    public <TTrip extends GraphTrip<TripItem>> void logEndWaitingOnPosition(String agentId, TTrip trip) {
        if (trip instanceof PTTrip) {
            log(new EndWaitingOnStationLogItem(getCurrentSimulationTime(), agentId));
        }

    }

}
