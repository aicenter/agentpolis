highway {

    agent = "SDAgent";
    //      agent = "RouteAgent";
//    agent = "GSDAgent";


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
        safetyReserve = 15;
    }
    rvo {
        visibilityGraphRadius = 1000;
        timestep = 1.0;

        agent {
            agentMaxSpeed = 20.0;
            radiusConstant = 1.0;
            timeHorizon = 100.0;
            timeHorizonObst = 100.0;
            orcaSpeedConstant = 1.0;
            maxNeighbors = 100;
        }

        vis {
            showInflatedObstacles = true;
            timestep = 1;
            maxtime = 100000;
        }
    }
    // Dashboard configuration
    dashboard {
        numberOfCarsInSimulation = 25;
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
        timestep = 10; //ms
        perfectExecution = false;
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
//          folder = "nets/skoda-parking"
//           folder = "nets/test_gsda0"
//        folder = "nets/test_bottleneck"
//        folder = "nets/test_snail"
        //  folder = "nets/test_sda1"
        //  folder = "nets/artificialHighway"
//         folder = "nets/mlada-boleslav/"
//        folder = "nets/skoda-parking/"
        // folder = "nets/CharlesSquare";
        // folder = "nets/dresden/";
        // folder = "super-collision";
        // folder = "nets/x-junction/";
         folder = "nets/pisek-all/"
//         folder = "nets/junction-big/";
//        folder = "nets/highway-straight/";
        // folder = "nets/artificialX-junction";
        // folder = "nets/ulesika";
        // folder = "nets/nartest";
        // folder = "nets/artificialHighway-funnel";
        // folder = "nets/hostinne";
        // folder = "nets/artificialXS-junction";
        // folder = "nets/artificialT-junction";
        // folder = "nets/pisek";
        lane {
            stepSize = 1; // scale distance between waypoints
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