/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.graphics;

import core.ResourceManager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author panca
 */
public class ImageLoader {
    
    public static Image loadImage(String name){       
        
        //URL dirUrl = ImageLoader.class.getResource(dir);
        //System.out.println("URL: "+dirUrl);
        BufferedImage img = null;
        try {
            
            img = javax.imageio.ImageIO.read(new File(ResourceManager.createResourceString(name, ResourceManager.RESSOURCE_TYPE_IMAGE)));
        } catch (IOException ex) {
            System.out.println("ERROR LOADING IMAGE FILE:  "+name);
            ex.printStackTrace();
            //System.exit(1);
        }
        return img;
    }
}
