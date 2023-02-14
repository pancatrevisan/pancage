/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import javax.swing.JFrame;

/** 
 *
 * @author panca
 */
public class Main {
    public static void main(String[] args) {
        JFrame j = new JFrame();
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setSize(800, 600); 
        MarioGame mg = new MarioGame();
        j.add(mg);
        j.setVisible(true);
        mg.start();
    }
}
