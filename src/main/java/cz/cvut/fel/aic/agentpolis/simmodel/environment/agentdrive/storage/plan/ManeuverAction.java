package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan;

public class ManeuverAction extends Action {

    private double speed;
    private int lane;
    private double duration;

    public ManeuverAction(int carId, double timeStamp, double speed, int lane, double duration) {
        super(carId, timeStamp);
        this.speed = speed;
        this.lane = lane;
        this.duration = duration;
    }

    public double getSpeed() {
        return speed;
    }

    public int getLane() {
        return lane;
    }

    public double getDuration() {
        return duration;
    }

	@Override
	public String toString() {
		return "ManeuverAction [carId=" + getCarId() + ", timeStamp=" + getTimeStamp() + " speed=" + speed + ", lane=" + lane + ", duration=" + duration + "]";
	}
    
    

}
