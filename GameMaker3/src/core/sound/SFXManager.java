package core.sound;

import java.util.Hashtable;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * A manager to hold all SFX of the game.
 * @author panca
 */
public class SFXManager {
    private static Hashtable<String, SFX> sfx = new Hashtable<String, SFX>();
    
    
    
    /**
     * Add an sfx to the manager.
     * @param name the key to obtain the sfx
     * @param sfxSound the SFX.
     */
    public static void addSFX(String name, SFX sfxSound){
        if(sfx.containsKey(name))
        {
            System.out.println("FILE ALREADY LOADED");
            return;
        }
        sfx.put(name, sfxSound);
    }
    
    /**
     * Play a sfx.
     * @param key  the SFX key/name
     */
    public static void playSFX(String key)
    {
        
        //if the sound is loaded
        if(sfx.containsKey(key)) {
            
            SFX s = sfx.get(key);
            
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, s.getFormat());
            SourceDataLine line = null;
            //play it
            try{
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(s.getFormat(),s.getSamples().length);
                line.start();
                line.write(s.getSamples(), 0, s.getSamples().length);
            } 
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
}
