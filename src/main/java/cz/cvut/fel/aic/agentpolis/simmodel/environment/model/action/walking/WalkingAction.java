package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.walking;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MoveEntityAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.query.PositionQuery;

/**
 * Action represents agent walk action from one position to other.
 *
 * @author Zbynek Moler
 */
@Singleton
public class WalkingAction {

	private final MoveEntityAction moveToNextNodeAction;

	private final AgentPositionModel agentPositionModel;
	private final EntityVelocityModel maxEntitySpeedStorage;

	private final PositionQuery positionQuery;

	@Inject
	public WalkingAction(MoveEntityAction moveToNextNodeAction, AgentPositionModel agentPositionModel,
						 EntityVelocityModel maxEntitySpeedStorage, PositionQuery positionQuery) {
		super();
		this.moveToNextNodeAction = moveToNextNodeAction;
		this.agentPositionModel = agentPositionModel;
		this.maxEntitySpeedStorage = maxEntitySpeedStorage;
		this.positionQuery = positionQuery;
	}

	/**
	 * Compute duration of walk from one position to other and do walk.
	 *
	 * @param startByNodeId
	 * @param destinationByNodeId
	 * @param typeOfGraphForMoving
	 */
	public void walk(final String agentId, int startByNodeId, int destinationByNodeId, GraphType typeOfGraphForMoving) {

		double velocity = maxEntitySpeedStorage.getEntityVelocityInmps(agentId);

		long duration = MoveUtil.computeDuration(velocity, positionQuery.getLengthBetweenPositions
                (typeOfGraphForMoving, startByNodeId, destinationByNodeId));

		moveToNextNodeAction.moveToNode(agentId, destinationByNodeId, duration, agentPositionModel);

	}

}
