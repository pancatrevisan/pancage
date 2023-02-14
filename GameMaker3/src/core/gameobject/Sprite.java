package core.gameobject;
import core.DataReaderWriter;
import core.graphics.ImageBank;
import core.phisics.CollisionBox;
import core.phisics.CollisionResult;
import core.phisics.Vector2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

/**
 *
 * @author panca
 */
public class Sprite{
    private String imageName;
    
    
    //do not serialize
    private transient Image image;
    
    private double time;
    private CollisionBox scenaryCollisionBox;
    
    private Vector<CollisionBox> attackCollisionBox;
    private Vector<CollisionBox> vulnerableCollisionBox;
    
    public Sprite()
    {
        attackCollisionBox      = new Vector<CollisionBox>();
        vulnerableCollisionBox  = new Vector<CollisionBox>();
    }
    
    public Sprite(String imgName, double time)
    {
        this.imageName = imgName;
        this.time = time;
        init();
        attackCollisionBox      = new Vector<CollisionBox>();
        vulnerableCollisionBox  = new Vector<CollisionBox>();
    }
    
    public void setImageName(String imgName)
    {
        this.imageName = imgName;
        init();
    }
    
    public void setTime(double time)
    {
        this.time = time;
    }
    
    public Image getImage(){
        
        //when the sprite is loaded, this attribute may not be initialized
        if(image == null)
            this.image = ImageBank.getImage(imageName);
        return image;
    }
    
    public CollisionBox getScenaryCollisionBox()
    {
        return scenaryCollisionBox;
    }

    public void setScenaryCollisionBox(CollisionBox scenaryCollisionBox) {
        this.scenaryCollisionBox = scenaryCollisionBox;
        
        /* For some reason, it is necessary to store the distance from the boxes 
            to the scenary collision box lower left point
        */
        Vector2D p = new Vector2D(scenaryCollisionBox.getLowerLeftPoint().getX()+scenaryCollisionBox.getWidth()/2,
                                   scenaryCollisionBox.getLowerLeftPoint().getY());
        for(CollisionBox cb:attackCollisionBox)
        {
            cb.setDistanceFromP(cb.getLowerLeftPoint().x-p.getX(), cb.getLowerLeftPoint().y-p.getY());
        }
        for(CollisionBox cb:vulnerableCollisionBox)
        {
            cb.setDistanceFromP(cb.getLowerLeftPoint().x-p.getX(), cb.getLowerLeftPoint().y-p.getY());
        }
    }

    public double getTime() {
        return time;
    }

    public String getImageName() {
        return imageName;
    }

    public Vector<CollisionBox> getAttackCollisionBox()
    {
        return attackCollisionBox;
    }
    public Vector<CollisionBox> getVulnerableCollisionBox()
    {
        return vulnerableCollisionBox;
    }

    
    
    public Sprite getFlipped()
    {
        Sprite s = new Sprite();
        // Flip the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        //BufferedImage dest = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);

        s.image = op.filter((BufferedImage)image, null);
        s.imageName = this.imageName +"";//copy
        s.time = this.time;
        Sprite thisSprite = this;
        int imgw = thisSprite.image.getWidth(null);
        Vector2D scbll = new 
            Vector2D(imgw - getScenaryCollisionBox().getUpperRightPoint().x, getScenaryCollisionBox().getLowerLeftPoint().y) ;
        Vector2D scbur = new 
            Vector2D(imgw - getScenaryCollisionBox().getLowerLeftPoint().x, getScenaryCollisionBox().getUpperRightPoint().y) ;
        
        CollisionBox cen = new CollisionBox(scbur, scbll);
        /*Vector2D p = new Vector2D(cen.getLowerLeftPoint().getX()+cen.getWidth()/2,
                                   cen.getLowerLeftPoint().getY());*/
        //s.setScenaryCollisionBox(cen);
        for(CollisionBox cb:attackCollisionBox)
        {
            Vector2D ll = new 
            Vector2D(imgw - cb.getUpperRightPoint().x, cb.getLowerLeftPoint().y) ;
            Vector2D ur = new 
            Vector2D(imgw - cb.getLowerLeftPoint().x, cb.getUpperRightPoint().y) ;
            
            
            CollisionBox ca = new CollisionBox(ur, ll);
            //ca.setDistanceFromP(ca.getLowerLeftPoint().x-p.getX(), ca.getLowerLeftPoint().y-p.getY());
            s.attackCollisionBox.add(ca);
            
        }
        for(CollisionBox cb:vulnerableCollisionBox)
        {
            Vector2D ll = new 
            Vector2D(imgw - cb.getUpperRightPoint().x, cb.getLowerLeftPoint().y) ;
            Vector2D ur = new 
            Vector2D(imgw - cb.getLowerLeftPoint().x, cb.getUpperRightPoint().y) ;
            
            /*Vector2D ur = new Vector2D(thisSprite.image.getWidth(null) - cb.getUpperRightPoint().x - cb.getWidth(), cb.getUpperRightPoint().y) ;
            Vector2D ll = new Vector2D(thisSprite.image.getWidth(null) - cb.getLowerLeftPoint().x - cb.getWidth(), cb.getLowerLeftPoint().y);*/
            CollisionBox ca = new CollisionBox(ur, ll);
            //ca.setDistanceFromP(ca.getLowerLeftPoint().x-p.getX(), ca.getLowerLeftPoint().y-p.getY());
            s.vulnerableCollisionBox.add(ca);
        }
        s.setScenaryCollisionBox(cen);
        return s;
    }
    
    public static Sprite load(DataInputStream dis)
    {
        Sprite s = null;
        try {
            String name = DataReaderWriter.readString(dis);
            double time = dis.readDouble();
            
            s = new Sprite(name, time);
            s.init();
            int atkbox = dis.readInt();
            for(int i = 0; i < atkbox; i++)
            {
                CollisionBox cb = CollisionBox.load(dis);
                s.getAttackCollisionBox().add(cb);
            }
            int vulbox = dis.readInt();
            for(int i = 0; i < vulbox; i++)
            {
                CollisionBox cb = CollisionBox.load(dis);
                s.getVulnerableCollisionBox().add(cb);
            }
            CollisionBox scenaryCB = CollisionBox.load(dis);
            s.setScenaryCollisionBox(scenaryCB);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return s;
    }
    
    public void save(DataOutputStream dos)
    {
        try
        {
            DataReaderWriter.writeString(imageName, dos);
            dos.writeDouble(time);
            dos.writeInt(attackCollisionBox.size());
            for(CollisionBox cb:attackCollisionBox)
            {
                cb.save(dos);
            }
            dos.writeInt(vulnerableCollisionBox.size());
            for(CollisionBox cb:vulnerableCollisionBox)
            {
                cb.save(dos);
            }
            scenaryCollisionBox.save(dos);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void init() {
        this.image = ImageBank.getImage(imageName);
        if(image == null)
        {
            ImageBank.putImage(imageName);
            image = ImageBank.getImage(imageName);
        }
    }

    public String toString()
    {
        if(imageName != null)
            return "Sprite: " + imageName; //.substring(imageName.lastIndexOf("/"), imageName.length());
        else
            return "Sprite ";
    }
}
