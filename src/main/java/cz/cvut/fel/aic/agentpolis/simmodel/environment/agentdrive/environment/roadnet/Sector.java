package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

import javax.vecmath.Point2f;
import java.util.ArrayList;

/**
 * Created by pavel on 19.6.14.
 */
public class Sector {
    private String id;
    private String type;
    private ArrayList<Point2f> shape;


    public Sector(String id, String type, ArrayList<Point2f> shape) {
        this.id = id;
        this.type = type;
        this.shape = shape;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Point2f> getShape() {
        return shape;
    }


}
