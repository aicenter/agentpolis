package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan;

import javax.vecmath.Point3f;

public class WPAction extends Action {

    private Point3f position;
    private double speed;

    public WPAction(int carId, double timeStamp, Point3f position,double speed) {
        super(carId, timeStamp);
        this.position = position;
        this.speed = speed ;
    }
    
    public Point3f getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }
    @Override
    public String toString(){
        return super.toString()+", pos: "+position+", speed:"+speed;
    }

}
