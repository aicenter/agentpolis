package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.spatialrefsys;

import java.io.Serializable;

/**
 * 
 * The class wraps Spatial Reference System Identifier (SRID). It is using for
 * projecting coordinates from WGS84 to Spatial Reference System given by SRID
 * 
 * @author Zbynek Moler
 * 
 */
public class SRID implements Serializable {

    private static final long serialVersionUID = -6220471765294814813L;
    
    public int srid;

	public SRID(int srid) {
		super();
		this.srid = srid;
	}

}
