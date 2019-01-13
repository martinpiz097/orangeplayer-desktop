/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.JTableHeader;
import org.muplayer.audio.Player;
import org.muplayer.audio.SeekOption;
import org.orangeplayer.playerdesktop.R;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.gui.components.MenuButton;
import org.orangeplayer.playerdesktop.model.TMTracks;
import org.orangeplayer.playerdesktop.sys.ComponentManager;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;
import org.orangeplayer.playerdesktop.sys.SysInfo;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_DARK_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PLAYER_DARK_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PLAYER_ICON;

/**
 *
 * @author martin
 */
public class PlayerForm extends javax.swing.JFrame {

    private PlayerController controller;
    private JFileChooser musicChooser;
    
    public PlayerForm() {
//        JLayeredPane pane = new JLayeredPane();
//        pane.setLayout(new OverlayLayout(pane));
//        setContentPane(pane);
        initComponents();
        configSize();
        //lblCover.setIcon(SysUtil.getResizedIcon((ImageIcon) lblCover.getIcon()));
        //setResizable(false);
        musicChooser = new JFileChooser(new File("/home/"+System.getProperty("user.name")));
        configMusicChooser();
        configIconManager();
        setLocationRelativeTo(null);
        configMenuButtons();
        
        //btnShowMenu.setVisible(false);
        hideMenu();
        panelContent.updateUI();
        tableScroll.getViewport().setBackground(WHITE);
        tableScroll.getViewport().setForeground(R.colors.DARK_COLOR);
        
        JTableHeader header = tblTracks.getTableHeader();
        header.setBackground(R.colors.PRIMARY_COLOR);
        header.setForeground(WHITE);
        
        controller = new PlayerController(this);
        //controller.setColumnsLeghts(((TMTracks)tblTracks.getModel()).getMaxValuesLenghts());
        Session.getInstance().add(SessionKey.CONTROLLER, controller);
        Session.getInstance().add(SessionKey.DARK_MODE, false);
        
        ComponentManager.getInstance().add(panelContent, panelListTracks, panelControls,  
                panelButtons, panelTrackInfo, trackContainer, buttonsContainer, 
                lblAlbum, lblArtist, lblCover, lblDuration, lblTitle, volSlider, tableScroll, 
                btnMute, btnNext, btnPlay, btnPrev, btnSeekNext, btnSeekPrev, btnShowMenu);
    }
    
    private void configIconManager() {
        /*IconManager iconMgr = IconManager.newInstance(false);
        iconMgr.addComponent(btnMute, IconType.COVER);
        iconMgr.addComponent(btnMute, IconType.MENU);
        iconMgr.addComponent(btnMute, IconType.MUTE);
        iconMgr.addComponent(btnMute, IconType.NEXTFOLDER);
        iconMgr.addComponent(btnMute, IconType.NEXTTRACK);
        iconMgr.addComponent(btnMute, IconType.PAUSE);
        iconMgr.addComponent(btnMute, IconType.PLAY);
        iconMgr.addComponent(btnMute, IconType.PREVFOLDER);
        iconMgr.addComponent(btnMute, IconType.PREVTRACK);
        iconMgr.addComponent(btnMute, IconType.STOP);
        iconMgr.addComponent(btnMute, IconType.UNMUTE);*/
        
    }
    
    private void setDarkMode(boolean enable) {
        Class<? extends PlayerForm> clazz = getClass();
        Session.getInstance().set(SessionKey.DARK_MODE, enable);
        String muteIcon = controller.getPlayer().isMute() && controller.isAlive() 
                ? "mute.png" : "volume.png";
        
        if (enable) {
            ComponentManager.getInstance().configColors(R.colors.DARK_COLOR, WHITE);
            setBackground(R.colors.DARK_COLOR);
            setForeground(WHITE);
            btnMute.setIcon(new ImageIcon(clazz.getResource("/icons/dark/"+muteIcon)));
            btnNext.setIcon(new ImageIcon(clazz.getResource("/icons/dark/seekNext.png")));
            btnPlay.setIcon(new ImageIcon(clazz.getResource("/icons/dark/play.png")));
            btnPrev.setIcon(new ImageIcon(clazz.getResource("/icons/dark/seekPrev.png")));
            btnSeekNext.setIcon(new ImageIcon(clazz.getResource("/icons/dark/trackNext.png")));
            btnSeekPrev.setIcon(new ImageIcon(clazz.getResource("/icons/dark/trackPrev.png")));
            btnShowMenu.setIcon(new ImageIcon(clazz.getResource("/icons/dark/menu.png")));
            
            if (controller.isAlive())
                if (!controller.getPlayer().getCurrent().hasCover())
                    lblCover.setIcon(R.icons("dark/vinilo.png"));
            tableScroll.getViewport().setBackground(R.colors.DARK_COLOR);
            tableScroll.getViewport().setForeground(WHITE);
            tblTracks.setBackground(R.colors.DARK_COLOR);
            tblTracks.setForeground(WHITE);

            panelButtons.setBorder(new LineBorder(WHITE, 1));
            
            validate();
            
            
            /*Component[] components = panelMenu.getComponents();
            
            Component component;
            for (int i = 0; i < components.length; i++) {
                component = components[i];
                if (component instanceof JButton) {
                    ((JButton) component).setIcon(defaultIcon);
                }
            }*/
        }
        else {
            ComponentManager.getInstance().configColors(WHITE, BLACK);
            setBackground(WHITE);
            setForeground(BLACK);
            btnMute.setIcon(new ImageIcon(clazz.getResource("/icons/"+muteIcon)));
            btnNext.setIcon(new ImageIcon(clazz.getResource("/icons/seekNext.png")));
            btnPlay.setIcon(new ImageIcon(clazz.getResource("/icons/play.png")));
            btnPrev.setIcon(new ImageIcon(clazz.getResource("/icons/seekPrev.png")));
            btnSeekNext.setIcon(new ImageIcon(clazz.getResource("/icons/trackNext.png")));
            btnSeekPrev.setIcon(new ImageIcon(clazz.getResource("/icons/trackPrev.png")));
            btnShowMenu.setIcon(new ImageIcon(clazz.getResource("/icons/menu.png")));
            
            if (controller.isAlive())
                if (!controller.getPlayer().getCurrent().hasCover())
                    lblCover.setIcon(new ImageIcon(clazz.getResource("/icons/vinilo.png")));
            
            tableScroll.getViewport().setBackground(WHITE);
            tableScroll.getViewport().setForeground(BLACK);
            
            tblTracks.setBackground(WHITE);
            tblTracks.setForeground(BLACK);
            
            panelButtons.setBorder(new LineBorder(BLACK, 1));
            
            validate();
        }
    }

    private void showMenu() {
        panelMenu.setVisible(true);
        btnShowMenu.setVisible(false);
    }
    
    private void hideMenu() {
        panelMenu.setVisible(false);
        btnShowMenu.setVisible(true);
    }
    
    private void configSize() {
        Dimension displaySize = SysInfo.DISPLAY_SIZE;
        Dimension preferredSize = getPreferredSize();
        //Dimension newSize = new Dimension((int)(displaySize.getWidth()/1.5), (int)(displaySize.getHeight()/1.5));
        //setSize(newSize);
        setSize(preferredSize);
        //setMinimumSize(panelContent.getPreferredSize());
        //setMinimumSize(preferredSize);
    }
    
     private void configMusicChooser() {
        musicChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Solo Carpetas";
            }
        });
        musicChooser.setDialogTitle("Biblioteca de Música");
        musicChooser.setMultiSelectionEnabled(true);
        musicChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
     
    private void configMenuButtons() {
        MenuButton btnAddMusic = new MenuButton("Cargar Música", R.icons("menu/open.png"));
        btnAddMusic.addActionListener((ActionEvent e) -> {
            musicChooser.showOpenDialog(null);
            //File selected = musicChooser.getSelectedFile();
            File[] selectedFiles = musicChooser.getSelectedFiles();
            
            if (selectedFiles != null) {
                Player player = controller.getPlayer();
                for (int i = 0; i < selectedFiles.length; i++)
                    player.addMusic(selectedFiles[i]);
              
                if (player.hasSounds())
                    if (controller.isAlive()) {
                        ((TMTracks)tblTracks.getModel()).loadList();
                        int indexOf = player.getListSoundPaths().indexOf(player.getCurrent().getDataSource().getPath());
                        tblTracks.getSelectionModel().setSelectionInterval(0, indexOf);
                        tblTracks.updateUI();
                    }
                    else
                        controller.start();
                else
                    JOptionPane.showMessageDialog(this, "No se ha encontrado música");
                musicChooser.setSelectedFiles(null);
            }
        });
        MenuButton btnClearList = new MenuButton("Limpiar Todo", R.icons("menu/clear.png"));
        btnClearList.addActionListener((ActionEvent e) -> {
            if (controller.isAlive()) {
                controller.shutdown();
                controller = new PlayerController(PlayerForm.this);
                Session session = Session.getInstance();
                session.set(SessionKey.GAIN, (int) 85);
                session.set(SessionKey.CONTROLLER, controller);
                //((TMTracks) tblTracks.getModel()).getTracks().clear();
                tblTracks.setModel(new TMTracks());
                tblTracks.updateUI();
                final String defaultText = "[No Song]";
                lblAlbum.setText(defaultText);
                lblArtist.setText(defaultText);
                lblDuration.setText(defaultText);
                lblTitle.setText(defaultText);
                boolean darkMode = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
                lblCover.setIcon(darkMode ? R.icons("dark/vinilo.png")
                        : R.icons("vinilo.png"));
                barProgress.setValue(0);
                barProgress.setString("00:00");
                volSlider.setValue(0);
                volSlider.setToolTipText("0");
                if (darkMode) {
                    btnPlay.setIcon(R.icons("dark/play.png"));
                    btnPlay.setBackground(BLACK);
                }
                else {
                    btnPlay.setIcon(R.icons("play.png"));
                    btnPlay.setBackground(WHITE);
                }
            }
            else
                JOptionPane.showMessageDialog(this, "La lista ya se encuentra vacía");
        });
        panelMenu.add(btnAddMusic);
        panelMenu.add(btnClearList);
        
        panelMenu.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        panelMenu = new javax.swing.JPanel();
        panelContent = new javax.swing.JPanel();
        btnShowMenu = new javax.swing.JButton();
        trackContainer = new javax.swing.JPanel();
        panelTrackInfo = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblAlbum = new javax.swing.JLabel();
        lblArtist = new javax.swing.JLabel();
        lblDuration = new javax.swing.JLabel();
        lblCover = new javax.swing.JLabel();
        buttonsContainer = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnPrev = new javax.swing.JButton();
        btnSeekPrev = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnSeekNext = new javax.swing.JButton();
        panelControls = new javax.swing.JPanel();
        btnMute = new javax.swing.JButton();
        barProgress = new javax.swing.JProgressBar();
        volSlider = new javax.swing.JSlider();
        panelListTracks = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tblTracks = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        itemDarkMode = new javax.swing.JCheckBoxMenuItem();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Orange Player");
        setBackground(java.awt.Color.white);

        panelMenu.setBackground(R.colors.PRIMARY_COLOR);
        panelMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelMenu.setForeground(java.awt.Color.black);
        panelMenu.setName(""); // NOI18N
        panelMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelMenuMouseExited(evt);
            }
        });
        panelMenu.setLayout(new java.awt.GridLayout(100, 1, 0, 5));

        panelContent.setBackground(java.awt.Color.white);
        panelContent.setName(""); // NOI18N
        panelContent.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                panelContentMouseMoved(evt);
            }
        });
        panelContent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelContentMouseClicked(evt);
            }
        });

        btnShowMenu.setBackground(java.awt.Color.white);
        btnShowMenu.setForeground(java.awt.Color.white);
        btnShowMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu.png"))); // NOI18N
        btnShowMenu.setBorder(null);
        btnShowMenu.setBorderPainted(false);
        btnShowMenu.setFocusPainted(false);
        btnShowMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnShowMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowMenuActionPerformed(evt);
            }
        });

        trackContainer.setBackground(java.awt.Color.white);
        trackContainer.setForeground(java.awt.Color.black);

        panelTrackInfo.setBackground(java.awt.Color.white);
        panelTrackInfo.setForeground(java.awt.Color.black);
        panelTrackInfo.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        lblTitle.setBackground(java.awt.Color.white);
        lblTitle.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lblTitle.setForeground(java.awt.Color.black);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("[No Song]");
        lblTitle.setOpaque(true);
        panelTrackInfo.add(lblTitle);

        lblAlbum.setBackground(java.awt.Color.white);
        lblAlbum.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblAlbum.setForeground(java.awt.Color.black);
        lblAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlbum.setText("[No Song]");
        lblAlbum.setOpaque(true);
        panelTrackInfo.add(lblAlbum);

        lblArtist.setBackground(java.awt.Color.white);
        lblArtist.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblArtist.setForeground(java.awt.Color.black);
        lblArtist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblArtist.setText("[No Song]");
        lblArtist.setOpaque(true);
        panelTrackInfo.add(lblArtist);

        lblDuration.setBackground(java.awt.Color.white);
        lblDuration.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblDuration.setForeground(java.awt.Color.black);
        lblDuration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDuration.setText("[No Song]");
        lblDuration.setOpaque(true);
        panelTrackInfo.add(lblDuration);

        lblCover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/vinilo.png"))); // NOI18N
        lblCover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCoverMouseClicked(evt);
            }
        });

        buttonsContainer.setBackground(java.awt.Color.white);
        buttonsContainer.setForeground(java.awt.Color.black);
        buttonsContainer.setToolTipText("");

        panelButtons.setBackground(java.awt.Color.white);
        panelButtons.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelButtons.setForeground(java.awt.Color.black);
        panelButtons.setToolTipText("");
        panelButtons.setLayout(new java.awt.GridLayout(1, 5, 10, 0));

        btnPrev.setBackground(java.awt.Color.white);
        btnPrev.setForeground(java.awt.Color.black);
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/trackPrev.png"))); // NOI18N
        btnPrev.setBorderPainted(false);
        btnPrev.setFocusPainted(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPrevMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPrevMouseEntered(evt);
            }
        });
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        panelButtons.add(btnPrev);

        btnSeekPrev.setBackground(java.awt.Color.white);
        btnSeekPrev.setForeground(java.awt.Color.black);
        btnSeekPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/seekPrev.png"))); // NOI18N
        btnSeekPrev.setBorderPainted(false);
        btnSeekPrev.setFocusPainted(false);
        btnSeekPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSeekPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSeekPrevMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSeekPrevMouseEntered(evt);
            }
        });
        btnSeekPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeekPrevActionPerformed(evt);
            }
        });
        panelButtons.add(btnSeekPrev);

        btnPlay.setBackground(java.awt.Color.white);
        btnPlay.setForeground(java.awt.Color.black);
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play.png"))); // NOI18N
        btnPlay.setBorderPainted(false);
        btnPlay.setFocusPainted(false);
        btnPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPlayMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPlayMouseEntered(evt);
            }
        });
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        panelButtons.add(btnPlay);

        btnNext.setBackground(java.awt.Color.white);
        btnNext.setForeground(java.awt.Color.black);
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/seekNext.png"))); // NOI18N
        btnNext.setBorderPainted(false);
        btnNext.setFocusPainted(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNextMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNextMouseEntered(evt);
            }
        });
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        panelButtons.add(btnNext);

        btnSeekNext.setBackground(java.awt.Color.white);
        btnSeekNext.setForeground(java.awt.Color.black);
        btnSeekNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/trackNext.png"))); // NOI18N
        btnSeekNext.setBorderPainted(false);
        btnSeekNext.setFocusPainted(false);
        btnSeekNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSeekNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSeekNextMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSeekNextMouseEntered(evt);
            }
        });
        btnSeekNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeekNextActionPerformed(evt);
            }
        });
        panelButtons.add(btnSeekNext);

        org.jdesktop.layout.GroupLayout buttonsContainerLayout = new org.jdesktop.layout.GroupLayout(buttonsContainer);
        buttonsContainer.setLayout(buttonsContainerLayout);
        buttonsContainerLayout.setHorizontalGroup(
            buttonsContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, buttonsContainerLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .add(10, 10, 10))
        );
        buttonsContainerLayout.setVerticalGroup(
            buttonsContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, Short.MAX_VALUE)
        );

        panelControls.setBackground(java.awt.Color.white);
        panelControls.setForeground(java.awt.Color.black);

        btnMute.setBackground(java.awt.Color.white);
        btnMute.setForeground(java.awt.Color.black);
        btnMute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/volume.png"))); // NOI18N
        btnMute.setBorderPainted(false);
        btnMute.setFocusPainted(false);
        btnMute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuteActionPerformed(evt);
            }
        });

        barProgress.setBackground(java.awt.Color.white);
        barProgress.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        barProgress.setForeground(R.colors.PRIMARY_COLOR);
        barProgress.setString("00:00");
        barProgress.setStringPainted(true);
        barProgress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barProgressMouseClicked(evt);
            }
        });

        volSlider.setBackground(java.awt.Color.white);
        volSlider.setForeground(R.colors.PRIMARY_COLOR
        );
        volSlider.setValue(85);
        volSlider.setValueIsAdjusting(true);
        volSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volSliderStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout panelControlsLayout = new org.jdesktop.layout.GroupLayout(panelControls);
        panelControls.setLayout(panelControlsLayout);
        panelControlsLayout.setHorizontalGroup(
            panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelControlsLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(barProgress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelControlsLayout.createSequentialGroup()
                        .add(btnMute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(volSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(0, 0, 0))
        );
        panelControlsLayout.setVerticalGroup(
            panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelControlsLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(barProgress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(volSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .add(btnMute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .add(0, 0, 0))
        );

        org.jdesktop.layout.GroupLayout trackContainerLayout = new org.jdesktop.layout.GroupLayout(trackContainer);
        trackContainer.setLayout(trackContainerLayout);
        trackContainerLayout.setHorizontalGroup(
            trackContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackContainerLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(trackContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(panelControls, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblCover, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelTrackInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(buttonsContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        trackContainerLayout.setVerticalGroup(
            trackContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackContainerLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelTrackInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(10, 10, 10)
                .add(lblCover)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelControls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10))
        );

        org.jdesktop.layout.GroupLayout panelContentLayout = new org.jdesktop.layout.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentLayout.createSequentialGroup()
                .addContainerGap()
                .add(btnShowMenu)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(trackContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(10, 10, 10))
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(trackContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnShowMenu))
                .add(0, 0, 0))
        );

        panelListTracks.setBackground(java.awt.Color.white);
        panelListTracks.setForeground(java.awt.Color.black);
        panelListTracks.setLayout(new java.awt.BorderLayout());

        tableScroll.setBackground(java.awt.Color.white);
        tableScroll.setForeground(java.awt.Color.black);

        tblTracks.setBackground(java.awt.Color.white);
        tblTracks.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        tblTracks.setForeground(java.awt.Color.black);
        tblTracks.setModel(new TMTracks());
        tblTracks.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblTracks.setGridColor(java.awt.Color.white);
        tblTracks.setSelectionBackground(R.colors.SECUNDARY_COLOR);
        tblTracks.setSelectionForeground(java.awt.Color.black);
        tblTracks.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTracks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTracksMouseClicked(evt);
            }
        });
        tableScroll.setViewportView(tblTracks);

        panelListTracks.add(tableScroll, java.awt.BorderLayout.CENTER);

        menuBar.setBackground(new java.awt.Color(238, 238, 238));
        menuBar.setForeground(java.awt.Color.black);

        jMenu1.setText("File");
        menuBar.add(jMenu1);

        jMenu2.setText("Edit");
        menuBar.add(jMenu2);

        jMenu3.setText("Interfaz");

        itemDarkMode.setSelected(false);
        itemDarkMode.setText("Modo Nocturno");
        itemDarkMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDarkModeActionPerformed(evt);
            }
        });
        jMenu3.add(itemDarkMode);

        menuBar.add(jMenu3);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(panelMenu, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(panelContent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(panelListTracks, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMenu, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(panelContent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(panelListTracks, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMenuActionPerformed
        showMenu();
    }//GEN-LAST:event_btnShowMenuActionPerformed

    private void btnPlayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPlayMouseEntered
        btnPlay.setBackground(R.colors.PRIMARY_COLOR);
    }//GEN-LAST:event_btnPlayMouseEntered

    private void btnNextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextMouseEntered
        btnNext.setBackground(R.colors.PRIMARY_COLOR);                                    
    }//GEN-LAST:event_btnNextMouseEntered

    private void btnSeekNextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeekNextMouseEntered
        btnSeekNext.setBackground(R.colors.PRIMARY_COLOR);
    }//GEN-LAST:event_btnSeekNextMouseEntered

    private void btnPrevMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevMouseEntered
        btnPrev.setBackground(R.colors.PRIMARY_COLOR);
    }//GEN-LAST:event_btnPrevMouseEntered

    private void btnSeekPrevMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeekPrevMouseEntered
        btnSeekPrev.setBackground(R.colors.PRIMARY_COLOR);
    }//GEN-LAST:event_btnSeekPrevMouseEntered

    private void btnPlayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPlayMouseExited
        boolean dark = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        btnPlay.setBackground(dark?R.colors.DARK_COLOR:WHITE);
    }//GEN-LAST:event_btnPlayMouseExited

    private void btnNextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextMouseExited
        boolean dark = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        btnNext.setBackground(dark?R.colors.DARK_COLOR:WHITE);
    }//GEN-LAST:event_btnNextMouseExited

    private void btnSeekNextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeekNextMouseExited
        boolean dark = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        btnSeekNext.setBackground(dark?R.colors.DARK_COLOR:WHITE);
    }//GEN-LAST:event_btnSeekNextMouseExited

    private void btnSeekPrevMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeekPrevMouseExited
        boolean dark = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        btnSeekPrev.setBackground(dark?R.colors.DARK_COLOR:WHITE);
    }//GEN-LAST:event_btnSeekPrevMouseExited

    private void btnPrevMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevMouseExited
        boolean dark = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        btnPrev.setBackground(dark?R.colors.DARK_COLOR:WHITE);
    }//GEN-LAST:event_btnPrevMouseExited

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        if (controller.isAlive()) {
            boolean darkEnabled = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
            Player player = controller.getPlayer();
            if (player.isPlaying()) {
                player.pause();
                btnPlay.setIcon(darkEnabled?PLAYER_DARK_ICON:PLAYER_ICON);
            }
            else {
                player.resumeTrack();
                btnPlay.setIcon(darkEnabled?PAUSE_DARK_ICON:PAUSE_ICON);
            }
        }
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if (controller.isAlive())
            controller.getPlayer().playNext();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnSeekPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeekPrevActionPerformed
        if (controller.isAlive())
            controller.getPlayer().playPrevious();
    }//GEN-LAST:event_btnSeekPrevActionPerformed

    private void btnSeekNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeekNextActionPerformed
        if (controller.isAlive())
            controller.getPlayer().seekFolder(SeekOption.NEXT);
    }//GEN-LAST:event_btnSeekNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        if (controller.isAlive())
            controller.getPlayer().seekFolder(SeekOption.PREV);
    }//GEN-LAST:event_btnPrevActionPerformed

    private void barProgressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barProgressMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            Player player = controller.getPlayer();
            
            Rectangle bounds = barProgress.getBounds();
            int position = evt.getX();
            double maxX = bounds.getMaxX();
            double minX = bounds.getMinX();
            int distance = (int) (maxX-minX);
            long trackDuration = player.getCurrent().getDuration();
            long goTo = (position*trackDuration) / distance;
            try {
                barProgress.setValue((int) goTo);
                player.getCurrent().gotoSecond(goTo);
                
                
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                Logger.getLogger(PlayerForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_barProgressMouseClicked

    private void tblTracksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTracksMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && 
                evt.getClickCount() == 2) {
            int selectedRow = tblTracks.getSelectedRow();
            controller.getPlayer().play(selectedRow);
            //Thread.sleep(500);
            //System.out.println("Current: "+controller.getPlayer().getCurrent().getTitle());
        }
    }//GEN-LAST:event_tblTracksMouseClicked

    private void lblCoverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCoverMouseClicked
        hideMenu();
    }//GEN-LAST:event_lblCoverMouseClicked

    private void panelContentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelContentMouseClicked
        hideMenu();
    }//GEN-LAST:event_panelContentMouseClicked

    private void panelMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMenuMouseExited
        /*Point point = evt.getPoint();
        Rectangle bounds = panelMenu.getBounds();
        if (point.x > bounds.x && point.y > bounds.y) {
            hideMenu();
        }*/
    }//GEN-LAST:event_panelMenuMouseExited

    private void panelContentMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelContentMouseMoved
        int x = evt.getX();
        if (x - evt.getComponent().getBounds().getMinX() < 21)
            showMenu();
        else
            hideMenu();
    }//GEN-LAST:event_panelContentMouseMoved

    private void itemDarkModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDarkModeActionPerformed
        setDarkMode(itemDarkMode.isSelected());
    }//GEN-LAST:event_itemDarkModeActionPerformed

    private void volSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volSliderStateChanged
        if (controller.isAlive()) {
            int vol = volSlider.getValue();
            controller.getPlayer().setGain(vol);
            
            // cambiar icono del boton
            
        }
    }//GEN-LAST:event_volSliderStateChanged

    private void btnMuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuteActionPerformed
         if (controller.isAlive()) {
            boolean dark = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
            String iconPath = dark ? "/icons/dark/" : "/icons/";
            
            Player player = controller.getPlayer();
            if (player.isMute()) {
                player.unmute();
                volSlider.setValue((int) Session.getInstance().get(SessionKey.GAIN));
                Session.getInstance().remove(SessionKey.GAIN);
                btnMute.setIcon(new ImageIcon(getClass().getResource(iconPath+"volume.png")));
            }
            else {
                btnMute.setIcon(new ImageIcon(getClass().getResource(iconPath+"mute.png")));
                Session.getInstance().add(SessionKey.GAIN, (int)player.getGain());
                player.mute();
                volSlider.setValue(0);
            }
        }
    }//GEN-LAST:event_btnMuteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        
        java.awt.EventQueue.invokeLater(() -> {
            new PlayerForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barProgress;
    private javax.swing.JButton btnMute;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSeekNext;
    private javax.swing.JButton btnSeekPrev;
    private javax.swing.JButton btnShowMenu;
    private javax.swing.JPanel buttonsContainer;
    private javax.swing.JCheckBoxMenuItem itemDarkMode;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelControls;
    private javax.swing.JPanel panelListTracks;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelTrackInfo;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JTable tblTracks;
    private javax.swing.JPanel trackContainer;
    private javax.swing.JSlider volSlider;
    // End of variables declaration//GEN-END:variables

    public PlayerController getController() {
        return controller;
    }

    public JFileChooser getMusicChooser() {
        return musicChooser;
    }

    public JProgressBar getBarProgress() {
        return barProgress;
    }

    public JButton getBtnMute() {
        return btnMute;
    }

    public JButton getBtnNext() {
        return btnNext;
    }

    public JButton getBtnPlay() {
        return btnPlay;
    }

    public JButton getBtnPrev() {
        return btnPrev;
    }

    public JButton getBtnSeekNext() {
        return btnSeekNext;
    }

    public JButton getBtnSeekPrev() {
        return btnSeekPrev;
    }

    public JButton getBtnShowMenu() {
        return btnShowMenu;
    }

    public JPanel getButtonsContainer() {
        return buttonsContainer;
    }

    public JScrollPane getjScrollPane1() {
        return tableScroll;
    }

    public JTable getjTable1() {
        return tblTracks;
    }

    public JLabel getLblAlbum() {
        return lblAlbum;
    }

    public JLabel getLblArtist() {
        return lblArtist;
    }

    public JLabel getLblCover() {
        return lblCover;
    }

    public JLabel getLblDuration() {
        return lblDuration;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public JPanel getPanelButtons() {
        return panelButtons;
    }

    public JPanel getPanelContent() {
        return panelContent;
    }

    public JPanel getPanelListTracks() {
        return panelListTracks;
    }

    public JPanel getPanelMenu() {
        return panelMenu;
    }

    public JPanel getPanelTrackInfo() {
        return panelTrackInfo;
    }

    public JPanel getTrackContainer() {
        return trackContainer;
    }

    public JSlider getVolSlider() {
        return volSlider;
    }

    public JTable getTblTracks() {
        return tblTracks;
    }
    
    
}
