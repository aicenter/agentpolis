package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.utils;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PlannerEdge extends DefaultWeightedEdge{

	private static final long serialVersionUID = 3299311676953392069L;
    
    public final int fromPosition;
	public final int toPosition;
	public final double distance;

	public PlannerEdge(int fromPosition, int toPosition, double distance) {
		super();
		this.fromPosition = fromPosition;
		this.toPosition = toPosition;
		this.distance = distance;
	}
}
