package cz.agents.agentpolis.siminfrastructure.logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The wrapper of Guava's event bus. The main reason for wrapper is to avoid the
 * new registration into event bus and enforce to log just and log items which
 * implements {@code LogItem}
 * 
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class PublishSubscribeLogger {

    private final EventBus eventBus;

    @Inject
    public PublishSubscribeLogger(EventBus eventBus) {
        super();
        this.eventBus = eventBus;

    }

    /**
     * 
     * Sends the implementation of {@code LogItem} into Guave event bus, The
     * {@code LogItem} could be subscribed by classes which are enhanced about
     * {@code Subscribe}
     * 
     * @param logItem
     */
    public <TLogItem extends LogItem> void log(TLogItem logItem) {
        eventBus.post(logItem);
    }

}
