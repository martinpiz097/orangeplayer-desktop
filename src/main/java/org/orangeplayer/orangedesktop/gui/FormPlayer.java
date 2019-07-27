/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
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
import org.muplayer.audio.model.TrackInfo;
import org.orangeplayer.orangedesktop.model.TCRSongs;
import org.orangeplayer.orangedesktop.model.TMSongs;
import org.orangeplayer.orangedesktop.sys.AppInfo;
import org.orangeplayer.orangedesktop.sys.Artist;
import org.orangeplayer.orangedesktop.sys.FormObject;
import org.orangeplayer.orangedesktop.sys.FormObjectManager;
import org.orangeplayer.orangedesktop.sys.ImageUtil;
import org.orangeplayer.orangedesktop.sys.TrackUtils;

import javax.swing.*;
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
        tblSongs.getTableHeader().setFont(new Font("Droid Sans", Font.BOLD, 24));
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
        Dimension dim = lblCover.getPreferredSize();
        
        ImageIcon coverIcon = hasCover
                ? ImageUtil.resizeIcon(new ImageIcon(track.getCoverData()), dim)
                : new ImageIcon(getClass().getResource("/img/cover256.png"));
        lblCover.setIcon(coverIcon);
        
        trackBar.setString("00:00");
        trackBar.setMaximum((int) track.getDuration());
        trackBar.setValue(0);
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
                LinkedList<Artist> artists = TrackUtils.getArtists(player.getTracksInfo());
                System.out.println("ArtistCount: "+artists.size());
                
                AtomicInteger counter = new AtomicInteger(0);
                artists.stream()
                        .forEach(artist-> {
                            panelListArtists.add(new PanelArtist(artist));
                            if (counter.incrementAndGet() >= 5 && counter.get() % 5 == 0) {
                                panelListArtists.updateUI();
                            }
                            
                        });
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

        panelHeader = new javax.swing.JPanel();
        btnPrev = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnMute = new javax.swing.JButton();
        trackBar = new javax.swing.JProgressBar();
        panelContainer = new javax.swing.JPanel();
        tabbed = new javax.swing.JTabbedPane();
        panelPlayer = new javax.swing.JPanel();
        lblCover = new javax.swing.JLabel();
        lblArtist = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblAlbum = new javax.swing.JLabel();
        panelSongs = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 61, 0));

        panelHeader.setBackground(new java.awt.Color(255, 61, 0));
        panelHeader.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnPrev.setBackground(new java.awt.Color(255, 61, 0));
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prev.png"))); // NOI18N
        btnPrev.setFocusable(false);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnPlay.setBackground(new java.awt.Color(255, 61, 0));
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/play.png"))); // NOI18N
        btnPlay.setFocusable(false);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(255, 61, 0));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/next.png"))); // NOI18N
        btnNext.setFocusable(false);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnOpen.setBackground(new java.awt.Color(255, 61, 0));
        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/open-music.png"))); // NOI18N
        btnOpen.setFocusable(false);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnMute.setBackground(new java.awt.Color(255, 61, 0));
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

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelContainer.setLayout(new java.awt.BorderLayout());

        tabbed.setBackground(new java.awt.Color(255, 61, 0));
        tabbed.setForeground(new java.awt.Color(0, 0, 0));
        tabbed.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbed.setFocusable(false);
        tabbed.setFont(new java.awt.Font("Droid Sans", 1, 24)); // NOI18N
        tabbed.setOpaque(true);

        panelPlayer.setBackground(new java.awt.Color(255, 255, 255));

        lblCover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cover256.png"))); // NOI18N

        lblArtist.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        lblArtist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblArtist.setText("Artista");

        lblTitle.setFont(new java.awt.Font("Droid Sans", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Titulo");

        lblAlbum.setFont(new java.awt.Font("Droid Sans", 1, 14)); // NOI18N
        lblAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlbum.setText("Album");

        javax.swing.GroupLayout panelPlayerLayout = new javax.swing.GroupLayout(panelPlayer);
        panelPlayer.setLayout(panelPlayerLayout);
        panelPlayerLayout.setHorizontalGroup(
            panelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                    .addComponent(lblCover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelPlayerLayout.setVerticalGroup(
            panelPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlayerLayout.createSequentialGroup()
                .addComponent(lblCover, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addComponent(lblArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        tabbed.addTab("Reproducción", panelPlayer);

        panelSongs.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout panelSongsLayout = new javax.swing.GroupLayout(panelSongs);
        panelSongs.setLayout(panelSongsLayout);
        panelSongsLayout.setHorizontalGroup(
            panelSongsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
        );
        panelSongsLayout.setVerticalGroup(
            panelSongsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );

        tabbed.addTab("Canciones", panelSongs);

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
            .addComponent(scrollArtists, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
        );
        panelArtistsLayout.setVerticalGroup(
            panelArtistsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollArtists, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );

        tabbed.addTab("Artistas", panelArtists);

        panelContainer.add(tabbed, java.awt.BorderLayout.CENTER);

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
        jMenuItem2.setText("Claro");
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
        tabbed.setBackground(AppInfo.PRIMARY_COLOR);
        tabbed.setForeground(Color.BLACK);
        tabbed.updateUI();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        tabbed.setBackground(Color.WHITE);
        tabbed.setForeground(Color.BLACK);
        tabbed.updateUI();
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
    private javax.swing.JButton btnPrev;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel panelArtists;
    private javax.swing.JPanel panelContainer;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelListArtists;
    private javax.swing.JPanel panelPlayer;
    private javax.swing.JPanel panelSongs;
    private javax.swing.JScrollPane scrollArtists;
    private javax.swing.JTabbedPane tabbed;
    private javax.swing.JTable tblSongs;
    private javax.swing.JProgressBar trackBar;
    // End of variables declaration//GEN-END:variables
}
