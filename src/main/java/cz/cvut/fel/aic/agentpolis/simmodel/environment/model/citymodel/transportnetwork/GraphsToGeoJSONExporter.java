package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Singleton
public class GraphsToGeoJSONExporter {
    private final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs;

    @Inject
    public GraphsToGeoJSONExporter(Graphs graphs) {
        this.graphs = graphs.getGraphs();
    }


    private void serializeGraphIntoGeoJson(String name, Graph<SimulationNode, SimulationEdge> graph) {
        JSONObject json = getJsonHeader();
        serializeNodes(name, graph, json);
        serializeEdges(name, graph, json);
    }


    private JSONObject getJsonHeader() {
        JSONObject json = new JSONObject();
        JSONObject crs = new JSONObject();
        JSONObject crsProperties = new JSONObject();

        crsProperties.put("name", "urn:ogc:def:crs:OGC:1.3:CRS84");
        crs.put("type", "name");
        crs.put("properties", crsProperties);
        json.put("crs", crs);
        json.put("type", "FeatureCollection");
        return json;
    }

    private void serializeNodes(String name, Graph<SimulationNode, SimulationEdge> graph, JSONObject json) {
        JSONArray features = new JSONArray();
        for (SimulationNode node : graph.getAllNodes()) {
            JSONObject feature = new JSONObject();
            JSONObject geometry = createGeometry(node);
            JSONObject properties = createProperties(node);
            feature.put("type", "Feature");
            feature.put("geometry", geometry);
            feature.put("properties", properties);

            features.add(feature);
        }
        json.put("features", features);

        try (FileWriter file = new FileWriter(name + "-nodes.geojson")) {
            file.write(json.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void serializeEdges(String name, Graph<SimulationNode, SimulationEdge> graph, JSONObject json) {
        JSONArray features = new JSONArray();
        for (SimulationEdge edge : graph.getAllEdges()) {
            JSONObject feature = new JSONObject();
            JSONObject geometry = createGeometry(edge, graph);
            JSONObject properties = createProperties(edge);
            feature.put("type", "Feature");
            feature.put("geometry", geometry);
            feature.put("properties", properties);

            features.add(feature);
        }
        json.put("features", features);

        try (FileWriter file = new FileWriter(name + "-edges.geojson")) {
            file.write(json.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createProperties(SimulationNode node) {
        JSONObject properties = new JSONObject();
        properties.put("id", node.id);
        properties.put("source_id", node.sourceId);
        properties.put("elevation", node.elevation);
        properties.put("isBikeSharingStation", node.isBikeSharingStation);
        properties.put("isParkAndRide", node.isParkAndRide);
        return properties;
    }

    private JSONObject createProperties(SimulationEdge edge) {
        JSONObject properties = new JSONObject();
        properties.put("id", edge.getUniqueId());
        properties.put("osm_id", edge.getLanesCount());
        properties.put("max_speed", edge.allowedMaxSpeedInMpS);
        properties.put("length", edge.getLength());
        properties.put("way_id", edge.wayID);
        properties.put("from_id", edge.fromId);
        properties.put("to_id", edge.toId);
        properties.put("opposite_way_id", edge.getOppositeWayId());
        return properties;
    }

    private JSONObject createGeometry(SimulationNode node) {
        JSONObject geometry = new JSONObject();
        geometry.put("type", "Point");
        JSONArray coordinates = new JSONArray();
        coordinates.add(node.getLongitude());
        coordinates.add(node.getLatitude());
        geometry.put("coordinates", coordinates);
        return geometry;
    }

    private JSONObject createGeometry(SimulationEdge edge, Graph<SimulationNode, SimulationEdge> graph) {
        SimulationNode from = graph.getNode(edge.fromId);
        SimulationNode to = graph.getNode(edge.toId);
        JSONObject geometry = new JSONObject();
        geometry.put("type", "LineString");
        JSONArray coordinates = new JSONArray();
        JSONArray fromCoordinates = new JSONArray();
        JSONArray toCoordinates = new JSONArray();
        fromCoordinates.add(from.getLongitude());
        fromCoordinates.add(from.getLatitude());
        toCoordinates.add(to.getLongitude());
        toCoordinates.add(to.getLatitude());
        coordinates.add(fromCoordinates);
        coordinates.add(toCoordinates);
        geometry.put("coordinates", coordinates);
        return geometry;
    }

    public void export(String exportFolder) {
        for (GraphType graphType : graphs.keySet()) {
            serializeGraphIntoGeoJson(exportFolder + graphType.toString(), graphs.get(graphType));
        }
    }
}
