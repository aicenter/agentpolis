package cz.agents.agentpolis.siminfrastructure.planner.trip;


import java.util.LinkedList;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * The class represents a locations which relating with traveling as passenger or
 driver of a vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTrip<T extends TripItem> extends GraphTrip<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 4781120952110023091L;
    
    private final String vehicleId;

    public VehicleTrip(LinkedList<T> trip, GraphType graphType, String vehicleId){
        super(trip, graphType);
        this.vehicleId = checkNotNull(vehicleId);
    }

    public String getVehicleId() {
        return vehicleId;
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        tripVisitior.visitTrip(this);
    }

    @Override
    public VehicleTrip clone() {
        LinkedList<TripItem> clonedTrip = new LinkedList<TripItem>();
        for (TripItem node : locations) {
            clonedTrip.addLast(new TripItem(node.tripPositionByNodeId));

        }
		return new VehicleTrip(clonedTrip, graphType, vehicleId);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("VehicleTrip");
        stringBuilder.append('\n');
        stringBuilder.append(graphType);
        stringBuilder.append('(');
        if (locations.isEmpty() == false) {
            stringBuilder.append(locations.getFirst().tripPositionByNodeId);
            stringBuilder.append("------------>");
            stringBuilder.append(locations.getLast().tripPositionByNodeId);
        }

//        if (locations.isEmpty() == false) {
//            for (TripItem tripItem : locations) {
//                stringBuilder.append(" ---> ");
//                stringBuilder.append(tripItem);
//            }
//        }

        stringBuilder.append(')');
        stringBuilder.append(' ');
        //stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }

}
