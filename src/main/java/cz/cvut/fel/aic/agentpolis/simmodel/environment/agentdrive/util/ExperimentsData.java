package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent.SDAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.HighwayStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;
import cz.agents.alite.configurator.Configurator;
import org.apache.log4j.Logger;

import javax.vecmath.Point3f;
import java.util.*;

/**
 * Created by david on 9/4/15.
 */
public class ExperimentsData {
    private long ENDTIME = 0;
    private Map<Integer, Pair<Float, Float>> graphOfArrivals = new LinkedHashMap<Integer, Pair<Float, Float>>();
    private LinkedList<Pair<Float, Integer>> numberOfCarsInSimulation = new LinkedList<Pair<Float, Integer>>();
    private LinkedList<Float> timesOfArrival = new LinkedList<Float>();
    private Map<Integer, Queue<Pair<Long, Float>>> distances = new LinkedHashMap<Integer, Queue<Pair<Long, Float>>>();
    private Map<Integer, Queue<Pair<Long, Float>>> speeds = new LinkedHashMap<Integer, Queue<Pair<Long, Float>>>();
    private Map<Integer, Pair<Point3f, Float>> lenghtOfjourney = new LinkedHashMap<Integer, Pair<Point3f, Float>>();
    private long radarDataSystemTime = 0l;
    private LinkedList<Integer> computationTime = new LinkedList<Integer>();
    private HighwayStorage storage;
    private final Logger logger = Logger.getLogger(ExperimentsData.class);

    public ExperimentsData(HighwayStorage storage) {
        this.storage = storage;
    }

    public void simulationStarted() {

    }

    public void simulationEnded() {
        if (Configurator.getParamBool("highway.dashboard.systemTime", false)) {
            ENDTIME = System.currentTimeMillis();
        } else {
            ENDTIME = storage.getHighwayEnvironment().getCurrentTime();
        }
        int numberOfCollisons = calculateNumberOfCollisions() / 2;
        FileUtil.getInstance().writeToFile(speeds, 1);
        if (Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) {
            Map<List<String>, Pair<Integer, Float>> listPairMap = FileUtil.getInstance().writeReport(numberOfCollisons, storage.getAgents().size() / ((ENDTIME - storage.getSTARTTIME()) / 1000f),
                    ENDTIME - storage.getSTARTTIME(), calculateAverageSpeed(speeds), lenghtOfjourney, timesOfArrival, computationTime);
            FileUtil.getInstance().writeGraphOfArrivals(graphOfArrivals, listPairMap);
            FileUtil.getInstance().writeNumberOfCarsInSimulation(numberOfCarsInSimulation);
            logger.info("Number of cars in time is " + storage.getAgents().size() / ((ENDTIME - storage.getSTARTTIME())));
        }
        logger.info("Number of collisions is " + numberOfCollisons + "\n");
        FileUtil.getInstance().writeToFile(distances, 0);
        logger.info("Times of arrival: " + timesOfArrival);
    }

    private int calculateNumberOfCollisions() {
        int num = 0;
        for (Map.Entry<Integer, Agent> entry : storage.getAgents().entrySet()) {
            Agent pair = entry.getValue();
            if (pair instanceof SDAgent) {
                num += ((SDAgent) pair).getNumberOfCollisions();
            }
        }
        return num;
    }

    public long getENDTIME() {
        return ENDTIME;
    }

    private Map<Integer, Float> calculateAverageSpeed(Map<Integer, Queue<Pair<Long, Float>>> speeds) {
        Map<Integer, Float> averageSpeeds = new HashMap<Integer, Float>();
        for (Map.Entry<Integer, Queue<Pair<Long, Float>>> obj : speeds.entrySet()) {
            Queue<Pair<Long, Float>> carspeeds = obj.getValue();
            Float sumSpeed = 0f;
            int numberOfSpeeds = 0;
            while (carspeeds.peek() != null) {
                sumSpeed += carspeeds.poll().getValue();
                numberOfSpeeds++;
            }
            averageSpeeds.put(obj.getKey(), sumSpeed / numberOfSpeeds);
        }
        return averageSpeeds;
    }

    public void updateNumberOfCars(RadarData object) {
        radarDataSystemTime = System.currentTimeMillis();
        if (Configurator.getParamBool("highway.dashboard.systemTime", false)) {
            if (storage.getSTARTTIME() != 0l) {
                numberOfCarsInSimulation.add(new Pair<Float, Integer>((System.currentTimeMillis() - storage.getSTARTTIME()) / 1000f, object.getCars().size()));
            } else numberOfCarsInSimulation.add(new Pair<Float, Integer>(0f, object.getCars().size()));
        } else {
            numberOfCarsInSimulation.add(new Pair<Float, Integer>(storage.getHighwayEnvironment().getCurrentTime() / 1000f, object.getCars().size()));
        }
    }

    public void updateTimesAndGraphOfArrivals(RadarData object, int id) {
        if (Configurator.getParamBool("highway.dashboard.systemTime", false)) {
            timesOfArrival.add((System.currentTimeMillis() - storage.getSTARTTIME()) / 1000f);
            Pair<Float, Float> floatFloatPair = graphOfArrivals.get(id);
            graphOfArrivals.put(id, new Pair<Float, Float>(floatFloatPair.getKey(), (System.currentTimeMillis() - storage.getSTARTTIME()) / 1000f));
        } else {
            timesOfArrival.add(storage.getHighwayEnvironment().getCurrentTime() / 1000f);
            Pair<Float, Float> floatFloatPair = graphOfArrivals.get(id);
            graphOfArrivals.put(id, new Pair<Float, Float>(floatFloatPair.getKey(), storage.getHighwayEnvironment().getCurrentTime() / 1000f));
        }
    }

    public void updateDistances(RadarData object, Map.Entry<Integer, RoadObject> entry) {
        Queue<Pair<Long, Float>> original = distances.get(entry.getKey());
        if (original == null)
            original = new LinkedList<Pair<Long, Float>>();
        Queue<Pair<Long, Float>> originalS = speeds.get(entry.getKey());
        if (originalS == null)
            originalS = new LinkedList<Pair<Long, Float>>();
        Long timeKey;
        if (Configurator.getParamBool("highway.dashboard.systemTime", false)) {
            timeKey = (System.currentTimeMillis() - storage.getSTARTTIME());
        } else {
            timeKey = storage.getHighwayEnvironment().getCurrentTime();
        }
        //FUNNEL
        //   Float distVal = entry.getValue().getPosition().distance(new Point3f(-1.75f, -600f, 0f));
        //X JUNTIONS
        Float distVal = entry.getValue().getPosition().distance(new Point3f(0f, 0f, 0f));

        Float speed = entry.getValue().getVelocity().length();
        if (original.isEmpty() || distVal < original.peek().getValue()) {
            original.add(new Pair<Long, Float>(timeKey, distVal));
        }
        distances.put(entry.getKey(), original);
        originalS.add(new Pair<Long, Float>(timeKey, speed));
        speeds.put(entry.getKey(), originalS);

        Pair<Point3f, Float> loj = lenghtOfjourney.get(entry.getKey());
        Point3f newPosition;
        Float distance;
        if (loj == null) {
            newPosition = entry.getValue().getPosition();
            distance = 0f;
        } else {
            Point3f oldPosition = lenghtOfjourney.get(entry.getKey()).getKey();
            newPosition = entry.getValue().getPosition();
            distance = lenghtOfjourney.get(entry.getKey()).getValue();
            distance += oldPosition.distance(newPosition);
        }
        lenghtOfjourney.put(entry.getKey(), new Pair<Point3f, Float>(newPosition, distance));
    }

    public void calcPlanCalculation(Long time) {
        if (radarDataSystemTime != 0L) {
            Integer planTime = (int) (time - radarDataSystemTime);
            computationTime.add(planTime);
        }
    }

    public void vehicleCreation(int id) {
        if (Configurator.getParamBool("highway.dashboard.systemTime", false)) {
            if (storage.getSTARTTIME() == 0L) {
                graphOfArrivals.put(id, new Pair<Float, Float>(0f, null));
            } else
                graphOfArrivals.put(id, new Pair<Float, Float>((System.currentTimeMillis() - storage.getSTARTTIME()) / 1000f, null));
        } else {
            graphOfArrivals.put(id, new Pair<Float, Float>(storage.getHighwayEnvironment().getCurrentTime() / 1000f, null));
        }
    }
}
