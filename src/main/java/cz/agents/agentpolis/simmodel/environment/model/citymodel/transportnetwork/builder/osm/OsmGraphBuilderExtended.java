package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.osm;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.structurebuilders.edge.RoadEdgeExtendedBuilder;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.structurebuilders.node.RoadNodeExtendedBuilder;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadNodeExtended;
import cz.agents.basestructures.GPSLocation;
import cz.agents.geotools.GPSLocationTools;
import cz.agents.geotools.Transformer;
import cz.agents.gtdgraphimporter.osm.*;
import cz.agents.gtdgraphimporter.osm.element.OsmNode;
import cz.agents.gtdgraphimporter.osm.element.OsmRelation;
import cz.agents.gtdgraphimporter.osm.element.OsmWay;
import cz.agents.gtdgraphimporter.osm.handler.OsmHandler;
import cz.agents.gtdgraphimporter.structurebuilders.TmpGraphBuilder;
import cz.agents.gtdgraphimporter.structurebuilders.node.NodeBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toSet;

/**
 * Class parsing OSM XML file using SAX parser. The output of this parser is a non-simplified graph defined by the OSM.
 * There are not done any other operations like simplification and finding of strongly connected components.
 *
 * @author Marek Cuchý
 * @author Zdenek Bousa
 */
public class OsmGraphBuilderExtended implements OsmElementConsumer {

    private static final Logger LOGGER = Logger.getLogger(OsmGraphBuilderExtended.class);

    /**
     * URL of the OSM to be parsed
     */
    private final URL osmUrl;

    /**
     * Factory for building graph nodes
     */
    private final Transformer projection;

    /**
     * Function extracting elevation from node tags
     */
    private final TagExtractor<Double> elevationExtractor;

    /**
     * Function extracting max speed from way tags
     */
    private final WayTagExtractor<Double> speedExtractor;

    /**
     * Predicate that says if an element is a park and ride location based on its tags
     */
    private final TagEvaluator parkAndRideEvaluator;

    /**
     * Predicate for each mode, allowed in the graph, that says if the mode is allowed on a particular way (edge)
     */
    private final Map<ModeOfTransport, TagEvaluator> modeEvaluators;

    /**
     * Predicate for each mode, allowed in the graph, that says if a way (edge) is one-way for the mode
     */
    private final Map<ModeOfTransport, TagEvaluator> oneWayEvaluators;

    /**
     * Function extracting lanes count tag.
     */
    private final LanesCountExtractorAP lanesCountExtractor;

    /**
     * Predicate that says if nodes of a way are in opposite order than they really are. Important only for one-way
     * edges. Current implementation just reverse the order of the nodes therefore the one-way evaluators must
     * calculate
     * with the opposite order tags.
     */
    private final TagEvaluator oppositeDirectionEvaluator;

    protected final Map<Long, OsmNode> osmNodes = new HashMap<>();
    protected final TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> builder = new TmpGraphBuilder<>();

    private int mergedEdges = 0;

    /**
     * Constructor
     */
    protected OsmGraphBuilderExtended(URL osmUrl, Transformer transformer,
                                      TagExtractor<Double> elevationExtractor,
                                      WayTagExtractor<Double> speedExtractor,
                                      TagEvaluator parkAndRideEvaluator,
                                      Map<ModeOfTransport, TagEvaluator> modeEvaluators,
                                      Map<ModeOfTransport, TagEvaluator> oneWayEvaluators,
                                      TagEvaluator oppositeDirectionEvaluator, LanesCountExtractorAP lanesCountExtractor) {

        this.osmUrl = osmUrl;
        this.projection = transformer;
        this.elevationExtractor = elevationExtractor;
        this.speedExtractor = speedExtractor;
        this.parkAndRideEvaluator = parkAndRideEvaluator;
        this.modeEvaluators = modeEvaluators;
        this.oneWayEvaluators = oneWayEvaluators;
        this.oppositeDirectionEvaluator = oppositeDirectionEvaluator;
        this.lanesCountExtractor = lanesCountExtractor;
    }

    /**
     * Getter
     *
     * @return built graph
     */
    public TmpGraphBuilder<RoadNodeExtended, RoadEdgeExtended> readOsmAndGetGraphBuilder() {
        parseOSM();
        return builder;
    }

    @Override
    public void accept(OsmNode node) {
        osmNodes.put(node.id, node);
    }

    @Override
    public void accept(OsmWay way) {
        way.removeMissingNodes(osmNodes.keySet());

        Set<ModeOfTransport> ModeOfTransports = getModeOfTransports(way);

        if (!ModeOfTransports.isEmpty()) {
            createEdges(way, ModeOfTransports);
        }
    }

    @Override
    public void accept(OsmRelation relation) {
    }

    /**
     *  Private section
     */

    /**
     * parser
     */
    private void parseOSM() {
        LOGGER.info("Parsing of OSM started...");

        long t1 = System.currentTimeMillis();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();
            xmlreader.setContentHandler(new OsmHandler(this));
            xmlreader.parse(new InputSource(osmUrl.openStream()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("OSM can't be parsed.", e);
        }

        LOGGER.info(getStatistic());
        long t2 = System.currentTimeMillis();
        LOGGER.info("Parsing of OSM finished in " + (t2 - t1) + "ms");
        osmNodes.clear();
    }

    /**
     * OSM way modes
     */
    private Set<ModeOfTransport> getModeOfTransports(OsmWay way) {
        Set<ModeOfTransport> ModeOfTransports = EnumSet.noneOf(ModeOfTransport.class);

        for (Entry<ModeOfTransport, TagEvaluator> entry : modeEvaluators.entrySet()) {
            ModeOfTransport mode = entry.getKey();
            if (entry.getValue().test(way.getTags())) {
                ModeOfTransports.add(mode);
            }
        }
        return ModeOfTransports;
    }

    /**
     * Create nodes & edges section
     */
    private void createEdges(OsmWay way, Set<ModeOfTransport> modeOfTransports) {
        List<Long> nodes = way.getNodes();

        //reverse nodes if way is the opposite direction. Have to cooperate with one-way evaluators.
        if (oppositeDirectionEvaluator.test(way.getTags())) {
            nodes = Lists.reverse(nodes);
        }
        nodes.forEach(this::createAndAddNode);

        Set<ModeOfTransport> bidirectionalModes = getBidirectionalModes(way, modeOfTransports);

        //the EdgeType parameters doesn't take into account the possibility of reversed direction - possible fix in
        // the future
        //
        // bidirectionalStatus is used for (int) uniqueWayId and (int) oppositeWayId. If 0, then edge is one-way.
        //If the number is 1, it is a bidirectional edge (in FORWARD) and if the number is 2, then it is the opposite
        // direction of the edge (BACKWARD)
        if (bidirectionalModes.isEmpty()) {
            createAndAddOrMergeEdges(nodes, modeOfTransports, way, EdgeType.FORWARD, 0);
        } else {
            way.addTag("[OsmParser]::bidirectional", "1"); // TODO: do it properly inside WayTagExtractor

            createAndAddOrMergeEdges(nodes, modeOfTransports, way, EdgeType.FORWARD, 1);
            createAndAddOrMergeEdges(Lists.reverse(nodes), bidirectionalModes, way, EdgeType.BACKWARD, 2);
        }
    }

    /**
     * Create node and give it an int number based on builder.getNodeCount() - number of already added nodes
     * in TmpGraphBuilder
     *
     * @param nodeId - source id in OsmNode
     */
    protected void createAndAddNode(long nodeId) {
        if (!builder.containsNode(nodeId)) {
            OsmNode osmNode = osmNodes.get(nodeId);
            RoadNodeExtendedBuilder roadNodeExtendedBuilder = new RoadNodeExtendedBuilder(builder.getNodeCount(), nodeId,
                    getProjectedGPS(osmNode));
            builder.addNode(roadNodeExtendedBuilder);
        }
    }

    /**
     * Create edges for each node in OsmWay
     *
     * @param bidirectionalStatus is used for (int) uniqueWayId and (int) oppositeWayId. If 0, then edge is one-way.
     *                            If the number is 1, it is a bidirectional edge (in FORWARD) and if the number is 2,
     *                            then it is the opposite direction of the edge (BACKWARD)
     */
    private void createAndAddOrMergeEdges(List<Long> nodes, Set<ModeOfTransport> modeOfTransports, OsmWay way,
                                          EdgeType edgeType, int bidirectionalStatus) {
        for (int i = 1; i < nodes.size(); i++) {
            createAndAddOrMergeEdge(nodes.get(i - 1), nodes.get(i), modeOfTransports, way, edgeType, bidirectionalStatus);
        }
    }

    protected void createAndAddOrMergeEdge(long fromSourceId, long toSourceId, Set<ModeOfTransport> modeOfTransports,
                                           OsmWay way, EdgeType edgeType, int bidirectionalStatus) {
        int tmpFromId = builder.getIntIdForSourceId(fromSourceId);
        int tmpToId = builder.getIntIdForSourceId(toSourceId);

        if (builder.containsEdge(tmpFromId, tmpToId)) {
            //edge already built, so add  another mode
            mergedEdges++;
            resolveConflictEdges(tmpFromId, tmpToId, modeOfTransports, way, edgeType);
        } else {
            // begin with new edge
            int uniqueId = builder.getEdgeCount();

            // decide on opposite way
            int oppositeWayUniqueId;
            if (bidirectionalStatus == 1) {
                oppositeWayUniqueId = uniqueId + 1; // opposite direction will follow in construction
            } else if (bidirectionalStatus == 2) {
                oppositeWayUniqueId = uniqueId - 1;
            } else {
                oppositeWayUniqueId = -1;
            }

            // create temporary edge
            RoadEdgeExtendedBuilder roadEdge = createRoadEdgeExtendedBuilder(tmpFromId, tmpToId, uniqueId,
                    oppositeWayUniqueId, modeOfTransports, way, edgeType);

            // add edge to TmpGraphBuilder
            builder.addEdge(roadEdge);
        }
    }

    protected void resolveConflictEdges(int tmpFromId, int tmpToId, Set<ModeOfTransport> newModeOfTransports,
                                        OsmWay way, EdgeType edgeType) {
        RoadEdgeExtendedBuilder edgeBuilder = (RoadEdgeExtendedBuilder) builder.getEdge(tmpFromId, tmpToId);
        edgeBuilder.addModeOfTransports(newModeOfTransports);
    }

    /**
     * Create new builder and use tag parsers
     */
    protected RoadEdgeExtendedBuilder createRoadEdgeExtendedBuilder(int fromId, int toId, int uniqueWayId, int oppositeWayUniqueId,
                                                                    Set<ModeOfTransport> ModeOfTransports,
                                                                    OsmWay way, EdgeType edgeType) {
        return new RoadEdgeExtendedBuilder(fromId, toId, way.getId(), uniqueWayId, oppositeWayUniqueId, (int) calculateLength(fromId, toId),
                ModeOfTransports, extractSpeed(way, edgeType), extractLanesCount(way, edgeType));
    }

    /**
     * Return subset of {@code ModeOfTransports} for which the way is bidirectional (isn't one-way).
     */
    private Set<ModeOfTransport> getBidirectionalModes(OsmWay way, Set<ModeOfTransport> ModeOfTransports) {
        return ModeOfTransports.stream().filter(mode -> isBidirectional(way, mode)).collect(toSet());
    }

    private boolean isBidirectional(OsmWay way, ModeOfTransport mode) {
        return !oneWayEvaluators.get(mode).test(way.getTags());
    }

    /**
     * Projection
     */
    protected double calculateLength(int fromId, int toId) {
        NodeBuilder<? extends RoadNodeExtended> n1 = builder.getNode(fromId);
        NodeBuilder<? extends RoadNodeExtended> n2 = builder.getNode(toId);
        return GPSLocationTools.computeDistance(n1.location, n2.location);
    }

    private GPSLocation getProjectedGPS(double lat, double lon, double elevation) {
        return GPSLocationTools.createGPSLocation(lat, lon, (int) Math.round(elevation), projection);
    }

    private GPSLocation getProjectedGPS(OsmNode osmNode) {
        return getProjectedGPS(osmNode.lat, osmNode.lon, elevationExtractor.apply(osmNode.getTags()));
    }

    /**
     * Parsing extra tags
     */
    protected float extractSpeed(OsmWay way, EdgeType edgeType) {
        return edgeType.apply(speedExtractor, way.getTags()).floatValue();
    }

    private boolean isParkAndRide(OsmNode osmNode) {
        return parkAndRideEvaluator.test(osmNode.getTags());
    }

    private Integer extractLanesCount(OsmWay way, EdgeType edgeType) {
        if (EdgeType.BACKWARD == edgeType) {
            return lanesCountExtractor.getBackwardValue(way.getTags());
        } else {
            return lanesCountExtractor.getForwardValue(way.getTags());
        }
    }

    /**
     * Stats
     */
    public String getStatistic() {
        return "Merged edges=" + mergedEdges;
    }

    @Override
    public String toString() {
        return "OsmGraphBuilder{" +
                "nodes=" + osmNodes.size() + '}';
    }

    protected enum EdgeType {
        FORWARD {
            @Override
            protected <T> TagExtractor<T> getExtractor(WayTagExtractor<T> extractor) {
                return extractor::getForwardValue;
            }
        },
        BACKWARD {
            @Override
            protected <T> TagExtractor<T> getExtractor(WayTagExtractor<T> extractor) {
                return extractor::getBackwardValue;
            }
        };

        /**
         * Get corresponding tag extractor function.
         *
         * @param extractor
         * @param <T>
         * @return
         */
        protected abstract <T> TagExtractor<T> getExtractor(WayTagExtractor<T> extractor);

        /**
         * Applies corresponding method of the {@code extractor} on the {@code tags}.
         *
         * @param extractor
         * @param tags
         * @param <T>
         * @return
         */
        public <T> T apply(WayTagExtractor<T> extractor, Map<String, String> tags) {
            return getExtractor(extractor).apply(tags);
        }
    }

    /**
     * Builder
     */
    public static class Builder {

        private static final ObjectMapper MAPPER = new ObjectMapper();

        static {
            MAPPER.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        }

        protected final Transformer transformer;
        protected final Set<ModeOfTransport> allowedModes;

        protected final URL osmUrl;

        protected TagExtractor<Double> elevationExtractor = new DoubleExtractor("height", 0);
        protected WayTagExtractor<Double> speedExtractor;
        protected LanesCountExtractorAP lanesCountExtractor = new LanesCountExtractorAP();

        protected Map<ModeOfTransport, TagEvaluator> modeEvaluators = new EnumMap<>(ModeOfTransport.class);
        protected Map<ModeOfTransport, TagEvaluator> oneWayEvaluators = new EnumMap<>(ModeOfTransport.class);
        protected TagEvaluator oppositeDirectionEvaluator = new OneTagEvaluator("oneway", "-1");
        protected TagEvaluator parkAndRideEvaluator = new OneTagEvaluator("park_and_ride", "yes");

        /**
         * @param osmUrl       -
         * @param projection   -
         * @param allowedModes modes that are required to be in the output graph. For each modes there have to be set appropriate mode
         *                     evaluator. If it isn't set the builder tries to use a default one, but it's possible that it is not
         *                     defined.
         */
        public Builder(URL osmUrl, Transformer projection, Set<ModeOfTransport> allowedModes) {
            if (allowedModes.isEmpty()) throw new IllegalArgumentException("Allowed modes can't be empty.");
            this.transformer = projection;
            this.allowedModes = allowedModes;
            this.osmUrl = osmUrl;
        }

        public Builder(File osmFile, Transformer projection, Set<ModeOfTransport> allowedModes) {
            this(getUrl(osmFile), projection, allowedModes);
        }

        public Builder(String osmPath, Transformer projection, Set<ModeOfTransport> allowedModes) {
            this(getUrl(osmPath), projection, allowedModes);
        }

        /**
         * Build
         */
        public OsmGraphBuilderExtended build() {
            loadMissingSettings();

            return new OsmGraphBuilderExtended(osmUrl, transformer, elevationExtractor, speedExtractor, parkAndRideEvaluator,
                    modeEvaluators, oneWayEvaluators, oppositeDirectionEvaluator, lanesCountExtractor);
        }

        /**
         * Tag evaluation
         */

        public Builder setElevationExtractor(TagExtractor<Double> elevationExtractor) {
            this.elevationExtractor = elevationExtractor;
            return this;
        }

        public Builder addModeEvaluators(Map<ModeOfTransport, TagEvaluator> modeEvaluators) {
            this.modeEvaluators.putAll(modeEvaluators);
            return this;
        }

        public Builder addModeEvaluator(ModeOfTransport mode, TagEvaluator modeEvaluator) {
            this.modeEvaluators.put(mode, modeEvaluator);
            return this;
        }

        public Builder addOneWayEvaluators(Map<ModeOfTransport, TagEvaluator> oneWayEvaluators) {
            this.oneWayEvaluators.putAll(oneWayEvaluators);
            return this;
        }

        public Builder addOneWayEvaluators(ModeOfTransport mode, TagEvaluator oneWayEvaluator) {
            this.oneWayEvaluators.put(mode, oneWayEvaluator);
            return this;
        }

        public Builder setOppositeDirectionEvaluator(TagEvaluator oppositeDirectionEvaluator) {
            this.oppositeDirectionEvaluator = oppositeDirectionEvaluator;
            return this;
        }

        public Builder setParkAndRideEvaluator(TagEvaluator parkAndRideEvaluator) {
            this.parkAndRideEvaluator = parkAndRideEvaluator;
            return this;
        }

        public Builder setSpeedExtractor(WayTagExtractor<Double> speedExtractor) {
            this.speedExtractor = speedExtractor;
            return this;
        }

        /**
         * Check for setting
         */
        protected void loadMissingSettings() {
            loadSpeedExtractorIfNeeded();
            loadModeEvaluatorsIfNeeded();
            loadOneWayEvaluatorsIfNeeded();
            //loadLaneCountExtractorIfNeeded();
        }

        /**
         * Add missing tag evaluators
         */
        private void loadSpeedExtractorIfNeeded() {
            if (speedExtractor == null) {
                try {
                    speedExtractor = MAPPER.readValue(
                            SpeedExtractor.class.getResourceAsStream("default_speed_mapping.json"),
                            SpeedExtractor.class);
                } catch (IOException e) {
                    throw new IllegalStateException("Default speed extractor can't be created.", e);
                }
            }
        }

        private void loadLaneCountExtractorIfNeeded() {
            if (lanesCountExtractor == null) {
                //lanesCountExtractor = MAPPER.readValue(null,LanesCountExtractorAP.class);
            } else {
                throw new IllegalStateException("Default lanes count extractor can't be created.");
            }
        }

        private void loadModeEvaluatorsIfNeeded() {
            Set<ModeOfTransport> missingModes = Sets.difference(allowedModes, modeEvaluators.keySet());
            for (ModeOfTransport mode : missingModes) {
                InputStream stream = OsmGraphBuilder.class.getResourceAsStream("mode/" + mode.name().toLowerCase() +
                        ".json");
                if (stream == null) {
                    throw new IllegalStateException("Default mode evaluator for " + mode + " isn't defined. You " +
                            "have to define it.");
                }
                try {
                    modeEvaluators.put(mode, MAPPER.readValue(stream, InclExclTagEvaluator.class));
                } catch (IOException e) {
                    throw new IllegalStateException("Default mode evaluator for mode " + mode + " can't be created.",
                            e);
                }
            }
        }

        private void loadOneWayEvaluatorsIfNeeded() {
            Set<ModeOfTransport> missingModes = Sets.difference(allowedModes, oneWayEvaluators.keySet());

            //default evaluator for all modes.
            TagEvaluator defaultEval = TagEvaluator.ALWAYS_FALSE;
            for (ModeOfTransport mode : missingModes) {
                InputStream stream = OsmGraphBuilder.class.getResourceAsStream("oneway/" + mode.name().toLowerCase() +
                        ".json");
                if (stream == null) {
                    oneWayEvaluators.put(mode, defaultEval);
                } else {
                    try {
                        oneWayEvaluators.put(mode, MAPPER.readValue(stream, InclExclTagEvaluator.class));
                    } catch (IOException e) {
                        LOGGER.warn("Default mode evaluator for mode " + mode + " can't be created. Used default " +
                                "evaluator for all modes.");
                        oneWayEvaluators.put(mode, defaultEval);
                    }
                }
            }
        }

        private static URL getUrl(String osmPath) {
            return getUrl(new File(osmPath));
        }

        private static URL getUrl(File osmFile) {
            try {
                return osmFile.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Incorrect file: " + osmFile, e);
            }
        }
    }
}
