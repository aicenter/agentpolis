package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.IdGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.AgentdriveDrive;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.PhysicalVehicleDrive;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import org.slf4j.LoggerFactory;

public class AgentdriveActivityFactory extends ActivityFactory implements PhysicalVehicleDriveFactory {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AgentdriveActivityFactory.class);

    private final TransportNetworks transportNetworks;

    private final VehicleMoveActivityFactory moveActivityFactory;

    private final TypedSimulation eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final IdGenerator tripIdGenerator;

    private final TripsUtil tripsUtil;

    private final AgentDriveModel agentDriveModel;

    @Inject
    public AgentdriveActivityFactory(TransportNetworks transportNetworks, VehicleMoveActivityFactory moveActivityFactory,
                                     TypedSimulation eventProcessor, StandardTimeProvider timeProvider, IdGenerator tripIdGenerator,
                                     TripsUtil tripsUtil, AgentDriveModel agentDriveModel) {
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripIdGenerator = tripIdGenerator;
        this.tripsUtil = tripsUtil;
        this.agentDriveModel = agentDriveModel;
    }

    @Override
    public <A extends Agent & Driver> void runActivity(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        create(agent, vehicle, trip).run();
    }

    @Override
    public <A extends Agent & Driver> AgentdriveDrive create(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        return new AgentdriveDrive(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor,
                timeProvider, agent, vehicle, trip, tripIdGenerator.getId(), agentDriveModel);
    }

    @Override
    public <A extends Agent & Driver> AgentdriveDrive create(A agent, PhysicalVehicle vehicle, SimulationNode target) {
        Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition().getId(), target.getId());

        return new AgentdriveDrive(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider,
                agent, vehicle, trip, tripIdGenerator.getId(), agentDriveModel);
    }
}
