package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.osm;

import cz.agents.gtdgraphimporter.osm.TagExtractor;
import cz.agents.gtdgraphimporter.osm.WayTagExtractor;

import java.util.Map;

/**
 * Copy from {@link cz.agents.gtdgraphimporter.osm.OsmGraphBuilder}
 *
 * @author Zdenek Bousa
 */
enum EdgeType {
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
