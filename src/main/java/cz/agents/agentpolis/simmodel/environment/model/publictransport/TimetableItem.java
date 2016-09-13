package cz.agents.agentpolis.simmodel.environment.model.publictransport;

import java.io.Serializable;

/**
 * 
 * It represent a the one record relating with time for public transport
 * including the over midnight tag
 * 
 * @author Zbynek Moler
 * 
 */
public class TimetableItem implements Cloneable, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7706772885407304705L;

    /**
     * Time in milliseconds
     */
    public final int time;
    
    public final boolean overMidnight;

    public TimetableItem(long time, boolean overMidnight) {
        this((int) time, overMidnight);
    }

    public TimetableItem(int time, boolean overMidnight) {
        super();
        this.time = time;
        this.overMidnight = overMidnight;
    }

    @Override
    public String toString() {
        return "TimeTableItem [time=" + time + ", overMidnight=" + overMidnight + "]";
    }
}
