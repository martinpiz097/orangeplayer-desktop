package org.orangeplayer.playerdesktop.base;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import org.muplayer.audio.Player;
import org.muplayer.audio.Track;
import org.orangeplayer.playerdesktop.gui.PlayerForm;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.orangeplayer.playerdesktop.R;
import org.orangeplayer.playerdesktop.gui.util.ComponentUtil;
import org.orangeplayer.playerdesktop.gui.model.TrackCardModel;
import org.orangeplayer.playerdesktop.gui.model.TCardModelLoader;
import org.orangeplayer.playerdesktop.gui.model.TFileModelLoader;
import static org.orangeplayer.playerdesktop.sys.SysUtil.getResizedIcon;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;
import static org.orangeplayer.playerdesktop.sys.SessionKey.DARK_MODE;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_COVER_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_DARK_COVER_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_ICON_SIZE;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_DARK_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_ICON;
import static org.orangeplayer.playerdesktop.sys.SysUtil.trimToLabel;

public class PlayerController extends Thread {
    private volatile Player player;

    private JButton playButton;
    private JButton seekNextButton;
    private JButton seekPrevButton;
    private JButton nextTrackButton;
    private JButton prevTrackButton;

    private JLabel coverLabel;
    private JLabel titleLabel;
    //private JLabel albumLabel;
    private JLabel artistLabel;
    
    private JProgressBar barTrack;
    
    private JPanel cardsContainer;
    
    //private JTable tblTracks;

    private PlayerForm form;
    

    private volatile Track current;

    private boolean on;

    public PlayerController(PlayerForm form, String trackPath) {
        try {
            player = new Player(trackPath);
            disableLogging();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.form = form;
        configAll();
    }

    public PlayerController(PlayerForm form) {
         try {
            player = new Player();
            disableLogging();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.form = form;
        configAll();
    }
    
    private void disableLogging() {
        try {
            LogManager.getLogManager().readConfiguration();
            Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
            Logger.getLogger("org.jaudiotagger.tag").setLevel(Level.OFF);
            Logger.getLogger("org.jaudiotagger.audio.mp3.MP3File").setLevel(Level.OFF);
            Logger.getLogger("org.jaudiotagger.tag.id3.ID3v23Tag").setLevel(Level.OFF);
        } catch (IOException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void configAll() {
        setPriority(MAX_PRIORITY);
        
        playButton = form.getBtnPlay();
        seekNextButton = form.getBtnSeekNext();
        seekPrevButton = form.getBtnSeekPrev();
        nextTrackButton = form.getBtnNext();
        prevTrackButton = form.getBtnPrev();

        coverLabel = form.getLblCover();
        titleLabel = form.getLblTitle();
        //albumLabel = form.getLblAlbum();
        artistLabel = form.getLblArtist();
        
        cardsContainer = form.getCardsContainer();

        barTrack = form.getBarProgress();

        //tblTracks = form.getTblTracks();
        
        on = false;
        setName(getClass().getSimpleName()+" "+getId());
    }
    
    private void updatePlayerInfo() {
        boolean darkEnabled = (boolean) Session.getInstance().get(DARK_MODE);
        
        // crear un darkCardColor para el modo oscuro
        int indexOf;
        if (current != null) {
            indexOf = player.getListSoundPaths().indexOf(current.getDataSource().getPath());
            updateCardBackground(indexOf, R.colors.CARD_COLOR);
        }
        current = player.getCurrent();
        
        barTrack.setValue(0);
        barTrack.setString("00:00");
        
        if (current.hasCover())
            coverLabel.setIcon(getResizedIcon(new ImageIcon(current.getCoverData()), DEFAULT_ICON_SIZE));
        else
            coverLabel.setIcon(getResizedIcon(DEFAULT_DARK_COVER_ICON, DEFAULT_ICON_SIZE));
        
        String title = current.getTitle();
        String artist = current.getArtist();
        
        artist = artist == null ? "Artista Desconocido" : artist;

        titleLabel.setText(trimToLabel(title, titleLabel));
        titleLabel.updateUI();
        artistLabel.setText(trimToLabel(artist, artistLabel));
        artistLabel.updateUI();
        form.getPanelTrackInfo().updateUI();
        
        indexOf = player.getListSoundPaths().indexOf(player.getCurrent().getDataSource().getPath());
        updateCardBackground(indexOf, R.colors.SECUNDARY_COLOR);
        
        if (player.isPlaying()) {
            playButton.setIcon(getResizedIcon(PAUSE_DARK_ICON, 32, 32));
        }
        
        cardsContainer.updateUI();
    }
    
    private void updateCardBackground(int index, Color bg) {
        ComponentUtil.configBackgrounds((JComponent) cardsContainer.getComponent(index), bg);
    }

    public boolean isOn() {
        return on;
    }

    public synchronized void shutdown() {
        on = false;
    }

    public synchronized Player getPlayer() {
        return player;
    }

    private boolean hasOneSecond(long ti) {
        int seconds = (int) ((System.currentTimeMillis()-ti)/1000);
        return seconds>=1;
    }
    
    /*public void setColumnsLeghts(int[] valuesLenght) {
        TableColumnModel columnModel = tblTracks.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++)
            columnModel.getColumn(i).setPreferredWidth(valuesLenght[i]*2+30);
        tblTracks.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }*/
    
    private void waitForPlayer() {
        while (!player.isPlaying() && player.getCurrent() == null);
    }

    public PlayerForm getForm() {
        return form;
    }

    @Override
    public void run() {
        on = true;

        new TCardModelLoader(player.getTrackTags(), cardsContainer).start();
        new TFileModelLoader(player, form.getFoldersContainer()).start();
        
        player.start();
        waitForPlayer();
        
        long ti = System.currentTimeMillis();
        
        Session.getInstance().set(SessionKey.GAIN, (int)player.getGain());
        updatePlayerInfo();

        barTrack.setMaximum((int) current.getDuration());

        playButton.setIcon(getResizedIcon(PAUSE_DARK_ICON, 32, 32));
        form.getLblVol().setText(String.valueOf((int)player.getGain()));
        updateCardBackground(0, R.colors.SECUNDARY_COLOR);

        while (on) {
            try {
                if (player.getCurrent() != current)
                    updatePlayerInfo();
                if (hasOneSecond(ti)) {
                    barTrack.setValue((int) current.getProgress());
                    barTrack.setString(current.getFormattedProgress());
                    ti = System.currentTimeMillis();
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        player.shutdown();
    }
    
    /*public static void main(String[] args) {
        File folder = new File("/home/martin/Dropbox/Java/Proyectos/IntelliJ/"
                + "OrangePlayerProject/jaudiotagger/src/org/jaudiotagger/");
        deleteContent(folder, "Log.", "Logger.getLogger(");
    }
    
    public static void deleteContent(File folder, String... contents) {
        if (contents == null)
            return;
        final File[] files = folder.listFiles();
        
        if (files != null) {
            File file;
            List<String> lines;
            StringBuilder sbNewLines = new StringBuilder();
            boolean contains = false;
            
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.isDirectory())
                    deleteContent(file, contents);
                else {
                    try {
                        lines = Files.readAllLines(file.toPath());
                        for (String line : lines) {
                            for (int j = 0; j < contents.length; j++) {
                                if (line.contains(contents[j])) {
                                    contains = true;
                                    break;
                                }
                            }
                            if (contains)
                                contains = !contains;
                            else
                                sbNewLines.append(line);
                        }
                        Files.write(file.toPath(), sbNewLines.toString().getBytes(), 
                                StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (IOException ex) {
                        Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        }
    }*/
    
}
