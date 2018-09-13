package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util;


import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.XMLReader;

import javax.vecmath.Point3f;
import java.util.*;
import java.io.*;


/**
 * Created by david on 10.3.15.
 */
public class FileUtil {
    private FileUtil(){}
    public void writeNumberOfCarsInSimulation(LinkedList<Pair<Float, Integer>> numberOfCarsInSimulation) {
        String file_name = "numberOfCars.m";
        try {
            FileWriter fstream = new FileWriter(file_name);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("time = [");
            for (Pair<Float, Integer> p : numberOfCarsInSimulation) {
                Float element = p.getKey();
                out.write(element + " ");
            }
            out.write("]");
            out.newLine();
            out.write("number = [");
            for (Pair<Float, Integer> p : numberOfCarsInSimulation) {
                Integer element = p.getValue();
                out.write(element + " ");
            }
            out.write("]");
            out.newLine();
            out.write("figure;");
            out.newLine();
            out.write("hold on;");
            out.newLine();
            out.write("plot(time,number);");
            out.newLine();
            out.write("title('Number of cars in simulation in time')");
            out.newLine();
            out.write("xlabel('Simulation time')");
            out.newLine();
            out.write("ylabel('Number of cars in simulation')");
            out.newLine();
            out.write("hold off;");
            out.newLine();
            out.close();
        }
        catch (Exception e) { // TODO Improve this
            System.out.println(e.getMessage());
        }
    }
    public void writeToFile(Map<Integer, Queue<Pair<Long,Float>>> distances, int speedOrDistances)
    {
        String file_name;
        if(speedOrDistances == 0) {
            file_name ="distances.m";
        }
        else
        {
            file_name ="speeds.m";
        }
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        try {
            FileWriter fstream = new FileWriter(file_name);
            BufferedWriter out = new BufferedWriter(fstream);
            int index = 0;
            String addstring = "";
            Random rand = new Random();
            for (Map.Entry<Integer, Queue<Pair<Long,Float>>> entry : distances.entrySet())
            {
                if(index > 25)
                {
                    addstring +="A";
                    index = 0;
                }
                out.write(addstring + alphabet[index] + " = [");
                Queue<Pair<Long,Float>> way =  entry.getValue();
                Iterator<Pair<Long,Float>> itr = way.iterator();
                while (itr.hasNext()) {
                    Float element = (Float) itr.next().getValue();
                    out.write(element + " ");
                }
                out.write("]");
                out.newLine();
                out.write(addstring + alphabet[index] + "time" + " = [");
                itr = way.iterator();
                while (itr.hasNext()) {
                    Float element = itr.next().getKey()/1000f;
                    out.write(element + " ");
                }
                out.write("]");
                index++;
                out.newLine();
            }
            out.write("figure;");
            out.newLine();
            out.write("hold on;");
            out.newLine();
            index = 0;
            addstring = "";
            for (Map.Entry<Integer, Queue<Pair<Long,Float>>> entry : distances.entrySet())
            {
                if(index > 25)
                {
                    addstring +="A";
                    index = 0;
                }
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                out.write("plot("+addstring + alphabet[index] + "time" + "," + addstring + alphabet[index++] + ",'Color'," +"[" + r +" " + g + " " + b + "])");
                out.newLine();
            }

            if(speedOrDistances == 0) {
                out.write("title('Car distances to the junction')");
                out.newLine();
                out.write("ylabel('distance')");
            }
            else
            {
                out.write("title('Car speeds')");
                out.newLine();
                out.write("ylabel('speed')");
            }
            out.newLine();
            out.write("xlabel('Simulation time')");
            out.newLine();
            out.write("hold off;");
            out.close();
            System.out.println("File created successfully.");

        } catch (Exception e) { // TODO Improve this
            System.out.println(e.getMessage());
        }
    }
    public void writeGraphOfArrivals(Map<Integer, Pair<Float,Float>>  graphOfArrivals,Map<List<String>,Pair<Integer,Float>> journeys)
    {
//        String file_name = "graphOfArrivals.m";
//        final XMLReader reader = new XMLReader();
//        reader.read(Configurator.getParamString("simulator.net.folder","notDefined"));
//        //reader.getRoutes().get(obj.getKey()
//        HashMap<Integer,List<String>> vehRoutes = new LinkedHashMap<Integer, List<String>>();
//        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
//        for (Map.Entry<Integer, Pair<Float, Float>> obj : graphOfArrivals.entrySet())
//        {
//            vehRoutes.put(obj.getKey(),reader.getRoutes().get(obj.getKey()));
//        }
//
//        try {
//            FileWriter fstream = new FileWriter(file_name);
//            BufferedWriter out = new BufferedWriter(fstream);
//            int index = 0;
//            String addstring = "";
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet())
//            {
//                if(index > 25)
//                {
//                    addstring +="A";
//                    index = 0;
//                }
//                out.write(addstring + alphabet[index] + " = [");
//                for (Map.Entry<Integer, Pair<Float, Float>> obje : graphOfArrivals.entrySet()) {
//                    if (obje.getValue().getValue() != null) {
//                        if(vehRoutes.get(obje.getKey()).equals(obj.getKey())) {
//                            Float element = (Float) obje.getValue().getKey();
//                            out.write(element + " ");
//                        }
//                    }
//                }
//                out.write("]");
//                out.newLine();
//                out.write(addstring + alphabet[index] + "time" + " = [");
//                for (Map.Entry<Integer, Pair<Float, Float>> obje : graphOfArrivals.entrySet()) {
//                    if (obje.getValue().getValue() != null) {
//                        if(vehRoutes.get(obje.getKey()).equals(obj.getKey())) {
//                            Float element = obje.getValue().getValue() - obje.getValue().getKey();
//                            out.write(element + " ");
//                        }
//                    }
//                }
//                out.write("]");
//                out.newLine();
//                index++;
//            }
///*            index = 0;
//            addstring = "";
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                if(index > 25)
//                {
//                    addstring +="A";
//                    index = 0;
//                }
//                out.write("p"+addstring + alphabet[index]+" = polyfit("+addstring + alphabet[index]+","+addstring + alphabet[index] + "time"+",4);");
//                out.newLine();
//                index++;
//            }
//            index = 0;
//            addstring = "";
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                if(index > 25)
//                {
//                    addstring +="A";
//                    index = 0;
//                }
//                out.write("x1"+addstring + alphabet[index]+" = linspace(0,"+addstring + alphabet[index]+"(length("+addstring + alphabet[index] +")));");
//                out.newLine();
//                index++;
//            }
//            index = 0;
//            addstring = "";
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                if (index > 25) {
//                    addstring += "A";
//                    index = 0;
//                }
//                out.write("y1"+addstring + alphabet[index]+" = polyval("+"x1"+addstring + alphabet[index]+","+"x1"+addstring + alphabet[index]+");");
//                out.newLine();
//                index++;
//            }
//            out.write("figure;");
//            out.newLine();
//            out.write("hold on;");
//            out.newLine();
//            index = 0;
//            addstring = "";
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                if (index > 25) {
//                    addstring += "A";
//                    index = 0;
//                }
//                out.write("plot("+addstring + alphabet[index]+","+addstring + alphabet[index] + "time"+",'o');");
//                out.newLine();
//                index++;
//            }
//            index = 0;
//            addstring = "";
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                if (index > 25) {
//                    addstring += "A";
//                    index = 0;
//                }
//                out.write("plot(x1"+addstring + alphabet[index]+",y1"+addstring + alphabet[index]+");");
//                out.newLine();
//                index++;
//            }
//            */
//            out.write("figure;");
//            out.newLine();
//            out.write("hold on;");
//            out.newLine();
//            index = 0;
//            addstring = "";
//            Random rand = new Random();
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                if (index > 25) {
//                    addstring += "A";
//                    index = 0;
//                }
//                float r = rand.nextFloat();
//                float g = rand.nextFloat();
//                float b = rand.nextFloat();
//                out.write("plot("+addstring + alphabet[index]+","+addstring + alphabet[index] + "time"+",'x'"+",'Color'," +"[" + r +" " + g + " " + b + "])");
//                out.newLine();
//                index++;
//            }
//            out.write("legend(");
//            int ppp = 0;
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : journeys.entrySet()) {
//                out.write("'"+ obj.getKey().toString()+"'");
//                if(ppp<journeys.size()-1)
//                {
//                    out.write(",");
//                }
//                ppp++;
//            }
//            out.write(");");
//            out.newLine();
//            out.write("hold off;");
//            out.close();
//            System.out.println("Graph of arrivals created");
//        }
//        catch (Exception e) { // TODO Improve this
//            System.out.println(e.getMessage());
//        }
    }
    public Map<List<String>,Pair<Integer,Float>> writeReport(int numberOfCollisions,float numberOfVehiclesPerSecond,long timeOfsimulation,
                            Map<Integer,Float> avspeed, Map<Integer, Pair<Point3f,Float>> lenghtOfjourney,
                            LinkedList<Float> timesOfArrival, LinkedList<Integer> computationTime)
    {
//        String file_name = "report.txt";
//        try {
//            FileWriter fstream = new FileWriter(file_name);
//            BufferedWriter out = new BufferedWriter(fstream);
//            out.write("Name of the scenario is: " + Configurator.getParamString("simulator.net.folder", "nets/junction-big/"));
//            out.newLine();
//            out.write("Number of collisions is :" + numberOfCollisions);
//            out.newLine();
//            out.write("Number of vehicles travelling throw junction per seccond is: " + numberOfVehiclesPerSecond);
//            out.newLine();
//            out.write("Time of simulation was: " + timeOfsimulation / 1000f);
//            out.newLine();
//            out.write("Maximum number of cars in simulation are: " + Configurator.getParamInt("highway.dashboard.numberOfCarsInSimulation", 40));
//            out.newLine();
//            out.write("Sumo simulation: " + Configurator.getParamBool("highway.dashboard.sumoSimulation", true));
//            out.newLine();
//            out.write("Distance to activate narowing mod: " + Configurator.getParamInt("highway.safeDistanceAgent.distanceToActivateNM", 400));
//            out.newLine();
//            out.write("Safety reserve distance: " + Configurator.getParamDouble("highway.safeDistanceAgent.safetyReserveDistance", 4.0)
//            );
//            out.newLine();
//            if(Configurator.getParamList("highway.dashboard.simulatorsToRun", String.class).isEmpty())
//            {
//                out.write("Simulator used: LocalSimulator");
//            }
//            else {
//                out.write("Simulator used: " + Configurator.getParamList("highway.dashboard.simulatorsToRun", String.class).get(0));
//            }
//            out.newLine();
//          /*  out.write("Avarage speed is: "+ (distance/timeOfsimulation)*3.6 + " km/h");
//            out.newLine();*/
//            final XMLReader reader = new XMLReader();
//            File flowsFile = Utils.getFileWithSuffix(Configurator.getParamString("simulator.net.folder", "nets/junction-big/"), ".flows.xml");
//            Scanner sc = new Scanner(flowsFile);
//            while (sc.hasNextLine()) {
//                String s = sc.nextLine();
//                out.write(s);
//                out.newLine();
//            }
//            sc.close();
//            Map<List<String>,Pair<Integer,Float>> averageJourneySpeed = new HashMap<List<String>, Pair<Integer,Float>>();
//            for (Map.Entry<Integer, Float> obj : avspeed.entrySet())
//            {
//                Pair<Integer, Float> integerFloatPair = averageJourneySpeed.get(reader.getRoutes().get(obj.getKey()));
//                Float newSpeed;
//                Integer newNumber;
//                if(integerFloatPair == null)
//                {
//                    newSpeed = obj.getValue();
//                    newNumber = 1;
//                }
//                else
//                {
//                    Float originalavspeed = integerFloatPair.getValue();
//                    Integer originalnumer = integerFloatPair.getKey();
//                    newSpeed = originalavspeed + obj.getValue();
//                    newNumber = originalnumer+1;
//                }
//                averageJourneySpeed.put(reader.getRoutes().get(obj.getKey()),new Pair<Integer, Float>(newNumber,newSpeed));
//                out.write("id: " + obj.getKey() + " route: " + reader.getRoutes().get(obj.getKey())
//                        + " avspeed: " + obj.getValue() + " distance traveled: " +
//                        lenghtOfjourney.get(obj.getKey()).getValue());
//                out.newLine();
//            }
//            out.newLine();
//            for (Map.Entry<List<String>, Pair<Integer,Float>> obj : averageJourneySpeed.entrySet())
//            {
//                Float spped = obj.getValue().getValue()/obj.getValue().getKey();
//
//                out.write("Journey: " + obj.getKey() + " avspeed: " + spped);
//                out.newLine();
//            }
//            out.write("Times of arrival in seconds:");
//            out.newLine();
//            out.write(timesOfArrival.toString());
//            out.newLine();
//            Integer summ=0;
//            for(Integer e : computationTime)
//            {
//                summ+=e;
//            }
//            Float res = (float)summ/computationTime.size();
//            out.write(res.toString() + " miliseconds");
//            out.newLine();
//            out.close();
//
//            System.out.println("Report created successfully.");
//            return averageJourneySpeed;
//        }
//     catch (Exception e) { // TODO Improve this
//        System.out.println(e.getMessage());
//         return null;
//        }
        return null;
    }
    public static FileUtil getInstance() {
        return FileUtilHolder.INSTANCE;
    }



    private static class FileUtilHolder {
        private static final FileUtil INSTANCE = new FileUtil();
    }
}

