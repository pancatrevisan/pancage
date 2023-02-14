/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.map;

import core.DataReaderWriter;
import core.graphics.ImageBank;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
/**
 *
 * @author panca
 */
public class Tileset{
    private Tile[][] tiles;
    private int tileSize;
    private String imageName;
    private transient Image image;
    public Tileset()
    {
        
    }
    
    public Tileset(Image image, int tileSize)
    {
        this.image = image;
        BufferedImage img = (BufferedImage) image;
        
        this.tileSize = tileSize;
        //cut img into many tiles.
        if( (img.getWidth() % tileSize!= 0) || (img.getHeight()%tileSize != 0))
        {
            System.out.println("ERROR: Tileset size incorrect");
            System.exit(1);
        }
        numTilesX = img.getWidth()/tileSize;
        numTilesY = img.getHeight()/tileSize;
        
        if(tiles == null)
            tiles = new Tile[numTilesY][numTilesX];
        
        for(int i = 0; i < numTilesY; i++)
        {
            for (int j = 0; j < numTilesX; j++) 
            {
                //cut a subimage.
               // tiles[i][j] = new Tile( i, j);
                if(tiles[i][j] == null)
                    tiles[i][j] = new Tile(img.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize), i, j);
                else
                    tiles[i][j].setImg(img.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize)); 
                
                tiles[i][j].init();
            }
        }
    }
    
    public String getImageName()
    {
        return imageName;
    }
    /**
     * This method initialize the tileset.
    */
    public void initTile(String imageName, int tileSize)
    {
        this.imageName = imageName;
        BufferedImage img = (BufferedImage) ImageBank.getImage(imageName);
        
        this.tileSize = tileSize;
        //BufferedImage img   null;//load image
        if(img == null)
        {
            ImageBank.putImage(imageName);
            img = (BufferedImage) ImageBank.getImage(imageName);
        }
        image = img;
        //cut img into many tiles.
        if( (img.getWidth() % tileSize!= 0) || (img.getHeight()%tileSize != 0))
        {
            System.out.println("ERROR: Tileset size incorrect");
            System.exit(1);
        }
        numTilesX = img.getWidth()/tileSize;
        numTilesY = img.getHeight()/tileSize;
        
        if(tiles == null)
            tiles = new Tile[numTilesY][numTilesX];
        
        for(int i = 0; i < numTilesY; i++)
        {
            for (int j = 0; j < numTilesX; j++) 
            {
                //cut a subimage.
               // tiles[i][j] = new Tile( i, j);
                if(tiles[i][j] == null)
                    tiles[i][j] = new Tile(img.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize), i, j);
                else
                    tiles[i][j].setImg(img.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize)); 
                
                tiles[i][j].init();
            }
        }
    }
    
    private int numTilesX;
    
    private int numTilesY;
    
    
    public Image getImage()
    {
        return image;
    }
    
    public void drawTileset(Graphics g)
    {
        for(int i = 0; i < getNumTilesY() ; i++)
        {
            for(int j = 0; j < getNumTilesX() ; j++) 
            {
                g.drawImage(getTile(i, j).getImg(), j * tileSize,i*tileSize, null);
            }
        }
    }
    
    
     public int getTileSize(){
        return tileSize;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getNumTilesX() {
        return numTilesX;
    }

    public void setNumTilesX(int numTilesX) {
        this.numTilesX = numTilesX;
    }

    public int getNumTilesY() {
        return numTilesY;
    }

    public void setNumTilesY(int numTilesY) {
        this.numTilesY = numTilesY;
    }
    
    public Tile getTile(int l, int c)
    {
        return tiles[l][c];
    }

    /**
     * Method called after load the object from a file.
     */
    
    public void init() {
       if(image == null) 
            initTile(imageName, tileSize);
    }
    
    public void save(DataOutputStream dos)
    {
        try {
            DataReaderWriter.writeString(imageName, dos);
            dos.writeInt(tileSize);
            for(int i = 0; i < getNumTilesY() ; i++){
                for(int j = 0; j < getNumTilesX() ; j++){
                    //save tile data
                    tiles[i][j].save(dos);
                }
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public static Tileset load(DataInputStream dis)
    {
        Tileset t = null;
        try {
            String imgName = DataReaderWriter.readString(dis);
            int tileSize   = dis.readInt();
            t = new Tileset();
            t.initTile(imgName,tileSize);
            for(int i = 0; i < t.getNumTilesY() ; i++){
                for(int j = 0; j < t.getNumTilesX() ; j++){
                    t.tiles[i][j] = Tile.load(dis);
                }
            }
            t.initTile(imgName, tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return t;
    }
}
