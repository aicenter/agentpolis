package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan;

import java.text.DecimalFormat;

public class ComboAction extends Action {

    private final int carId;
    private final double speed;
    private final int lane;
    private final double duration;
    private final double timeStamp;
    private double steer;
    private double gas;
    private double brake;
    private double time;
    private double wpx;
    private double wpy;

    public ComboAction(int carId, double speed, int lane, double duration, double timeStamp) {
        super(carId, timeStamp);
        this.carId = carId;
        this.speed = speed;
        this.lane = lane;
        this.duration = duration;
        this.timeStamp = timeStamp;
        this.steer = 0;
        this.gas = 0;
        this.brake = 0;
        this.time = 0;
    }

    public ComboAction(int vehicleId, double speed2, int lane2, int duration2, double startTime,
            double steer, double gas, double brake, double time) {
        this(vehicleId, speed2, lane2, duration2, startTime);
        this.steer = steer;
        this.gas = gas;
        this.brake = brake;
        this.time = time;
    }

    public ComboAction(int vehicleId, double speed2, int lane2, int duration2, double startTime,
              double wpx, double wpy) {
        this(vehicleId, speed2, lane2, duration2, startTime);
       
        this.time = time;
        this.wpx = wpx;
        this.wpy = wpy;
    }

    public int getCarId() {
        return carId;
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

    public double getTimeStamp() {
        return timeStamp;
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

    public double getTime() {
        return time;
    }

    public double getWpx() {
        return wpx;
    }

    public double getWpy() {

        return wpy;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0");
        return "Action [carId=" + carId + ", speed=" + df.format(speed) + ", lane=" + lane
                + ", duration=" + df.format(duration) + "]";
    }

}
