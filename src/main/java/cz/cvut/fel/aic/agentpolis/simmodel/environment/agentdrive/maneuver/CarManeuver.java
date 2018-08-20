package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;



abstract public class CarManeuver {
	protected double duration;  //duration of maneuver
	protected int laneIn;
	protected int laneOut;
	protected int expectedLaneOut;
	protected double velocityIn;
	protected double velocityOut;
	protected double positionIn;
	protected double positionOut; 

	protected double acceleration;
	protected long startTime;

	protected boolean isInfiniteInTime = false;	//starts on startTime, never ends. 
	//For time comparison when used as crash maneuver
	private boolean isSafeManeuver = false;		//used by peer-to-peer (when the algorithm fails
	private int guidePointsCount;
	private boolean isFinishing = false;

	private double distanceInFirstLane;
	
	public CarManeuver(CarManeuver pred) {
		this(pred.getLaneOut(),pred.getVelocityOut(),pred.getPositionOut(),pred.getEndTime());
	
	}

	

	public CarManeuver(int laneIn,double velocityIn,double positionIn,long startTime){
		this.laneIn = laneIn;
		this.velocityIn = velocityIn;
		this.positionIn = positionIn;
		this.startTime = startTime;
	}

	public boolean equals(CarManeuver otherCarManeuver){
		if((laneIn == otherCarManeuver.getLaneIn())&&(laneOut == otherCarManeuver.getLaneOut())
				&&(positionIn == otherCarManeuver.getPositionIn())&&(positionOut == otherCarManeuver.getPositionOut())
				&&(startTime == otherCarManeuver.getStartTime())&&(this.getEndTime() == otherCarManeuver.getEndTime())
				&&(velocityIn == otherCarManeuver.getVelocityIn())&&(velocityOut == otherCarManeuver.getVelocityOut())
				&&(acceleration == otherCarManeuver.getAcceleration())
				&&(isInfiniteInTime == otherCarManeuver.isInfiniteInTime)
				&&(expectedLaneOut == otherCarManeuver.getExpectedLaneOut())){
			return true;
		}
		else return false;
	}

	public double getDuration() {
		return duration;
	}
	public int getLaneIn() {
		return laneIn;
	}
	public int getLaneOut() {
		return laneOut;
	}
	public int getExpectedLaneOut() {
		return expectedLaneOut;
	}
	public double getVelocityIn() {
		return velocityIn;
	}
	public double getVelocityOut() {
		return velocityOut;
	}

	/*
	 * returns driven distance from the simulation start
	 * when used with cyclic highway, cars are on a same position if (x1 == x2 % highwayCircleLength)
	 */
	public double getPositionIn() {
		return positionIn;
	}

	/*
	 * returns driven distance from the simulation start
	 * when used with cyclic highway, cars are on a same position if (x1 == x2 % highwayCircleLength)
	 */
	public double getPositionOut() {
		return positionOut;
	}
	public double getAcceleration() {
		return acceleration;
	}
	public double getLength(){
		return positionOut-positionIn;
	}
	public long getStartTime(){
		return startTime;
	}
	public long getEndTime(){
		return startTime+(long)(duration*1000);
	}
	public boolean isInfiniteInTime() {
		return isInfiniteInTime;
	}
	public void setInfiniteInTime(boolean isInfiniteInTime) {
		this.isInfiniteInTime = isInfiniteInTime;
	}

	public void setSafeManeuver(boolean isSafeManuever){
		this.isSafeManeuver  = isSafeManuever;
	}
	public boolean isSafeManeuver() {
		return isSafeManeuver;
	}
	
	public int getGuidePointsCount() {
		return guidePointsCount;
	}
	public void setGuidePointsCount(int guidePointsCount) {
		this.guidePointsCount = guidePointsCount;
	}
	
	public void setFinishing(boolean b) {
		this.isFinishing = b;

	}

	public boolean isFinishing() {
		return isFinishing;
	}


	public double getDistanceInFirstLane() {
		return this.distanceInFirstLane;
	}
	@Override
	public String toString(){
		return this.getClass().getSimpleName()+ " vIn= " + getVelocityIn()+ " vOut= "+getVelocityOut()+", "+laneIn+"->"+laneOut + ", posOut=" + positionOut;

	}

}
