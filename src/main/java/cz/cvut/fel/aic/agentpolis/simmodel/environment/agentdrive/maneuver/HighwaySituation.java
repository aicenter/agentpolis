package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;

import java.util.ArrayList;

public class HighwaySituation extends ArrayList<CarManeuver>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2118932755053154678L;
	private CarManeuver  carAheadMan;
	private CarManeuver carLeftMan;
	private CarManeuver carRightMan;
	private CarManeuver carRightAheadMan;
	private CarManeuver carLeftAheadMan;

	public CarManeuver getCarAheadMan() {
		 return carAheadMan;
	}	
	public CarManeuver getCarLeftMan() {		
		return carLeftMan;
	}
	public CarManeuver getCarRightMan() {		
		return carRightMan;
	}
	public CarManeuver getCarLeftAheadMan() {		
		return carLeftAheadMan;
	}
	public CarManeuver getCarRightAheadMan() {		
		return carRightAheadMan;
	}
	

	public void trySetCarAheadManeuver(CarManeuver man){
		if(carAheadMan==null||man.getPositionIn()<carAheadMan.getPositionIn()){
		carAheadMan = man;
		}
	}
	public void trySetCarLeftMan(CarManeuver man) {
		if(carLeftMan==null||man.getPositionIn()>carLeftMan.getPositionIn()){
		carLeftMan = man;		
		}
	}
	public void trySetCarRightMan(CarManeuver man) {
		if(carRightMan==null||man.getPositionIn()>carRightMan.getPositionIn()){
		carRightMan = man;		
		}
	}
	public void trySetCarLeftAheadMan(CarManeuver man) {
		if(carLeftAheadMan==null||man.getPositionIn()<carLeftAheadMan.getPositionIn()){
		carLeftAheadMan = man;		
		}
	}
	public void trySetCarRightAheadMan(CarManeuver man) {
		if(carRightAheadMan==null||man.getPositionIn()<carRightAheadMan.getPositionIn()){
		carRightAheadMan = man;		
		}
	}
	
	@Override
	public String toString() {
		return "Higway Situation: Ahead-"+carAheadMan+", AheadLeft-"+carLeftAheadMan+", AheadRight-"+carRightAheadMan+", Left-"+carLeftMan+", Right-"+carRightMan;
	}

	

}
