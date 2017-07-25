package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.alite.simulation.DrawListener;
import cz.agents.alite.simulation.MultipleDrawListener;
import cz.agents.alite.vis.element.StyledPoint;
import cz.agents.alite.vis.element.aggregation.StyledPointElements;
import cz.agents.alite.vis.element.implemetation.StyledPointImpl;
import cz.agents.alite.vis.layer.GroupLayer;
import cz.agents.alite.vis.layer.VisLayer;
import cz.agents.alite.vis.layer.common.CommonLayer;
import cz.agents.alite.vis.layer.terminal.StyledPointLayer;
import cz.agents.basestructures.Node;

/**
 * Layer showing all entities
 *
 * @author Ondrej Milenovsky
 * @author Zbynek Moler
 */
public class VisEntityLayer extends CommonLayer {

	private VisEntityLayer() {
	}

	/**
	 * minimal remaining time in ms when creating points
	 */
	private static int MIN_TIME = 5;
	
	private static ThisElements thisElements;
    
    private static boolean active = false;

    public static boolean isActive() {
        return active;
    }
    
    
    
    
    

	public static VisLayer createSynchronized(final EntityStorage<AgentPolisEntity> container,
											  final EntityPositionModel agentPositionModel,
											  final EntityPositionModel vehiclePositionModel,
											  final Map<Integer, ? extends Node> nodesFromAllGraphs,
											  final MultipleDrawListener drawListener,
											  final Map<EntityType, VisEntity> entityStyles, Projection projection) {
		GroupLayer group = GroupLayer.create();
		group.setHelpOverrideString("Entity layer");
		thisElements = new ThisElements(container, agentPositionModel, vehiclePositionModel, nodesFromAllGraphs, drawListener, entityStyles, projection);
		group.addSubLayer(StyledPointLayer.create(thisElements));
        active = true;
		return group;
	}

	public static VisLayer create(final EntityStorage<AgentPolisEntity> container,
								  final EntityPositionModel agentPositionModel,
								  final EntityPositionModel vehiclePositionModel,
								  final Map<Integer, Node> nodesFromAllGraphs,
								  final Map<EntityType, VisEntity> entityStyles, Projection projection) {
		return createSynchronized(container, agentPositionModel, vehiclePositionModel, nodesFromAllGraphs, null,
                entityStyles, projection);
	}
	
	public static void addEntity(AgentPolisEntity entity){
		thisElements.addEntity(entity);
	}
	
	public static void removeEntity(AgentPolisEntity entity){
		thisElements.removeEntity(entity);
	}

	private static class ThisElements implements StyledPointElements, DrawListener {

		private EntityStorage<AgentPolisEntity> storage;
		private MultipleDrawListener drawListener;
		private Collection<StyledPointImpl> lastPoints;
		private final Map<EntityType, VisEntity> entityStyles;
		private final Projection projection;
		private final EntityPositionModel agentPositionModel;
		private final EntityPositionModel vehiclePositionModel;
		private final Map<Integer, ? extends Node> nodesFromAllGraphs;

		public ThisElements(EntityStorage<AgentPolisEntity> storage, final EntityPositionModel agentPositionModel,
							final EntityPositionModel vehiclePositionModel,
							Map<Integer, ? extends Node> nodesFromAllGraphs, MultipleDrawListener drawListener,
							Map<EntityType, VisEntity> entityStyles, Projection projection) {
			this.storage = storage;
			this.agentPositionModel = agentPositionModel;
			this.vehiclePositionModel = vehiclePositionModel;
			this.nodesFromAllGraphs = nodesFromAllGraphs;
			this.drawListener = drawListener;
			lastPoints = new ArrayList<>();
			this.entityStyles = entityStyles;
			this.projection = projection;
		}
		
		public void addEntity(AgentPolisEntity entity){
			storage.addEntity(entity);
		}
		
		public void removeEntity(AgentPolisEntity entity){
			storage.removeEntity(entity);
		}

		public Iterable<? extends StyledPoint> getPoints() {
			if (drawListener != null) {
				drawListener.requestDraw(this);
			} else {
				drawFrame(Long.MAX_VALUE);
			}
			return lastPoints;
		}

		public boolean drawFrame(long deadline) {
			List<StyledPointImpl> points = new ArrayList<>();
			List<AgentPolisEntity> entities = getVisibleEntities();

			for (AgentPolisEntity entity : entities) {
				if (deadline - System.currentTimeMillis() < MIN_TIME) {
					lastPoints = points;
					return false;
				}
				StyledPointImpl point = makePoint(entity, entityStyles, projection);
				if (point != null) {
					points.add(point);
				}
			}
			lastPoints = points;
			return true;
		}

		private StyledPointImpl makePoint(AgentPolisEntity entity, final Map<EntityType, VisEntity> entityStyles,
										  final Projection projection) {

			Color color;
			int width;

			VisEntity entityStyleVis = entityStyles.get(entity.getType());

			if (entityStyleVis == null) {
				return null;
			}

			color = entityStyleVis.getEntityColor();
			width = entityStyleVis.getWidht();

			Node node = nodesFromAllGraphs.get(getPositionModel(entity).getEntityPositionByNodeId(entity.getId()));

			return new StyledPointImpl(projection.project(node), color, width);
		}

		private List<AgentPolisEntity> getVisibleEntities() {
			List<AgentPolisEntity> ret = new ArrayList<>();

			for (String name : storage.getEntityIds()) {
				AgentPolisEntity entity = storage.getEntityById(name);
				EntityPositionModel positionModel = getPositionModel(entity);

				// is visible
				if (positionModel.getEntityPositionByNodeId(name) != null) {
					ret.add(entity);
				}
			}
			return ret;
		}

		private EntityPositionModel getPositionModel(AgentPolisEntity entity) {
			EntityPositionModel positionModel = null;

			if (entity instanceof Agent) {
				positionModel = agentPositionModel;
			}

			if (entity instanceof PhysicalVehicle) {
				positionModel = vehiclePositionModel;
			}

			return positionModel;
		}

		public String getName() {
			return "Vis entities layer";
		}

	}

}
