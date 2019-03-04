/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.mpizexternal.utils.MaterialColors;
import org.muplayer.audio.Player;
import org.orangeplayer.playerdesktop.R;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;

/**
 *
 * @author martin
 */
public class TCRTracks implements TableCellRenderer {

    private PlayerController controller;
    private Player player;

    public TCRTracks() {
        controller = (PlayerController) Session.getInstance().get(SessionKey.CONTROLLER);
        player = controller.getPlayer();
    }
    
    private int getCurrentIndex() {
        return player.getListSoundPaths().indexOf(player.getCurrent().getDataSource().getPath());
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lblCell = new JLabel();
        lblCell.setOpaque(true);
        lblCell.setBackground(Color.WHITE);
        lblCell.setForeground(Color.BLACK);
        lblCell.setFont(new Font("Sans Serif", Font.BOLD, 12));
        if (row == getCurrentIndex())
            lblCell.setBackground(MaterialColors.LIGHT_BLUE_600);
        else if (isSelected)
            lblCell.setBackground(R.colors.SECUNDARY_COLOR);
        
        return lblCell;
    }
    
}
