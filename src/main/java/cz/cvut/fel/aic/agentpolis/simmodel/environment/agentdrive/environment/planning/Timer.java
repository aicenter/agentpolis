package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning;

/**
 * Simple class, that measures elapsed time, since some mark
 * Created by wmatex on 13.11.14.
 */
public class Timer {
    private static long NANO  = 1000000000;
    private static long MICRO = 1000000;
    private static long MILLI = 1000;
    long startTime = 0;

    public Timer(boolean start) {
        if (start) {
            this.reset();
        }
    }

    public String getElapsedTime() {
        long elapsed = this.getNanoTime()-startTime;

        long sec = elapsed/MILLI;
        long msec = elapsed-sec*MILLI;
        return String.format("%ds %03dms", sec, msec);
    }

    public long getRawElapsedTime() {
//        return (this.getNanoTime()-startTime)/MICRO;
        return (this.getNanoTime()-startTime);
    }

    public void reset() {
        startTime = this.getNanoTime();
    }

//    private long getNanoTime() {
//        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
//    }

    private long getNanoTime() {
        return System.currentTimeMillis();
    }
}
