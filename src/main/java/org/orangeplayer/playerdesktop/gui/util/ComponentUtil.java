/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
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
             try {
                 Method method = clazz.getMethod("setCurrentTheme", Properties.class);
                 method.invoke(null, getGuiProperties(macStyle));
                 
             } catch (NoSuchMethodException | SecurityException | IllegalAccessException | 
                     IllegalArgumentException | InvocationTargetException ex) {
                 
             }
             UIManager.setLookAndFeel(clazz.newInstance());
             
         } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
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
    
    public static void configApplicationLookAndFeel() {
        UIConfig config = UIConfig.getInstance();
        boolean isModern = Boolean.parseBoolean(config.getProperty(UIConfig.KEYS.IS_MODERN_THEME));
        
        if (isModern)
            setLookAndFeel(config.getProperty(UIConfig.KEYS.THEME), true);
        else
            setLookAndFeel(config.getProperty(UIConfig.KEYS.THEME));
    }
    
    public static void configBackgrounds(JComponent comp, Color color) {
        comp.setBackground(color);
        comp.updateUI();
        final int componentCount = comp.getComponentCount();
        
        if (componentCount > 0) {
            final Component[] components = comp.getComponents();
            Arrays.stream(components).parallel()
                    .forEach(c->{
                        configBackgrounds((JComponent) c, color);
                    });
        }
    }
    
    public static void configBorders(JComponent parent, Border border) {
        final int componentCount = parent.getComponentCount();
        
        if (componentCount > 0) {
            final Component[] components = parent.getComponents();
            Arrays.stream(components).parallel()
                    .forEach(c->{
                        ((JComponent)c).setBorder(border);
                        ((JComponent)c).updateUI();
                    });
        }
        parent.updateUI();
    }
    
    public static void configCardsBorders(JComponent parent, Border border) {
        final int componentCount = parent.getComponentCount();
        
        if (componentCount > 0) {
            final Component[] components = parent.getComponents();
            Arrays.stream(components).parallel()
                    .forEach(c->{
                        if (c instanceof JPanel || c instanceof JScrollPane) {
                            ((JComponent)c).setBorder(border);
                            ((JComponent)c).updateUI();
                        }
                    });
        }
        parent.updateUI();
    }
    
    public static void deleteAllActionListeners(JButton comp) {
        ActionListener[] actionListeners = comp.getActionListeners();
        
        if (actionListeners != null && actionListeners.length > 0)
            for (int i = 0; i < actionListeners.length; i++)
                comp.removeActionListener(actionListeners[i]);
    }

}
