/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.alite.simulation.Simulation;

/**
 *
 * @author F-I-D-O
 */
@Singleton
public class SimulationProvider{
	
	private Simulation simulation;

	
	
	
	public Simulation getSimulation() {
		return simulation;
	}
	
	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	
	
	@Inject
	public SimulationProvider() {
	}
	
}
