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
package cz.cvut.fel.aic.agentpolis.system;

import com.google.inject.AbstractModule;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.GeojsonMapInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.MapInitializer;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import ninja.fido.config.Configuration;

/**
 *
 * @author david
 */
public class LiteModule extends AbstractModule{
	
	protected final AgentpolisConfig agentpolisConfig;
	
	public LiteModule() {
		agentpolisConfig = new AgentpolisConfig();
		Configuration.load(agentpolisConfig);
	}

	@Override
	protected void configure() {
		bind(AgentpolisConfig.class).toInstance(agentpolisConfig);
		bind(Transformer.class).toInstance(new Transformer(agentpolisConfig.srid));
		bind(MapInitializer.class).to(GeojsonMapInitializer.class);
	}
	
	
	
}
