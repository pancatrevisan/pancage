/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.map;

import core.Config;
import core.DataReaderWriter;

import core.gameobject.BaseObject;
import core.phisics.Vector2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author panca
 */
public class Map {
    private Background background;
    private Background foreground;
    private Tileset tileset;
    private Tile[][][] tiles;
   // private int layer;
    private int numberOfLayers;
    private Vector2D lowerLeftWindowPoint;
    private Vector2D upperRightWindowPoint;
    private int windowHeight;
    private int windowWidth;
    private int mapHeight;
    private int mapWidth;
    private int numberOfTilesX;
    private int numberOfTilesY;
    
    private boolean moveX;
    private boolean moveY;
    private double gravity;
    private String music;
    /**
     * Objects in map.
     */
    private Vector<BaseObject> objects;
    
    private Hashtable<String, BaseObject> objectsPerName;
    
    public Map(Tileset tileset, int numberOfTilesX, int numberOfTilesY, int windowWidth, int windowHeight, int numberOfLayers)
    {
        
        this.tileset = tileset;
        this.numberOfTilesX = numberOfTilesX;
        this.numberOfTilesY = numberOfTilesY;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.numberOfLayers = numberOfLayers;
        mapHeight = tileset.getTileSize() * numberOfTilesY;
        mapWidth  = tileset.getTileSize() * numberOfTilesX;
        lowerLeftWindowPoint = new Vector2D(0,0);
        upperRightWindowPoint = new Vector2D(windowWidth,windowHeight);
        tiles = new Tile[numberOfLayers][numberOfTilesY][numberOfTilesX];
        background = new Background(this);
        foreground = new Background(this);
        objects = new Vector<BaseObject>();
        objectsPerName = new Hashtable<String, BaseObject>();
    }
    
    public void setMusic(String music)
    {
        this.music = music;
    }
    
    public String getMusic()
    {
        return this.music;
    }
    
    public void init()
    {
        tileset.init();
        if(background!= null)
            background.init();
        //for(BaseObject b:objects)
        //    b.init();
    }
    public static Map load(String src)
    {
        Map m = null;
        
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(new File(src)));
            Tileset t = Tileset.load(dis);
            t.init();
            Background bl = null;
            Background fr = null;
            if(dis.readBoolean())//has bkg
            {
                bl = Background.load(dis);
            }
            if(dis.readBoolean())//has foreground
            {
                fr = Background.load(dis);
            }
            int ww = dis.readInt();
            int wh = dis.readInt();
            
            boolean mvx = dis.readBoolean();
            boolean mvy = dis.readBoolean();
            double g = dis.readDouble();
            String mus = DataReaderWriter.readString(dis);
            int nl = dis.readInt();
            
            int numtx = dis.readInt();
            int numty = dis.readInt();
            m = new Map(t, numtx, numty, ww, wh, nl);
            m.moveX = mvx;
            m.moveY = mvy;
            m.music = mus;
            m.gravity = g;
            if(bl!=null)
            {
                bl.init();
                bl.setMapReference(m);
            }
            if(fr!=null){
                fr.init();
                fr.setMapReference(m);
            }
            
            
            m.setBackground(bl);
            m.setForeground(fr);
            for(int i = 0; i < m.numberOfLayers; i++)
            {
                for(int j = 0; j < m.numberOfTilesY; j++)
                {
                    for (int k = 0; k < m.numberOfTilesX; k++) {
                        if(dis.readBoolean()){
                            int line = dis.readInt();
                            int col  = dis.readInt();
                            m.tiles[i][j][k] = t.getTile(line, col);
                        }
                    }
                }
            }
            int numObj = dis.readInt();
            for(int i = 0; i < numObj; i++)
            {
                BaseObject b = BaseObject.load(dis);
                b.setMapReference(m);
                m.addObject(b);
            }
            dis.close();
            m.init();
            
            return m;
            
        } catch (Exception ex) {
            System.out.println("ERROR LOADING MAP "+src);
            ex.printStackTrace();
            System.exit(2);
        }
        return null;
    }
    
    public static void save(Map map, String dest)
    {
        
        if(!dest.endsWith(Config.FILE_EXT_MAP))
        {
            dest+= Config.FILE_EXT_MAP;
        }
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(dest)));
            map.tileset.save(dos);
            //do it have a bkg?
            if(map.background!=null){
                dos.writeBoolean(true);
                map.background.save(dos);
            }
            else
                dos.writeBoolean(false);
            
            
            if(map.foreground!= null)
            {
                dos.writeBoolean(true);
                map.foreground.save(dos);
            }
            else
                dos.writeBoolean(false);


            dos.writeInt(map.windowWidth);
            dos.writeInt(map.windowHeight);
            dos.writeBoolean(map.moveX);
            dos.writeBoolean(map.moveY);
            dos.writeDouble(map.gravity);
            DataReaderWriter.writeString(map.music, dos);

            dos.writeInt(map.numberOfLayers);
            dos.writeInt(map.numberOfTilesX);
            dos.writeInt(map.numberOfTilesY);

            //save the map reference to tiles
            for(int i = 0; i < map.numberOfLayers; i++)
            {
                for(int j = 0; j < map.numberOfTilesY; j++)
                {
                    for (int k = 0; k < map.numberOfTilesX; k++) {
                        dos.writeBoolean(map.tiles[i][j][k]!= null);
                        if(map.tiles[i][j][k]!=null){
                            dos.writeInt(map.tiles[i][j][k].getLine());
                            dos.writeInt(map.tiles[i][j][k].getColumn());
                        }
                    }
                }
            }
            dos.writeInt(map.objects.size());
            for(BaseObject b:map.objects)
            {                
                b.save(dos);
            }
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Move the window to an specified point. Check moveY and moveX
    */
    public void moveWindow(Vector2D point)
    {
        int x = (int) point.x;
        int y = (int) point.y;
        
        int midX = windowWidth / 2;
        int midY = windowHeight / 2;   
        int windowStartX  = x - midX;
        int windowEndX = x + midX;
        
        int windowStartY = y - midY;
        int windowEndY = y + midY;
        
        if(windowStartX < 0 )
        {
            windowStartX = 0;
            windowEndX = windowWidth;
        }
        
        if(windowEndX >= mapWidth)
        {
            windowEndX = mapWidth -1;
            windowStartX = mapWidth -1 -1 - windowWidth;
        }
        
        //move y.
        if(windowStartY < 0)
        {
            windowStartY = 0;
            windowEndY = windowHeight-1;
        }
        if(windowEndY >= mapHeight)
        {
            windowEndY = mapHeight -1;
            windowStartY = mapHeight -1 - windowHeight;
        }
        if(moveY){
            upperRightWindowPoint.y = windowEndY;
            lowerLeftWindowPoint.y = windowStartY;
        }
        
        if(moveX){
            upperRightWindowPoint.x = windowEndX;
            lowerLeftWindowPoint.x = windowStartX;
        }
    }
    
    /**
     * Add an object to this map.
     * @param obj 
     */
    public void addObject(BaseObject obj)
    {
        objects.add(obj);
        if(obj.getName()!= null)
        {
            objectsPerName.put(obj.getName(), obj);
        }
        else
            objectsPerName.put(obj.toString(), obj);
    }
    
    /**
     * Get an object in the map by its name.
     * @param name
     * @return 
     */
    public BaseObject getObjectByName(String name)
    {
        if(objectsPerName.containsKey(name))
            return objectsPerName.get(name);
        else
            return null;
    }
    
    
    public Vector<BaseObject> getObjects()    
    {
        return this.objects;
    }
    
    /** 
     * Return the objects in map that are at maxDist from the origin
    */
    public Vector<BaseObject> getObjects(Vector2D origin, double maxDist)
    {
        Vector<BaseObject> ret = new Vector<BaseObject>();
        for(BaseObject b:objects)
            if(b.getPosition().dist(origin) < maxDist)
                ret.add(b);
        return ret;
    }
    
    
    public void update(double t)
    {
        if(background!= null)
            background.update(t);
        if(foreground!= null)
            foreground.update(t);
        if(objects!= null)
        {
            for (BaseObject b:objects)
                b.update(t);
        }
    }
    public void setWindowSize(int windowWidth, int windowHeight)
    {
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        lowerLeftWindowPoint = new Vector2D(0,0);
        upperRightWindowPoint = new Vector2D(windowWidth,windowHeight);
    }
    
    /**
     * Render actual layer to g
     * @param g 
     * @param layer if layer == -1, render all layers
     */
    public void render(Graphics2D g, int layer)
    {
        
        if(background!= null)
            background.render(g);
        int windowStartY = (int) lowerLeftWindowPoint.y;
        int windowEndY   = (int) upperRightWindowPoint.y;        
        
        int windowStartX = (int) lowerLeftWindowPoint.x;
        int windowEndX = (int) upperRightWindowPoint.x;
        
        //RENDER SCENARY.
        int tStartY = (mapHeight - windowEndY)   / tileset.getTileSize();
        int tEndY   = (mapHeight - windowStartY) / tileset.getTileSize() -1;
        if(tEndY < numberOfTilesY-1)
            tEndY++;
        int tStartX = windowStartX / tileset.getTileSize();
        int tEndX   = windowEndX   / tileset.getTileSize();
        
        int dX = windowStartX % tileset.getTileSize();
        int dY = (mapHeight - windowEndY)%tileset.getTileSize();
        
        int nTY = tEndY - tStartY;
        int nTX = tEndX - tStartX;
        
        
        if(layer == -1)
        {
            
            for(int i = 0; i <= nTY; i++)
            {
                for(int j = 0; j <= nTX; j++)
                {

                    if(tiles[0][tStartY + i][tStartX + j]!= null)
                    {
                        g.drawImage(tiles[0][tStartY + i][tStartX + j].getImg(), j*tileset.getTileSize() - dX, i * tileset.getTileSize() - dY, null);
                    }
                }
            }
            
            for(BaseObject n:objects)
            {
               
                if(n.isVisible() && n.isAlive())
                    n.render(g);
            }
            
            for(int i = 0; i <= nTY; i++)
            {
                for(int j = 0; j <= nTX; j++)
                {
                    if(tiles[1][tStartY + i][tStartX + j]!= null)
                    {
                        g.drawImage(tiles[1][tStartY + i][tStartX + j].getImg(), j*tileset.getTileSize() - dX, i * tileset.getTileSize() - dY, null);
                    }
                }
            }
        }
        else
        {
            for(int i = 0; i <= nTY; i++)
            {
                for(int j = 0; j <= nTX; j++)
                {
                    if(tiles[layer][tStartY + i][tStartX + j]!= null)
                    {
                        g.drawImage(tiles[layer][tStartY + i][tStartX + j].getImg(), j*tileset.getTileSize() - dX, i * tileset.getTileSize() - dY, null);
                    }
                }
            }
            for(BaseObject n:objects)
            {
                if(n.isVisible() && n.isAlive())
                    n.render(g);
            }
        }
        if(foreground!=null)
            foreground.render(g);
    }

    /**
     * Render the entire map to g
     * @param g
     * @param layer 
     */
    public void previewMap(Graphics g, int layer)
    {
        for(int i = 0; i < numberOfTilesY; i++)
        {
            for(int j = 0; j < numberOfTilesX; j++)
            {
                if(layer == -1){
                    if(tiles[0][ i][ j]!= null)
                    {
                        g.drawImage(tiles[0][i][j].getImg(), j*tileset.getTileSize(), i * tileset.getTileSize(), null);
                    }
                    if(tiles[1][i][j]!= null)
                    {
                        g.drawImage(tiles[1][i][j].getImg(), j*tileset.getTileSize(), i * tileset.getTileSize(), null);
                    }
                }
                else
                    if(tiles[layer][i][j]!= null)
                    {
                        g.drawImage(tiles[layer][i][j].getImg(), j*tileset.getTileSize() , i * tileset.getTileSize(), null);
                    }
            }
        }
    }
    
    public void setTile(int layer, Tile t, int l, int c)
    {
        tiles[layer][l][c] = t;
    }
    
    public Tile getTile(int layer, int line, int col)
    {
        if(line < numberOfTilesY && col < numberOfTilesX && line >=0 && col >= 0)
        {
            return tiles[layer][line][col];
        }
        return null;
    }
    
    public double getGravity()
    {
        return gravity;
    }
    
    public void setGravity(double gravity)
    {
        this.gravity = gravity;
    }
    
    public Background getBackground() {
        return background;
    }

    public Background getForeground() {
        return foreground;
    }

    public Tileset getTileset() {
        return tileset;
    }
    
    public int getWindowHeight() {
        return windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getNumberOfTilesX() {
        return numberOfTilesX;
    }

    public int getNumberOfTilesY() {
        return numberOfTilesY;
    }

    public boolean isMoveX() {
        return moveX;
    }

    public boolean isMoveY() {
        return moveY;
    }

   
    public void setBackground(Background background) {
        this.background = background;
    }

    public void setForeground(Background foreground) {
        this.foreground = foreground;
    }

    public void setMoveX(boolean moveX) {
        this.moveX = moveX;
    }

    public void setMoveY(boolean moveY) {
        this.moveY = moveY;
    }

    public Vector2D getLowerLeftWindowPoint() {
        return lowerLeftWindowPoint;
    }

    public Vector2D getUpperRightWindowPoint() {
        return upperRightWindowPoint;
    }
    
    
}
