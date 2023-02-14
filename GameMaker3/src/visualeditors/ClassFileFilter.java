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
 * @author panca
 */
public class ClassFileFilter extends FileFilter{

    @Override
    public boolean accept(File file) {
        if( (file.isDirectory())             ||
            (file.getName().endsWith("class"))
           )
            return true;
        else
        return false;
    }

    @Override
    public String getDescription() {
        return "Java Class Files.";
    }
    
}
