package org.orangeplayer.playerdesktop.sys;

import javax.swing.*;
import java.awt.*;
import org.orangeplayer.playerdesktop.R;

public class SysInfo {
    public static final ImageIcon DEFAULT_COVER_ICON = R.icons("vinilo.png");
    public static final ImageIcon DEFAULT_DARK_COVER_ICON = R.icons("dark/vinilo.png");
    public static final ImageIcon PLAYER_ICON = R.icons("play.png");
    public static final ImageIcon PLAYER_DARK_ICON = R.icons("dark/play.png");
    public static final ImageIcon PAUSE_ICON = R.icons("pause.png");
    public static final ImageIcon PAUSE_DARK_ICON = R.icons("dark/pause.png");

    public static final Dimension DISPLAY_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
}
