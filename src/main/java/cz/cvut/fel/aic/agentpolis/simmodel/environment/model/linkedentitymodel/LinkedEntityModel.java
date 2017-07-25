package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;

@Singleton
public class LinkedEntityModel {

    private final Map<String, String> linkedEntityToMainEntity;
    private final Map<String, Set<String>> linkedEntities;
    private final Map<String, LinkedEntitySensor> linkedEntityAndCallback;

    public LinkedEntityModel() {
        linkedEntities = new HashMap<String, Set<String>>();
        linkedEntityToMainEntity = new HashMap<String, String>();
        linkedEntityAndCallback = new HashMap<String, LinkedEntitySensor>();
    }

    public Set<String> getLinkedEntites(String entityId) {
        Set<String> entities = linkedEntities.get(entityId);
        if (entities == null) {
            entities = new HashSet<String>();
        }

        return new HashSet<String>(entities);
    }

    /**
     * Makes a relationship (link) between two entities
     * 
     * 
     * @param mainEntityById
     *            - the independent entity
     * @param linkedEntityById
     *            - the entity depend on mainEntityById
     * @param linkedEntityCallback
     *            - the callback relating with linkedEntityById
     */
    public void linkEnities(String mainEntityById, String linkedEntityById,
            LinkedEntitySensor linkedEntityCallback) {

        assert linkedEntityById != null : "linkedEntityById has not to be null";
        assert mainEntityById != null : "mainEntityById has not to be null";

        Set<String> entities = getLinkedEntites(mainEntityById);
        entities.add(linkedEntityById);

        linkedEntityAndCallback.put(linkedEntityById, linkedEntityCallback);
        linkedEntityToMainEntity.put(linkedEntityById, mainEntityById);
        linkedEntities.put(mainEntityById, entities);

    }

    /**
     * Makes a relationship (link) between two entities
     * 
     * 
     * @param mainEntityById
     *            - the independent entity
     * @param linkedEntityById
     *            - the entity depend on mainEntityById
     * 
     */
    public void linkEnities(String mainEntityById, String linkedEntityById) {

        linkEnities(mainEntityById, linkedEntityById, new EmptyLinkedEntityCallback());
    }

    /**
     * 
     * Cancels all the relationship for given entity
     * 
     * @param linkedEntityById
     */
    public void unLinkEnities(String linkedEntityById) {

        assert linkedEntityById != null : "linkedEntityById has not to be null";

        LinkedEntitySensor linkedEntityCallback = linkedEntityAndCallback.get(linkedEntityById);
        String mainEntity = linkedEntityToMainEntity.get(linkedEntityById);

        unLinkEnitiesWithoutCalling(linkedEntityById);

        linkedEntityCallback.entityWasUnlinked(mainEntity);

    }

    /**
     * 
     * Cancels all the relationship for given entity without calling callback
     * 
     * @param linkedEntityById
     */
    public void unLinkEnitiesWithoutCalling(String linkedEntityById) {

        assert linkedEntityById != null : "linkedEntityById has not to be null";

        String mainEntityById = linkedEntityToMainEntity.get(linkedEntityById);

        Set<String> entities = getLinkedEntites(mainEntityById);
        entities.remove(linkedEntityById);

        linkedEntityToMainEntity.remove(linkedEntityById);
        linkedEntities.put(mainEntityById, entities);

        linkedEntityAndCallback.remove(linkedEntityById);

    }

    /**
     * Returns the number entities linked with a particular entity
     * 
     * @param mainEntityId
     * @return
     */
    public int numOfLinkedEntites(String mainEntityId) {
        Set<String> linkedEntitiesToMain = linkedEntities.get(mainEntityId);
        if (linkedEntitiesToMain == null) {
            return 0;
        }

        return linkedEntitiesToMain.size();
    }

    /**
     * It represents empty call
     * 
     * @author Zbynek Moler
     * 
     */
    private class EmptyLinkedEntityCallback implements LinkedEntitySensor {

        public void entityWasUnlinked(String unlinkedFromEntityId) {
            // it is empty call
        }

    }

}
