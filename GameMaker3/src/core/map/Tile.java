/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
/**
 *
 * @author panca
 */
public class Tile implements Serializable{
    
    
    //types of cell. Filled with..
    public static final int TERRAIN_TYPE_EMPTY      = 0; 
    public static final int TERRAIN_TYPE_GROUND     = 1;
    public static final int TERRAIN_TYPE_WATER      = 2;
    public static final int TERRAIN_TYPE_SLIPPERY   = 3;//icy, gelly, whatever
    public static final int TERRAIN_TYPE_POISON     = 4; //take a portion of life
    public static final int TERRAIN_TYPE_KILL       = 5; // kill character
    public static final int TERRAIN_TYPE_HILL       = 6; // kill character
    public static final int TERRAIN_TYPE_CUSTOM1    = 7;
    public static final int TERRAIN_TYPE_CUSTOM2    = 8; 
    public static final int TERRAIN_TYPE_CUSTOM3    = 9; 
    
    
    /**
     * Line and columns of tileset.
     */
    private int line;
    private int column;
    
    //img of tile
    private transient Image img;
    private transient boolean[][] tileCollision;
    
    private int terrainType;
    
    //directions wich the character/enemies can pass
    private boolean fromLeft, fromRight, fromDown, fromUp;
    
    public Tile()
    {
        img = null;
        terrainType = TERRAIN_TYPE_EMPTY;
    }
    
    public Tile(Image img, int line, int column)
    {
        this.line  = line;
        this. column = column;
        this.img = img;
        BufferedImage bi = (BufferedImage) img;
        tileCollision = new boolean[img.getHeight(null)][img.getWidth(null)];
        for(int i = 0; i < img.getHeight(null); i++)
        {
            for(int j = 0; j < img.getWidth(null); j++)
            {
                int alpha2 = (bi.getRGB(j,i) >> 24) & 0xFF;
                if (alpha2 > 0)
                    tileCollision[i][j] = true;
            }
        }
        
        fromDown = true;
        fromLeft = true;
        fromRight = true;
        fromUp = true;
    }
    
    
    public void save(DataOutputStream dos)
    {
        try {
            dos.writeInt(line);
            dos.writeInt(column);
            dos.writeInt(terrainType);
            dos.writeBoolean(fromLeft);
            dos.writeBoolean(fromRight);
            dos.writeBoolean(fromDown);
            dos.writeBoolean(fromUp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Tile load(DataInputStream dis)
    {
        Tile t= null;
        try {
            t = new Tile();
            t.line = dis.readInt();
            t.column = dis.readInt();
            t.terrainType = dis.readInt();
            t.fromLeft = dis.readBoolean();
            t.fromRight = dis.readBoolean();
            t.fromDown  = dis.readBoolean();
            t.fromUp    = dis.readBoolean();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
        
    }
    
    public void init()
    {
        
    }
    
    public boolean[][] getTileCollision()
    {
        return tileCollision;
    }
    
    ///Getters and Setters
    
    public Image getImg()
    {
        return this.img;
    }

    public void setImg(Image img) {
        this.img = img;
        BufferedImage bi = (BufferedImage) img;
        tileCollision = new boolean[img.getHeight(null)][img.getWidth(null)];
        for(int i = 0; i < img.getHeight(null); i++)
        {
            for(int j = 0; j < img.getWidth(null); j++)
            {
                int alpha2 = (bi.getRGB(j,i) >> 24) & 0xFF;
                if (alpha2 > 0)
                    tileCollision[i][j] = true;
            }
        }
    }
    
    public boolean passFromLeft()
    {
        return fromLeft;
    }
    
    public boolean passFromRight()
    {
        return fromRight;
    }
    
    public boolean passFromDown()
    {
        return fromDown;
    }
    
    public boolean passFromUp()
    {
        return fromUp;
    }
    
    public void setPassFromLeft(boolean left)
    {
        fromLeft = left;
    }
    
    public void setPassFromRight(boolean right)
    {
        fromRight = right;
    }
    
    public void setPassFromUp(boolean up)
    {
        fromUp = up;
    }
    
    public void setPassFromDown(boolean down)
    {
        fromDown = down;
    }

    public int getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(int terrainType) {
        this.terrainType = terrainType;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
