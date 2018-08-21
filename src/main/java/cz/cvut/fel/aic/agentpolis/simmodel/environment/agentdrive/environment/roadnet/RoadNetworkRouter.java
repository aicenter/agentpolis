package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.RandomProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.agents.alite.configurator.Configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RoadNetworkRouter {

    //TODO refactor not static with the setRoadNetwork
    private static RoadNetwork roadNetwork = null;
    private static HashMap<Integer, List<String>> loadedRoutes;

    public static void setRoadNet(RoadNetwork roadNetwork) {
        RoadNetworkRouter.roadNetwork = roadNetwork;
    }

    public static List<Edge> generateRoute(int id) {
        ArrayList<Edge> route = new ArrayList<Edge>();
        HashMap<String, Edge> edges = roadNetwork.getEdges();

        if (!Configurator.getParamBool("highway.dashboard.sumoSimulation", true) && Configurator.getParamBool("highway.rvo.agent.randomRoutes", true).equals(true)) {

            Random rand = RandomProvider.getRandom();
            Object[] values = getRoutes().values().toArray();
            List<String> randomValue = (List<String>) values[rand.nextInt(values.length)];
            for (String edge : randomValue) {
                route.add(edges.get(edge));
            }
        } else {
            List<String> edgesNames = getRoutes().get(id);
            for (String name : edgesNames) {
                route.add(edges.get(name));
            }

        }

        return route;
    }

    private static HashMap<Integer, List<String>> readRoutesFromFile() {
        String netFolderPath = Configurator.getParamString("simulator.net.folder", "notDefined");
        XMLReader reader = new XMLReader(netFolderPath);
        HashMap<Integer, List<String>> routes = reader.getRoutes();
        return routes;
    }
    private static HashMap<Integer,List<String>> getRoutes(){
        if(loadedRoutes==null){
            loadedRoutes = readRoutesFromFile();
        }
        return loadedRoutes;
    }
}
