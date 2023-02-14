/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import core.GameWindow;
import core.ResourceManager;
import core.gameobject.BaseObject;
import core.map.Map;
import core.map.Tileset;
import core.phisics.Vector2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

/**
 *
 * @author panca
 */
public class MarioGame extends GameWindow{

    Pulador mario;
    
    Map map;
    public MarioGame()
    {
        super(240,240, 800,600,60.0);
        
        map = Map.load(ResourceManager.createResourceString("map_verde3.map", ResourceManager.RESSOURCE_TYPE_MAP));
        
        mario = (Pulador) map.getObjectByName("mario");
        map.getObjects().remove(mario);
        map.addObject(mario);
        mario.setMapReference(map);
        this.setMapReference(map);
        
    }
    
    @Override
    public void update(double t) {
        //mario.update(t);
        map.update(t);
        map.moveWindow(mario.getPosition());
        
        
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        map.render(g, -1);
        //mario.render((Graphics2D) g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            mario.setFacingSide(BaseObject.FACING_RIGHT);
            mario.setAndando(true);
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            mario.setFacingSide(BaseObject.FACING_LEFT);
            mario.setAndando(true);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE)
            mario.setPulando(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)
            mario.setAndando(false);
    }
    
}
