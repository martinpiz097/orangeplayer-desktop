/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import java.util.ArrayList;
import javax.swing.JPanel;
import org.muplayer.audio.AudioTag;

/**
 *
 * @author martin
 */
public class TCardModelLoader extends Thread {

    private TrackCardModel model;

    public TCardModelLoader(ArrayList<AudioTag> listTags, JPanel cardsContainer) {
        model = new TrackCardModel(listTags, cardsContainer);
        setPriority(MAX_PRIORITY);
    }
    
    @Override
    public void run() {
        model.loadSongs();
        System.out.println("TCardListModel finished!");
    }
    
}
