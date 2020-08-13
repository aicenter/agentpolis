/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.name.Names;
import cz.cvut.fel.aic.agentpolis.mock.TestVisioInitializer;
import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;

/**
 *
 * @author fido
 */
public class TestModule extends StandardAgentPolisModule{

	public TestModule() {
		super();	  
		
//		if(System.getProperty("test") == null){
//			agentpolisConfig.showVisio = false;
//		}

		agentpolisConfig.visio.showVisio = VisualTests.SHOW_VISIO;
		
		agentpolisConfig.congestionModel.batchSize = 1;
		agentpolisConfig.congestionModel.maxFlowPerLane = 5.0;
		agentpolisConfig.congestionModel.defaultCrossroadDrivingLanes = 2;
	}

    @Override
    protected void configureNext() {
        super.configureNext(); 
        int lineWidth = 24;
        bind(int.class).annotatedWith(Names.named("HighwayLayer edge width")).toInstance(lineWidth);
    }
	
	

	@Override
	protected void bindVisioInitializer() {
		bind(VisioInitializer.class).to(TestVisioInitializer.class);
	}
	
}
