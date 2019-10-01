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
package cz.cvut.fel.aic.agentpolis;

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
