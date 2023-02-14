/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tester.animation;

import core.GameWindow;
import core.ResourceManager;
import core.gameobject.Animation;
import core.map.Map;
import core.map.Tileset;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * This tester loads an empty map swich comes with the framework.
 * @author panca
 */
public class TestAnimationGame extends GameWindow 
{
    private AnimTestObject obj;
    Map map;
    public TestAnimationGame(int windowWidth, int windowHeight, int resolutionWidth, int resolutionHeight, double framesPerSecond, Animation anim, double x, double y) {
        super(windowWidth, windowHeight, resolutionWidth, resolutionHeight, framesPerSecond);
        obj = new AnimTestObject(anim, x, y);
        Image img = null;
        try{
            
            img = javax.imageio.ImageIO.read(new File(getClass().getClassLoader().getResource("core/resources/do_not_remove.png").toURI()));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        Tileset t = new Tileset(img,64);
        map = new Map(t, 40, 20, 640,480, 2);
        map.init();
        //map = Map.load(ResourceManager.createResourceString("do_not_remove.map", ResourceManager.RESSOURCE_TYPE_MAP));
        obj.setMapReference(map);
        this.setMapReference(map);
    }

    @Override
    public void update(double t) {
        map.render(pincel, 1);
        obj.update(t);
    }

    @Override
    public void render(Graphics2D g) {
        obj.render( g);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
