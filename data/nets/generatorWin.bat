netconvert   --osm-files "%1\%1.osm" -o "%1\%1.net.xml" --tls.join --no-internal-links --no-turnarounds true --proj "+proj=utm +ellps=bessel +units=m"
netconvert   --osm-files "%1\%1.osm" --plain-output-prefix "%1\plain" --proj.plain-geo
python "%SUMO_HOME%tools/randomTrips.py" -n "%1\%1.net.xml" -e 50 -l -o "%1\%1.trips.xml"
duarouter --trip-files="%1\%1.trips.xml" --net="%1\%1.net.xml"  --output-file="%1\%1.rou.xml" --ignore-errors --routing-algorithm=astar