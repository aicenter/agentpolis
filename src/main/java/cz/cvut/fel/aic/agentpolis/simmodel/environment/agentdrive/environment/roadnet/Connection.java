package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

/**
 * Structure holding connections through junction loaded from sumo .net.xml file
 * Created by pavel on 20.6.14.
 */
public class Connection {
    private final String from;
    private final String to;
    private final String fromLane;
    private final String toLane;

    public Connection(String from, String to, String fromLane, String toLane) {
        this.from = from;
        this.to = to;
        this.fromLane = fromLane;
        this.toLane = toLane;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFromLane() {
        return fromLane;
    }

    public String getToLane() {
        return toLane;
    }
}
