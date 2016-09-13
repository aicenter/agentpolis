package cz.agents.agentpolis.siminfrastructure.logger;

import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * 
 * The logger module for Guice framework.
 * 
 * @author Zbynek Moler
 * 
 */
public class LoggerModul extends AbstractModule {

    private final List<Object> loggers;

    public LoggerModul(List<Object> loggers) {
        super();
        this.loggers = loggers;
    }

    @Provides
    @Singleton
    PublishSubscribeLogger providePublishSubscribeLogger() {
        EventBus eventBus = new EventBus();
        for (Object publishSubscribeLogger : loggers) {
            eventBus.register(publishSubscribeLogger);
        }
        return new PublishSubscribeLogger(eventBus);
    }

    @Override
    protected void configure() {
        // TODO Auto-generated method stub

    }

}
