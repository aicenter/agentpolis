/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author fido
 */
public class Crossroad extends Connection {

    private final int batchSize;

    private final List<ChoosingTableData> inputLanesChoosingTable;

    private final List<Lane> inputLanes;

    private final Map<Lane, Link> outputLinksMappedByInputLanes;
    private final Map<Connection, Link> outputLinksMappedByNextConnections;

    final TimeProvider timeProvider;


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
                     SimulationNode node, TimeProvider timeProvider) {
        super(simulationProvider, config, congestionModel, node);
        this.timeProvider = timeProvider;
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
    }

    @Override
    protected void serveLanes() {
        Log.debug(this, "Serve lanes START");

        // getting all lanes with waiting vehicles
        findNonEmptyLanes();

        boolean tryTransferNextVehicle = true;
        int c = 0;
        while (tryTransferNextVehicle) {
            c++;

            // if there are no waiting vehicles
            if (readyLanes.isEmpty()) {
                for (Lane inputLane : inputLanes) {
                    checkDrivingQue(inputLane);
                }
                break;
            }

            if (chosenLane == null || (metersTransferedThisBatch > batchSize)) {
                chooseLane();
            }

            tryTransferNextVehicle = tryTransferVehicle(chosenLane);
        }
        Log.debug(this, "Serve lanes END, with " + c + " loops");
    }

    private void laneDepleted(Lane lane) {
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

    private void chooseLane() {
        if (readyLanes.size() == 1) {
            chosenLane = readyLanes.get(0);
        } else {
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
        Log.debug(this, "CROSSROAD: TryTransferVehicles START");

        /* no vehicles in queue */
        if (!chosenLane.hasWaitingVehicles()) {
            laneDepleted(chosenLane);
            Log.debug(this, "CROSSROAD: TryTransferVehicles END returns true - lane depleted");

            return true;
        }

        // first vehicle
        VehicleTripData vehicleTripData = chosenLane.getFirstWaitingVehicle();

        double vehicleLength = vehicleTripData.getVehicle().getLength();

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

            return false;
        }
        // next queue is full
        else {
            Log.debug(Connection.class, "Crossroad {0}: No space in queue to {1}!", node.id,
                    nextLane.link.toNode.id);
            nextLane.setWakeConnectionAfterTransfer(true);
            laneDepleted(chosenLane);
            Log.debug(this, "TryTransferVehicles END - returning true");

            return true;
        }
    }

    @Override
    protected long computeTransferDelay(VehicleTripData vehicleTripData, Lane toLane, Lane from) {

        return computeConnectionArrivalDelay(vehicleTripData) + computeCrossroadDelay(vehicleTripData, toLane);
    }

    @Override
    protected long computeConnectionArrivalDelay(VehicleTripData vehicleTripData) {
        DelayData delayData = vehicleTripData.getVehicle().getDelayData();
        long currentSimTime = timeProvider.getCurrentSimTime();
        long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
        return Math.max(0, arrivalExpectedTime - currentSimTime);
    }

    private long computeCrossroadDelay(VehicleTripData vehicleTripData, Lane toLane) {
        long freeFlowVehicleTransferTime = congestionModel.computeTransferDelay(vehicleTripData, toLane);
//        long freeFlowVehicleTransferTime = (long) (1E3 * vehicleTripData.getVehicle().getLength() / vehicleTripData.getVehicle().getVelocity());
        long crossroadTransferTime = (long) (1E3 * vehicleTripData.getVehicle().getLength() / maxFlow);
        return Math.max(0, crossroadTransferTime - freeFlowVehicleTransferTime);
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
