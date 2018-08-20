package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan;

public class ActuatorsAction extends Action {
    private double steer;
    private double gas;
    private double brake;
    private double duration;

    public ActuatorsAction(int carId, double timeStamp, double steer, double gas, double brake,
            double duration) {
        super(carId, timeStamp);
        this.steer = steer;
        this.gas = gas;
        this.brake = brake;
        this.duration = duration;
    }

    public double getSteer() {
        return steer;
    }

    public double getGas() {
        return gas;
    }

    public double getBrake() {
        return brake;
    }

    public double getDuration() {
        return duration;
    }

}
