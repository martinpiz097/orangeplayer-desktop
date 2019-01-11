package org.orangeplayer.playerdesktop.sys;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SysInfo {
    public static final URL DEFAULT_COVER_ICON_PATH = SysInfo.class.getResource("/icons/vinilo.png");
    public static final URL DEFAULT_DARK_COVER_ICON_PATH = SysInfo.class.getResource("/icons/dark/vinilo.png");
    public static final ImageIcon DEFAULT_COVER_ICON = new ImageIcon(DEFAULT_COVER_ICON_PATH);
    public static final ImageIcon DEFAULT_DARK_COVER_ICON = new ImageIcon(DEFAULT_DARK_COVER_ICON_PATH);
    public static final URL PLAYER_ICON_PATH = SysInfo.class.getResource("/icons/play.png");
    public static final URL PLAYER_DARK_ICON_PATH = SysInfo.class.getResource("/icons/dark/play.png");
    public static final URL PAUSE_ICON_PATH = SysInfo.class.getResource("/icons/pause.png");
    public static final URL PAUSE_DARK_ICON_PATH = SysInfo.class.getResource("/icons/dark/pause.png");

    public static Color PRIMARY_COLOR = Color.decode("#FA6024");
    //public static final Color AUXILIARY_COLOR = Color.decode("#FA6024");
    //public static final Color SECUNDARY_COLOR = Color.decode("#ff3d00");
    public static final Color SECUNDARY_COLOR = Color.decode("#ffab91");
    public static final Color DARK_COLOR = Color.decode("#212121");

    public static final Dimension DISPLAY_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
}
