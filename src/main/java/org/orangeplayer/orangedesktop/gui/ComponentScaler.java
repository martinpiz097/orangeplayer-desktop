/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.gui;

import java.awt.Font;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author martin
 */
public class ComponentScaler {
    private final LinkedList<JComponent> listComponents;

    public ComponentScaler() {
        listComponents = new LinkedList<>();
    }
    
    private boolean hasIconProperty(JComponent comp) {
        try {
            comp.getClass().getMethod("setIcon", Icon.class);
            return true;
        } catch (NoSuchMethodException | SecurityException ex) {
            return false;
        }
    }
    
    private boolean hasFontMethod(JComponent comp) {
        try {
            comp.getClass().getMethod("setFont", Font.class);
            return true;
        } catch (NoSuchMethodException | SecurityException ex) {
            return false;
        }
    }
    
    public ComponentScaler addComponent(JComponent comp) {
        listComponents.add(comp);
        return this;
    }
    
    public void increaseFonts() {
        listComponents.parallelStream()
                .forEach(comp->{
                    if (hasFontMethod(comp)) {
                        Font font = comp.getFont();
                        comp.setFont(new Font(font.getName(), font.getStyle(), font.getSize()+2));
                        comp.updateUI();
                    }
                });
    }
    
    public void decreaseFonts() {
        listComponents.parallelStream()
                .forEach(comp->{
                    if (hasFontMethod(comp)) {
                        Font font = comp.getFont();
                        comp.setFont(new Font(font.getName(), font.getStyle(), font.getSize()-2));
                        comp.updateUI();
                    }
                });
    }
    
}
