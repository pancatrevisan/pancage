/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualeditors.objeditor;

import core.gameobject.Sprite;
import core.phisics.CollisionBox;
import core.phisics.Vector2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import visualeditors.GuiHelper;

/**
 *
 * @author panca
 */
public class SpriteEditor extends javax.swing.JFrame {

    
    public class Box
    {        
        Vector2D ul, lr;
        double width, height;
        
        public void setUl(Vector2D v)
        {
            ul = v;
        }
        
        
        public void setLr(Vector2D v)
        {
            lr = v;
        }
        
        public Box(Vector2D ul, Vector2D lr)
        {
            this.ul = ul;
            this.lr = lr;
        }
        public double getWidth()
        {
            width = Math.sqrt((ul.x - lr.x) * (ul.x - lr.x));
            return width;
        }
        public double getHeight()
        {
            height = Math.sqrt((ul.y - lr.y) * (ul.y - lr.y));
            return height;
        }
        public String toString()
        {
            return "Collision Box";
        }
    }
    
    private int zoom;
    private Box currentBox;
    private Vector2D selectedPoint;
    boolean addingAtk, addingVuln;
    private Sprite currentSprite;
    private JPanel imagePanel;
    private Color atkBoxColor = new Color(255,0,0);
    private Color vulnBoxColor = new Color(0,0,255);
    private Vector<Box> atkBoxes = new Vector<Box>();
    private Vector<Box> vulBoxes = new Vector<Box>();
    private boolean updatingBox;
    private AnimationEditor father;
    /**
     * Creates new form SpriteEditor
     */
    public SpriteEditor(Sprite sprite, AnimationEditor father) {
        
        zoom = 1;
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(SpriteEditor.this.father != null)
                    SpriteEditor.this.father.setVisible(true);
            }
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        currentSprite = sprite;
        this.father = father;
        
        if(currentSprite.getImage()!=null)
        {
            for(CollisionBox c:currentSprite.getAttackCollisionBox())
            {
                Vector2D ul = new Vector2D(c.getUpperLeftPoint().x, currentSprite.getImage().getHeight(null) - c.getUpperLeftPoint().y);
                Vector2D lr = new Vector2D(c.getLowerRightPoint().x, currentSprite.getImage().getHeight(null) - c.getLowerRightPoint().y);
                atkBoxes.add(new Box(ul, lr));
            }
            for(CollisionBox c:currentSprite.getVulnerableCollisionBox())
            {
                Vector2D ul = new Vector2D(c.getUpperLeftPoint().x, currentSprite.getImage().getHeight(null) - c.getUpperLeftPoint().y);
                Vector2D lr = new Vector2D(c.getLowerRightPoint().x, currentSprite.getImage().getHeight(null) - c.getLowerRightPoint().y);
                vulBoxes.add(new Box(ul, lr));
            }
            
        }
        
        
        imagePanel = new JPanel(){
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                if(currentSprite != null)
                {
                    if(currentSprite.getImage() != null)
                    {
                        //g.drawImage(currentSprite.getImage(), 0, 0, null);
                        g.drawImage(currentSprite.getImage(),0,0,
                                currentSprite.getImage().getWidth(null)*zoom,currentSprite.getImage().getHeight(null)*zoom,null);
                    }
                    msg.setText("");
                    if(addingAtk){
                        g.setColor(atkBoxColor);
                        msg.setText("RIGHT BUTTON TO FINISH");
                    }
                    else if(addingVuln){
                        g.setColor(vulnBoxColor);
                        msg.setText("RIGHT BUTTON TO FINISH");
                    }
                    else if(updatingBox)
                    {
                        g.setColor(Color.ORANGE);
                        msg.setText("RIGHT BUTTON TO FINISH");
                        jComboBox1.setSelectedIndex(-1);
                        jComboBox3.setSelectedIndex(-1);
                    }
                    if(addingAtk || addingVuln || updatingBox)
                    {
                        g.drawRect((int)currentBox.ul.x, (int)currentBox.ul.y, (int)currentBox.getWidth(), (int)currentBox.getHeight());
                       // g.setColor(Color.WHITE);
                       // g.drawRect((int)currentBox.ul.x-1, (int)currentBox.ul.y-1, (int)currentBox.getWidth()+2, (int)currentBox.getHeight()+2);
                    }
                    /*else if(updatingBox)
                    {
                        g.drawRect((int)currentBox.ul.x, (int)currentBox.ul.y, (int)currentBox.getWidth(), (int)currentBox.getHeight());
                        g.setColor(Color.WHITE);
                        g.drawRect((int)currentBox.ul.x-1, (int)currentBox.ul.y-1, (int)currentBox.getWidth()+2, (int)currentBox.getHeight()+2);
                    }*/
                    else
                    {
                        
                        for (Box box : atkBoxes) {
                            g.setColor(atkBoxColor);
                            
                            g.drawRect((int)box.ul.x * zoom, (int)box.ul.y * zoom, (int)box.getWidth() * zoom, (int)box.getHeight() * zoom);
                            
                           /* g.setColor(Color.WHITE);
                            g.drawRect(((int)box.ul.x-1)*zoom, ((int)box.ul.y-1)*zoom, ((int)box.getWidth()+2) * zoom, ((int)box.getHeight()+2)*zoom);
                            */
                        }
                        for (Box box : vulBoxes) {
                            g.setColor(vulnBoxColor);
                            g.drawRect((int)box.ul.x * zoom, (int)box.ul.y * zoom, (int)box.getWidth() * zoom, (int)box.getHeight() * zoom);
                         /*   g.setColor(Color.WHITE);
                            g.drawRect(((int)box.ul.x-1)*zoom, ((int)box.ul.y-1)*zoom, ((int)box.getWidth()+2) * zoom, ((int)box.getHeight()+2)*zoom);
                            */
                        }
                        
                    }
                }
            }
        };
        
        imagePanel.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e){
                if(currentBox == null)
                    return;
                //right button
                if(e.getButton() == MouseEvent.BUTTON3)
                {
                    //when finished editting in zoom, rescale
                    selectedPoint = null;
                    currentBox.lr.x = Math.ceil(currentBox.lr.x / zoom);
                    currentBox.lr.y = Math.ceil(currentBox.lr.y / zoom);
                    currentBox.ul.x = Math.ceil(currentBox.ul.x / zoom);
                    currentBox.ul.y = Math.ceil(currentBox.ul.y / zoom);
                    
                    //add
                    if(addingAtk)
                    {                                                
                        atkBoxes.add(currentBox);
                        jComboBox1.updateUI();
                    }
                    else if(addingVuln)
                    {
                        vulBoxes.add(currentBox);
                        jComboBox3.updateUI();
                    }
                    else if (updatingBox) //is updating a box
                    {
                        updatingBox = false;
                    }
                    addingAtk = false;
                    addingVuln = false;
                    currentBox = null;
                    
                    imagePanel.updateUI();
                    return;
                }
                double px = e.getX() - currentBox.ul.x;
                double py = e.getY() - currentBox.ul.y;
                selectedPoint = null;
                
                double dist = Math.sqrt(px*px + py*py);
               
                if(dist <=4) 
                    selectedPoint = currentBox.ul;
                else{
                    px = e.getX() - currentBox.lr.x;
                    py = e.getY() - currentBox.lr.y;

                    dist = Math.sqrt(px*px+py*py);
                    if(dist <=4) 
                        selectedPoint = currentBox.lr;
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        imagePanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectedPoint == null)
                    return;
                
                selectedPoint.x = e.getX();
                selectedPoint.y = e.getY();
                
                imagePanel.updateUI();
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane(imagePanel);
        jLabel5 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jButton13 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        msg = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel5.setText("Time:");

        jButton7.setText("Select image");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setText("Add");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Remove");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(atkBoxes));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel3.setText("milis");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("VULNERABLE");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("ATTACK");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(vulBoxes));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jButton13.setText("Remove");

        jButton12.setText("Add");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        msg.setText("jLabel1");

        jPanel1.setBackground(vulnBoxColor);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jLabel1.setText("Atk color");

        jPanel2.setBackground(atkBoxColor);
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jLabel2.setText("Vuln color");

        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("-");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText(zoom+"x");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jButton1)
                .addGap(27, 27, 27)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addGap(18, 18, 18)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(33, 33, 33)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(11, 11, 11)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton12)
                        .addGap(18, 18, 18)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msg)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jButton7)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9)
                            .addComponent(jButton10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton12)
                            .addComponent(jButton13)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton3)
                        .addGap(10, 10, 10)
                        .addComponent(jButton4)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(msg)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        Image image = null;
        String imageName = GuiHelper.copyImage(this);
        if(imageName == null)
            return;
        

        
        currentSprite.setImageName(imageName);
        image = currentSprite.getImage();
        Dimension d = new Dimension(currentSprite.getImage().getWidth(null)*zoom,currentSprite.getImage().getHeight(null)*zoom);
        imagePanel.setPreferredSize(d);
        imagePanel.updateUI();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:

        ///add atk box
        if(! addingAtk && !addingVuln)
        {
            addingAtk = true;
            
            Box cb = new Box(new Vector2D(jScrollPane4.getViewport().getViewPosition().x+10,jScrollPane4.getViewport().getViewPosition().y+10),
                             new Vector2D(jScrollPane4.getViewport().getViewPosition().x+ 30,jScrollPane4.getViewport().getViewPosition().y+30));
            currentBox = cb;
            //currentSprite.getAttackCollisionBox().add(cb);
        }
        
        imagePanel.updateUI();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        //add vuln box
        if(! addingAtk && !addingVuln)
        {
            addingVuln = true;
            Box cb = new Box(new Vector2D(0,0), new Vector2D(30,30));
            currentBox = cb;
            //currentSprite.getAttackCollisionBox().add(cb);
        }
        imagePanel.updateUI();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
        vulnBoxColor = javax.swing.JColorChooser.showDialog(this, "Choose a color", vulnBoxColor);
        jPanel1.setBackground(vulnBoxColor);
        jPanel1.repaint();
        imagePanel.updateUI();
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
        atkBoxColor = javax.swing.JColorChooser.showDialog(this, "Choose a color", atkBoxColor);
        jPanel2.setBackground(atkBoxColor);
        jPanel2.repaint();
        imagePanel.updateUI();
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
        {
            currentBox = (Box)jComboBox1.getSelectedItem();
            updatingBox = true;
            currentBox.lr.x *= zoom;
            currentBox.lr.y *= zoom;
            currentBox.ul.x *= zoom;
            currentBox.ul.y *= zoom;
        }
        
        imagePanel.updateUI();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(jTextField1.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this, "Insert the sprite time in millis!","TIME",JOptionPane.ERROR_MESSAGE);
            return;
        }
        try
        {
            int time = Integer.parseInt(jTextField1.getText());
            currentSprite.setTime(core.dataconversion.DataConversion.milisToSecs(time));
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Insert the sprite time in millis!","TIME",JOptionPane.ERROR_MESSAGE);
            return;
        }
            
        for(Box b : vulBoxes)
        {
            Vector2D ur = new Vector2D(b.lr.x, currentSprite.getImage().getHeight(null)-b.ul.y);
            Vector2D ll = new Vector2D(b.ul.x, currentSprite.getImage().getHeight(null)-b.lr.y);
            //the collisionbox uses inverse coordinates
            CollisionBox cb = new CollisionBox(ur, ll);
            currentSprite.getVulnerableCollisionBox().add(cb);
        }
        for(Box b:atkBoxes)
        {
            Vector2D ur = new Vector2D(b.lr.x, currentSprite.getImage().getHeight(null)-b.ul.y);
            Vector2D ll = new Vector2D(b.ul.x, currentSprite.getImage().getHeight(null)-b.lr.y);
            //the collisionbox uses inverse coordinates
            CollisionBox cb = new CollisionBox(ur, ll);
            currentSprite.getAttackCollisionBox().add(cb);
        }
        if(father != null)
            father.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        //remove atk
        if(jComboBox1.getSelectedIndex() > 0)
        {
            jComboBox1.removeItemAt(jComboBox1.getSelectedIndex());
            imagePanel.updateUI();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(father != null)
        {
            father.getSpriteList().removeElement(this.currentSprite);
            father.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(currentSprite == null || currentSprite.getImage() == null)
            return;
        zoom *=2;
        jLabel4.setText(zoom+"x");
        Image image = currentSprite.getImage();
        Dimension d = new Dimension(currentSprite.getImage().getWidth(null)*zoom,currentSprite.getImage().getHeight(null)*zoom);
        imagePanel.setPreferredSize(d);
        imagePanel.updateUI();
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(currentSprite == null || currentSprite.getImage() == null)
            return;
        if(zoom >= 2)
            zoom /=2;
        jLabel4.setText(zoom+"x");
        Image image = currentSprite.getImage();
        Dimension d = new Dimension(currentSprite.getImage().getWidth(null)*zoom,currentSprite.getImage().getHeight(null)*zoom);
        imagePanel.setPreferredSize(d);
        imagePanel.updateUI();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
        {
            currentBox = (Box)jComboBox3.getSelectedItem();
            updatingBox = true;
            currentBox.lr.x *= zoom;
            currentBox.lr.y *= zoom;
            currentBox.ul.x *= zoom;
            currentBox.ul.y *= zoom;
        }
        imagePanel.updateUI();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel msg;
    // End of variables declaration//GEN-END:variables
}
