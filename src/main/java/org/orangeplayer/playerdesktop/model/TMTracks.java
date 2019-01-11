/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.model;

import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.muplayer.audio.AudioTag;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;
import org.jaudiotagger.tag.FieldKey;
import org.muplayer.audio.Track;

/**
 *
 * @author martin
 */
public class TMTracks implements TableModel {

    private ArrayList<AudioTag> listTracks;
    
    public TMTracks() {
        loadList();
    }
    
    public int[] getMaxValuesLenghts() {
        int[] valuesLenghts = new int[getColumnCount()];
        if (listTracks.isEmpty()) {
            for (int i = 0; i < valuesLenghts.length; i++) 
                valuesLenghts[i] = 50;
        }
        
        else {
            int max = 0;
            int valueLenght;
            for (int i = 0; i < valuesLenghts.length; i++) {
                for (int j = 0; j < getRowCount(); j++) {
                    valueLenght = getValueAt(j, i).toString().length();
                    if (valueLenght > max)
                        max = valueLenght;
                }
                valuesLenghts[i] = max;
                max = 0;
            }
        }
        return valuesLenghts;
        
    }
    
    public void loadList() {
        Object controller = Session.getInstance().get(SessionKey.CONTROLLER);
        listTracks = controller == null ? new ArrayList<>() :
                ((PlayerController)controller).getPlayer().getTrackTags();
    }
    
    public ArrayList<AudioTag> getTracks() {
        return listTracks;
    }
    
    @Override
    public int getRowCount() {
        return listTracks.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "Título";
            case 1:
                return "Álbum";
            default:
                return "Artista";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AudioTag tag = listTracks.get(rowIndex);
        switch(columnIndex) {
            case 0:
                String title = tag.getTag(FieldKey.TITLE);
                return title == null ? tag.getFileSource().getName() :
                        title;
            case 1:
                String album = tag.getTag(FieldKey.ALBUM);
                return album == null ? "Desconocido" :
                        album;
            default:
                String artist = tag.getTag(FieldKey.ARTIST);
                return artist == null ? "Desconocido" :
                        artist;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
    
}
