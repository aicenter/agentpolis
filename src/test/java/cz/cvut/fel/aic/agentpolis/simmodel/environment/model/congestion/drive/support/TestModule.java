/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.name.Names;
import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.mock.TestVisioInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;

/**
 *
 * @author fido
 */
public class TestModule extends StandardAgentPolisModule{

	public TestModule() {
		super();	  
		
		agentpolisConfig.visio.showVisio = VisualTests.SHOW_VISIO;		
		agentpolisConfig.congestionModel.batchSize = 1;
		agentpolisConfig.congestionModel.maxFlowPerLane = 5.0;
		agentpolisConfig.congestionModel.defaultCrossroadDrivingLanes = 2;
//                agentpolisConfig.congestionModel.fundamentalDiagramDelay = true;               
	}

    @Override
    protected void configureNext() {
        super.configureNext(); 
        int lineWidth = 1;
        bind(int.class).annotatedWith(Names.named("HighwayLayer edge width")).toInstance(lineWidth);
    }
	
	

	@Override
	protected void bindVisioInitializer() {
		bind(VisioInitializer.class).to(TestVisioInitializer.class);
	}
	
}
