package cz.cvut.fel.aic.agentpolis.simmodel.environment.linkedentitymodel.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.linkedentitymodel.LinkedEntityModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.linkedentitymodel.sensor.LinkedEntitySensor;

/**
 * Action implements methods, which links some entity with other entity. E.g.
 * passenger with vehicle
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class LinkEntityAction {

    private final LinkedEntityModel linkedEntityStorage;

    @Inject
    public LinkEntityAction(LinkedEntityModel linkedEntityStorage) {
        super();
        this.linkedEntityStorage = linkedEntityStorage;
    }

    /**
     * Links entity (e.g. passenger) with vehicle
     * 
     * @param mainEntityById
     *            - e.g. vehicleId
     * @param linkedEntityById
     *            - e.g. passengerId
     */
    public void linkEnities(String mainEntityById, String linkedEntityById) {
        // it is important to add immediately
        linkedEntityStorage.linkEnities(mainEntityById, linkedEntityById);
    }

    /**
     * Links entity (e.g. passenger) with vehicle and adds callback
     * 
     * @param mainEntityById
     * @param linkedEntityById
     * @param linkedEntityCallback
     */
    public void linkEnities(String mainEntityById, String linkedEntityById,
            LinkedEntitySensor linkedEntityCallback) {
        // it is important to add immediately
        linkedEntityStorage.linkEnities(mainEntityById, linkedEntityById, linkedEntityCallback);
    }

    /**
     * Unlinks linked entity
     * 
     * @param unLinkedEntityById
     */
    public void unLinkEnities(String unLinkedEntityById) {
        // it is important to add immediately
        linkedEntityStorage.unLinkEnities(unLinkedEntityById);

    }

    /**
     * Unlinks linked entity, but during unlinking will not invokes callback
     * 
     * @param unLinkedEntityById
     */
    public void unLinkEnitiesWithoutCalling(String unLinkedEntityById) {
        // it is important to add immediately
        linkedEntityStorage.unLinkEnitiesWithoutCalling(unLinkedEntityById);

    }

}
