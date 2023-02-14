/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualeditors;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Panca
 */
public class SoundFileFilter extends FileFilter
{
    public SoundFileFilter()
    {
        super();
    }

    public String getDescription()
    {
        return "Compatible Sound Types";
    }

    public boolean accept(File img)
    {
        //Only list  jpg/bmp/png images
        if( (img.isDirectory())             ||
            (img.getName().endsWith("wav"))
           )
            return true;
        else
        return false;
    }

}