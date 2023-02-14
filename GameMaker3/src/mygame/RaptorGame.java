/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import core.GameWindow;
import core.ResourceManager;
import core.map.Map;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


/**
 *
 * @author Panca
 */
public class RaptorGame extends GameWindow{

    Raptor raptor;
    Map map;
    public RaptorGame() 
    {
        super(800,600,60.0);
        
        //raptor = new Raptor();
        map = Map.load(ResourceManager.createResourceString("jungle_map5.map", ResourceManager.RESSOURCE_TYPE_MAP));
        super.setMapReference(map);
        raptor = (Raptor) map.getObjectByName("raptor");
        
    }

    @Override
    public void update(double t) {
        map.update(t);
        map.moveWindow(raptor.getPosition());
        
    }

    @Override
    public void render(Graphics2D g) {
        map.render(g, -1);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            this.stopRunning();
            System.exit(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            raptor.setFacingSide(Raptor.FACING_LEFT);
            raptor.setWalking(true);
        }
        else if(e.getKeyCode() == KeyEvent.VK_D)
        {
            raptor.setFacingSide(Raptor.FACING_RIGHT);
            raptor.setWalking(true);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            raptor.jump();
        }
        else if(e.getKeyCode() == KeyEvent.VK_L)
        {
            raptor.bite();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_A ||e.getKeyCode() == KeyEvent.VK_D)
        {
            raptor.setWalking(false);
        }
    }
    
}
