package org.orangeplayer.playerdesktop.base;

import org.muplayer.audio.Player;
import org.muplayer.audio.Track;
import org.orangeplayer.playerdesktop.gui.PlayerForm;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.table.TableColumnModel;
import static org.orangeplayer.playerdesktop.sys.SysUtil.getResizedIcon;
import org.orangeplayer.playerdesktop.model.TMTracks;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;
import static org.orangeplayer.playerdesktop.sys.SessionKey.DARK_MODE;
import org.orangeplayer.playerdesktop.sys.SysInfo;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_COVER_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.DEFAULT_DARK_COVER_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_DARK_ICON;
import static org.orangeplayer.playerdesktop.sys.SysInfo.PAUSE_ICON;

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
        boolean darkEnabled = (boolean) Session.getInstance().get(DARK_MODE);
        
        barTrack.setValue(0);
        barTrack.setString("00:00");
        current = player.getCurrent();
        if (current.hasCover()) {
            coverLabel.setIcon(getResizedIcon(new ImageIcon(current.getCoverData())));
        }
        else
            coverLabel.setIcon(getResizedIcon(darkEnabled?DEFAULT_DARK_COVER_ICON:DEFAULT_COVER_ICON));

        titleLabel.setText(current.getTitle());
        String album = current.getAlbum();
        String artist = current.getArtist();
        albumLabel.setText(album == null ? "Álbum Desconocido" : album);
        artistLabel.setText(artist == null ? "Artista Desconocido" : artist);
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
    
    public void setColumnsLeghts(int[] valuesLenght) {
        TableColumnModel columnModel = tblTracks.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++)
            columnModel.getColumn(i).setPreferredWidth(valuesLenght[i]*2+30);
        tblTracks.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    @Override
    public void run() {
          on = true;
        player.start();
        while (!player.isPlaying());
        //new TTableUpdater(tblTracks).start();
        //Thread.sleep(500);
        
        TMTracks tblModel = (TMTracks) tblTracks.getModel();
        tblModel.loadList();
        setColumnsLeghts(tblModel.getMaxValuesLenghts());
        tblTracks.updateUI();
        updatePlayerInfo();

        long ti = System.currentTimeMillis();

        barTrack.setMaximum((int) current.getDuration());
        playButton.setIcon(((boolean)Session.getInstance().get(DARK_MODE))?
                PAUSE_DARK_ICON:PAUSE_ICON);

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
        player.shutdown();
    }
}
