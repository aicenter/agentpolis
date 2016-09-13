package cz.agents.agentpolis.simmodel.environment.model;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.agentpolis.utils.key.KeyWithString;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The agent position holder and provider
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class AgentPositionModel extends EntityPositionModel {

    @Inject
    public AgentPositionModel(Map<String, Integer> entityPositionMap,
            Map<String, Set<PositionUpdated>> sensingPositionNodeMap,
            Map<KeyWithString, Set<PositionUpdated>> callbackBoundedWithPosition,
            EventProcessor eventProcessor) {
        super(entityPositionMap, sensingPositionNodeMap, callbackBoundedWithPosition,
                eventProcessor);
    }

}
