/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import static java.lang.Thread.MAX_PRIORITY;
import javax.swing.JPanel;
import org.muplayer.audio.Player;

/**
 *
 * @author martin
 */
public class TFileModelLoader extends Thread {
    private FileCardModel model;

    public TFileModelLoader(Player player, JPanel cardsContainer) {
        model = new FileCardModel(player, cardsContainer);
        setPriority(MAX_PRIORITY);
    }
    
    @Override
    public void run() {
        model.loadSongs();
        System.out.println("TCardListModel finished!");
    }
}
