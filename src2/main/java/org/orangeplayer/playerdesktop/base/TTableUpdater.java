///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.orangeplayer.playerdesktop.base;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JTable;
//import javax.swing.table.TableColumnModel;
//import org.orangeplayer.playerdesktop.model.TMTracks;
//import org.orangeplayer.playerdesktop.sys.Session;
//
///**
// *
// * @author martin
// */
//public class TTableUpdater extends Thread {
//    
//    private final JTable tblTracks;
//    private final TMTracks tblModel;
//    private final PlayerController controller;
//    private int songCount;
//    
//    public TTableUpdater(JTable tblTracks, PlayerController controller) {
//        this.tblTracks = tblTracks;
//        this.tblModel = (TMTracks) tblTracks.getModel();
//        this.controller = controller;
//        setName("TableUpdater "+getId());
//    }
//    
//    public void setColumnsLeghts(int[] valuesLenght) {
//        TableColumnModel columnModel = tblTracks.getColumnModel();
//        for (int i = 0; i < columnModel.getColumnCount(); i++)
//            columnModel.getColumn(i).setPreferredWidth(valuesLenght[i]*2+30);
//        tblTracks.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//    }
//
//    @Override
//    public void run() {
//        tblModel.loadList();
//        tblTracks.updateUI();
//        setColumnsLeghts(tblModel.getMaxValuesLenghts());
//        songCount = controller.getPlayer().getSongsCount();
//        
//        while (true) {            
//            try {
//                if (songCount != tblModel.getRowCount()) {
//                    tblModel.loadList();
//                    tblTracks.updateUI();
//                    setColumnsLeghts(tblModel.getMaxValuesLenghts());
//                }
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(TTableUpdater.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//    
//    
//}
