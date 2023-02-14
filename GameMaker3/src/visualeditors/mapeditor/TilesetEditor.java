/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualeditors.mapeditor;

import core.Config;
import core.ResourceManager;
import core.graphics.ImageLoader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import core.map.Tileset;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualeditors.ImageTipeFilter;



/**
 *
 * @author panca
 */
public class TilesetEditor extends javax.swing.JFrame {

    int tileSize;
    
    Tileset tileset;
    private String imageName;
    Image image;
    
    BufferedImage[] activeIcons; // up, down, left, right
    BufferedImage[] inActiveIcons;
    
    JPanel panelImage;
    
    int selectedTileX;
    int selectedTileY;
    private JFrame father;
    
    private JFileChooser chooseImage;
    private JFileChooser chooseTileset;
    /**
     * Creates new form TilesetEditor
     */
    public TilesetEditor(Tileset t, JFrame father) {
        tileset = t;
        this.father = father;
        activeIcons = new BufferedImage[4];
        inActiveIcons = new BufferedImage[4];
        
        try{
        activeIcons[0] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/upActive.png"));
        activeIcons[1] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/downActive.png"));
        activeIcons[2] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/leftActive.png"));
        activeIcons[3] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/rightActive.png"));
        
        inActiveIcons[0] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/upInactive.png"));
        inActiveIcons[1] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/downInactive.png"));
        inActiveIcons[2] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/leftInactive.png"));
        inActiveIcons[3] = javax.imageio.ImageIO.read(new File("ressources/visualeditors/tileEditor/rightInactive.png"));
        }
        catch(Exception e){
            e.printStackTrace();
        
        }
        panelImage = new JPanel(){
            public void paint(Graphics g)
            {
                super.paint(g);
                paintImagePreview(g);
            }
        };
        selectedTileX = -1;//none selected.
        panelImage.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(image!=null)
                {
                    if (( e.getX() < image.getWidth(null)) && (e.getY()< image.getHeight(null)) )
                    {
                        if(tileSize > 0)
                        {
                            selectedTileX = e.getX() / tileSize;
                            selectedTileY = e.getY() / tileSize;
                            
                            //position inside tile
                            int tilePosX = e.getX() % tileSize;
                            int tilePosY = e.getY() % tileSize;
                            
                            //from up
                            if( ((tilePosX >= (tileSize/2) - inActiveIcons[0].getWidth()/2) &&
                                  (tilePosX <  (tileSize/2) - inActiveIcons[0].getWidth()/2 + activeIcons[0].getWidth()) )
                                    
                                    &&
                                 ((tilePosY >= (tileSize/4) - inActiveIcons[0].getWidth()/2) && 
                                  (tilePosY < (tileSize/4) - inActiveIcons[0].getWidth()/2 + inActiveIcons[0].getWidth()))
                              )
                            {
                                tileset.getTile(selectedTileY, selectedTileX).setPassFromUp(!tileset.getTile(selectedTileY, selectedTileX).passFromUp());
                            }
                            
                            //from down
                            else if( ( (tilePosX >= (tileSize/2) - inActiveIcons[0].getWidth()/2) &&
                                     (tilePosX < (tileSize/2) - inActiveIcons[0].getWidth()/2 + inActiveIcons[0].getWidth())) &&
                                    
                                     ( (tilePosY >= (tileSize - (tileSize/4)) - inActiveIcons[0].getWidth()/2) && 
                                     (tilePosY < (tileSize - (tileSize/4)) - inActiveIcons[0].getWidth()/2+ inActiveIcons[0].getWidth()))
                                   )
                            {
                                tileset.getTile(selectedTileY, selectedTileX).setPassFromDown(!tileset.getTile(selectedTileY, selectedTileX).passFromDown());
                            }
                            
                            //from left
                            if( ( (tilePosX >= (tileSize/4) - inActiveIcons[0].getWidth()/2) &&
                                  (tilePosX < (tileSize/4) - inActiveIcons[0].getWidth()/2 + inActiveIcons[0].getWidth())) &&
                                    
                                  ((tilePosY >= (tileSize/2) - inActiveIcons[0].getWidth()/2) &&
                                    (tilePosY < (tileSize/2) - inActiveIcons[0].getWidth()/2 +inActiveIcons[0].getWidth() )) )
                            {
                                tileset.getTile(selectedTileY, selectedTileX).setPassFromLeft(!tileset.getTile(selectedTileY, selectedTileX).passFromLeft());
                            }
                            
                            //from right
                            if( ( (tilePosX >= (tileSize - (tileSize/4)) - inActiveIcons[0].getWidth()/2 - inActiveIcons[0].getWidth()/2) &&
                                  (tilePosX < (tileSize - (tileSize/4)) - inActiveIcons[0].getWidth()/2 - inActiveIcons[0].getWidth()/2 + inActiveIcons[0].getWidth())) &&
                                    
                                  ((tilePosY >= (tileSize/2) - inActiveIcons[0].getWidth()/2) &&
                                    (tilePosY < (tileSize/2) - inActiveIcons[0].getWidth()/2 +inActiveIcons[0].getWidth() )) )
                            {
                                tileset.getTile(selectedTileY, selectedTileX).setPassFromRight(!tileset.getTile(selectedTileY, selectedTileX).passFromRight());
                            }
                                
                        }
                        
                    }
                    jComboBox1.setSelectedIndex(tileset.getTile(selectedTileY, selectedTileX).getTerrainType());
                }
                else
                {
                    System.out.println("Image null");
                }
                
                scrollPrewiewImage.updateUI();
                wizzard1.updateUI();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(image!=null)
                {
                    if (( e.getX() < image.getWidth(null)) && (e.getY()< image.getHeight(null)) )
                    {
                        if(tileSize > 0)
                        {
                            selectedTileX = e.getX() / tileSize;
                            selectedTileY = e.getY() / tileSize;
                        }
                    }
                    jComboBox1.setSelectedIndex(tileset.getTile(selectedTileY, selectedTileX).getTerrainType());
                }
                else
                {
                    System.out.println("Image null");
                }
                scrollPrewiewImage.updateUI();
                wizzard1.updateUI();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        initComponents();
        wizzard1.setVisible(true);
        chooseImage = new JFileChooser();
        chooseImage.setFileFilter(new ImageTipeFilter());
        
        chooseTileset = new JFileChooser();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        if(tileset.getNumTilesX() > 0)
        {
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            fieldTileSize.setEnabled(false);
            scrollPrewiewImage.updateUI();
            wizzard1.updateUI();
            tileSize = tileset.getTileSize();
            image = new BufferedImage(tileSize * tileset.getNumTilesX(), tileSize * tileset.getNumTilesY(), BufferedImage.TYPE_4BYTE_ABGR);
            tileset.drawTileset(image.getGraphics());
        }
        
        addWindowListener(new WindowListener() {

            @Override
            public void windowClosing(WindowEvent e) {
                
                TilesetEditor.this.father.setVisible(true);
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
    }
    
    
    private void paintImagePreview(Graphics g)
    {
        if(tileset == null)
        {
            g.drawString("Choose an image and tile size", 20, 20);
        }
        else
        {
            for(int i = 0; i < tileset.getNumTilesY() ; i++)
            {
                for(int j = 0; j < tileset.getNumTilesX() ; j++) 
                {
                    g.drawImage(tileset.getTile(i, j).getImg(), j * tileSize,i*tileSize, null);
                    if(tileset.getTile(i, j).passFromUp())
                    {
                        g.drawImage(activeIcons[0], j * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize/4) - inActiveIcons[0].getWidth()/2, null);
                    }
                    else
                    {
                        g.drawImage(inActiveIcons[0], j * tileSize + (tileSize/2)- inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize/4)- inActiveIcons[0].getWidth()/2, null);
                    }
                    
                    
                    if(tileset.getTile(i, j).passFromDown())
                    {
                      g.drawImage(activeIcons[1], j * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize - (tileSize/4)) - inActiveIcons[0].getWidth()/2, null);
                    }
                    else
                    {
                        g.drawImage(inActiveIcons[1], j * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize - (tileSize/4)) - inActiveIcons[0].getWidth()/2, null);
                    }
                    
                    
                    if(tileset.getTile(i, j).passFromLeft())
                    {
                        g.drawImage(activeIcons[2], j * tileSize + (tileSize/4) - inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2, null);
                    }
                    else
                    {
                        g.drawImage(inActiveIcons[2], j * tileSize + (tileSize/4) - inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2, null);
                    }
                    
                    
                    if(tileset.getTile(i, j).passFromRight())
                    {
                      g.drawImage(activeIcons[3], j * tileSize + (tileSize - (tileSize/4))- inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2, null);
                    }
                    else
                    {
                        g.drawImage(inActiveIcons[3], j * tileSize + (tileSize - (tileSize/4))- inActiveIcons[0].getWidth()/2,  i * tileSize + (tileSize/2) - inActiveIcons[0].getWidth()/2, null);
                    }
                }
            }
            if(tileSize > 0)
            {
                for(int i = 0; i < image.getHeight(null); i+= tileSize)
                {
                    g.drawLine(0, i, image.getWidth(null)-1, i);
                }
                for(int i = 0; i < image.getWidth(null); i+= tileSize)
                {
                    g.drawLine(i, 0, i, image.getHeight(null)-1);
                }
                
                if(selectedTileX!= -1)
                {
                    g.setColor(Color.red);
                    g.drawRect(selectedTileX * tileSize, selectedTileY * tileSize,
                                tileSize, tileSize);
                }
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        wizzard1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        scrollPrewiewImage = new javax.swing.JScrollPane(panelImage)
        ;
        fieldTileSize = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Tileset");

        jButton1.setText("Choose Img");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fieldTileSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldTileSizeActionPerformed(evt);
            }
        });

        jLabel1.setText("Tile Size:");

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Apply");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TERRAIN_TYPE_EMPTY","TERRAIN_TYPE_GROUND", "TERRAIN_TYPE_WATER", "TERRAIN_TYPE_SLIPPERY","TERRAIN_TYPE_POISON","TERRAIN_TYPE_KILL","TERRAIN_TYPE_HILL", "TERRAIN_TYPE_CUSTOM1", "TERRAIN_TYPE_CUSTOM2","TERRAIN_TYPE_CUSTOM3" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });

        jLabel2.setText("Tile type:");

        javax.swing.GroupLayout wizzard1Layout = new javax.swing.GroupLayout(wizzard1);
        wizzard1.setLayout(wizzard1Layout);
        wizzard1Layout.setHorizontalGroup(
            wizzard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wizzard1Layout.createSequentialGroup()
                .addComponent(scrollPrewiewImage, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wizzard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(wizzard1Layout.createSequentialGroup()
                        .addGroup(wizzard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(wizzard1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldTileSize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addComponent(jLabel2))
                        .addGap(0, 94, Short.MAX_VALUE)))
                .addContainerGap())
        );
        wizzard1Layout.setVerticalGroup(
            wizzard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wizzard1Layout.createSequentialGroup()
                .addGroup(wizzard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wizzard1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addGroup(wizzard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldTileSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jButton2))
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(scrollPrewiewImage))
                .addContainerGap())
        );

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wizzard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wizzard1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //chose img
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String choosenImg = "";
        
        int r = chooseImage.showOpenDialog(this);
        if (r!= JFileChooser.APPROVE_OPTION)
            return;
        
        choosenImg = chooseImage.getSelectedFile().getAbsolutePath();
        File f2 = new File(choosenImg);
        
        //File f = new File(ResourceManager.createResourceURI(f2.getName(), ResourceManager.RESSOURCE_TYPE_IMAGE));
        File f = ResourceManager.createFile(f2.getName(), ResourceManager.RESSOURCE_TYPE_IMAGE);
        
        try {
            image = javax.imageio.ImageIO.read(new File(choosenImg));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        BufferedImage bi = (BufferedImage) image;
        try{
            javax.imageio.ImageIO.write(bi, "png", f);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        choosenImg = f2.getName();
        imageName = f2.getName();
        image = (BufferedImage) ImageLoader.loadImage(choosenImg);
        
        scrollPrewiewImage.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
        panelImage.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
        
        
        scrollPrewiewImage.updateUI();
        wizzard1.updateUI();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fieldTileSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldTileSizeActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_fieldTileSizeActionPerformed

    //cut image
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        try
        {
            this.tileSize = Integer.parseInt(fieldTileSize.getText());
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, "Insert an integer value!");
            return;
        }
        
        if( (image.getWidth(null)%tileSize!=0) || (image.getHeight(null)%tileSize!=0))
        {
            tileSize = 0;
            JOptionPane.showMessageDialog(this, "Incompatible tile size.","ERROR",JOptionPane.ERROR_MESSAGE);
            //tileset = null;
        }
        else
        {
            Tileset t = new Tileset();
            tileset.initTile(imageName, tileSize);
        }
        scrollPrewiewImage.updateUI();
        wizzard1.updateUI();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    //applicar
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        father.setVisible(true);
        father.repaint();
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1PropertyChange

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            if(selectedTileX>=0 && selectedTileY >=0)
                tileset.getTile(selectedTileY, selectedTileX).setTerrainType(jComboBox1.getSelectedIndex());
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldTileSize;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane scrollPrewiewImage;
    private javax.swing.JPanel wizzard1;
    // End of variables declaration//GEN-END:variables
}
