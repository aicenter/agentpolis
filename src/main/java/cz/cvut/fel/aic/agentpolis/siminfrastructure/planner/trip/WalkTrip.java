package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.GraphType;

/**
 * The implementation is a specific type of a general locations {@code GraphTrip}
 * representation. It represents a walk locations.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class WalkTrip extends GraphTrip<TripItem> {

    /**
     * 
     */
    private static final long serialVersionUID = 2416011174348898223L;

    public WalkTrip(LinkedList<TripItem> trip, GraphType graphType){
        super(trip, graphType);
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        tripVisitior.visitTrip(this);

    }

    @Override
    public WalkTrip clone() {
        LinkedList<TripItem> clonedTrip = new LinkedList<TripItem>();
        for (TripItem node : locations) {
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
