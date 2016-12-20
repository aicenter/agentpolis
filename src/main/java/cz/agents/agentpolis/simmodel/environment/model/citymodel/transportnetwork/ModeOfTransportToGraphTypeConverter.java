package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import cz.agents.multimodalstructures.additional.ModeOfTransport;

import java.util.HashSet;
import java.util.Set;

public class ModeOfTransportToGraphTypeConverter {
    public static GraphType convert(ModeOfTransport mode) {
        GraphType type = EGraphType.HIGHWAY;
        switch (mode) {
            case WALK:
                type = EGraphType.PEDESTRIAN;
                break;
            case TAXI:
            case CAR:
            case SHARED_CAR:
            case MOTORCYCLE:
            case SHARED_ELECTRIC_SCOOTER:
                type = EGraphType.HIGHWAY;
                break;
            case BIKE:
            case SHARED_BIKE:
                type = EGraphType.BIKEWAY;
                break;
            case BUS:
            case BUS_ON_DEMAND:
                type = EGraphType.BUSWAY;
                break;
            case TRAM:
                type = EGraphType.TRAMWAY;
                break;
            case UNDERGROUND:
                type = EGraphType.METROWAY;
                break;
            case TRAIN:
                break;
            case TROLLEYBUS:
                type = EGraphType.TROLLEYBUSWAY;
                break;
            case FERRY:
            case OTHER:
            case CABLE_CAR:
            case GONDOLA:
            case FUNICULAR:
            default:
                return EGraphType.HIGHWAY;
        }
        return type;
    }

    public static Set<ModeOfTransport> convert(GraphType graphType) {
        Set<ModeOfTransport> modes = new HashSet<>();

        //TODO we need to define the relations
        modes.add(ModeOfTransport.CAR);

        return modes;
    }
}
