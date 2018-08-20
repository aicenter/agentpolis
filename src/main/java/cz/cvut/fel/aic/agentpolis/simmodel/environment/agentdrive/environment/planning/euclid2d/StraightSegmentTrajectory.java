package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid2d;


import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Point4d;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Straight;
import org.jgrapht.GraphPath;

/**
 * A wrapper that interprets a graph path in euclidean 2i + time graph as a trajectory.
 */

public class StraightSegmentTrajectory extends BasicSegmentedTrajectory {

    GraphPath<Point4d, Straight> graphPath;

    public StraightSegmentTrajectory(GraphPath<Point4d, Straight> graphPath, double duration) {

        super(graphPath.getEdgeList(), duration);
        this.graphPath = graphPath;
    }

}
