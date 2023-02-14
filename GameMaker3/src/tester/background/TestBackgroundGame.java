package tester.background;

import core.GameWindow;
import core.map.Background;
import core.map.BackgroundLayer;
import core.map.Map;
import core.map.Tileset;
import core.phisics.Vector2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class TestBackgroundGame extends GameWindow
{

   private Map mapReference;
    Vector2D pos;
    public TestBackgroundGame( int resolutionWidth, int resolutionHeight, double framesPerSecond, Map mapReference) {
        super(mapReference.getWindowWidth(), mapReference.getWindowHeight(), resolutionWidth, resolutionHeight, framesPerSecond);
        pos = new Vector2D(20,20);
        this.mapReference = mapReference;
        this.setMapReference(mapReference);
    }

    @Override
    public void update(double t) {
        
        mapReference.moveWindow(pos);
        mapReference.update(t);
    }

    @Override
    public void render(Graphics2D g) {
        mapReference.render(g, -1);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            pos.x -= 5;
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
            pos.x += 5;
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            this.stopRunning();
            
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyTyped(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
