package cz.cvut.fel.aic.agentpolis.simmodel;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

public abstract class ADAgent extends Agent {

    private double posX;
    private double posY;

    public ADAgent(String agentId, SimulationNode position) {
        super(agentId, position);
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
}
