package core.sound;

import core.ResourceManager;
import java.io.File;
import java.io.IOException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Class to play a music. The music can be a midi file, an WAV or OGG file.
 * OGG Files are played using VorbisSPI1.0.3 lib (http://www.javazoom.net/vorbisspi/vorbisspi.html).
 * @author panca
 */
public class Music {
    
    private static Sequencer sequencer;
    private static boolean playRaw;
    private static String music;
    private static Thread rawPlayer = null;
    
    /**
     * Only one music can be played at time.
     * @param music The name of the file to be played as music. This file must
     * be placed in gameresources/SOUND_PATH/
     */
    public static void play(String music)
    {
        Music.music = music;
        if( music.endsWith("midi") || (music.endsWith("mid")) ){
            try{            
                Sequence sequence = MidiSystem.getSequence(new File(ResourceManager.createResourceString(music, ResourceManager.RESSOURCE_TYPE_MUSIC)));
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.setSequence(sequence);
                sequencer.start();
            }catch(Exception e){
                System.out.println("ERROR PLAYING MIDI");
                e.printStackTrace();
                System.exit(1);
            }
        }
        else
        {
            rawPlayer = new Thread(new Runnable(){

            @Override
            public void run() {
                try{
                    String path = ResourceManager.createResourceString(music, ResourceManager.RESSOURCE_TYPE_MUSIC);
                    File file = new File(path);
                    AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
                    AudioInputStream in= AudioSystem.getAudioInputStream(file);
                    AudioInputStream din = null;
                    if (in != null){
                      AudioFormat baseFormat = in.getFormat();

                      AudioFormat  decodedFormat = new AudioFormat(
                              AudioFormat.Encoding.PCM_SIGNED,
                              baseFormat.getSampleRate(),
                              16,
                              baseFormat.getChannels(),
                              baseFormat.getChannels() * 2,
                              baseFormat.getSampleRate(),
                              false);

                      din = AudioSystem.getAudioInputStream(decodedFormat, in);
                      playRaw = true;
                      rawplay(decodedFormat, din);
                      in.close();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
              }
            });
            rawPlayer.start();
        }
    }
    
    private static void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException, InterruptedException
    {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);		
        if (line != null)
        {
          // Start
          line.start();
          int nBytesRead = 0, nBytesWritten = 0;
          while (nBytesRead != -1 && playRaw)
          {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
                
                
          }
          // Stop
          line.drain();
          line.stop();
          line.close();
          din.close();
        }		
    }

     private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
      SourceDataLine res = null;
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
      res = (SourceDataLine) AudioSystem.getLine(info);
      res.open(audioFormat);
      return res;
    }
    
    public static void stopPlay(String music)
    {
        if( music.endsWith("midi") || (music.endsWith("mid")) ){
            try{
                if(sequencer!= null)
                    sequencer.stop();
                
            }catch(Exception e){
                System.out.println("ERROR STOPPING PLAYING MIDI");
                e.printStackTrace();
                System.exit(1);
            }
        }
        else
        {
            playRaw = false;
            rawPlayer.interrupt();
        }
    }
    
}
