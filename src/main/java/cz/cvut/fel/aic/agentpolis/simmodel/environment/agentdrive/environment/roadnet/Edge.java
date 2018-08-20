package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;



import javax.vecmath.Point2f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Structure holding data about an edge loaded from sumo .net.xml file
 * Created by pavel on 19.6.14.
 */
public class Edge extends Sector {

    private final String from;
    private final String to;
    private final int priority;
    private final HashMap<String, LaneImpl> lanes;


    public Edge(String id, String from, String to, String priority, String type, ArrayList<Point2f> shape) {
        super(id, type, shape);
        this.from = from;
        this.to = to;
        this.priority = Integer.parseInt(priority);
        this.lanes = new HashMap<String, LaneImpl>();
    }

    public void putLanes(HashMap<String, LaneImpl> laneMap) {
        for (Map.Entry<String, LaneImpl> entry : laneMap.entrySet()) {
            entry.getValue().setEdge(this);
            lanes.put(entry.getKey(), entry.getValue());
        }
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getPriority() {
        return priority;
    }

    public HashMap<String, LaneImpl> getLanes() {
        return lanes;
    }

    /**
     * Returns lane by given index
     * @return null if the index is invalid
     */
    public LaneImpl getLaneByIndex(int laneIdx) {
        return getLanes().get(String.format("%s_%d", getId(), laneIdx));
    }


}
