package core;

import core.map.Map;
import core.sound.Music;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

/**
 * This class represents a game window. It takes care of rendering and the 
 * game loop process. The update and render methods are abstract, and the 
 * developer must implement it.
 * @author panca
 */
public abstract class GameWindow extends JPanel implements KeyListener{
    private double framesPerSecond;
    protected Graphics2D pincel;
    protected Image buffer;
    private Map mapReference;
    private int windowWidth, windowHeight, resolutionWidth, resolutionHeight;
    private boolean running;
    public GameWindow(int windowWidth, int windowHeight, 
            int resolutionWidth, int resolutionHeight, double framesPerSecond)
    {
        super();
        this.framesPerSecond = framesPerSecond;
        setPreferredSize(new Dimension(resolutionWidth, resolutionHeight));
        this.setFocusable(true);
        this.addKeyListener(this);
        this.windowHeight = windowHeight;
        this.windowWidth  = windowWidth;
        
        this.resolutionHeight = resolutionHeight;
        this.resolutionWidth = resolutionWidth;
        
    }
    
    /**
     *
     * @param resolutionWidth
     * @param resolutionHeight
     * @param framesPerSecond
     */
    public GameWindow(int resolutionWidth, int resolutionHeight, double framesPerSecond)
    {
        super();
        
        this.framesPerSecond = framesPerSecond;
         this.resolutionHeight = resolutionHeight;
        this.resolutionWidth = resolutionWidth;
        setPreferredSize(new Dimension(resolutionWidth, resolutionHeight));
        this.setFocusable(true);
        this.addKeyListener(this);
    }
    public void start()
    {
        running = true;
        if(mapReference!= null)
        {
            if(mapReference.getMusic()!=null){
                Music.play(mapReference.getMusic());
            }
        }
        gameLoop();   
    }

    public Map getMapReference() {
        return mapReference;
    }
    
    /**
     * The window needs a map reference.
     * @param mapReference 
     */
    public void setMapReference(Map mapReference) {
        this.mapReference = mapReference;
        this.windowHeight = mapReference.getWindowHeight();
        this.windowWidth = mapReference.getWindowWidth();
    }
    
    
    /**
     * The game is updated. The time passed from the last frame is t seconds.
     * @param t Time passed from last frame. In seconds.
     */
    public abstract void  update(double t);
    /**
     * Render grapgics to the window graphics context g
     * @param g 
     */
    public abstract void  render(Graphics2D g); 
    
    /**
     * Gameloop based on the book Black java game 
     * Black Art of Java Game Programming.
     */
    public void gameLoop()
    {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        //nanoTime has more precision.
        beforeTime = System.nanoTime();
        //tempo de duração de cada frame.
        long period = (long) (1000.0/framesPerSecond);
        period = period*1000000L;
        /* Number of frames with a delay of 0 ms before the animation thread 
         * yields to other running threads. */
        int NO_DELAYS_PER_YIELD = 5;
        int MAX_FRAME_SKIPS = 5;
        int numFrames = 0;
        int frames = 0;
        long antTime = System.currentTimeMillis();
        
        double inc = 1.0/framesPerSecond;
        while(running) 
        {
            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;
            frames++;

            if(System.currentTimeMillis() - antTime >= 1000){
                
                numFrames = frames;
                inc = 1.0/numFrames;
                frames = 0;
                antTime = System.currentTimeMillis();
                System.out.println("FPS: "+numFrames);
            }
            update(inc);
            gameRender( );
            paintScreen( );
            if (sleepTime > 0){ // some time left in this cycle
                try{
                    Thread.sleep(sleepTime/1000000L); // nano -> ms
                }
                catch(InterruptedException ex){}
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            }
            else { // sleepTime <= 0; frame took longer than the period
                excess -= sleepTime; // store excess time value
                overSleepTime = 0L;
                if (++noDelays >= NO_DELAYS_PER_YIELD){
                    Thread.yield( ); // give another thread a chance to run
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
            /* If frame animation is taking too long, update the game state
            without rendering it, to get the updates/sec nearer to
            the required FPS. */
            int skips = 0;
            while((excess > period) && (skips < MAX_FRAME_SKIPS)){
                excess -= period;
                //gameUpdate(timeDiff); // update state but don't render
                update(inc);
                skips++;
            }
        }
    }
    
    /**
     * stops the game and stop music.
     */
    public void stopRunning()
    {
        running = false;
        if(mapReference!= null && mapReference.getMusic()!=null)
        {
            Music.stopPlay(mapReference.getMusic());
        }
        
    }
    
    /**
     * paint the screen.
     */
    private void gameRender(){
        if (buffer == null){
            buffer = createImage(windowWidth, windowHeight);
            if(buffer  == null){
                System.out.println("NULL BUFFER!");
                return;
            }
            else{
                pincel = (Graphics2D) buffer.getGraphics();
            }
        }
        if(pincel == null)
            return;
        pincel.setColor(Color.white);
        pincel.fillRect(0, 0, windowWidth, windowHeight);
        render(pincel);
        
    }
    //Draws the buffer on window
    private void paintScreen()
    {
        Graphics g;
        try{
            g = this.getGraphics();
            if( (g!=null) && (buffer != null))
                g.drawImage(buffer, 0, 0, resolutionWidth,resolutionHeight,null);
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception e){
            System.out.println("Graphics context error " + e);
        }
    }
}
