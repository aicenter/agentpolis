package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan;

import java.text.DecimalFormat;

public abstract class Action {

    private final int carId;
    private final double timeStamp;
    
    public Action(int carId, double timeStamp) {
        this.carId = carId;
        this.timeStamp = timeStamp;
        
    }
 
    public int getCarId() {
        return carId;
    }

    
    public double getTimeStamp() {
        return timeStamp;
    }
   

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0");
        return "Action [carId=" + carId + ", timeStamp = "+timeStamp;
    }

}
