package cz.agents.agentpolis.simmodel.environment.model.delaymodel.key;

/**
 * Represents key based on position between to nodes  
 * 
 * @author Zbynek Moler
 *
 */
public class FromToPositionKey {

	
	public final long fromPositionByNodeId;
	public final long toPositionByNodeId;
	
	public FromToPositionKey(long fromPositionByNodeId, long toPositionByNodeId) {
		super();
		
		this.fromPositionByNodeId = fromPositionByNodeId;
		this.toPositionByNodeId = toPositionByNodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fromPositionByNodeId ^ (fromPositionByNodeId >>> 32));
		result = prime * result + (int) (toPositionByNodeId ^ (toPositionByNodeId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		FromToPositionKey other = (FromToPositionKey) obj;
		if (fromPositionByNodeId != other.fromPositionByNodeId){
			return false;
		}
		if (toPositionByNodeId != other.toPositionByNodeId){
			return false;
		}
		return true;
	}

		
	

}
