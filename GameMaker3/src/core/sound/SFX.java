package core.sound;


import core.ResourceManager;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * A simple sound effect. The SFX is played with an animantion. The file can be
 * a WAV or OGG file. OGG Files are played using VorbisSPI1.0.3 lib 
 * (http://www.javazoom.net/vorbisspi/vorbisspi.html).
 * @author panca
 */
public class SFX
{
    
    private transient AudioFormat format;
    private String name;
    private transient byte[] samples;
    /**
     * 
     * @param fileName SFX Name. The name of the file inside gameresources.
     */
    public SFX(String fileName){
        this.name = fileName;
        loadFile();
    }
    
    /**
     * Load a file to memory and add it to the SFXManager. The SFXManager 
     * holds all SFX and its possible to get some SFX using its name.
     */
    private void loadFile()
    {
        if(name == null)
            return;
        AudioInputStream stream = null;
        String path = ResourceManager.createResourceString(name, ResourceManager.RESSOURCE_TYPE_SOUND);
        try{
            
            
            InputStream audioSrc = new FileInputStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            stream = AudioSystem.getAudioInputStream(bufferedIn);
            //verifica o formato de audio. Codificacao, amostra, etc.
            format = stream.getFormat();
            //quantos bytes vao ser utilizados para 
            //carregar este audio para memoria?
            int tam = (int) (stream.getFrameLength() * format.getFrameSize());
            //aloca memoria
            samples = new byte[tam];
            DataInputStream is = new DataInputStream(stream);
            //finalmente Le as amostras
            is.readFully(samples);
        }   
        catch (Exception ex){
            System.out.println("ERROR LOADING SFX "+path);
            ex.printStackTrace();
            //System.exit(1);       
        }
        SFXManager.addSFX(name, this);
    }
    
    public AudioFormat getFormat() {
        return format;
    }

    public byte[] getSamples() {
        return samples;
    }

    
    public void init() {
        
        loadFile();
        
    }
    
    public String getName()
    {
        return name;
    }
}
