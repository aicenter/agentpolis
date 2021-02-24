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
package cz.cvut.fel.aic.agentpolis.simpresentationlayer.support;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;

public class TestModule extends StandardAgentPolisModule {

	public TestModule() {
		super();

		agentpolisConfig.visio.showVisio = true; //VisualTests.SHOW_VISIO;
		agentpolisConfig.srid = testSRID();
	}

	static int testSRID() {
		return 32650; // SRID used for both tests - changing this should give various distance results, but shouldn't break the visualization
	}


	@Override
	protected void bindVisioInitializer() {
		bind(VisioInitializer.class).to(TestVisioInitializer.class);
	}

}
