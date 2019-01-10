package org.orangeplayer.playerdesktop.sys;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SysInfo {
    public static final URL DEFAULT_COVER_ICON_PATH = SysInfo.class.getResource("/icons/vinilo.png");
    public static final ImageIcon DEFAULT_COVER_ICON = new ImageIcon(DEFAULT_COVER_ICON_PATH);
    public static final URL PLAYER_ICON_PATH = SysInfo.class.getResource("/icons/play.png");
    public static final URL PAUSE_ICON_PATH = SysInfo.class.getResource("/icons/pause.png");
    public static final Color PRIMARY_COLOR = Color.decode("#FA6024");
    public static final Dimension DISPLAY_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
}
