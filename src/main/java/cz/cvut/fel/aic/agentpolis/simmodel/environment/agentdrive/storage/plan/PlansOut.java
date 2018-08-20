package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;

import java.util.*;


public class PlansOut {
	
	private Map<Integer, Collection<Action>> plans;
    private Map<Integer, RoadObject> currStates;

    public PlansOut(PlansOut oldPlans) {
	    this();
        this.plans.putAll(oldPlans.plans);
    }

    public PlansOut() {
        this.plans =  new LinkedHashMap<Integer, Collection<Action>>();
        this.currStates = new LinkedHashMap<Integer, RoadObject>();
    }

    public void addActions(int id, List<Action> actions){
        Collection<Action> plan = plans.get(id);
        if(plan == null){
            plan = new LinkedList<Action>();
            plans.put(id, plan);
        }
        plan.addAll(actions);
    }

    public void addAction(Action action){
		Collection<Action> plan = plans.get(action.getCarId());
		if(plan == null){
			plan = new LinkedList<Action>();
			plans.put(action.getCarId(), plan);
		}
		plan.add(action);
	}
	
	public Collection<Action> getPlan(int carId) {
		return plans.get(carId);
	}
	
	public Collection<Integer> getCarIds() {
		return plans.keySet();
	}

    public void clear() {
       plans.clear();
        
    }
    public void initNewPlans(){
        plans = new LinkedHashMap<Integer, Collection<Action>>();
    }

    public Map<Integer, RoadObject> getCurrStates() {
        return currStates;
    }

    public void setCurrStates(Map<Integer, RoadObject> currStates) {
        this.currStates = currStates;
    }
    @Override
    public String toString() {
        return plans.toString();
    }


}
