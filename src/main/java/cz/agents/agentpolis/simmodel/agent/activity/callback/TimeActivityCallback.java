package cz.agents.agentpolis.simmodel.agent.activity.callback;

/**
 * 
 * New terminology: It is the activity called WaitingFinished
 * 
 * The callback invoked during executing {@code TimeSpendingActivity}
 * 
 * @author Zbynek Moler
 * */
public interface TimeActivityCallback {

    /**
     * Method is invoked, when time occurred
     * 
     * @param time
     */
    public void timeCallback();
}
