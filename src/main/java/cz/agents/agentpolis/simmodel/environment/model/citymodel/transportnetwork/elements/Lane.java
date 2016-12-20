package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zdenek Bousa
 */
public class Lane {
    private static final Logger LOGGER = Logger.getLogger(Lane.class);
    private final long laneUniqueId;
    private final String parentEdgeUniqueId;
    private List<Integer> heading = new LinkedList<>();


    public Lane(long laneUniqueId, String parentEdgeUniqueId) {
        LOGGER.warn("NotImplementedException", new NotImplementedException());
        this.laneUniqueId = laneUniqueId;
        this.parentEdgeUniqueId = parentEdgeUniqueId;
    }

    public int[] getHeadingIds() {
        return null;
    }

    public void addHeadingEdgeId(int headingEdgeID) {

    }

    public long getLaneUniqueId() {
        return laneUniqueId;
    }

    public String getParentEdgeUniqueId() {
        return parentEdgeUniqueId;
    }

    @Singleton
    public static class LaneBuilder {
        private long laneCounter = 0;

        public Lane createNewLane(String parent) {
            return new Lane(0, "");
        }

        public LaneBuilder getBuilder() {
            return this;
        }
    }
}
