/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.sys;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author martin
 */
public class SysUtil {
    public static ImageIcon getResizedIcon(ImageIcon img) {
        return getResizedIcon(img, 256, 256);
    }
    
    public static ImageIcon getResizedIcon(ImageIcon img, int width, int height) {
        return new ImageIcon(img.getImage().getScaledInstance(width,
                height, Image.SCALE_SMOOTH));
    }
}
