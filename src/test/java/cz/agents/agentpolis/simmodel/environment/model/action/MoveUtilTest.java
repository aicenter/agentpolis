package cz.agents.agentpolis.simmodel.environment.model.action;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;

public class MoveUtilTest {

	Mocks mocks;
	
	@Before
	public void setUp(){		
		mocks = new Mocks();
	}
	
	@Test
	public void moveUtil(){
		
		
		long duration = MoveUtil.computeDuration(100,36);
	
		assertEquals(360, duration);
		
	}
	
}
