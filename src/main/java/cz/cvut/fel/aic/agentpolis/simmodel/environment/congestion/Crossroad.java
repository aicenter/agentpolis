/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
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

    
    

    public Crossroad(Config config, SimulationProvider simulationProvider, CongestionModel congestionModel,
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
//        carsPerTick = computeFlowInMetersPerTick();
    }

    @Override
    protected void serveLanes() {

        // getting all lanes with waiting vehicles
        findNonEmptyLanes();
        
        boolean tryTransferNextVehicle = true;
        while (tryTransferNextVehicle) {
            
            // if there are no waiting vehicles
            if (readyLanes.isEmpty()) {
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
        
        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {
            endDriving(vehicleTripData, chosenLane);
            return true;
        }

        Lane nextLane = getNextLane(chosenLane, vehicleTripData);
        double vehicleLength = vehicleTripData.getVehicle().getLength();
        
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
            Log.log(Connection.class, Level.FINE, "Crossroad {0}: No space in queue to {1}!", node.id, 
                    nextLane.link.toNode.id);
            nextLane.setWakeConnectionAfterTransfer(true);
            return false;
        }
    }
    
    @Override
    protected long computeTransferDelay(VehicleTripData vehicleTripData) {
        return Math.round(vehicleTripData.getVehicle().getLength() * 1E3 / maxFlow);
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
