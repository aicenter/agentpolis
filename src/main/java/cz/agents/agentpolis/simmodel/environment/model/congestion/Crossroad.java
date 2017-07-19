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
 *
 * @author fido
 */
public class Crossroad  extends Connection{
    private final Map<SimulationNode,Link> linksMappedByNextNodes;
    
    private final double maxFlow;
    
    private final int laneCount;
    
    private final int tickLength;
    
    private final int batchSize;
    
    private final List<ChoosingTableData> inputLanesChoosingTable;
    
    private final List<Lane> inputLanes;
	
	private final Map<Lane,Link> outputLinksMappedByInputLanes;
    
    private final Map<Connection,Link> outputLinksMappedByNextConnections;

    public Crossroad(Config config, SimulationProvider simulationProvider, CongestionModel congestionModel,
            SimulationNode node) {
        super(simulationProvider, config, congestionModel, node);
        this.linksMappedByNextNodes = new HashMap<>();
        inputLanes = new LinkedList<>();
        inputLanesChoosingTable = new LinkedList<>();
		outputLinksMappedByInputLanes = new HashMap<>();
        outputLinksMappedByNextConnections = new HashMap<>();
        batchSize = config.congestionModel.batchSize;
        laneCount = getLaneCount();
        maxFlow = computeMaxFlow(config);
//        tickLength = computeTickLength();
        tickLength =congestionModel.config.congestionModel.connectionTickLength;
    }
    
    
    
    public int getNumberOfInputLanes(){
        return inputLanes.size();
    }

    private void addInputLane(Lane lane){
        inputLanes.add(lane);
    }
    
    void init(){
        initInputLanesRandomTable();
    }

    @Override
    protected void serveLanes() {
        Lane chosenLane = null;
        
        List<Lane> nonEmptyLanes = new LinkedList();
        for (Lane inputLane : inputLanes) {
            if(inputLane.hasWaitingVehicles()){
                nonEmptyLanes.add(inputLane);
            }
        }
        
        if(nonEmptyLanes.isEmpty()){
            return;
        }
        else if(nonEmptyLanes.size() == 1){
            chosenLane = nonEmptyLanes.get(0);
        }
        else{
            do{
                chosenLane = chooseLane();
            }while(!chosenLane.hasWaitingVehicles());
        }
        
        tryToServeLane(chosenLane);
        
        // wake up after some time
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, 
                tickLength);
    }
    
    
    
    
    private double computeMaxFlow(Config config){
        return config.congestionModel.maxFlowPerLane * 2;
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
            if(random <= choosingTableData.threshold){
                chosenLane = choosingTableData.inputLane;
                break;
            }
        }
        return chosenLane;
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
	
	private final class ChoosingTableData{
        final double threshold;
        
        final Lane inputLane;

        public ChoosingTableData(double threshold, Lane inputLane) {
            this.threshold = threshold;
            this.inputLane = inputLane;
        }
        
        
    }   

}
