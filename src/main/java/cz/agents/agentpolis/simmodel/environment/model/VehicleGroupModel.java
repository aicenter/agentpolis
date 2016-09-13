package cz.agents.agentpolis.simmodel.environment.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.Singleton;

import cz.agents.agentpolis.utils.InitAndGetterUtil;

/**
 * 
 * The instance of class holds links between vehicles and their assigned group
 * id (e.g. line id).
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleGroupModel {

    private final Map<String, Set<String>> vehicleIdsBoundedWithGroupsId;
    private final Map<String, Set<String>> groupsIdsBoundedWithVehicle;
    private final Map<String, String> groupIdBoundedWithVehicle;

    public VehicleGroupModel() {
        vehicleIdsBoundedWithGroupsId = new HashMap<String, Set<String>>();
        groupsIdsBoundedWithVehicle = new HashMap<String, Set<String>>();
        groupIdBoundedWithVehicle = new HashMap<String, String>();
    }

    public void addVehicleToGroups(String grouId, String vehicleId) {
        proccessOperationWithVehicleToGroups(grouId, vehicleId, new OperationWithVehicleToGroups() {

            public void doOperationWithVehicleToGroups(String grouId, Set<String> groupIds,
                    String vehicleId, Set<String> vehicleIds) {
                groupIds.add(grouId);
                vehicleIds.add(vehicleId);
            }
        });

    }

    public void removeVehicleFromGroups(String grouId, String vehicleId) {

        proccessOperationWithVehicleToGroups(grouId, vehicleId, new OperationWithVehicleToGroups() {

            public void doOperationWithVehicleToGroups(String grouId, Set<String> groupIds,
                    String vehicleId, Set<String> vehicleIds) {
                groupIds.remove(grouId);
                vehicleIds.remove(vehicleId);
            }
        });

    }

    public Set<String> getVehiclesFromGroups(String groupId) {
        return getDataFromMap(groupId, vehicleIdsBoundedWithGroupsId);
    }

    public Set<String> getGroupsIdsForVehicle(String vehicleId) {
        return getDataFromMap(vehicleId, groupsIdsBoundedWithVehicle);
    }

    public void addVehicleToGroup(String groupId, String vehicleId) {
        Set<String> vehicleIds = InitAndGetterUtil.getDataOrInitFromMap(
                vehicleIdsBoundedWithGroupsId, groupId, new HashSet<String>());
        vehicleIds.add(vehicleId);
        vehicleIdsBoundedWithGroupsId.put(groupId, vehicleIds);
        groupIdBoundedWithVehicle.put(vehicleId, groupId);

    }

    public void removeVehicleFromGroup(String groupId, String vehicleId) {
        Set<String> vehicleIds = InitAndGetterUtil.getDataOrInitFromMap(
                vehicleIdsBoundedWithGroupsId, groupId, new HashSet<String>());
        vehicleIds.remove(vehicleId);
        vehicleIdsBoundedWithGroupsId.put(groupId, vehicleIds);
        groupIdBoundedWithVehicle.remove(vehicleId);

    }

    public Set<String> getVehiclesFromGroup(String groupId) {
        return vehicleIdsBoundedWithGroupsId.get(groupId);
    }

    public String getGroupIdForVehicle(String vehicleId) {
        return groupIdBoundedWithVehicle.get(vehicleId);
    }

    private Set<String> getDataFromMap(String key, Map<String, Set<String>> map) {
        Set<String> data = InitAndGetterUtil.getDataOrInitFromMap(map, key, new HashSet<String>());
        return new HashSet<String>(data);
    }

    private interface OperationWithVehicleToGroups {

        public void doOperationWithVehicleToGroups(String grouId, Set<String> groupIds,
                String vehicleId, Set<String> vehicleIds);

    }

    private void proccessOperationWithVehicleToGroups(String groupId, String vehicleId,
            OperationWithVehicleToGroups operationWithVehicleToGroups) {
        Set<String> vehicleIds = InitAndGetterUtil.getDataOrInitFromMap(
                vehicleIdsBoundedWithGroupsId, groupId, new HashSet<String>());
        Set<String> groupIds = InitAndGetterUtil.getDataOrInitFromMap(groupsIdsBoundedWithVehicle,
                vehicleId, new HashSet<String>());

        operationWithVehicleToGroups.doOperationWithVehicleToGroups(vehicleId, vehicleIds, groupId,
                groupIds);

        vehicleIdsBoundedWithGroupsId.put(groupId, vehicleIds);
        groupsIdsBoundedWithVehicle.put(vehicleId, groupIds);
    }

}
