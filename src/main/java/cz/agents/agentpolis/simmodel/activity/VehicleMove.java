/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;


import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.agent.TransportEntity;
import cz.agents.agentpolis.simmodel.entity.TransportableEntity;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

import java.util.List;

/**
 * @param <A>
 * @author fido
 */
public class VehicleMove<A extends Agent & Driver> extends Move<A> {

    public VehicleMove(ActivityInitializer activityInitializer,
                       TypedSimulation eventProcessor, A agent, Edge edge, Node from, Node to) {
        super(activityInitializer, eventProcessor, agent, edge, from, to);

    }

    @Override
    protected void finishMove() {
        if (agent instanceof Driver && agent.getVehicle() != null) {
            moveVehicle(agent.getVehicle());
        }
        super.finishMove();
    }

    private void moveVehicle(Vehicle vehicle) {
        vehicle.setPosition(to);
        if (vehicle instanceof TransportEntity && !((TransportEntity) vehicle).getTransportedEntities().isEmpty()) {
            moveTransportedEntities(((TransportEntity) vehicle).getTransportedEntities());
        }
    }

    private void moveTransportedEntities(List<TransportableEntity> transportedEntities) {
        for (TransportableEntity transportedEntity : transportedEntities) {
            transportedEntity.setPosition(to);
        }
    }


}
