package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.osm;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadNodeExtended;
import cz.agents.geotools.Transformer;
import cz.agents.gtdgraphimporter.osm.OsmGraphBuilder;
import cz.agents.gtdgraphimporter.osm.TagEvaluator;
import cz.agents.gtdgraphimporter.osm.TagExtractor;
import cz.agents.gtdgraphimporter.osm.WayTagExtractor;
import cz.agents.gtdgraphimporter.structurebuilders.TmpGraphBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;
import java.util.Map;

/**
 * Adding parser for multi lanes and so on.
 *
 * @author Zdenek Bousa
 */
public class OsmGraphBuilderExtended extends OsmGraphBuilder {
    protected OsmGraphBuilderExtended(URL osmUrl, Transformer transformer, TagExtractor<Double> elevationExtractor, WayTagExtractor<Double> speedExtractor, TagEvaluator parkAndRideEvaluator, Map<ModeOfTransport, TagEvaluator> modeEvaluators, Map<ModeOfTransport, TagEvaluator> oneWayEvaluators, TagEvaluator oppositeDirectionEvaluator) {
        super(osmUrl, transformer, elevationExtractor, speedExtractor, parkAndRideEvaluator, modeEvaluators, oneWayEvaluators, oppositeDirectionEvaluator);
    }


    public TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> readOsmAndGetGraphBuilderFoExt() {
        //super.parseOSM();
        throw new NotImplementedException();
    }
}
