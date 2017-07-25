package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.viewer.historian.event;

import java.time.ZonedDateTime;

/**
 * Event view
 * @author Zbynek Moler
 *
 */
public interface ViewLogItem {

	public String getSource();

	public String getDescription() ;

	public String getHtmlDescription();

	public ViewLogItemType getType();

	public ZonedDateTime getTime();

	public int getStep();
	
}
