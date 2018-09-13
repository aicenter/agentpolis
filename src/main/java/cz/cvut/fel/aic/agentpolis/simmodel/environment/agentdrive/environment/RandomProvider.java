package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
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
        long seed = AgentDriveModel.adConfig.ADSeed;
        random = new Random(seed);
    }

    public static Random getRandom() {
        if (random == null) init();
        return random;
    }
}
