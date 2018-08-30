package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.*;

import javax.vecmath.Point3f;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface RoadNetwork {

    NetworkLocation getNetworkLocation();

    HashMap<String, Edge> getEdges();

    HashMap<String, Junction> getJunctions();

    ArrayList<String> getBridges();

    HashMap<String, LaneImpl> getLanes();

    ArrayList<String> getTunnels();

    Lane getClosestLane(Point3f position);

    ActualLanePosition getActualPosition(Point3f position);
    
//    List<Edge> getEdgeFromJunctions(Junction origin, Junction end);
//
//    List<Edge> getEdgeFromJunctions(String origin, String end);

    List<Edge> getEdgeFromJunctions(long originAgentpolisID, long endAgentpolisID) throws PathNotFoundException;
}





