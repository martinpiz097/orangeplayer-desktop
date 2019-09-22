/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.muplayer.audio.Player;
import org.muplayer.audio.Track;
import org.muplayer.audio.interfaces.PlayerListener;
import org.orangeplayer.orangedesktop.model.TCRSongs;
import org.orangeplayer.orangedesktop.model.TMSongs;
import org.orangeplayer.orangedesktop.sys.AppInfo;
import org.orangeplayer.orangedesktop.sys.Artist;
import org.orangeplayer.orangedesktop.sys.FormObject;
import org.orangeplayer.orangedesktop.sys.FormObjectManager;
import org.orangeplayer.orangedesktop.sys.ImageUtil;
import org.orangeplayer.orangedesktop.sys.TrackUtils;

import javax.swing.*;
import org.orangelogger.sys.SystemUtil;
import org.orangeplayer.orangedesktop.gui.util.BlurredImage;
import org.orangeplayer.orangedesktop.scale.ScaleData;
/**
 *
 * @author martin
 */
public class FormPlayer extends javax.swing.JFrame {

    private JFileChooser musicChooser;
    private FormObjectManager objectManager;
    private Player player;
    
    public FormPlayer() {
        setUi();
        initComponents();
        setDefaultConfig();

        tblSongs.setModel(new TMSongs(new ArrayList<>()));
        tblSongs.setDefaultRenderer(Object.class, new TCRSongs());
        tblSongs.getTableHeader().setFont(new Font("Droid Sans", Font.BOLD, 18));
        //tblSongs.getTableHeader().setVisible(false);
        tblSongs.setRowHeight(64);
        tblSongs.setRowMargin(5);
        
        tblSongs.updateUI();
        
    }
    
    private void setDefaultConfig() {
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        tblSongs.setModel(new TMSongs(new ArrayList<>()));
        musicChooser = new JFileChooser();
        setFileChooser();
        
        player = new Player();
        configPlayer();
        objectManager = FormObjectManager.getInstance();
        objectManager.add(FormObject.PLAYER, player);
        
        menuBar.updateUI();

        
    }
    
    private void setDefaultData() {
        lblTitle.setText("Título Desconocido");
        lblArtist.setText("Artista Desconocido");
        lblAlbum.setText("Álbum Desconocido");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/img/play.png")));
        tblSongs.setModel(new TMSongs(new ArrayList<>()));
        tblSongs.setDefaultRenderer(JLabel.class, new TCRSongs());
        tblSongs.updateUI();
        
        if (panelListArtists.getComponentCount() > 0) {
            panelListArtists.removeAll();
            panelListArtists.updateUI();
        }
        
    }
    
    private void setFileChooser() {
        musicChooser.setMultiSelectionEnabled(false);
        musicChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        musicChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() && f.list() != null;
            }

            @Override
            public String getDescription() {
                return "Solo carpetas";
            }
        });
    }
    
    private void updateByTrack(Track track) {
        lblTitle.setText(track.getTitle());
        String artist = track.getArtist();
        String album = track.getAlbum();
        boolean hasCover = track.hasCover();

        lblArtist.setText(artist == null ? "Artista Desconocido" : artist);
        lblAlbum.setText(album == null ? "Álbum Desconocido" : album);
        Dimension dim = new Dimension(512, 512);
        
        ImageIcon coverIcon = hasCover
                ? ImageUtil.resizeIcon(new ImageIcon(track.getCoverData()), dim)
                : ImageUtil.resizeIcon(new ImageIcon(getClass().getResource("/img/cover256.png")), dim);
        lblCover.setIcon(coverIcon);
        BlurredImage.paint(lblCover, getClass().getResource("/img/cover256.png"));
        lblCurrentCover.setIcon(coverIcon);
        lblCurrentCover.updateUI();

        
        trackBar.setString("00:00");
        trackBar.setMaximum((int) track.getDuration());
        trackBar.setValue(0);

        lblCurrentTitle.setText(track.getTitle());
        lblCurrentArtist.setText(track.getArtist() == null ? "Artista Desconocido" : track.getArtist());
        lblCurrentAlbum.setText(track.getAlbum() == null ? "Álbum Desconocido" : track.getAlbum());
        
    }
    
    private void configCurrentCover(Track current) {
        if (current != null && current.hasCover()) {
            lblCurrentCover.setIcon(ImageUtil.resizeIcon(current.getCoverData(), lblCurrentCover));
        }
        else {
            URL resource = getClass().getResource("/img/cover256.png");
            ImageIcon sourceIcon = new ImageIcon(resource);
            lblCurrentCover.setIcon(ImageUtil.resizeIcon(sourceIcon, lblCurrentCover.getBounds()));
        }
    }
    
    private void configPlayer() {
        player.addPlayerListener(new PlayerListener() {
            @Override
            public void onSongChange(Track track) {
                updateByTrack(track);
            }
            
            @Override
            public void onPlayed(Track track) {
                btnPlay.setIcon(new ImageIcon(getClass().getResource("/img/pause.png")));
                updateByTrack(track);
            }
            
            @Override
            public void onPlaying(Track track) {
                trackBar.setValue((int) Math.round(track.getProgress()));
                trackBar.setString(track.getFormattedProgress());
            }

            @Override
            public void onResumed(Track track) {
                btnPlay.setIcon(new ImageIcon(getClass().getResource("/img/pause.png")));
            }

            @Override
            public void onPaused(Track track) {
                btnPlay.setIcon(new ImageIcon(getClass().getResource("/img/play.png")));
            }

            @Override
            public void onStarted() {
                btnPlay.setIcon(new ImageIcon(getClass().getResource("/img/pause.png")));
                tblSongs.setModel(new TMSongs());
                List<Artist> artists = TrackUtils.getArtists(player.getTracksInfo());

                Track current = player.getCurrent();
                configCurrentCover(current);
                
                for (int i = 0; i < artists.size(); i++) {
                    panelListArtists.add(new PanelArtist(artists.get(i)));
                    if (i >= 4 && i % 4 == 0) {
                        panelListArtists.updateUI();
                    }
                }
                
                panelListArtists.updateUI();
                
            }

            @Override
            public void onStopped(Track track) {
                btnPlay.setIcon(new ImageIcon(getClass().getResource("/img/play.png")));
            }

            @Override
            public void onSeeked(Track track) {
                
            }

            @Override
            public void onShutdown() {
                setDefaultData();
            }
        });
    }
    
    private void setUi() {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
            UIManager.put("TabbedPane.selected", Color.white);
            UIManager.put("TabbedPane.borderHightlightColor", AppInfo.PRIMARY_COLOR);
            UIManager.put("TabbedPane.darkShadow", AppInfo.PRIMARY_COLOR);
            UIManager.put("TabbedPane.light", AppInfo.PRIMARY_COLOR);
            UIManager.put("TabbedPane.selectHighlight", AppInfo.PRIMARY_COLOR);
            UIManager.put("TabbedPane.darkShadow", AppInfo.PRIMARY_COLOR);
            UIManager.put("TabbedPane.focus", AppInfo.PRIMARY_COLOR);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FormPlayer.class.getName()).log(Level.SEVERE, null, ex);
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

        formPlaying = new javax.swing.JFrame();
        panelPlayer = new javax.swing.JPanel();
        lblCover = new javax.swing.JLabel();
        panelData = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblArtist = new javax.swing.JLabel();
        lblAlbum = new javax.swing.JLabel();
        panelHeader = new javax.swing.JPanel();
        btnPrev = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnMute = new javax.swing.JButton();
        trackBar = new javax.swing.JProgressBar();
        btnPlayer = new javax.swing.JButton();
        panelContainer = new javax.swing.JPanel();
        tabbed = new javax.swing.JTabbedPane();
        panelCurrent = new javax.swing.JPanel();
        panelCurrentTrack = new javax.swing.JPanel();
        lblCurrentCover = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblCurrentTitle = new javax.swing.JLabel();
        lblCurrentAlbum = new javax.swing.JLabel();
        lblCurrentArtist = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSongs = new javax.swing.JTable();
        panelArtists = new javax.swing.JPanel();
        scrollArtists = new javax.swing.JScrollPane();
        panelListArtists = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        panelPlayer.setBackground(new java.awt.Color(255, 255, 255));

        lblCover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cover256.png"))); // NOI18N

        panelData.setBackground(new java.awt.Color(255, 255, 255));
        panelData.setLayout(new java.awt.GridLayout(3, 1));

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Droid Sans", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Titulo");
        panelData.add(lblTitle);

        lblArtist.setBackground(new java.awt.Color(255, 255, 255));
        lblArtist.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        lblArtist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblArtist.setText("Artista");
        panelData.add(lblArtist);

        lblAlbum.setBackground(new java.awt.Color(255, 255, 255));
        lblAlbum.setFont(new java.awt.Font("Droid Sans", 1, 14)); // NOI18N
        lblAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlbum.setText("Album");
        panelData.add(lblAlbum);

        javax.swing.GroupLayout panelPlayerLayout = new javax.swing.GroupLayout(panelPlayer);
        panelPlayer.setLayout(panelPlayerLayout);
        panelPlayerLayout.setHorizontalGroup(
            panelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlayerLayout.createSequentialGroup()
                .addComponent(lblCover, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelData, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelPlayerLayout.setVerticalGroup(
            panelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlayerLayout.createSequentialGroup()
                .addContainerGap(233, Short.MAX_VALUE)
                .addComponent(panelData, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(196, 196, 196))
        );

        formPlaying.getContentPane().add(panelPlayer, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 61, 0));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(255, 102, 51));
        panelHeader.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnPrev.setBackground(new java.awt.Color(255, 102, 51));
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prev.png"))); // NOI18N
        btnPrev.setFocusable(false);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnPlay.setBackground(new java.awt.Color(255, 102, 51));
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/play.png"))); // NOI18N
        btnPlay.setFocusable(false);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(255, 102, 51));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/next.png"))); // NOI18N
        btnNext.setFocusable(false);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnOpen.setBackground(new java.awt.Color(255, 102, 51));
        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/open-music.png"))); // NOI18N
        btnOpen.setFocusable(false);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnMute.setBackground(new java.awt.Color(255, 102, 51));
        btnMute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/unmute.png"))); // NOI18N
        btnMute.setFocusable(false);
        btnMute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuteActionPerformed(evt);
            }
        });

        trackBar.setBackground(new java.awt.Color(255, 255, 255));
        trackBar.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        trackBar.setForeground(new java.awt.Color(255, 61, 0));
        trackBar.setMaximum(10000);
        trackBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        trackBar.setString("00:00");
        trackBar.setStringPainted(true);
        trackBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                trackBarMouseReleased(evt);
            }
        });

        btnPlayer.setBackground(new java.awt.Color(255, 102, 51));
        btnPlayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow_bottom.png"))); // NOI18N
        btnPlayer.setFocusable(false);
        btnPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(trackBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMute, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPlay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMute)
                            .addComponent(btnOpen)
                            .addComponent(trackBar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnPlayer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));

        tabbed.setBackground(new java.awt.Color(255, 255, 255));
        tabbed.setForeground(new java.awt.Color(0, 0, 0));
        tabbed.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbed.setFocusable(false);
        tabbed.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        tabbed.setOpaque(true);

        panelCurrent.setBackground(new java.awt.Color(255, 255, 255));
        panelCurrent.setLayout(new java.awt.BorderLayout());

        panelCurrentTrack.setBackground(new java.awt.Color(255, 255, 255));
        panelCurrentTrack.setLayout(new java.awt.BorderLayout());

        lblCurrentCover.setBackground(new java.awt.Color(255, 255, 255));
        lblCurrentCover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentCover.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelCurrentTrack.add(lblCurrentCover, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.GridLayout(3, 0));

        lblCurrentTitle.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        lblCurrentTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentTitle.setText("[Nada en Reproduccion]");
        jPanel1.add(lblCurrentTitle);

        lblCurrentAlbum.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lblCurrentAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentAlbum.setText("[Nada en Reproduccion]");
        jPanel1.add(lblCurrentAlbum);

        lblCurrentArtist.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblCurrentArtist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentArtist.setText("[Nada en Reproduccion]");
        jPanel1.add(lblCurrentArtist);

        panelCurrentTrack.add(jPanel1, java.awt.BorderLayout.SOUTH);

        panelCurrent.add(panelCurrentTrack, java.awt.BorderLayout.CENTER);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setOpaque(false);

        tblSongs.setFont(new java.awt.Font("Droid Sans", 0, 14)); // NOI18N
        tblSongs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Título"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSongs.setRowHeight(64);
        tblSongs.setRowMargin(3);
        tblSongs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSongsMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblSongs);

        panelCurrent.add(jScrollPane1, java.awt.BorderLayout.EAST);

        tabbed.addTab("Reproducción", panelCurrent);

        panelArtists.setBackground(new java.awt.Color(255, 255, 255));

        scrollArtists.setBackground(new java.awt.Color(255, 255, 255));
        scrollArtists.setOpaque(false);

        panelListArtists.setBackground(new java.awt.Color(255, 255, 255));
        panelListArtists.setLayout(new java.awt.GridLayout(0, 4, 10, 10));
        scrollArtists.setViewportView(panelListArtists);

        javax.swing.GroupLayout panelArtistsLayout = new javax.swing.GroupLayout(panelArtists);
        panelArtists.setLayout(panelArtistsLayout);
        panelArtistsLayout.setHorizontalGroup(
            panelArtistsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollArtists, javax.swing.GroupLayout.DEFAULT_SIZE, 1235, Short.MAX_VALUE)
        );
        panelArtistsLayout.setVerticalGroup(
            panelArtistsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollArtists, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );

        tabbed.addTab("Artistas", panelArtists);

        javax.swing.GroupLayout panelContainerLayout = new javax.swing.GroupLayout(panelContainer);
        panelContainer.setLayout(panelContainerLayout);
        panelContainerLayout.setHorizontalGroup(
            panelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbed)
        );
        panelContainerLayout.setVerticalGroup(
            panelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbed)
        );

        menuBar.setBackground(new java.awt.Color(0, 0, 0));
        menuBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        menuBar.setForeground(new java.awt.Color(255, 255, 255));

        jMenu1.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N

        itemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        itemExit.setBackground(new java.awt.Color(0, 0, 0));
        itemExit.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N
        itemExit.setForeground(new java.awt.Color(255, 255, 255));
        itemExit.setText("Salir");
        itemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExitActionPerformed(evt);
            }
        });
        jMenu1.add(itemExit);

        menuBar.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(0, 0, 0));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Edit");
        jMenu2.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N
        menuBar.add(jMenu2);

        jMenu3.setBackground(new java.awt.Color(0, 0, 0));
        jMenu3.setForeground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("View");
        jMenu3.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N

        jMenu4.setBackground(new java.awt.Color(0, 0, 0));
        jMenu4.setForeground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("Modo");
        jMenu4.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N
        jMenu4.setOpaque(true);

        jMenuItem1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuItem1.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N
        jMenuItem1.setForeground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setText("Normal");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuItem2.setBackground(new java.awt.Color(0, 0, 0));
        jMenuItem2.setFont(new java.awt.Font("Droid Sans", 1, 16)); // NOI18N
        jMenuItem2.setForeground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setText("Oscuro");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenu3.add(jMenu4);

        menuBar.add(jMenu3);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if (player.isAlive() && player.hasSounds()) {
            player.playNext();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        int open = musicChooser.showOpenDialog(this);
        
        if (open == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = musicChooser.getSelectedFile();
            if (selectedFolder != null) {
                player.addMusic(selectedFolder);
                if (!player.isAlive()) {
                    player.start();
                }
            }
        }
        
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        if (player.isAlive() && player.hasSounds()) {
            if (player.isPlaying()) {
                player.pause();
            }
            else
                player.resumeTrack();
        }
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        if (player.isAlive() && player.hasSounds()) {
            player.playPrevious();
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void tblSongsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSongsMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            if (player.isAlive() && player.hasSounds()) {
                player.play(tblSongs.getSelectedRow());
            }
        }
    }//GEN-LAST:event_tblSongsMouseReleased

    private void itemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_itemExitActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        tabbed.setBackground(Color.WHITE);
        tabbed.setForeground(Color.BLACK);
        tabbed.updateUI();
        
        panelContainer.setBackground(Color.WHITE);
        panelContainer.setForeground(Color.BLACK);
        panelContainer.updateUI();
        
        panelCurrent.setBackground(Color.WHITE);
        panelCurrent.setForeground(Color.BLACK);
        panelCurrent.updateUI();
        
        panelArtists.setBackground(Color.WHITE);
        panelArtists.setForeground(Color.BLACK);
        panelArtists.updateUI();
        
        tblSongs.setBackground(Color.WHITE);
        tblSongs.setForeground(Color.BLACK);
        tblSongs.updateUI();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        tabbed.setBackground(Color.decode("#242424"));
        tabbed.setForeground(Color.WHITE);
        tabbed.updateUI();
        
        panelContainer.setBackground(Color.decode("#242424"));
        panelContainer.setForeground(Color.WHITE);
        panelContainer.updateUI();
        
        panelCurrent.setBackground(Color.decode("#242424"));
        panelCurrent.setForeground(Color.WHITE);
        panelCurrent.updateUI();
        
        panelArtists.setBackground(Color.decode("#242424"));
        panelArtists.setForeground(Color.WHITE);
        panelArtists.updateUI();
        
        tblSongs.setBackground(Color.decode("#242424"));
        tblSongs.setForeground(Color.WHITE);
        tblSongs.updateUI();
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void btnMuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuteActionPerformed
        if (player.isActive()) {
            ImageIcon icon;
            if (player.isMute()) {
                icon = new ImageIcon(getClass().getResource("/img/unmute.png"));
                player.unmute();
            }
            else {
                icon = new ImageIcon(getClass().getResource("/img/mute.png"));
                player.mute();
            }
            btnMute.setIcon(icon);
        }
    }//GEN-LAST:event_btnMuteActionPerformed

    private void trackBarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trackBarMouseReleased
        if (player.isActive()) {
            ScaleData scaleData = new ScaleData(
                    trackBar.getBounds().getMaxX(), player.getCurrent().getDuration());
            double gotoSec = scaleData.scalePoint(evt.getPoint());
            player.seek(gotoSec);
        }
    }//GEN-LAST:event_trackBarMouseReleased

    private void btnPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayerActionPerformed
        if (player.isAlive()) {
            formPlaying.setSize(formPlaying.getPreferredSize());
            formPlaying.setLocationRelativeTo(null);
            //setVisible(false);
            formPlaying.setVisible(true);
        }
    }//GEN-LAST:event_btnPlayerActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        configCurrentCover(player.getCurrent());
        System.out.println("Windows Resized");
    }//GEN-LAST:event_formComponentResized

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(() -> {
            new FormPlayer().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMute;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlayer;
    private javax.swing.JButton btnPrev;
    private javax.swing.JFrame formPlaying;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblCurrentAlbum;
    private javax.swing.JLabel lblCurrentArtist;
    private javax.swing.JLabel lblCurrentCover;
    private javax.swing.JLabel lblCurrentTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel panelArtists;
    private javax.swing.JPanel panelContainer;
    private javax.swing.JPanel panelCurrent;
    private javax.swing.JPanel panelCurrentTrack;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelListArtists;
    private javax.swing.JPanel panelPlayer;
    private javax.swing.JScrollPane scrollArtists;
    private javax.swing.JTabbedPane tabbed;
    private javax.swing.JTable tblSongs;
    private javax.swing.JProgressBar trackBar;
    // End of variables declaration//GEN-END:variables
}
