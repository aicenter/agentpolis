  package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import cz.agents.multimodalstructures.edges.RoadEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SimulationEdge extends RoadEdge {

    private static final long serialVersionUID = -8040470592422680160L;

    /**
     * It holds graph types for which the edge is defined. It contains information about modes sharing the same space
     * (e.g. tramways with cars).
     */
    private final SetMultimap<GraphType, GraphType> graphTypes;
    private Map<GraphType, Integer> laneCounts;

    private SimulationEdge(int fromNodeId,
                           int toNodeId,
                           long wayID,
                           Set<ModeOfTransport> permittedModes,
                           float allowedMaxSpeedInMpS,
                           int lengthInMetres,
                           Map<GraphType, Integer> laneCounts,
                           SetMultimap<GraphType, GraphType> graphTypes) {
        super(fromNodeId, toNodeId, wayID, permittedModes, allowedMaxSpeedInMpS, lengthInMetres);
        this.graphTypes = graphTypes;
        this.laneCounts = laneCounts;
    }

    public SetMultimap<GraphType, GraphType> getGraphTypes() {
        return graphTypes;
    }

    public int getLaneCount(GraphType type) {
        Integer laneCount = laneCounts.get(type);
        return laneCount == null ? 0 : laneCount;
    }

    public Map<GraphType, Integer> getLaneCounts() {
        return laneCounts;
    }


    public static class SimulationEdgeBuilder {

        private static final float DEFAULT_MAX_SPEED_IN_MPS = 50 / 3.6f;
        private long wayID;
        private Set<ModeOfTransport> permittedModes;
        private float allowedMaxSpeedInMpS = DEFAULT_MAX_SPEED_IN_MPS;
        private int lengthInMetres;
        SetMultimap<GraphType, GraphType> graphTypes = HashMultimap.create();
        private Map<GraphType, Integer> laneCounts = new HashMap<>();

        public SimulationEdgeBuilder(int length, float allowedMaxSpeedInMpS, long wayID,
                                     Set<ModeOfTransport> permittedModes) {
            this.lengthInMetres = length;
            this.allowedMaxSpeedInMpS = allowedMaxSpeedInMpS;
            this.wayID = wayID;
            this.permittedModes = permittedModes;
            initLaneCounts(permittedModes);
            initGraphTypes(permittedModes);
        }

        public SimulationEdgeBuilder(RoadEdge roadEdge) {
            this(roadEdge.length, roadEdge.allowedMaxSpeedInMpS, roadEdge.wayID, roadEdge.getPermittedModes());
        }

        public SimulationEdgeBuilder(int laneCount, int length, GraphType graphType) {
            this.lengthInMetres = length;
            permittedModes = ModeOfTransportToGraphTypeConverter.convert(graphType);
            addType(graphType);
            addLaneCount(graphType, laneCount);
        }

        private void initGraphTypes(Set<ModeOfTransport> permittedModes) {
            permittedModes.stream().forEach(
                    modeOfTransport -> addType(ModeOfTransportToGraphTypeConverter.convert(modeOfTransport)));
        }

        private void initLaneCounts(Set<ModeOfTransport> permittedModes) {
            permittedModes.stream().forEach(
                    modeOfTransport -> addLaneCount(ModeOfTransportToGraphTypeConverter.convert(modeOfTransport), 1));

        }

        public SimulationEdge build(int fromNodeId, int toNodeId) {
            return new SimulationEdge(
                    fromNodeId,
                    toNodeId,
                    wayID,
                    permittedModes,
                    allowedMaxSpeedInMpS,
                    lengthInMetres,
                    laneCounts,
                    graphTypes);
        }

        public SimulationEdgeBuilder addType(GraphType type) {
            graphTypes.put(type, type);
            return this;
        }

        public SimulationEdgeBuilder addLaneCount(GraphType type, int laneCount) {
            laneCounts.put(type, laneCount);
            return this;
        }

        public SimulationEdgeBuilder addSharedType(GraphType newType, GraphType sharedType) {
            graphTypes.put(newType, newType);
            Set<GraphType> alreadyShared = new HashSet<>(graphTypes.get(sharedType)); //avoid concurrent modification exception
            for (GraphType type : alreadyShared) {
                graphTypes.put(newType, type);
                graphTypes.put(type, newType);
            }
            return this;
        }

        public SimulationEdgeBuilder removeType(GraphType type) {
            laneCounts.remove(type);
            Set<GraphType> toRemove = graphTypes.removeAll(type);
            for (GraphType toRem : toRemove) {
                graphTypes.remove(toRem, type);
            }
            return this;
        }

        public boolean containsGraphType(GraphType type) {
            return graphTypes.containsKey(type);
        }
    }
}
