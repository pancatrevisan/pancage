package core;

import java.io.File;

/**
 * Use this class to access data!
 * @author panca
 */
public class ResourceManager 
{
    public static final int RESSOURCE_TYPE_IMAGE = 1;
    public static final int RESSOURCE_TYPE_ANIMATION = 2;
    public static final int RESSOURCE_TYPE_SOUND = 3;
    public static final int RESSOURCE_TYPE_MAP = 4;
    public static final int RESSOURCE_TYPE_MUSIC = 5;
    public static final int RESSOURCE_TYPE_OTHER = 6;
    
    /**
     * Get a string to acess data in the game resources.
     * @param fileSubPath
     * @param resourceType
     * @return 
     */
    public static String createResourceString(String fileSubPath, int resourceType)
    {
        if(resourceType ==  RESSOURCE_TYPE_IMAGE){
            
            
            String a = Config.IMAGE_PATH+File.separatorChar+fileSubPath;
            
            return a;
        }
        else if(resourceType ==  RESSOURCE_TYPE_ANIMATION){
                return (Config.ANIM_PATH+File.separatorChar+fileSubPath);
        }
        else if(resourceType == RESSOURCE_TYPE_SOUND){
                return (Config.SOUND_PATH+File.separatorChar+fileSubPath);
        }
        else if(resourceType == RESSOURCE_TYPE_MAP){
                return (Config.MAPS_PATH+File.separatorChar+fileSubPath);
        }
        else if(resourceType == RESSOURCE_TYPE_MUSIC){
                return (Config.MUSICS_PATH+File.separatorChar+fileSubPath);
        }
        else{
                return (Config.RESOURCE_PATH+File.separatorChar+fileSubPath);
        }
    }
    
    /**
     * Create a new file in game resources.
     * @param fileSubPath
     * @param resourceType
     * @return 
     */
    public static File createFile(String fileSubPath, int resourceType)
    {
        try{
            File ressource = null; 
            
            if(resourceType ==  RESSOURCE_TYPE_IMAGE){
                ressource = new File(Config.IMAGE_PATH+File.separatorChar + fileSubPath);
            }
            else if(resourceType ==  RESSOURCE_TYPE_ANIMATION){
                ressource = new File(Config.ANIM_PATH+File.separatorChar + fileSubPath);
            }
            else if(resourceType == RESSOURCE_TYPE_SOUND){
                ressource = new File(Config.SOUND_PATH+File.separatorChar + fileSubPath);
            }
            else if(resourceType == RESSOURCE_TYPE_MAP){
                ressource = new File(Config.MAPS_PATH+File.separatorChar + fileSubPath);
            }
            else if(resourceType == RESSOURCE_TYPE_MUSIC){
                ressource = new File(Config.MUSICS_PATH+File.separatorChar + fileSubPath);
            }
            else
                ressource = new File(Config.RESOURCE_PATH+File.separatorChar + fileSubPath);
            
            
            
            return ressource;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
