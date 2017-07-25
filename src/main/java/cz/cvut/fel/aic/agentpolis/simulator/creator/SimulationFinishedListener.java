package cz.cvut.fel.aic.agentpolis.simulator.creator;

/**
 * This interface should be implemented by all classes that need to be informed
 * about a simulation finish, e.g., all classes with resources to be closed.
 *
 */
public interface SimulationFinishedListener {

    void simulationFinished();

}
