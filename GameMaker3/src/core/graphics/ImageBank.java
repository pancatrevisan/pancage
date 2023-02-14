package core.graphics;

import java.awt.Image;
import java.util.Hashtable;

/**
 * All images will be placed in the imageBank. It will prevent to loand the 
 * same image several times.
 * @author panca
 */
public class ImageBank 
{
    private static Hashtable<String, Image> images = new Hashtable<String, Image>();
   
    public ImageBank()
    {
        
    }
    
    /**
     * get an image from the bank;
     * @param key
     * @return 
     */
    public static Image getImage(String key)
    {
        if(key == null)
            return null;
        
        if(images.containsKey(key))
        {            
            return images.get(key);
        }
        return null;
    }
    
    //put an image in the bank.
    public static void putImage(String img)
    {
        if(!images.containsKey(img))
            images.put(img, ImageLoader.loadImage(img));
    }
    
    public static void removeImage(String id)
    {
        images.remove(id);
    }
}
