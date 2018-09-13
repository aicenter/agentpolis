package cz.cvut.fel.aic.agentpolis.config;

import java.util.Map;

public class ADModel {

    public String agentType;

    public double safetyReserveDistance;

    public boolean narrowingMode;

    public int distanceToActivateNM;

    public double laneChangeManeuverDuration;

    public double straightManeuverDuration;

    public double accelerationManeuverDuration;

    public double deaccelerationManueverDuration;

    public double acceleration;

    public double deacceleration;

    public double maximalSpeed;

    public double maxSpeedVariance;

    public double insertSpeed;

    public int checkingDistance;

    public int insertSafetyReserve;

    public double timestep;

    public int ADSeed;

    public String netFolderPath;

    public float stepSize;


    // Default parameters
    public ADModel() {
        agentType = "GSDAgent";
        safetyReserveDistance = 10.0;     // [m] - safety distance offset (including vehicle length and separation gap)
        narrowingMode = true;
        distanceToActivateNM = 500; // [m] - when distance to obstacle is smaller than this value NARROWING MODE is activated
        //maneuver is discratization unit
        //available maneuvers are Straight, Acceleration, Deacceleration, LaneLeft, LaneRight
        //parameters of maneuvers are following
        laneChangeManeuverDuration = 0.5; // [s]
        straightManeuverDuration = 0.5;     //[s]
        accelerationManeuverDuration = 0.3;   //[s]
        deaccelerationManueverDuration = 0.3;   //[s]
        acceleration = 4.0;                     //[m/s^2]
        deacceleration = -6.0;                  //[m/s^2]
        maximalSpeed = 20.0;                    //[m/s]
        maxSpeedVariance = 0.60;                     //[%]
        insertSpeed = 0.5;  //[m/s]
        checkingDistance = 500;
        insertSafetyReserve = 10;
        timestep = 0.8;
        ADSeed = 1;
        netFolderPath = "";
        stepSize = 0.45f; // distance between waypoints
    }

    public ADModel(Map adModel) {
        this.agentType = (String) adModel.get("agent");
        this.safetyReserveDistance = (double) adModel.get("safetyReserveDistance");
        this.narrowingMode = (boolean) adModel.get("narrowingModeActive");
        this.distanceToActivateNM = (int) adModel.get("distanceToActivateNM");
        this.laneChangeManeuverDuration = (double) adModel.get("laneChangeManeuverDuration");
        this.straightManeuverDuration = (double) adModel.get("straightManeuverDuration");
        this.accelerationManeuverDuration = (double) adModel.get("accelerationManeuverDuration");
        this.deaccelerationManueverDuration = (double) adModel.get("deaccelerationManueverDuration");
        this.acceleration = (double) adModel.get("acceleration");
        this.deacceleration = Double.parseDouble((String) adModel.get("deacceleration"));
        this.maximalSpeed = (double) adModel.get("maximalSpeed");
        this.maxSpeedVariance = (double) adModel.get("maxSpeedVariance");
        this.insertSpeed = (double) adModel.get("insertSpeed");
        this.checkingDistance = (int) adModel.get("checkingDistance");
        this.insertSafetyReserve = (int) adModel.get("safetyReserve");
        this.timestep = (double) adModel.get("timestep");
        this.ADSeed = (int) adModel.get("seed");
        this.netFolderPath = (String) adModel.get("folder");
        this.stepSize = ((Double) adModel.get("stepSize")).floatValue();
    }
}
