package cz.agents.agentpolis.simulator.visualization.visio.viewer;

import com.google.common.eventbus.Subscribe;
import cz.agents.agentpolis.siminfrastructure.logger.LogItem;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItemImpl;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItemType;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItmeTypeImpl;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.vis.NewHistorianView;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * The viewer is based on origin "Historian view" from AgentC
 * 
 * @author Zbynek Moler
 * 
 */
public class LogItemViewer {

    private static final long HALF_MINUTE_FACTOR = 1000; // 1000 millis =
                                                         // 1s
    private static final int MAX_VALUE_PER_COLOR = 255;

    private static final Logger logger = Logger.getLogger(LogItemViewer.class);

    private final Map<Class<? extends LogItem>, ViewLogItmeTypeImpl> historyEventTypes = new HashMap<>();
    private NewHistorianView newHistorianView;

    private final Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer;
    private final TimeProvider timeProvider;
    private final long simulationDuration;

    public LogItemViewer(Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer,
                         final TimeProvider timeProvider, final long simulationDuration) {
        super();
        this.allowedLogItemClassesLogItemViewer = allowedLogItemClassesLogItemViewer;
        this.timeProvider = timeProvider;
        this.simulationDuration = simulationDuration;
    }

    @Subscribe
    public void logObject(Object logItem) {
        Class<?> logItemClass = logItem.getClass();
        if (allowedLogItemClassesLogItemViewer.contains(logItemClass)) {
            update(mapCommonLogItemToSpecific(logItemClass));
        }
    }

    /**
     * Maps common event to suitable event for event view.
     */
    public ViewLogItemImpl mapCommonLogItemToSpecific(Class<?> logItemClass) {
        ViewLogItmeTypeImpl wrapper = historyEventTypes.get(logItemClass);

        String eventName = logItemClass.getSimpleName();
        int currentTimeOfSimulationInSecond = Integer.MAX_VALUE;
        long currentTimeOfSimInSecond = (timeProvider.getCurrentSimTime() / HALF_MINUTE_FACTOR);

        if (currentTimeOfSimInSecond < Integer.MAX_VALUE) {
            currentTimeOfSimulationInSecond = (int) currentTimeOfSimInSecond;
        }

        return new ViewLogItemImpl(wrapper, "", eventName, "", timeProvider.getCurrentZonedDateTime(), currentTimeOfSimulationInSecond);
    }

    /**
     * Updates and sets new event view
     */
    public void update(ViewLogItemImpl specificEvent) {
        newHistorianView.stepChanged(specificEvent.getStep());
        newHistorianView.update(specificEvent);

    }

    /**
     * Invokes GUI window.
     */
    public void runView() {
        int numberOfEvents = allowedLogItemClassesLogItemViewer.size();

        ViewLogItemType[] historyEventTypeArray = new ViewLogItemType[numberOfEvents];

        int colorFactor = MAX_VALUE_PER_COLOR;
        if (allowedLogItemClassesLogItemViewer.size() > 0) {
            colorFactor /= allowedLogItemClassesLogItemViewer.size();
        }

        int i = 0;
        for (Class<? extends LogItem> enumEvent : allowedLogItemClassesLogItemViewer) {

            int colorRed = colorFactor * i;
            int colorGreen = colorFactor * (numberOfEvents - i);
            int colorBlue = colorGreen;

            ViewLogItmeTypeImpl wrapper = new ViewLogItmeTypeImpl(new Color(colorRed, colorGreen,
                    colorBlue), enumEvent.getSimpleName());
            historyEventTypes.put(enumEvent, wrapper);
            historyEventTypeArray[i++] = wrapper;
        }

        this.newHistorianView = new NewHistorianView(historyEventTypeArray, simulationDuration);

        try {
            SwingUtilities.invokeAndWait(() -> {
				JFrame jf = new JFrame();
				jf.getContentPane().add(newHistorianView);
				jf.pack();
				jf.setSize(new Dimension(1150, 750));
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setVisible(true);
			});
        } catch (InterruptedException | InvocationTargetException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

}
