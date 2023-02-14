package core.map;


import core.DataReaderWriter;
import core.graphics.ImageBank;
import core.phisics.Vector2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

/**
 * A layer of the background. It can be a simple color, an tiled image, or an 
 * movable image. Use the visual editor to create the map background.
 * @author panca
 */
public class BackgroundLayer
{
    
    public static final int TYPE_COLOR = 0;
    public static final int TYPE_IMAGE = 1;
    
    private int layerType;
    private Map mapReference;
    
    private transient Image image;
    private String imageName;
    private Color color;
    //private int imageW, imageH;
    /**
     * tile the layer horizontally
     */
    private boolean tileFromCamX;
    
    /**
     * tile the layer vertically
     */
    private boolean tileFromCamY;    
    private boolean fillWindow;

    
    /**
     * The layer move automatically?
     */
    private boolean moveLayer;
    //points to make the layer 'move' with speed
    private Vector2D upperRightImagePoint;
    private Vector2D lowerLeftImagePoint;
    //the speed the map will atomatically move.
    private Vector2D moveSpeed;
    
    public BackgroundLayer()
    {
        moveSpeed = new Vector2D();
        lowerLeftImagePoint  = new Vector2D(0,0);
        upperRightImagePoint = new Vector2D();
    }
    
    public BackgroundLayer(String imageName, Color c, int layerType)
    {
        moveSpeed = new Vector2D();
        lowerLeftImagePoint  = new Vector2D();
        upperRightImagePoint = new Vector2D();
        
        this.layerType = layerType;
        if(imageName == null && c == null)
        {
            System.out.println("image and color NULL. ERROR: BackgroundLayer");
            System.exit(1);            
        }
        if(layerType == TYPE_COLOR && c == null)
        {
            System.out.println("color NULL. ERROR: BackgroundLayer");
            System.exit(1);            
        }
        if(layerType == TYPE_IMAGE && imageName == null)
        {
            System.out.println("image NULL. ERROR: BackgroundLayer");
            System.exit(1);            
        }
        this.imageName = imageName;
        this.color     = c;
    }
    
    //initialize the data
    public void setData(String imageName, Color c, int layerType)
    {   
        moveSpeed = new Vector2D();
        lowerLeftImagePoint  = new Vector2D(0,0);
        upperRightImagePoint = new Vector2D();
        this.layerType = layerType;
        if(imageName == null && c == null)
        {
            System.out.println("image and color NULL. ERROR: BackgroundLayer");
            System.exit(1);            
        }
        if(layerType == TYPE_COLOR && c == null)
        {
            System.out.println("color NULL. ERROR: BackgroundLayer");
            System.exit(1);            
        }
        if(layerType == TYPE_IMAGE && imageName == null)
        {
            System.out.println("image NULL. ERROR: BackgroundLayer");
            System.exit(1);            
        }
        this.imageName = imageName;
        this.color     = c;
        init();
    }
    
    public void setMapReference(Map mapReference)
    {
        this.mapReference = mapReference;
        if(mapReference == null)
            return;
        upperRightImagePoint.x = mapReference.getWindowWidth();
        upperRightImagePoint.y = mapReference.getWindowHeight();
        lowerLeftImagePoint.x = 0;
        lowerLeftImagePoint.y = 0;
        
    }
    

    public void init()
    {
        if(imageName == null)
            return;
        if(ImageBank.getImage(imageName) == null)
        {
            ImageBank.putImage(imageName);
        }
        image = ImageBank.getImage(imageName);        
    }
    
    public void render(Graphics2D g)
    {
       if(image == null)
       {
           
           g.setColor(color);
           g.fillRect(0, 0, mapReference.getWindowWidth(), mapReference.getWindowHeight());
       }
       else if(fillWindow)
       {
           g.drawImage(image, 0, 0, mapReference.getWindowWidth(), mapReference.getWindowHeight(),
                   0,0,image.getWidth(null), image.getHeight(null), null);
       }
       else if(tileFromCamX || tileFromCamY)
       {
           if(tileFromCamX && tileFromCamY)
           {
               int tStartX = (int) (mapReference.getLowerLeftWindowPoint().x/ image.getWidth(null));
                int tEndX   = (int) (mapReference.getUpperRightWindowPoint().x / image.getWidth(null));

                int tStartY = (int) ((mapReference.getMapHeight() - mapReference.getUpperRightWindowPoint().y)   / image.getHeight(null)); 
                int tEndY   = (int) ((mapReference.getMapHeight() - mapReference.getLowerLeftWindowPoint().y)   / image.getHeight(null) -1); 

                int nTY = tEndY - tStartY;
                int nTX = tEndX - tStartX;

                int dX = (int) (mapReference.getLowerLeftWindowPoint().x % image.getWidth(null));

                int dY = (int) ((mapReference.getMapHeight() - mapReference.getUpperRightWindowPoint().y)%image.getHeight(null));

                for(int i =0; i <= nTY+1; i++)
                {
                    for(int j = 0; j <= nTX; j++)
                    {
                        g.drawImage(image, j*image.getWidth(null) - dX, i * image.getHeight(null) - dY, null);
                    }
                }
           }
           else if(tileFromCamX)
           {
               int tStartX = (int) (mapReference.getLowerLeftWindowPoint().x / image.getWidth(null));
               int tEndX   = (int) (mapReference.getUpperRightWindowPoint().x / image.getWidth(null));
               int nTX = tEndX - tStartX;
            
               int dX = (int) (mapReference.getLowerLeftWindowPoint().x % image.getWidth(null));
            
               for(int j = 0; j <= nTX; j++)
               {
                   g.drawImage(image, j*image.getWidth(null) - dX, 0, null);
               }     
          }
          else if(tileFromCamY)
          {
              int tStartY = (int) ((mapReference.getMapHeight() - mapReference.getUpperRightWindowPoint().y)   / image.getHeight(null)); 
              int tEndY   = (int) ((mapReference.getMapHeight() - mapReference.getLowerLeftWindowPoint().y)   / image.getHeight(null) -1);
              int nTY = tEndY - tStartY;
              int dY = (int) (mapReference.getMapHeight() - mapReference.getUpperRightWindowPoint().y)%image.getHeight(null);
              for(int i =0; i <= nTY+1; i++)
              {
                g.drawImage(image, 0, i * image.getHeight(null) - dY, null);
              }
          }
       }
       else if(moveLayer)
       {
           
           double startX, startY = 0;
           if(lowerLeftImagePoint.x >= 0)
               startX = lowerLeftImagePoint.x - image.getWidth(null);
           else 
               startX = lowerLeftImagePoint.x;
           
           if(upperRightImagePoint.y < mapReference.getWindowHeight())
               startY = upperRightImagePoint.y + image.getHeight(null);
           else
               startY = upperRightImagePoint.y; 
          
          for(double y = startY; y >= 0; y-= image.getHeight(null))
          {
              for(double x = startX; x < mapReference.getWindowWidth(); x+= image.getWidth(null))
              {
                  g.drawImage(image, (int)x, mapReference.getWindowHeight() - ( int) y, null);
              }
          }
       }
    }
    
    public void update(double elapsed)
    {
        if(fillWindow || layerType == TYPE_COLOR)
            return;
        if(moveLayer){
            //if the layer moves..
            upperRightImagePoint.x += elapsed * moveSpeed.x;
            upperRightImagePoint.y += elapsed * moveSpeed.y;
            
            lowerLeftImagePoint.x += elapsed * moveSpeed.x;
            lowerLeftImagePoint.y += elapsed * moveSpeed.y;
            
            if(moveSpeed.y > 0)
            {
                if(lowerLeftImagePoint.y > mapReference.getWindowHeight())
                {
                    upperRightImagePoint.y = -1;
                    lowerLeftImagePoint.y = -image.getHeight(null);
                    lowerLeftImagePoint.y -= 1;
                }
            }
            else if(moveSpeed.y < 0)
            {
                if(upperRightImagePoint.y < 0)
                {
                    lowerLeftImagePoint.y = mapReference.getWindowHeight();
                    upperRightImagePoint.y = lowerLeftImagePoint.y + image.getHeight(null);
                }
            }
            
            
            if(moveSpeed.x < 0)//left
            {
                 if(upperRightImagePoint.x<0)
                 {
                     lowerLeftImagePoint.x = mapReference.getWindowWidth();
                     upperRightImagePoint.x = lowerLeftImagePoint.x + image.getWidth(null);
                 }
            }
            else if(moveSpeed.x > 0)
            {
                if(lowerLeftImagePoint.x > mapReference.getWindowWidth())
                {
                    upperRightImagePoint.x = mapReference.getWindowWidth() - image.getWidth(null);
                    lowerLeftImagePoint.x = upperRightImagePoint.x - image.getWidth(null);                   
                }
            }
        }
    }

    public int getLayerType() {
        return layerType;
    }

     public void save(DataOutputStream dos)
    {
        try {
            dos.writeInt(layerType);
            if(layerType == TYPE_COLOR)
            {
                dos.writeInt(color.getRGB());
            }
            else if(layerType == TYPE_IMAGE)
            {
                DataReaderWriter.writeString(imageName, dos);
                dos.writeBoolean(tileFromCamX);
                dos.writeBoolean(tileFromCamY);
                dos.writeBoolean(fillWindow);
                dos.writeBoolean(moveLayer);
                dos.writeDouble(moveSpeed.x);
                dos.writeDouble(moveSpeed.y);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
     public static BackgroundLayer load(DataInputStream dis)
     {
         BackgroundLayer bl = null;
         try {
             
             int lt = dis.readInt();
             if(lt == BackgroundLayer.TYPE_COLOR)
             {
                 Color c = new Color(dis.readInt());
                 bl= new BackgroundLayer(null, c, lt);
             }
             else
             {
                 String imgName = DataReaderWriter.readString(dis);
                 boolean tileCamX = dis.readBoolean();
                 boolean tileCamY = dis.readBoolean();
                 boolean fillwindow = dis.readBoolean();
                 boolean moveLayer = dis.readBoolean();
                 double movspdX = dis.readDouble();
                 double movspdY = dis.readDouble();
                 bl= new BackgroundLayer(imgName, null, lt);
                 bl.tileFromCamX = tileCamX;
                 bl.tileFromCamY = tileCamY;
                 bl.fillWindow = fillwindow;
                 bl.moveLayer = moveLayer;
                 Vector2D mvSpedd = new Vector2D(movspdX,movspdY);
                 bl.moveSpeed = mvSpedd;
             }
         } catch (Exception e) 
         {
             e.printStackTrace();
         }
         return bl;
     }
    
    public Vector2D getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(Vector2D moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public boolean isTileFromCamX() {
        return tileFromCamX;
    }

    public void setTileFromCamX(boolean tileFromCamX) {
        this.tileFromCamX = tileFromCamX;
    }

    public boolean isTileFromCamY() {
        return tileFromCamY;
    }

    public void setTileFromCamY(boolean tileFromCamY) {
        this.tileFromCamY = tileFromCamY;
    }

    public boolean isMoveLayer() {
        return moveLayer;
    }

    public void setMoveLayer(boolean moveLayer) {
        this.moveLayer = moveLayer;
    }


    public boolean isFillWindow() {
        return fillWindow;
    }

    public void setFillWindow(boolean fillWindow) {
        this.fillWindow = fillWindow;
    }

    public Image getImage() {
        return image;
    }

    public String getImageName() {
        return imageName;
    }

    public Color getColor() {
        return color;
    }
    
    @Override
    public String toString()
    {
        if(layerType == TYPE_COLOR)
            return "Color Bakground Layer";
        else if(layerType == TYPE_IMAGE)
            return "Image Bakground Layer";
        else
            return "Layer Without Type";
    }
}