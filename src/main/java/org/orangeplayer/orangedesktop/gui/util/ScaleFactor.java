/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.gui.util;

import java.awt.Font;

/**
 *
 * @author martin
 */
public class ScaleFactor {
    public Font scaleFont(Font font) {
        int size = font.getSize();
        
        switch (size) {
            case 10:
                return new Font(font.getName(), font.getStyle(), 12);
            case 12:
                return new Font(font.getName(), font.getStyle(), 14);
            case 14:
                return new Font(font.getName(), font.getStyle(), 18);
            case 18:
                return new Font(font.getName(), font.getStyle(), 24);
            default:
                return new Font(font.getName(), font.getStyle(), 28);
        }
    }
}
