package core.gameobject;

import core.Config;
import core.DataReaderWriter;
import core.map.Map;
import core.map.Tile;
import core.phisics.CollisionBox;
import core.phisics.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

/**
 * This is the base class for all game objetcs, such as playable characteres, 
 * enemies and power ups.
 * @author Panca
 */
public abstract class BaseObject implements Serializable{    
    //used to change the object facing.
    public static final int FACING_RIGHT = 0;
    public static final int FACING_LEFT = 1;
    
    protected boolean climbing;
    
    protected int facingSide;
    protected boolean onFloor;
    protected boolean onCeil;
    protected Vector2D position;
    protected Animation currentAnimation;
    protected Map mapReference;
    protected boolean  collideLeft, collideRight;
    
    protected Vector2D displacement;
    protected Sprite currentSprite;
    protected boolean alive;
    protected String name;
    //tile the character is stepping
    protected int tileSteppingType;
    //is the object on top of something?
    protected boolean onTopOfSomething;
    
   public BaseObject()
   {
       position = new Vector2D(); 
       displacement = new Vector2D();
   }
   
   
   //used to pull other BaseObject
   public void pull(BaseObject obj)
   {
       
   }
   
   
   public void putOnTop(BaseObject obj)
   {
       
       CollisionBox cb= obj.getCurrentSprite().getScenaryCollisionBox().centerBottomAt(obj.getPosition());
       onTopOfSomething = true;
       
       
      
       double y = cb.getUpperLeftPoint().y;//+currentSprite.getScenaryCollisionBox().getLowerLeftPoint().y;
       
       Vector2D n = new Vector2D(position.x, y);
       CollisionBox cb2 = currentSprite.getScenaryCollisionBox().centerBottomAt(n);
       
       position.y = cb2.getLowerLeftPoint().y;
      
       
   }
   
   /**
    * Update the object state based on the time passed from the last update.
    * @param elapsedTime  in seconds.
    */
   public abstract void update(double elapsedTime);
   
   /**
    * Use this method to set the object new position. It will take care of
    * cenary collision.
    * @param x
    * @param y 
    */
    public void setNewPosition(double x, double y)
    {
        
        displacement.x = x - position.getX();
        if(displacement.x != 0){
            position.setX(x);
            handleHorizontalCollision(displacement.x);
        }
        displacement.y = y-position.getY();
        
        
        if(displacement.y != 0){
            position.setY(y);
            handleVerticalCollision(displacement.y);
        }
        tileSteppingType = Tile.TERRAIN_TYPE_EMPTY;
        int tiley = mapReference.getNumberOfTilesY()- 1 - (int) (position.getY() / mapReference.getTileset().getTileSize());
        int tilex = (int) (position.getX() / mapReference.getTileset().getTileSize());
        if(tiley >=0 && tiley < mapReference.getNumberOfTilesY() && tilex>=0 && tilex < mapReference.getNumberOfTilesX())
        {
            if(mapReference.getTile(0, tiley, tilex)!= null)
                tileSteppingType = mapReference.getTile(0, tiley, tilex).getTerrainType();
        }
    }
   
   /**
     * Render character to and arbitrary thing'
     * the Graphics2D object is an abstraction to draw into an image, JPanel,
     * JFrame, etc. Render the current window view.
     */
    public void render(Graphics2D g)
    {
        if(currentAnimation == null)
        {
            System.out.println("ERROR. MUST SET currentAction");
            System.exit(2);
        }
        
        CollisionBox actual = currentAnimation.getCurrentSprite().getScenaryCollisionBox();
       // CollisionBox cb = actual.getTranslated(position);//center the box to the position point.
        CollisionBox cb = actual.centerBottomAt(position);//center the box to the position point.
        Image img = currentAnimation.getCurrentSprite().getImage();
        double mid =  (cb.getUpperRightPoint().getX() - cb.getLowerLeftPoint().getX())/2;
        
        //image x
        int px = (int) (position.getX() - mid - actual.getImageReferenceX());
        px = (int) (px - mapReference.getLowerLeftWindowPoint().x);
        
        int py = (int)(position.getY() - mapReference.getLowerLeftWindowPoint().y- actual.getImageReferenceY()); // window intert position
        py = mapReference.getWindowHeight() - py - img.getHeight(null) ;
         //finally draw img
        g.drawImage(img, px, py,null);
        
        if(true)
            return;
        //TODO: REMOVER
        // System.out.println("NAME> "+name);
        //debug
        //TODO: remove debug
        /*System.out.println("SCY: "+cb.getLowerLeftPoint().x+","+cb.getLowerLeftPoint().y
                               +"->"+cb.getUpperRightPoint().x+","+cb.getUpperRightPoint().y);*/
        cb.renderBox(g, mapReference, Color.yellow);
        
        
        
        for(CollisionBox cb3:currentSprite.getVulnerableCollisionBox()){
            CollisionBox cb2 = cb3.getTranslated(position);
            
            cb2.renderBox(g, mapReference, Color.RED);
        }
        
        
        for(CollisionBox cb3:currentSprite.getAttackCollisionBox()){
            CollisionBox cb2 = cb3.getTranslated(position);
            
            cb2.renderBox(g, mapReference, Color.BLUE);
        }
        
        
       
    }

    public void handleHorizontalCollision(double displacementX){
        this.collideLeft = false;
        this.collideRight  = false;
        CollisionBox translated = currentSprite.getScenaryCollisionBox().centerBottomAt(position);
        //ponto superior esquerdo transladado.
        Vector2D pse = translated.getUpperLeftPoint(); 
        //ponto inferior esquerdo
        Vector2D pie = translated.getLowerLeftPoint();
        //ponto inferior direito
        Vector2D pid = translated.getLowerRightPoint();
        int tYStart = mapReference.getNumberOfTilesY()- 1 
			- (int) (pse.y / mapReference.getTileset().getTileSize());
        int tYEnd   = mapReference.getNumberOfTilesY()- 1 
			- (int) (pie.y / mapReference.getTileset().getTileSize());
        boolean takeCareOfCollison = true;
        if(Config.TEST_PIXEL_COLLISION)
        {
            if(displacementX <0)
            {
                int tileX = (int)pie.getX() / mapReference.getTileset().getTileSize(); 
                //////////
                //test left lower tile..
                if(tYStart <0 || tYEnd > mapReference.getNumberOfTilesY())
                    return;
                if(tileX < 0 || tileX > mapReference.getNumberOfTilesX())
                    return;
                Tile t = mapReference.getTile(0, tYStart, tileX);
                //go to next tile
                if(t !=null && t.getTerrainType() == Tile.TERRAIN_TYPE_HILL)
                {
                   
                    boolean[][] tileCollision = t.getTileCollision();

                    int l = 0; 
                    int c = 0;

                    if(displacementX<0){
                        l = (int)pie.getX()%mapReference.getTileset().getTileSize();
                        c = (int)pie.getY()%mapReference.getTileset().getTileSize();
                        if(tileCollision[l][c])
                        {
                            int s = 0;
                            for(s = 0; s < Config.TILE_MAX_PIXELS_TO_GO_UP+1 && !tileCollision[l][c+s]; s++)
                            {

                            }
                            //go up
                            if(s <= Config.TILE_MAX_PIXELS_TO_GO_UP){
                                position.y += s;
                                climbing = true;
                            }
                            else
                                takeCareOfCollison = false;
                        }
                    }
                }
            }
            else if(displacementX > 0)
            {
                int tileX = (int)pid.getX() / mapReference.getTileset().getTileSize(); 
                //////////
                //test left lower tile..
                if(tYStart <0 || tYEnd > mapReference.getNumberOfTilesY())
                    return;
                if(tileX < 0 || tileX > mapReference.getNumberOfTilesX())
                    return;
                Tile t = mapReference.getTile(0, tYStart, tileX);
                //go to next tile
                if(t !=null && t.getTerrainType() == Tile.TERRAIN_TYPE_HILL)
                {
                    
                    boolean[][] tileCollision = t.getTileCollision();

                    int l = 0; 
                    int c = 0;

                    if(displacementX<0){
                        l = (int)pie.getX()%mapReference.getTileset().getTileSize();
                        c = (int)pie.getY()%mapReference.getTileset().getTileSize();
                        if(tileCollision[l][c])
                        {
                            int s = 0;
                            for(s = 0; s < Config.TILE_MAX_PIXELS_TO_GO_UP+1 && !tileCollision[l][c+s]; s++)
                            {

                            }
                            //go up
                            if(s <= Config.TILE_MAX_PIXELS_TO_GO_UP){
                                position.y += s;
                                climbing = true;
                            }
                            else
                                takeCareOfCollison = false;
                        }
                    }
                }
            }
            //return;
        }
        
        
        if(!takeCareOfCollison)
            return;
        
        //-->
        if(displacementX > 0)
        {
            boolean ended = false;
            int tileX = (int)pid.getX() / mapReference.getTileset().getTileSize();
            for(int tileY = tYStart; tileY <= tYEnd && !ended; tileY++)
            {    
                if(mapReference.getTile(0,tileY, tileX)!=null && !mapReference.getTile(0, tileY, tileX).passFromLeft())
                {                    
                    //move back to left
                    int p = (tileX)*mapReference.getTileset().getTileSize()-1;
                    position.x = p - translated.getWidth()/2;
                    this.collideRight = true;
                    ended = true;//houve uma colisao, nao precisa testar mais tiles.
                }                
            }
        }
        //<--
        else if(displacementX < 0)
        {
            boolean ended = false;
            int tileX = (int)pie.getX() / mapReference.getTileset().getTileSize();            
            for(int tileY = tYStart; tileY <= tYEnd && !ended; tileY++)
            {
                Tile t = mapReference.getTile(0, tileY, tileX);
                //go to next tile
                if(t !=null  && !mapReference.getTile(0, tileY, tileX).passFromRight() )
                {
                    //lowe left point x.
                    int llx = (tileX+1)*mapReference.getTileset().getTileSize();
                    //position = llx - box width
                    position.x = llx + translated.getWidth()/2;
                    //position.x = (tileX+1)*mapReference.getTileset().getTileSize();
                    this.collideLeft= true;
                    ended = true;
                    
                }
            }     
        }
    }
    
    public void handleVerticalCollision(double displacementY){
        if(climbing || onTopOfSomething )
        {
            onFloor = true;
            return;
        }
        //ponto superior esquerdo transladado.
        CollisionBox translated = currentSprite.getScenaryCollisionBox().centerBottomAt(position);
        
       //ponto superior esquerdo transladado.
        Vector2D pse = translated.getUpperLeftPoint(); 
        //ponto inferior esquerdo
        Vector2D pie = translated.getLowerLeftPoint();
        //ponto inferior direito
        Vector2D pid = translated.getLowerRightPoint();
        
        //ponto superior direito
        Vector2D psd = translated.getUpperRightPoint();
        
        int tXStart = (int)pie.getX() / mapReference.getTileset().getTileSize();
        int tXEnd   = (int)pid.getX() / mapReference.getTileset().getTileSize();
        
        this.onCeil = false;
        this.onFloor = false;
        //o personagem foi para cima
	if(displacementY > 0) {
            boolean ended = false;
            int tileY = mapReference.getNumberOfTilesY()- 1 - (int) (pse.getY() / mapReference.getTileset().getTileSize());
            for(int tileX = tXStart; tileX<=tXEnd && !ended; tileX++){    
                if(mapReference.getTile(0,tileY, tileX)!=null && !mapReference.getTile(0,tileY, tileX).passFromDown()){
                    int newY = (tileY+1) *mapReference.getTileset().getTileSize();
                    newY = mapReference.getMapHeight()- newY -1;
                    int altura = (int) (psd.y - pid.y);
                    position.y = newY - altura;
                    ended = true;
                    this.onCeil = true;
                }
            }
        }
		// o personagem foi para baixo
        else if(displacementY < 0) {
            boolean ended    = false;
            int tileY = mapReference.getNumberOfTilesY()- 1 - (int) (pie.getY() / mapReference.getTileset().getTileSize());
            for(int tileX = tXStart; tileX<=tXEnd && !ended; tileX++){    
                if(mapReference.getTile(0 , tileY, tileX)!=null && !mapReference.getTile(0, tileY, tileX).passFromUp()){
                    int novoY = (tileY-1) *mapReference.getTileset().getTileSize();
                    novoY = mapReference.getMapHeight()- novoY - mapReference.getTileset().getTileSize();
                    position.setY(novoY);
                    ended = true; 
                    onFloor = true;
                    
                }
            }
        }
    }

    /**
     * Is the object in the visible window of the scenary?
     * @return 
     */
    public boolean isVisible() {
        if(position.x+currentSprite.getImage().getWidth(null) > mapReference.getLowerLeftWindowPoint().x 
        && position.x < mapReference.getUpperRightWindowPoint().x
        && position.y + currentSprite.getImage().getHeight(null) >=0 && position.y < mapReference.getUpperRightWindowPoint().y)
            return true;
        return false;
    }

    public int getTileSteppingType() {
        return tileSteppingType;
    }
    
    /**
     * Saves the class name and the position of the object inside the map.
     * 
     * @param dos 
     */
    public void save(DataOutputStream dos)
    {
        try {
            DataReaderWriter.writeString(this.getClass().getName(), dos);  
            DataReaderWriter.writeString(name, dos);
            dos.writeDouble(position.x);
            dos.writeDouble(position.y);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }       
    
    public static BaseObject load(DataInputStream dis)
    {
        BaseObject b = null;
        try {
            String className = DataReaderWriter.readString(dis);
            b = (BaseObject) Class.forName(className).newInstance();
            String name = DataReaderWriter.readString(dis);
            b.setName(name);
            Vector2D pos = new Vector2D();
            pos.x = dis.readDouble();
            pos.y = dis.readDouble();
            b.position = pos;
        }
        catch (Exception e) {           
            e.printStackTrace();
        }
        
        return b;
    }
    
    
     public void apllyGravity(double t){
        setNewPosition(position.x, position.y-this.mapReference.getGravity()*t);
    }

    public int getFacingSide() {
        return facingSide;
    }
    public String getName()
    {
        return this.name;
    }
    public boolean isOnFloor() {
        return onFloor;
    }

    public boolean isOnCeil() {
        return onCeil;
    }

    public Vector2D getPosition() {
        return position;
    }

    public boolean isCollideLeft() {
        return collideLeft;
    }

    public boolean isCollideRight() {
        return collideRight;
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setFacingSide(int facingSide) {
        this.facingSide = facingSide;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setMapReference(Map mapReference) {
        this.mapReference = mapReference;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setName(String name){
        this.name = name;
    } 
    
}
