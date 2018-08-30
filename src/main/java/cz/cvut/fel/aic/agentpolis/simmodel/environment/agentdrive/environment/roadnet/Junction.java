package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;


import javax.vecmath.Point2f;
import java.util.ArrayList;

/**
 * Structure holding data about a junction loaded from sumo .net.xml file
 * Created by pavel on 19.6.14.
 */
public class Junction extends Sector {
    private final ArrayList<Request> requests;
    private Point2f center;
    private ArrayList<String> incLanes;
    private ArrayList<String> intLanes;
    private String agentpolisId;
    private Double lat;
    private Double lon;

    public Junction(String id, String type, Point2f center, ArrayList<String> incLanes,
                    ArrayList<String> intLanes, ArrayList<Point2f> shape, ArrayList<Request> requests, double lat, double lon) {
        super(id, type, shape);
        this.center = center;
        this.incLanes = incLanes;
        this.intLanes = intLanes;
        this.requests = requests;
        this.lat = lat;
        this.lon = lon;
        this.agentpolisId = computeAgentpolisId();
        System.out.println("x: " + agentpolisId);
    }

    public Point2f getCenter() {
        return center;
    }

    public ArrayList<String> getIncLanes() {
        return incLanes;
    }

    public ArrayList<String> getIntLanes() {
        return intLanes;
    }

    private String computeAgentpolisId() {
        long lon = (long) (this.lon * Math.pow(10, 6));
        long lat = (long) (this.lat * Math.pow(10, 6));
        if (lon < 0 && lat < 0) {
            return "1" + Long.toString(lon).substring(1) + Long.toString(lat).substring(1);
        } else if (lon < 0 && lat >= 0) {
            return "2" + Long.toString(lon).substring(1) + Long.toString(lat);
        } else if (lon >= 0 && lat < 0) {
            return "3" + Long.toString(lon) + Long.toString(lat).substring(1);
        } else {
            return Long.toString(lon) + Long.toString(lat);
        }
    }

    public long getAgentpolsId() {
        return Long.parseLong(agentpolisId);
    }
}
