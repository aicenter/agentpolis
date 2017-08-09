package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;

import org.apache.commons.lang.NotImplementedException;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.GraphType;

/**
 * 
 * The public transport locations representation wrapping needed the information for
 driver to be able to execute his/her plan
 * 
 * @author Zbynek Moler
 * 
 */
public class PTVehilceTrip extends GraphTrip<DepartureTripItem> {

    /**
     * 
     */
    private static final long serialVersionUID = -5087844407678432807L;

    public PTVehilceTrip(LinkedList<DepartureTripItem> trip, GraphType graphType){
        super(trip, graphType);
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        throw new NotImplementedException();

    }

    @Override
    public PTVehilceTrip clone() {
        LinkedList<DepartureTripItem> clonedTrip = new LinkedList<DepartureTripItem>();
        DepartureTripItemCloneVisotr cloneVisotr = new DepartureTripItemCloneVisotr();

        for (DepartureTripItem tripItem : locations) {
            tripItem.visitDepartureTripItem(cloneVisotr);
            clonedTrip.addLast(cloneVisotr.departureTripItemClone);

        }
		return new PTVehilceTrip(clonedTrip, graphType);
    }

    private static class DepartureTripItemCloneVisotr implements DepartureTripItemVisitor {

        public DepartureTripItem departureTripItemClone;

        @Override
        public void visitDepartureTripItem(NotWaitingDepartureTripItem notWaitingDepartureTripItem) {
            departureTripItemClone = new NotWaitingDepartureTripItem(
                    notWaitingDepartureTripItem.tripPositionByNodeId);

        }

        @Override
        public void visitDepartureTripItem(WaitingDepartureTripItem waitingDepartureTripItem) {
            departureTripItemClone = new WaitingDepartureTripItem(
                    waitingDepartureTripItem.tripPositionByNodeId,
                    waitingDepartureTripItem.departureTime, waitingDepartureTripItem.overMidnight);
        }

    }

}
