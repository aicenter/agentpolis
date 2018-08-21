package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment;

import cz.agents.alite.configurator.Configurator;
import org.apache.log4j.Logger;

import java.util.Random;

/* RandomProvider should be used for all random generating in the simulation
 *
 */
public final class RandomProvider {

    private final static Logger logger = Logger.getLogger(RandomProvider.class);

    //TODO better random generator could be used
    private static Random random;

    public static void init() {
        // Setting seed of random generator
        long seed = Configurator.getParamInt("simulator.lite.seed", -1);
        random = new Random(seed);
    }

    public static Random getRandom() {
        if (random == null) init();
        return random;
    }
}
