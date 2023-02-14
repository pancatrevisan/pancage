/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import core.ResourceManager;
import core.gameobject.Animation;
import core.gameobject.BaseObject;

/**
 *
 * @author Panca
 */
public class Plataforma extends BaseObject{

    private int MAX_UP = 150;
    private double SPEED = 100;
    private double up = 0;
    private boolean goingUp = true;
    public Plataforma()
    {
        super();
        currentAnimation = Animation.load(ResourceManager.createResourceString("platform1.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        currentSprite = currentAnimation.getCurrentSprite();
        alive = true;
    }
    
    @Override
    public void update(double elapsedTime) {
        
        if(goingUp)
        {
            double inc = elapsedTime * SPEED;
            up += inc;
            position.y += inc;
            if(up >= MAX_UP)
            {
                up = 0;
                goingUp = false;
            }
        }
        else
        {
            double inc = elapsedTime * SPEED;
            up += inc;
            position.y -= inc;
            if(up >= MAX_UP)
            {
                up = 0;
                goingUp = true;
            }
        }
    }
    
}
