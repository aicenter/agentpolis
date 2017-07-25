package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cz.agents.agentpolis.apgooglearth.density.IDensityGE;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.basestructures.Node;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * 
 * The density for the selected entities
 * 
 * @author Zbynek Moler
 * 
 */
public class DensityGE implements IDensityGE {

    private final EntityPositionModel entityPositionStorage;
    private final Map<Integer, ? extends Node> nodesFromAllGraphs;
    private final Set<String> allowedEntitiesIds;

    public DensityGE(EntityPositionModel entityPositionStorage, Map<Integer, ? extends Node> nodesFromAllGraphs,
            Set<String> allowedEntitiesIds) {
        super();
        this.entityPositionStorage = entityPositionStorage;
        this.nodesFromAllGraphs = nodesFromAllGraphs;
        this.allowedEntitiesIds = allowedEntitiesIds;
    }

    public Map<Coordinate, Long> getCoordinateEntitiesInRegionBounds(RegionBounds regionBounds) {

        Map<Coordinate, Long> counterEntitiesPerNode = new HashMap<>();

        for (String entityId : allowedEntitiesIds) {

            Integer entityPositionByNodeId = entityPositionStorage.getEntityPositionByNodeId(entityId);
            Node node = nodesFromAllGraphs.get(entityPositionByNodeId);
            double lat = node.getLatitude();
            double lon = node.getLongitude();

            if (regionBounds.contains(lon, lat)) {

                Coordinate coordinate = new Coordinate(lon, lat);

                Long counter = counterEntitiesPerNode.get(coordinate);
                if (counter == null) {
                    counter = 0L;
                }
                counter++;

                counterEntitiesPerNode.put(coordinate, counter);
            }

        }

        return counterEntitiesPerNode;
    }

}
