/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tester.animation;

import core.gameobject.Animation;
import core.gameobject.BaseObject;

/**
 * This is an simple object with only one animation. This object is usefull to
 * preview animation in the animation editor.
 * @author panca
 */
public class AnimTestObject extends BaseObject{

    
    public AnimTestObject(Animation testAnim, double x, double y)
    {
        super();
        position.x = x;
        position.y = y;
        this.currentAnimation = testAnim;
        currentSprite = currentAnimation.getCurrentSprite();
    }
    @Override
    public void update(double elapsedTime) 
    {
        currentAnimation.update(elapsedTime);
        currentSprite = currentAnimation.getCurrentSprite();
    }
   
    
}
