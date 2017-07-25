/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;

/**
 *
 * @author fido
 */
public enum CongestionTestType implements EntityType{
    TEST_VEHICLE,
    TEST_DRIVER;

    @Override
    public String getDescriptionEntityType() {
        return "";
    }
}
