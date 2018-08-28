package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;


import cz.agents.alite.configurator.Configurator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.NetworkLocation;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util.Utils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tt.euclid2d.Point;
import tt.euclid2d.region.Rectangle;

import javax.vecmath.Point2f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * XMLReader reads a .net.xml, .rou.xml file and creates a street network.
 */
public class XMLReader {
    private final static Logger logger = Logger.getLogger(XMLReader.class);

    private NetworkLocation networkLocation;
    private HashMap<String, Edge> edgeMap;
    private HashMap<String, Junction> junctionMap;
    private HashMap<String, LaneImpl> laneMap;
    private ArrayList<Connection> connectionList;
    private ArrayList<String> tunnels;
    private ArrayList<String> bridges;

    private Network network;

    private HashMap<Integer, List<String>> routes;
    private Map<Integer, Point2f> initialPositions;
    private Map<Integer, Float> departures;
    private boolean isNetworkLoaded = false;


    public XMLReader() {
    }

    public XMLReader(String networkFolder) {
        read(networkFolder);
        isNetworkLoaded = true;
    }

    public boolean isNetworkLoaded() {
        return isNetworkLoaded;
    }
//
//    /**
//     * @return map from vehicleID to its route
//     */
//    public HashMap<Integer, List<String>> getRoutes() {
//        checkIfLoaded();
//        return routes;
//    }

    public Network getNetwork() {
        checkIfLoaded();
        return network;
    }

    public Map<Integer, Point2f> getInitialPositions() {
        checkIfLoaded();
        return initialPositions;
    }

    public Map<Integer, Float> getDepartures() {
        checkIfLoaded();
        return departures;
    }

    private void checkIfLoaded() {
        if (!isNetworkLoaded) {
            logger.warn("Calling " + this.getClass().getName() + " methods, while Network is not loaded!");
        }
    }

    public RoadNetwork parseNetwork(String netFolderPath) {
        read(netFolderPath);
        return network;
    }


    public void read(String networkFolder) {
        logger.info("PARSING NETWORK");

        try {
            File networkFile = Utils.getFileWithSuffix(networkFolder, ".net.xml");
            File plainNodeFile = Utils.getFileWithSuffix(networkFolder, ".nod.xml");
            parseNetworkFromFile(networkFile, plainNodeFile);

            parseMultilevelJunctions();

            network = new Network(networkLocation, edgeMap, junctionMap, laneMap, connectionList, tunnels, bridges);
            isNetworkLoaded = true;
            logger.info("NETWORK PARSED");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            logger.info("Parsing routes...");
//            routes = parseRoutes(Utils.getFileWithSuffix(networkFolder, ".rou.xml"));
//            logger.info("Routes parsed!");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * Parses routes (vehicles and routes)
     *
     * @param routesFile .rou.xml file
     * @return map vehicleID -> it's route (list of edge IDs)
     */
    public HashMap<Integer, List<String>> parseRoutes(File routesFile) {
        HashMap<Integer, List<String>> plans = new HashMap<Integer, List<String>>();
        initialPositions = new HashMap<Integer, Point2f>();
        departures = new HashMap<Integer, Float>();

        logger.info("PARSING ROUTES");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(routesFile);

            NodeList edgeNodeList = doc.getElementsByTagName("vehicle");
            for (int temp = 0; temp < edgeNodeList.getLength(); temp++) {

                Node lNode = edgeNodeList.item(temp);

                if (lNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element l = (Element) lNode;
                    int id = Integer.parseInt(l.getAttribute("id"));
                    float depart = Float.valueOf(l.getAttribute("depart"));
                    departures.put(id, depart);
                    String initPosition = l.getAttribute("initialPosition");
                    if (initPosition != null && !initPosition.isEmpty()) {
                        Point2f initialPosition = parseShape(initPosition).get(0);
                        initialPositions.put(id, initialPosition);
                    }
                    Element route = (Element) l.getElementsByTagName("route").item(0);
                    ArrayList<String> plan = separateStrings(route.getAttribute("edges"));
                    plans.put(id, plan);
                }
            }
            return plans;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<String> separateStrings(String inputString) {
        StringTokenizer st = new StringTokenizer(inputString);
        ArrayList<String> ret = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            ret.add(st.nextToken());
        }
        return ret;
    }


    private void parseNetworkFromFile(File networkFile, File plainNodeFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(networkFile);
        Document plainDoc = dbFactory.newDocumentBuilder().parse(plainNodeFile);

        networkLocation = parseLocation(doc);
        laneMap = new HashMap<String, LaneImpl>();
        edgeMap = new HashMap<String, Edge>();
        parseEdgesAndLanes(doc);
        junctionMap = parseJunctions(doc, plainDoc);
        connectionList = parseConnections(doc);
    }

    private ArrayList<Connection> parseConnections(Document doc) {
        NodeList connectionNodeList = doc.getElementsByTagName("connection");
        ArrayList<Connection> connections = new ArrayList<Connection>();
        for (int temp = 0; temp < connectionNodeList.getLength(); temp++) {

            Node cNode = connectionNodeList.item(temp);

            if (cNode.getNodeType() == Node.ELEMENT_NODE) {

                Element c = (Element) cNode;

                String from = c.getAttribute("from");
                String to = c.getAttribute("to");
                String fromLane = c.getAttribute("fromLane");
                String toLane = c.getAttribute("toLane");

                Connection connection = new Connection(from, to, fromLane, toLane);
                connections.add(connection);
            }

        }
        return connections;
    }

    private HashMap<String, Junction> parseJunctions(Document doc, Document plainDoc) {
        NodeList junctionNodeList = doc.getElementsByTagName("junction");
        NodeList plainNodeList = plainDoc.getElementsByTagName("node");
        HashMap<String, Junction> junctions = new HashMap<String, Junction>();

        for (int temp = 0; temp < junctionNodeList.getLength(); temp++) {
            Node jNode = junctionNodeList.item(temp);
            Node plainNode = plainNodeList.item(temp);
            if (jNode.getNodeType() == Node.ELEMENT_NODE) {
                Element p = (Element)plainNode;
                Element j = (Element) jNode;

                double lon = Double.valueOf(p.getAttribute("x"));
                double lat = Double.valueOf(p.getAttribute("y"));
                String id = j.getAttribute("id");
                String type = j.getAttribute("type");
                float x = Float.valueOf(j.getAttribute("x"));
                float y = Float.valueOf(j.getAttribute("y"));
                Point2f center = Utils.transSUMO2Alite(x, y);
                String incLanesStr = j.getAttribute("incLanes");

                String intLanesStr = j.getAttribute("incLanes");
                String shapeStr = j.getAttribute("shape");

                NodeList requestNodeList = j.getElementsByTagName("request");
                ArrayList<Request> requestList = new ArrayList<Request>();
                for (int requestIndex = 0; requestIndex < requestNodeList.getLength(); requestIndex++) {

                    String index = j.getAttribute("index");
                    String response = j.getAttribute("response");
                    String foes = j.getAttribute("foes");
                    Request request = new Request(index, response, foes);
                    requestList.add(request);
                }

                Junction junction = new Junction(id, type, center, separateStrings(incLanesStr), separateStrings(intLanesStr), parseShape(shapeStr), requestList, lat, lon);
                junctions.put(junction.getId(), junction);
            }
        }
        return junctions;
    }

    private void parseEdgesAndLanes(Document doc) {
        NodeList edgeNodeList = doc.getElementsByTagName("edge");

        for (int temp = 0; temp < edgeNodeList.getLength(); temp++) {

            Node nNode = edgeNodeList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element e = (Element) nNode;

                String id = e.getAttribute("id");
                String from = e.getAttribute("from");
                String to = e.getAttribute("to");
                String priority = e.getAttribute("priority");
                String type = e.getAttribute("type");
                String shapeStr1 = e.getAttribute("shape");

                Edge edge = new Edge(id, from, to, priority, type, parseShape(shapeStr1));
                NodeList laneNodeList = e.getElementsByTagName("lane");
                HashMap<String, LaneImpl> lanes = parseLanes(laneNodeList);
                edge.putLanes(lanes);
                laneMap.putAll(lanes);

                edgeMap.put(edge.getId(), edge);
            }
        }
    }

    private NetworkLocation parseLocation(Document document) throws ParserConfigurationException {
        NodeList locationNodeList = document.getElementsByTagName("location");
        if (locationNodeList.getLength() > 1) {
            throw new ParserConfigurationException("File contains more than 1 location tag");
        }
        Element locationElement = (Element) locationNodeList.item(0);
        String netOffset = locationElement.getAttribute("netOffset");
        String convBoundary = locationElement.getAttribute("convBoundary");
        String origBoundary = locationElement.getAttribute("origBoundary");
        String projParameter = locationElement.getAttribute("projParameter");

        NetworkLocation location = new NetworkLocationImpl(parsePoint(netOffset), parseBoundary(convBoundary), parseBoundary(origBoundary), projParameter);

        return location;
    }


    /**
     * @param lanesNodeList nodeList of lanes
     * @return map LaneID (String) -> Lane
     */
    private HashMap<String, LaneImpl> parseLanes(NodeList lanesNodeList) {
        HashMap<String, LaneImpl> ret = new HashMap<String, LaneImpl>();
        for (int temp = 0; temp < lanesNodeList.getLength(); temp++) {

            Node lNode = lanesNodeList.item(temp);

            if (lNode.getNodeType() == Node.ELEMENT_NODE) {

                Element l = (Element) lNode;
                String laneId = l.getAttribute("id");
                int index = Integer.parseInt(l.getAttribute("index"));
                float speed = Float.valueOf(l.getAttribute("speed"));
                float length = Float.valueOf(l.getAttribute("length"));
                String shapeStr = l.getAttribute("shape");


                LaneImpl lane = new LaneImpl(laneId, index, speed, length, parseShape(shapeStr));
                ret.put(laneId, lane);
            }
        }

        return ret;
    }





    private void parseMultilevelJunctions() {
        String folderPath = Configurator.getParamString("simulator.net.folder", "nets/junction-big");
        tunnels = new ArrayList<String>();
        bridges = new ArrayList<String>();
        try {
            File tunnelsFile = Utils.getFileWithSuffix(folderPath, "." + MultilevelJunctionEdge.tunnels.toString());
            File bridgesFile = Utils.getFileWithSuffix(folderPath, "." + MultilevelJunctionEdge.bridges.toString());
            parseJunctionAndBridgesFiles(tunnelsFile, bridgesFile);
        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage());
            logger.warn("tunnels/bridges file not found, parsing osm file");
            parseOSMForTunnelsAndBridges(folderPath);
        }
    }

    private void parseJunctionAndBridgesFiles(File tunnelsFile, File bridgesFile) {

        if (!parseFileToList(tunnelsFile, tunnels) || !parseFileToList(bridgesFile, bridges)) {
            logger.warn("Parsing tunnels,bridges unsuccessful, parsing osm file");
            parseOSMForTunnelsAndBridges(tunnelsFile.getParent());
        }
    }

    private boolean parseFileToList(File file, ArrayList list) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void parseOSMForTunnelsAndBridges(String folderPath) {
        File osmFile;
        try {
            osmFile = Utils.getFileWithSuffix(folderPath, ".osm");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(osmFile);

            NodeList wayList = doc.getElementsByTagName("way");

            for (int temp = 0; temp < wayList.getLength(); temp++) {

                Node wNode = wayList.item(temp);

                if (wNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element w = (Element) wNode;
                    String id = w.getAttribute("id");

                    NodeList tagNodeList = w.getElementsByTagName("tag");

                    for (int t = 0; t < tagNodeList.getLength(); t++) {

                        Node tNode = tagNodeList.item(t);

                        if (tNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element tag = (Element) tNode;
                            String key = tag.getAttribute("k");
                            if (key.equals(MultilevelJunctionEdge.tunnels.toString())) {
                                tunnels.add(id);
                                break;
                            }
                            if (key.equals(MultilevelJunctionEdge.bridges.toString())) {
                                bridges.add(id);
                                break;
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            logger.warn("osm file not found, exception caught");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private enum MultilevelJunctionEdge {
        tunnels, bridges
    }

    private Rectangle parseBoundary(String boundaryString) {
        String[] strings = boundaryString.split(",");
        float x1 = Float.parseFloat(strings[0]);
        float y1 = Float.parseFloat(strings[1]);
        float x2 = Float.parseFloat(strings[2]);
        float y2 = Float.parseFloat(strings[3]);

        Point corner1 = new Point(x1, y1);
        Point corner2 = new Point(x2, y2);

        Rectangle rectangle = new Rectangle(corner1, corner2);

        return rectangle;
    }


    /**
     * Parse shape String to a list of Points
     *
     * @param shapeString shape points separated by spaces
     * @return list of shape points
     */
    private ArrayList<Point2f> parseShape(String shapeString) {
        StringTokenizer st = new StringTokenizer(shapeString);
        ArrayList<Point2f> shape = new ArrayList<Point2f>();
        while (st.hasMoreTokens()) {
            String p = st.nextToken();
            Point2f point = parsePoint(p);
            shape.add(point);
        }
        return shape;
    }

    private Point2f parsePoint(String pointString) {
        String[] strings = pointString.split(",");
        float x = Float.parseFloat(strings[0]);
        float y = Float.parseFloat(strings[1]);
        Point2f point = Utils.transSUMO2Alite(x, y);
        return point;
    }



}
