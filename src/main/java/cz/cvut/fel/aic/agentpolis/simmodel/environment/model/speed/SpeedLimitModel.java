package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.speed;

import java.util.Map;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.key.GraphFromToNodeKey;

/**
 * 
 * This implementation of {@code SpeedInfluenceModel} influences the speed of
 * vehicle on a particular a road segment. The influence is determined according
 * to a speed limit for a particular road segment.
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
public class SpeedLimitModel implements SpeedInfluenceModel {

    private final Map<GraphFromToNodeKey, Double> speedLimistInmpsForSpecificSegments;

    public SpeedLimitModel(Map<GraphFromToNodeKey, Double> speedLimistInmpsForSpecificSegments) {
        super();
        this.speedLimistInmpsForSpecificSegments = speedLimistInmpsForSpecificSegments;
    }

    @Override
    public double computedInfluencedSpeed(GraphType graphType, long fromNodeByNodeId,
            long toNodeByNodeId, double originSpeedInmps, double influencedSpeedInmps) {

        GraphFromToNodeKey key = new GraphFromToNodeKey(graphType, fromNodeByNodeId, toNodeByNodeId);
        Double speedLimit = speedLimistInmpsForSpecificSegments.get(key);

        if (speedLimit != null && influencedSpeedInmps > speedLimit) {
            assert speedLimit > 0 : "Speed limit was set on zero";

            return speedLimit;
        }

        return influencedSpeedInmps;
    }

}
