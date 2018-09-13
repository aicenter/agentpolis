package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.ADAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.VehicleMoveActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.ADDriver;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.ADPhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.DestinationUpdateMessage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.VehicleInitializationData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simmodel.eventType.DriveEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.eventType.Transit;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import cz.cvut.fel.aic.geographtools.Graph;

import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentdriveDrive<A extends ADAgent & ADDriver> extends PhysicalVehicleDrive<A> {

    private final ADPhysicalVehicle vehicle;

    private final Trip<SimulationNode> trip;

    private final Graph<SimulationNode, SimulationEdge> graph;

    private final EventProcessor eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final int tripId;

    private final AgentDriveModel agentDriveModel;

    private SimulationNode from;

    private SimulationNode to;

    public AgentdriveDrive(ActivityInitializer activityInitializer, TransportNetworks transportNetworks,
                           VehicleMoveActivityFactory moveActivityFactory, TypedSimulation eventProcessor,
                           StandardTimeProvider timeProvider,
                           A agent, ADPhysicalVehicle vehicle, Trip<SimulationNode> trip,
                           int tripId, AgentDriveModel agentDriveModel) {
        super(activityInitializer, agent);
        this.vehicle = vehicle;
        this.trip = trip;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripId = tripId;
        this.agentDriveModel = agentDriveModel;
        graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
    }

    @Override
    protected void performAction() {
        if (getTrip() == null || getTrip().isEmpty()) {
            finish();
            return;
        }
        System.out.println("Agent: " + this.agent.getId() + " Trip: " + getTrip().locationIdsToString());
        eventProcessor.addEvent(AgentdriveEventType.INITIALIZE, null, null, new VehicleInitializationData(vehicle.getId(), vehicle.getVelocity(), getTripSourceIds(), timeProvider.getCurrentSimTime()));
        from = trip.getAndRemoveFirstLocation();

        vehicle.setPosition(from);
        agent.setPos(Double.MAX_VALUE, Double.MAX_VALUE);
        vehicle.setPosition(Double.MAX_VALUE, Double.MAX_VALUE);

        agent.startDriving(vehicle);
        agent.setTargetNode(trip.getFirstLocation());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.isType(AgentdriveEventType.FINISH)) {
            agent.endDriving();
            finish();
        } else if (event.isType(AgentdriveEventType.UPDATE_DESTINATION)) {
            DestinationUpdateMessage eum = ((DestinationUpdateMessage) event.getContent());
            if (Integer.parseInt(this.getAgent().getId()) == eum.getCarId()) {
                if (trip.getLocations().isEmpty()) {
                    //TODO: Exception
                } else if (eum.getReachedAgentPolisId() == trip.getFirstLocation().getSourceId()) {

                    // sendTripUpdateToAD();
                } else if (eum.getReachedAgentPolisId() == trip.getLocations().get(trip.getLocations().size() - 1).sourceId) {
                    vehicle.setLastFromPosition(from);
                    finish();
                    eventProcessor.addEvent(AgentdriveEventType.FINISH, null, null, agent.getId());
                }
            }
        } else if (event.isType(AgentdriveEventType.UPDATE_POS)) {
            UpdatePositionMessage upm = (UpdatePositionMessage) event.getContent();
            if (upm.getCarPositions().containsKey(Integer.parseInt(agent.getId()))) {
                Point3f position = upm.getCarPositions().get(Integer.parseInt(agent.getId()));
                vehicle.setPosX(position.getX());
                vehicle.setPosY(position.getY());
            }
        } else if(event.isType(AgentdriveEventType.UPDATE_NODE_POS)){
            EdgeUpdateMessage eum = (EdgeUpdateMessage) event.getContent();
            for (int id : eum.getLastJunctions().keySet()){
                if (Integer.parseInt(agent.getId()) == id){
                    if (!trip.isEmpty() && eum.getLastJunctions().get(id) == trip.getFirstLocation().getSourceId()){
                        //vehicle.setLastFromPosition(from);
                        //if (to != null) from = to;
                        from = trip.getAndRemoveFirstLocation();
                        to = trip.getFirstLocation();
                        if (to == null) break;
                        vehicle.setPosition(from);
                        agent.setPosition(from);
                        //if (!trip.isEmpty())agent.setTargetNode(trip.getFirstLocation());
                        agent.setTargetNode(to);
                        triggerVehicleEnteredEdgeEvent();
                        break;
                    }
                }
            }
        }
        super.handleEvent(event);
    }


    private void triggerVehicleEnteredEdgeEvent() {
        SimulationEdge edge = graph.getEdge(from, to);
        Transit transit = new Transit(timeProvider.getCurrentSimTime(), edge.wayID, tripId);
        eventProcessor.addEvent(DriveEvent.VEHICLE_ENTERED_EDGE, null, null, transit); // post-delayed action
    }

    protected void sendTripUpdateToAD() {
        eventProcessor.addEvent(AgentdriveEventType.UPDATE_TRIP, null, null, new TripUpdateMessage(agent.getId(), getTripSourceIds()));
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    protected List<Enum> getEventTypesToHandle() {
        List<Enum> typesList = new ArrayList<>();
        typesList.addAll(Arrays.asList(AgentdriveEventType.UPDATE_DESTINATION, AgentdriveEventType.DATA, AgentdriveEventType.UPDATE_NODE_POS, AgentdriveEventType.UPDATE_POS));
        return typesList;
    }

    private List<Long> getTripSourceIds() {
        List<Long> ids = new ArrayList<>();
        for (SimulationNode node : trip.getLocations()) {
            ids.add(node.getSourceId());
        }
        return ids;
    }
}
