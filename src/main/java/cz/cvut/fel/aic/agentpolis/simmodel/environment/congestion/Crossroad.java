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
import java.util.logging.Level;

/**
 * @author fido
 */
public class Crossroad extends Connection {

    private final int batchSize;

    private final List<ChoosingTableData> inputLanesChoosingTable;

    private final List<CongestionLane> inputCongestionLanes;

    private final Map<CongestionLane, Link> outputLinksMappedByInputLanes;
    private final Map<Connection, Link> outputLinksMappedByNextConnections;

    final TimeProvider timeProvider;


    private final double maxFlow;

    /**
     * currently or last served congestionLane
     */
    private CongestionLane chosenCongestionLane;

    /**
     * lanes that are ready to transfer vehicles ie non-empty with free next congestionLane
     */
    private List<CongestionLane> readyCongestionLanes;

    /**
     * total length off all vehicles transferred this batch
     */
    private double metersTransferedThisBatch;


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


    private void addInputLane(CongestionLane congestionLane) {
        inputCongestionLanes.add(congestionLane);

    }

    void init() {
        initInputLanesRandomTable();
    }

    @Override
    protected void serveLanes() {
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

    private void laneDepleted(CongestionLane congestionLane) {
        readyCongestionLanes.remove(chosenCongestionLane);
        chosenCongestionLane = null;
    }

    public int getNumberOfInputLanes() {
        return inputCongestionLanes.size();
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
        final double step = (double) 1 / inputCongestionLanes.size();
        double key = step;

        for (CongestionLane inputCongestionLane : inputCongestionLanes) {
            inputLanesChoosingTable.add(new ChoosingTableData(key, inputCongestionLane));
            key += step;
        }
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

    @Override
    protected Link getNextLink(CongestionLane inputCongestionLane) {
        return outputLinksMappedByInputLanes.get(inputCongestionLane);
    }

    @Override
    public Link getNextLink(Connection nextConnection) {
        return outputLinksMappedByNextConnections.get(nextConnection);
    }


    void addNextLink(Link link, CongestionLane inputCongestionLane, Connection targetConnection) {
        outputLinksMappedByInputLanes.put(inputCongestionLane, link);
        outputLinksMappedByNextConnections.put(targetConnection, link);
        addInputLane(inputCongestionLane);
    }

    private void findNonEmptyLanes() {
        readyCongestionLanes = new LinkedList();
        for (CongestionLane inputCongestionLane : inputCongestionLanes) {
            if (inputCongestionLane.hasWaitingVehicles()) {
                readyCongestionLanes.add(inputCongestionLane);
            }
        }
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
        VehicleTripData vehicleTripData = chosenCongestionLane.getFirstWaitingVehicle();

        double vehicleLength = vehicleTripData.getVehicle().getLength();

        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {

            scheduleEndDriving(vehicleTripData, chosenCongestionLane);

            metersTransferedThisBatch += vehicleLength;

            return false;
        }

        CongestionLane nextCongestionLane = getNextLane(chosenCongestionLane, vehicleTripData);

        // successful transfer
        if (nextCongestionLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            scheduleVehicleTransfer(vehicleTripData, chosenCongestionLane, nextCongestionLane);

            metersTransferedThisBatch += vehicleLength;

            return false;
        }
        // next queue is full
        else {
            Log.log(Connection.class, Level.FINER, "Crossroad {0}: No space in queue to {1}!", node.id,
                    nextCongestionLane.parentLink.toNode.id);
            nextCongestionLane.setWakeConnectionAfterTransfer(true);
            laneDepleted(chosenCongestionLane);
            Log.info(this, "TryTransferVehicles END - returning true");

            return true;
        }
    }

    @Override
    protected long computeTransferDelay(VehicleTripData vehicleTripData, CongestionLane toCongestionLane) {

        return computeConnectionArrivalDelay(vehicleTripData) + computeCrossroadDelay(vehicleTripData, toCongestionLane);
    }

    @Override
    protected long computeConnectionArrivalDelay(VehicleTripData vehicleTripData) {
        DelayData delayData = vehicleTripData.getVehicle().getDelayData();
        long currentSimTime = timeProvider.getCurrentSimTime();
        long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
        return Math.max(0, arrivalExpectedTime - currentSimTime);
    }

    private long computeCrossroadDelay(VehicleTripData vehicleTripData, CongestionLane toCongestionLane) {
        long freeFlowVehicleTransferTime = congestionModel.computeTransferDelay(vehicleTripData, toCongestionLane);
//        long freeFlowVehicleTransferTime = (long) (1E3 * vehicleTripData.getVehicle().getLength() / vehicleTripData.getVehicle().getVelocity());
        long crossroadTransferTime = (long) (1E3 * vehicleTripData.getVehicle().getLength() / maxFlow);
        return Math.max(0, crossroadTransferTime - freeFlowVehicleTransferTime);
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
