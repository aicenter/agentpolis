package cz.cvut.fel.aic.agentpolis.simmodel.agent.activity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;

import cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.callback.TimeActivityCallback;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
import cz.agents.alite.common.event.EventProcessor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * New terminology: It is the activity called Wait
 * 
 * This activity provides agent to spend same simulation time. It is specific
 * activity, because takes {@code EventProcessor}.
 * 
 * @author Zbynek Moler
 * 
 */
public class TimeSpendingActivity {

    private final EventProcessor eventProcessor;

    @Inject
    public TimeSpendingActivity(EventProcessor eventProcessor) {
        super();
        this.eventProcessor = eventProcessor;
    }

    public void spendingTime(final TimeActivityCallback timeCallback,
            long durationFromCurrentTimeInMillis) {
        checkNotNull(timeCallback);
        checkArgument(durationFromCurrentTimeInMillis > 0,
                "It is not possible to set up duration less or equal then zero");

        eventProcessor.addEvent(new EventHandlerAdapter() {

            @Override
            public void handleEvent(Event event) {

                timeCallback.timeCallback();

            }

        }, durationFromCurrentTimeInMillis);

    }

    /** sets alarm for specified duration */
    public void spendingTime(TimeActivityCallback sensingTime, Duration durationFromCurrentTime) {
        spendingTime(sensingTime, durationFromCurrentTime.toMillis());
    }

}
