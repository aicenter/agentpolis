/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanners;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.NearestElementUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;

import java.util.*;
import org.slf4j.LoggerFactory;

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

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TripsUtil.class);
    
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
                    LOGGER.error(null, ex);
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
                    LOGGER.error(null, ex);
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
               LOGGER.error(null, ex);
            }
        }
        ShortestPathPlanner pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);

        VehicleTrip finalTrip = null;
        try {
            finalTrip = pathPlanner.findTrip(vehicle.getId(), startNodeId, targetNodeId);
        } catch (TripPlannerException ex) {
            LOGGER.error(null, ex);
        }

        return finalTrip;
    }

    public Trip<SimulationNode> createTrip(int startNodeId, int targetNodeId) {
        return createTrip(startNodeId, targetNodeId, GRAPH_TYPES);
    }

    public Trip<SimulationNode> createTrip(int startNodeId, int targetNodeId, GraphType graphType) {
        return createTrip(startNodeId, targetNodeId, new HashSet(Arrays.asList(graphType)));
    }

    public Trip<SimulationNode> createTrip(GPSLocation startLocation, GPSLocation targetLocation, GraphType graphType) {
        int startNodeId = nearestElementUtils.getNearestElement(startLocation, graphType).id;
        int targetNodeId = nearestElementUtils.getNearestElement(targetLocation, graphType).id;
        return createTrip(startNodeId, targetNodeId, new HashSet(Arrays.asList(graphType)));
    }

    public Trip<SimulationNode> createTrip(int startNodeId, int targetNodeId, Set<GraphType> graphTypes) {
        if (startNodeId == targetNodeId) {
            try {
                throw new Exception("Start node cannot be the same as end node");
            } catch (Exception ex) {
                LOGGER.error(null, ex);
            }
        }
        ShortestPathPlanner pathPlanner = pathPlanners.getPathPlanner(graphTypes);

        Trip finalTrip = null;
        try {
            finalTrip = pathPlanner.findTrip(startNodeId, targetNodeId);
        } catch (TripPlannerException ex) {
            LOGGER.error(null, ex);
        }

        return finalTrip;
    }

    public static Trip<SimulationNode> mergeTrips(Trip<SimulationNode>... trips) {
        int i = 0;
        Trip<SimulationNode> firstTrip = null;
        do {
            firstTrip = trips[i];
            i++;
        } while (firstTrip == null);

        Trip<SimulationNode> newTrip = new Trip<SimulationNode>(firstTrip.getLocations());

        for (int j = i; j < trips.length; j++) {
            Trip<SimulationNode> trip = trips[j];
            if (trip != null) {
                for (SimulationNode location : trip.getLocations()) {
                    if (!newTrip.getLocations().peekLast().equals(location)) {
                        newTrip.extendTrip(location);
                    }
                }
            }
        }
        return newTrip;
    }

    public static VehicleTrip mergeTripsOld(VehicleTrip<TripItem>... trips) {
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


    private float getEdgeDuration(Node startNode, Node targetNode) {
        SimulationEdge edge = network.getEdge(startNode, targetNode);
        return edge.getLength() / edge.allowedMaxSpeedInMpS;
    }


    public float getTripDurationInSeconds(Trip<? extends Node> trip) {
        float duration = 0;

        LinkedList<? extends Node> locations = trip.getLocations();
        if (locations.size() >= 2) {
            Node startNode = locations.getFirst();
            for (int i = 1; i < locations.size(); i++) {
                Node targetNode = locations.get(i);
                duration += getEdgeDuration(startNode, targetNode);
                startNode = targetNode;
            }
        }

        return duration;
    }


}
