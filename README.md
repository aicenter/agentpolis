# Agentpolis

Agentpolis is a fully agent-based platform for modeling transportation systems. It comprises a high-performance discrete-event simulation core, a cohesive set of high-level abstractions for building extensible agent-based models and a library of predefined components frequently used in transportation and mobility models. Together with a suite of supporting tools, Agentpolis enables rapid prototyping and execution of data-driven simulations of a wide range of mobility and transportation phenomena. 

# Agentdrive package

[Original AgentDrive project](https://github.com/schaemar/AgentDrive)

Agentdrive is used in agentpolis to simulate traffic which takes in consideration collision-avoidance agents and as a result of that creates more accurate simulation.

##### Major differences 

Agentdrive is initialized in AgentDriveModel and it is designed to handle events from activities and periodically send updates to those activities.

Agentdrive works with positions given in projected coordinates. As a result of that there are new interfaces for activities, agents and vehicles added which are meant to be used with AD package. 

##### Agentdrive events overview

INITIALIZE - Event meant to be send to AgentDriveModel with VehicleInitializationData as a content. Adds vehicle to AD's environment.

UPDATE_TRIP - Event meant to be send to AgentDriveModel with TripUpdateMessage as a content. Changes planned route of a vehicle.

DATA - Event periodically send to AgentDriveModel. Calls updateVehicles(eventContent); 

UPDATE_NODE_POS - Event meant to be send to an activity from AgentDriveModel with content EdgeUpdateMessage. Updates last visited node in an activity.

UPDATE_DESTINATION - Event meant to be send to an activity from AgentDriveModel with content DestinationUpdateMessage. Informs activity about an agent reaching its destination.

UPDATE_POS - Event meant to be send to an activity from AgentDriveModel with content UpdatePositionMessage. Sends out current agent coordinates.

FINISH - Event meant to be send to AgentDriveModel. Should remove an agent from AD's environment.

##### Suggestions for improvements

- AgentDrive should be used as a dependency and there should be no need to have full package in agentpolis, but AD in agentpolis is quite different from original AD project. Original AD project should be modified to make it possible.
- Optimize methods in RouteNavigator and Network. Simulation becomes very slow with 50+ vehicles.
- Updating vehicle position in between AD's updates should make simulation considerably faster and also keep it smooth. (similar to imperfect execution in agentdrive) 

##### Demo
Demo simulation can be found in [Agentpolis Demo](https://github.com/aicenter/agentpolis-demo) in branch agentdrive. 
## Agentpolis Demo
The capabilities of Agentpolis are illustrated in [Agentpolis Demo](https://github.com/aicenter/agentpolis-demo). 

## Website
http://agents.felk.cvut.cz/projects/agentpolis
