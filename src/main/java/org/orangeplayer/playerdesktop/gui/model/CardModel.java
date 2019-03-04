/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import org.orangeplayer.playerdesktop.R;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.gui.components.ItemPanel;

/**
 *
 * @author martin
 * @param <T>
 * @param <P>
 */
public abstract class CardModel<T, P extends ItemPanel<T>> {
    protected ArrayList<T> listObjects;
    protected JPanel panelList;

    public CardModel() {}
    
    public CardModel(ArrayList<T> listObjects, JPanel panelList) {
        this.listObjects = listObjects;
        this.panelList = panelList;
    }
    
    public CardModel(PlayerController controller, JPanel panelList) {
    }
    
    protected abstract P getCardPanel(T element);

    protected abstract void configCardEvents(P card);
    
    public ArrayList<T> getListObjects() {
        return listObjects;
    }

    public void setListObjects(ArrayList<T> listObjects) {
        this.listObjects = listObjects;
    }

    public JPanel getPanelList() {
        return panelList;
    }

    public void setPanelList(JPanel panelList) {
        this.panelList = panelList;
    }
    
    public void loadSongs() {
        panelList.removeAll();
        final int trackCount = listObjects.size();
        panelList.setLayout(new GridLayout(trackCount, 1, 0, 8));
        final MatteBorder cardBorder = new MatteBorder(0, 0, 3, 3, R.colors.CARD_BORDER_COLOR);
        
        P card;
        for (int i = 0; i < trackCount; i++) {
            card = getCardPanel(listObjects.get(i));
            configCardEvents(card);
            card.setBorder(cardBorder);
            card.updateUI();
            panelList.add(card, i);
        }
        //System.out.println("Elementos: "+panelList.getComponentCount());
        panelList.updateUI();
    }
}
