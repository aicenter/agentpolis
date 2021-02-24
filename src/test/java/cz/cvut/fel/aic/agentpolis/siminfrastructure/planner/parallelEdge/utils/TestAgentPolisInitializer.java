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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.parallelEdge.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

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
