/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.sys;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author martin
 */
public class SysUtil {
    public static ImageIcon getResizedIcon(ImageIcon img) {
        return getResizedIcon(img, 256, 256);
    }
    
    public static ImageIcon getResizedIcon(ImageIcon img, JComponent parent) {
        final Dimension parentSize = parent.getSize();
        final int width = parentSize.width;
        final int height = parentSize.height;
        
        if ((width == 0 || width == -1) || (height == 0 || height == -1))
            return getResizedIcon(img, 64, 64);
        else
            return getResizedIcon(img, width, height);
    }
    
    public static ImageIcon getResizedIcon(ImageIcon img, int width, int height) {
        return new ImageIcon(img.getImage().getScaledInstance(width,
                height, Image.SCALE_SMOOTH));
    }
    
    public static ImageIcon getResizedIcon(ImageIcon img, Dimension dim) {
        return new ImageIcon(img.getImage().getScaledInstance(dim.width,
                dim.height, Image.SCALE_SMOOTH));
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
