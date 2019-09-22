/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.sys;

import java.util.ArrayList;
import java.util.List;
import org.muplayer.audio.model.TrackInfo;

/**
 *
 * @author martin
 */
public class TrackUtils {
    public static List<Artist> getArtists(List<TrackInfo> listInfos) {
        listInfos.sort((TrackInfo o1, TrackInfo o2) -> o1.getArtist().compareTo(o2.getArtist()));
        List<Artist> listArtists = new ArrayList<>();
        
        String currentArtist = null;
        byte[] coverData = null;
        
        if (listInfos instanceof ArrayList) {
            TrackInfo info;
            for (int i = 0; i < listInfos.size(); i++) {
                info = listInfos.get(i);
                if (currentArtist == null) {
                    currentArtist = info.getArtist();
                }
                else if (!currentArtist.equals(info.getArtist())) {
                    listArtists.add(new Artist(currentArtist, coverData));
                    currentArtist = info.getArtist();
                    coverData = info.hasCover() ? info.getCoverData() : null;
                }
                else if (info.hasCover() && coverData == null) {
                        coverData = info.getCoverData();
                }
            }
        }
        else {
            for (TrackInfo info : listInfos) {
                if (currentArtist == null) {
                    currentArtist = info.getArtist();
                }
                else if (!currentArtist.equals(info.getArtist())) {
                    listArtists.add(new Artist(currentArtist, coverData));
                    currentArtist = info.getArtist();
                    coverData = info.hasCover() ? info.getCoverData() : null;
                }
                else if (info.hasCover() && coverData == null) {
                        coverData = info.getCoverData();
                }
            }
        }
        return listArtists;
    }
    
    
}
