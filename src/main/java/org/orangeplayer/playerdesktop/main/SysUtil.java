/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.main;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author martin
 */
public class SysUtil {
     public static ImageIcon getResizedIcon(ImageIcon img) {
        return new ImageIcon(img.getImage().getScaledInstance(256,
                256, Image.SCALE_SMOOTH));
    }
}
