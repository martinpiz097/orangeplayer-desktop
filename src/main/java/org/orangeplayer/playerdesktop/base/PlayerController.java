package org.orangeplayer.playerdesktop.base;

import org.muplayer.audio.Player;
import org.muplayer.audio.Track;
import org.orangeplayer.playerdesktop.gui.PlayerForm;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

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
        /*playButton = form.getBtnPlay();
        seekNextButton = form.getBtnNext();
        seekPrevButton = form.getBtnPrev();
        nextTrackButton = form.getBtnNextTrack();
        prevTrackButton = form.getBtnPrevTrack();

        coverLabel = form.getLblCover();
        titleLabel = form.getLblTitle();
        albumLabel = form.getLblAlbum();
        artistLabel = form.getLblArtists();*/

        on = false;
        setName(getClass().getSimpleName()+" "+getId());
    }

    private ImageIcon getResizedIcon(ImageIcon img) {
        return new ImageIcon(img.getImage().getScaledInstance(256,
                256, Image.SCALE_SMOOTH));
    }

    private void updatePlayerInfo() {
        /*form.getBarTrack().setValue(0);
        form.getBarTrack().setString("00:00");
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
        form.getPanelPlayer().updateUI();*/
    }

    public boolean isOn() {
        return on;
    }

    public synchronized void shutdown() {
        on = false;
    }

    public Player getPlayer() {
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
        updatePlayerInfo();
        long ti = System.currentTimeMillis();

        //JProgressBar barTrack = form.getBarTrack();
        //barTrack.setMaximum((int) current.getDuration());

        //form.getBtnPlay().setIcon(new ImageIcon(PAUSE_ICON_PATH));

        while (on) {
            try {
                if (player.getCurrent() != current)
                    updatePlayerInfo();
                //System.out.println("Current: "+(current == null ? null : current.getTitle()));
                if (hasOneSecond(ti)) {
                    //barTrack.setValue((int) current.getProgress());
                    //barTrack.setString(current.getFormattedProgress());
                    ti = System.currentTimeMillis();
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
