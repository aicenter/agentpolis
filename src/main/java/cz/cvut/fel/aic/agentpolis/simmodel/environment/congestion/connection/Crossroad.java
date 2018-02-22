/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.CongestedTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes.CongestionLane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Crossroad extends Connection {
    private final TimeProvider timeProvider;
    private final int batchSize;
    private final double maxFlow;

    private final List<ChoosingTableData> inputLanesChoosingTable;
    private final List<CongestionLane> inputCongestionLanes;

    private final Map<CongestionLane, Link> outputLinksMappedByInputLanes;
    private final Map<Connection, Link> outputLinksMappedByNextConnections;

    /**
     * lanes that are ready to transfer vehicles ie non-empty with free next congestionLane
     */
    private List<CongestionLane> readyCongestionLanes;
    /**
     * total length off all vehicles transferred this batch
     */
    private double metersTransferedThisBatch;

    /**
     * currently or last served congestionLane
     */
    private CongestionLane chosenCongestionLane;

    public Crossroad(AgentpolisConfig config, SimulationProvider simulationProvider, CongestionModel congestionModel,
                     SimulationNode node, TimeProvider timeProvider) {
        super(simulationProvider, config, congestionModel, node);
        this.timeProvider = timeProvider;
        inputCongestionLanes = new LinkedList<>();
        inputLanesChoosingTable = new LinkedList<>();
        outputLinksMappedByInputLanes = new HashMap<>();
        outputLinksMappedByNextConnections = new HashMap<>();
        batchSize = config.congestionModel.batchSize;
        maxFlow = config.congestionModel.maxFlowPerLane * config.congestionModel.defaultCrossroadDrivingLanes;

    }

    public int getOutputLaneCount() {
        int count = 0;
        for (Map.Entry<Connection, Link> entry : outputLinksMappedByNextConnections.entrySet()) {
            Link link = entry.getValue();
            count += link.getLaneCount();
        }
        return count;
    }

    public int getNumberOfInputLanes() {
        return inputCongestionLanes.size();
    }

    //
    // ========================= Build ======================
    //
    public void init() {
        final double step = (double) 1 / inputCongestionLanes.size();
        double key = step;

        for (CongestionLane inputCongestionLane : inputCongestionLanes) {
            inputLanesChoosingTable.add(new ChoosingTableData(key, inputCongestionLane));
            key += step;
        }

    }

    @Override
    public Link getNextLink(CongestionLane inputCongestionLane) {
        return outputLinksMappedByInputLanes.get(inputCongestionLane);
    }

    @Override
    public Link getNextLink(Connection nextConnection) {
        return outputLinksMappedByNextConnections.get(nextConnection);
    }

    public void addNextLink(Link link, CongestionLane inputCongestionLane, Connection targetConnection) {
        outputLinksMappedByInputLanes.put(inputCongestionLane, link);
        outputLinksMappedByNextConnections.put(targetConnection, link);
        addInputLane(inputCongestionLane);
    }

    //
    // ========================= Simulation event transfer ======================
    //
    /**
     * Tries to transfer one vehicle. The vehicle will be transfered in next tick.
     */
    @Override
    protected void serveLanes(Event event) {
        Log.info(this, "Serve lanes START");

        // getting all lanes with waiting vehicles
        findNonEmptyLanes();

        boolean tryTransferNextVehicle = true;
        int c = 0;
        while (tryTransferNextVehicle) {
            c++;

            // if there are no waiting vehicles
            if (readyCongestionLanes.isEmpty()) {
                for (CongestionLane inputCongestionLane : inputCongestionLanes) {
                    checkDrivingQue(inputCongestionLane);
                }
                break;
            }

            if (chosenCongestionLane == null || (metersTransferedThisBatch > batchSize)) {
                chooseLane();
            }

            tryTransferNextVehicle = tryTransferVehicle(chosenCongestionLane);
        }
        Log.info(this, "Serve lanes END, with " + c + " loops");
    }

    private boolean tryTransferVehicle(CongestionLane chosenCongestionLane) {
        Log.info(this, "CROSSROAD: TryTransferVehicles START");

        /* no vehicles in queue */
        if (!chosenCongestionLane.hasWaitingVehicles()) {
            laneDepleted(chosenCongestionLane);
            Log.info(this, "CROSSROAD: TryTransferVehicles END returns true - congestionLane depleted");

            return true;
        }

        // first vehicle
        CongestedTripData congestedTripData = chosenCongestionLane.getFirstWaitingVehicle();

        double vehicleLength = congestedTripData.getVehicle().getLength();

        // vehicle ends on this node
        if (congestedTripData.isTripFinished()) {

            scheduleEndDriving(congestedTripData, chosenCongestionLane);

            metersTransferedThisBatch += vehicleLength;

            return false;
        }

        CongestionLane nextCongestionLane = getNextLane(chosenCongestionLane, congestedTripData);

        // successful transfer
        if (nextCongestionLane.queueHasSpaceForVehicle(congestedTripData.getVehicle())) {
            scheduleVehicleTransfer(congestedTripData, chosenCongestionLane, nextCongestionLane);

            metersTransferedThisBatch += vehicleLength;

            return false;
        }
        // next queue is full
        else {
            Log.log(Connection.class, Level.FINER, "Crossroad {0}: No space in queue to {1}!", node.id,
                    nextCongestionLane.parentLink.toNode.id);
            nextCongestionLane.setWakePreviousConnectionAfterTransfer(true);
            laneDepleted(chosenCongestionLane);
            Log.info(this, "TryTransferVehicles END - returning true");

            return true;
        }
    }


    @Override
    protected long computeTransferDelay(CongestedTripData congestedTripData, CongestionLane toCongestionLane) {

        return computeConnectionArrivalDelay(congestedTripData) + computeCrossroadDelay(congestedTripData, toCongestionLane);
    }

    @Override
    protected long computeConnectionArrivalDelay(CongestedTripData congestedTripData) {
        DelayData delayData = congestedTripData.getVehicle().getDelayData();
        long currentSimTime = timeProvider.getCurrentSimTime();
        long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
        return Math.max(0, arrivalExpectedTime - currentSimTime);
    }

    private long computeCrossroadDelay(CongestedTripData congestedTripData, CongestionLane toCongestionLane) {
        long freeFlowVehicleTransferTime = congestionModel.computeTransferDelay(congestedTripData, toCongestionLane);
//        long freeFlowVehicleTransferTime = (long) (1E3 * congestedTripData.getVehicle().getLength() / congestedTripData.getVehicle().getVelocity());
        long crossroadTransferTime = (long) (1E3 * congestedTripData.getVehicle().getLength() / maxFlow);
        return Math.max(0, crossroadTransferTime - freeFlowVehicleTransferTime);
    }


    //
    // ========================= Make junction lanes and logic ======================
    //
    private void laneDepleted(CongestionLane congestionLane) {
        readyCongestionLanes.remove(chosenCongestionLane);
        chosenCongestionLane = null;
    }


    private void addInputLane(CongestionLane congestionLane) {
        inputCongestionLanes.add(congestionLane);

    }

    private void chooseLane() {
        if (readyCongestionLanes.size() == 1) {
            chosenCongestionLane = readyCongestionLanes.get(0);
        } else {
            do {
                chosenCongestionLane = chooseRandomFreeLane();
            } while (!chosenCongestionLane.hasWaitingVehicles());
        }
        metersTransferedThisBatch = 0;
    }

    private CongestionLane chooseRandomFreeLane() {
        double random = Math.random();
        CongestionLane chosenCongestionLane = null;
        for (ChoosingTableData choosingTableData : inputLanesChoosingTable) {
            if (random <= choosingTableData.threshold) {
                chosenCongestionLane = choosingTableData.inputCongestionLane;
                break;
            }
        }
        return chosenCongestionLane;
    }

    private void findNonEmptyLanes() {
        readyCongestionLanes = new LinkedList();
        for (CongestionLane inputCongestionLane : inputCongestionLanes) {
            if (inputCongestionLane.hasWaitingVehicles()) {
                readyCongestionLanes.add(inputCongestionLane);
            }
        }
    }

    private final class ChoosingTableData {
        final double threshold;
        final CongestionLane inputCongestionLane;

        public ChoosingTableData(double threshold, CongestionLane inputCongestionLane) {
            this.threshold = threshold;
            this.inputCongestionLane = inputCongestionLane;
        }
    }
}
