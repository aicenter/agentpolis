highway {

//    agent = "SDAgent";
//    agent = "RouteAgent";
    agent = "GSDAgent";


    safeDistanceAgent {
        safetyReserveDistance = 10.0;     // [m] - safety distance offset (including vehicle length and separation gap)
        narrowingModeActive = true;
        distanceToActivateNM = 500;
        // [m] - when distance to obstacle is smaller than this value NARROWING MODE is activated

        maneuvers {
            //maneuver is discratization unit
            //available maneuvers are Straight, Acceleration, Deacceleration, LaneLeft, LaneRight
            //parameters of maneuvers are following
            laneChangeManeuverDuration = 0.5; // [s]
            straightManeuverDuration = 0.5;     //[s]
            accelerationManeuverDuration = 0.3;   //[s]
            deaccelerationManueverDuration = 0.3;   //[s]
            acceleration = 4.0;                     //[m/s^2]
            deacceleration = -6.0;                  //[m/s^2]
            maximalSpeed = 20.0;                    //[m/s]
            maxSpeedVariance = 0.60;                     //[%]
        }
    }
    storage {
        insertSpeed = 0.5;  //[m/s]
        checkingDistance = 500;
        safetyReserve = 5;
    }
    dashboard {
        numberOfCarsInSimulation = 250;
        sumoSimulation = true;
        systemTime = false;
    }

    SimulatorLocal {
        timestep = 1;
    }
}
simulator {
    lite {
        name = "Simulator-Lite";
        seed = 0;                          // random number generator seed
        simulationDuration = -1;      // - 1 = infinity
        simulationSpeed = 1;
        timestep = 20; //ms
        perfectExecution = true;
        vis {
            isOn = true;
            SimulationControlLayer = true;
            NetVisLayer = true;
            TrafficVisLayer = true;
            ZoomVehicleLayer = false;
            AgentDriveVisLayer = true;
            RoadObjectLayer = true;
            StateSpaceVehicleLayer = true;
        }
    }
    net {
        folder = "nets/test_gsda0";
        lane {
            stepSize = 0.2; // distance between waypoints
        }
    }
    netLayer {
        lane {
            view = true;
            width = 1;
        }
        edge {
            view = false;
            width = 10;
        }
        crossRoad {
            view = false;
            width = 1;
        }
    }
}