package visualeditors;
import javax.swing.filechooser.FileFilter;
import java.io.File;
public class ImageTipeFilter  extends FileFilter
{
    public ImageTipeFilter()
    {
        super();
    }

    public String getDescription()
    {
        return "Compatible Image Types";
    }

    public boolean accept(File img)
    {
        //Only list  jpg/bmp/png images
        if( (img.isDirectory())             ||
            (img.getName().endsWith("jpg")) ||
            (img.getName().endsWith("bmp")) ||
            (img.getName().endsWith("png")) ||
            (img.getName().endsWith("jpeg"))
           )
            return true;
        else
        return false;
    }

}
