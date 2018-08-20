package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

/**
 * Structure holding a request data of a junction connection loaded from sumo .net.xml file
 * Created by pavel on 19.6.14.
 */
public class Request {

    private final String index;
    private final String response;
    private final String foes;

    public Request(String index, String response, String foes) {
        this.index = index;
        this.response = response;
        this.foes = foes;
    }
}
