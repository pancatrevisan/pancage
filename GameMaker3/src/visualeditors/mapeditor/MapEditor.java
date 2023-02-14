/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualeditors.mapeditor;

import core.Config;
import core.gameobject.BaseObject;
import core.graphics.ImageBank;
import core.map.Background;
import core.map.Map;
import core.map.Tile;
import core.map.Tileset;
import core.phisics.Vector2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Vector;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import tester.TestFrame;
import tester.background.TestBackgroundGame;
import visualeditors.GuiHelper;
import visualeditors.ImageTipeFilter;
import visualeditors.PanelWithThings;
//import visualeditors.objeditor.MovableLabel;

/**
 *
 * @author panca
 */
public class MapEditor extends javax.swing.JFrame {

    Color bkgColor, lineColor;
    BufferedImage buffer;
    boolean viewGrid;
    private int selectedLayer;    
    private Map map;
    private Tile selectedTile;
    private Tileset tileset;
    
    private JPanel panelTile, panelLayer0, panelOverLayer;
    private PanelWithThings panelObjects;
    private Image tileImage;
    private JFileChooser imageChooser;
    
    private int lastSelectedPanelIndex = 0;
    private String mapMusic;


    /**
     * Creates new form MapEditor
     */
    public MapEditor() {
        //getObjectClassNames();
        
        viewGrid = true;
        
        bkgColor = Color.white;
        lineColor = Color.red;
        tileset= new Tileset();
        selectedLayer = 0;
        imageChooser = new JFileChooser();
        imageChooser.setFileFilter(new ImageTipeFilter());
        panelTile = new JPanel(){
            
            @Override
            public void paint(Graphics g)
                {
                    super.paint(g);
                    
                    if(tileset!= null)
                    {
                        //if(map.getTileset() != null)
                        {
                            tileImage = ImageBank.getImage(tileset.getImageName());
                            if(tileImage == null)
                                return;
                            setPreferredSize(new Dimension(tileImage.getWidth(null),tileImage.getHeight(null)));
                            g.drawImage(tileImage, 0, 0, null);
                            if(selectedTile!=null)
                            {
                                g.setColor(Color.RED);
                                g.drawRect(selectedTile.getColumn()*tileset.getTileSize(), selectedTile.getLine()*tileset.getTileSize(), tileset.getTileSize(), tileset.getTileSize());
                            }
                        }
                    }
                    else
                        g.drawString("Tile", 20, 20);
                }
        };
        
        panelTile.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(tileset == null)
                    return;
                if(tileset.getImage() == null)
                    return;
                int l, c;
                l = e.getY() / tileset.getTileSize();
                c = e.getX() / tileset.getTileSize();
                if( (l < 0  || l >= tileset.getNumTilesY()) || (c<0 || c >= tileset.getNumTilesX()) )
                    return;
                selectedTile = tileset.getTile(l, c);
                
                repaint();
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
        
        panelLayer0 = new JPanel(){
            @Override
            public void paint(Graphics g)
                {
                    super.paint(g);
                    if(map == null)
                        g.drawString("layer 0", 20, 20);
                    else
                    {
                        
                        g.setColor(bkgColor);
                        g.fillRect(0, 0, map.getMapWidth(), map.getMapHeight());
                        map.previewMap(g,0);
                        //window
                        
                        g.setColor(Color.red);
                        g.drawRect(0, map.getMapHeight()-map.getWindowHeight(), map.getWindowWidth(), map.getWindowHeight());
                        g.setColor(Color.black);
                        g.drawRect(1, 1+map.getMapHeight()-map.getWindowHeight(), map.getWindowWidth(), map.getWindowHeight());
                        
                        
                        g.setColor(lineColor);
                        if(!viewGrid)
                            return;
                        for(int i = 0; i < map.getNumberOfTilesX()+1; i++)
                        {
                            g.drawLine(i*map.getTileset().getTileSize()-1, 0, 
                                       i*map.getTileset().getTileSize()-1, map.getMapHeight());
                        }
                        for(int i = 0; i < map.getNumberOfTilesY(); i++)
                        {
                            g.drawLine(0, i*map.getTileset().getTileSize()-1, 
                                       map.getMapWidth(), i*map.getTileset().getTileSize()-1);
                        }
                        
                    }
                }
        };
        panelLayer0.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(tileset == null || map == null)
                    return;
                int x = e.getX() / tileset.getTileSize();
                int y = e.getY() / tileset.getTileSize();
                if(e.getButton() == MouseEvent.BUTTON1){
                    
                    if( (x >= 0 && x < map.getNumberOfTilesX()) && (y >= 0 && y < map.getNumberOfTilesY()))
                    {
                        map.setTile(0, selectedTile, y, x);
                    }
                    
                    panelLayer0.updateUI();
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {
                    map.setTile(0, null, y, x);
                }
                e.consume();
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        }
        );
        
        panelOverLayer = new JPanel(){
            @Override
            public void paint(Graphics g)
                {
                    super.paint(g);
                    if(map == null)
                        g.drawString("Overlayer", 20, 20);
                    else
                    {
                        if (buffer == null)
                                buffer = new BufferedImage(map.getMapWidth(), map.getMapHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                        RescaleOp op = new RescaleOp(0.5f, 0, null);
                        
                        Graphics g2 = buffer.getGraphics();
                        g2.setColor(bkgColor);
                        g2.fillRect(0, 0, map.getMapWidth(), map.getMapHeight());
                        map.previewMap(g2,0);                      
                        
                        op.filter(buffer, buffer);
                        g.drawImage(buffer, 0, 0, null);
                        g.setColor(lineColor);
                        map.previewMap(g,1);                      
                        
                        
                        //window
                        
                        g.setColor(Color.red);
                        g.drawRect(0, map.getMapHeight()-map.getWindowHeight(), map.getWindowWidth(), map.getWindowHeight());
                        g.setColor(Color.black);
                        g.drawRect(1, 1+map.getMapHeight()-map.getWindowHeight(), map.getWindowWidth(), map.getWindowHeight());
                        
                        
                        if(!viewGrid)
                            return;
                        for(int i = 0; i < map.getNumberOfTilesX()+1; i++)
                        {
                            g.drawLine(i*map.getTileset().getTileSize()-1, 0, 
                                       i*map.getTileset().getTileSize()-1, map.getMapHeight());
                        }
                        for(int i = 0; i < map.getNumberOfTilesY(); i++)
                        {
                            g.drawLine(0, i*map.getTileset().getTileSize()-1, 
                                       map.getMapWidth(), i*map.getTileset().getTileSize()-1);
                        }
                        
                    }
                }
        };
        panelOverLayer.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(tileset == null || map == null)
                    return;int x = e.getX() / tileset.getTileSize();
                int y = e.getY() / tileset.getTileSize();
                if(e.getButton() == MouseEvent.BUTTON1)
                {    
                    if( (x >= 0 && x < map.getNumberOfTilesX()) && (y >= 0 && y < map.getNumberOfTilesY()))
                    {
                        map.setTile(1, selectedTile, y, x);
                    }
                    
                    panelOverLayer.updateUI();
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {
                    map.setTile(1, null, y, x);
                }
                e.consume();
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        panelObjects = new PanelWithThings();
        
        initComponents();
        
        
        
    }

    private void setWindowDialogConfigs()
    {
        newMapH.setText(""+map.getWindowHeight());
        newMapW.setText(""+map.getWindowWidth());
        moveWinX.setSelected(map.isMoveX());
        moveWinY.setSelected(map.isMoveY());
        
        Dimension d = new Dimension(map.getMapWidth(), map.getMapHeight());
        panelLayer0.setPreferredSize(d);
        panelLayer0.setMinimumSize(d);
        panelLayer0.setMaximumSize(d);
        
        panelOverLayer.setPreferredSize(d);
        panelOverLayer.setMinimumSize(d);
        panelOverLayer.setMaximumSize(d);
        
        
        panelObjects.setPreferredSize(d);
        panelObjects.setMinimumSize(d);
        panelObjects.setMaximumSize(d);
        
        newMapDialog.setVisible(false);
        
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
        this.repaint();
        
    }
    
   /* private void getObjectClassNames()
    {
        try
        {
            objectClassNames = new Vector<String>();
        
            //System.out.println(": "+Config.class.getResource("../"+Config.PROJECT_PACKAGE.replace('.', File.separatorChar)));
            File packageDir = new File(Config.class.getResource("../"+Config.PROJECT_PACKAGE.replace('.', File.separatorChar)).toURI());
            System.out.println("pd: "+packageDir.getAbsolutePath());
            
            Vector<File> subFiles = new Vector<File>();
            Vector<File> classes = new Vector<File>();
            
            
            //all subfiles in the game package dir...
            File files[] = packageDir.listFiles();
            for(File f: files)
            {
                subFiles.add(f);
                classes.add(f);
            }
            files = null;

            //get ALL files/directories.
            for(File f:subFiles)
            {
                if(f.isDirectory())
                {
                    File files2[] = f.listFiles();
                    for(File f2: files2)
                    {
                        classes.add(f2);
                    }
                }
            }
            
            //check if the class is subclass of BaseObject
            for(File f:classes)
            {
                if(!f.isDirectory()){
                    String classs = f.getPath();
                    classs = classs.substring(classs.indexOf(Config.PROJECT_PACKAGE)).replace(""+File.separatorChar, ".");
                    System.out.println("class: "+classs);
                    try{
                        Object bo = Class.forName(classs.replace(".class", "")).newInstance();
                        if(bo.getClass().getSuperclass().getName().equalsIgnoreCase("BaseObject")){
                            String classsN = f.getPath();
                            classsN = classsN.substring(classsN.indexOf(Config.PROJECT_PACKAGE)).replace(""+File.separatorChar, ".");
                            objectClassNames.add(classsN.replace(".class", ""));
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();

                    }
                }
            }
         }
         catch(Exception e)
         {
            e.printStackTrace();
         }
         for(String s:objectClassNames)
            System.out.println("Clean Class: "+s);
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newMapDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        mapWindowW = new javax.swing.JTextField();
        mapTileX = new javax.swing.JTextField();
        mapTileY = new javax.swing.JTextField();
        mapWindowH = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        dialogSetWindowSize = new javax.swing.JDialog();
        newMapH = new javax.swing.JTextField();
        newMapW = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        moveWinX = new javax.swing.JCheckBox();
        moveWinY = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane(panelLayer0);
        jScrollPane2 = new javax.swing.JScrollPane(panelOverLayer);
        jScrollPane5 = new javax.swing.JScrollPane(panelObjects);
        jScrollPane3 = new javax.swing.JScrollPane(panelTile);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();

        jLabel1.setText("Tiles X:");

        jLabel2.setText("Tiles Y:");

        jLabel3.setText("Window Width:");

        jLabel4.setText("Window Height:");

        jToggleButton1.setText("Create!");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setText("Help");

        javax.swing.GroupLayout newMapDialogLayout = new javax.swing.GroupLayout(newMapDialog.getContentPane());
        newMapDialog.getContentPane().setLayout(newMapDialogLayout);
        newMapDialogLayout.setHorizontalGroup(
            newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newMapDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newMapDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jToggleButton2))
                    .addGroup(newMapDialogLayout.createSequentialGroup()
                        .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mapWindowW, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mapTileX, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mapTileY, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mapWindowH, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(newMapDialogLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jToggleButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        newMapDialogLayout.setVerticalGroup(
            newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newMapDialogLayout.createSequentialGroup()
                .addComponent(jToggleButton2)
                .addGap(20, 20, 20)
                .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(mapTileX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(mapTileY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(mapWindowW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(newMapDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(mapWindowH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jToggleButton1)
                .addContainerGap())
        );

        jLabel7.setText("Window Height:");

        jLabel8.setText("Window Width:");

        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Apply");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        moveWinX.setText("Move Window X");

        moveWinY.setText("Move Window Y");

        javax.swing.GroupLayout dialogSetWindowSizeLayout = new javax.swing.GroupLayout(dialogSetWindowSize.getContentPane());
        dialogSetWindowSize.getContentPane().setLayout(dialogSetWindowSizeLayout);
        dialogSetWindowSizeLayout.setHorizontalGroup(
            dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogSetWindowSizeLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(moveWinY)
                    .addComponent(moveWinX)
                    .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(dialogSetWindowSizeLayout.createSequentialGroup()
                            .addComponent(jButton4)
                            .addGap(29, 29, 29)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                        .addGroup(dialogSetWindowSizeLayout.createSequentialGroup()
                            .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel8))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(newMapW, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(newMapH)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        dialogSetWindowSizeLayout.setVerticalGroup(
            dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogSetWindowSizeLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newMapH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newMapW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveWinX)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(moveWinY)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(dialogSetWindowSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(44, 44, 44))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseDragged(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });
        jTabbedPane1.addTab("Layer 0", jScrollPane1);

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });
        jTabbedPane1.addTab("Overlayer", jScrollPane2);
        jTabbedPane1.addTab("Objects", jScrollPane5);

        jMenu1.setText("File");

        jMenuItem6.setText("New");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Save");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Load");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem3.setText("Tileset");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Window...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem8.setText("Background");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem10.setText("Foreground");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText("Music");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setText("Gravity");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Add");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem5.setText("Object");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("View...");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Grid");
        jCheckBoxMenuItem1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxMenuItem1ItemStateChanged(evt);
            }
        });
        jMenu4.add(jCheckBoxMenuItem1);

        jMenuItem7.setText("Grid Color");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem9.setText("Preview Map");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        TilesetEditor te = new TilesetEditor(tileset, this);
        te.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        te.setVisible(true);
        //load Tile.
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
        
        
        
        if(map!= null){

            JViewport v = null;

            if(lastSelectedPanelIndex == 0)
            {
                v = jScrollPane1.getViewport();
            }else if(lastSelectedPanelIndex == 1)
            {
                v = jScrollPane2.getViewport();

            }else if(lastSelectedPanelIndex == 2)
            {
                v = jScrollPane5.getViewport();
            }

            lastSelectedPanelIndex = jTabbedPane1.getSelectedIndex();

            if(v!=null && v.getBounds()!=null){
                if(lastSelectedPanelIndex == 0)
                {
                    if(jScrollPane1.getViewport() != null)
                    jScrollPane1.getViewport().setViewPosition(new Point(v.getViewPosition().x,v.getViewPosition().y));//setBounds(v.getBounds().x, v.getBounds().y, v.getBounds().width, v.getBounds().height);
                }else if(lastSelectedPanelIndex == 1)
                {
                    if(jScrollPane2.getViewport()!=null)
                    jScrollPane2.getViewport().setViewPosition(new Point(v.getViewPosition().x,v.getViewPosition().y));//.getViewport().setBounds(v.getBounds().x, v.getBounds().y, v.getBounds().width, v.getBounds().height);

                }else if(lastSelectedPanelIndex == 2)
                {
                    if(jScrollPane5.getViewport()!=null)
                        jScrollPane5.getViewport().setViewPosition(new Point(v.getViewPosition().x,v.getViewPosition().y));//.getViewport().setBounds(v.getBounds().x, v.getBounds().y, v.getBounds().width, v.getBounds().height);
                }
            }
        }
        if(jTabbedPane1.getSelectedIndex() == 2)
        {
            
        }
        if(jTabbedPane1.getSelectedIndex() != selectedLayer)
        {
            //change layers.
        }
        //jScrollPane1.setViewport(null);
        
        jScrollPane1.updateUI();
        jScrollPane2.updateUI();
        jScrollPane5.updateUI();
        jTabbedPane1.updateUI();
        
        
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        if(tileset.getImage() == null)
        {
            JOptionPane.showMessageDialog(this, "Create a Tileset First!");
            return;
        }
        newMapDialog.setModal(true);
        newMapDialog.pack();
        newMapDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        //create new MAP
        int mapx = Integer.parseInt(mapTileX.getText());
        int mapy = Integer.parseInt(mapTileY.getText());
        int mapw = Integer.parseInt(mapWindowW.getText());
        int maph = Integer.parseInt(mapWindowH.getText());
        if(maph > mapx * tileset.getTileSize() || mapx > mapy * tileset.getTileSize())
        {
            JOptionPane.showMessageDialog(newMapDialog, "Map window size must be smaller then map size!","Map Size Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        map = new Map(tileset,mapx, mapy,mapw,maph,2);
        Dimension d = new Dimension(mapx * tileset.getTileSize(), mapy * tileset.getTileSize());
        panelLayer0.setPreferredSize(d);
        panelLayer0.setMinimumSize(d);
        panelLayer0.setMaximumSize(d);
        
        panelOverLayer.setPreferredSize(d);
        panelOverLayer.setMinimumSize(d);
        panelOverLayer.setMaximumSize(d);
        
        panelObjects.setPreferredSize(d);
        panelObjects.setMinimumSize(d);
        panelObjects.setMaximumSize(d);
        
        panelObjects.setMap(map);
        
        newMapDialog.setVisible(false);
        
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
        this.repaint();
        setWindowDialogConfigs();
        
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
        
        if(selectedTile == null)
            return;
        if(map == null)
            return;
        
        /*int x = 
        
        map.setTile(1, selectedTile, WIDTH, WIDTH);*/
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void jTabbedPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseDragged
        // TODO add your handling code here:
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
    }//GEN-LAST:event_jTabbedPane1MouseDragged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        //save map
        JFileChooser jfc = new JFileChooser(Config.MAPS_PATH);
        int op = jfc.showSaveDialog(this);
        if (op != JFileChooser.CANCEL_OPTION)
        {
            map.getObjects().clear();
            String f = jfc.getSelectedFile().getAbsolutePath();
            Vector<PanelWithThings.Thing> things = panelObjects.getThings();
            for(PanelWithThings.Thing t:things){
                BaseObject bo = (BaseObject) t.getThing();
                
                bo.setPosition(new Vector2D(t.getX()+bo.getCurrentSprite().getScenaryCollisionBox().getWidth()/2, 
                        map.getMapHeight() - (t.getY()+bo.getCurrentSprite().getImage().getHeight(null) - bo.getCurrentSprite().getScenaryCollisionBox().getLowerLeftPoint().y)));//+bo.getCurrentSprite().getScenaryCollisionBox().getHeight()));
                map.addObject(bo);
            }
            Map.save(map, f);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jCheckBoxMenuItem1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ItemStateChanged
        // TODO add your handling code here:
        viewGrid = jCheckBoxMenuItem1.isSelected();
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
    }//GEN-LAST:event_jCheckBoxMenuItem1ItemStateChanged

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        JColorChooser jcc = new JColorChooser(lineColor);
        lineColor
                = jcc.showDialog(this, "Choose color", Color.yellow);
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        //LOAD MAP
        JFileChooser jfc = new JFileChooser(Config.MAPS_PATH);
        int op = jfc.showOpenDialog(this);
        if (op != JFileChooser.CANCEL_OPTION)
        {
            String f = jfc.getSelectedFile().getAbsolutePath();
            map = Map.load(f);
            tileset = map.getTileset();
            panelLayer0.updateUI();
            panelOverLayer.updateUI();
            panelTile.updateUI();
            setWindowDialogConfigs();
            panelObjects.setMap(map);
            for(BaseObject b: map.getObjects())
            {
                panelObjects.addThing(b.getCurrentSprite().getImage(), 
                        (int) ((int) b.getPosition().getX()-b.getCurrentSprite().getScenaryCollisionBox().getWidth()/2), 
                        //(int) (map.getMapHeight() - b.getPosition().getY()-b.getCurrentSprite().getScenaryCollisionBox().getHeight()),
                        (int) (map.getMapHeight() - (b.getPosition().getY()+b.getCurrentSprite().getImage().getHeight(null) +b.getCurrentSprite().getScenaryCollisionBox().getLowerLeftPoint().y)),
                        true, b);
                }

            
        }
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        if(map == null)
        {
            JOptionPane.showMessageDialog(this, "Create a map first!");
            return;
        }
        new DialogAddObject(this, true, panelObjects, jScrollPane5, map).setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        dialogSetWindowSize.pack();
        dialogSetWindowSize.setModal(true);
        dialogSetWindowSize.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dialogSetWindowSize.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
        dialogSetWindowSize.pack();
        dialogSetWindowSize.setModal(true);
        dialogSetWindowSize.setVisible(true);
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int mapW = Integer.parseInt(newMapW.getText());
        int mapH = Integer.parseInt(newMapH.getText());
        if(mapH > map.getMapWidth() || mapW > map.getMapWidth())
        {
            JOptionPane.showMessageDialog(dialogSetWindowSize, "Map Window Size must be smaller than map size","Map Size Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        map.setMoveX(moveWinX.isSelected());
        map.setMoveY(moveWinY.isSelected());
        map.setWindowSize(mapW, mapH);
        panelLayer0.updateUI();
        panelOverLayer.updateUI();
        dialogSetWindowSize.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        new BackgroundEditor(this.map, this.map.getBackground()).setVisible(true);
        
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        map.getObjects().clear();
        
        Vector<PanelWithThings.Thing> things = panelObjects.getThings();
        for(PanelWithThings.Thing t:things){
            BaseObject bo = (BaseObject) t.getThing();

            bo.setPosition(new Vector2D(t.getX()+bo.getCurrentSprite().getScenaryCollisionBox().getWidth()/2, map.getMapHeight() - t.getY()-bo.getCurrentSprite().getScenaryCollisionBox().getHeight()));
            map.addObject(bo);
        }
        final TestBackgroundGame tbg = new TestBackgroundGame(map.getWindowWidth(), map.getWindowHeight(),60, map);
        TestFrame t = new TestFrame(tbg);
        
        t.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        t.setSize(map.getWindowWidth(), map.getWindowHeight());
        t.setVisible(true);
        
        new Thread(t).start();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        if(map == null)
        {
            JOptionPane.showMessageDialog(this, "Create a map first!");
            return;
        }
        mapMusic = GuiHelper.copyMusic(this);
        map.setMusic(mapMusic);
        
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        try{
            double g = Double.parseDouble(JOptionPane.showInputDialog("New Gravity (current: "+map.getGravity()+")"));
            map.setGravity(g);
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Gravity must be a double value!");
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        if(map.getForeground() == null)
            map.setForeground(new Background(map));
        new BackgroundEditor(this.map, this.map.getForeground()).setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MapEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MapEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MapEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MapEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapEditor().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog dialogSetWindowSize;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JTextField mapTileX;
    private javax.swing.JTextField mapTileY;
    private javax.swing.JTextField mapWindowH;
    private javax.swing.JTextField mapWindowW;
    private javax.swing.JCheckBox moveWinX;
    private javax.swing.JCheckBox moveWinY;
    private javax.swing.JDialog newMapDialog;
    private javax.swing.JTextField newMapH;
    private javax.swing.JTextField newMapW;
    // End of variables declaration//GEN-END:variables
}
