/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualeditors;

import core.map.Map;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author panca
 * The code is mine, I want to call it THING.
 */
public class PanelWithThings extends JPanel
{
    private Vector<Thing> things;
    private Thing selectedThing;
    private Map map;
    public PanelWithThings()
    {
        super();
        things = new Vector<Thing>();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                for(int i = 0; i < things.size(); i++)
                {
                    Thing t = things.get(i);
                    if(t.isClicked(p))
                    {
                        if(e.getButton() == MouseEvent.BUTTON3)
                        {
                            int r = JOptionPane.showConfirmDialog(PanelWithThings.this, "Remove this object? its not possible to undo this operation!");
                            if(r == JOptionPane.OK_OPTION)
                                things.remove(t);
                            updateUI();
                        }
                        else
                            selectedThing = t;
                    }
                        
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                selectedThing = null;
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectedThing != null)
                {
                    Point p = e.getPoint();
                    selectedThing.x = p.x;
                    selectedThing.y = p.y;
                    
                }
                updateUI();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
            }
        });
    }
    
    public Vector<Thing> getThings()
    {
        return things;
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        if(map!= null)
            map.previewMap(g, 0);
        for(Thing t:things)
        {
            g.drawImage(t.img, t.x, t.y, null);
        }
    }
    
    
    public void setMap(Map map){
        this.map = map;
    }
    
    public void addThing(Image img, int x, int y, boolean clickable, Object obj)
    {
        Thing t = new Thing(img, x,  y, clickable, obj);
        things.add(t);
        
        updateUI();
    }
    
   
    
    public class Thing
    {
        private Image img;
        private Object thing;
        private int x, y;
        private boolean clickable;
        public Thing(Image img, int x, int y, boolean clickable, Object thing)
        {
            this.img = img;
            this.x = x;
            this.y = y;
            this.clickable = clickable;
            this.thing = thing;
        }
        public boolean isClicked(Point p)
        {
            if(p.x > x && p.x < x + img.getWidth(null))
            {
                if(p.y >= y && p.y <= p.y + img.getHeight(null))
                {
                    return true;
                }
            }
            return false;
        }
        public Object getThing()
        {
            return thing;
        }

        

        public void setImg(Image img) {
            this.img = img;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean isClickable() {
            return clickable;
        }

        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }
        
        
    }
}
