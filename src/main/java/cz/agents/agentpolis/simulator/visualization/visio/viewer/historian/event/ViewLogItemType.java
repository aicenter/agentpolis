package cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event;

import java.awt.Color;

/**
 * Type of event
 * @author Zbynek Moler
 *
 */
public interface ViewLogItemType {

	public Color getColor();
	
	public String getEventName();
	
	public String toString();
}
