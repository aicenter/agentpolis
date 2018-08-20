package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util;

import org.apache.log4j.Logger;

import javax.vecmath.Point2f;
import java.io.*;
import java.net.URL;


public class Utils {

    private final static Logger logger = Logger.getLogger(Utils.class);


    public static URL getResourceUrl(String resourcePath) throws FileNotFoundException {

        logger.trace("input path: " + resourcePath);
        URL url = Utils.class.getClassLoader().getResource(resourcePath);
        logger.trace("output url: " + url);

        if (url == null) {
            throw new FileNotFoundException("File in: " + resourcePath + " not found in the resources");
        }

        return url;
    }

    public static File getFileWithSuffix(String resourceFolderPath, String suffix) throws FileNotFoundException {
        File folder = getResourceFile(resourceFolderPath);
        File file = null;
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File f : files) {
                if (f.getName().endsWith(suffix)) {
                    if (file != null)
                        throw new FileNotFoundException("Found two or more files with the same suffix.");
                    file = f;
                }
            }
        }
        if(file == null) throw new FileNotFoundException("File with \"" + suffix + "\" suffix in " + resourceFolderPath + " not found");
        return file;
    }

    // this might get more complex regarding OpenDS config
    public static int name2ID(String name) {
        return Integer.parseInt(name);
    }

    public static File getResourceFile(String path) throws FileNotFoundException {
        String url = "data/" + path;//getResourceUrl(path);
        File file = new File(url);
        if (file.exists()) {
            return file;
        } else {
            throw new FileNotFoundException("File: " + url);
        }
    }

    /**
     * Transformation of SUMO to ALite coordinates
     *
     * @param x x in SUMO coordinates
     * @param y y in SUMO coordinates
     * @return coordinates in Alite coordinates (x,-y)
     */
    public static Point2f transSUMO2Alite(float x, float y) {
        return new Point2f(x, -y);
    }


}
