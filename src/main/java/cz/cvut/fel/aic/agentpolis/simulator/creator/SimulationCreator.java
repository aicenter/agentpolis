/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simulator.creator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeEventGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import cz.cvut.fel.aic.agentpolis.utils.ResourceReader;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.net.URL;
import org.slf4j.LoggerFactory;

@Singleton
public class SimulationCreator {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SimulationCreator.class);
    private TypedSimulation simulation;
    private final AgentpolisConfig config;
    private final SimulationProvider simulationProvider;
    private final AllNetworkNodes allNetworkNodes;
    private final Graphs graphs;
    private final Provider<VisioInitializer> visioInitializerProvider;
    private final TimeEventGenerator timeEventGenerator;

    @Inject
    public SimulationCreator(final AgentpolisConfig config, SimulationProvider simulationProvider,
                             AllNetworkNodes allNetworkNodes, Graphs graphs,
                             Provider<VisioInitializer> visioInitializerProvider,
                             TimeEventGenerator timeEventGenerator) {
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.allNetworkNodes = allNetworkNodes;
        this.graphs = graphs;
        this.visioInitializerProvider = visioInitializerProvider;
        this.timeEventGenerator = timeEventGenerator;
    }

    public void prepareSimulation(final MapData osmDTO, final long seed) {
        LOGGER.debug("Using seed {}.", seed);

        initLogger();
        initSimulation();

        LOGGER.info(">>> CREATING MAP");

        initEnvironment(osmDTO, seed);
    }

    public void prepareSimulation(final MapData osmDTO) {
        prepareSimulation(osmDTO, 0L);
    }

    public void startSimulation() {
        long simTimeInit = System.currentTimeMillis();

        initVisio();

        if (config.skipSimulation) {
            LOGGER.info("Skipping simulation...");
        } else {
            LOGGER.info("Simulation initalized. ({} ms)", (System.currentTimeMillis() - simTimeInit));
            long simulationStartTime = System.currentTimeMillis();
            timeEventGenerator.start();
            simulation.run();
            LOGGER.info("Simulation finished: ({} ms)", (System.currentTimeMillis() - simulationStartTime));
        }
    }

    private void initSimulation() {
        simulation = new TypedSimulation(config.simulationDurationInMillis);
        simulation.setPrintouts(10000000);
        simulationProvider.setSimulation(simulation);
    }

    private void initEnvironment(MapData osmDTO, long seed) {
        LOGGER.info("Creating instance of environment...");
        allNetworkNodes.setAllNetworkNodes(osmDTO.nodesFromAllGraphs);
        graphs.setGraphs(osmDTO.graphByType);
        LOGGER.info("Done.");
    }

    private void initLogger() {
        LOGGER.info("Loading log4j properties");

        if (new File(config.log4jXmlDir).exists()) {
            try {
                DOMConfigurator.configure(config.log4jXmlDir);
                LOGGER.info("Loaded log4j properties");
                return;
            } catch (Exception ignored) {
                LOGGER.warn("Ignoring logger configuration exception.");
            }
        }

        URL pathToResource = ResourceReader.getPathToResource("/log4j/log4j.properties");
        if (pathToResource != null) {
            try {
                PropertyConfigurator.configure(pathToResource);
                LOGGER.info("Loaded log4j properties.");
                return;
            } catch (Exception ignored) {
            }
        }

        pathToResource = ResourceReader.getPathToResource("/log4j/log4j.xml");

        if (pathToResource != null) {
            try {
                DOMConfigurator.configure(pathToResource);
                LOGGER.info("Loaded log4j properties.");
                return;
            } catch (Exception ignored) {

            }
        }

        LOGGER.info("Failed to load log4j properties.");
    }

    private void initVisio() {
        if (config.showVisio) {
            LOGGER.info("Initializing Visio");
            visioInitializerProvider.get().initialize(simulation);
            simulation.setSimulationSpeed(1);
            LOGGER.info("Initialized Visio");
        } else {
            simulation.setSimulationSpeed(0);
        }
    }

}
