package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Integer;
import java.util.Map;

public class SimulationDuration {
  public Integer hours;

  public Integer seconds;

  public Integer minutes;

  public Integer days;

  public SimulationDuration(Map simulationDuration) {
	this.hours = (Integer) simulationDuration.get("hours");
	this.seconds = (Integer) simulationDuration.get("seconds");
	this.minutes = (Integer) simulationDuration.get("minutes");
	this.days = (Integer) simulationDuration.get("days");
  }
}
