package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.ActualLanePosition;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Edge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Lane;
import org.apache.log4j.Logger;

import javax.vecmath.Point2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for car navigation on given route
 */
public class RouteNavigator {

    private final Logger logger = Logger.getLogger(RouteNavigator.class);

    private int CP_pointPtr;
    private int CP_routePtr;
    private Lane CP_agentLane;
    private boolean CP_myLifeEnds;
    private int pointPtr;
    private int routePtr;
    private Lane agentLane;
    private boolean myLifeEnds;
    private Point2f initialPosition;

    /// Route represented as a list of edges, that the car should visit
    private List<Edge> route;
    private boolean isInitialized = false;

    public RouteNavigator(List<Edge> route) {
        this.setRoute(route);
    }

    private boolean initialize() {
        if (route == null || route.isEmpty()) {
            logger.warn("Cannot initialize without a proper route set!");
            return false;
        } else {
            routePtr = 0;
            System.out.println(route);
            agentLane = route.get(0).getLaneByIndex(0);
            initialPosition = agentLane.getInnerPoints().get(0);
            myLifeEnds = false;
            return true;
        }
    }

    public void reset() {
        resetPointPtr();
        initialize();
    }

    public void resetPointPtr() {
        pointPtr = 0;
    }
//    public void hardReset()
//    {
//        pointPtr = 0;
//        route = new ArrayList<Edge>();
//        myLifeEnds = false;
//        if (!Configurator.getParamBool("highway.dashboard.sumoSimulation",true) && Configurator.getParamBool("highway.rvo.agent.randomRoutes", true).equals(true)) {
//            setRoute();
//        }
//        else
//        {
//            setRoute(id);
//        }
//    }


    private void setRoute(List<Edge> routeToDrive) {
        this.route = routeToDrive;
        isInitialized = this.initialize();

    }
//    private void setRoute() {
//        Network network = Network.getInstance();
//        XMLReader reader = XMLReader.getInstance();
//        Map<Integer, List<String>> routes = reader.getRoutes(routeFile);
//        Map<String, Edge> edges = network.getEdges();
//
//        Random rand = new Random();
//        Object[] values = routes.values().toArray();
//        List<String> randomValue = (List<String>)values[rand.nextInt(values.length)];
//        //int id = rand.nextInt(routes.size()-1);
//        for (String edge : /*routes.get(id)*/ randomValue) {
//            route.add(edges.get(edge));
//        }
//        routePtr = 0;
//        agentLane = route.get(0).getLaneByIndex(0);
//    }

    public void changeLaneLeft() {
        Lane leftLane = agentLane.getLaneLeft();
        if (leftLane != null) {
            agentLane = leftLane;
        }
    }

    public void changeLaneRight() {
        Lane rightLane = agentLane.getLaneRight();
        if (rightLane != null) {
            agentLane = rightLane;
        }
    }

    /**
     * Method for advancing in route. First it checks if there is the end of the lane, if it is than try to switch lanes.
     * if this does not succeed than tries to switch to the another edge.
     */
    public void advanceInRoute() {
        if (!isInitialized) {
            logger.error("not initialized");
            return;
        }

        if (pointPtr >= agentLane.getInnerPoints().size() - 1) {
            // We are at the end of the lane
            if (routePtr >= route.size() - 1) { // end of the plan
//                 routePtr = -1;
//                 pointPtr = -1;
//                  agentLane = route.get(0).getLaneByIndex(0);
//                  agentLane = null;
                myLifeEnds = true;
            } else {
                Lane nextLane = getNeighbourLane(); //check for neighbour lane

                int desiredPoint = getDesiredNeighbourLinePoint(nextLane);
                if (desiredPoint == -1)  //neighbour lane is shorter than my lane or does not exist
                {
                    nextLane = getFollowingLane(route.get(routePtr + 1));
                    if (nextLane != null) {
                        pointPtr = 0;
                        routePtr++;
                        agentLane = nextLane;
                    } else {
                        myLifeEnds = true;
                    }
                } else {
                    pointPtr = desiredPoint;
                    agentLane = nextLane;
                }
            }
        } else {
            pointPtr++;
        }
    }

    private int getDesiredNeighbourLinePoint(Lane nextLane) {
        if (nextLane == null) return -1;
        int idealDistanceAroundMe = 4;
        int ii = 0;
        Point2f pp = agentLane.getInnerPoints().get(pointPtr);

        while (pp.distance(nextLane.getInnerPoints().get(ii)) > idealDistanceAroundMe) {
            ii++;
            if (ii == nextLane.getInnerPoints().size()) return -1;

        }
        while (pp.distance(nextLane.getInnerPoints().get(ii)) <= idealDistanceAroundMe) {
            ii++;
            if (ii == nextLane.getInnerPoints().size()) return -1;
        }
        return ii;
    }

    public Lane getNeighbourLane() {
        Lane neighbourLane = agentLane.getLaneLeft();
        if (neighbourLane == null) {
            // Try right lane
            neighbourLane = agentLane.getLaneRight();
        }
        return neighbourLane;
    }

    private Lane getFollowingLane(Edge edge) {
        Lane nextLane = agentLane.getNextLane(edge);
        if (nextLane == null) {
            // Lane doesn't continue to the edge in route, maybe we should change lane
            // Try left lane
            Lane changeLane = agentLane.getLaneLeft();
            // nextLane = changeLane;
            while (changeLane != null) {
                nextLane = changeLane.getNextLane(edge);
                if (nextLane != null) {
                    break;
                }
                changeLane = changeLane.getLaneLeft();
            }

            if (nextLane == null) {
                // Try right lane
                changeLane = agentLane.getLaneRight();
                while (changeLane != null) {
                    nextLane = changeLane.getNextLane(edge);
                    if (nextLane != null) {
                        break;
                    }
                    changeLane = changeLane.getLaneRight();
                }
            }
        }

        return nextLane;
    }

    public Point2f getRoutePoint() {
        if (isInitialized) {
            if (pointPtr >= agentLane.getInnerPoints().size()) pointPtr = agentLane.getInnerPoints().size() - 1;
            return agentLane.getInnerPoints().get(pointPtr);
        } else {
            logger.warn("not initialized!");
            return null;
        }
    }

    public int getRoutePtr() {
        return routePtr;
    }

    public Point2f getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Point2f initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Vector3f getInitialVelocity() {
        Point2f p1 = route.get(0).getLanes().values().iterator().next().getInnerPoints().get(0);
        Point2f p2 = route.get(0).getLanes().values().iterator().next().getInnerPoints().get(1);
        return new Vector3f(p2.x - p1.x, p2.y - p1.y, 0);
    }

    public Point2f next() {
        Point2f p = getRoutePoint();
        advanceInRoute();
        return p;
    }

    public Point2f nextWithReset() {
        int OLDpointPtr = pointPtr;
        int OLDroutePtr = routePtr;
        Lane OLDagentLane = agentLane;
        Point2f p = getRoutePoint();
        advanceInRoute();
        pointPtr = OLDpointPtr;
        routePtr = OLDroutePtr;
        agentLane = OLDagentLane;
        return p;
    }

    public void setCheckpoint() {
        CP_agentLane = agentLane;
        CP_pointPtr = pointPtr;
        CP_routePtr = routePtr;
    }

    public void resetToCheckpoint() {
        agentLane = CP_agentLane;
        pointPtr = CP_pointPtr;
        routePtr = CP_routePtr;
        myLifeEnds = CP_myLifeEnds;
    }

    public String getUniqueLaneIndex() {
        return agentLane.getLaneId();
    }

    public Lane getLane() {
        return agentLane;
    }

    public int getActualPointer() {
        return pointPtr;
    }

    public void setActualPosition(ActualLanePosition myLanePosition) {
        if (myLanePosition.getEdge().equals(agentLane.getParentEdge())) {
            agentLane = myLanePosition.getLane();
            this.pointPtr = myLanePosition.getIndex();
        } else {
            agentLane = myLanePosition.getLane();
            this.pointPtr = myLanePosition.getIndex();
            routePtr++;
        }
    }

    public List<Edge> getFollowingEdgesInPlan() {
        List<Edge> rem = new ArrayList<Edge>();
        int maxNumber = 5;
        for (int i = routePtr + 1; i < route.size() && i < maxNumber; i++) {
            rem.add(route.get(i));
        }
        return rem;
    }

    public List<Edge> getCommitedEdgesInPlan() {
        return route.subList(0, Math.min(routePtr + 1, route.size()));
    }

    public Integer getFirstUncommitedEdgeIndex() {
        return Math.min(routePtr + 2, route.size());
    }

    public boolean updatePlan(List<Edge> newPlanEdges) {
        List<Edge> newRoute = new ArrayList<>(route.subList(0, routePtr + 1));
        int newPlanIndex = 0;
        int index = getFirstUncommitedEdgeIndex();
        if (areConnected(route.get(index - 1), newPlanEdges.get(newPlanIndex))) {
            newRoute.addAll(newPlanEdges.subList(newPlanIndex, newPlanEdges.size()));
            return true;
        } else {
            for (int i = index; i < route.size(); i++) {
                if (areConnected(route.get(i), newPlanEdges.get(newPlanIndex))) {
                    if (index == i) {
                        newRoute.add(route.get(index));
                    } else {
                        newRoute.addAll(route.subList(index, i));
                    }
                    newRoute.addAll(newPlanEdges);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areConnected(Edge from, Edge to) {
        return from.getTo().equals(to.getFrom());
//        for (Lane l : from.getLanes().values()){
//            for (Lane incL : l.getOutgoingLanes()){
//                if (incL.getParentEdge().getId().equals(to.getId())){
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    public List<Edge> getRoute() {
        return route;
    }

    public boolean isMyLifeEnds() {
        return myLifeEnds;
    }

    public void setMyLifeEnds(boolean myLifeEnds) {
        this.myLifeEnds = myLifeEnds;
    }


}
