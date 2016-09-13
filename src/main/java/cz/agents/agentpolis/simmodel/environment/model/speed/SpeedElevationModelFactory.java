package cz.agents.agentpolis.simmodel.environment.model.speed;

import java.util.HashMap;
import java.util.Map;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.key.GraphFromToNodeKey;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Edge;

/**
 *
 * It calculates speed coefficient based on elevation of vertices. The speed
 * calculation is taken from
 * eu.superhub.wp5.plannercore.structures.graphs.BikeTimedGraph
 *
 * @author Marek Cuchy
 *
 */
public class SpeedElevationModelFactory {

    private Map<GraphFromToNodeKey, Double> speedCoeffs = new HashMap<>();

    public void addGraph(GraphType graphType, Graph<?, ?> graph) {
        for (Edge edge : graph.getAllEdges()) {
            int fromNodeId = edge.fromId;
            int toNodeId = edge.toId;

            double fromElevation = graph.getNode(fromNodeId).elevation;
            double toElevation = graph.getNode(toNodeId).elevation;

            double elevationGain = toElevation - fromElevation;

            double l = edge.length;
            double rises = (elevationGain > 0) ? elevationGain : 0;
            double drops = (elevationGain < 0) ? -elevationGain : 0;

            double coeff = l * getDownhillSpeedMultiplier(drops, l) / (l + 8 * rises);
            speedCoeffs.put(new GraphFromToNodeKey(graphType, edge), coeff);
        }
    }

    public SpeedElevationModel createModel() {
        SpeedElevationModel model = new SpeedElevationModel(speedCoeffs);
        speedCoeffs.clear();
        return model;
    }

    private double getDownhillSpeedMultiplier(double drop, double length) {
        double MAXIMUM_DOWNHILL_SPEED_MULTIPLIER = 2.5; // s_dmax
        double CRITICAL_DOWNHILL_GRADE = 0.1; // d'c

        double downhillGrade = (length == 0d) ? 0 : drop / length;
        if (downhillGrade > CRITICAL_DOWNHILL_GRADE)
            return MAXIMUM_DOWNHILL_SPEED_MULTIPLIER;
        else
            return (((downhillGrade / CRITICAL_DOWNHILL_GRADE) * (MAXIMUM_DOWNHILL_SPEED_MULTIPLIER - 1)) + 1);
    }

}
