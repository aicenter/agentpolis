package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.EdgeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalEdge;

import java.util.List;

public class SimulationEdgeFactory implements EdgeFactory<SimulationNode,SimulationEdge> {

    @Override
    public SimulationEdge createEdge(InternalEdge internalEdge, GraphBuilder<SimulationNode,SimulationEdge> graphBuilder) {
        List<GPSLocation> coordinatesList = internalEdge.get("coordinateList");
        EdgeShape edgeShape = new EdgeShape(coordinatesList);
		SimulationNode fromNode = graphBuilder.getNode(internalEdge.getFromNode().id);
        SimulationNode toNode = graphBuilder.getNode(internalEdge.getToNode().id);
        return new SimulationEdge(fromNode, toNode, internalEdge.get("wayID"),
                internalEdge.get("uniqueWayID"), internalEdge.get("oppositeWayUniqueId"), internalEdge.getLength(),
                internalEdge.get("allowedMaxSpeedInMpS"),
                internalEdge.get("lanesCount"), edgeShape);
    }

}
