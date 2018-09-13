package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.ADAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.PhysicalVehicleDrive;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.ADDriver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.ADPhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

public interface ADVehicleDriveFactory {
    public <A extends ADAgent & ADDriver> void runActivity(A agent, ADPhysicalVehicle vehicle, Trip<SimulationNode> trip);

    public <A extends ADAgent & ADDriver> PhysicalVehicleDrive<A> create(A agent, ADPhysicalVehicle vehicle, Trip<SimulationNode> trip);
    public <A extends ADAgent & ADDriver> PhysicalVehicleDrive<A> create(A agent, ADPhysicalVehicle vehicle, SimulationNode target);
}
