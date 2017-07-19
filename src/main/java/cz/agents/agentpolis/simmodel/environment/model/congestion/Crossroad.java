/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author fido
 */
public class Crossroad extends Connection {

    private final int tickLength;

    private final int batchSize;

    private int carsPerTick;

    private final List<ChoosingTableData> inputLanesChoosingTable;

    private final List<Lane> inputLanes;

    private final Map<Lane, Link> outputLinksMappedByInputLanes;
    private final Map<Connection, Link> outputLinksMappedByNextConnections;
    private double maxFlowPerLane;

    public Crossroad(Config config, SimulationProvider simulationProvider, CongestionModel congestionModel,
                     SimulationNode node) {
        super(simulationProvider, config, congestionModel, node);
        inputLanes = new LinkedList<>();
        inputLanesChoosingTable = new LinkedList<>();
        outputLinksMappedByInputLanes = new HashMap<>();
        outputLinksMappedByNextConnections = new HashMap<>();
        batchSize = config.congestionModel.batchSize;
        tickLength = config.congestionModel.connectionTickLength;
        maxFlowPerLane = config.congestionModel.maxFlowPerLane;
    }


    private void addInputLane(Lane lane) {
        inputLanes.add(lane);

    }

    void init() {
        initInputLanesRandomTable();
        carsPerTick = computeCarsPerTick();
    }

    @Override
    protected void serveLanes() {
        tryStartDelayedVehicles();
        Lane chosenLane = null;

        List<Lane> nonEmptyLanes = new LinkedList();
        for (Lane inputLane : inputLanes) {
            if (inputLane.hasWaitingVehicles()) {
                nonEmptyLanes.add(inputLane);
            }
        }

        int carCounter = 0;
        while (carCounter < carsPerTick) {

            if (nonEmptyLanes.isEmpty()) {
                // not sending tick event - performance reasons, the crossroad is woken up when a vehicle arrives
                awake = false;
                return;
            }
            if (nonEmptyLanes.size() == 1) {
                chosenLane = nonEmptyLanes.get(0);
            } else {
                do {
                    chosenLane = chooseLane();
                } while (!chosenLane.hasWaitingVehicles());
            }
            carCounter += tryToServeLane(chosenLane);
            if (!chosenLane.hasWaitingVehicles()) {
                nonEmptyLanes.remove(chosenLane);
            }
        }

        // wake up after some time
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null,
                tickLength);
    }

    private void tryStartDelayedVehicles() {
        for (Lane inputLane : inputLanes) {
            inputLane.startFromStartHereQueue();
        }
    }

    private int computeCarsPerTick() {
        return (int) Math.max(1, Math.round(maxFlowPerLane * getOutputLaneCount() * tickLength / 1000.0));
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

    private Lane chooseLane() {
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

    private int tryToServeLane(Lane chosenLane) {
        int carCounter = 0;
        while (carCounter < batchSize) {
            if (!tryTransferVehicle(chosenLane)) {
                break;
            }
            carCounter++;
        }
        return carCounter;
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

    private final class ChoosingTableData {
        final double threshold;

        final Lane inputLane;

        public ChoosingTableData(double threshold, Lane inputLane) {
            this.threshold = threshold;
            this.inputLane = inputLane;
        }


    }

}
