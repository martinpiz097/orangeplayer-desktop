/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.sys;

import java.awt.FontMetrics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
    
    public static URL getResource(String path) {
        return SysUtil.class.getResource(path);
    }
    
    public static ImageIcon getImage(String path) {
        return new ImageIcon(getResource(path));
    }
    
    public static String trimToLabel(String text, JLabel lbl) {
        final FontMetrics metrics = lbl.getFontMetrics(lbl.getFont());
        final int maxWidth = lbl.getWidth();
        final char[] textChars = text.toCharArray();
        
        int decCounter = textChars.length;
        
        while (metrics.charsWidth(textChars, 0, decCounter)>maxWidth)
            decCounter--;
        return text.substring(0, decCounter);
    }
    
}
