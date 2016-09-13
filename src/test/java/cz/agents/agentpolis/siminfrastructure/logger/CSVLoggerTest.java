package cz.agents.agentpolis.siminfrastructure.logger;

import java.io.File;
import java.io.IOException;

import com.google.common.collect.ImmutableSet;

import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem.EndDrivingLogItem;
import cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem.PassengerStartedTravelLogItem;
import cz.agents.agentpolis.simulator.logger.subscriber.CSVLogSubscriber;

public class CSVLoggerTest {

    // @Test
    public void test() throws IOException {
        CSVLogSubscriber newInstance = CSVLogSubscriber
                .newInstance(ImmutableSet.<Class<? extends LogItem>> of(
                        PassengerStartedTravelLogItem.class, EndDrivingLogItem.class), new File(
                        "test.csv"));

        newInstance.logObject(new PassengerStartedTravelLogItem("publisherId", 8640000, 6.0, 12.0));
        newInstance.logObject(new PassengerStartedTravelLogItem("publisherId", 8640002, 6.0, 12.0));
        newInstance.logObject(new EndDrivingLogItem(8640002, "publisherId", "vehilce"));
        newInstance.closecsvWrite();
    }
}
