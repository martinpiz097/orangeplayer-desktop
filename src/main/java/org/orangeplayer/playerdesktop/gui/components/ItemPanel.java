/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.components;

import javax.swing.JPanel;
import org.orangeplayer.playerdesktop.sys.Session;

/**
 *
 * @author martin
 */
public abstract class ItemPanel<T> extends JPanel {
    
    protected T element;
    protected final Session session;

    public ItemPanel(T element) {
        this.element = element;
        this.session = Session.getInstance();
    }

    protected abstract void configUI();

    public T getElement() {
        return element;
    }

}
