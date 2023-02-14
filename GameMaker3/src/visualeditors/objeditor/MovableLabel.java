/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualeditors.objeditor;


import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


/**
 *
 * @author panca
 */
public class MovableLabel extends JLabel
{
    private boolean clicked;
    private Container owner;
    int inX, inY;
    private Image img;
    private int zoom;
    /**
     * The object this MovableLabel containts
     */
    Object containedObject;
    Point posBkp;
    /**
     * the string to describe wich object is this.
     */
    String objectDescription;
    
    public void setZoom(int zoom)
    {
        this.zoom = zoom;
        setSize(img.getWidth(null) *zoom, img.getHeight(null)*zoom);
        setBounds(0,0,img.getWidth(null)*zoom, img.getHeight(null)*zoom);
    }
    
    public MovableLabel(Container owner)
    {
        
        super("teste");
        posBkp = new Point();
        zoom = 1;
        this.owner = owner;
        addMouseListener(new MovableLabelMouseListener(this));
        addMouseMotionListener(new MovableLabelMouseMotionListener(this));
       setLocation(100, 100);
       setSize(50, 20);
       
        
    }

    public Object getContainedObject() {
        return containedObject;
    }

    public void setContainedObject(Object containedObject) {
        this.containedObject = containedObject;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }
    
    @Override
    public void paint(Graphics g)
    {
        //super.paint(g);
        setLocation(posBkp);
        g.drawImage(img, 0, 0, img.getWidth(null)*zoom, img.getHeight(null)*zoom, null);
    }
    
    
 
    
    /**
     * Receive one image to be displayed
     * @param img 
     * @param owner 
     */
    public MovableLabel(Image img,Container owner)
    {
        super();
        posBkp = new Point();
        zoom = 1;
        this.img = img;
        this.owner = owner;
        addMouseListener(new MovableLabelMouseListener(this));
        addMouseMotionListener(new MovableLabelMouseMotionListener(this));
        this.setIcon(new ImageIcon(img));
        setSize(img.getWidth(null), img.getHeight(null));
        setBounds(0,0,img.getWidth(null), img.getHeight(null));
    }
    
    private class MovableLabelMouseListener implements MouseListener
    {
        private MovableLabel owner;
        
        public MovableLabelMouseListener(MovableLabel ml)
        {
            owner = ml;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            /*if(e.getButton() == MouseEvent.BUTTON3)
            {
                Sprite en = (Sprite)MovableLabel.this.containedObject;
                Vector<MovableLabel> v = scenaryEditor.enemyList;             
                v.remove(MovableLabel.this);
                
                owner.remove(MovableLabel.this);
                MovableLabel.this.setVisible(false);
                owner.updateUI();
                System.out.println("era para remover.");
                System.out.println("botao direito");
                return;
            }*/
            if(e.getButton() == MouseEvent.BUTTON1){
                clicked = true;
                inX = e.getX();
                inY = e.getY();
                
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1){
                clicked = false;
                
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
    
    
    public void setLocation(Point p)
    {
        super.setLocation(p);
         posBkp.x = p.x;
         posBkp.y = p.y;
        
    }
    
    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
        posBkp.x = x;
        posBkp.y = y;
    }
    private class MovableLabelMouseMotionListener implements MouseMotionListener
    {
        private MovableLabel owner;
        public MovableLabelMouseMotionListener(MovableLabel ml)
        {
            owner = ml;
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            
            if(owner.clicked)
            {
                
                Point p = MovableLabel.this.owner.getMousePosition();
            
                p.setLocation(p.getX()-inX, p.getY()-inY);
                posBkp.x = p.x;
                posBkp.y = p.y;
                setLocation(p);
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
        
            
        }
    }
    
}
