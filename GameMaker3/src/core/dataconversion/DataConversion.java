package core.dataconversion;

/**
 *
 * @author panca
 */
public class DataConversion {
    
    public static double milisToSecs(int milis)
    {
        return milis / 1000.0;
    }
    
    public int invY(int y, int height)
    {
        return height - y;
    }
    
    public int screenToWorld(int y)
    {
        return 0;
    }
}
