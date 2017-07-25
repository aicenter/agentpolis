/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;
import java.util.List;
import java.util.Set;

/**
 *
 * @author fido
 */
public interface AgentPolisMainModule {
    public void initializeParametrs(List<Object> loggers, final Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer);
}
