package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.osm;

import com.google.common.primitives.Ints;

import java.util.Map;


/**
 * Lanes count extractor.Traffic lanes suitable for vehicles wider than a motorbike.
 * General information - total, forward and backward number of lanes.
 * <p>
 * Does not provide support for reserved and conditional lanes.
 * Additional information:  @see <a href="http://wiki.openstreetmap.org/wiki/Key:lanes">osmWiki</a>
 *
 * @author Zdenek Bousa
 */

public class LanesCountExtractorAP {
    public static final Integer DEFAULT_LANES_COUNT = 1;
    private Integer defaultLanesCount;

    public LanesCountExtractorAP() {
        this.defaultLanesCount = DEFAULT_LANES_COUNT;
    }

    public LanesCountExtractorAP(Integer defaultLanesCount) {
        this.defaultLanesCount = defaultLanesCount;
    }

    /**
     * Total number of lanes extractor
     */
    private Integer getMainValue(Map<String, String> tags) {
        return parseCount(tags.get("lanes"));
    }

    public Integer getForwardValue(Map<String, String> tags) {
        return resolve(tags, "lanes:forward");
    }

    public Integer getBackwardValue(Map<String, String> tags) {
        return resolve(tags, "lanes:backward");
    }

    private Integer resolve(Map<String, String> tags, String key) {
        Integer count = parseCount(tags.get(key));
        Integer twoWay = parseBiDirectional(tags.get("[OsmParser]::bidirectional")); // tag added in OsmGraphBuilderExtended->line 221

        if (count == null) {
            count = getMainValue(tags);
            if (count == null) {
                count = defaultLanesCount; //fallback
            }

            // Based on documentation lanes should be total number of lanes on the road. Therefore for two-way count = number of lanes/2
            // and for one-way count = lanes
            if (twoWay == 1) {
                count = count / 2;
            }

        }
        if (count < 1) {
            count = 1;
        }
        return count;
    }

    private Integer parseCount(String s) {
        if (s == null) return null;
        return Ints.tryParse(s);
    }

    private Integer parseBiDirectional(String s) {
        if (s == null) return 0;
        return Ints.tryParse(s);
    }
}
