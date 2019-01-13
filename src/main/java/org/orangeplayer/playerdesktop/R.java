/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop;

import javax.swing.ImageIcon;
import org.orangeplayer.playerdesktop.sys.AppColors;

/**
 *
 * @author martin
 */
public class R {
    
    public static final AppColors colors;
    
    static {
        colors = new AppColors();
    }
    
    public static ImageIcon icons(String path) {
        return new ImageIcon(R.class.getResource("/icons/"+path));
    }

    
    
}
