package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;


import ags.utils.dataStructures.KdTree;
import ags.utils.dataStructures.SquareEuclideanDistanceFunction;
import ags.utils.dataStructures.utils.MaxHeap;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.NetworkLocation;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class holding the network data
 * It provides the following data: edges, junctions, lanes, connections, tunnels, bridges
 * It also provides a converter from x,y coordinates to a specific lane
 * <p/>
 */
public class Network implements RoadNetwork {
    private NetworkLocation networkLocation;
    private HashMap<String, Edge> edges;
    private HashMap<String, Junction> junctions;
    private HashMap<String, LaneImpl> lanes;
    private KdTree<ActualLanePosition> kdTree;
    private SquareEuclideanDistanceFunction distanceFunction;
    private ArrayList<Connection> connections;
    private ArrayList<String> tunnels;
    private ArrayList<String> bridges;


//    public static synchronized Network getInstance() {
//        if (instance == null) {
//            instance = new Network();
//        }
//        return instance;
//    }


    /**
     * call this method to initialize the network structure
     *
     * @param edges
     * @param junctions
     * @param laneMap
     * @param connectionList
     * @param tunnelsRaw
     * @param bridgesRaw
     */
    public Network(NetworkLocation networkLocation, HashMap<String, Edge> edges,
                   HashMap<String, Junction> junctions, HashMap<String, LaneImpl> laneMap,
                   ArrayList<Connection> connectionList, ArrayList<String> tunnelsRaw, ArrayList<String> bridgesRaw) {
        this.networkLocation = networkLocation;
        this.edges = edges;
        this.junctions = junctions;
        this.lanes = laneMap;
        this.connections = connectionList;
        tunnels = createTunnelsAndBridges(tunnelsRaw);
        bridges = createTunnelsAndBridges(bridgesRaw);


        connectLanes();
        fillKdTree();
    }

    /**
     * converts raw data about bridges and tunnels from the osm file
     * to lists of edges which are actual bridges and tunnels in the network representation
     *
     * @param dataRaw
     * @return
     */
    private ArrayList<String> createTunnelsAndBridges(ArrayList<String> dataRaw) {
        ArrayList<String> ret = new ArrayList<String>();
        for (String t : dataRaw) {
            for (String k : edges.keySet()) {
                if (k.contains(t)) {
                    ret.add(k);
                }
            }
        }
        return ret;
    }


    /**
     * connects the created lanes using connections
     */
    private void connectLanes() {
        for (Connection c : connections) {
            lanes.get(createLaneId(c.getFrom(), c.getFromLane())).addOutgoingLane(lanes.get(createLaneId(c.getTo(), c.getToLane())));
            lanes.get(createLaneId(c.getTo(), c.getToLane())).addIncomingLane(lanes.get(createLaneId(c.getFrom(), c.getFromLane())));
        }
    }

    private String createLaneId(String edgeId, String laneIndex) {
        return edgeId + "_" + laneIndex;
    }


    /**
     * creates a kd-Tree and fills it with point - lane pairs
     * The kd-Tree is used for fast look up of the cars current lane based on its x,y coordinates only
     */
    private void fillKdTree() {
        kdTree = new KdTree(2);
        this.distanceFunction = new SquareEuclideanDistanceFunction();

        for (Map.Entry<String, LaneImpl> entry : lanes.entrySet()) {
            for (int i = 0; i < entry.getValue().getInnerPoints().size(); i++) {
                Point2f p = entry.getValue().getInnerPoints().get(i);
                double[] point = new double[2];
                point[0] = p.x;
                point[1] = p.y;
                kdTree.addPoint(point, new ActualLanePosition(entry.getValue(), i));
            }
        }
    }

    /**
     * returns the cars current lane based on its x,y coordinates
     * uses kd-Tree to obtain nearest neighbours of the given point
     *
     * @param position
     * @return
     */
    public Lane getLane(Point2f position) {
        double[] point = new double[2];
        point[0] = position.x;
        point[1] = position.y;
        MaxHeap<ActualLanePosition> nearestNeighbour = kdTree.findNearestNeighbors(point, 1, distanceFunction);
        return nearestNeighbour.getMax().getLane();
    }

    public ActualLanePosition getActualPosition(Point3f position) {
        double[] point = new double[2];
        point[0] = position.x;
        point[1] = position.y;
        MaxHeap<ActualLanePosition> nearestNeighbour = kdTree.findNearestNeighbors(point, 1, distanceFunction);
        return nearestNeighbour.getMax();
    }

    public ArrayList<ActualLanePosition> getTwoActualLanePositions(Point3f position) {
        ArrayList<ActualLanePosition> kdlanes = new ArrayList<ActualLanePosition>(2);
        double[] point = new double[2];
        point[0] = position.x;
        point[1] = position.y;
        for (ActualLanePosition actualLanePosition : kdTree.getNearestNeighborIterator(point, 2, distanceFunction)) {
            kdlanes.add(actualLanePosition);
        }
        return kdlanes;
    }

    /**
     * returns the cars current lane based on its x,y coordinates
     * uses kd-Tree to obtain nearest neighbours of the given point
     *
     * @param position
     * @return
     */
    public Lane getClosestLane(Point3f position) {
        Point2f pos2d = new Point2f(position.x, position.y);
        return getLane(pos2d);
    }

    public int getLaneNum(Point3f position) {
        return getClosestLane(position).getIndex();
    }

    @Override
    public NetworkLocation getNetworkLocation() {
        return networkLocation;
    }

    public HashMap<String, Edge> getEdges() {
        return edges;
    }

    public HashMap<String, Junction> getJunctions() {
        return junctions;
    }

    public ArrayList<String> getBridges() {
        return bridges;
    }

    public HashMap<String, LaneImpl> getLanes() {
        return lanes;
    }

    public ArrayList<String> getTunnels() {
        return tunnels;
    }

    public Junction getOriginOfEdge(Edge e) {
        return junctions.get(e.getFrom());
    }
}
