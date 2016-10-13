/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment;

import cz.agents.agentpolis.siminfrastructure.logger.LogItem;
import cz.agents.agentpolis.simulator.creator.SimulationParameters;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 *
 * @author fido
 */
public interface AgentPolisMainModule {
    public void initializeParametrs(EnvironmentFactory envinromentFactory, SimulationParameters parameters, 
        List<Object> loggers, final Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer,
        ZonedDateTime initDate);
}
