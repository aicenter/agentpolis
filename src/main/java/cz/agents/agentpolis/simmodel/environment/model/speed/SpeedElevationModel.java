package cz.agents.agentpolis.simmodel.environment.model.speed;

import java.util.Map;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.key.GraphFromToNodeKey;

/**
 * It calculates speed based on elevation of vertices.
 *
 * @author Marek Cuchy
 *
 */
public class SpeedElevationModel implements SpeedInfluenceModel {

    private final Map<GraphFromToNodeKey, Double> speedCoeffs;

    public SpeedElevationModel(Map<GraphFromToNodeKey, Double> speedCoeffs) {
        this.speedCoeffs = speedCoeffs;
    }

    @Override
    public double computedInfluencedSpeed(GraphType graphType, long fromNodeByNodeId,
            long toNodeByNodeId, double originSpeedInmps, double influencedSpeedInmps) {
        GraphFromToNodeKey key = new GraphFromToNodeKey(graphType, fromNodeByNodeId, toNodeByNodeId);
        Double speedCoeff = speedCoeffs.get(key);

        if (speedCoeff != null) {
            return speedCoeff * influencedSpeedInmps;
        }

        return influencedSpeedInmps;
    }
}
