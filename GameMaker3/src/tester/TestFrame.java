/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tester;

import core.GameWindow;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 * This is a simple JFrame to show a gameWindow. It must be created in another
 * Thread. If the JFrame is closed, the game is finished and the Thread 
 * destroyed.
 * @author panca
 */
public class TestFrame extends JFrame implements Runnable{

    GameWindow game;
    public TestFrame(GameWindow gw)
    {
        super("Test ");
        game = gw;
        this.add(gw);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                TestFrame.this.game.stopRunning();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                TestFrame.this.game.stopRunning();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }
    @Override
    public void run() {
        while(!this.isVisible())
        {
            
        }
        
        if(game!=null)
            game.start();
    }
    
}
