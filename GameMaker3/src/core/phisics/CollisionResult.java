/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.phisics;

/**
 *
 * @author panca
 */
public class CollisionResult {
    
    private int collisionType;
    private Vector2D distance;

    
    public CollisionResult()
    {
        
    }
    
    public CollisionResult(int collisionType, Vector2D distance) {
        this.collisionType = collisionType;
        this.distance = distance;
    }
    
    
    public int getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(int collisionType) {
        this.collisionType = collisionType;
    }

    public Vector2D getDistance() {
        return distance;
    }

    public void setDistance(Vector2D distance) {
        this.distance = distance;
    }
    
    
    
    
}
