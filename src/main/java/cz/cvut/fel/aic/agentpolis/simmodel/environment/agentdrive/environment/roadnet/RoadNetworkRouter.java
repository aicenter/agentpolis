package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.RandomProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RoadNetworkRouter {

    private static RoadNetwork roadNetwork = null;
    private static HashMap<Integer, List<String>> loadedRoutes;

    public static void setRoadNet(RoadNetwork roadNetwork) {
        RoadNetworkRouter.roadNetwork = roadNetwork;
    }

    public static List<Edge> generateRoute(int id) {
        ArrayList<Edge> route = new ArrayList<Edge>();
        return route;
    }
}
