/* 
 * Copyright (C) 2017 fido.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity;


import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

import java.util.List;

/**
 * @param <A>
 * @author fido
 */
public class VehicleMove<A extends Agent & Driver> extends Move<A> {

    public VehicleMove(ActivityInitializer activityInitializer,
                       TypedSimulation eventProcessor, A agent, SimulationEdge edge, SimulationNode from, SimulationNode to) {
        super(activityInitializer, eventProcessor, agent, edge, from, to);

    }

    @Override
    protected void performAction() {
        if (agent instanceof Driver && agent.getVehicle() != null) {
            moveVehicle(agent.getVehicle());
        }
        super.performAction();
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
