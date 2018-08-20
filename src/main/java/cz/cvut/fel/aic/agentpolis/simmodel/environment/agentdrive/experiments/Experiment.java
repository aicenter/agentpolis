package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.experiments;

import org.geotools.console.CommandLine;

import java.text.ParseException;

/**
 * General interface for various scaling experiments
 * Created by wmatex on 26.11.14.
 */
public interface Experiment {
    /**
     * Run the experiment with the scaling quality
     *
     * @param quality Current value for the measured quality
     * @return
     */
    public double run(double quality, boolean verbose);

    /**
     * Initialize the experiment
     */
    public void setUp(CommandLine c) throws ParseException;

    /**
     * Log useful information to log file
     */
    public void log(FileLogger logger);

}
