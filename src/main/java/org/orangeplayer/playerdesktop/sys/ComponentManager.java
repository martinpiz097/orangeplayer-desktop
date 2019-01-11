/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.sys;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.function.Predicate;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author martin
 */
public class ComponentManager {
    private LinkedList<JComponent> listComponents;

    private static final ComponentManager manager;
    
    static {
        manager = new ComponentManager();
    }
    
    public static ComponentManager getInstance() {
        return manager;
    }
    
    public ComponentManager() {
        listComponents = new LinkedList<>();
    }

    public void add(JComponent component) {
        if (component != null)
            listComponents.add(component);
    }
    
    public void add(JComponent... components) {
        if (components != null && components.length > 0)
            for (int i = 0; i < components.length; i++)
                add(components[i]);
    }
    
    public void setBackground(Color color) {
        listComponents.parallelStream().forEach(comp-> {
            comp.setBackground(color);
            comp.updateUI();
        });
    }
    
    /*public void setBackgroundTree(Color color, Component... parents) {
        if (parents != null && parents.length > 0) {
            
            JComponent parent;
            for (int i = 0; i < parents.length; i++) {
                parent = (JComponent) parents[i];
                parent.setBackground(color);
                if (parent.getComponentCount() > 0)
                    setBackgroundTree(color, (JComponent[]) parent.getComponents());
                parent.updateUI();
            }
        }
    }*/
    
    public void setForeground(Color color) {
        listComponents.parallelStream().forEach(comp-> {
            comp.setForeground(color);
            comp.updateUI();
        });
    }
    
    public void configColors(Color bg, Color fg) {
        listComponents.parallelStream().forEach((comp)->{
            comp.setBackground(bg);
            comp.setForeground(fg);
            comp.updateUI();
        });
    }
    
    public void configColors(Color bg, Color fg, Class<?> clazz, boolean excluded) {
        listComponents.parallelStream().filter(comp->
                excluded?!comp.getClass().equals(clazz)
                        :comp.getClass().equals(clazz)).parallel()
                .forEach(comp->{
                     comp.setBackground(bg);
                     comp.setForeground(fg);
                     comp.updateUI();
                });
    }

}
