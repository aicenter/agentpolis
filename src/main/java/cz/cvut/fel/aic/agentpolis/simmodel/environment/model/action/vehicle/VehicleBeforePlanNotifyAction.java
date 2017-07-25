package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.vehicle;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.BeforePlanNotifyModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;

/**
 * Starts process of notifying dependent entities before these entities are
 * informed about vehicle next plan.
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleBeforePlanNotifyAction {

    private final BeforePlanNotifyModel planNotifyStorage;
    private final LinkedEntityModel linkedEntityStorage;

    @Inject
    public VehicleBeforePlanNotifyAction(BeforePlanNotifyModel planNotifyStorage,
            LinkedEntityModel linkedEntityStorage) {
        super();
        this.planNotifyStorage = planNotifyStorage;
        this.linkedEntityStorage = linkedEntityStorage;
    }

    /**
     * Notify dependent entities before these entities are informed about
     * vehicle next plan.
     * 
     * @param movingActionCallback
     */
    public void callBeforePlanNotify(String vehicleId, MovingActionCallback movingActionCallback,
            long fromPositionByNodeId, long toPositionByNodeId) {

        Set<String> linkedPassenger = linkedEntityStorage.getLinkedEntites(vehicleId);

        planNotifyStorage.callBeforePlanNotify(vehicleId, fromPositionByNodeId, toPositionByNodeId,
                linkedPassenger, movingActionCallback);

    }
}
