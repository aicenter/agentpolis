package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.alite.configreader.ConfigReader;
import cz.agents.alite.configurator.Configurator;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.ModuleSimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.HighwayStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.alite.common.event.typed.AliteEntity;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.apache.log4j.PropertyConfigurator;


import javax.vecmath.Point2f;
import java.security.ProviderException;
import java.util.*;

@Singleton
public class AgentDriveModel extends AliteEntity {

    private final Graph<SimulationNode, SimulationEdge> graph;
    private final AgentpolisConfig config;
    private final SimulationProvider simulationProvider;
    private final TimeProvider timeProvider;
    private HighwayEnvironment highwayEnvironment;
    private RoadNetwork roadNetwork;
    private boolean initialPlansReceived = false;

    private static String CONFIG_FILE = "settings/groovy/highway.groovy";

    @Inject
    public AgentDriveModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                           SimulationProvider simulationProvider, TimeProvider timeProvider/*, ShapeUtils shapeUtils*/)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        initConfigurator();
        initRoadNetwork();

        highwayEnvironment = new HighwayEnvironment(simulationProvider.getSimulation(), roadNetwork);
        initTraffic();

        // Adds this class as an EventHandler for TypedSimulation
        init(simulationProvider.getSimulation());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.isType(AgentdriveEventType.INITIALIZE) && !initialPlansReceived) {
            initialPlansReceived = true;
            highwayEnvironment.getStorage().setSTARTTIME(getEventProcessor().getCurrentTime());
            ADMessage adMessage = (ADMessage) event.getContent();
            highwayEnvironment.updateCars(ADMessageToRadarData(adMessage));
            getEventProcessor().addEvent(AgentdriveEventType.DATA, null, null, highwayEnvironment.getStorage().getCurrentRadarData());
        } else if (event.isType(AgentdriveEventType.UPDATE_PLAN)) {
            ADMessage adMessage = (ADMessage) event.getContent();
            //highwayEnvironment.getAgents().get(adMessage.getVehicle().getId()).getNavigator().updateRoute();
            //TODO: change plans, decide until when is agent able to modify its route
        } else if (event.isType(AgentdriveEventType.DATA)) {
            if (HighwayStorage.isFinished) {
                getEventProcessor().addEvent(AgentdriveEventType.FINISH, null, null, null);
            } else {
                highwayEnvironment.updateCars((RadarData) event.getContent());
                getEventProcessor().addEvent(AgentdriveEventType.DATA, null, null, highwayEnvironment.getStorage().getCurrentRadarData(), 1000);
            }
        }
    }

    protected List<Enum> getEventTypesToHandle() {
        return Arrays.asList(AgentdriveEventType.values());
    }

    public HighwayEnvironment getHighwayEnvironment() {
        return highwayEnvironment;
    }

    private RadarData ADMessageToRadarData(ADMessage adMessage) {
        //TODO
        return new RadarData();
    }

    @Override
    public EventProcessor getEventProcessor() {
        return simulationProvider.getSimulation();
    }

    private void initRoadNetwork() {
        XMLReader xmlReader = new XMLReader();
        roadNetwork = xmlReader.parseNetwork(Configurator.getParamString("simulator.net.folder", "nets/junction-big/"));
        RoadNetworkRouter.setRoadNet(roadNetwork);
    }

    private void initConfigurator() {
        // Configuration loading using alite's Configurator and ConfigReader
        ConfigReader configReader = new ConfigReader();
        configReader.loadAndMerge(CONFIG_FILE);
        Configurator.init(configReader);
        String logfile = Configurator.getParamString("cz.highway.configurationFile", "settings/log4j/log4j.properties");
        PropertyConfigurator.configure(logfile);
    }

    private void initTraffic() {
        final XMLReader reader = new XMLReader(Configurator.getParamString("simulator.net.folder", "notDefined"));
        // All vehicle id's
        final Collection<Integer> vehicles = reader.getRoutes().keySet();
        final Map<Integer, Float> departures = reader.getDepartures();
        final int size;
        if (!Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) {
            size = Configurator.getParamInt("highway.dashboard.numberOfCarsInSimulation", vehicles.size());
        } else {
            size = vehicles.size();
        }
        final HighwayStorage storage = highwayEnvironment.getStorage();

        Iterator<Integer> vehicleIt = vehicles.iterator();
        Set<Integer> plannedVehiclesLocal = new HashSet<Integer>();
        int sizeL = size;
        if (size > vehicles.size()) sizeL = vehicles.size();

        for (int i = 0; i < sizeL; i++) {
            int vehicleID = vehicleIt.next();
            if (Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) {
                storage.addForInsert(vehicleID, departures.get(vehicleID));
            } else {
                storage.addForInsert(vehicleID);
            }
            plannedVehiclesLocal.add(vehicleID);
        }
        final Set<Integer> plannedVehicles = plannedVehiclesLocal;
        highwayEnvironment.addSimulatorHandler(new ModuleSimulatorHandler(highwayEnvironment, new HashSet<Integer>(plannedVehicles), highwayEnvironment.getPlanCallback()));
    }


    /*
     * Temporary solution for AgentDrive and AgentPolis network
     * */
    public static GraphBuilder<SimulationNode, SimulationEdge> roadNetworkToGraph() {
        ConfigReader configReader = new ConfigReader();
        configReader.loadAndMerge(CONFIG_FILE);
        Configurator.init(configReader);
        String logfile = Configurator.getParamString("cz.highway.configurationFile", "settings/log4j/log4j.properties");
        PropertyConfigurator.configure(logfile);
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        XMLReader xmlReader = new XMLReader();
        RoadNetwork network = xmlReader.parseNetwork(Configurator.getParamString("simulator.net.folder", "nets/junction-big/"));
        Map<String, Integer> idConvertor = new HashMap<>();

        int i = 0;
        for (Junction j : network.getJunctions().values()) {
            Point2f center = j.getCenter();

            int x = Math.round(center.x) * 50;
            int y = Math.round(center.y) * 50;
            System.out.println(j.getId());
            idConvertor.put(j.getId(), i);
            SimulationNode node = new SimulationNode(i, 0, x, y, x, y, 0);
            i++;
            graphBuilder.addNode(node);
        }
        for (Edge e : network.getEdges().values()) {
            SimulationEdge edge = new SimulationEdge(graphBuilder.getNode(idConvertor.get(e.getFrom())),
                    graphBuilder.getNode(idConvertor.get(e.getTo())),
                    0, 0, 0, Math.round(e.getLaneByIndex(0).getLength()), 40,
                    e.getLanes().size(),
                    new EdgeShape(Arrays.asList(graphBuilder.getNode(idConvertor.get(e.getFrom())), graphBuilder.getNode(idConvertor.get(e.getTo())))));
            graphBuilder.addEdge(edge);
        }
        return graphBuilder;
    }
}
