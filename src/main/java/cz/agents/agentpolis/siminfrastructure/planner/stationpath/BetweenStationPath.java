package cz.agents.agentpolis.siminfrastructure.planner.stationpath;

import java.io.Serializable;

/**
 * 
 * The wrapper for path between two stations. The path is represented by the
 * sequence of non-station positions
 * 
 * @author Zbynek Moler
 * 
 */
public class BetweenStationPath implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6841812949152626717L;
    
    public final int[] previousNonStationNodesByNodeId;

    public BetweenStationPath(int[] previousNonStationNodesByNodeId) {
        super();
        this.previousNonStationNodesByNodeId = previousNonStationNodesByNodeId;
    }

}
