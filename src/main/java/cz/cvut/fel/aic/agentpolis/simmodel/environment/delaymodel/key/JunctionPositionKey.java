package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key;

/**
 * Represents key (crossroad) based on position
 * 
 * @author Zbynek Moler
 *
 */
public class JunctionPositionKey {
				
		private long positionByNodeId;
		
		public JunctionPositionKey(long positionByNodeId) {
			super();			
			this.positionByNodeId = positionByNodeId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (positionByNodeId ^ (positionByNodeId >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			JunctionPositionKey other = (JunctionPositionKey) obj;
			if (positionByNodeId != other.positionByNodeId){
				return false;
			}
			return true;
		}

		


}
