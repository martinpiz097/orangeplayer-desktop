package org.orangeplayer.playerdesktop.base;

import org.muplayer.audio.Player;
import org.muplayer.audio.Track;
import org.orangeplayer.playerdesktop.gui.PlayerForm;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.orangeplayer.playerdesktop.sys.SysUtil.getResizedIcon;
import org.orangeplayer.playerdesktop.model.TMTracks;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;
import static org.orangeplayer.playerdesktop.sys.SessionKey.DARK_MODE;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_COVER_ICON_PATH;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_DARK_ICON_PATH;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_ICON_PATH;

public class PlayerController extends Thread {
    private volatile Player player;

    private JButton playButton;
    private JButton seekNextButton;
    private JButton seekPrevButton;
    private JButton nextTrackButton;
    private JButton prevTrackButton;

    private JLabel coverLabel;
    private JLabel titleLabel;
    private JLabel albumLabel;
    private JLabel artistLabel;
    
    private JProgressBar barTrack;
    
    private JTable tblTracks;

    private PlayerForm form;

    private volatile Track current;

    private boolean on;

    public PlayerController(PlayerForm form, String trackPath) {
        try {
            player = new Player(trackPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.form = form;
        configAll();
    }

    public PlayerController(PlayerForm form) {
         try {
            player = new Player();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.form = form;
        configAll();
    }
    
    private void configAll() {
        playButton = form.getBtnPlay();
        seekNextButton = form.getBtnSeekNext();
        seekPrevButton = form.getBtnSeekPrev();
        nextTrackButton = form.getBtnNext();
        prevTrackButton = form.getBtnPrev();

        coverLabel = form.getLblCover();
        titleLabel = form.getLblTitle();
        albumLabel = form.getLblAlbum();
        artistLabel = form.getLblArtist();
        
        barTrack = form.getBarProgress();

        tblTracks = form.getTblTracks();
        
        on = false;
        setName(getClass().getSimpleName()+" "+getId());
    }
    
    private void updatePlayerInfo() {
        barTrack.setValue(0);
        barTrack.setString("00:00");
        current = player.getCurrent();
        if (current.hasCover())
            coverLabel.setIcon(getResizedIcon(new ImageIcon(current.getCoverData())));
        else
            coverLabel.setIcon(getResizedIcon(new ImageIcon(DEFAULT_COVER_ICON_PATH)));

        titleLabel.setText(current.getTitle());
        String album = current.getAlbum();
        String artist = current.getArtist();
        albumLabel.setText(album == null ? "No Album" : album);
        artistLabel.setText(artist == null ? "No Artist" : artist);
        form.getLblDuration().setText(current.getFormattedDuration());
        form.getPanelContent().updateUI();
        int indexOf = player.getListSoundPaths().indexOf(player.getCurrent().getDataSource().getPath());
        tblTracks.getSelectionModel().setSelectionInterval(0, indexOf);
        
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

    @Override
    public void run() {
          on = true;
        player.start();
        while (!player.isPlaying());
        //new TTableUpdater(tblTracks).start();
        //Thread.sleep(500);
        ((TMTracks) tblTracks.getModel()).loadList();
        tblTracks.updateUI();
        updatePlayerInfo();

        long ti = System.currentTimeMillis();

        barTrack.setMaximum((int) current.getDuration());
        playButton.setIcon(new ImageIcon(((boolean)Session.getInstance().get(DARK_MODE))?
                PAUSE_DARK_ICON_PATH:PAUSE_ICON_PATH));

        while (on) {
            try {
                if (player.getCurrent() != current)
                    updatePlayerInfo();
                if (hasOneSecond(ti)) {
                    barTrack.setValue((int) current.getProgress());
                    barTrack.setString(current.getFormattedProgress());
                    ti = System.currentTimeMillis();
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
