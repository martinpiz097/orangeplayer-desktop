/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.sys;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static org.orangeplayer.playerdesktop.sys.SysUtil.getResource;

/**
 *
 * @author martin
 */
public class IconManager {

    private HashMap<JComponent, IconType> mapIcons;
    private boolean darkMode;

    private static IconManager iconManager;
    
    public static IconManager newInstance(boolean darkMode) {
        iconManager = new IconManager(darkMode);
        return iconManager;
    }
    
    public static IconManager getInstance() {
        return iconManager;
    }
    
    public static enum IconType {
        PLAY, PAUSE, STOP, NEXTTRACK, PREVTRACK, NEXTFOLDER, PREVFOLDER, 
        MUTE, UNMUTE, MENU, COVER
    }

    private IconManager(boolean darkMode) {
        mapIcons = new HashMap<>();
        this.darkMode = darkMode;
    }

    public void addComponent(JComponent comp, IconType type) {
        mapIcons.put(comp, type);
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }
    
    public void configComponents() {
        configComponents(true);
    }
    
    public void configComponents(boolean configCover) {
        Set<Map.Entry<JComponent, IconType>> entrySet = mapIcons.entrySet();
        AtomicReference<JComponent> compRefer = new AtomicReference<>();
        AtomicReference<IconType> iconRefer = new AtomicReference<>();
        entrySet.parallelStream()
                .forEach(entry->{
                    compRefer.set(entry.getKey());
                    iconRefer.set(entry.getValue());
                    
                    switch(iconRefer.get()) {
                        case MENU:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/menu.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/menu.png")));
                            break;
                            
                        case MUTE:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/mute.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/mute.png")));
                            break;
                            
                        case NEXTFOLDER:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/trackNext.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/trackNext.png")));
                            break;
                            
                        case NEXTTRACK:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/seekNext.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/seekNext.png")));
                            break;
                            
                        case PAUSE:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/pause.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/pause.png")));
                            break;
                            
                        case PLAY:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/play.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/play.png")));
                            break;
                            
                        case PREVFOLDER:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/trackPrev.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/trackPrev.png")));
                            break;
                            
                        case PREVTRACK:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/seekPrev.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/seekPrev.png")));
                            break;
                            
                        case STOP:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/stop.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/stop.png")));
                            break;
                            
                        case UNMUTE:
                            if (darkMode)
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/volume.png")));
                            else
                                ((JButton)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/volume.png")));
                            break;
                            
                        case COVER:
                            if (configCover)
                                if (darkMode)
                                    ((JLabel)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/dark/vinilo.png")));
                                else
                                    ((JLabel)compRefer.get()).setIcon(new ImageIcon(getResource("/icons/vinilo.png")));
                            break;
                        
                        default:
                            
                            break;
                            
                           
                    }
                });
    }
    
    public void configComponent(JComponent comp) {
        Map.Entry<JComponent, IconType> en = 
                mapIcons.entrySet().parallelStream()
                .filter(entry->entry.getKey().equals(comp)).findFirst().orElse(null);
        
        if (en != null) {
            IconType type = en.getValue();
            
            switch (type) {
                case MENU:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/menu.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/menu.png")));
                    break;

                case MUTE:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/mute.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/mute.png")));
                    break;

                case NEXTFOLDER:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/trackNext.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/trackNext.png")));
                    break;

                case NEXTTRACK:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/seekNext.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/seekNext.png")));
                    break;

                case PAUSE:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/pause.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/pause.png")));
                    break;

                case PLAY:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/play.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/play.png")));
                    break;

                case PREVFOLDER:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/trackPrev.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/trackPrev.png")));
                    break;

                case PREVTRACK:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/seekPrev.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/seekPrev.png")));
                    break;

                case STOP:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/stop.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/stop.png")));
                    break;

                case UNMUTE:
                    if (darkMode)
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/dark/volume.png")));
                    else
                        ((JButton) comp).setIcon(new ImageIcon(getResource("/icons/volume.png")));
                    break;

                case COVER:
                    if (darkMode)
                        ((JLabel) comp).setIcon(new ImageIcon(getResource("/icons/dark/vinilo.png")));
                    else
                        ((JLabel) comp).setIcon(new ImageIcon(getResource("/icons/vinilo.png")));
                    break;

                default:

                    break;

            }
        }
        
    }
    
}
