/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.agentpolis.simulator.visualization.visio.VisioUtils;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.AbstractLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 * @param <E>
 */
public abstract class EntityLayer<E extends AgentPolisEntity> extends AbstractLayer{
    
    private static final Double TEXT_MARGIN_BOTTOM = 5.0;
    
    private static final Color TEXT_BACKGROUND_COLOR = Color.WHITE;
    
    
    
    private final EntityStorage<E> entityStorage;
    
    private HashMap<Point2d,ArrayList<E>> entityMap;
    
    
    
    protected PositionUtil positionUtil;
    
    protected Dimension dim;

    public EntityLayer(EntityStorage<E> entityStorage) {
        this.entityStorage = entityStorage;
    }
    
    @Inject
    public void init(PositionUtil positionUtil){
        this.positionUtil = positionUtil;
    }
    
    
    
    @Override
    public void paint(Graphics2D canvas) {
        dim = Vis.getDrawingDimension();

        EntityStorage<E>.EntityIterator entityIterator = entityStorage.new EntityIterator();
        E entity;
        entityMap = new HashMap<>();
        while((entity = entityIterator.getNextEntity()) != null){
            if(skipDrawing(entity)){
                continue;
            }
            Point2d entityPosition = getEntityPosition(entity);
			if(!entityMap.containsKey(entityPosition)){
                entityMap.put(entityPosition, new ArrayList<>());
            }
            entityMap.get(entityPosition).add(entity);
        }
        
        for (Map.Entry<Point2d, ArrayList<E>> entry : entityMap.entrySet()) {
            Point2d entityPosition = entry.getKey();
            ArrayList<E> entities = entry.getValue();
            drawEntities(entities, entityPosition, canvas, dim);
        }
    }

    protected abstract Point2d getEntityPosition(E entity);
    
    protected void drawEntities(ArrayList<E> entities, Point2d agentPosition, Graphics2D canvas, Dimension dim) {
        Color color = getEntityDrawColor(entities.get(0));
        canvas.setColor(color);
        int radius = getEntityDrawRadius();
		int width = Vis.transW(radius * 2);

        int x1 = (int) (agentPosition.getX() - radius);
        int y1 = (int) (agentPosition.getY() - radius);
        int x2 = (int) (agentPosition.getX() + radius);
        int y2 = (int) (agentPosition.getY() + radius);
        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            canvas.fillOval(x1, y1, width, width);
            
        }
        
        if(entities.size() > 1){
            VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()), 
                new Point((int) (x1 - TEXT_MARGIN_BOTTOM), y1 - (y2 - y1) / 2), color, 
                TEXT_BACKGROUND_COLOR);
        }
    }

    protected abstract Color getEntityDrawColor(E entity);

    protected abstract int getEntityDrawRadius();

    protected boolean skipDrawing(E entity) {
        return false;
    }
    
}
