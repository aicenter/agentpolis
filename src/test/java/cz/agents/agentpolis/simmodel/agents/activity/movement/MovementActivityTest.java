package cz.agents.agentpolis.simmodel.agents.activity.movement;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

public class MovementActivityTest {

	Mocks mocks;
	
	@Before
	public void setUp(){
		mocks = new Mocks();
	}
	
	@Test
	public void movementActivityTest(){
		
//		LinkedList<Long> destination = new LinkedList<Long>();
//		destination.add(GraphMock.node1.getId());
//		destination.add(GraphMock.node2.getId());
//		destination.add(GraphMock.node3.getId());
//		
//		Trip trip = new Trip(destination, GraphMock.GraphType.GRAPH1);
//		
//		MovementActivityCallback movementCallback = mock(MovementActivityCallback.class);
//		
//		MovingActionMock movingActionMock = new MovingActionMock();
//				
//		MovementActivity movementActivity = new MovementActivity(mocks.walkerAgent, movementCallback, movingActionMock, trip);
//		movementActivity.move();				
//		
//		mocks.eventProcessor.run();
//		
//		assertEquals(GraphMock.node1.getId(), movingActionMock.startNode.longValue());
//		assertEquals(GraphMock.node2.getId(), movingActionMock.destinationByNodeId.longValue());
//		
		
	}
	
	public class MovingActionMock implements MovingAction<TripItem>{
		
		public int startNode;
		public int destinationByNodeId;
		
		public MovingActionMock() {
			
		}

		@Override
		public void moveToNextNode(int startNode, int destinationByNodeId, GraphType typeOfGraphForMoving) {
			this.startNode = startNode;
			this.destinationByNodeId = destinationByNodeId;
			
		}

		@Override
		public long moveTime() {
			return 1;
		}

		@Override
		public void notifyPlanForNextMove(int startNode, int destinationByNodeId,
										  MovingActionCallback movingActionCallback) {
			this.startNode = startNode;
			this.destinationByNodeId = destinationByNodeId;
			
		}

		

		@Override
		public String movingEntityId() {
			// TODO Auto-generated method stub
			return null;
		}

		
		@Override
		public double takenCapacity() {
			// TODO Auto-generated method stub
			return 0;
		}

	

		@Override
		public void beforeNotifyingAboutPlan(MovingActionCallback movingActionCallback, long fromPositionByNodeId,
				long toPositionByNodeId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void waitToDepartureTime(TripItem fromTripItem, TripItem toTripItem,
				MovingActionCallback movingActionCallback) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
}
