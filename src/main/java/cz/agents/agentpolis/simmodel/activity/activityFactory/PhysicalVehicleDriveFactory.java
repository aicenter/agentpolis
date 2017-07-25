/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

/**
 * @author fido
 */
public interface PhysicalVehicleDriveFactory {
    public <A extends Agent & Driver> void runActivity(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip);

    public <A extends Agent & Driver> Activity<A> create(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip);
}
