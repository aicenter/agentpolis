package cz.agents.agentpolis.siminfrastructure.logger;

/**
 * The most of implementations of {@code LogItem} hold the same information
 * therefore this class provides this common information
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class ALogItem implements LogItem {
    public final String agentId;
    public final long simulationTime;

    public ALogItem(String publisherId, long simulationTime) {
        super();
        this.agentId = publisherId;
        this.simulationTime = simulationTime;
    }
}
