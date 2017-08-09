package cz.cvut.fel.aic.agentpolis.simmodel.environment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The general storage for entity in a simulation model (e.g. for vehicle,
 * agents).
 *
 * @param <TEntity> Entity type
 */
public class EntityStorage<TEntity extends AgentPolisEntity> implements Iterable<TEntity> {

    private final Map<String, TEntity> entities;
    private final Map<EntityType, Set<String>> entitiesByType;

    public EntityStorage() {
        super();
        this.entities = new ConcurrentHashMap<>();
        this.entitiesByType = new ConcurrentHashMap<>();
    }

    /**
     * Adds entity into the storage, its id has to be unique
     *
     * @param entity
     */
    public void addEntity(TEntity entity) {
        checkArgument(entities.containsKey(entity.getId()) == false, "Duplicate entity i storage: "
                + entity.getId());

        entities.put(entity.getId(), entity);

        EntityType type = entity.getType();
        if (!entitiesByType.containsKey(type)) {
            entitiesByType.put(type, new HashSet<String>());
        }

        entitiesByType.get(type).add(entity.getId());
    }

    public void removeEntity(TEntity entity) {
        entities.remove(entity.getId());
        entitiesByType.get(entity.getType()).remove(entity.getId());
    }

    /**
     * Returns entity base on given id
     *
     * @param entityId
     * @return
     */
    public TEntity getEntityById(String entityId) {
        return entities.get(checkNotNull(entityId));
    }

    /**
     * Returns all entity ids
     */
    public ImmutableSet<String> getEntityIds() {
        return ImmutableSet.copyOf(entities.keySet());
    }

    @Override
    public Iterator<TEntity> iterator() {
        return new EntityIterator();
    }

    public class EntityIterator implements Iterator<TEntity> {

        private final Iterator<TEntity> iterator;

        public EntityIterator() {
            iterator = entities.values().iterator();
        }

        public TEntity getNextEntity() {
            while (iterator.hasNext()) {
                TEntity entity = iterator.next();
                return entity;
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public TEntity next() {
            return iterator.next();
        }

    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }

    public Collection<TEntity> getEntities() {
        return entities.values();
    }

}
