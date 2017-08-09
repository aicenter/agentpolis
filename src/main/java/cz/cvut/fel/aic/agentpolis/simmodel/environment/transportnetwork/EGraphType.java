package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork;

/**
 * Definition of based graphs used in AgentPolis
 * 
 * @author Zbynek Moler
 * 
 */
public enum EGraphType implements GraphType {
    PEDESTRIAN, METROWAY, HIGHWAY, TRAMWAY, BUSWAY, TROLLEYBUSWAY, RAILWAY, BIKEWAY;
}
