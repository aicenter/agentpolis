<!--
Copyright (c) 2021 Czech Technical University in Prague.

This file is part of Agentpolis project.
(see https://github.com/aicenter/agentpolis).

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
-->

# 7.0.0

## FIxed
- New York Map envelope added to default config
- prepare map script uncommented to be ready for new users
- `AstarShortestPathPlanner` concurrency bug fixed
- `EntityLayer`: concurrency bug related to entity position map fixed
- Bug fix in vehicle plan layer, it now correctly handles the cases when the vehicle just finished the move
- Python script for map preparation now creates the map dir if it not exists

## Changed
- `Trip` refactoring 
	- new more systematic trip system, locations backed by array
	- Trip ID moved to trip
- `JGraphT` version updated
- Dijkstra replaced by Astar in path planning
- `getEntityPositionInTime` and `drawEntityShape` methods in vehicle layer made public
- parent updated to 3.0.0
- `EdgeShape` members made private
- log format changed: package structure discarded
- not implemented exception in Lane class changed to our exception
- `DefaultVisioInitializer` 
	- constructor changed (unused networks and config discarded)
	- centroid for initial view is now computed from GPS coordinate instead of the projected coordinates
- Guice version raised
- logback config removed to enable config in client projects
- Alite version raised to 2.1.0 SNAPSHOT

## Added
- vehicle highlighting by switching to static size
- `VisioUtils.printTextWithBackgroud` method now has a transparency option
- Not implemented exception class
- ResourceReader.getAbsoultePathToResource method
- new sample config file for Plzen

## Removed
- old process_map.py script for preparing map. Current script for the same purpose is agentpolis/prepare_map.py


# 6.0.0

## FIxed
- `DelayData` are now deleted after each `Move` action, preventing some bugs related to expected travel time
- concurrency exceptions related `EtityLayer.entityPositionMap` fixed
- `VisioPositionUtil.getPositionInterpolatedInTime` method now handle the case where the vehicle just finished the move action


## Changed
- start of the semantic versioning
- `wayId` in `SimulationEdge` renamed to `staticId` and changed from `long` to `BigInteger`
- `edge.length` is now used everywhere, instead of `edge.shape.length`
- all durations are now computed in MoveUtil
- posted speed is now in kmh
- all properties related to edge length converted to integers, with the cm precision. All related methods renamed to
 prevent confusion.
- map simplification turned off as it is done in preprocessing
- `Benchmark` class is now thread-safe
- changed versins of all AIC dependencies, check Alite, Geographtools and Graph Importer projects changelogs for changes


## Added
- python package initialization: all python code is now in an `agentpolis` python module. One of the advantages of the 
new setup is the easier installation process.
- map preparation script that depend on roadmaptools
- integer `index` property added to `SimulationNode` to enable language independent random access to nodes. Index to 
node mapping can be plugged in anywhere injecting the `NodesMappedByIndex` class
- new `getLocationIndexes` and `printLocationIndexes` debug methods in `TripsUtil`
- `getRemainingTime` method added to `DelayData` class


