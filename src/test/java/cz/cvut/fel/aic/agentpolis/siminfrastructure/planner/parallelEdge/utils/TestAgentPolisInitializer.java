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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.parallelEdge.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;

/**
 *
 * @author matal
 */
public class TestAgentPolisInitializer {
	private Module mainModule;

	public TestAgentPolisInitializer(TestStandardAgentPolisModule mainModule) {
		this.mainModule = mainModule;
	}
	
	public TestAgentPolisInitializer() {
		this(new TestStandardAgentPolisModule());
	}

	public void overrideModule(Module module){
		mainModule = Modules.override(mainModule).with(module);
	}
	
	public Injector initialize(){
		Injector injector = Guice.createInjector(mainModule);
		return injector;
	}
}
