package core.gameobject;

import core.Config;
import core.DataReaderWriter;

import core.sound.SFX;
import core.sound.SFXManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

/**
 * This class represents an animation of sprites. An animation have a set of sprites
 * and the order it is displayed. 
 * @author panca
 */
public class Animation
{
    private Sprite[] sprites;
    private int[] spriteOrder;
    private int currentSprite;
    private int spriteNumber;
    private boolean loop;
    private boolean animFinished;
    private double totalTime;
    private SFX sfx;
    private boolean sfxPlayed;
    private boolean loopSFX;
    public Animation()
    {
        
    }
    
    
    /**
     * 
     * @param spr the sprites
     * @param animOrder the order the sprite will be shown
     * @param loop the animation loops?
     * @param sfx the animation sound effect. null if has no sound
     */
    public Animation(Sprite[] spr, int[] animOrder, boolean loop, SFX sfx)
    {
        this.sprites = spr;
        this.spriteOrder = animOrder;
        animFinished = false;
        this.loop = loop;
        currentSprite = 0;
        spriteNumber = animOrder.length;
        this.sfx = sfx;
    }
    
    /**
     * Returns the current sprite of the animation.
     * @return 
     */
    public Sprite getCurrentSprite()
    {
        return sprites[spriteOrder[currentSprite]];
    }
    
    
    public SFX getSFX()
    {
        return sfx;
    }
    
    public boolean isLoopSFX()
    {
        return loopSFX;
    }
    
    /**
     * Update the animation e. g. computes the next sprite to be shown.
     * @param time 
     */
    public void update(double time)
    {
        if(!sfxPlayed && sfx!= null){
            if(sfx!= null && sfx.getName()!= null)
                SFXManager.playSFX(sfx.getName());
                sfxPlayed = true;
        }
        
        totalTime += time;
        if(animFinished)
            return;
        if(totalTime > getCurrentSprite().getTime())
        {
            //próximo sprite
            currentSprite++;
            //loop
            if(currentSprite>=spriteNumber)
            {
                if(loop)
                {
                    currentSprite = 0;
                    if(loopSFX)
                    {
                        sfxPlayed = false;
                    }
                }
                else{
                    currentSprite = spriteNumber -1;
                    animFinished = true;
                }
                
            }
            //reseta o tempo 
            totalTime = 0;
        }
    }
    
    /**
     * Set current sprite as the sprite 0 and replay the sound
     */
    public void restartAnimation()
    {
        currentSprite = 0;
        animFinished = false;
        totalTime = 0;
        
        sfxPlayed = false;
    }
    
    public boolean finished()
    {
       return animFinished;
        /*if(currentSprite >= spriteNumber && !loop)
            return true;
        return false;*/
    }
    /**
     * the sfx will be played one time or will loop?
     * @param loop 
     */
    public void setLoopSFX(boolean loop)
    {
        this.loopSFX = loop;
    }

    public void setSFX(SFX sfx)
    {
        this.sfx = sfx;
    }
    
    public Sprite[] getSprites() {
        return sprites;
    }

    public void setSprites(Sprite[] sprites) {
        this.sprites = sprites;
    }

    public int[] getSpriteOrder() {
        return spriteOrder;
    }

    public void setSpriteOrder(int[] spriteOrder) {
        this.spriteOrder = spriteOrder;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    public Animation getLeftFlippedCopy()
    {
        Animation a = null;
        
        Sprite s[] = new Sprite[this.sprites.length];
        int sorder[] = new int[this.spriteOrder.length];
        for(int i = 0 ; i< sorder.length; i++)
            sorder[i] = spriteOrder[i];
        for(int i = 0 ; i < s.length; i++)
        {
            s[i] = sprites[i].getFlipped();            
        }
        a = new Animation(s,sorder,this.loop,new SFX(this.sfx.getName()));
        a.loopSFX = this.loopSFX;
        
        
        return a;
    }
    /**
     * Load an animation. animName is the path/name relative to PROJECT/ANIM_PATH/animName
     * @param animFullPath
     * @return 
     */
    public static Animation load(String animFullPath)
    {
        Animation a = null;
        try{
            DataInputStream dis = new DataInputStream(new FileInputStream(new File(animFullPath)));
            int sprnum = dis.readInt();
            boolean loopAnim = dis.readBoolean();
            boolean loopSFX = dis.readBoolean();
            String sfxName = DataReaderWriter.readString(dis);
            int spriteOrderSize = dis.readInt();
            
            Sprite[] s = new Sprite[sprnum];
            int[] spriteOrder = new int[spriteOrderSize];
            for(int i = 0; i < spriteOrderSize; i++){
                spriteOrder[i] = dis.readInt();
                
            }
            
            for(int i = 0; i < sprnum; i++)
                s[i] = Sprite.load(dis);
            
            dis.close();
            SFX sfx = new SFX(sfxName);
            
            a = new Animation(s, spriteOrder, loopAnim, sfx);
            a.setLoopSFX(loopSFX);
            
            a.init();
            
            
            
            return a;
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error loading animation: "+e.getMessage(), "Animation load error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return a;
    }
    
    /**
     * Save the animation. The images are not saved.
     * @param a
     * @param file 
     */
    public static void save(Animation a, String file)
    {
        if(!file.endsWith(Config.FILE_EXT_ANIM))
            file += Config.FILE_EXT_ANIM;
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(file)));
            //ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(file)));
            dos.writeInt(a.sprites.length);
            dos.writeBoolean(a.loop);
            dos.writeBoolean(a.loopSFX);
            DataReaderWriter.writeString(a.getSFX().getName(), dos);
            dos.writeInt(a.spriteOrder.length);
            for(int i=0; i < a.spriteOrder.length; i++)
                dos.writeInt(a.spriteOrder[i]);
            
            for(Sprite s:a.sprites)
            {
                s.save(dos);
            }
            dos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * load all animation data (images and sfx);
     */
    public void init() {
        for(Sprite s: getSprites())
                s.init();//ImageBank.putImage(s.getImageName());
        if(sfx!=null)
            sfx.init();
    }
   
}
