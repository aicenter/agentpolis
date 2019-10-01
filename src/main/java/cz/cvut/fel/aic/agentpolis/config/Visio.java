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
package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Double;
import java.util.Map;

public class Visio {
  public Boolean showStackedEntities;

  public Double minZoomToShowStackEntitiesCount;

  public Double minEntityZoom;

  public Boolean showVisio;

  public Visio(Map visio) {
    this.showStackedEntities = (Boolean) visio.get("show_stacked_entities");
    this.minZoomToShowStackEntitiesCount = (Double) visio.get("min_zoom_to_show_stack_entities_count");
    this.minEntityZoom = (Double) visio.get("min_entity_zoom");
    this.showVisio = (Boolean) visio.get("show_visio");
  }
}
