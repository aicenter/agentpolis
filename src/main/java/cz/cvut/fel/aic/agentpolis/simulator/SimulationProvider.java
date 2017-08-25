/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

/**
 *
 * @author F-I-D-O
 */
@Singleton
public class SimulationProvider{
	
	private TypedSimulation simulation;

	
	
	
	public TypedSimulation getSimulation() {
		return simulation;
	}
	
	public void setSimulation(TypedSimulation simulation) {
		this.simulation = simulation;
	}

	
	
	@Inject
	public SimulationProvider() {
	}
	
}
