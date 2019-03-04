/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.muplayer.audio.AudioTag;
import org.muplayer.audio.Player;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.gui.components.TrackCard;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;

/**
 *
 * @author martin
 */
public class TrackCardModel extends CardModel<AudioTag, TrackCard> {

    public TrackCardModel() {
    }

    public TrackCardModel(ArrayList<AudioTag> listObjects, JPanel panelList) {
        super(listObjects, panelList);
    }

    public TrackCardModel(PlayerController controller, JPanel panelList) {
        super(controller, panelList);
        listObjects = controller.getPlayer().getTrackTags();
        this.panelList = panelList;
    }

    @Override
    protected TrackCard getCardPanel(AudioTag tag) {
        return new TrackCard(tag);
    }
    
    @Override
    protected void configCardEvents(TrackCard card) {
        card.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    PlayerController controller = (PlayerController) Session.getInstance().get(SessionKey.CONTROLLER);
                    Player player = controller.getPlayer();
                    player.play(player.getListSoundPaths().indexOf(
                            card.getElement().getFileSource().getPath()));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

}
