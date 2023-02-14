/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualeditors.objeditor;

import core.phisics.Vector2D;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;



/**
 *
 * @author panca
 */
public class ResizableLabel extends JLabel
{
    private Vector2D upperLeftPoint, lowerRightPoint;
    private final int BOX_SIZE = 4; //4 px
    private boolean upperLeftSelected, lowerRightSelected, centerSelected;
    private Container owner;
    int inX, inY;
    private Color color;
    private boolean active;
    public ResizableLabel(int x, int y, int w, int h, Container owner, Color color)
    {
        super();
        active = true;
        this.color = color;
        this.owner = owner;
        upperLeftPoint = new Vector2D(x,y);
        lowerRightPoint = new Vector2D(x+w, y+h);
        setSize(w, h);
        setLocation(x, y);
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //mousePressed(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1)
                {
                    int mx = e.getX();
                    int my = e.getY();
                    
                    if(mx < BOX_SIZE)
                    {
                        if(my < BOX_SIZE)
                        {
                            //upper left box
                            ResizableLabel.this.upperLeftSelected = true;
                        }
                    }
                    else if (mx > ResizableLabel.this.getWidth() - BOX_SIZE)
                    {
                        ResizableLabel.this.lowerRightSelected = true;
                    }
                    else 
                    {
                        centerSelected = true;
                        inX = e.getX();
                        inY = e.getY();
                        
                    }
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {
                    ResizableLabel.this.active = false;
                }
                
                updateUI();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                upperLeftSelected = false;
                lowerRightSelected = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                if(e.getButton() == MouseEvent.BUTTON3)
                {
                    ResizableLabel.this.active = false;
                    ResizableLabel.this.updateUI();
                    return;
                }
                if(upperLeftSelected)
                {
                    ResizableLabel.this.upperLeftPoint.setX(e.getX());
                    ResizableLabel.this.upperLeftPoint.setY(e.getY());
                    setSize((int)lowerRightPoint.getX(), (int)lowerRightPoint.getY());
                    
                }
                else if(lowerRightSelected)
                {
                    ResizableLabel.this.lowerRightPoint.setX(e.getX());
                    ResizableLabel.this.lowerRightPoint.setY(e.getY());
                    setSize((int)lowerRightPoint.getX(), (int)lowerRightPoint.getY());
                }
                else if(centerSelected)
                {
                    Point p = ResizableLabel.this.owner.getMousePosition();
                    
              
                    p.setLocation(p.getX()-inX, p.getY()-inY);
                    setLocation(p);
                }
                
                ResizableLabel.this.updateUI();
                ResizableLabel.this.owner.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        if(!active)
            return;
        super.paint(g);
        g.setColor(color);
        g.drawRect(0, 0,this.getWidth()-1, this.getHeight()-1);
        
        //g.fillRect(0,0,BOX_SIZE, BOX_SIZE);
        g.fillRect(this.getWidth()-1-BOX_SIZE,this.getHeight()-1-BOX_SIZE,BOX_SIZE, BOX_SIZE);
        
        g.fillRect(ResizableLabel.this.getWidth()/2 - BOX_SIZE, ResizableLabel.this.getHeight()/2 - BOX_SIZE, BOX_SIZE, BOX_SIZE);
    }
    
    public void drawTo(Graphics g)
    {
        if(!active)
            return;
        g.setColor(color);
        g.drawRect(this.getLocation().x, this.getLocation().y,this.getWidth()-1, this.getHeight()-1);
        
        //g.fillRect(0,0,BOX_SIZE, BOX_SIZE);
        g.fillRect(this.getLocation().x+this.getWidth()-1-BOX_SIZE,this.getLocation().y+this.getHeight()-1-BOX_SIZE,BOX_SIZE, BOX_SIZE);
        
        g.fillRect(this.getLocation().x+ResizableLabel.this.getWidth()/2 - BOX_SIZE, this.getLocation().y + ResizableLabel.this.getHeight()/2 - BOX_SIZE, BOX_SIZE, BOX_SIZE);
    }
    
    public String toString()
    {
        return "ResizableLabel";
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vector2D getUpperLeftPoint() {
        return upperLeftPoint;
    }

    public Vector2D getLowerRightPoint() {
        return lowerRightPoint;
    }
    
    
}
