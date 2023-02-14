/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import core.GameWindow;
import core.ResourceManager;
import core.gameobject.Animation;
import core.gameobject.BaseObject;
import core.phisics.CollisionBox;
import java.util.Vector;

/**
 *
 * @author Panca
 */
public class Raptor extends BaseObject{

    Animation walkRight,walkLeft, stopRight,stopLeft, biteRight, biteLeft, jumpRight, jumpLeft;
    public Raptor()
    {
        super();
        alive = true;
        walkRight = Animation.load(ResourceManager.createResourceString("walk_right.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        walkLeft = walkRight.getLeftFlippedCopy();
        
        biteRight = Animation.load(ResourceManager.createResourceString("bite_right.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        biteLeft = biteRight.getLeftFlippedCopy();
        
        stopRight = Animation.load(ResourceManager.createResourceString("stop_right.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        stopLeft = stopRight.getLeftFlippedCopy();
        
        jumpRight = Animation.load(ResourceManager.createResourceString("jump_right.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        jumpLeft = jumpRight.getLeftFlippedCopy();
        
        
        currentAnimation = stopRight;
        currentSprite = currentAnimation.getCurrentSprite();
    }
    
    double walkSpeed = 300;
    double jump_height = 80;
    double jump_total = 0;
    
    boolean walk, bite, jump;
    @Override
    public void update(double elapsedTime) {
        currentAnimation.update(elapsedTime);
        currentSprite = currentAnimation.getCurrentSprite();
       
        //set current animation...
        if(bite){
            checkBite();
            if(currentAnimation.finished())
                bite = false;
            return;
        }
        else if(jump){
            if(currentAnimation != jumpLeft && currentAnimation!=jumpRight){
                if(facingSide == FACING_LEFT)
                    currentAnimation = jumpLeft;
                else
                    currentAnimation = jumpRight;
            }
        }
        else if(walk && onFloor){
            if(currentAnimation != walkLeft && currentAnimation != walkRight){
                if(facingSide == FACING_LEFT)
                    currentAnimation = walkLeft;
                else
                    currentAnimation = walkRight;
            }
        }
        else if(onFloor){
            if(facingSide == FACING_LEFT)
                    currentAnimation = stopLeft;
                else
                    currentAnimation = stopRight;
        }
        
        //now move!
        double inc = walkSpeed * elapsedTime;
        if(walk && facingSide == FACING_RIGHT){   
            setNewPosition(position.x+inc, position.y);
        }
        else if(walk &&facingSide == FACING_LEFT){   
            setNewPosition(position.x-inc, position.y);
        }
        
        if(jump){
            jump_total+= inc;
            setNewPosition(position.x, position.y + inc);
            if(jump_total >= jump_height){
                jump_total = 0;
                jump = false;
            }
        }
        else{
            apllyGravity(elapsedTime);
        }
        
    }
    
    private void checkBite(){
        if(currentSprite.getAttackCollisionBox().size() <= 0)
            return;
        CollisionBox atk = currentSprite.getAttackCollisionBox().elementAt(0);
        atk = atk.getTranslated(position);
        for(int i = 0; i < mapReference.getObjects().size(); i++){
            BaseObject b = mapReference.getObjects().get(i);
            Vector<CollisionBox> boxes = 
                    b.getCurrentSprite().getVulnerableCollisionBox();
            if(b.getName().startsWith("balloon")){
                if(b.isAlive() && boxes.size()> 0){
                    CollisionBox vuln = boxes.get(0);
                    if(vuln != null){
                        vuln = vuln.getTranslated(b.getPosition());
                        if(atk.collide(vuln)){
                            Balloon conv = (Balloon) b;
                            conv.explode();
                        }
                    }
                }
            }
        }
    }
    
    public void setWalking(boolean w)
    {
        walk = w;       
    }
    
    public void bite(){
        if(onFloor){
            bite = true;
            if(facingSide == FACING_LEFT)
                currentAnimation = biteLeft;
            else
                currentAnimation = biteRight;
            currentAnimation.restartAnimation();
        }
    }
    
    public void jump(){
        if(onFloor)
            jump = true;
    }
}
