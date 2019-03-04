/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import static java.lang.Thread.MAX_PRIORITY;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.muplayer.audio.AudioTag;
import org.orangeplayer.playerdesktop.gui.components.ItemPanel;

/**
 *
 * @author martin
 */
public abstract class TModelLoader<T, P extends ItemPanel<T>> extends Thread {
    protected CardModel<T, P> model;

    public TModelLoader(ArrayList<T> listTags, JPanel cardsContainer) {
        setPriority(MAX_PRIORITY);
        setName(getClass().getSimpleName());
    }
    
    @Override
    public void run() {
        model.loadSongs();
        System.out.println(getName()+" finished!");
    }
}
