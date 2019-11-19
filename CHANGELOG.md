# 7.0.0

## Changed
- Trip refactoring - new more systematic trip system, locations backed by array
- JGraphT version updated
- Dijkstra replaced by Astar in path planning

## Added
- vehicle highlighting by switching to static size


# 6.0.0

## FIxed
- `DelayData` are now deleted after each `Move` action, preventing some bugs related to expected travel time


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

