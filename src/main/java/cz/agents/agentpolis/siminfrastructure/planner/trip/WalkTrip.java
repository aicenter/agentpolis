package cz.agents.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * The implementation is a specific type of a general trip {@code Trip}
 * representation. It represents a walk trip.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class WalkTrip extends Trip<TripItem> {

    /**
     * 
     */
    private static final long serialVersionUID = 2416011174348898223L;

    public WalkTrip(LinkedList<TripItem> trip, GraphType graphType) {
        super(trip, graphType);
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        tripVisitior.visitTrip(this);

    }

    @Override
    public WalkTrip clone() {
        LinkedList<TripItem> clonedTrip = new LinkedList<TripItem>();
        for (TripItem node : trip) {
            clonedTrip.addLast(new TripItem(node.tripPositionByNodeId));

        }
        return new WalkTrip(clonedTrip, graphType);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("WalkTrip: ");
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}
