package cz.agents.agentpolis.siminfrastructure.planner;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanners;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.NearestElementUtils;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.agents.basestructures.GPSLocation;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author fido
 */
@Singleton
public class TripsUtil {

    protected static final Set<GraphType> GRAPH_TYPES = new HashSet(Arrays.asList(EGraphType.HIGHWAY));


    protected final ShortestPathPlanners pathPlanners;

    private final NearestElementUtils nearestElementUtils;
    private final Graph<SimulationNode, SimulationEdge> network;


    @Inject
    public TripsUtil(ShortestPathPlanners pathPlanners, NearestElementUtils nearestElementUtils, HighwayNetwork network) {
        this.pathPlanners = pathPlanners;
        this.nearestElementUtils = nearestElementUtils;
        this.network = network.getNetwork();
    }


    public VehicleTrip locationsToVehicleTrip(List<Node> locations, boolean precomputedPaths, PhysicalVehicle vehicle) {
        ShortestPathPlanner pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);

        VehicleTrip finalTrip = null;
        LinkedList<TripItem> tripItems = new LinkedList<>();

        int startNodeId = locations.get(0).getId();

        if (precomputedPaths) {
            tripItems.add(new TripItem(startNodeId));
        }

        for (int i = 1; i < locations.size(); i++) {
            int targetNodeId = locations.get(i).getId();
            if (startNodeId == targetNodeId) {
                try {
                    throw new Exception("There can't be two identical locations in a row");
                } catch (Exception ex) {
                    Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (precomputedPaths) {
                tripItems.add(new TripItem(targetNodeId));
            } else {
                try {
                    VehicleTrip partialTrip = pathPlanner.findTrip(vehicle.getId(), startNodeId, targetNodeId);
                    while (partialTrip.hasNextTripItem()) {
                        tripItems.add(partialTrip.getAndRemoveFirstTripItem());
                    }

                } catch (TripPlannerException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            startNodeId = targetNodeId;
        }

        finalTrip = new VehicleTrip(tripItems, EGraphType.HIGHWAY, vehicle.getId());

        return finalTrip;
    }

    public VehicleTrip createTrip(int startNodeId, int targetNodeId, PhysicalVehicle vehicle) {
        if (startNodeId == targetNodeId) {
            try {
                throw new Exception("Start node cannot be the same as end node");
            } catch (Exception ex) {
                Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ShortestPathPlanner pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);

        VehicleTrip finalTrip = null;
        try {
            finalTrip = pathPlanner.findTrip(vehicle.getId(), startNodeId, targetNodeId);
        } catch (TripPlannerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return finalTrip;
    }

    public Trip<Node> createTrip(int startNodeId, int targetNodeId) {
        return createTrip(startNodeId, targetNodeId, GRAPH_TYPES);
    }

    public Trip<Node> createTrip(int startNodeId, int targetNodeId, GraphType graphType) {
        return createTrip(startNodeId, targetNodeId, new HashSet(Arrays.asList(graphType)));
    }

    public Trip<Node> createTrip(GPSLocation startLocation, GPSLocation targetLocation, GraphType graphType) {
        int startNodeId = nearestElementUtils.getNearestElement(startLocation, graphType).id;
        int targetNodeId = nearestElementUtils.getNearestElement(targetLocation, graphType).id;
        return createTrip(startNodeId, targetNodeId, new HashSet(Arrays.asList(graphType)));
    }

    public Trip<Node> createTrip(int startNodeId, int targetNodeId, Set<GraphType> graphTypes) {
        if (startNodeId == targetNodeId) {
            try {
                throw new Exception("Start node cannot be the same as end node");
            } catch (Exception ex) {
                Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ShortestPathPlanner pathPlanner = pathPlanners.getPathPlanner(graphTypes);

        Trip finalTrip = null;
        try {
            finalTrip = pathPlanner.findTrip(startNodeId, targetNodeId);
        } catch (TripPlannerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return finalTrip;
    }

    public static VehicleTrip mergeTrips(VehicleTrip<TripItem>... trips) {
        int i = 0;
        VehicleTrip firstTrip = null;
        do {
            firstTrip = trips[i];
            i++;
        } while (firstTrip == null);

        VehicleTrip<TripItem> newTrip = new VehicleTrip<>(new LinkedList<>(), firstTrip.getGraphType(), firstTrip.getVehicleId());

        for (int j = 0; j < trips.length; j++) {
            VehicleTrip<TripItem> trip = trips[j];
            if (trip != null) {
                for (TripItem location : trip.getLocations()) {
                    newTrip.extendTrip(location);
                }
            }
        }
        return newTrip;
    }

    private int getEdgeLength(int startNodeId, int targetNodeId) {
        return network.getEdge(startNodeId, targetNodeId).getLength();
    }


    public int getTripLengthInMeters(Trip<Node> trip) {
        int length = 0;

        LinkedList<Node> locations = trip.getLocations();
        if (locations.size() >= 2) {
            int startNodeId = locations.getFirst().id;
            for (int i = 1; i < locations.size(); i++) {
                int targetNodeId = locations.get(i).id;
                length += getEdgeLength(startNodeId, targetNodeId);
                startNodeId = targetNodeId;
            }
        }

        return length;
    }

}
