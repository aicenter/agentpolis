package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

import cz.agents.alite.common.event.EventType;

public enum AgentdriveEventType implements EventType {
    INITIALIZE,
    UPDATE_PLAN,
    DATA,
    UPDATE_EDGE,
    FINISH
}
