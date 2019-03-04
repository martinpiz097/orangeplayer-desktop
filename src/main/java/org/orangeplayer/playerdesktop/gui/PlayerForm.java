/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui;

import org.orangeplayer.playerdesktop.gui.util.UIConfig;
import org.orangeplayer.playerdesktop.gui.util.UILookAndFeel;
import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import org.muplayer.audio.Player;
import org.orangeplayer.playerdesktop.R;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.gui.components.MenuButton;
import org.orangeplayer.playerdesktop.gui.model.TMTracks;
import org.orangeplayer.playerdesktop.sys.ComponentManager;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;
import org.orangeplayer.playerdesktop.sys.SysInfo;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.mpizexternal.MaterialLookAndFeel;
import org.muplayer.audio.SeekOption;
import org.muplayer.audio.util.AudioExtensions;
import org.orangeplayer.playerdesktop.gui.components.PanelButton;
import org.orangeplayer.playerdesktop.gui.util.ComponentUtil;
import static org.orangeplayer.playerdesktop.gui.util.ComponentUtil.*;
import org.orangeplayer.playerdesktop.gui.util.Toast;
import org.orangeplayer.playerdesktop.sys.MemoryCleaner;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_ICON_SIZE;
import org.orangeplayer.playerdesktop.sys.SysUtil;


/**
 *
 * @author martin
 */
public class PlayerForm extends javax.swing.JFrame {

    private PlayerController controller;
    private JFileChooser musicChooser;

    private JPanel current;
    
    private UIConfig config;
    
    public PlayerForm() {
//        JLayeredPane pane = new JLayeredPane();
//        pane.setLayout(new OverlayLayout(pane));
//        setContentPane(pane);
        config = UIConfig.getInstance();
        initComponents();
        controller = new PlayerController(this);

        getContentPane().setBackground(WHITE);
        configSize();
        setLocationRelativeTo(null);

        configMusicChooser();
        configIconManager();
        
        Session session = Session.getInstance();
        session.add(SessionKey.CONTROLLER, controller);
        session.add(SessionKey.DARK_MODE, false);
        session.add(SessionKey.GAIN, (int)controller.getPlayer().getGain());

        ComponentManager.getInstance().add(header, headerMenu, menu, rootContainer,  
                panelButtons, panelTrackInfo, body, panelButtons, footer,
                lblArtist, lblCover, tabbed, lblTitle, volSlider, tableScroll, 
                btnNext, btnPlay, btnPrev, btnSeekNext, btnSeekPrev, btnMute, lblVol,
                btnMenuAddMusic, btnMenuClearAll, btnShowMenu, panelFolderOptions, btnGoBack
        );
        
        if (Boolean.parseBoolean(config.getProperty(UIConfig.KEYS.DARK_MODE))) {
            setDarkMode(true);
            itemDarkMode.setSelected(true);
        }
        configTransitions();
        //configCards(panelPlaying, MenuIndexes.PLAYING_INDEX);
        configCards();
        configPlayingIcons();
        
        lblCover.setIcon(SysUtil.getResizedIcon(R.icons("dark/vinilo.png"), DEFAULT_ICON_SIZE));

        tabbed.remove(tabArtists);
        tabbed.remove(tabAlbums);
        tabbed.updateUI();
        
        panelFolderOptions.setVisible(false);
        tabFolders.updateUI();
    }
    
    private void configPlayingIcons() {
        final Dimension dim = new Dimension(32, 32);
        btnPlay.setIcon(SysUtil.getResizedIcon((ImageIcon) btnPlay.getIcon(), dim));
        btnNext.setIcon(SysUtil.getResizedIcon((ImageIcon) btnNext.getIcon(), dim));
        btnPrev.setIcon(SysUtil.getResizedIcon((ImageIcon) btnPrev.getIcon(), dim));
        btnSeekNext.setIcon(SysUtil.getResizedIcon((ImageIcon) btnSeekNext.getIcon(), dim));
        btnSeekPrev.setIcon(SysUtil.getResizedIcon((ImageIcon) btnSeekPrev.getIcon(), dim));
        btnShowMenu.setIcon(SysUtil.getResizedIcon((ImageIcon)btnShowMenu.getIcon(), dim));
        
        panelButtons.updateUI();
        header.updateUI();
    }

    /*private void configCurrent(JPanel current, int btnIndex) {
        boolean darkMode = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        container.removeAll();
        this.current = current;
        final MatteBorder currentBorder = new MatteBorder(0, 0, 3, 3, R.colors.CARD_BORDER_COLOR);
        current.setBorder(currentBorder);
        //current.setBackground(R.colors.CARD_COLOR);
        ComponentUtil.configBackground(current, R.colors.CARD_COLOR);
            
        current.updateUI();

        //final Dimension containerSize = container.getPreferredSize();
        //final int width = containerSize.width-10;
        //final int heigth = containerSize.height-10;
        //current.setPreferredSize(new Dimension(width, heigth));
        //current.setSize(current.getPreferredSize());
        //System.out.println("ContainerSize: "+containerSize);
        //System.out.println("CurrentSize: "+current.getPreferredSize());
        
        final int width = getWidth()-menu.getWidth();
        final int heigth = getHeight()-header.getHeight();
        final Dimension containerSize = new Dimension(width, heigth);
        
        System.out.println("ContainerSizeBefore: "+containerSize);
        GroupLayout containerLayout = new GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(containerLayout.createSequentialGroup()
                .addGap(0, 10, 10)
                    .addComponent(current, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, 10))
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(GroupLayout.Alignment.CENTER, containerLayout.createSequentialGroup()
                .addGap(0, 10, 10)
                .addComponent(current, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        
        container.setSize(containerSize);
        System.out.println("ContainerSizeAfter: "+container.getSize());
        container.updateUI();

        /*Dimension size = container.getPreferredSize();
        current.setVisible(true);
        current.updateUI();
        container.updateUI();
        
        System.out.println("Container Size: "+size);
        System.out.println(container.getLayout().getClass().getSimpleName());
        
        
        //container.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(current)
                .addGap(10)
        );
        
//        GZIPOutputStream out = new GZIPOutputStream(null);
//        out.write(b);

        

        this.current = current;
        current.updateUI();

        container.updateUI();
        menu.getComponents()[btnIndex].setBackground(darkMode ? R.colors.DARK_COLOR : WHITE);
        menu.updateUI();
    }*/
    
    private void configCards() {
        // falta configuracion para dark mode
        //boolean darkMode = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
        final MatteBorder cardsBorder = new MatteBorder(1, 1, 3, 3, R.colors.CARD_BORDER_COLOR);
        ComponentUtil.configBackgrounds(cardsContainer, R.colors.CARD_COLOR);
        ComponentUtil.configCardsBorders(cardsContainer, cardsBorder);
        cardsContainer.setBackground(R.colors.CONTAINER_COLOR);
        cardsContainer.updateUI();
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
        //asignTransition(btnMute, R.colors.PRIMARY_COLOR);
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
            //btnMute.setIcon(R.icons("dark/"+muteIcon));
            btnNext.setIcon(R.icons("dark/seekNext.png"));
            btnPlay.setIcon(R.icons("dark/play.png"));
            btnPrev.setIcon(R.icons("dark/seekPrev.png"));
            btnSeekNext.setIcon(R.icons("dark/trackNext.png"));
            btnSeekPrev.setIcon(R.icons("dark/trackPrev.png"));
            //btnShowMenu.setIcon(R.icons("dark/menu.png"));

            if ((controller.isAlive() && !controller.getPlayer().getCurrent().hasCover())
                    || (!controller.isAlive()))
                lblCover.setIcon(R.icons("dark/vinilo.png"));
            tableScroll.getViewport().setBackground(R.colors.DARK_COLOR);
            tableScroll.getViewport().setForeground(WHITE);
            tblTracks.setBackground(R.colors.DARK_COLOR);
            tblTracks.setForeground(WHITE);

            //panelButtons.setBorder(new LineBorder(WHITE, 1));

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
            //btnMute.setIcon(R.icons(muteIcon));
            btnNext.setIcon(R.icons("seekNext.png"));
            btnPlay.setIcon(R.icons("play.png"));
            btnPrev.setIcon(R.icons("seekPrev.png"));
            btnSeekNext.setIcon(R.icons("trackNext.png"));
            btnSeekPrev.setIcon(R.icons("trackPrev.png"));
            //btnShowMenu.setIcon(R.icons("menu.png"));

           if ((controller.isAlive() && !controller.getPlayer().getCurrent().hasCover())
                    || (!controller.isAlive()))
                lblCover.setIcon(R.icons("vinilo.png"));

            tableScroll.getViewport().setBackground(WHITE);
            tableScroll.getViewport().setForeground(BLACK);

            tblTracks.setBackground(WHITE);
            tblTracks.setForeground(BLACK);

            //panelButtons.setBorder(new LineBorder(BLACK, 1));

            validate();
        }
    }
//    private void showMenu() {
//        panelMenu.setVisible(true);
//        panelButton.setVisible(false);
//    }
//
//    private void hideMenu() {
//        panelMenu.setVisible(false);
//        panelButton.setVisible(true);
//    }

    private void configSize() {
        Dimension displaySize = SysInfo.DISPLAY_SIZE;
        Dimension preferredSize = getPreferredSize();
        Dimension maximumSize = getMaximumSize();
        
        Dimension newSize = new Dimension(displaySize);
        newSize.setSize(newSize.getWidth()/1.5, newSize.getHeight()/1.64);
        //setSize(newSize);
        setSize(preferredSize);
        
        //setResizable(false);
        //setMinimumSize(preferredSize);
        //setMinimumSize(preferredSize);
    }

     private void configMusicChooser() {
        musicChooser = new JFileChooser(new File("/home/"+System.getProperty("user.name")));
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

    private void changeMenuCompColors(Color color) {
        Component[] components = menu.getComponents();
        Component comp;
        for (int i = 0; i < components.length; i++) {
            comp = components[i];
            if (comp instanceof MenuButton)
                comp.setBackground(color);
        }
        menu.updateUI();
    }
    
    private void changeMenuCompColors() {
        changeMenuCompColors(menu.getBackground());
    }
    
    private void loadMusic() {
        musicChooser.showOpenDialog(null);
        File[] selectedFiles = musicChooser.getSelectedFiles();

        if (selectedFiles != null) {
            Player player = controller.getPlayer();
            for (int i = 0; i < selectedFiles.length; i++)
                player.addMusic(selectedFiles[i]);

            if (player.hasSounds()) {
                if (!controller.isAlive()) {
                    int vol = volSlider.getValue();
                    controller.getPlayer().setGain(vol);
                    controller.start();
                }
            }
            else
                JOptionPane.showMessageDialog(this, "No se ha encontrado música");
            musicChooser.setSelectedFiles(null);
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

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        tableSongPopup = new javax.swing.JPopupMenu();
        itemDeleteSong = new javax.swing.JMenuItem();
        olderPanelListTracks = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tblTracks = new javax.swing.JTable();
        panelConfig = new javax.swing.JPanel();
        menu = new javax.swing.JPanel();
        btnMenuAddMusic = new org.orangeplayer.playerdesktop.gui.components.MenuButton();
        btnMenuClearAll = new org.orangeplayer.playerdesktop.gui.components.MenuButton();
        rootContainer = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        headerMenu = new javax.swing.JPanel();
        btnShowMenu = new javax.swing.JButton();
        body = new javax.swing.JPanel();
        tabbed = new javax.swing.JTabbedPane();
        tabSongs = new javax.swing.JPanel();
        cardScroll = new javax.swing.JScrollPane();
        cardsContainer = new javax.swing.JPanel();
        tabArtists = new javax.swing.JPanel();
        tabAlbums = new javax.swing.JPanel();
        tabFolders = new javax.swing.JPanel();
        foldersScroll = new javax.swing.JScrollPane();
        foldersContainer = new javax.swing.JPanel();
        panelFolderOptions = new javax.swing.JPanel();
        btnGoBack = new javax.swing.JButton();
        btnPlayFolder = new javax.swing.JButton();
        footer = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnSeekPrev = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnSeekNext = new javax.swing.JButton();
        volSlider = new javax.swing.JSlider();
        lblCover = new javax.swing.JLabel();
        barProgress = new javax.swing.JProgressBar();
        btnMute = new javax.swing.JButton();
        lblVol = new javax.swing.JLabel();
        panelTrackInfo = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblArtist = new javax.swing.JLabel();
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

        itemDeleteSong.setText("Eliminar de la Lista");
        itemDeleteSong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDeleteSongActionPerformed(evt);
            }
        });
        tableSongPopup.add(itemDeleteSong);

        olderPanelListTracks.setBackground(java.awt.Color.white);
        olderPanelListTracks.setLayout(new java.awt.BorderLayout());

        tableScroll.setBackground(java.awt.Color.white);

        tblTracks.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblTracks.setForeground(java.awt.Color.black);
        tblTracks.setModel(new TMTracks());
        tblTracks.setRowMargin(3);
        tblTracks.setSelectionBackground(R.colors.SECUNDARY_COLOR);
        tblTracks.setSelectionForeground(java.awt.Color.black);
        tableScroll.setViewportView(tblTracks);

        olderPanelListTracks.add(tableScroll, java.awt.BorderLayout.CENTER);

        org.jdesktop.layout.GroupLayout panelConfigLayout = new org.jdesktop.layout.GroupLayout(panelConfig);
        panelConfig.setLayout(panelConfigLayout);
        panelConfigLayout.setHorizontalGroup(
            panelConfigLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        panelConfigLayout.setVerticalGroup(
            panelConfigLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Orange Player");
        setBackground(java.awt.Color.white);
        setIconImage(R.icons("logo1.1.png").getImage()
        );

        menu.setBackground(R.colors.PRIMARY_COLOR);
        menu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menu.setAutoscrolls(true);
        menu.setLayout(new java.awt.GridLayout(15, 1));

        btnMenuAddMusic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu/open.png"))); // NOI18N
        btnMenuAddMusic.setText("Agregar Música");
        btnMenuAddMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuAddMusicActionPerformed(evt);
            }
        });
        menu.add(btnMenuAddMusic);

        btnMenuClearAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu/clear.png"))); // NOI18N
        btnMenuClearAll.setText("Limpiar Todo");
        btnMenuClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuClearAllActionPerformed(evt);
            }
        });
        menu.add(btnMenuClearAll);

        rootContainer.setBackground(java.awt.Color.white);

        header.setBackground(R.colors.PRIMARY_COLOR);
        header.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, new java.awt.Color(204, 204, 204)));

        headerMenu.setBackground(R.colors.PRIMARY_COLOR);
        headerMenu.setLayout(new java.awt.GridLayout(1, 20, 3, 0));

        btnShowMenu.setBackground(R.colors.PRIMARY_COLOR);
        btnShowMenu.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnShowMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu.png"))); // NOI18N
        btnShowMenu.setBorderPainted(false);
        btnShowMenu.setFocusPainted(false);
        btnShowMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowMenuActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout headerLayout = new org.jdesktop.layout.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap()
                .add(btnShowMenu, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(headerMenu, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(headerLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(headerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(btnShowMenu, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(headerMenu, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(20, 20, 20))
        );

        body.setBackground(java.awt.Color.white);

        tabbed.setBackground(java.awt.Color.white);
        tabbed.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbed.setToolTipText("");
        tabbed.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        tabbed.setName(""); // NOI18N
        tabbed.setOpaque(true);

        tabSongs.setBackground(R.colors.CONTAINER_COLOR);
        tabSongs.setLayout(new java.awt.BorderLayout());

        cardScroll.setBackground(java.awt.Color.white);
        cardScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        cardScroll.setViewportBorder(null);
        cardScroll.setOpaque(true);

        cardsContainer.setBackground(R.colors.CONTAINER_COLOR);

        org.jdesktop.layout.GroupLayout cardsContainerLayout = new org.jdesktop.layout.GroupLayout(cardsContainer);
        cardsContainer.setLayout(cardsContainerLayout);
        cardsContainerLayout.setHorizontalGroup(
            cardsContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1325, Short.MAX_VALUE)
        );
        cardsContainerLayout.setVerticalGroup(
            cardsContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 560, Short.MAX_VALUE)
        );

        cardScroll.setViewportView(cardsContainer);

        tabSongs.add(cardScroll, java.awt.BorderLayout.CENTER);

        tabbed.addTab("Listado de Canciones", tabSongs);

        tabArtists.setBackground(R.colors.CONTAINER_COLOR);

        org.jdesktop.layout.GroupLayout tabArtistsLayout = new org.jdesktop.layout.GroupLayout(tabArtists);
        tabArtists.setLayout(tabArtistsLayout);
        tabArtistsLayout.setHorizontalGroup(
            tabArtistsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 942, Short.MAX_VALUE)
        );
        tabArtistsLayout.setVerticalGroup(
            tabArtistsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 566, Short.MAX_VALUE)
        );

        tabbed.addTab("Artistas", tabArtists);

        tabAlbums.setBackground(R.colors.CONTAINER_COLOR);

        org.jdesktop.layout.GroupLayout tabAlbumsLayout = new org.jdesktop.layout.GroupLayout(tabAlbums);
        tabAlbums.setLayout(tabAlbumsLayout);
        tabAlbumsLayout.setHorizontalGroup(
            tabAlbumsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 942, Short.MAX_VALUE)
        );
        tabAlbumsLayout.setVerticalGroup(
            tabAlbumsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 566, Short.MAX_VALUE)
        );

        tabbed.addTab("Álbunes", tabAlbums);

        tabFolders.setBackground(R.colors.CONTAINER_COLOR);
        tabFolders.setLayout(new java.awt.BorderLayout());

        foldersScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        foldersContainer.setBackground(R.colors.CONTAINER_COLOR);

        org.jdesktop.layout.GroupLayout foldersContainerLayout = new org.jdesktop.layout.GroupLayout(foldersContainer);
        foldersContainer.setLayout(foldersContainerLayout);
        foldersContainerLayout.setHorizontalGroup(
            foldersContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 936, Short.MAX_VALUE)
        );
        foldersContainerLayout.setVerticalGroup(
            foldersContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 560, Short.MAX_VALUE)
        );

        foldersScroll.setViewportView(foldersContainer);

        tabFolders.add(foldersScroll, java.awt.BorderLayout.CENTER);

        btnGoBack.setBackground(java.awt.Color.white);
        btnGoBack.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnGoBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back.png"))); // NOI18N
        btnGoBack.setText("Volver Atrás");
        btnGoBack.setFocusPainted(false);
        btnGoBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoBack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnPlayFolder.setBackground(java.awt.Color.white);
        btnPlayFolder.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnPlayFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/playFolder.png"))); // NOI18N
        btnPlayFolder.setText("Reproducir Carpeta Actual");
        btnPlayFolder.setFocusPainted(false);
        btnPlayFolder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayFolder.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        org.jdesktop.layout.GroupLayout panelFolderOptionsLayout = new org.jdesktop.layout.GroupLayout(panelFolderOptions);
        panelFolderOptions.setLayout(panelFolderOptionsLayout);
        panelFolderOptionsLayout.setHorizontalGroup(
            panelFolderOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelFolderOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(btnPlayFolder)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnGoBack)
                .addContainerGap(639, Short.MAX_VALUE))
        );
        panelFolderOptionsLayout.setVerticalGroup(
            panelFolderOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, btnPlayFolder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, Short.MAX_VALUE)
            .add(btnGoBack, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        tabFolders.add(panelFolderOptions, java.awt.BorderLayout.PAGE_START);

        tabbed.addTab("Carpetas", tabFolders);

        org.jdesktop.layout.GroupLayout bodyLayout = new org.jdesktop.layout.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, bodyLayout.createSequentialGroup()
                .add(tabbed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .add(0, 0, 0))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabbed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout rootContainerLayout = new org.jdesktop.layout.GroupLayout(rootContainer);
        rootContainer.setLayout(rootContainerLayout);
        rootContainerLayout.setHorizontalGroup(
            rootContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(body, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(header, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rootContainerLayout.setVerticalGroup(
            rootContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rootContainerLayout.createSequentialGroup()
                .add(header, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(body, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        footer.setBackground(R.colors.DARK_COLOR);
        footer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer.setForeground(java.awt.Color.white);

        panelButtons.setBackground(R.colors.DARK_COLOR);
        panelButtons.setForeground(java.awt.Color.white);
        panelButtons.setLayout(new java.awt.GridLayout(1, 5, 5, 0));

        btnSeekPrev.setBackground(R.colors.DARK_COLOR);
        btnSeekPrev.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnSeekPrev.setForeground(java.awt.Color.white);
        btnSeekPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dark/trackPrev.png"))); // NOI18N
        btnSeekPrev.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 5, true));
        btnSeekPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeekPrevActionPerformed(evt);
            }
        });
        panelButtons.add(btnSeekPrev);

        btnPrev.setBackground(R.colors.DARK_COLOR);
        btnPrev.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnPrev.setForeground(java.awt.Color.white);
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dark/seekPrev.png"))); // NOI18N
        btnPrev.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 5, true));
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        panelButtons.add(btnPrev);

        btnPlay.setBackground(R.colors.DARK_COLOR);
        btnPlay.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnPlay.setForeground(java.awt.Color.white);
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dark/play.png"))); // NOI18N
        btnPlay.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 5, true));
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        panelButtons.add(btnPlay);

        btnNext.setBackground(R.colors.DARK_COLOR);
        btnNext.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnNext.setForeground(java.awt.Color.white);
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dark/seekNext.png"))); // NOI18N
        btnNext.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 5, true));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        panelButtons.add(btnNext);

        btnSeekNext.setBackground(R.colors.DARK_COLOR);
        btnSeekNext.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnSeekNext.setForeground(java.awt.Color.white);
        btnSeekNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dark/trackNext.png"))); // NOI18N
        btnSeekNext.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 5, true));
        btnSeekNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeekNextActionPerformed(evt);
            }
        });
        panelButtons.add(btnSeekNext);

        volSlider.setBackground(R.colors.DARK_COLOR);
        volSlider.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        volSlider.setPaintLabels(true);
        volSlider.setPaintTicks(true);
        volSlider.setValue(85);
        volSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volSliderStateChanged(evt);
            }
        });

        lblCover.setBackground(R.colors.DARK_COLOR);
        lblCover.setForeground(java.awt.Color.white);
        lblCover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCover.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        barProgress.setBackground(java.awt.Color.white);
        barProgress.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        barProgress.setForeground(R.colors.PRIMARY_COLOR);
        barProgress.setOpaque(true);
        barProgress.setString("");
        barProgress.setStringPainted(true);

        btnMute.setBackground(R.colors.DARK_COLOR);
        btnMute.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnMute.setForeground(java.awt.Color.white);
        btnMute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dark/volume.png"))); // NOI18N
        btnMute.setBorderPainted(false);
        btnMute.setFocusPainted(false);

        lblVol.setBackground(R.colors.DARK_COLOR);
        lblVol.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblVol.setForeground(java.awt.Color.white);
        lblVol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVol.setText("85");

        panelTrackInfo.setBackground(R.colors.DARK_COLOR);
        panelTrackInfo.setForeground(java.awt.Color.white);
        panelTrackInfo.setLayout(new java.awt.GridLayout(2, 1));

        lblTitle.setBackground(R.colors.DARK_COLOR);
        lblTitle.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("Nada en Reproducción");
        panelTrackInfo.add(lblTitle);

        lblArtist.setBackground(R.colors.DARK_COLOR);
        lblArtist.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblArtist.setForeground(java.awt.Color.white);
        lblArtist.setText("Nada en Reproducción");
        panelTrackInfo.add(lblArtist);

        org.jdesktop.layout.GroupLayout footerLayout = new org.jdesktop.layout.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(footerLayout.createSequentialGroup()
                .add(lblCover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelTrackInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(footerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(footerLayout.createSequentialGroup()
                        .add(volSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 454, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(lblVol, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 515, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btnMute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(barProgress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(footerLayout.createSequentialGroup()
                .add(barProgress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(footerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(footerLayout.createSequentialGroup()
                        .add(footerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnMute))
                        .add(footerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(footerLayout.createSequentialGroup()
                                .add(11, 11, 11)
                                .add(volSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(footerLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblVol, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(panelTrackInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setBackground(new java.awt.Color(238, 238, 238));

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
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
            .add(layout.createSequentialGroup()
                .add(menu, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(rootContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(footer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rootContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(menu, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(0, 0, 0)
                .add(footer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 148, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        loadMusic();
    }//GEN-LAST:event_itemAddFolderActionPerformed

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

    private void optionMacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionMacActionPerformed
        changeLookAndFeel(UILookAndFeel.MAC);
    }//GEN-LAST:event_optionMacActionPerformed

    private void itemRestoreConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRestoreConfigActionPerformed
        config.restoreConfigs();
        Toast.show("¡Configuraciones Restablecidas!", Toast.LENGHT_SHORT);
    }//GEN-LAST:event_itemRestoreConfigActionPerformed

    private void itemDeleteSongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDeleteSongActionPerformed
        TMTracks tblModel = (TMTracks)tblTracks.getModel();
        if (tblModel.getRowCount() > 0) {
            Player player = controller.getPlayer();
            ArrayList<String> listSound = player.getListSoundPaths();
            int[] selectedRows = tblTracks.getSelectedRows();
            int selected;
            for (int i = 0; i < selectedRows.length; i++) {
                selected = selectedRows[i];
                tblModel.getTracks().remove(selected);
                listSound.remove(selected);
            }
            tblTracks.updateUI();
            new Thread(() -> {
                ArrayList<String> listFolder = player.getListFolderPaths();
                Iterator<String> itListFolders = listFolder.parallelStream().iterator();
                AtomicReference<String> nextPath = new AtomicReference<>();
                
                while (itListFolders.hasNext()) {
                    nextPath.set(itListFolders.next());
                    if (listSound.parallelStream().allMatch(
                            (String snd) -> !new File(snd).getParent().equals(nextPath.get())))
                        itListFolders.remove();
                }
            }).start();
        }
    }//GEN-LAST:event_itemDeleteSongActionPerformed

    private void btnShowMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMenuActionPerformed
        menu.setVisible(!menu.isVisible());
    }//GEN-LAST:event_btnShowMenuActionPerformed

    private void volSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volSliderStateChanged
        final int newValue = volSlider.getValue();
        controller.getPlayer().setGain(newValue);
        lblVol.setText(String.valueOf(newValue));
    }//GEN-LAST:event_volSliderStateChanged

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
            final Player player = controller.getPlayer();

            final boolean darkMode = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
            if (player.isPlaying()) {
                player.pause();
                btnPlay.setIcon(SysUtil.getResizedIcon(R.icons("dark/play.png"), 32, 32));
            }
            else {
                player.resumeTrack();
                btnPlay.setIcon(SysUtil.getResizedIcon(R.icons("dark/pause.png"), 32, 32));
            }
            btnPlay.updateUI();
        }
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        if (controller.isAlive())
        controller.getPlayer().playPrevious();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnSeekPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeekPrevActionPerformed
        if (controller.isAlive())
        controller.getPlayer().seekFolder(SeekOption.PREV);
    }//GEN-LAST:event_btnSeekPrevActionPerformed

    private void btnMenuAddMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuAddMusicActionPerformed
        loadMusic();
    }//GEN-LAST:event_btnMenuAddMusicActionPerformed

    private void btnMenuClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuClearAllActionPerformed
        if (controller.isAlive()) {
            controller.shutdown();
            controller = new PlayerController(PlayerForm.this);
            cardsContainer.removeAll();
            cardsContainer.updateUI();

            foldersContainer.removeAll();
            foldersContainer.updateUI();
            
            panelFolderOptions.setVisible(false);
            tabFolders.updateUI();

            Session session = Session.getInstance();
            session.set(SessionKey.GAIN, (int) 85);
            session.set(SessionKey.CONTROLLER, controller);
            final String defaultText = "[No Song]";
            lblArtist.setText(defaultText);
            lblTitle.setText(defaultText);

            //boolean darkMode = (boolean) Session.getInstance().get(SessionKey.DARK_MODE);
            lblCover.setIcon(SysUtil.getResizedIcon(R.icons("dark/vinilo.png"), DEFAULT_ICON_SIZE));
            barProgress.setValue(0);
            barProgress.setString("00:00");
            volSlider.setValue((int) controller.getPlayer().DEFAULT_VOLUME);
            volSlider.setToolTipText("85");

            btnPlay.setIcon(SysUtil.getResizedIcon(R.icons("dark/play.png"), 32, 32));
            btnPlay.setBackground(R.colors.DARK_COLOR);
        }
            else
                JOptionPane.showMessageDialog(this, "La lista ya se encuentra vacía");
    }//GEN-LAST:event_btnMenuClearAllActionPerformed

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
        /*Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        Mixer.Info info;
        Line.Info[] lineInfo;
        Mixer mixer;
        for (int i = 0; i < mixerInfo.length; i++) {
            info = mixerInfo[i];
            System.out.println("Mixer "+i+": "+info.toString());
            mixer = AudioSystem.getMixer(info);
            lineInfo = mixer.getSourceLineInfo();
            
            for (int j = 0; j < lineInfo.length; j++)
                System.out.println("SourceLine: "+mixer.getLine(lineInfo[j]).toString());
            
            System.out.println("---------------------------------");
        }
        
       
        System.exit(0);
        */
        configApplicationLookAndFeel();
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        MemoryCleaner.startNow();
        java.awt.EventQueue.invokeLater(() -> {
            new PlayerForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barProgress;
    private javax.swing.JPanel body;
    private javax.swing.JButton btnGoBack;
    private org.orangeplayer.playerdesktop.gui.components.MenuButton btnMenuAddMusic;
    private org.orangeplayer.playerdesktop.gui.components.MenuButton btnMenuClearAll;
    private javax.swing.JButton btnMute;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlayFolder;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSeekNext;
    private javax.swing.JButton btnSeekPrev;
    private javax.swing.JButton btnShowMenu;
    private javax.swing.JScrollPane cardScroll;
    private javax.swing.JPanel cardsContainer;
    private javax.swing.JPanel foldersContainer;
    private javax.swing.JScrollPane foldersScroll;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel header;
    private javax.swing.JPanel headerMenu;
    private javax.swing.JMenuItem itemAddFolder;
    private javax.swing.JMenuItem itemAddSongs;
    private javax.swing.JMenu itemChangeUi;
    private javax.swing.JCheckBoxMenuItem itemDarkMode;
    private javax.swing.JMenuItem itemDeleteSong;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemOpenFile;
    private javax.swing.JMenuItem itemOpenFolder;
    private javax.swing.JMenuItem itemRestoreConfig;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVol;
    private javax.swing.JPanel menu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel olderPanelListTracks;
    private javax.swing.JRadioButtonMenuItem optMaterial;
    private javax.swing.JRadioButtonMenuItem optSystem;
    private javax.swing.JRadioButtonMenuItem optionDefault;
    private javax.swing.JRadioButtonMenuItem optionGtk;
    private javax.swing.JRadioButtonMenuItem optionMac;
    private javax.swing.JRadioButtonMenuItem optionMetal;
    private javax.swing.JRadioButtonMenuItem optionNimbus;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelConfig;
    private javax.swing.JPanel panelFolderOptions;
    private javax.swing.JPanel panelTrackInfo;
    private javax.swing.JPanel rootContainer;
    private javax.swing.JPanel tabAlbums;
    private javax.swing.JPanel tabArtists;
    private javax.swing.JPanel tabFolders;
    private javax.swing.JPanel tabSongs;
    private javax.swing.JTabbedPane tabbed;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JPopupMenu tableSongPopup;
    private javax.swing.JTable tblTracks;
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

    public JPanel getCardsContainer() {
        return cardsContainer;
    }

    public JScrollPane getjScrollPane1() {
        return tableScroll;
    }

    public JTable getjTable1() {
        return tblTracks;
    }
//
//    public JLabel getLblAlbum() {
//        return lblAlbum;
//    }

    public JLabel getLblArtist() {
        return lblArtist;
    }

    public JLabel getLblCover() {
        return lblCover;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public JPanel getPanelButtons() {
        return panelButtons;
    }

    public JPanel getPanelMenu() {
        return menu;
    }

    public JPanel getPanelTrackInfo() {
        return panelTrackInfo;
    }
    
    public JSlider getVolSlider() {
        return volSlider;
    }

    public JTable getTblTracks() {
        return tblTracks;
    }

    public JPanel getTabSongs() {
        return tabSongs;
    }

    public JPanel getTabAlbums() {
        return tabAlbums;
    }

    public JPanel getTabArtists() {
        return tabArtists;
    }

    public JPanel getTabFolders() {
        return tabFolders;
    }
    
    

    public JLabel getLblVol() {
        return lblVol;
    }

    public JPanel getHeaderMenu() {
        return headerMenu;
    }

    public JPanel getFoldersContainer() {
        return foldersContainer;
    }

    public JButton getBtnGoBack() {
        return btnGoBack;
    }

    public JPanel getPanelFolderOptions() {
        return panelFolderOptions;
    }

    public JButton getBtnPlayFolder() {
        return btnPlayFolder;
    }
    
}
