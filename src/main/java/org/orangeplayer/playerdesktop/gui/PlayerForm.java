/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui;

import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
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

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.mpizexternal.MaterialLookAndFeel;
import org.muplayer.audio.Track;
import org.muplayer.audio.util.AudioExtensions;
import org.muplayer.system.AudioUtil;
import static org.orangeplayer.playerdesktop.sys.SysInfo.*;
import static org.orangeplayer.playerdesktop.gui.util.ComponentUtil.*;
import org.orangeplayer.playerdesktop.gui.util.Toast;
import org.orangeplayer.playerdesktop.sys.MemoryCleaner;


/**
 *
 * @author martin
 */
public class PlayerForm extends javax.swing.JFrame {

    private PlayerController controller;
    private JFileChooser musicChooser;

    private UIConfig config;
    
    public PlayerForm() {
//        JLayeredPane pane = new JLayeredPane();
//        pane.setLayout(new OverlayLayout(pane));
//        setContentPane(pane);

        config = UIConfig.getInstance();
        initComponents();
        controller = new PlayerController(this);
        
        configSize();
        musicChooser = new JFileChooser(new File("/home/"+System.getProperty("user.name")));
        configMusicChooser();
        configIconManager();
        setLocationRelativeTo(null);
        configMenuButtons();

        //hideMenu();
        showMenu();
        
        panelContent.updateUI();
        tableScroll.getViewport().setBackground(WHITE);
        tableScroll.getViewport().setForeground(R.colors.DARK_COLOR);

        JTableHeader header = tblTracks.getTableHeader();
        header.setBackground(R.colors.PRIMARY_COLOR);
        header.setForeground(WHITE);

        //controller.setColumnsLeghts(((TMTracks)tblTracks.getModel()).getMaxValuesLenghts());
        Session session = Session.getInstance();
        session.add(SessionKey.CONTROLLER, controller);
        session.add(SessionKey.DARK_MODE, false);
        session.add(SessionKey.GAIN, (int)controller.getPlayer().getGain());

        ComponentManager.getInstance().add(panelContent, panelListTracks, panelControls, panelContainer, splitMusic,
                panelButtons, panelTrackInfo, trackContainer, buttonsContainer,
                lblAlbum, lblArtist, lblCover, lblDuration, lblTitle, lblVol, volSlider, tableScroll, 
                btnMute, btnNext, btnPlay, btnPrev, btnSeekNext, btnSeekPrev, btnShowMenu);
        
        if (Boolean.parseBoolean(config.getProperty(UIConfig.KEYS.DARK_MODE))) {
            setDarkMode(true);
            itemDarkMode.setSelected(true);
        }
        configTransitions();
    }
    
    private void configTransitions() {
        Component[] components = panelButtons.getComponents();
        final int componentsCount = components.length;
        Component comp;
        JButton btn;
        
        for (int i = 0; i < componentsCount; i++) {
            comp = components[i];
            if (comp instanceof JButton)
                asignTransition((JButton)comp, R.colors.PRIMARY_COLOR);
        }
        asignTransition(btnMute, R.colors.PRIMARY_COLOR);
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
            btnMute.setIcon(R.icons("dark/"+muteIcon));
            btnNext.setIcon(R.icons("dark/seekNext.png"));
            btnPlay.setIcon(R.icons("dark/play.png"));
            btnPrev.setIcon(R.icons("dark/seekPrev.png"));
            btnSeekNext.setIcon(R.icons("dark/trackNext.png"));
            btnSeekPrev.setIcon(R.icons("dark/trackPrev.png"));
            btnShowMenu.setIcon(R.icons("dark/menu.png"));

            if ((controller.isAlive() && !controller.getPlayer().getCurrent().hasCover())
                    || (!controller.isAlive()))
                lblCover.setIcon(R.icons("dark/vinilo.png"));
            tableScroll.getViewport().setBackground(R.colors.DARK_COLOR);
            tableScroll.getViewport().setForeground(WHITE);
            tblTracks.setBackground(R.colors.DARK_COLOR);
            tblTracks.setForeground(WHITE);

            //panelButtons.setBorder(new LineBorder(WHITE, 1));

            splitMusic.setBackground(R.colors.DARK_COLOR);
            
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
            btnMute.setIcon(R.icons(muteIcon));
            btnNext.setIcon(R.icons("seekNext.png"));
            btnPlay.setIcon(R.icons("play.png"));
            btnPrev.setIcon(R.icons("seekPrev.png"));
            btnSeekNext.setIcon(R.icons("trackNext.png"));
            btnSeekPrev.setIcon(R.icons("trackPrev.png"));
            btnShowMenu.setIcon(R.icons("menu.png"));

           if ((controller.isAlive() && !controller.getPlayer().getCurrent().hasCover())
                    || (!controller.isAlive()))
                lblCover.setIcon(R.icons("vinilo.png"));

            tableScroll.getViewport().setBackground(WHITE);
            tableScroll.getViewport().setForeground(BLACK);

            tblTracks.setBackground(WHITE);
            tblTracks.setForeground(BLACK);

            //panelButtons.setBorder(new LineBorder(BLACK, 1));

            splitMusic.setBackground(WHITE);
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
        Dimension maximumSize = getMaximumSize();
        
        Dimension newSize = new Dimension(displaySize);
        newSize.setSize(newSize.getWidth()/1.5, newSize.getHeight()/1.64);
        setSize(newSize);
        //setSize(preferredSize);
        
        setResizable(false);
        //setMinimumSize(preferredSize);
        //setMinimumSize(preferredSize);
    }

     private void configMusicChooser() {
        musicChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String[] supported = AudioExtensions.SUPPORTEDEXTENSIONS;
                return f.isDirectory() || Arrays.stream(supported)
                        .parallel()
                        .anyMatch(extension -> f.getName().endsWith(extension));
            }

            @Override
            public String getDescription() {
                return "Solo Carpetas y archivos soportados";
            }
        });
        musicChooser.setDialogTitle("Biblioteca de Música");
        musicChooser.setMultiSelectionEnabled(true);
        musicChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private void configMenuButtons() {
        MenuButton btnAddMusic = new MenuButton("Cargar Música", R.icons("menu/open.png"));
        btnAddMusic.addActionListener((ActionEvent e) -> {
            itemAddFolder.doClick();
        });

        MenuButton btnAddSongs = new MenuButton("Cargar Canciones", R.icons("menu/open.png"));
        btnAddSongs.addActionListener((ActionEvent e) -> {
            itemAddSongs.doClick();
        });

        //MenuButton btnAddSongs = new MenuButton("Cargar Canciones", R.icons("menu/open.png"));
//        btnAddSongs.addActionListener((ActionEvent e) -> {
//            itemAddSongs.doClick();
//        });

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
        panelContainer = new javax.swing.JPanel();
        splitMusic = new javax.swing.JSplitPane();
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
        lblVol = new javax.swing.JLabel();
        panelListTracks = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tblTracks = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemOpenFolder = new javax.swing.JMenuItem();
        itemOpenFile = new javax.swing.JMenuItem();
        itemAddFolder = new javax.swing.JMenuItem();
        itemAddSongs = new javax.swing.JMenuItem();
        itemExit = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        itemDarkMode = new javax.swing.JCheckBoxMenuItem();
        itemChangeUi = new javax.swing.JMenu();
        optionDefault = new javax.swing.JRadioButtonMenuItem();
        optSystem = new javax.swing.JRadioButtonMenuItem();
        optionMetal = new javax.swing.JRadioButtonMenuItem();
        optionNimbus = new javax.swing.JRadioButtonMenuItem();
        optionGtk = new javax.swing.JRadioButtonMenuItem();
        optMaterial = new javax.swing.JRadioButtonMenuItem();
        optionMac = new javax.swing.JRadioButtonMenuItem();
        itemRestoreConfig = new javax.swing.JMenuItem();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Orange Player");
        setBackground(java.awt.Color.white);
        setIconImage(R.icons("logo1.1.png").getImage()
        );

        panelMenu.setBackground(R.colors.PRIMARY_COLOR);
        panelMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelMenu.setForeground(java.awt.Color.black);
        panelMenu.setName(""); // NOI18N
        panelMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelMenuMouseExited(evt);
            }
        });
        panelMenu.setLayout(new java.awt.GridLayout(100, 1));

        panelContainer.setBackground(java.awt.Color.white);
        panelContainer.setForeground(java.awt.Color.black);
        panelContainer.setLayout(new java.awt.BorderLayout(5, 5));

        splitMusic.setBackground(java.awt.Color.white);
        splitMusic.setDividerSize(3);
        splitMusic.setContinuousLayout(true);

        panelContent.setBackground(java.awt.Color.white);
        panelContent.setForeground(java.awt.Color.black);
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
        panelTrackInfo.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

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
        panelButtons.setForeground(java.awt.Color.black);
        panelButtons.setToolTipText("");
        panelButtons.setLayout(new java.awt.GridLayout(1, 5, 10, 0));

        btnPrev.setBackground(java.awt.Color.white);
        btnPrev.setForeground(java.awt.Color.black);
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/trackPrev.png"))); // NOI18N
        btnPrev.setBorderPainted(false);
        btnPrev.setFocusPainted(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
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
            .add(buttonsContainerLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 373, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        buttonsContainerLayout.setVerticalGroup(
            buttonsContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonsContainerLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(10, 10, 10))
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

        lblVol.setBackground(java.awt.Color.white);
        lblVol.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblVol.setForeground(java.awt.Color.black);
        lblVol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVol.setText("85");

        org.jdesktop.layout.GroupLayout panelControlsLayout = new org.jdesktop.layout.GroupLayout(panelControls);
        panelControls.setLayout(panelControlsLayout);
        panelControlsLayout.setHorizontalGroup(
            panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelControlsLayout.createSequentialGroup()
                .add(panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(barProgress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelControlsLayout.createSequentialGroup()
                        .add(btnMute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(volSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 322, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)
                        .add(lblVol, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(3, 3, 3))
        );
        panelControlsLayout.setVerticalGroup(
            panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelControlsLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(barProgress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(panelControlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(volSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(btnMute, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblVol, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(0, 0, 0))
        );

        org.jdesktop.layout.GroupLayout trackContainerLayout = new org.jdesktop.layout.GroupLayout(trackContainer);
        trackContainer.setLayout(trackContainerLayout);
        trackContainerLayout.setHorizontalGroup(
            trackContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackContainerLayout.createSequentialGroup()
                .add(trackContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(panelTrackInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblCover, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(buttonsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, Short.MAX_VALUE))
            .add(trackContainerLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelControls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .add(16, 16, 16)
                .add(buttonsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout panelContentLayout = new org.jdesktop.layout.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(btnShowMenu, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(10, 10, 10)
                .add(trackContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelContentLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(panelContentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(trackContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnShowMenu))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        splitMusic.setLeftComponent(panelContent);

        panelListTracks.setLayout(new java.awt.BorderLayout(10, 10));

        tableScroll.setBackground(java.awt.Color.white);
        tableScroll.setForeground(java.awt.Color.black);

        tblTracks.setBackground(java.awt.Color.white);
        tblTracks.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        tblTracks.setForeground(java.awt.Color.black);
        tblTracks.setModel(new TMTracks());
        tblTracks.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tblTracks.setGridColor(java.awt.Color.white);
        tblTracks.setRowHeight(20);
        tblTracks.setRowMargin(5);
        tblTracks.setSelectionBackground(R.colors.SECUNDARY_COLOR);
        tblTracks.setSelectionForeground(java.awt.Color.white);
        tblTracks.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTracks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTracksMouseClicked(evt);
            }
        });
        tblTracks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTracksKeyPressed(evt);
            }
        });
        tableScroll.setViewportView(tblTracks);

        panelListTracks.add(tableScroll, java.awt.BorderLayout.CENTER);

        splitMusic.setRightComponent(panelListTracks);

        panelContainer.add(splitMusic, java.awt.BorderLayout.CENTER);

        menuBar.setBackground(new java.awt.Color(238, 238, 238));
        menuBar.setForeground(java.awt.Color.black);

        jMenu1.setText("Archivo");

        itemOpenFolder.setText("Abrir Carpeta");
        itemOpenFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemOpenFolderActionPerformed(evt);
            }
        });
        jMenu1.add(itemOpenFolder);

        itemOpenFile.setText("Abrir Archivo");
        itemOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemOpenFileActionPerformed(evt);
            }
        });
        jMenu1.add(itemOpenFile);

        itemAddFolder.setText("Agregar Carpeta");
        itemAddFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAddFolderActionPerformed(evt);
            }
        });
        jMenu1.add(itemAddFolder);

        itemAddSongs.setText("Agregar Canciones");
        jMenu1.add(itemAddSongs);

        itemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        itemExit.setText("Salir");
        itemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExitActionPerformed(evt);
            }
        });
        jMenu1.add(itemExit);

        menuBar.add(jMenu1);

        jMenu3.setText("Interfaz");

        itemDarkMode.setSelected(false);
        itemDarkMode.setText("Modo Nocturno");
        itemDarkMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDarkModeActionPerformed(evt);
            }
        });
        jMenu3.add(itemDarkMode);

        itemChangeUi.setText("Cambiar Tema");

        optionDefault.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optionDefault.setSelected(true);
        optionDefault.setText("Default");
        optionDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionDefaultActionPerformed(evt);
            }
        });
        itemChangeUi.add(optionDefault);

        optSystem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optSystem.setText("System");
        optSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optSystemActionPerformed(evt);
            }
        });
        itemChangeUi.add(optSystem);

        optionMetal.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optionMetal.setText("Metal");
        optionMetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionMetalActionPerformed(evt);
            }
        });
        itemChangeUi.add(optionMetal);

        optionNimbus.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optionNimbus.setText("Nimbus");
        optionNimbus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionNimbusActionPerformed(evt);
            }
        });
        itemChangeUi.add(optionNimbus);

        optionGtk.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optionGtk.setText("GTK");
        optionGtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionGtkActionPerformed(evt);
            }
        });
        itemChangeUi.add(optionGtk);

        optMaterial.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optMaterial.setText("Material Design");
        optMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optMaterialActionPerformed(evt);
            }
        });
        itemChangeUi.add(optMaterial);

        optionMac.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        optionMac.setText("Mac Theme");
        optionMac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionMacActionPerformed(evt);
            }
        });
        itemChangeUi.add(optionMac);

        jMenu3.add(itemChangeUi);

        itemRestoreConfig.setText("Restablecer Configuraciones");
        itemRestoreConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRestoreConfigActionPerformed(evt);
            }
        });
        jMenu3.add(itemRestoreConfig);

        menuBar.add(jMenu3);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(panelMenu, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMenu, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(panelContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void panelMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMenuMouseExited
        /*Point point = evt.getPoint();
        Rectangle bounds = panelMenu.getBounds();
        if (point.x > bounds.x && point.y > bounds.y) {
            hideMenu();
        }*/
    }//GEN-LAST:event_panelMenuMouseExited

    private void itemDarkModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDarkModeActionPerformed
        boolean darkEnabled = itemDarkMode.isSelected();
        setDarkMode(darkEnabled);
        config.setProperty(UIConfig.KEYS.DARK_MODE, darkEnabled);
        
        configTransitions();
//        try {
//            UIManager.setLookAndFeel(new HiFiLookAndFeel());
//            SwingUtilities.updateComponentTreeUI(this);
//            
//        } catch (UnsupportedLookAndFeelException ex) {
//            Logger.getLogger(PlayerForm.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }//GEN-LAST:event_itemDarkModeActionPerformed

    private void itemOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemOpenFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemOpenFileActionPerformed

    private void itemOpenFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemOpenFolderActionPerformed
    }//GEN-LAST:event_itemOpenFolderActionPerformed

    private void itemAddFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAddFolderActionPerformed
        musicChooser.showOpenDialog(null);
        //File selected = musicChooser.getSelectedFile();
        File[] selectedFiles = musicChooser.getSelectedFiles();

        if (selectedFiles != null) {
            Player player = controller.getPlayer();
            for (int i = 0; i < selectedFiles.length; i++)
                player.addMusic(selectedFiles[i]);

            if (player.hasSounds()) {
                if (controller.isAlive()) {
                    //((TMTracks) tblTracks.getModel()).loadList();
                    tblTracks.setModel(new TMTracks());
                    int indexOf = player.getListSoundPaths().indexOf(player.getCurrent().getDataSource().getPath());
                    tblTracks.getSelectionModel().setSelectionInterval(0, indexOf);
                    //tblTracks.updateUI();
                } else {
                    controller.start();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se ha encontrado música");
            }
            musicChooser.setSelectedFiles(null);
        }
    }//GEN-LAST:event_itemAddFolderActionPerformed

    private void tblTracksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTracksKeyPressed
        if (evt.getKeyCode() == VK_ENTER) {
            int selectedRow = tblTracks.getSelectedRow();
            tblTracks.getSelectionModel().setSelectionInterval(0, selectedRow);
            controller.getPlayer().play(selectedRow);
            //Thread.sleep(500);
            //System.out.println("Current: "+controller.getPlayer().getCurrent().getTitle());
        }
    }//GEN-LAST:event_tblTracksKeyPressed

    private void tblTracksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTracksMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2) {
            int selectedRow = tblTracks.getSelectedRow();
            controller.getPlayer().play(selectedRow);
            //Thread.sleep(500);
            //System.out.println("Current: "+controller.getPlayer().getCurrent().getTitle());
        }
    }//GEN-LAST:event_tblTracksMouseClicked

    private void optSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optSystemActionPerformed
        changeLookAndFeel(UILookAndFeel.SYSTEM);
    }//GEN-LAST:event_optSystemActionPerformed

    private void optionMetalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionMetalActionPerformed
        changeLookAndFeel(UILookAndFeel.METAL);
    }//GEN-LAST:event_optionMetalActionPerformed

    private void optionNimbusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionNimbusActionPerformed
        changeLookAndFeel(UILookAndFeel.NIMBUS);
    }//GEN-LAST:event_optionNimbusActionPerformed

    private void optionGtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionGtkActionPerformed
        changeLookAndFeel(UILookAndFeel.GTK);
    }//GEN-LAST:event_optionGtkActionPerformed

//    private Object copy(Object obj) {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(obj);
//            
//            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            Object readObject = ois.readObject();
//            ois.close();
//            bais.close();
//            oos.close();
//            baos.close();
//            return readObject;
//        } catch (IOException ex) {
//            Logger.getLogger(PlayerForm.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(PlayerForm.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
    private void optMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optMaterialActionPerformed
        changeLookAndFeel(UILookAndFeel.MATERIAL);
    }//GEN-LAST:event_optMaterialActionPerformed

    private void optionDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionDefaultActionPerformed
        changeLookAndFeel(UILookAndFeel.DEFAULT);
    }//GEN-LAST:event_optionDefaultActionPerformed

    private void itemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_itemExitActionPerformed

    private void panelContentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelContentMouseClicked
        hideMenu();
    }//GEN-LAST:event_panelContentMouseClicked

    private void panelContentMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelContentMouseMoved
        //        int x = evt.getX();
        //        if (x - evt.getComponent().getBounds().getMinX() < 21)
        //            showMenu();
        //        else
        //            hideMenu();
    }//GEN-LAST:event_panelContentMouseMoved

    private void volSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volSliderStateChanged
        if (controller.isAlive()) {
            int vol = volSlider.getValue();
            controller.getPlayer().setGain(vol);

            boolean darkMode = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
            int gain = (int) Session.getInstance().get(SessionKey.GAIN);
            if (vol == 0)
                btnMute.setIcon(R.icons(darkMode?"dark/mute.png": "mute.png"));
            else {
                btnMute.setIcon(R.icons(darkMode?"dark/volume.png": "volume.png"));
                Session.getInstance().set(SessionKey.GAIN, vol);
            }
            lblVol.setText(String.valueOf(vol));
        }
    }//GEN-LAST:event_volSliderStateChanged

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

    private void btnMuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuteActionPerformed
        if (controller.isAlive()) {
            Player player = controller.getPlayer();
            if (player.isMute())
            volSlider.setValue((int) Session.getInstance().get(SessionKey.GAIN));
            else
            volSlider.setValue(0);

        }
    }//GEN-LAST:event_btnMuteActionPerformed

    private void btnSeekNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeekNextActionPerformed
        if (controller.isAlive())
        controller.getPlayer().seekFolder(SeekOption.NEXT);
    }//GEN-LAST:event_btnSeekNextActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if (controller.isAlive())
        controller.getPlayer().playNext();
    }//GEN-LAST:event_btnNextActionPerformed

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

    private void btnSeekPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeekPrevActionPerformed
        if (controller.isAlive())
        controller.getPlayer().playPrevious();
    }//GEN-LAST:event_btnSeekPrevActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        if (controller.isAlive())
        controller.getPlayer().seekFolder(SeekOption.PREV);
    }//GEN-LAST:event_btnPrevActionPerformed

    private void lblCoverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCoverMouseClicked
        hideMenu();
    }//GEN-LAST:event_lblCoverMouseClicked

    private void btnShowMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMenuActionPerformed
        showMenu();
    }//GEN-LAST:event_btnShowMenuActionPerformed

    private void optionMacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionMacActionPerformed
        changeLookAndFeel(UILookAndFeel.MAC);
    }//GEN-LAST:event_optionMacActionPerformed

    private void itemRestoreConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRestoreConfigActionPerformed
        config.restoreConfigs();
        Toast.show("¡Configuraciones Restablecidas!", Toast.LENGHT_SHORT);
    }//GEN-LAST:event_itemRestoreConfigActionPerformed

    private void setUiOptionsChecked() {
        setItemsChecked(itemChangeUi);
    }
    
    private void setItemsChecked(JMenu menu) {
        JMenuItem item;
        
        for (int i = 0; i < menu.getItemCount(); i++) {
            item = menu.getItem(i);
            if (item instanceof JRadioButtonMenuItem)
                ((JRadioButtonMenuItem) item).setSelected(false);
        }
    }
    
    private void changeLookAndFeel(UILookAndFeel laf) {
        setUiOptionsChecked();
        switch (laf) {
            case DEFAULT:
                config.setProperty(UIConfig.KEYS.THEME,
                        SysInfo.DEFAULT_LOOK_AND_FEEL_CLASS);
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, true);
                optionDefault.setSelected(true);
                break;
            case SYSTEM:
                config.setProperty(UIConfig.KEYS.THEME, UIManager.getSystemLookAndFeelClassName());
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, false);
                optSystem.setSelected(true);
                break;
            case METAL:
                config.setProperty(UIConfig.KEYS.THEME,
                        MetalLookAndFeel.class.getName());
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, false);
                optionMetal.setSelected(true);
                break;
            case NIMBUS:
                config.setProperty(UIConfig.KEYS.THEME,
                        NimbusLookAndFeel.class.getName());
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, false);
                optionNimbus.setSelected(true);
                break;
            case GTK:
                config.setProperty(UIConfig.KEYS.THEME,
                        GTKLookAndFeel.class.getName());
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, false);
                optionGtk.setSelected(true);
                break;
            case MATERIAL:
                config.setProperty(UIConfig.KEYS.THEME,
                        MaterialLookAndFeel.class.getName());
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, false);
                optMaterial.setSelected(true);
                break;
            case MAC:
                config.setProperty(UIConfig.KEYS.THEME,
                        McWinLookAndFeel.class.getName());
                config.setProperty(UIConfig.KEYS.IS_MODERN_THEME, true);
                optionDefault.setSelected(true);
                break;
        }
        //SwingUtilities.updateComponentTreeUI(this);
        Toast.show("¡Tema Modificado! Los cambios tendrán efecto después de reiniciar", Toast.LENGHT_SHORT);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        configApplicationLookAndFeel();
        MemoryCleaner.startNow();
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
    private javax.swing.JMenuItem itemAddFolder;
    private javax.swing.JMenuItem itemAddSongs;
    private javax.swing.JMenu itemChangeUi;
    private javax.swing.JCheckBoxMenuItem itemDarkMode;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemOpenFile;
    private javax.swing.JMenuItem itemOpenFolder;
    private javax.swing.JMenuItem itemRestoreConfig;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVol;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JRadioButtonMenuItem optMaterial;
    private javax.swing.JRadioButtonMenuItem optSystem;
    private javax.swing.JRadioButtonMenuItem optionDefault;
    private javax.swing.JRadioButtonMenuItem optionGtk;
    private javax.swing.JRadioButtonMenuItem optionMac;
    private javax.swing.JRadioButtonMenuItem optionMetal;
    private javax.swing.JRadioButtonMenuItem optionNimbus;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelContainer;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelControls;
    private javax.swing.JPanel panelListTracks;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelTrackInfo;
    private javax.swing.JSplitPane splitMusic;
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
//
//    public JButton getBtnShowMenu() {
//        return btnShowMenu;
//    }

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
