package cz.agents.agentpolis.simmodel.environment.model.action.driving;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.DriveActionLogger;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.DrivingFinishedActivityCallback;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.AgentStorage;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePlanNotificationModel;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveEntityAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.action.LinkEntityAction;
import cz.agents.agentpolis.simmodel.environment.model.query.PositionQuery;
import cz.agents.agentpolis.simmodel.environment.model.speed.SpeedInfluenceModel;
import cz.agents.agentpolis.simmodel.environment.model.speed.SpeedInfluenceModels;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import java.util.HashMap;

/**
 * Action provides logic for moving a vehicle from one place to other place,
 * including moving linked entities with the vehicle.
 *
 * @author Zbynek Moler
 */
@Singleton
public class MoveVehicleAction {

    private final EventProcessor eventProcessor;

    private final LinkEntityAction linkEntityAction;
    private final MoveEntityAction moveToNextNodeAction;
    private final EntityVelocityModel maxEntitySpeedStorage;

    private final VehiclePositionModel vehiclePositionStorage;
    private final AgentPositionModel agentPositionStorage;

    private final LinkedEntityModel linkedEntityStorage;
    private final VehiclePlanNotificationModel passengerNotifyAboutVehiclePlanStorage;

    private final PositionQuery positionQuery;

    private final SpeedInfluenceModels speedInfluenceModels;

    // ---------- LOG ---------------------
    private final DriveActionLogger driveActionLogger;

    // ---------- LOG ---------------------
    
    
    
    private final HashMap<String, DelayData> vehicleDelayData;
    
    
    
    
    

    @Inject
    public MoveVehicleAction(EventProcessor eventProcessor, LinkEntityAction linkEntityAction,
            MoveEntityAction moveToNextNodeAction, EntityVelocityModel maxEntitySpeedStorage,
            VehiclePositionModel vehiclePositionStorage, AgentPositionModel agentPositionStorage,
            LinkedEntityModel linkedEntityStorage, AgentStorage agents,
            VehiclePlanNotificationModel passengerNotifyAboutVehiclePlanStorage,
            PositionQuery positionQuery, SpeedInfluenceModels speedInfluenceModels,
            DriveActionLogger driveActionLogger) {
        super();
        this.eventProcessor = eventProcessor;
        this.linkEntityAction = linkEntityAction;
        this.moveToNextNodeAction = moveToNextNodeAction;
        this.maxEntitySpeedStorage = maxEntitySpeedStorage;
        this.vehiclePositionStorage = vehiclePositionStorage;
        this.agentPositionStorage = agentPositionStorage;
        this.linkedEntityStorage = linkedEntityStorage;
        this.passengerNotifyAboutVehiclePlanStorage = passengerNotifyAboutVehiclePlanStorage;
        this.positionQuery = positionQuery;
        this.speedInfluenceModels = speedInfluenceModels;
        this.driveActionLogger = driveActionLogger;
        vehicleDelayData = new HashMap<>();
    }
    
    public DelayData getDelayDataForVehicle(String vehicleId){
        return vehicleDelayData.get(vehicleId);
    }
    

    /**
     * Method moves vehicle from start place (node) to destination place (node).
     *
     * @param startNode
     * @param destinationByNodeId
     * @param typeOfGraphForMoving
     *            - taken information about using graph
     */
    public void driven(final String vehicleId, final int startNode,
            final int destinationByNodeId, final GraphType typeOfGraphForMoving) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                drivenBaseOnDeparture(vehicleId, startNode, destinationByNodeId,
                        typeOfGraphForMoving);

            }

            @Override
            public EventProcessor getEventProcessor() {
                return null;
            }
        });

    }

    /**
     * Invokes actions for moving vehicle and linked entities ( like passengers
     * and driver)
     *
     * @param startNodeByNodeId
     * @param destinationByNodeId
     * @param typeOfGraphForMoving
     */
    private void drivenBaseOnDeparture(final String vehicleId, int startNodeByNodeId,
                                       int destinationByNodeId, GraphType typeOfGraphForMoving) {

        double velocityInmps = maxEntitySpeedStorage.getEntityVelocityInmps(vehicleId);
        velocityInmps = computeIncludesOnVelocity(velocityInmps, typeOfGraphForMoving,
                startNodeByNodeId, destinationByNodeId);

        double lengthInMeter = positionQuery.getLengthBetweenPositions(typeOfGraphForMoving,
                startNodeByNodeId, destinationByNodeId);

        long duration = MoveUtil.computeDuration(velocityInmps, lengthInMeter);
        
        vehicleDelayData.put(vehicleId, new DelayData(duration, eventProcessor.getCurrentTime()));

        moveToNextNodeAction.moveToNode(vehicleId, destinationByNodeId, duration,
                vehiclePositionStorage);

        Set<String> passengersByIds = linkedEntityStorage.getLinkedEntites(vehicleId);

        // ---------- LOG ---------------------
        driveActionLogger.logNumPassengers(vehicleId, passengersByIds.size(), startNodeByNodeId,
                destinationByNodeId);
        driveActionLogger.logEntityMovePlan(vehicleId, startNodeByNodeId, destinationByNodeId);
        // ---------- LOG ---------------------

        for (String passengerById : passengersByIds) {
            moveToNextNodeAction.moveToNode(passengerById, destinationByNodeId, duration,
                    agentPositionStorage);
        }
    }

    private double computeIncludesOnVelocity(double originVelocityImps, GraphType graphType,
            long fromNodeByNodeId, long toNodeByNodeId) {

        double influencedSpeed = originVelocityImps;
        for (SpeedInfluenceModel speedInfluenceModel : speedInfluenceModels
                .getSpeedInfluenceModels()) {
            influencedSpeed = speedInfluenceModel.computedInfluencedSpeed(graphType,
                    fromNodeByNodeId, toNodeByNodeId, originVelocityImps, influencedSpeed);
        }

        assert influencedSpeed > 0 : "influencedSpeed has to be grater then zero";

        return influencedSpeed;
    }

    /**
     * Represents getting off all linked agents (passengers and driver)
     */
    public void finishedDriving(final String vehicleId,
            final DrivingFinishedActivityCallback drivingActivityCallback) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                Set<String> passengersByIds = linkedEntityStorage.getLinkedEntites(vehicleId);
                passengersByIds.forEach(linkEntityAction::unLinkEnities);
                drivingActivityCallback.finishedDriving();
            }

            @Override
            public EventProcessor getEventProcessor() {
                // TODO Auto-generated method stub
                return null;
            }
        });

    }

    /**
     * Add empty sensor for notify driver about next vehicle move
     *
     * @param driveId
     */
    public void addDriverEmptyNotify(String driveId) {

        passengerNotifyAboutVehiclePlanStorage.addPassengerVehiclePlanAndSensorCallbacksForNotify(
                driveId, new EmptyPassengerVehiclePlanCallback());
    }

    /**
     * Remove empty sensor for notify driver about next vehicle move
     *
     * @param driveId
     */
    public void removeDriverNotify(String driveId) {
        passengerNotifyAboutVehiclePlanStorage.removePassengerVehiclePlanAndSensorCallback(driveId);
    }

    private static class EmptyPassengerVehiclePlanCallback implements VehicleArrivedCallback {

        @Override
        public void notifyPassengerAboutVehiclePlan(int fromNodeId, int toNodeId, String vehicleId) {
            // Do nothing

        }

        @Override
        public void notifyWaitingPassengerAboutVehiclePlan(int fromNodeId, int toNodeId,
                                                           String vehicleId) {
            // Do nothing

        }

    }

}
