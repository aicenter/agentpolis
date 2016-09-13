package cz.agents.agentpolis.simmodel.environment.model.speed;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

/**
 * 
 * The holder of {@code SpeedInfluenceModel}
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class SpeedInfluenceModels {

    private final List<SpeedInfluenceModel> speedInfluenceModels = new ArrayList<SpeedInfluenceModel>();

    public void addSpeedInfluenceModel(SpeedInfluenceModel speedInfluenceModel) {
        speedInfluenceModels.add(speedInfluenceModel);
    }

    public List<SpeedInfluenceModel> getSpeedInfluenceModels() {
        return speedInfluenceModels;
    }
}
