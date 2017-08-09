package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.factories;

import com.google.inject.Injector;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.updates.StaticGraphUpdateGE;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;
import cz.cvut.fel.aic.geographtools.Edge;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * The factory for the initialization of static graph visualization for GE
 * 
 * @author Zbynek Moler
 * 
 */
public class StaticGraphUpdateGEFactory extends UpdateGEFactory {

    private final GraphType graphType;
    private final Color graphColor;

    public StaticGraphUpdateGEFactory(GraphType graphType, Color graphColor,
            CameraAltVisibility cameraAltVisibility, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, nameOfUpdateKmlView);
        this.graphType = graphType;
        this.graphColor = graphColor;
    }

    @Override
    public UpdateKmlView createUpdateKmlView(Injector injector, RegionBounds regionBounds) {

        Graph<?,?> graph = injector.getInstance(TransportNetworks.class).getGraph(graphType);
        Collection<? extends Edge> edges = graph.getAllEdges();

        List<List<Coordinate>> coordinates = new ArrayList<List<Coordinate>>();

        for (Edge edge : edges) {
            List<Coordinate> coordinatesInner = new ArrayList<Coordinate>();
            
            Node fromNode = graph.getNode(edge.fromId);
            Node toNode = graph.getNode(edge.toId);

            coordinatesInner.add(new Coordinate(fromNode.getLongitude(), fromNode.getLatitude()));
            coordinatesInner.add(new Coordinate(toNode.getLongitude(), toNode.getLatitude()));

            coordinates.add(coordinatesInner);
        }

        return new StaticGraphUpdateGE(coordinates, graphColor);
    }

}
