/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.phisics;
import java.io.Serializable;
/**
 *
 * @author panca
 */
public class Vector2D implements Serializable{
    
    private static final long serialVersionUID = 6529685098267757699L;
    public double x, y;
    private double norm;

    public Vector2D()
    {
        
        
    }
    
    public Vector2D(double x, double y) 
    {
        this.x = x;
        this.y = y;
        computeNorm();
    }
    
    
    public Vector2D sub(Vector2D anotherVector)
    {
        return new Vector2D(x - anotherVector.getX(), y - anotherVector.getY());
    }
    
    
    public Vector2D add(Vector2D anotherVector)
    {
        return new Vector2D(x + anotherVector.getX(), y + anotherVector.getY());
    }
    
    public void computeNorm()
    {
        norm = Math.sqrt(x*x + y*y);
    }
    
    public double getNorm() {
        return norm;
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public String toString()
    {
        return x + "," + y;
    }
    
    public double dist(Vector2D v)
    {
        double rx = v.x - x;
        double ry = v.y - y;
        return Math.sqrt(rx*rx + ry*ry);
    }
}
