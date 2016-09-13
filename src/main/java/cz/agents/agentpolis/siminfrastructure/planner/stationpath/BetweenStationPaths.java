package cz.agents.agentpolis.siminfrastructure.planner.stationpath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.key.GraphFromToNodeKey;
import cz.agents.agentpolis.utils.InitAndGetterUtil;
import cz.agents.agentpolis.utils.key.Key;

/**
 * 
 * The storage of paths between stations
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class BetweenStationPaths implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3540234292459158845L;
    
    private Map<GraphFromToNodeKey, List<BetweenStationPath>> betweenStationPaths = new HashMap<>();
    private Set<String> skippedTripIds = new HashSet<>();

    /**
     * 
     * Adds a path (a sequence of non-station nodes) between two stations
     * 
     * @param fromStationByNodeId
     * @param toStationByNodeId
     * @param previousNonStationNodesByNodeId
     * @param graphType
     */
    public void addBetweenStationPath(int fromStationByNodeId, int toStationByNodeId,
            int[] previousNonStationNodesByNodeId, GraphType graphType) {

        GraphFromToNodeKey graphFromToNodeKey = new GraphFromToNodeKey(graphType,
                fromStationByNodeId, toStationByNodeId);

        List<BetweenStationPath> betweenStationPathsTmp = InitAndGetterUtil.getDataOrInitFromMap(
                betweenStationPaths, graphFromToNodeKey, new ArrayList<>());

        betweenStationPathsTmp.add(new BetweenStationPath(previousNonStationNodesByNodeId));
        betweenStationPaths.put(graphFromToNodeKey, betweenStationPathsTmp);

    }

    /**
     * Return paths between given stations
     * 
     * @param fromStationByNodeId
     * @param toStationByNodeId
     * @param graphType
     * @return
     */
    public List<BetweenStationPath> getFromStationByNodeId(long fromStationByNodeId,
            long toStationByNodeId, GraphType graphType) {
        return betweenStationPaths.get(new GraphFromToNodeKey(graphType, fromStationByNodeId,
                toStationByNodeId));
    }

    /**
     * 
     * Return a trip ids for which do not exists path between some of stations
     * 
     * @return
     */
    public Set<String> getSkippedTripIds() {
        return skippedTripIds;
    }

    /**
     * 
     * Checks if given trip id was skipped
     * 
     * @param tripId
     * @return
     */
    public boolean isTripIdSkipped(String tripId) {
        return skippedTripIds.contains(tripId);
    }

    /**
     * 
     * Adds trip id as skipped
     * 
     * @param tripId
     */
    public void addSkippedTripId(String tripId) {
        skippedTripIds.add(tripId);
    }

    public static class KeyWithGroupId {
        private final Key key;
        private final String groupId;

        public KeyWithGroupId(int fromStationByNodeId, int toStationByNodeId, String groupId) {
            super();
            this.key = new Key(fromStationByNodeId, toStationByNodeId);
            this.groupId = groupId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            KeyWithGroupId other = (KeyWithGroupId) obj;
            if (groupId == null) {
                if (other.groupId != null)
                    return false;
            } else if (!groupId.equals(other.groupId))
                return false;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            return true;
        }

    }

}
