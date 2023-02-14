package core.map;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;


/**
 * A background is made of several background layers.
 * @author panca
 */
public class Background
{
    private Vector<BackgroundLayer> layers;
    private Map mapReference;
    
    public Background(Map mapReference)
    {
        this.mapReference = mapReference;
        layers = new Vector<BackgroundLayer>();
    }
    
    public void render(Graphics2D g)
    {
        for(BackgroundLayer b:layers)
            b.render(g);
    }

    
    public void init() {
        for(BackgroundLayer bl:layers)
            bl.init();
    }
    
    public void addLayer(BackgroundLayer bl)
    {
        bl.setMapReference(mapReference);
        layers.add(bl);
    }
    
    public void setMapReference(Map map)
    {
        this.mapReference = map;
        for(BackgroundLayer bl:layers)
            bl.setMapReference(mapReference);
    }
    public void update(double elapsed)
    {
        for(BackgroundLayer b:layers)
            b.update(elapsed);
    }
    
    public Vector<BackgroundLayer> getLayers()
    {
        return layers;
    }
    
    public void save(DataOutputStream dos)
    {
        try {
            dos.writeInt(layers.size());
            for(BackgroundLayer bl:layers)
            {
                bl.save(dos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Background load(DataInputStream dis)
    {
        Background b = null;
        try {
            int numLayers = dis.readInt();
            b = new Background(null);
            for(int i = 0; i < numLayers; i++)
            {
                BackgroundLayer bl = BackgroundLayer.load(dis);
                b.addLayer(bl);
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return b;
    }
}
