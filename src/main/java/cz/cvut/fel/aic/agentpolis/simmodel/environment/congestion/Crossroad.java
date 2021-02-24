/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 * @author fido
 */
public class Crossroad extends Connection {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Crossroad.class);
	
	private final int batchSize;

	private final List<ChoosingTableData> inputLanesChoosingTable;

	private final List<Lane> inputLanes;

	private final Map<Lane, Link> outputLinksMappedByInputLanes;
	private final Map<Connection, Link> outputLinksMappedByNextConnections;
	
	private final double maxFlow;
	
	/**
	 * currently or last served lane
	 */
	private Lane chosenLane;
	
	/**
	 * lanes that are ready to transfer vehicles ie non-empty with free next lane
	 */
	private List<Lane> readyLanes;
	
	/**
	 * total length off all vehicles transferred this batch
	 */  
	private double metersTransferedThisBatch;

	
	

	public Crossroad(AgentpolisConfig config, SimulationProvider simulationProvider, CongestionModel congestionModel,
					 SimulationNode node) {
		super(simulationProvider, config, congestionModel, node);
		inputLanes = new LinkedList<>();
		inputLanesChoosingTable = new LinkedList<>();
		outputLinksMappedByInputLanes = new HashMap<>();
		outputLinksMappedByNextConnections = new HashMap<>();
		batchSize = config.congestionModel.batchSize;
		maxFlow = config.congestionModel.maxFlowPerLane * config.congestionModel.defaultCrossroadDrivingLanes;
	}


	private void addInputLane(Lane lane) {
		inputLanes.add(lane);

	}

	void init() {
		initInputLanesRandomTable();
//		carsPerTick = computeFlowInMetersPerTick();
	}

	@Override
	protected void serveLanes() {

		// getting all lanes with waiting vehicles
		findNonEmptyLanes();
		
		boolean tryTransferNextVehicle = true;
		while (tryTransferNextVehicle) {
			
			// if there are no waiting vehicles
			if (readyLanes.isEmpty()) {
				for (Lane inputLane : inputLanes) {
					checkDrivingQue(inputLane);
				}
				break;
			}

			if(chosenLane == null || (metersTransferedThisBatch > batchSize)){
				chooseLane();
			}
			
			tryTransferNextVehicle = tryTransferVehicle(chosenLane);
		}
	}
	
	private void laneDepleted(Lane lane){
		readyLanes.remove(chosenLane);
		chosenLane = null;
	}

	public int getNumberOfInputLanes() {
		return inputLanes.size();
	}

	private int getOutputLaneCount() {
		int count = 0;
		for (Map.Entry<Connection, Link> entry : outputLinksMappedByNextConnections.entrySet()) {
			Link link = entry.getValue();
			count += link.getLaneCount();
		}
		return count;
	}

	private void initInputLanesRandomTable() {
		final double step = (double) 1 / inputLanes.size();
		double key = step;

		for (Lane inputLane : inputLanes) {
			inputLanesChoosingTable.add(new ChoosingTableData(key, inputLane));
			key += step;
		}
	}
	
	private void chooseLane(){
		if (readyLanes.size() == 1) {
			chosenLane = readyLanes.get(0);
		} 
		else {
			do {
				chosenLane = chooseRandomFreeLane();
			} while (!chosenLane.hasWaitingVehicles());
		}
		metersTransferedThisBatch = 0;
	}

	private Lane chooseRandomFreeLane() {
		double random = Math.random();
		Lane chosenLane = null;
		for (ChoosingTableData choosingTableData : inputLanesChoosingTable) {
			if (random <= choosingTableData.threshold) {
				chosenLane = choosingTableData.inputLane;
				break;
			}
		}
		return chosenLane;
	}

	@Override
	protected Link getNextLink(Lane inputLane) {
		return outputLinksMappedByInputLanes.get(inputLane);
	}

	@Override
	public Link getNextLink(Connection nextConnection) {
		return outputLinksMappedByNextConnections.get(nextConnection);
	}


	void addNextLink(Link link, Lane inputLane, Connection targetConnection) {
		outputLinksMappedByInputLanes.put(inputLane, link);
		outputLinksMappedByNextConnections.put(targetConnection, link);
		addInputLane(inputLane);
	}

	private void findNonEmptyLanes() {
		readyLanes = new LinkedList();
		for (Lane inputLane : inputLanes) {
			if (inputLane.hasWaitingVehicles()) {
				readyLanes.add(inputLane);
			}
		}
	}

	private boolean tryTransferVehicle(Lane chosenLane) {
		
		/* no vehicles in queue */
		if (!chosenLane.hasWaitingVehicles()){
			laneDepleted(chosenLane);
			return true;
		}
		
		// first vehicle
		VehicleTripData vehicleTripData = chosenLane.getFirstWaitingVehicle();
		
		double vehicleLength = (int)((double)vehicleTripData.getVehicle().getLengthCm()/100.0);
		
		// vehicle ends on this node
		if (vehicleTripData.isTripFinished()) {
			scheduleEndDriving(vehicleTripData, chosenLane);
			
			metersTransferedThisBatch += vehicleLength;
			return false;
		}

		Lane nextLane = getNextLane(chosenLane, vehicleTripData);

		// successful transfer
		if (nextLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
			scheduleVehicleTransfer(vehicleTripData, chosenLane, nextLane);
			
			metersTransferedThisBatch += vehicleLength;
			
			if (vehicleTripData.getTrip().isEmpty()) {
				vehicleTripData.setTripFinished(true);
			}
			
			return false;
		} 
		// next queue is full
		else {
			LOGGER.debug("Crossroad {}: No space in queue to {}!", node.id, 
					nextLane.link.toNode.id);
			nextLane.setWakeConnectionAfterTransfer(true);
			laneDepleted(chosenLane);
			return true;
		}
	}
	
	@Override
	protected long computeTransferDelay(VehicleTripData vehicleTripData) {
		return Math.round(vehicleTripData.getVehicle().getLengthCm() * 1E3 / maxFlow);
	}
	

	private final class ChoosingTableData {
		final double threshold;

		final Lane inputLane;

		public ChoosingTableData(double threshold, Lane inputLane) {
			this.threshold = threshold;
			this.inputLane = inputLane;
		}


	}

}
