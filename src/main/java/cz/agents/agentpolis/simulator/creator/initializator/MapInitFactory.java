package cz.agents.agentpolis.simulator.creator.initializator;

import java.io.File;

import com.google.inject.Injector;

import cz.agents.agentpolis.simulator.creator.initializator.impl.MapData;

/**
 * 
 * Each factory, which imports transport networks and and map data, should
 * implement this interface.
 * 
 * @author Zbynek Moler
 * 
 */
public interface MapInitFactory {

    public MapData initMap(File mapFile, Injector injector, long simulationDurationInMilisec);

}
