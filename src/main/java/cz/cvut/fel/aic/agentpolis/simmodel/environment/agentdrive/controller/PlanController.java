package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.controller;



import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Point3f;

/**
 *
 * @author ondra on 9.6. 2014
 */
public class PlanController{
    
    private LinkedList<Point3f> wayPoints;
    
    private final float SAFE_DIST_CONST = 0.5f;

    public PlanController() {
        wayPoints = new LinkedList<>();
    }

    public Point3f getNextWayPoint(Point3f pos, float velocity){
        if (wayPoints.isEmpty()) return null;
        if (isInMinDist(pos,velocity)){
            wayPoints.removeFirst();
            return getNextWayPoint(pos,velocity);
        }else{
            return wayPoints.getFirst();
        }
    }
    
    public LinkedList<Point3f> getWayPoints() {
        return wayPoints;
    }
    
    public List<Point3f> getNNextWayPoints(int n){
        if(n > wayPoints.size()){
            return wayPoints;
        }else{
            return wayPoints.subList(0, n);
        }
    }
    public List<Point3f> getAllNextWayPoints(){
            return wayPoints;
    }

    public void setWayPoints(LinkedList<Point3f> wayPoints) {
        this.wayPoints = wayPoints;
    }
    
    public boolean isInMinDist(Point3f pos,float velocity){
        return pos.distance(wayPoints.getFirst()) < velocity * 20/1000f + SAFE_DIST_CONST;
    }

    
}
