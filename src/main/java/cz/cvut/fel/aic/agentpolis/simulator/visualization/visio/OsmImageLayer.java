package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.alite.simulation.Simulation;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.AbstractLayer;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class OsmImageLayer extends AbstractLayer {

    private Logger LOGGER = Logger.getLogger(Simulation.class);

    private static final double WORLD_X = 20026376.39;
    private static final double WORLD_Y = 20048966.10;
    private static final String OSM_TILES_ROOT = System.getProperty("user.home")+"/.GMapCatcher";
    private static final String IMAGE_FILTER = ".png";

    class OSMTile {

        int x,y;
        int worldX, worldY;
        BufferedImage image;

        public OSMTile(int x, int y, BufferedImage image) {
            this.x = x;
            this.y = y;

            this.image = image;
        }
    }
    private final Multimap<Integer, OSMTile> OSMTiles;

    @Inject
    public OsmImageLayer() {
        Path dir = Paths.get(OSM_TILES_ROOT + "/OSM_tiles");
        if (!Files.isDirectory(dir)) {
            LOGGER.info("No OSM tile folder found.");
            OSMTiles = null;
            return;
        }

        OSMTiles = LinkedListMultimap.create();

        try {
            Files.walk(dir)
                    .filter(p -> p.toString().endsWith(IMAGE_FILTER))
                    .forEach(this::getImage);
        } catch (IOException e) {
            LOGGER.warn("Error while walking tile directory tree.");
        }
    }

    private void getImage(Path p) {
        int count = p.getNameCount();
        String zoomS = p.getName(count-3).toString();
        int zoom = Integer.parseInt(p.getName(count-3).toString());
        int x = Integer.parseInt(p.getName(count-2).toString());
        int y = Integer.parseInt(p.getFileName().toString().replaceAll(IMAGE_FILTER,""));
        try {
            BufferedImage img = ImageIO.read(p.toFile());
            OSMTiles.put(zoom, new OSMTile(x,y, img));
        } catch (IOException e) {
            LOGGER.warn("Could not load image "+p);
        }
    }

    @Override
    public void paint(Graphics2D canvas) {
        if (OSMTiles == null) return;

        //Dimension dim = Vis.getDrawingDimension();
        int zoomLevel = 16;
        //Rectangle2D drawingRectangle = new Rectangle(dim); //pixel
        //double invX = Vis.transInvX((int) drawingRectangle.getX());// pixel -> world
        //double invY = Vis.transInvY((int) drawingRectangle.getY());
        for (OSMTile t : OSMTiles.get(zoomLevel)) {
            int tileOrigX = Vis.transX(t.x*(WORLD_X*2d/(1<<zoomLevel))-WORLD_X);
            int tileOrigY = Vis.transY(-t.y*(WORLD_Y*2d/(1<<zoomLevel))+WORLD_Y);
            int tileZoomW = Vis.transW(WORLD_X*2d/(1<<zoomLevel));
            int tileZoomH = Vis.transH(WORLD_Y*2d/(1<<zoomLevel));
            canvas.drawImage(t.image, tileOrigX,tileOrigY,
                    tileZoomW,tileZoomH, null);
        }
    }
}

