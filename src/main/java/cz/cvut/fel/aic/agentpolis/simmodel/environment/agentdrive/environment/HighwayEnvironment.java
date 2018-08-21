package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment;

import cz.agents.alite.configreader.ConfigReader;
import cz.agents.alite.configurator.Configurator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.SimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.RoadNetworkRouter;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.XMLReader;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.HighwayStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * public class {@link HighwayEnvironment} provides {@link HighwayStorage},
 * {@link RoadNetwork}
 */
@SuppressWarnings("JavadocReference")
public class HighwayEnvironment {

    private final Logger logger = Logger.getLogger(HighwayEnvironment.class);

    private HighwayStorage storage;


    private RoadNetwork roadNetwork;
    private List<SimulatorHandler> simulatorHandlers = new LinkedList<SimulatorHandler>();
    private EventProcessor ep;
    private long time;
    int numberOfPlanCalculations = 0;
    long timeDifference;

    public HighwayEnvironment() {

    }

    public HighwayEnvironment(final EventProcessor eventProcessor) {
        ep = eventProcessor;
//        XMLReader xmlReader = new XMLReader();
//        roadNetwork = xmlReader.parseNetwork(Configurator.getParamString("simulator.net.folder", "nets/junction-big/"));
//        RoadNetworkRouter.setRoadNet(roadNetwork);
        storage = new HighwayStorage(this);
        logger.info("Initialized storage and RoadNetwork");
    }

    public HighwayEnvironment(final EventProcessor eventProcessor, RoadNetwork roadNetwork) {
        ep = eventProcessor;
        this.roadNetwork = roadNetwork;
        RoadNetworkRouter.setRoadNet(roadNetwork);
        storage = new HighwayStorage(this);
        logger.info("Initialized storage and RoadNetwork");
    }

    public HighwayEnvironment(Integer time, RoadNetwork roadNetwork) {
        this.time = time;
        this.roadNetwork = roadNetwork;
        RoadNetworkRouter.setRoadNet(roadNetwork);
        storage = new HighwayStorage(this);
        logger.info("Initialized storage and RoadNetwork");
    }

    public void updateCars(RadarData radarData) {
        getStorage().updateCars(radarData);
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public void setRoadNetwork(RoadNetwork roadNetwork) {
        this.roadNetwork = roadNetwork;
        RoadNetworkRouter.setRoadNet(roadNetwork);
    }

    public HighwayStorage getStorage() {
        return storage;
    }

    public void addSimulatorHandler(SimulatorHandler sim) {
        simulatorHandlers.add(sim);
    }

    public List<SimulatorHandler> getSimulatorHandlers() {
        return simulatorHandlers;
    }

    public long getCurrentTime() {
        return ep == null ? time : ep.getCurrentTime();
    }

    public void setCurrentTime(long time) {
        this.time = time;
    }
}