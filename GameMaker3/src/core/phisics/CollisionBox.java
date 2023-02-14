/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.phisics;

import java.awt.Color;
import java.awt.Graphics;
import core.map.Map;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author panca
 */
public class CollisionBox
{
    public static final int COLLISION_TYPE_NONE     = 0;
    public static final int COLLISION_TYPE_UP       = 1;
    public static final int COLLISION_TYPE_DOWN     = 2;
    public static final int COLLISION_TYPE_LEFT     = 3;
    public static final int COLLISION_TYPE_RIGHT    = 4;
    
    /**
     * the distance from the lower left point in image coordinates:
     * *--------------*
     * |              |
     * |              |
     * |   *---*      |
     * |   |   |      |
     * |   *---*      |
     * |  /           |
     * | /            |
     * *--------------*
     * Used in the scenary collision box.
     */
    private double  imageReferenceX, imageReferenceY;
    
    private Vector2D distanceFromP;
    /**
     * It's the higher coordinate.
     */
    private Vector2D upperRightPoint;
    
    /**
     * It's the lower coordinate.
     */
    private Vector2D lowerLeftPoint;
    
    
    /**
     * its the hiher left coordinate.
     */
    private Vector2D upperLeftPoint;
    
    /**
     * its the lower right coordinate.
     */
    private Vector2D lowerRightPoint;
    
    private double width, height;
    /**
     * The middle point in the collision box.
     */
    private Vector2D midPoint;
    public CollisionBox(Vector2D uppRight, Vector2D lowLeft)
    {
        this.upperRightPoint = uppRight;
        this.lowerLeftPoint = lowLeft;
        width  = uppRight.getX() - lowLeft.getX();
        height = uppRight.getY() - lowLeft.getY();
        this.imageReferenceX = lowLeft.getX();
        this.imageReferenceY = lowLeft.getY();
        
        upperLeftPoint = new Vector2D(lowLeft.getX(), uppRight.getY());
        lowerRightPoint = new Vector2D(uppRight.getX(), lowLeft.getY());
        
        this.midPoint = new Vector2D(uppRight.sub(lowLeft).getX() / 2, 
                uppRight.sub(lowLeft).getY()/2); 
        
    }
    
    public CollisionBox()
    {
        
    }
    
    /**
     * Used to move scenary collision boxes!
     * @param point
     * @return 
     */
    public CollisionBox centerBottomAt(Vector2D point)
    {        
        int mid = (int) ((lowerRightPoint.getX() - lowerLeftPoint.getX()) / 2);
        int h   = (int) (upperRightPoint.getY() - lowerRightPoint.getY());        
        Vector2D ll = new Vector2D(point.getX() - mid, 0);
        Vector2D ur = new Vector2D(point.getX() + mid, 0);
        ll.setY(point.getY());
        ur.setY(point.getY()+h);
        
        CollisionBox c = new CollisionBox(ur, ll);
        return c;
    }
    
    /**
     * Get a new copy of this collision box translated (collision and atk).
     * USE THIS ONLY TO ATK AND VULN BOXES
     * @param point
     * @return 
     */
    public CollisionBox getTranslated(Vector2D point)
    {
        CollisionBox ncb =null;
        
        
        Vector2D ll = new Vector2D(point.getX()+distanceFromP.getX(), point.getY()+distanceFromP.getY());
        Vector2D ur = new Vector2D(ll.getX()+width, ll.getY()+height);
        
        ncb = new CollisionBox(ur, ll);
        return ncb;
    }

    public boolean collide(CollisionBox anotherBox)
    {
        double b1_x, b2_x, b1_w, b2_w, b1_y, b2_y, b1_h, b2_h;
        b1_x = this.getLowerLeftPoint().getX();
        b1_w = this.getWidth();
        b1_h = this.getHeight();
        b1_y = this.getLowerLeftPoint().getY();
        
        b2_x = anotherBox.getLowerLeftPoint().getX();
        b2_w = anotherBox.getWidth();
        b2_h = anotherBox.getHeight();
        b2_y = anotherBox.getLowerLeftPoint().getY();
        
        if ((b1_x > b2_x + b2_w - 1) || // is b1 on the right side of b2?
        (b1_y > b2_y + b2_h - 1) || // is b1 under b2?
        (b2_x > b1_x + b1_w - 1) || // is b2 on the right side of b1?
        (b2_y > b1_y + b1_h - 1))   // is b2 under b1?
        {
        // no collision
            return false;
        }
        return true;
    }
    
    /**
     *
     * @param anotherBox
     * @param movimento
     * @return
     */
    public CollisionResult collisionType(CollisionBox anotherBox, Vector2D movimento)
    {
        CollisionResult cr = new CollisionResult();
        cr.setCollisionType(COLLISION_TYPE_NONE);
        double b1_xe, b1_xd, b2_xe, b2_xd,  b1_yb,b1_yc, b2_yb,b2_yc;
        b1_xe = this.lowerLeftPoint.x;
        b1_xd = this.upperRightPoint.x;
        b1_yb = this.lowerLeftPoint.y;
        b1_yc = this.upperRightPoint.y;
		
        b2_xe = anotherBox.lowerLeftPoint.x;
        b2_xd = anotherBox.upperRightPoint.x;
        b2_yb = anotherBox.lowerLeftPoint.y;
        b2_yc = anotherBox.upperRightPoint.y;
        //'pisou'
        if(movimento.y < 0 && b1_yb < b2_yc && b1_yb > b2_yb){
           if((b1_xe >= b2_xe && b1_xe < b2_xd) || (b1_xd >= b2_xe && b1_xd < b2_xd) ){
               
               cr.setCollisionType(COLLISION_TYPE_DOWN);
               cr.setDistance(new Vector2D(0, Math.abs(b1_yb - b2_yc)));

           }
        }
		//'foi pisado'
        if (movimento.y > 0 && b1_yc >= b2_yb && b1_yc < b2_yc ){
           if((b1_xe >= b2_xe && b1_xe < b2_xd) || (b1_xd >= b2_xe && b1_xd < b2_xd) )
           {
               cr.setCollisionType(COLLISION_TYPE_UP);
           }
        }
        //bateu a direita
        if(movimento.x > 0 && b1_xd >=  b2_xe && b1_xd < b2_xd){
            if((b1_yb >= b2_yb && b1_yb < b2_yc ) || (b1_yc >= b2_yb && b1_yc < b2_yc)){
                cr.setCollisionType(COLLISION_TYPE_RIGHT);
            }
        }
		//bateu esquerda
        if(movimento.x < 0 && b1_xe < b2_xd && b1_xe >  b2_xe){
            if((b1_yb >= b2_yb && b1_yb < b2_yc ) || (b1_yc >= b2_yb && b1_yc < b2_yc)){
                cr.setCollisionType(COLLISION_TYPE_LEFT);
            }
        }
        //se nao houve movimento..
        return cr;
    }
    
    public Vector2D getUpperRightPoint() {
        return upperRightPoint;
    }

    public void setUpperRightPoint(Vector2D upperRightPoint) {
        this.upperRightPoint = upperRightPoint;
    }

    public Vector2D getLowerLeftPoint() {
        return lowerLeftPoint;
    }

    public void setLowerLeftPoint(Vector2D lowerLeftPoint) {
        this.lowerLeftPoint = lowerLeftPoint;
    }

    public Vector2D getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(Vector2D midPoint) {
        this.midPoint = midPoint;
    }

    public Vector2D getUpperLeftPoint() {
        return upperLeftPoint;
    }

    public void setUpperLeftPoint(Vector2D upperLeftPoint) {
        this.upperLeftPoint = upperLeftPoint;
    }

    public Vector2D getLowerRightPoint() {
        return lowerRightPoint;
    }

    public void setLowerRightPoint(Vector2D lowerRightPoint) {
        this.lowerRightPoint = lowerRightPoint;
    }    
    
    public void renderBox(Graphics g, Map mapRef, Color color)
    { 
        g.setColor(color);
        int px, py, w, h;
        w = (int) (this.getLowerRightPoint().getX() - this.getLowerLeftPoint().getX()) ;
        h = (int) (this.getUpperLeftPoint().getY() - this.getLowerLeftPoint().getY());
        px = (int) ((this.getUpperLeftPoint().getX() - mapRef.getLowerLeftWindowPoint().x));// + distanceFromMiddleX);
        
        py = (int) ((int) this.getUpperLeftPoint().getY() -  mapRef.getLowerLeftWindowPoint().y) ;
        py = mapRef.getWindowHeight() - py;
        g.setColor(color);
        g.drawRect(px, py, w, h);  
    }    

    public double getImageReferenceX() {
        return imageReferenceX;
    }

    public void setImageReferenceX(double imageReferenceX) {
        this.imageReferenceX = imageReferenceX;
    }

    public double getImageReferenceY() {
        return imageReferenceY;
    }

    public void setImageReferenceY(double imageReferenceY) {
        this.imageReferenceY = imageReferenceY;
    }

    public void setDistanceFromP(double x, double y)
    {
        distanceFromP = new Vector2D(x, y);
    }

    public Vector2D getDistanceFromP()
    {
        return distanceFromP;
    }
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    
    public void save(DataOutputStream dos)
    {
        try {
            dos.writeDouble(imageReferenceX);
            dos.writeDouble(imageReferenceY);
            if(distanceFromP!=null){
                dos.writeDouble(distanceFromP.x);
                dos.writeDouble(distanceFromP.y);
            }
            else
            {
                double zero = 0.0;
                dos.writeDouble(zero);
                dos.writeDouble(zero);
            }
            dos.writeDouble(upperRightPoint.x);
            dos.writeDouble(upperRightPoint.y);
            dos.writeDouble(lowerLeftPoint.x);
            dos.writeDouble(lowerLeftPoint.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static CollisionBox load(DataInputStream dis)
    {
        CollisionBox c = null;
        try {
            double imgRefX = dis.readDouble();
            double imgRefY = dis.readDouble();
            double distFromPx = dis.readDouble();
            double distFromPy = dis.readDouble();
            double uprpX = dis.readDouble();
            double uprpY = dis.readDouble();
            Vector2D up = new Vector2D(uprpX, uprpY);
            
            double llpX = dis.readDouble();
            double llpY = dis.readDouble();
            Vector2D lo = new Vector2D(llpX, llpY);
            
            c =  new CollisionBox(up, lo);
            c.setDistanceFromP(distFromPx, distFromPy);
            
            c.setImageReferenceX(imgRefX);
            c.setImageReferenceY(imgRefY);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return c;
        
    }
    
}  
