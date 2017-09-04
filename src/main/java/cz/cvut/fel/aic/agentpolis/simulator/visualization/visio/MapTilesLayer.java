package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapTilesLayer extends AbstractLayer {

    public static class DownloadTask implements Runnable {

        private URL url;
        private File file;

        public DownloadTask(URL url, File file) {
            this.url = url;
            this.file = file;
        }

        @Override
        public void run() {
            file.getParentFile().mkdirs();
            try {
                downloadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void downloadFile() throws IOException {
            URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Agentpolis");

            InputStream inputStream = urlConnection.getInputStream();
            ReadableByteChannel rbc = Channels.newChannel(inputStream);
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    public static class DownloadManager {

        public static final int MAX_CONCURRENT_DOWNLOADS = 2;

        private ExecutorService exec;
        @Inject
        public DownloadManager() {
            this.exec = Executors.newFixedThreadPool(MAX_CONCURRENT_DOWNLOADS);
        }

        public Future<?> submit(DownloadTask downloadTask) {
            return exec.submit(downloadTask);
        }
    }

    private Logger LOGGER = Logger.getLogger(Simulation.class);

    private static final double WORLD_X = 4.007501984E7;
    private static final double WORLD_Y = 4.007501668E7;
    private final String osmTileServer;

    private final Path dir;

    private final HashMap<OsmKey, BufferedImage> OSMTiles;
    private final HashMap<OsmKey, Future<?>> OSMDownloads;
    private int zoomLevel;
    private int minIDX, minIDY, maxIDX, maxIDY;
    private int zoomW;
    private int zoomH;

    private DownloadManager downloadManager;

    @Inject
    public MapTilesLayer(Config config) {
        this.downloadManager = new DownloadManager();
        this.dir = Paths.get(config.pathToMapTiles);
        this.osmTileServer = config.osmTileServer;
        if (!Files.isDirectory(dir)) {
            LOGGER.info("Cannot access the directory with map tiles: " + config.pathToMapTiles);
            OSMTiles = null;
            OSMDownloads = null;
            return;
        }
        OSMTiles = new HashMap<>();
        OSMDownloads = new HashMap<>();
    }

    private BufferedImage getTile(int zoom, int x, int y) {
        // make key
        OsmKey osmKey = new OsmKey(zoom, x, y);

        // check if tile is in memory
        if (OSMTiles.containsKey(osmKey)) return OSMTiles.get(osmKey);

        // check if tile is being downloaded, return if it is
        if (OSMDownloads.containsKey(osmKey)) {
            if (!OSMDownloads.get(osmKey).isDone()) {
                return null;
            }
            else OSMDownloads.remove(osmKey);
        }

        // check if file exists on disk, cache and return it
        Path p = dir.resolve(Paths.get(Integer.toString(zoom),Integer.toString(x),Integer.toString(y)+".png"));
        if (Files.isRegularFile(p) && Files.isReadable(p)) try {
            BufferedImage img = ImageIO.read(p.toFile());
            OSMTiles.put(osmKey, img);
            return img;
        } catch (IOException e) {
            LOGGER.warn("Could not open local file "+p.toString());
        }

        // download tile if not found on disk
        if (!OSMDownloads.containsKey(osmKey)) {
            String directoryUrl = "/"+Integer.toString(zoom)+"/"+Integer.toString(x)+"/"+Integer.toString(y)+".png";
            try {
                URL downloadUrl = new URL("http://" + osmTileServer + directoryUrl);
                DownloadTask downloadTask = new DownloadTask(downloadUrl,p.toFile());
                Future<?> future = downloadManager.submit(downloadTask);
                OSMDownloads.put(osmKey,future);
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    @Override
    public void paint(Graphics2D canvas) {
        if (OSMTiles == null) return;

        // get screen dimensions
        Dimension dim = Vis.getDrawingDimension();
        Rectangle drawingRectangle = new Rectangle(dim);

        // calculate zoomed image sizes and level modifier
        zoomW = Vis.transW(WORLD_X / (1 << zoomLevel));
        zoomH = Vis.transH(WORLD_Y / (1 << zoomLevel));
        int zoomLevelModifier = (int) log2(zoomW/256d);
        // modify and bind zoomLevel and recalculate zoomed image sizes
        if (zoomLevelModifier != 0) {
            zoomLevel += zoomLevelModifier;
            if (zoomLevel > 19) zoomLevel=19;
            else if (zoomLevel < 0) zoomLevel=0;
            zoomW = Vis.transW(WORLD_X/(1<<zoomLevel));
            zoomH = Vis.transH(WORLD_Y/(1<<zoomLevel));
        }

        // calculate tileID bounds for current zoomLevel and screen dimensions
        minIDX = Math.max(0,worldToSlippyX(Vis.transInvX(0)));
        minIDY = Math.max(0,worldToSlippyY(Vis.transInvY(0)));
        maxIDX = Math.min((1<<zoomLevel)-1,worldToSlippyX(Vis.transInvX((int) drawingRectangle.getMaxX())) + 1);
        maxIDY = Math.min((1<<zoomLevel)-1,worldToSlippyY(Vis.transInvY((int) drawingRectangle.getMaxY())) + 1);

        // finally draw tiles
        drawImages(canvas);
    }

    private void drawImages(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (int i = minIDX; i < maxIDX; i++) {
            for (int j = minIDY; j < maxIDY; j++) {
                BufferedImage bufferedImage = getTile(zoomLevel,i,j);
                if (bufferedImage != null) graphics.drawImage(bufferedImage,
                        Vis.transX(SlippyToWorldX(i)),Vis.transY(SlippyToWorldY(j)),
                        zoomW+1,zoomH+1,null);
            }
        }
    }

    private int worldToSlippyX(double d) {
        return (int)((d+WORLD_X/2)*(1<<zoomLevel)/WORLD_X);
    }

    private int worldToSlippyY(double d) { return (int)(-1*(d-WORLD_Y/2)*(1<<zoomLevel)/WORLD_Y); }

    private double SlippyToWorldX(int i) {
        return i*(WORLD_X/(1<<zoomLevel))-WORLD_X/2;
    }

    private double SlippyToWorldY(int i) { return -i*(WORLD_Y/(1<<zoomLevel))+WORLD_Y/2; }

    private static double log2(double num) { return (Math.log(num)/Math.log(2)); }

    class OsmKey {

        private final int zoom, x, y;

        OsmKey(int zoom, int x, int y) {
            this.zoom = zoom;
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OsmKey osmKey = (OsmKey) o;

            return zoom == osmKey.zoom && x == osmKey.x && y == osmKey.y;
        }

        @Override
        public int hashCode() {
            int result = zoom;
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }
}

