package cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event;

import java.awt.Color;

/**
 * Implementation view event
 * @author Zbynek Moler
 *
 */
public class ViewLogItmeTypeImpl implements ViewLogItemType {

	private final Color color;
	private String eventName;

	public ViewLogItmeTypeImpl(Color color, String eventName) {
		super();
		this.color = color;
		this.eventName = eventName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Color getColor() {
		return color;
	}
	
	@Override
	public String toString() {	
		return eventName;
	}
}
