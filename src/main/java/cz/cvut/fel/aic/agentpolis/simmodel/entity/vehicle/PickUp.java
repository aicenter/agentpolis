/*
 * Copyright (C) 2018 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 * @param <T>
 * @param <E>
 */
public final class PickUp<T extends TransportableEntity, E extends TransportEntity> {

    public static <T extends TransportableEntity, E extends TransportEntity> void pickUp(T entity,
            boolean isVehicleFull, E transportingEntity, List<T> transportedEntities) {
        if (isVehicleFull) {
            try {
                throw new Exception(
                        String.format("Cannot pick up entity, the vehicle is full! [%s]", entity));
            } catch (Exception ex) {
                Logger.getLogger(PhysicalTransportVehicle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        transportedEntities.add(entity);
        entity.setTransportingEntity(transportingEntity);
    }

}
