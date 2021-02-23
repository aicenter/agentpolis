package cz.cvut.fel.aic.agentpolis.config;

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
