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

import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;

/**
 * @author fido
 */
public class Lane extends EventHandlerAdapter {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Lane.class);
	
	private static final int MIN_LINK_CAPACITY_IN_METERS = 5;


	private final double linkCapacityInMeters;

	final LinkedList<VehicleQueueData> drivingQueue;
	private final LinkedList<VehicleQueueData> waitingQueue;

	final Link link;

	private final TimeProvider timeProvider;


	private LinkedList<VehicleTripData> startHereQueue;

	private Link nextLink;

	private int currentlyUsedCapacityInCm;

	private double waitingQueueInMeters;

	private boolean wakeConnectionAfterTransfer;

	private final SimulationProvider simulationProvider;


	private boolean eventScheduled;


	public boolean wakeConnectionAfterTransfer() {
		return wakeConnectionAfterTransfer;
	}

	public void setWakeConnectionAfterTransfer(boolean wakeConnectionAfterTransfer) {
		this.wakeConnectionAfterTransfer = wakeConnectionAfterTransfer;
	}

	public Link getNextLink() {
		return nextLink;
	}


	public Lane(Link link, double linkCapacityInMeters, TimeProvider timeProvider,
				SimulationProvider simulationProvider) {
		this.link = link;
		this.simulationProvider = simulationProvider;
		this.linkCapacityInMeters = linkCapacityInMeters > MIN_LINK_CAPACITY_IN_METERS
				? linkCapacityInMeters : MIN_LINK_CAPACITY_IN_METERS;
		this.timeProvider = timeProvider;
		this.drivingQueue = new LinkedList<>();
		this.waitingQueue = new LinkedList<>();
		eventScheduled = false;
	}


	void removeFromQueue(VehicleTripData vehicleData) {
		currentlyUsedCapacityInCm -= vehicleData.getVehicle().getLengthCm();
		waitingQueueInMeters -= (int)((double)vehicleData.getVehicle().getLengthCm()/100.0);
		waitingQueue.remove();

		updateVehiclesInQueue(vehicleData.getVehicle().getLengthCm());

		// wake previous connection and start que processing
		if (wakeConnectionAfterTransfer) {
			
			/* wake up previous connection */
			wakeUpPreviousConnection(0);
			
			/* wake up start here processing */
			handleChange();

			setWakeConnectionAfterTransfer(false);
		}
	}

	VehicleTripData getFirstWaitingVehicle() {
		return waitingQueue.getFirst().getVehicleTripData();
	}

	private boolean isEmpty() {
		return drivingQueue.isEmpty() && waitingQueue.isEmpty();
	}

	boolean hasWaitingVehicles() {
		updateWaitingQueue();
		return !waitingQueue.isEmpty();

	}

	private void updateWaitingQueue() {
		long currentTime = timeProvider.getCurrentSimTime();
		while (!drivingQueue.isEmpty() && currentTime >= drivingQueue.peek().getMinPollTime()) {
			VehicleQueueData vehicleQueueData = drivingQueue.pollFirst();
			VehicleTripData vehicleTripData = vehicleQueueData.getVehicleTripData();
			waitingQueue.addLast(vehicleQueueData);
			waitingQueueInMeters += (int)((double)vehicleTripData.getVehicle().getLengthCm()/100.0);
		}
	}

	void startDriving(VehicleTripData vehicleTripData) {
		addToStartHereQueue(vehicleTripData);
		handleChange();
	}

	void prepareAddingToqueue(VehicleTripData vehicleTripData) {
		currentlyUsedCapacityInCm += vehicleTripData.getVehicle().getLengthCm();
	}

	void addToQue(VehicleTripData vehicleTripData) {

		// wake up next connection
		if (isEmpty()) {
			long minDelay = computeDelay(vehicleTripData.getVehicle());
			wakeUpNextConnection(minDelay);
		}

		long estimatedDelayToQueueEnd = link.congestionModel.computeDelayAndSetVehicleData(vehicleTripData, this);

		long minExitTime = timeProvider.getCurrentSimTime() + estimatedDelayToQueueEnd;
		drivingQueue.add(new VehicleQueueData(vehicleTripData, minExitTime));
	}

	boolean queueHasSpaceForVehicle(PhysicalVehicle vehicle) {
		double freeCapacity = linkCapacityInMeters*100 - currentlyUsedCapacityInCm;
		return freeCapacity > vehicle.getLengthCm();
	}

	private void addToStartHereQueue(VehicleTripData vehicleTripData) {
		if (startHereQueue == null) {
			startHereQueue = new LinkedList<>();
		}
		startHereQueue.add(vehicleTripData);
	}

	public double getQueueLength() {
		return waitingQueueInMeters;
	}

	public int getUsedLaneCapacityInCm() {
		return currentlyUsedCapacityInCm;
	}

	long computeDelay(PhysicalVehicle vehicle, double distance) {
		double speed = computeSpeed(vehicle);
		double duration = distance / speed;
		long durationInMs = Math.max(1, (long) (1000 * duration));
		return durationInMs;
	}

	long computeDelay(PhysicalVehicle vehicle) {
		SimulationEdge edge = link.edge;
		return computeDelay(vehicle, edge.shape.getShapeLength());
	}

	void updateDelayData(PhysicalVehicle vehicle) {
		CongestionModel congestionModel = link.congestionModel;
		long currentSimTime = congestionModel.getTimeProvider().getCurrentSimTime();

		SimulationEdge edge = link.edge;
		DelayData previousDelayData = vehicle.getDelayData();
		double completionRatioOfPreviousDelay = (currentSimTime - previousDelayData.getDelayStartTime()) / (double) previousDelayData.getDelay();
		if (completionRatioOfPreviousDelay > 1.0) completionRatioOfPreviousDelay = 1.0;

		int startDistanceOffset = (int) Math.round(previousDelayData.getStartDistanceOffset() 
				+ completionRatioOfPreviousDelay * previousDelayData.getDelayDistance());

		int distance = edge.getLengthCm() - startDistanceOffset - vehicle.getQueueBeforeVehicleLength();
		assert distance >= 0.0;

		long durationInMs = computeDelay(vehicle, distance);
		vehicle.getDriver().setDelayData(new DelayData(durationInMs, currentSimTime, distance, startDistanceOffset));
	}

	double computeSpeed(PhysicalVehicle vehicle) {
		SimulationEdge edge = link.edge;
		double freeFlowVelocity = MoveUtil.computeAgentOnEdgeVelocity(vehicle, edge);

		double speed = freeFlowVelocity;
                
                //may use congestionModel.on ??
		if (link.congestionModel.addFundamentalDiagramDelay) {                   
			speed = computeCongestedSpeed(freeFlowVelocity, edge);
		}

		return speed;
	}

	private double computeCongestedSpeed(double freeFlowVelocity, SimulationEdge edge) {
		double carsPerKilometer = (double)getDrivingCarsCountOnLane() / edge.shape.getShapeLength() * 1000.0;

		double congestedSpeed;
		if (carsPerKilometer < 20) {
			congestedSpeed = freeFlowVelocity;
		} else if (carsPerKilometer > 70) {
			congestedSpeed = 0.1 * freeFlowVelocity;
		} else {
			congestedSpeed = freeFlowVelocity * calculateSpeedCoefficient(carsPerKilometer);
		}
		LOGGER.info("Congested speed: {}cars/km -> {}m/s", carsPerKilometer, congestedSpeed/100f);

		return congestedSpeed;
	}

	private int getDrivingCarsCountOnLane() {
		return drivingQueue.size();
	}

	private double calculateSpeedCoefficient(double carsPerKilometer) {
		//		WoframAlpha LinearModelFit[{{20, 100}, {30, 60}, {40, 40}, {70, 10}}, {x, x^2}, x]
		// interpolate speed for freeFlowSpeed = 100kmph
		//		0.0428177 x^2 - 5.61878 x + 193.757 (quadratic)
		double x = carsPerKilometer;
		double reducedSpeed = (0.0428177 * x * x - 5.61878 * x + 193.757);
		return reducedSpeed / 100.0;
	}


	private void updateVehiclesInQueue(int transferredVehicleLength) {
		for (VehicleQueueData vehicleQueueData : waitingQueue) {
			updateVehicle(vehicleQueueData, transferredVehicleLength);
		}

		for (VehicleQueueData vehicleQueueData : drivingQueue) {
			updateVehicle(vehicleQueueData, transferredVehicleLength);
		}
	}

	private void updateVehicle(VehicleQueueData vehicleQueueData, int transferredVehicleLength) {
		PhysicalVehicle vehicle = vehicleQueueData.getVehicleTripData().getVehicle();
		CongestionModel congestionModel = link.congestionModel;

		// set que before vehicle
		vehicle.setQueueBeforeVehicleLength(vehicle.getQueueBeforeVehicleLength() - transferredVehicleLength);

		long currentSimulationTime = congestionModel.getTimeProvider().getCurrentSimTime();

		// create new delay
		updateDelayData(vehicle);

		/* update min exit time for driving queue */
		vehicleQueueData.setMinPollTime(currentSimulationTime + vehicle.getDelayData().getDelay());
	}

	private void handleChange() {
		if (!eventScheduled) {
			tryScheduleStartVehicle();
		}
	}

	@Override
	public void handleEvent(Event event) {
		startFirstVehicleInStartHereQueue();
		tryScheduleStartVehicle();
	}

	public void wakeUpNextConnection(long delay) {
		link.congestionModel.wakeUpConnection(link.toConnection, delay);
	}

	public void wakeUpPreviousConnection(long delay) {
		link.congestionModel.wakeUpConnection(link.fromConnection, delay);
	}

	private void startFirstVehicleInStartHereQueue() {
		VehicleTripData vehicleTripData = startHereQueue.pollFirst();
		addToQue(vehicleTripData);
		eventScheduled = false;
	}

	private void tryScheduleStartVehicle() {
		if (startHereQueue == null || startHereQueue.isEmpty()) {
			return;
		}

		VehicleTripData vehicleTripData = startHereQueue.peek();

		if (!queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
			setWakeConnectionAfterTransfer(true);
			return;
		}

		/* next que capacity reservation */
		this.prepareAddingToqueue(vehicleTripData);

		long delay = CongestionModel.computeFreeflowTransferDelay(vehicleTripData.getVehicle());

		String message = "Vehicle " + vehicleTripData.getVehicle().getId() + " delayed start";

		simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, message, delay);
		eventScheduled = true;
	}

}
