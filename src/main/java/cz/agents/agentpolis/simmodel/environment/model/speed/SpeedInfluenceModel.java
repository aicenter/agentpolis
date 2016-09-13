package cz.agents.agentpolis.simmodel.environment.model.speed;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * 
 * The speed of a vehicle could be affected by the implementation of this
 * interface
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
public interface SpeedInfluenceModel {

    /**
     * 
     * Computes the new speed value for movement between two position, the
     * returned value has to be grater then zero
     * 
     * @param graphType
     * @param fromNodeByNodeId
     * @param toNodeByNodeId
     * @param originSpeedInmps
     * @param influencedSpeedInmps
     * @return
     */
    public double computedInfluencedSpeed(GraphType graphType, long fromNodeByNodeId,
            long toNodeByNodeId, double originSpeedInmps, double influencedSpeedInmps);

}
