package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger;

import cz.agents.alite.common.event.EventProcessor;

/**
 * Each specific logger which you want to implement in AgentPolis should be
 * extended by this class
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class Logger {

    private final PublishSubscribeLogger publishSubscribeLogger;
    private final EventProcessor eventProcessor;

    public Logger(PublishSubscribeLogger publishSubscribeLogger, EventProcessor eventProcessor) {
        super();
        this.publishSubscribeLogger = publishSubscribeLogger;
        this.eventProcessor = eventProcessor;
    }

    /**
     * 
     * Return the current simulation time in millisecond. The simulation time is
     * measured from the start simulation (0 millisecond)
     * 
     * @return - value is in millisecond
     */
    protected long getCurrentSimulationTime() {
        return eventProcessor.getCurrentTime();
    }

    /**
     * 
     * Sends the implementation of {@code LogItem} into Guave event bus, The
     * {@code LogItem} could be subscribed by classes which are enhanced about
     * {@code Subscribe}
     * 
     * @param logItem
     */
    protected <TLogItem extends LogItem> void log(TLogItem logItem) {
        publishSubscribeLogger.log(logItem);
    }

}
