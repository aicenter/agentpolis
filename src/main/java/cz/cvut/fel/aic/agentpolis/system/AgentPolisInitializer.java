/* 
 * Copyright (C) 2017 fido.
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
package cz.cvut.fel.aic.agentpolis.system;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class AgentPolisInitializer {
    private Module mainModule;

    public AgentPolisInitializer(StandardAgentPolisModule mainModule) {
        this.mainModule = mainModule;
    }
    
    public AgentPolisInitializer() {
        this(new StandardAgentPolisModule());
    }

    public void overrideModule(Module module){
        mainModule = Modules.override(mainModule).with(module);
    }
    
    public Injector initialize(){
        Injector injector = Guice.createInjector(mainModule);
        return injector;
    }
}
