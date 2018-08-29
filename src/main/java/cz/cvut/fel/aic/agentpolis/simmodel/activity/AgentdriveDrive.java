package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.VehicleMoveActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentdriveEventType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.EdgeUpdateMessage;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentdriveDrive<A extends Agent & Driver> extends PhysicalVehicleDrive<A> {

    private final Vehicle vehicle;

    private final Trip<SimulationNode> trip;

    private final VehicleMoveActivityFactory moveActivityFactory;

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
                           A agent, Vehicle vehicle, Trip<SimulationNode> trip,
                           int tripId, AgentDriveModel agentDriveModel) {
        super(activityInitializer, agent);
        this.vehicle = vehicle;
        this.trip = trip;
        this.moveActivityFactory = moveActivityFactory;
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

    }

    protected void start() {
    }

    @Override
    public void handleEvent(Event event) {
        if (event.isType(AgentdriveEventType.DATA)) {
//            RadarData radarData = (RadarData) event.getContent();
//            //TODO
//            eventProcessor.addEvent(AgentdriveEventType.UPDATE_PLAN, null, null, new ADMessage(vehicle, trip, graph, tripId, agentDriveModel), 10000);
        } else if (event.isType(AgentdriveEventType.FINISH)) {
            agent.endDriving();
            finish();
        } else if (event.isType(AgentdriveEventType.UPDATE_EDGE) && Integer.parseInt(this.getAgent().getId()) == ((EdgeUpdateMessage) event.getContent()).getCarId()) {
            to = trip.getAndRemoveFirstLocation();
            agent.setPosition(to);
        }
        super.handleEvent(event);
    }

    @Override
    protected void onChildActivityFinish(Activity activity) {
        if (trip.isEmpty() || stoped) {
            agent.endDriving();
            vehicle.setLastFromPosition(from);
            finish();
        } else {
            from = to;
            move();
        }
    }

    private void move() {
        to = trip.getAndRemoveFirstLocation();
        SimulationEdge edge = graph.getEdge(from, to);

        runChildActivity(moveActivityFactory.create(agent, edge, from, to));
        triggerVehicleEnteredEdgeEvent();
    }

    private void triggerVehicleEnteredEdgeEvent() {
        SimulationEdge edge = graph.getEdge(from, to);
        Transit transit = new Transit(timeProvider.getCurrentSimTime(), edge.wayID, tripId);
        eventProcessor.addEvent(DriveEvent.VEHICLE_ENTERED_EDGE, null, null, transit); // post-delayed action
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    protected List<Enum> getEventTypesToHandle() {
        List<Enum> typesList = new ArrayList<>();
        typesList.addAll(Arrays.asList(AgentdriveEventType.FINISH, AgentdriveEventType.DATA, AgentdriveEventType.UPDATE_EDGE));
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
