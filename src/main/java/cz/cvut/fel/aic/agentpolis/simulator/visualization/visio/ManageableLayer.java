package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.ToggleLayer;

public class ManageableLayer extends ToggleLayer {
	private String name;

	public ManageableLayer(String name, VisLayer layer) {
		this.name = name;
		this.setEnabled(true);
		this.addSubLayer(layer);
	}

	public void toggle() {
		this.setEnabled(!this.getEnabled());
	}

	public String getName() {
		return name;
	}

	public boolean isVisible() {
		return getEnabled();
	}
}
