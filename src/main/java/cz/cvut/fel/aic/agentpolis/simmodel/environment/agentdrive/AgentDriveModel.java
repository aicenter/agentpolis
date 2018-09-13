package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.ADModel;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.ModuleSimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.HighwayStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.VehicleInitializationData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.alite.common.event.typed.AliteEntity;
import cz.cvut.fel.aic.geographtools.Graph;


import java.security.ProviderException;
import java.util.*;

@Singleton
public class AgentDriveModel extends AliteEntity {

    private final Graph<SimulationNode, SimulationEdge> graph;
    private final AgentpolisConfig config;
    public static ADModel adConfig = new ADModel();
    private final SimulationProvider simulationProvider;
    private final TimeProvider timeProvider;
    private HighwayEnvironment highwayEnvironment;
    private RoadNetwork roadNetwork;
    private boolean initialPlansReceived = false;
    private XMLReader xmlReader;
    private ModuleSimulatorHandler msh;
    private static String CONFIG_FILE = "settings/groovy/highway.groovyx";

    @Inject
    public AgentDriveModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                           SimulationProvider simulationProvider, TimeProvider timeProvider/*, ShapeUtils shapeUtils*/)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        AgentDriveModel.adConfig = config.adModel;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        initRoadNetwork();
        highwayEnvironment = new HighwayEnvironment(simulationProvider.getSimulation(), roadNetwork);
        // initTraffic();

        // Adds this class as an EventHandler for TypedSimulation
        init(simulationProvider.getSimulation());
        msh = new ModuleSimulatorHandler(highwayEnvironment, new HashSet<Integer>(), highwayEnvironment.getPlanCallback());
        highwayEnvironment.addSimulatorHandler(msh);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.isType(AgentdriveEventType.INITIALIZE)) {
            if (!initialPlansReceived) {
                initialPlansReceived = true;
                highwayEnvironment.getStorage().setSTARTTIME(timeProvider.getCurrentSimTime());
            }
            VehicleInitializationData vid = (VehicleInitializationData) event.getContent();
            initVehicle(vid);
            getEventProcessor().addEvent(AgentdriveEventType.DATA, null, null, highwayEnvironment.getStorage().getCurrentRadarData());
        } else if (event.isType(AgentdriveEventType.UPDATE_TRIP)) {
            TripUpdateMessage tripUpdateMessage = (TripUpdateMessage) event.getContent();
            highwayEnvironment.getStorage().updateRoute(Integer.parseInt(tripUpdateMessage.getId()), tripUpdateMessage.getNodeIds());
        } else if (event.isType(AgentdriveEventType.DATA)) {
            if (HighwayStorage.isFinished) {
                getEventProcessor().addEvent(AgentdriveEventType.FINISH, null, null, null);
            } else {
                highwayEnvironment.updateCars((RadarData) event.getContent());
                getEventProcessor().addEvent(AgentdriveEventType.DATA, null, null, highwayEnvironment.getStorage().getCurrentRadarData(), 50);
                getEventProcessor().addEvent(AgentdriveEventType.UPDATE_POS, null, null, new UpdatePositionMessage((RadarData) event.getContent()));
                getEventProcessor().addEvent(AgentdriveEventType.UPDATE_NODE_POS, null, null, new EdgeUpdateMessage((RadarData)event.getContent(), roadNetwork));
            }
        } else if (event.isType(AgentdriveEventType.FINISH)) {
            int id = Integer.parseInt((String) event.getContent());
            highwayEnvironment.getStorage().removeAgent(id);
        }
    }

    protected List<Enum> getEventTypesToHandle() {
        return Arrays.asList(AgentdriveEventType.values());
    }

    public HighwayEnvironment getHighwayEnvironment() {
        return highwayEnvironment;
    }

    @Override
    public EventProcessor getEventProcessor() {
        return simulationProvider.getSimulation();
    }

    private void initRoadNetwork() {
        xmlReader = new XMLReader();
        roadNetwork = xmlReader.parseNetwork(config.adModel.netFolderPath);
        RoadNetworkRouter.setRoadNet(roadNetwork);
    }

    public void initVehicle(VehicleInitializationData vid) {
        final HighwayStorage storage = highwayEnvironment.getStorage();
        storage.addForInsert(vid);
        msh.addPlannedVehicle(Integer.parseInt(vid.getId()));
    }
}
