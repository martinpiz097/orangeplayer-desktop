package org.orangeplayer.playerdesktop.sys;

import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orangeplayer.playerdesktop.R;

public class SysInfo {
    public static final ImageIcon DEFAULT_COVER_ICON = R.icons("vinilo.png");
    public static final ImageIcon DEFAULT_DARK_COVER_ICON = R.icons("dark/vinilo.png");
    public static final ImageIcon PLAYER_ICON = R.icons("play.png");
    public static final ImageIcon PLAYER_DARK_ICON = R.icons("dark/play.png");
    public static final ImageIcon PAUSE_ICON = R.icons("pause.png");
    public static final ImageIcon PAUSE_DARK_ICON = R.icons("dark/pause.png");

    public static final Dimension DISPLAY_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final Dimension DEFAULT_ICON_SIZE = new Dimension(80, 80);
    

    //public static final String DEFAULT_LOOK_AND_FEEL_CLASS = McWinLookAndFeel.class.getName();
    public static final String DEFAULT_LOOK_AND_FEEL_CLASS = UIManager.getSystemLookAndFeelClassName();
    
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String USER_NAME = System.getProperty("user.name");
    public static final File CONFIG_FOLDER;

    static {
        if (OS_NAME.toLowerCase().contains("windows"))
                CONFIG_FOLDER = new File("C:/Users/"+USER_NAME, ".orangePlayer");
            else
                CONFIG_FOLDER = new File("/home/"+USER_NAME, ".orangePlayer");
            
            if (!CONFIG_FOLDER.exists())
                CONFIG_FOLDER.mkdir();
        try {
            Files.setAttribute(CONFIG_FOLDER.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            Logger.getLogger(SysInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
