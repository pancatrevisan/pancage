/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualeditors;

import core.ResourceManager;
import java.awt.Container;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author panca
 */
public class GuiHelper {
    static final JFileChooser chooseImage = new JFileChooser();
    public static String copyImage(Container component) {
        String choosenImg = "";
        
        chooseImage.setFileFilter(new ImageTipeFilter());
        int r = chooseImage.showOpenDialog(component);
        if (r!= JFileChooser.APPROVE_OPTION)
            return null;
        
        choosenImg = chooseImage.getSelectedFile().getAbsolutePath();
        
        File dstImage = ResourceManager.createFile(chooseImage.getSelectedFile().getName(), ResourceManager.RESSOURCE_TYPE_IMAGE);
        if(dstImage.exists())
        {
            int opt = JOptionPane.showConfirmDialog(component, "Image already Exists. Overwrite?");
            if(opt != JOptionPane.YES_OPTION)
            {
                String newName = JOptionPane.showInputDialog("Type the new image name: ");
                if(!newName.endsWith(".png"))
                    newName += ".png";
                dstImage = ResourceManager.createFile(newName, ResourceManager.RESSOURCE_TYPE_IMAGE);
            }
        }
        try{
            Image image = javax.imageio.ImageIO.read(new File(choosenImg));
            BufferedImage bi = (BufferedImage) image;
            javax.imageio.ImageIO.write(bi, "png", dstImage);
            image = javax.imageio.ImageIO.read(dstImage);
            return dstImage.getName();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
        //BufferedImage dst = (BufferedImage) 
        
    }
    
    public static String copySound(Container component) {
        String choosenImg = "";
        
        chooseImage.setFileFilter(new SoundFileFilter());
        int r = chooseImage.showOpenDialog(component);
        if (r!= JFileChooser.APPROVE_OPTION)
            return null;
        
        choosenImg = chooseImage.getSelectedFile().getAbsolutePath();
        
        File dstImage = ResourceManager.createFile(chooseImage.getSelectedFile().getName(), ResourceManager.RESSOURCE_TYPE_SOUND);
        if(dstImage.exists())
        {
            int opt = JOptionPane.showConfirmDialog(component, "Sound already Exists. Overwrite?");
            if(opt != JOptionPane.YES_OPTION)
            {
                String newName = JOptionPane.showInputDialog("Type the new sound name: ");
                
                dstImage = ResourceManager.createFile(newName, ResourceManager.RESSOURCE_TYPE_SOUND);
            }
        }
        try{
            FileInputStream is = new FileInputStream(choosenImg);
            OutputStream os = new FileOutputStream(dstImage);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            return dstImage.getName();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
        //BufferedImage dst = (BufferedImage) 
        
    }
    
    public static String copyMusic(Container component)
    {
        String choosenImg = "";
        
        chooseImage.setFileFilter(new MusicFileFilter());
        int r = chooseImage.showOpenDialog(component);
        if (r!= JFileChooser.APPROVE_OPTION)
            return null;
        
        choosenImg = chooseImage.getSelectedFile().getAbsolutePath();
        
        File dstImage = ResourceManager.createFile(chooseImage.getSelectedFile().getName(), ResourceManager.RESSOURCE_TYPE_MUSIC);
        if(dstImage.exists())
        {
            int opt = JOptionPane.showConfirmDialog(component, "Sound already Exists. Overwrite?");
            if(opt != JOptionPane.YES_OPTION)
            {
                String newName = JOptionPane.showInputDialog("Type the new sound name: ");
                
                dstImage = ResourceManager.createFile(newName, ResourceManager.RESSOURCE_TYPE_MUSIC);
            }
        }
        try{
            FileInputStream is = new FileInputStream(choosenImg);
            OutputStream os = new FileOutputStream(dstImage);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            return dstImage.getName();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
