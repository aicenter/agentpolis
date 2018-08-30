package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import cz.agents.alite.configurator.Configurator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.SimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Region;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Edge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.PathNotFoundException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util.ExperimentsData;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.*;

public class HighwayStorage {

    private final Logger logger = Logger.getLogger(HighwayStorage.class);
    public static boolean isFinished = false;
    private ExperimentsData experimentsData;


    private HighwayEnvironment highwayEnvironment;
    protected double acceleration = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.deacceleration", -6.0);
    private final RoadDescription roadDescription;
    private final RoadNetwork roadNetwork;
    private final Map<Integer, Agent> agents = new LinkedHashMap<Integer, Agent>();
    private final Map<Integer, RoadObject> posCurr = new LinkedHashMap<Integer, RoadObject>();
    private TreeSet<Integer> forRemoveFromPosscur;
    private final Map<Integer, List<Action>> actions = new LinkedHashMap<Integer, List<Action>>();
    private final float SAVE_DISTANCE = 10;
    private final Map<Integer, Region> trajectories = new LinkedHashMap<Integer, Region>();
    public Queue<VehicleInitializationData> vehiclesForInsert;
    private Comparator<VehicleInitializationData> comparator;
    private RadarData currentRadarData = new RadarData();
    private int counter = 0;

    public void setSTARTTIME(long STARTTIME) {
        this.STARTTIME = STARTTIME;
    }

    private long STARTTIME;

    private static final double CHECKING_DISTANCE = Configurator.getParamDouble("highway.storage.checkingDistance", 500d);

    private static final double SAFETY_RESERVE = Configurator.getParamDouble("highway.storage.safetyReserve", 10d);
    private static final double INSERT_SPEED = Configurator.getParamDouble("highway.storage.insertSpeed", 0.5d);


    private boolean t = true;

    public HighwayStorage(HighwayEnvironment environment) {
        this.highwayEnvironment = environment;
        experimentsData = new ExperimentsData(this);
        roadNetwork = highwayEnvironment.getRoadNetwork();
        roadDescription = new RoadDescription(roadNetwork);
        comparator = new QueueComparator();
        vehiclesForInsert = new PriorityQueue<VehicleInitializationData>(20, comparator);
        logger.setLevel(Level.DEBUG);
    }

    public ExperimentsData getExperimentsData() {
        return experimentsData;
    }

    public long getSTARTTIME() {
        return STARTTIME;
    }


    public void updateCar(RoadObject carState) {
        int carId = carState.getId();


        Lane lane = roadNetwork.getClosestLane(carState.getPosition());
        int laneNum = lane.getIndex();
        carState.setLane(laneNum);
        logger.trace("Lane changed to:" + laneNum);
        posCurr.put(carId, carState);

    }

    public Agent createAgent(final int id, List<Edge> route) {
        String agentClassName = Configurator.getParamString("highway.agent", "RouteAgent");
        Agent agent = null;
        if (agentClassName.equals("RouteAgent")) {
            agent = new RouteAgent(id, route);
        } else if (agentClassName.equals("SDAgent")) {
            agent = new SDAgent(id, route, highwayEnvironment.getRoadNetwork());
        } else if (agentClassName.equals("GSDAgent")) {
            agent = new GSDAgent(id, route, highwayEnvironment.getRoadNetwork());
        } else {
            try {
                throw new Exception("Agent class: " + agentClassName + " not supported!");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        VehicleSensor sensor = new VehicleSensor(highwayEnvironment, agent, this);
        VehicleActuator actuator = new VehicleActuator(highwayEnvironment, agent, this);
        agent.addSensor(sensor);
        agent.addActuator(actuator);

        agents.put(id, agent);
        return agent;
    }

    public void act(int carId, List<Action> action) {
        actions.put(carId, action);
    }

    public RoadDescription getRoadDescription() {
        return roadDescription;
    }

    public Map<Integer, Agent> getAgents() {
        return agents;
    }

    public Map<Integer, RoadObject> getPosCurr() {
        return posCurr;
    }

    public Map<Integer, List<Action>> getActions() {
        return actions;
    }

    public Map<Integer, Region> getTrajectories() {
        return trajectories;
    }

    public void updateCars(RadarData object) {

        //  if (!object.getCars().isEmpty()) {
        getExperimentsData().updateNumberOfCars(object);

        forRemoveFromPosscur = new TreeSet<Integer>(posCurr.keySet());
        for (RoadObject car : object.getCars()) {
            updateCar(car);
            forRemoveFromPosscur.remove(car.getId());
        }
//        if (!forRemoveFromPosscur.isEmpty()) {
//            for (Integer id : forRemoveFromPosscur) {
//                addForInsert(id);
//                getExperimentsData().updateTimesAndGraphOfArrivals(object, id);
//            }
//        }
        if (!object.getCars().isEmpty())
            logger.debug("HighwayStorage updated vehicles: received " + object);

        for (Map.Entry<Integer, RoadObject> entry : posCurr.entrySet()) {
            getExperimentsData().updateDistances(object, entry);
        }
        recreate(object);
//               if (Configurator.getParamBool("highway.dashboard.sumoSimulation", true) &&
//                       posCurr.size() == 0 && vehiclesForInsert.isEmpty()) {
//                   isFinished = true;
//                   getExperimentsData().simulationEnded();
//
//               }

        List<Integer> agentToRemove = new ArrayList<>();
        for (Agent a : this.getAgents().values()) {
            /* get actions that were originally send with NEW_PLAN event */
            /* List<Action> actions = */

            List<Action> actions = a.getActuator().act(a.agentReact());
            int id = Integer.parseInt(a.getName());//actions.get(0).getCarId();
            if (!getPosCurr().containsKey(id)) continue;
            for (SimulatorHandler handler : highwayEnvironment.getSimulatorHandlers()) {
                if (handler.hasVehicle(id)) {
                    handler.addActions(id, actions);
                }
                if (handler.isReady()) {
                    getExperimentsData().calcPlanCalculation(System.currentTimeMillis());
                    //numberOfPlanCalculations++;
                    handler.sendPlans(getPosCurr());
                    currentRadarData = handler.getNewRadarData();
                    counter++;
                }
            }
            if (a.getNavigator().isMyLifeEnds()) agentToRemove.add(Integer.parseInt(a.getName()));
        }
        for (Integer id : agentToRemove) {
            getExperimentsData().updateTimesAndGraphOfArrivals(null, id);
            removeAgent(id);
        }

    }


    public void removeAgent(Integer carID) {
        agents.remove(carID);
        posCurr.remove(carID);
    }

    public void addForInsert(VehicleInitializationData vid) {
        vehiclesForInsert.add(vid);
    }

    public void addForInsert(String id, float velocity, List<Long> startingNodeId, long departureTime) {
        vehiclesForInsert.add(new VehicleInitializationData(id, velocity, startingNodeId, departureTime));
    }

    public void recreate(RadarData object) {
        Queue<VehicleInitializationData> notInsertedVehicles = new PriorityQueue<>(20, comparator);
        while (vehiclesForInsert.peek() != null) {
            VehicleInitializationData vehicle = vehiclesForInsert.poll();
            int id = Integer.parseInt(vehicle.getId());
            if (posCurr.containsKey(id)) {
                posCurr.remove(id);
            }
            if (agents.containsKey(id) && Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) continue;
            if (!isDeleted(object, id)) {
                notInsertedVehicles.add(vehicle);
                continue;
            } else {
//                removeAgent(id);
            }
            double updateTime = 0d;
            double randomUpdateTime = 0d;
            if (Configurator.getParamBool("highway.dashboard.systemTime", false)) {
                updateTime = (System.currentTimeMillis() - STARTTIME); //getEventProcessor().getCurrentTime();
            } else {
                updateTime = highwayEnvironment.getCurrentTime() - STARTTIME;
            }
            if (vehicle.getDepartureTime() > updateTime ||
                    (posCurr.size() >= Configurator.getParamInt("highway.dashboard.numberOfCarsInSimulation", agents.size()))) {
                notInsertedVehicles.add(vehicle);
                continue;
            }
            long originId = vehicle.getStartingNodeId().get(0);
            long endId = vehicle.getStartingNodeId().get(1);
            RouteNavigator routeNavigator = new RouteNavigator(convertNodeRouteToEdgeRoute(vehicle.getStartingNodeId()));
            if (routeNavigator.getRoute() == null || routeNavigator.getRoute().isEmpty()) {
                System.out.println("x");
                return;
            }
            routeNavigator.setCheckpoint();
            Point2f position = routeNavigator.next();
            Point3f initialPosition = new Point3f(position.x, position.y, 0);
            Point2f next = routeNavigator.next();
            routeNavigator.resetToCheckpoint();
            Vector3f initialVelocity = new Vector3f(next.x - position.x, next.y - position.y, 0);
            initialVelocity.normalize();
            initialVelocity.scale((float)INSERT_SPEED);


            Agent agent;
            if (agents.containsKey(id)) {
                agent = agents.get(id);
            } else {
                agent = createAgent(id, routeNavigator.getRoute());
            }

            RoadObject newRoadObject = new RoadObject(id, updateTime, agent.getNavigator().getLane().getIndex(), initialPosition, initialVelocity);
            agent.getNavigator().setMyLifeEnds(false);


            boolean isSafe = false;
            while (true) {
                if (isSafe(newRoadObject, agent.getNavigator())) {
                    isSafe = true;
                    break;
                } else if (routeNavigator.getLane().getLaneLeft() != null) {
                    routeNavigator.resetPointPtr();
                    routeNavigator.changeLaneLeft();
                    initialPosition.setX(routeNavigator.next().x);
                    initialPosition.setY(routeNavigator.next().y);
                    routeNavigator.setInitialPosition(new Point2f(initialPosition.x, initialPosition.y));
                    agent.setNavigator(routeNavigator);
                    agent.getNavigator().setMyLifeEnds(false);
                    newRoadObject = new RoadObject(id, updateTime, agent.getNavigator().getLane().getIndex(), initialPosition, initialVelocity);
                } else break;
            }

            if (/*it < numberOftryes*/isSafe) {
                routeNavigator.setInitialPosition(new Point2f(initialPosition.x, initialPosition.y));
                agent.setNavigator(routeNavigator);
                agent.getNavigator().setMyLifeEnds(false);
                getExperimentsData().vehicleCreation(id);
                updateCar(newRoadObject);
            } else {
                notInsertedVehicles.add(vehicle);
                removeAgent(newRoadObject.getId());
            }
        }
        while (notInsertedVehicles.peek() != null) {
            vehiclesForInsert.add(notInsertedVehicles.poll());
        }
    }

    private boolean isDeleted(RadarData object, int id) {
        for (RoadObject car : object.getCars()) {
            if (car.getId() == id)
                return false;
        }
        return true;
    }

    public boolean isSafe(RoadObject newRoadObject, RouteNavigator stateNavigator) {
        for (Map.Entry<Integer, RoadObject> obj : posCurr.entrySet()) {
            RoadObject entry = obj.getValue();
            if (!agents.containsKey(entry.getId())) return true;
            double distanceToSecondCar = Utils.getDistanceBetweenTwoRoadObjects(newRoadObject, roadNetwork.getActualPosition(newRoadObject.getPosition()), entry, roadNetwork.getActualPosition(entry.getPosition()), agents.get(entry.getId()).getNavigator().getFollowingEdgesInPlan());//entry.getPosition().distance(newRoadObject.getPosition());
            if (distanceToSecondCar < CHECKING_DISTANCE) {
                if (newRoadObject.getPosition().distance(entry.getPosition()) < SAFETY_RESERVE) return false;
                if (distanceToSecondCar < SAFETY_RESERVE) {
                    if (stateNavigator.getLane().getParentEdge() == agents.get(entry.getId()).getNavigator().getLane().getParentEdge() &&
                            stateNavigator.getLane() != agents.get(entry.getId()).getNavigator().getLane()
                            || stateNavigator.getLane().getParentEdge() != agents.get(entry.getId()).getNavigator().getLane().getParentEdge()
                            && newRoadObject.getPosition().distance(entry.getPosition()) > SAFETY_RESERVE) {
                        //continue
                    } else {
                        return false;
                    }
                }
                List<Edge> followingEdgesInPlan = agents.get(entry.getId()).getNavigator().getFollowingEdgesInPlan();
                for (Edge e : followingEdgesInPlan) {
                    if (stateNavigator.getLane().getParentEdge().equals(e)) {
                        double safedist = safeDistance(acceleration, entry.getVelocity().length(), INSERT_SPEED);
                        if (safedist + SAFETY_RESERVE >= distanceToSecondCar || newRoadObject.getPosition().distance(entry.getPosition()) < SAFETY_RESERVE) {
                            logger.warn("Vehicle could not be added to traffic at given time, because it was not safe. Vehicle will be added as soon as it is safe!");
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    private double safeDistance(double a0, double v0, double v1) {
        double safeDist = (v1 * v1 - v0 * v0) / (2 * a0);
        return safeDist;
    }

    public RadarData getCurrentRadarData() {
        return currentRadarData;
    }

    public int getCounter() {
        return counter;
    }

    private class QueueComparator implements Comparator<VehicleInitializationData> {
        @Override
        public int compare(VehicleInitializationData o1, VehicleInitializationData o2) {
            if (o1.getDepartureTime() < o2.getDepartureTime()) {
                return -1;
            }
            if (o1.getDepartureTime() > o2.getDepartureTime()) {
                return 1;
            }
            return 0;
        }
    }

    public HighwayEnvironment getHighwayEnvironment() {
        return highwayEnvironment;
    }

    public List<Edge> convertNodeRouteToEdgeRoute(List<Long> nodeRoute) {
        Iterator<Long> nodeIter = nodeRoute.iterator();
        List<Edge> edgeRoute = new ArrayList<>();
        long previousNodeId = 0;
        if (nodeIter.hasNext()) previousNodeId = nodeIter.next();
        while (nodeIter.hasNext()) {
            long nodeId = nodeIter.next();
            try {
                edgeRoute.addAll(highwayEnvironment.getRoadNetwork().getEdgeFromJunctions(previousNodeId, nodeId));
            } catch (PathNotFoundException e) {
                e.printStackTrace();
                return edgeRoute;
            }
            previousNodeId = nodeId;
        }
        return edgeRoute;
    }
}
