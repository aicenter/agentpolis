package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util.Utils;
import cz.cvut.fel.aic.alite.common.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public class VehicleActuator {

    private HighwayStorage storage;
    private int id;
    private Entity entity;
    private HighwayEnvironment highwayEnvironment;

    public VehicleActuator(HighwayEnvironment environment, Entity relatedEntity, HighwayStorage storage) {
        this.highwayEnvironment = environment;
        this.entity = relatedEntity;
        this.storage = storage;
        this.id = Utils.name2ID(relatedEntity.getName());
    }

    public void act(Action action) {
        List<Action> actions = new LinkedList<Action>();
        actions.add(action);
        act(actions);
    }

    public List<Action> act(List<Action> actions) {
        storage.act(id, actions);
        return actions;
    }

    public String getNameOfRelatedEntity() {
        return entity.getName();
    }
}
