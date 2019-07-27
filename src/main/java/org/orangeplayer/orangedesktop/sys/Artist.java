/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.sys;

/**
 *
 * @author martin
 */
public class Artist {
    private final String name;
    private final byte[] cover;

    public Artist(String name, byte[] cover) {
        this.name = name;
        this.cover = cover;
    }

    public boolean hasCover() {
        return cover != null;
    }
    
    public String getName() {
        return name;
    }

    public byte[] getCover() {
        return cover;
    }
    
}
