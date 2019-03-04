/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.util;

import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orangeplayer.playerdesktop.sys.SysInfo;
import static org.orangeplayer.playerdesktop.sys.SysInfo.CONFIG_FOLDER;

/**
 *
 * @author martin
 */
public class UIConfig {
    private File fileConfig;
    private Properties props;

    private static final UIConfig instance;
    
    static {
        instance = new UIConfig();
    }
    
    public static enum KEYS {
        THEME, 
        IS_MODERN_THEME, // isModern es para los de jtattoo
        DARK_MODE
    }
    
    public static UIConfig getInstance() {
        return instance;
    }
    
    
    public UIConfig() {
        fileConfig = new File(CONFIG_FOLDER, "uicnf.properties");
        props = new Properties();
        
        if (fileConfig.exists())
            loadData();
        else
            saveDefaultData();
    }
    
    private void saveDefaultData() {
        try {
            // Initial Config
            fileConfig.createNewFile();
            fileConfig.setExecutable(false);
            Files.setAttribute(fileConfig.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
            
            // Properties
            props.setProperty(KEYS.THEME.name(), SysInfo.DEFAULT_LOOK_AND_FEEL_CLASS);
            props.setProperty(KEYS.IS_MODERN_THEME.name(), Boolean.toString(true));
            props.setProperty(KEYS.DARK_MODE.name(), Boolean.toString(false));

            // Saving
            saveData();
        } catch (IOException ex) {
            Logger.getLogger(UIConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveData() {
        try {
            props.store(new FileWriter(fileConfig), "");
        } catch (IOException ex) {
            Logger.getLogger(UIConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadData() {
        try {
            props.load(new FileReader(fileConfig));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UIConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UIConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getProperty(KEYS key) {
        return props.getProperty(key.name());
    }
    
    public void setProperty(KEYS key, Object value) {
        props.setProperty(key.name(), value.toString());
        saveData();
    }
    
    public void restoreConfigs() {
        try {
            props = null;
            props = new Properties();
            fileConfig.delete();
            fileConfig.createNewFile();
            saveDefaultData();
        } catch (IOException ex) {
            Logger.getLogger(UIConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
