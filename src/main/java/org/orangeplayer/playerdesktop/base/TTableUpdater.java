/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.base;

import javax.swing.JTable;
import org.orangeplayer.playerdesktop.model.TMTracks;

/**
 *
 * @author martin
 */
public class TTableUpdater extends Thread {
    
    private final JTable tblTracks;

    public TTableUpdater(JTable tblTracks) {
        this.tblTracks = tblTracks;
        setName("TableUpdater "+getId());
    }

    @Override
    public void run() {
        ((TMTracks)tblTracks.getModel()).loadList();
        tblTracks.updateUI();
    }
    
    
}
