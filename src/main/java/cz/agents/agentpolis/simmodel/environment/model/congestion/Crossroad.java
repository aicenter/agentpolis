/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fido
 */
public class Crossroad  extends Connection{
    private final Map<SimulationNode,Link> linksMappedByNextNodes;
    
    private final double maxFlow;
    
    private final int laneCount;
    
    private final int tickLength;
    
    private final int batchSize;
    
    private final Map<Double,Lane> inputLanesRandomTable;
    
    private final List<Lane> inputLanes;

    public Crossroad(Config config, SimulationProvider simulationProvider) {
        super(simulationProvider, config);
        this.linksMappedByNextNodes = new HashMap<>();
        inputLanes = new LinkedList<>();
        inputLanesRandomTable = new HashMap<>();
        batchSize = config.batchSize;
        laneCount = getLaneCount();
        maxFlow = computeMaxFlow(config);
        tickLength = computeTickLength();
    }
    
    
    

    void addInputLane(Lane lane){
        inputLanes.add(lane);
    }
    
    void init(){
        initInputLanesRandomTable();
    }
    
    void startDriving(PhysicalVehicle vehicle){
        Trip<SimulationNode> trip = vehicle.getDriver().getCurrentTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
        linksMappedByNextNodes.get(nextLocation).startDriving(vehicle);
    }
    
    private double computeMaxFlow(Config config){
        return config.maxFowPerLane * 2;
    }

    private int getLaneCount() {
        int count = 0;
        for (Map.Entry<SimulationNode, Link> entry : linksMappedByNextNodes.entrySet()) {
            SimulationNode key = entry.getKey();
            Link link = entry.getValue();
            count += link.getLaneCount();
        }
        return count;
    }

    private int computeTickLength() {
        return (int) Math.round(batchSize / maxFlow * 1000);
    }

    @Override
    protected void handleTick() {
        Lane chosenLane = null;
        do{
            chosenLane = chooseLane();
        }while(chosenLane.isEmpty());
        
        tryToServeLane(chosenLane);
    }

    private void initInputLanesRandomTable() {
        final double step = 1 / inputLanes.size();
        double key = step;
        
        for (Lane inputLane : inputLanes) {
            inputLanesRandomTable.put(key, inputLane);
            key += step;
        }
    }

    private Lane chooseLane() {
        double random = Math.random();
        return inputLanesRandomTable.get(random);
    }

    private void tryToServeLane(Lane chosenLane) {
        int carCounter = 0;
        while(carCounter < batchSize){
            if(!tryTransferVehicle(chosenLane)){
                break;
            }
            carCounter++;
        }
    }


}
