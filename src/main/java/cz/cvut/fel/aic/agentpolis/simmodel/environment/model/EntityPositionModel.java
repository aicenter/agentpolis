package cz.cvut.fel.aic.agentpolis.simmodel.environment.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.agentpolis.utils.InitAndGetterUtil;
import cz.agents.agentpolis.utils.key.KeyWithString;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
import cz.agents.alite.common.event.EventProcessor;
import java.util.Iterator;

/**
 * The EntityPositionStorage holds positions of mobile objects (citizens, cars, ...) in the UrbanSim environment.
 * <p>
 * The state consists of position of the body at its node.
 * <p>
 * (The models are not in new terminology, the environment objects are instead of the models)
 *
 * @author Antonin Komenda
 * @author Libor Wagner
 * @author Zbynek Moler
 */
public abstract class EntityPositionModel {

	/**
	 * Map of the store entity position info *
	 */
	private final Map<String, Integer> entityPositionMap;

	private final Map<String, Integer> currentTargetPosition = Maps.newHashMap();

	/**
	 * Map of the position sensings
	 */
	private final Map<String, Set<PositionUpdated>> entityPositionSensors = new HashMap<>();

	private final Map<KeyWithString, Set<PositionUpdated>> callbackBoundedWithPosition;

	private final EventProcessor eventProcessor;

	public EntityPositionModel(Map<String, Set<PositionUpdated>> sensingPositionNodeMap,
							   Map<KeyWithString, Set<PositionUpdated>> callbackBoundedWithPosition,
							   EventProcessor eventProcessor) {
		super();
		this.callbackBoundedWithPosition = callbackBoundedWithPosition;
		this.eventProcessor = eventProcessor;
        this.entityPositionMap = new HashMap<>();
	}

	public void setNewEntityPosition(String entityId, int positionByNodeId) {
		setNewEntityPosition(entityId, positionByNodeId, false);
	}

	/**
	 * Set new entity position.
	 *
	 * @param entityId
	 * 		Id of related entity.
	 * @param positionByNodeId
	 * 		New position.
	 * @param silent
	 * 		If true the position sensing is omitted.
	 */
	public synchronized void setNewEntityPosition(String entityId, int positionByNodeId, boolean silent) {
		entityPositionMap.put(entityId, positionByNodeId);
		if (!silent) {
			callCallbacks(entityId, positionByNodeId);
		}
	}

	// Call all sensings.
	private void callCallbacks(final String entityId, final int nodeId) {

		Set<PositionUpdated> entityCallbacks = entityPositionSensors.get(entityId);
		if (entityCallbacks != null) {
			for (PositionUpdated callback : entityCallbacks) {
				newEntityPosition(entityId, nodeId, callback);
			}
		}

		Set<PositionUpdated> callbacks = callbackBoundedWithPosition.get(new KeyWithString(nodeId, entityId));
		if (callbacks != null) {
			callbacks = new HashSet<>(callbacks);
			for (PositionUpdated callback : callbacks) {
				newEntityPosition(entityId, nodeId, callback);
			}
		}

	}

	/**
	 * Remove entity from this storage. This doesn't invoke sensing.
	 *
	 * @param entityId
	 * 		Name of entity to be removed.
	 */
	public synchronized void removeEntity(String entityId) {
		entityPositionMap.remove(entityId);
	}

	public synchronized void addSensingPositionNode(String entityId, PositionUpdated positionSensor) {

		Set<PositionUpdated> callbacks = InitAndGetterUtil.getDataOrInitFromMap(entityPositionSensors, entityId, new
				HashSet<>());

		callbacks.add(positionSensor);

		entityPositionSensors.put(entityId, callbacks);

	}

	public synchronized void removeSensingPositionNode(String entityId, PositionUpdated sensingPositionNode) {

		Set<PositionUpdated> callbacks = InitAndGetterUtil.getDataOrInitFromMap(entityPositionSensors, entityId, new
				HashSet<>());

		callbacks.remove(sensingPositionNode);

		entityPositionSensors.put(entityId, callbacks);

	}

	/**
	 * returns names of all bodies
	 */
	public synchronized Set<String> getIDs() {
		return entityPositionMap.keySet();
	}

	/**
	 * returns names of all bodies
	 */
	public synchronized Set<String> getCopyIDs() {
		return new HashSet<>(entityPositionMap.keySet());
	}

	public synchronized Integer getEntityPositionByNodeId(String entityId) {
		return entityPositionMap.get(entityId);
	}
    
    public synchronized Integer getEntityTargetPositionByNodeId(String entityId) {
		return currentTargetPosition.get(entityId);
	}

	public void removePositionCallbackForNode(String entityId, long nodeId, PositionUpdated positionEntityCallback) {

		KeyWithString keyWithString = new KeyWithString(nodeId, entityId);

		Set<PositionUpdated> positionCallbacksForNode = InitAndGetterUtil.getDataOrInitFromMap
				(callbackBoundedWithPosition, keyWithString, new HashSet<>());

		positionCallbacksForNode.remove(positionEntityCallback);

		if (positionCallbacksForNode.isEmpty()) {
			callbackBoundedWithPosition.remove(keyWithString);
		} else {
			callbackBoundedWithPosition.put(keyWithString, positionCallbacksForNode);
		}

	}

	public void addPositionCallbackForNode(String entityId, long nodeId, PositionUpdated positionSensor) {

		KeyWithString keyWithString = new KeyWithString(nodeId, entityId);

		Set<PositionUpdated> positionCallbacksForNode = InitAndGetterUtil.getDataOrInitFromMap
				(callbackBoundedWithPosition, keyWithString, new HashSet<>());

		positionCallbacksForNode.add(positionSensor);

		callbackBoundedWithPosition.put(keyWithString, positionCallbacksForNode);
	}

	private synchronized void newEntityPosition(final String entityId, final int nodeId, final PositionUpdated entityCallback) {

		Integer targetNode = currentTargetPosition.get(entityId);
		if (targetNode == null || !targetNode.equals(nodeId)) {
			return;
		}

		eventProcessor.addEvent(new EntityPositionHandler(entityId, nodeId, entityCallback));
	}

	public synchronized boolean setTargetPositionAndReturnIfWasSame(String entityId, int nodeId) {
		Integer old = currentTargetPosition.put(entityId, nodeId);
		return old != null && old.equals(nodeId);
	}
    
    public synchronized Map<String, Integer> getCurrentTargetPositions() {
		return new HashMap<>(currentTargetPosition);
	}
    
    public synchronized Map<String, Integer> getCurrentPositions() {
		return new HashMap<>(entityPositionMap);
	}
    
    public synchronized void removetargetPosition(String entityId){
        currentTargetPosition.remove(entityId);
    }
    

	class EntityPositionHandler extends EventHandlerAdapter {

		final long nodeId;
		final String entityId;
		final PositionUpdated entityCallback;

		EntityPositionHandler(String entityId, long nodeId, PositionUpdated entityCallback) {
			this.nodeId = nodeId;
			this.entityId = entityId;
			this.entityCallback = entityCallback;
		}

		@Override
		public void handleEvent(Event event) {
            removetargetPosition(entityId);
			entityCallback.newEntityPosition(entityId, nodeId);
		}
	}

	
    
    public class EntityNodePositionIterator{
		
		private final Iterator<String> idIterator;

		public EntityNodePositionIterator() {
			idIterator = getCopyIDs().iterator();
		}
		
		public Integer getNextEntityNodeId(){
			while(idIterator.hasNext()){
				return getEntityPositionByNodeId(idIterator.next());
			}
			return null;
		}
		
	}

}
