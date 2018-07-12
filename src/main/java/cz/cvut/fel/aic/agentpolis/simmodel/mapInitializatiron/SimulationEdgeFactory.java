package cz.cvut.fel.aic.agentpolis.simmodel.mapInitializatiron;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.EdgeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalEdge;

import java.util.List;

public class SimulationEdgeFactory implements EdgeFactory<SimulationEdge> {

    @Override
    public SimulationEdge createEdge(InternalEdge internalEdge) {
        List<GPSLocation> coordinatesList = internalEdge.get("coordinateList");
        EdgeShape edgeShape = new EdgeShape(coordinatesList);
        return new SimulationEdge(internalEdge.fromId, internalEdge.toId, internalEdge.get("wayID"),
                internalEdge.get("uniqueWayID"), internalEdge.get("oppositeWayUniqueId"), internalEdge.getLength(),
                internalEdge.get("allowedMaxSpeedInMpS"),
                internalEdge.get("lanesCount"), edgeShape);
    }

}
