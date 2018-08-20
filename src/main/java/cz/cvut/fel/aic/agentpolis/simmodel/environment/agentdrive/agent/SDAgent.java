package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.RandomProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.alite.configurator.Configurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SDAgent extends RouteAgent {

    protected static final Logger logger = Logger.getLogger(SDAgent.class);

    private final static double DISTANCE_TO_ACTIVATE_NM = Configurator.getParamDouble("highway.safeDistanceAgent.distanceToActivateNM", 300.0);
    private final static double SAFETY_RESERVE = Configurator.getParamDouble("highway.safeDistanceAgent.safetyReserveDistance", 4.0);
    private final static double MAX_SPEED = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.maximalSpeed", 70.0);
    private final static double MAX_SPEED_VARIANCE = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.maxSpeedVariance", 0.8);
    protected final static int CHECKING_DISTANCE = 500;
    private final static double LANE_SPEED_RATIO = Configurator.getParamDouble("highway.safeDistanceAgent.laneSpeedRatio", 0.1);
    private final static long PLANNING_TIME = 1000;
    protected int numberOfCollisions = 0;
    protected int num_of_lines;
    protected ActualLanePosition myActualLanePosition;
    protected CarManeuver currentManeuver = null;
    private HighwaySituation currentHighwaySituation = null;
    protected int edgeIndex = 0;

    // maximal speed after variance application
    private final double initialMaximalSpeed = (RandomProvider.getRandom().nextDouble() - 0.5) * 2 * MAX_SPEED_VARIANCE * MAX_SPEED + MAX_SPEED;
    protected double maximalSpeed = initialMaximalSpeed;
    protected double acceleration = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.deacceleration", -6.0);

    public List<Action> agentReact() {
        return super.agentReact(plan());
    }

    protected RoadNetwork roadNetwork;

    public SDAgent(int id, RoadNetwork roadNetwork) {
        super(id);
        this.roadNetwork = roadNetwork;
        logger.setLevel(Level.DEBUG);
        num_of_lines = navigator.getLane().getParentEdge().getLanes().keySet().size();
    }

    public HighwaySituation getHighwaySituation() {
        return currentHighwaySituation;
    }

    public CarManeuver plan() {
        CarManeuver maneuver = null;

        RoadObject currState = sensor.senseCurrentState();
        if (currState == null) {
            logger.debug("No plan for car " + this.getName());
            return null;
        }
        if (navigator.getLane() == null) {
            logger.debug("No plan for car " + this.getName());
            return null;
        }
        int myLaneIndex = navigator.getLane().getIndex();
        for (LaneImpl l : navigator.getLane().getParentEdge().getLanes().values()) {
            if (l.getInnerPoints().contains(new Point2f(currState.getPosition().x, currState.getPosition().y))) {
                myLaneIndex = l.getIndex();
            }
        }
        while (myLaneIndex < navigator.getLane().getIndex()) {
            navigator.changeLaneRight();
        }
        while (myLaneIndex > navigator.getLane().getIndex()) {
            navigator.changeLaneLeft();
        }

        // Simulator did not send update yet
        if (currState == null) {
            return null;
        }

        logger.debug("Startnode: " + currState);
        HighwaySituation situationPrediction = (HighwaySituation) getStateSpace(currState);
        logger.debug("SS: " + situationPrediction);
        logger.debug("Situation: " + situationPrediction);
        logger.debug("Navigator: " + navigator.getRoutePoint());

        int lane = currState.getLaneIndex();
        double velocity = currState.getVelocity().length();

        double distance = 0;
        long updateTime = (long) (currState.getUpdateTime() * 1000);
        CarManeuver acc = new AccelerationManeuver(lane, velocity, distance, updateTime);
        CarManeuver str = new StraightManeuver(lane, velocity, distance, updateTime);
        CarManeuver left = new LaneLeftManeuver(lane, velocity, distance, updateTime);
        CarManeuver right = new LaneRightManeuver(lane, velocity, distance, updateTime);
        CarManeuver dec = new DeaccelerationManeuver(lane, velocity, distance, updateTime);

        int preferredLane = getPreferredLane(currState);

        if (isNarrowingMode(currState)) {
            logger.debug("PreferredLane: " + preferredLane);
            logger.info("Narrowing mode activated");
            CarManeuver preferredMan = null;
            if (preferredLane < currState.getLaneIndex()) {
                preferredMan = right;
            } else if (preferredLane > currState.getLaneIndex()) {
                preferredMan = left;
            }
            if (preferredMan != null && isSafeMan(currState, preferredMan, situationPrediction)) {
                maneuver = preferredMan;
            } else if (isSafeMan(currState, acc, situationPrediction)) {
                maneuver = acc;
            } else if (isSafeMan(currState, str, situationPrediction)) {
                maneuver = str;
            } else if (isSafeMan(currState, dec, situationPrediction)) {
                maneuver = dec;
            } else {
                maneuver = dec;
            }
        } else { // Not narrowingMode
            //performing changing lane?
            if (currentManeuver != null && currentManeuver.getLaneIn() != currentManeuver.getLaneOut() && currentManeuver.getClass().equals(LaneLeftManeuver.class) && isSafeMan(currState, left, situationPrediction)) {
                maneuver = left;
            } else if (currentManeuver != null && currentManeuver.getLaneIn() != currentManeuver.getLaneOut() && currentManeuver.getClass().equals(LaneRightManeuver.class) && isSafeMan(currState, right, situationPrediction)) {
                maneuver = right;
            } else {
                Junction myNearestJunction = roadNetwork.getJunctions().get(navigator.getLane().getParentEdge().getTo());
                if (isSafeMan(currState, right, situationPrediction)) {
                    maneuver = right;
                } else if (isSafeMan(currState, acc, situationPrediction)) {
                    maneuver = acc;
                } else if (isSafeMan(currState, str, situationPrediction)) {
                    maneuver = str;
                } else if (isSafeMan(currState, left, situationPrediction)) {
                    maneuver = left;
                } else if (isSafeMan(currState, dec, situationPrediction)) {
                    maneuver = dec;
                } else {
                    logger.info("Nothing is safe, shouldn't happen!");
                    maneuver = dec;
                }
            }
        }
        logger.info("Planned maneuver for carID " + currState.getId() + " " + maneuver);
        currentManeuver = maneuver;
        currentHighwaySituation = situationPrediction;
        return maneuver;
    }

    @Deprecated
    private double transGeoToDistance(double x, double y) {
        return sensor.getRoadDescription().distance(new Point2d(x, y));
    }

    private int getPreferredLane(RoadObject startNode) {
        //TODO: finding shortest lane may be arguably better
        return startNode.getLaneIndex();
    }

    private boolean isNarrowingMode(RoadObject state) {
        if (Configurator.getParamBool("highway.safeDistanceAgent.narrowingModeActive", false).equals(false)) {
            return false;
        }
        Junction myNearestJunction = roadNetwork.getJunctions().get(myActualLanePosition.getEdge().getTo());
        Point2f junctionWaypoint = myNearestJunction.getCenter();
        double distance = junctionWaypoint.distance(Utils.convertPoint3ftoPoint2f(state.getPosition()));
        return distance < DISTANCE_TO_ACTIVATE_NM;
    }

    private boolean isLaneGoingOn(double position, double distance, int lane) {
        Collection<RoadObject> obstacles = sensor.senseObstacles();
        for (RoadObject obs : obstacles) {
            if (obs.getLaneIndex() != lane)
                continue;
            double obsDist = obs.getPosition().distance(new Point3f(0, 0, 0));
            if (obsDist > position && obsDist < position + distance) {
                return false;
            }
        }
        return true;
    }

    protected boolean isSafeMan(RoadObject state, CarManeuver man, HighwaySituation situation) {
        boolean narrowingMode = isNarrowingMode(state);

        if (isHighwayCollision(state, man)) {
            logger.info(state + " Highway Collision detected!" + man);
            return false;
        }
        if (isRulesCollision(man)) {
            logger.info("Rules Collision detected! " + man);
            return false;

        }
        HighwaySituation emptySituation = new HighwaySituation();
        emptySituation.clear();
        if (!isSafe(man, emptySituation)) {
            logger.info("Empty situation is not safe");
            return false;
        }
        if (man.getClass().equals(AccelerationManeuver.class)
                || man.getClass().equals(StraightManeuver.class)) {

            return isInSafeDistance(situation.getCarAheadMan(), man)// sufficient if not narrowing
                    && (!narrowingMode || (isInSafeDistance(situation.getCarLeftAheadMan(), man) || (situation
                    .getCarLeftAheadMan() != null && situation.getCarLeftAheadMan()
                    .getVelocityOut() == 0.0))
                    && ((isInSafeDistance(
                    situation.getCarRightAheadMan(), man) || (situation
                    .getCarRightAheadMan() != null && situation
                    .getCarRightAheadMan().getVelocityOut() == 0.0))));

        } else if (man.getClass().equals(LaneLeftManeuver.class)) {
            logger.info("Is in safe distance if LaneLeftManeuver: " + isInSafeDistance(situation.getCarLeftAheadMan(), man) + " " + isInSafeDistance(man, situation.getCarLeftMan()));
            return isInSafeDistance(situation.getCarLeftAheadMan(), man)
                    && isInSafeDistance(man, situation.getCarLeftMan());
        } else if (man.getClass().equals(LaneRightManeuver.class)) {
            logger.info("Is in safe distance if LaneRightManeuver: " + isInSafeDistance(situation.getCarRightAheadMan(), man) + " " + isInSafeDistance(man, situation.getCarRightMan()));
            return isInSafeDistance(situation.getCarRightAheadMan(), man)
                    && isInSafeDistance(man, situation.getCarRightMan());
        } else {
            return true;
        }

    }

    private boolean isHighwayCollision(RoadObject state, CarManeuver man) {
        if (!canPerformManeuver(man)) {
            return true;
        } else if (!isLaneGoingOn(man.getPositionOut(), safeDistance(man, SAFETY_RESERVE), state.getLaneIndex())) {
            return true;
        } else
            return false;
    }

    private double safeDistance(CarManeuver man, double safetyReserve) {
        double a0 = acceleration;
        double v0 = man.getVelocityOut();
        double v1 = 0;
        return safeDistance(a0, v0, v1, safetyReserve);
    }

    private double safeDistance(CarManeuver manAhead, CarManeuver manBehind, double safetyReserve) {
        double a0 = acceleration;
        double v0 = manBehind.getVelocityOut();
        double v1 = manAhead.getVelocityIn();
        double safeDist = safeDistance(a0, v0, v1, safetyReserve);
        return safeDist;
    }

    private double safeDistance(double a0, double v0, double v1) {
        double safeDist = (v1 * v1 - v0 * v0) / (2 * a0);
        return safeDist;
    }

    private double safeDistance(double a0, double v0, double v1, double safetyReserve) {
        double safeDist = safeDistance(a0, v0, v1) + safetyReserve;
        return safeDist;
    }

    private boolean isInSafeDistance(CarManeuver manAhead, CarManeuver manBehind) {
        if (manAhead == null || manBehind == null) return true;
        double realDist = manAhead.getPositionIn() - manBehind.getPositionOut();
        if (realDist < 0) return false; // maneuvers planned position out is before me.
        if (safeDistance(manAhead, manBehind, SAFETY_RESERVE) < realDist) return true;
        else return false;
    }

    public ArrayList<CarManeuver> getStateSpace(RoadObject state) {
        Collection<RoadObject> cars = sensor.senseCars();
        long planningStartTime = sensor.getCurrentTime();
        return generateSS(state, cars, planningStartTime, planningStartTime + PLANNING_TIME);
    }

    public boolean isCollision(CarManeuver carManeuver, ArrayList<CarManeuver> stateSpace) {
        for (int i = 0; i < stateSpace.size(); i++) {
            if (isCollision(carManeuver, stateSpace.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollision(CarManeuver carManeuver, CarManeuver carManeuver2) {

        if (isTimeIntersect(carManeuver, carManeuver2)
                && isPositionIntersect(carManeuver, carManeuver2)) {
            return true;
        } else
            return false;
    }

    public boolean isPositionIntersect(CarManeuver a, CarManeuver b) {
        if (a.getPositionIn() > b.getPositionOut() || b.getPositionIn() > a.getPositionOut()) {
            return false;
        }
        return a.getLaneIn() == b.getLaneIn() || a.getLaneIn() == b.getLaneOut()
                || a.getLaneOut() == b.getLaneIn() || a.getLaneOut() == b.getLaneOut();

    }

    public boolean isTimeIntersect(CarManeuver carManeuver, CarManeuver carManeuver2) {
        long ret = getTimeConflict(carManeuver, carManeuver2);
        return ret != -1;
    }

    /*
     * Cooperative method returns conflictTime, if a conflict is found else return -1
     */
    public long getTimeConflict(CarManeuver carManeuver, CarManeuver carManeuver2) {
        long car1Start = carManeuver.getStartTime();
        long car1End = carManeuver.getEndTime();
        long car2Start = carManeuver2.getStartTime();
        long car2End = carManeuver2.getEndTime();

        if (carManeuver2.isInfiniteInTime())
            car2End = Long.MAX_VALUE;

        if ((car1Start > car2End) || (car2Start > car1End))
            return -1;
        else {
            if (car1Start > car2Start)
                return car1Start;
            else
                return car2Start;
        }
    }

    private boolean isSafe(CarManeuver carManeuver, ArrayList<CarManeuver> stateSpace) {
        CarManeuver man = new DeaccelerationManeuver(carManeuver);
        boolean ret = true;
        while (man.getVelocityOut() != 0.0) {
            if (isCollision(man, stateSpace)) {
                ret = false;
                break;
            }
            man = new DeaccelerationManeuver(man);
        }
        return ret;
    }


    private boolean laneChange(CarManeuver carManeuver) {
        return carManeuver.getClass().equals(LaneLeftManeuver.class) || carManeuver.getClass().equals(LaneRightManeuver.class);
    }

    private boolean isRulesCollision(CarManeuver carManeuver) {
        if (laneChange(carManeuver)) {
            double minSpeedToTurn = 2;
            if (carManeuver.getVelocityIn() < minSpeedToTurn) {
                return true;
            }
        }
        double laneMaxSpeed = maximalSpeed + carManeuver.getLaneIn() * LANE_SPEED_RATIO * maximalSpeed;
        logger.info("laneMasSpeed= " + laneMaxSpeed + " maximalSpeed=" + maximalSpeed + " speedVariance=" + MAX_SPEED_VARIANCE + " MAX_SPEED conf = " + MAX_SPEED);
        return carManeuver.getVelocityOut() > laneMaxSpeed;
    }

    public boolean canPerformManeuver(CarManeuver carManeuver) {
        // always allow a finishing maneuver
        if (carManeuver.isFinishing()) {
            return true;
        }

        int laneOut = carManeuver.getExpectedLaneOut();
        if (laneOut < 0) {
            logger.debug("LaneOut= " + laneOut);
            return false;
        }
        if (laneOut >= navigator.getLane().getParentEdge().getLanes().keySet().size()) {
            logger.debug("laneOut = " + laneOut);
            return false;
        }

        return true;
    }


    public HighwaySituation generateSS(RoadObject state, Collection<RoadObject> cars, long from, long to) {

        HighwaySituation situationPrediction = new HighwaySituation();
        for (int i = 0; i < navigator.getRoute().size(); i++) {
            if (navigator.getRoute().get(i) == navigator.getLane().getParentEdge()) {
                edgeIndex = i;
                logger.debug("Index of an edge in a route is " + edgeIndex);
                break;
            }
        }
        logger.debug("GenerateSS for " + state.getId());
        ActualLanePosition temp = myActualLanePosition;
        myActualLanePosition = roadNetwork.getActualPosition(state.getPosition());
        if (!checkCorrectRoute()) {
            myActualLanePosition = temp;
        }
        navigator.setActualPosition(myActualLanePosition);
        Lane myLane = myActualLanePosition.getLane();
        Edge myEdge = myActualLanePosition.getEdge();

        logger.debug("myLane = " + myLane.getIndex());
        num_of_lines = myEdge.getLanes().size();
        int myIndexOnRoute = myActualLanePosition.getIndex();

        //removing too far cars and myself from the collection

        // Main logic, first there is a check if there is a junction nearby. If so the junction mode is enabled. If not,
        //vehicle is driven by standard Safe-distance agent
        Lane entryLane;
        ActualLanePosition entryActualLanePosition;
        Junction myNearestJunction = roadNetwork.getJunctions().get(myEdge.getTo());
        countCollisions(cars, state);
        for (RoadObject entry : cars) {
            ArrayList<CarManeuver> predictedManeuvers;
            entryActualLanePosition = roadNetwork.getActualPosition(entry.getPosition());
            entryLane = entryActualLanePosition.getLane();
            if (myNearestJunction.equals(roadNetwork.getJunctions().get(entryLane.getParentEdge().getTo()))) {
                // if operating vehicle and other vehicle is heading to the same junction
                // other vehicle is heading to the same junction but the junction is not close.
                //this is the classical Safe-distance method, all five states can be set.
                if (entryLane.getParentEdge().equals(myEdge)) {
                    predictedManeuvers = getPlannedManeuvers(state, myActualLanePosition, entry, entryActualLanePosition, from, to, null);
                    situationPrediction.addAll(predictedManeuvers);
                    CarManeuver man = predictedManeuvers.get(0);
                    int entryNearestWaipoint = entryActualLanePosition.getIndex(); // finding nearest waipoint on the road.
                    if (myLane.getLaneId().equals(entryLane.getLaneId())) {
                        if (entryNearestWaipoint > myIndexOnRoute) {
                            situationPrediction.trySetCarAheadManeuver(man);
                        }
                    } else {
                        if (entryNearestWaipoint < myIndexOnRoute) {
                            if (myLane.getIndex() - entryLane.getIndex() == -1) {
                                situationPrediction.trySetCarLeftMan(man);
                            } else if (myLane.getIndex() - entryLane.getIndex() == 1) {
                                situationPrediction.trySetCarRightMan(man);
                            }
                        } else {
                            if (myLane.getIndex() - entryLane.getIndex() == -1) {
                                situationPrediction.trySetCarLeftAheadMan(man);
                            } else if (myLane.getIndex() - entryLane.getIndex() == 1) {
                                situationPrediction.trySetCarRightAheadMan(man);
                            }
                        }
                    }
                }
            }
            if (!entryLane.getParentEdge().equals(myEdge)) { // This is for checking vehicles behind the junction.
                List<Edge> remE = navigator.getFollowingEdgesInPlan();
                for (Edge planned : remE) {
                    if (planned.equals(entryLane.getParentEdge())) {
                        if (Utils.getDistanceBetweenTwoRoadObjects(state, myActualLanePosition, entry, entryActualLanePosition, remE) > CHECKING_DISTANCE) {
                            continue;
                        }
                        predictedManeuvers = getPlannedManeuvers(state, myActualLanePosition, entry, entryActualLanePosition, from, to, remE);
                        CarManeuver man = predictedManeuvers.get(0);
                        situationPrediction.trySetCarAheadManeuver(man);
                        situationPrediction.trySetCarLeftAheadMan(man);
                        situationPrediction.trySetCarRightAheadMan(man);
                    }
                }
            }
        }
        return situationPrediction;
    }

    private double getEclideanDistanceBetweenRoadObjectAndPoint2f(RoadObject state, Point2f edgeBeginPoint) {
        return sensor.getRoadDescription().distance(new Point2d(state.getPosition().x, state.getPosition().y), edgeBeginPoint);
    }

    protected boolean checkCorrectRoute() {
        if (!navigator.getRoute().contains(myActualLanePosition.getEdge())) {
            logger.warn("Agent " + getName() + " is on a route it should not be!  ");

            /*
            This can happen when a car is crossing a junction and method getActualPosition return position
            on a lane it's just crossing, but this lane is not connected to actual edge car is trying to get.
             */
            if (edgeIndex + 1 < navigator.getRoute().size()) {
                myActualLanePosition = new ActualLanePosition(navigator.getRoute().get(edgeIndex + 1).getLaneByIndex(0), myActualLanePosition.getIndex()+1);

            } else {
                myActualLanePosition = new ActualLanePosition(navigator.getRoute().get(edgeIndex).getLaneByIndex(0), myActualLanePosition.getIndex()+1);
            }
            return false;
        }
        return true;
    }

    /**
     * This method is for measuring number of collisons by HighwayStorage.
     *
     * @return number of vehicle's collisions
     */
    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }

    public ArrayList<CarManeuver> getPlannedManeuvers(RoadObject me, ActualLanePosition myActualLanePosition, RoadObject car, ActualLanePosition otherActualLanePosition, long from, long to, List<Edge> rem) {

        ArrayList<CarManeuver> plan = new ArrayList<CarManeuver>();
        CarManeuver lastManeuver;
        lastManeuver = new StraightManeuver(car.getLaneIndex(), car.getVelocity().length(),
                Utils.getDistanceBetweenTwoRoadObjects(me, myActualLanePosition, car, otherActualLanePosition, rem), (long) (car.getUpdateTime() * 1000));
        plan.add(lastManeuver);
        while (lastManeuver.getEndTime() <= to) {
            lastManeuver = new StraightManeuver(lastManeuver);
            plan.add(lastManeuver);
        }

        return plan;
    }

    public CarManeuver getCurrentManeuver() {
        return currentManeuver;
    }

    private void countCollisions(Collection<RoadObject> cars, RoadObject state) {
        for (RoadObject entry : cars) {
            float distanceToSecondCar = entry.getPosition().distance(state.getPosition());
            if (distanceToSecondCar > CHECKING_DISTANCE || state.getPosition().equals(entry.getPosition())) {
                continue;
            } else {
                if (distanceToSecondCar < 2.24) {
                    logger.info("Collision between " + state.getId() + " and " + entry.getId());
                    numberOfCollisions++;
                }
            }
        }
    }
}
