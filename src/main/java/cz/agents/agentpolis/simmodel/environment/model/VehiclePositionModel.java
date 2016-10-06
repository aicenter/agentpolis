package cz.agents.agentpolis.simmodel.environment.model;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.agentpolis.utils.key.KeyWithString;
import cz.agents.alite.common.event.EventProcessor;

/**
 * The vehicle position holder and provider
 * <p/>
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 *
 * @author Zbynek Moler
 */
@Singleton
public class VehiclePositionModel extends EntityPositionModel {

    @Inject
    public VehiclePositionModel(Map<String, Set<PositionUpdated>> sensingPositionNodeMap,
            Map<KeyWithString, Set<PositionUpdated>> callbackBoundedWithPosition,
            EventProcessor eventProcessor) {
        super(sensingPositionNodeMap, callbackBoundedWithPosition,
                eventProcessor);
    }

}
