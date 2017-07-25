package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

/**
 * To visit a a particular type of trip object is possible to this via this the
 * implementation of the interface
 * 
 * @author Zbynek Moler
 * 
 */
public interface TripVisitior {

    public void visitTrip(WalkTrip walkTrip);

    public void visitTrip(PTTrip ptTrip);

    public void visitTrip(VehicleTrip vehicleTrip);

    public void visitTrip(TeleportTrip teleportTrip);
}
