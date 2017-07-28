package cz.cvut.fel.aic.agentpolis.utils;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * The util provides for read resource from JAR file
 * 
 * @author Zbynek Moler
 *
 */
public final class ResourceReader {

    private ResourceReader() {
    }

    public static final InputStream getResourceAsStream(String relativePath) {
	return ResourceReader.class.getResourceAsStream(relativePath);
    }

    public static final URL getPathToResource(String relativePath) {
	return ResourceReader.class.getResource(relativePath);
    }

}
