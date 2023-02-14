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
public class Balloon extends BaseObject{

    boolean explode;
    private Animation explodeAnim, normalAmim;
    public Balloon()
    {
        super();
        alive = true;
        normalAmim = Animation.load(ResourceManager.createResourceString("baloon_normal.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        explodeAnim = Animation.load(ResourceManager.createResourceString("baloon_explode.ani", ResourceManager.RESSOURCE_TYPE_ANIMATION));
        currentAnimation = normalAmim;
        currentSprite = normalAmim.getCurrentSprite();
    }
    @Override
    public void update(double elapsedTime) 
    {
        currentAnimation.update(elapsedTime);
        currentSprite = currentAnimation.getCurrentSprite();
        if(explode)
        {
            if(currentAnimation.finished())
            {
                alive = false;
                return;
            }
        }
    }
    
    public void explode()
    {
        explode = true;
        currentAnimation = explodeAnim;
        currentAnimation.restartAnimation();
    }
}
