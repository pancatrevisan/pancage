package core;

import java.io.File;

/**
 * Configuration of paths.
 * DO NOT MOVE THIS CLASS TO ANOTHER PACKAGE. LEAVE AS IT IS!
 */
public class Config {
    
    /**
     * The game main package. You can Edit It
     */
    public static final String PROJECT_PACKAGE = "mygame";
    
    
    
    
    /*
        Configs for pixel collision. NOT IN USE
    */
    public static final int TILE_MAX_PIXELS_TO_GO_UP = 3;
    public static final boolean TEST_PIXEL_COLLISION = true;
    
    /***************************************************************************/
    /******************************DO NOT CHANGE********************************/
    
    /**
     * File extension for maps.
     */
    public static final String FILE_EXT_MAP = ".map";
    
    /**
     * File extension for animations.
     */
    public static final String FILE_EXT_ANIM = ".ani";
    
    /**
     * The game resource path. Do not change!
     */
    public static final String RESOURCE_PATH ="gameresources";
    
    /**
     * ALL game images (sprites, bkg's, tileset's) 
     */
    public static final String IMAGE_PATH = RESOURCE_PATH+ File.separatorChar + "images";
    
    /**
     * Path to animations used by the objects. The animations can be created using
     * the editor.
     */
    public static final String ANIM_PATH = RESOURCE_PATH +File.separatorChar+"animations";
    
    /**
     * Path to music and sound effects.
     */
    public static final String SOUND_PATH = RESOURCE_PATH +File.separatorChar+"sounds";
    
    /**
     * Path to maps.
     */
    public static final String MAPS_PATH = RESOURCE_PATH +File.separatorChar+"maps";
    
    
    
    public static final String MUSICS_PATH = RESOURCE_PATH +File.separatorChar+"musics";
    /***************************************************************************/        
}
