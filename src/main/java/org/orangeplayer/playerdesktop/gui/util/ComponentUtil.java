/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.util;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.mpizexternal.animation.MaterialUIMovement;
import org.mpizexternal.animation.MaterialUITimer;

/**
 *
 * @author martin
 */
public class ComponentUtil {
     private static void checkExistingTransition(JComponent comp) {
        MouseListener[] mouseListeners = comp.getMouseListeners();
        
        for (int i = 0; i < mouseListeners.length; i++)
            if (mouseListeners[i] instanceof MaterialUITimer)
                comp.removeMouseListener(mouseListeners[i]);
        
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            ActionListener[] actionListeners = ((JButton) comp).getActionListeners();
            for (int i = 0; i < actionListeners.length; i++)
                if (actionListeners[i] instanceof MaterialUITimer)
                    btn.removeActionListener(actionListeners[i]);
        }
    }
    
    public static void asignTransition(JComponent comp, Color color) {
        checkExistingTransition(comp);
        MaterialUIMovement.add(comp, color);
    }
    
    public static Properties getGuiProperties(boolean macStyle) {
         Properties props = new Properties();
	props.setProperty("logoString", "");
        props.setProperty("licensekey", "");
	//props.setProperty("textShadow", "off");
	props.setProperty("macStyleWindowDecoration", (macStyle ? "on" : "off"));
	//props.setProperty("menuOpaque", "off");
        return props;
    }
    
    public static void setLookAndFeel(Class<? extends LookAndFeel> clazz, boolean macStyle) {
         try {
             Method method = clazz.getMethod("setCurrentTheme", Properties.class);
             method.invoke(null, getGuiProperties(macStyle));
             UIManager.setLookAndFeel(clazz.newInstance());
             
         } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | UnsupportedLookAndFeelException ex) {
             Logger.getLogger(ComponentUtil.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
    
    public static void setLookAndFeel(String className, boolean macStyle) {
         try {
             setLookAndFeel((Class<? extends LookAndFeel>) Class.forName(className), macStyle);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(ComponentUtil.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    public static void setLookAndFeel(Class<? extends LookAndFeel> clazz) {
         try {
             UIManager.setLookAndFeel(clazz.newInstance());
         } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | UnsupportedLookAndFeelException ex) {
             Logger.getLogger(ComponentUtil.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
    
    public static void setLookAndFeel(String className) {
         try {
             UIManager.setLookAndFeel(className);
         } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | UnsupportedLookAndFeelException | ClassNotFoundException ex) {
             Logger.getLogger(ComponentUtil.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
    
}
