package cz.cvut.fel.aic.agentpolis.simmodel.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.ADPhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

public interface ADDriver<V extends ADPhysicalVehicle> extends Driver<V>{

    public void setPosition(SimulationNode position);

    public void setPos(double x, double y);

    public double getPosX();

    public double getPosY();
}
