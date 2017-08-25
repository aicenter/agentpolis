package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

public class OsmImageLayer extends AbstractLayer {

    private Logger LOGGER = Logger.getLogger(Simulation.class);

    private static final double WORLD_X = 4.007501984E7;
    private static final double WORLD_Y = 4.007501668E7;
    private static final String OSM_TILES_ROOT = System.getProperty("user.home")+"/.GMapCatcher";

    private final Path dir = Paths.get(OSM_TILES_ROOT + "/OSM_tiles");

    private final HashMap<OsmKey, BufferedImage> OSMTiles;
    private int zoomLevel;
    private int minIDX, minIDY, maxIDX, maxIDY;
    private int zoomW;
    private int zoomH;

    @Inject
    public OsmImageLayer() {
        if (!Files.isDirectory(dir)) {
            LOGGER.info("No OSM tile folder found.");
            OSMTiles = null;
            return;
        }

        OSMTiles = new HashMap<>();
    }

    private BufferedImage getTile(int zoom, int x, int y) {
        OsmKey osmKey = new OsmKey(zoom, x, y);
        if (OSMTiles.containsKey(osmKey)) return OSMTiles.get(osmKey);
        Path p = dir.resolve(Paths.get(Integer.toString(zoom),Integer.toString(x),Integer.toString(y)+".png"));
        if (Files.isRegularFile(p) && Files.isReadable(p)) try {
            BufferedImage img = ImageIO.read(p.toFile());
            OSMTiles.put(osmKey, img);
            return img;
        } catch (IOException e) {
            LOGGER.warn("Could not open file "+p.toString());
        }
        OSMTiles.put(osmKey,null);
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
            if (zoomLevel > 17) zoomLevel=17;
            else if (zoomLevel < 0) zoomLevel=0;
            zoomW = Vis.transW(WORLD_X/(1<<zoomLevel));
            zoomH = Vis.transH(WORLD_Y/(1<<zoomLevel));
        }

        // calculate tileID bounds for current zoomLevel and screen dimensions
        minIDX = worldToSlippyX(Vis.transInvX(0));
        minIDY = worldToSlippyY(Vis.transInvY(0));
        maxIDX = worldToSlippyX(Vis.transInvX((int) drawingRectangle.getMaxX())) + 1;
        maxIDY = worldToSlippyY(Vis.transInvY((int) drawingRectangle.getMaxY())) + 1;

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

