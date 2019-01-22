/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.util;

/**
 *
 * @author martin
 */
public class Toast {
    
    public static final int LENGHT_LONG = 3000;
    public static final int LENGHT_SHORT = 1000;
    
    public static void show(String msg, int duration) {
        duration = duration > LENGHT_LONG ? LENGHT_LONG : (duration < LENGHT_SHORT ? LENGHT_SHORT : duration);
        new ToastDialog(msg, duration).setVisible(true);
    }
}
