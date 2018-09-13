package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

public class ADPhysicalVehicle extends PhysicalVehicle {

    private double posX;
    private double posY;

    public ADPhysicalVehicle(String vehicleId, EntityType type, double lengthInMeters, GraphType usingGraphTypeForMoving, SimulationNode position, double maxVelocity) {
        super(vehicleId, type, lengthInMeters, usingGraphTypeForMoving, position, maxVelocity);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setPosition(double x, double y) {
        posX = x;
        posY = y;
    }
}
