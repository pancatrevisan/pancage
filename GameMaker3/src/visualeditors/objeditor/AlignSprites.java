/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package visualeditors.characterEditor;
package visualeditors.objeditor;

import core.gameobject.Sprite;
import core.phisics.CollisionBox;
import core.phisics.Vector2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author panca
 */
public class AlignSprites extends javax.swing.JDialog {

    
    private int zoom;
    Vector<Vector2D> positions;
    JPanel panelPreviewSprites;
    ResizableLabel scenaryCollisionBox;
    MovableLabel movableSprite;
    Sprite[] originalSprites;
    int actualSprite;
    int maxSprites;
    boolean ended;
   
    int biggerWidth, biggerHeight;
    /**
     * Creates new form AlignSprites
     */
    public AlignSprites(java.awt.Frame parent, boolean modal, Sprite[] sprites) {
        super(parent, modal);
        zoom = 1;
        actualSprite = 0;
        
        positions = new Vector<Vector2D>();
        positions.add(new Vector2D(0,0));
        maxSprites = sprites.length;
        originalSprites = sprites;
        
        for(Sprite s:originalSprites)
        {
            if(s.getImage().getWidth(null) > biggerWidth)
                biggerWidth = s.getImage().getWidth(null);
            if(s.getImage().getHeight(null) > biggerHeight)
                biggerHeight = s.getImage().getHeight(null);
        }

        
        panelPreviewSprites = new JPanel()
        {
            public void paint(Graphics g)
            {
                super.paint(g);
                paintSprites(g);
            }
        };
        
        panelPreviewSprites.setLayout(null);
  
        /*if(maxSprites > 1)
        {
            movableSprite = new MovableLabel(originalSprites.get(actualSprite).getImage(),panelPreviewSprites, null);
            panelPreviewSprites.add(movableSprite);
            
        }*/
        
        //scenaryCollisionBox = new ResizableLabel(10, 10, 20, 20, panelPreviewSprites, Color.yellow);
        initComponents();
        jTextField1.setEditable(false);
        jTextField1.setText(actualSprite+1+" of "+maxSprites);
        
        
        updateButtons();
    }

    ///o ultimo é maxSprites-1.
    private void updateButtons()
    {
        if(actualSprite  == 0)
        {
            buttonNext.setEnabled(true);
            buttonPrevious.setEnabled(false);
            buttonUp.setEnabled(false);
            buttonL.setEnabled(false);
            buttonR.setEnabled(false);
            buttonD.setEnabled(false);
            buttonApply.setEnabled(false);
            ended = false;
            if(actualSprite == this.maxSprites-1)//one sprite
            {
                buttonNext.setEnabled(false);
                scenaryCollisionBox = new ResizableLabel(0, 0, 20, 20, panelPreviewSprites, Color.yellow);
                
                panelPreviewSprites.add(scenaryCollisionBox);
                buttonApply.setEnabled(true);
                ended = true;
            }
            
        }
        else if(actualSprite < this.maxSprites) // 0 < spr < max
        {
            
            buttonNext.setEnabled(true);
            
            
            buttonPrevious.setEnabled(true);
            if(actualSprite < 2)
                buttonPrevious.setEnabled(false);
            buttonUp.setEnabled(true);
            buttonL.setEnabled(true);
            buttonR.setEnabled(true);
            buttonD.setEnabled(true);
            buttonApply.setEnabled(false);
            
        }
        else
        {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(true);
            buttonUp.setEnabled(false);
            buttonL.setEnabled(false);
            buttonR.setEnabled(false);
            buttonD.setEnabled(false);  
            buttonApply.setEnabled(true);
            
        }
    }
    
    private void updateScreen()
    {
        jLabel3.setText(zoom+"x");
        
        Dimension d = new Dimension(biggerWidth*zoom,biggerHeight * zoom);
        panelPreviewSprites.setPreferredSize(d);
        panelPreviewSprites.updateUI();
        jScrollPane1.updateUI();
    }
    
    private void paintSprites(Graphics g)
    {
        for(int i = 0; i < positions.size(); i++)
        {
            g.drawImage(originalSprites[i].getImage(),(int)positions.get(i).getX(), (int)positions.get(i).getY(),
                                originalSprites[i].getImage().getWidth(null)*zoom,originalSprites[i].getImage().getHeight(null)*zoom,null);
            //g.drawImage(originalSprites[i].getImage(), (int)positions.get(i).getX(), (int)positions.get(i).getY(), null);
        }
        
        //draw over the other objects.
        if(scenaryCollisionBox != null)
            scenaryCollisionBox.drawTo(g);
            
        /*if(originalSprites!= null && originalSprites.size()>0)
            g.drawImage(originalSprites.get(0).getImage(), 0, 0, null);*/
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane(panelPreviewSprites);
        buttonApply = new javax.swing.JButton();
        buttonNext = new javax.swing.JButton();
        buttonPrevious = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        buttonUp = new javax.swing.JButton();
        buttonL = new javax.swing.JButton();
        buttonR = new javax.swing.JButton();
        buttonD = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        buttonApply.setText("Apply");
        buttonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApplyActionPerformed(evt);
            }
        });

        buttonNext.setText("Next");
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextActionPerformed(evt);
            }
        });

        buttonPrevious.setText("Previous");
        buttonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPreviousActionPerformed(evt);
            }
        });

        jLabel2.setText("Position every sprite, one by one");

        jLabel1.setText("Current Sprite:");

        buttonUp.setText("/\\");
            buttonUp.setToolTipText("Move the actual sprite one pixel up.");
            buttonUp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonUpActionPerformed(evt);
                }
            });

            buttonL.setText("<");
            buttonL.setToolTipText("Move the actual sprite one pixel left.");
            buttonL.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonLActionPerformed(evt);
                }
            });

            buttonR.setText(">");
            buttonR.setToolTipText("Move the actual sprite one pixel right.");
            buttonR.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonRActionPerformed(evt);
                }
            });

            buttonD.setText("\\/");
            buttonD.setToolTipText("Move the actual sprite one pixel down.");
            buttonD.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonDActionPerformed(evt);
                }
            });

            jButton1.setText("+");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jButton2.setText("-");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jLabel3.setText(zoom+"x");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel2))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(50, 50, 50)
                            .addComponent(buttonL, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(59, 59, 59)
                            .addComponent(buttonR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(102, 102, 102)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(buttonUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonD, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(buttonApply, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(buttonPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(buttonNext, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                        .addComponent(jTextField1))))))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1)
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(jLabel2)
                    .addGap(42, 42, 42)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonNext)
                        .addComponent(buttonPrevious))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(buttonUp)
                    .addGap(9, 9, 9)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonL)
                        .addComponent(buttonR))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(buttonD)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                    .addComponent(buttonApply)
                    .addGap(51, 51, 51))
                .addGroup(layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(jButton1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton2)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel3)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void buttonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextActionPerformed
        // TODO add your handling code here:
        Vector2D pos = new Vector2D();               
        if(actualSprite>0)
        {     
            pos.setX(movableSprite.getX());
            pos.setY(movableSprite.getY());
            
            panelPreviewSprites.remove(movableSprite);
            positions.add(pos);
        }
        
        actualSprite++;
        jTextField1.setText(actualSprite+1+" of "+maxSprites);
        if(actualSprite < maxSprites)
        {
            movableSprite = new MovableLabel(originalSprites[actualSprite].getImage(), panelPreviewSprites);
            movableSprite.setZoom(zoom);
            panelPreviewSprites.add(movableSprite); 
        }
        else
        {
            buttonNext.setEnabled(false);
            jTextField1.setText(actualSprite+" of "+maxSprites);
            scenaryCollisionBox = new ResizableLabel(0, 0, 20, 20, panelPreviewSprites, Color.yellow);
            panelPreviewSprites.add(scenaryCollisionBox);
            
        }
        panelPreviewSprites.updateUI();
        updateButtons();
        
    }//GEN-LAST:event_buttonNextActionPerformed

    private void buttonPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPreviousActionPerformed
        // TODO add your handling code here:
        if(actualSprite < 1)
            return;
        
        if(scenaryCollisionBox != null)
        {
            panelPreviewSprites.remove(scenaryCollisionBox);
            scenaryCollisionBox = null;
        }
        
        actualSprite--;
        positions.remove(actualSprite);
        
        panelPreviewSprites.remove(movableSprite);
        movableSprite = new MovableLabel(originalSprites[actualSprite].getImage(), panelPreviewSprites);
        movableSprite.setZoom(zoom);
        panelPreviewSprites.add(movableSprite);
        panelPreviewSprites.updateUI();
        
        updateButtons();
    }//GEN-LAST:event_buttonPreviousActionPerformed

    private void buttonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpActionPerformed
        // TODO add your handling code here:
        if(movableSprite != null)
        {
            movableSprite.setLocation(movableSprite.getX(), movableSprite.getY()-1);
        }
        else if(scenaryCollisionBox != null)
        {
            scenaryCollisionBox.setLocation(movableSprite.getX(), movableSprite.getY()-1);
        }
    }//GEN-LAST:event_buttonUpActionPerformed

    private void buttonRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRActionPerformed
        // TODO add your handling code here:
        if(movableSprite != null)
        {
            movableSprite.setLocation(movableSprite.getX()+1, movableSprite.getY());
        }
        else if(scenaryCollisionBox != null)
        {
            scenaryCollisionBox.setLocation(movableSprite.getX()+1, movableSprite.getY());
        }
        movableSprite.updateUI();
    }//GEN-LAST:event_buttonRActionPerformed

    private void buttonLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLActionPerformed
        // TODO add your handling code here:
        if(movableSprite != null)
        {
            movableSprite.setLocation(movableSprite.getX()-1, movableSprite.getY());
        }
        else if(scenaryCollisionBox != null)
        {
            scenaryCollisionBox.setLocation(movableSprite.getX()-1, movableSprite.getY());
        }
    }//GEN-LAST:event_buttonLActionPerformed

    private void buttonDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDActionPerformed
        // TODO add your handling code here:
        if(movableSprite != null)
        {
            movableSprite.setLocation(movableSprite.getX(), movableSprite.getY()+1);
        }
        else if(scenaryCollisionBox != null)
        {
            scenaryCollisionBox.setLocation(movableSprite.getX(), movableSprite.getY()+1);
        }
    }//GEN-LAST:event_buttonDActionPerformed

    private void buttonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonApplyActionPerformed
        // TODO add your handling code here:
        
        if(actualSprite >= maxSprites-1)
        {
            //put the collision box in all sprites (oh my).
            for(int i = 0; i < originalSprites.length; i++)
            {
                Sprite s = originalSprites[i];
                int imgh = s.getImage().getHeight(null);
    
                Vector2D nul = new Vector2D(-positions.get(i).getX()+scenaryCollisionBox.getX(), -positions.get(i).getY()+scenaryCollisionBox.getY());
                //Vector2D nlr = new Vector2D(-positions.get(i).getX()+lr.getX(), -positions.get(i).getY()+lr.getY());                                               
                
                int w = scenaryCollisionBox.getWidth();
                int h = scenaryCollisionBox.getHeight();
                
                Vector2D ur, ll;
                /*
                ll = new Vector2D(nul.getX(), imgh - nul.getY()-h);
                ur = new Vector2D(nul.getX()+w, imgh - nul.getY());
                */
                ur = new Vector2D(nul.getX()+ w, nul.getY()) ;
                ll = new Vector2D(nul.getX(), nul.getY()+h);
                
                
                
                
                ur.x = ur.x / zoom;
                ur.y = ur.y / zoom;
                ll.x = ll.x / zoom;
                ll.y = ll.y / zoom;
                
                //invert y 
                ur.y = imgh - ur.y;
                ll.y = imgh - ll.y;
                
                
                
                
                
                
                
                CollisionBox cb = new CollisionBox(ur,ll);
                
                
                s.setScenaryCollisionBox(cb);
            }
            this.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Align every sprite and the collision box!");
        }
    }//GEN-LAST:event_buttonApplyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        zoom *= 2;
        if(movableSprite!= null)
            movableSprite.setZoom(zoom);
        this.updateScreen();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(zoom >=2)
            zoom /= 2;
        if(movableSprite!= null)
            movableSprite.setZoom(zoom);
        this.updateScreen();
    }//GEN-LAST:event_jButton2ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonApply;
    private javax.swing.JButton buttonD;
    private javax.swing.JButton buttonL;
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonPrevious;
    private javax.swing.JButton buttonR;
    private javax.swing.JButton buttonUp;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
