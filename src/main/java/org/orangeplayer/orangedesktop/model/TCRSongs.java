/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.muplayer.audio.model.TrackInfo;
import org.orangeplayer.orangedesktop.sys.AppInfo;
import javax.swing.ImageIcon;
import org.orangeplayer.orangedesktop.sys.ImageUtil;

/**
 *
 * @author martin
 */
public class TCRSongs implements TableCellRenderer {
    
    private ImageIcon getIcon(TrackInfo trackInfo) {
        ImageIcon icon;
        URL defaultIcon = getClass().getResource("/img/cover.png");
        Dimension imgDim = new Dimension(48, 48);
        
        if (trackInfo.hasCover()) {
           icon = ImageUtil.resizeIcon(new ImageIcon(trackInfo.getCoverData()), imgDim); 
        }
        else {
           icon = ImageUtil.resizeIcon(new ImageIcon(defaultIcon), imgDim);
        }
        return icon;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lblCell = new JLabel();
        TrackInfo song = ((TMSongs)table.getModel()).getInfo(row);
        
        lblCell.setOpaque(true);
        lblCell.setFont(new Font("Droid Sans", Font.BOLD, 18));
        lblCell.setText(song.getTitle());
        lblCell.setVerticalAlignment(JLabel.CENTER);
        lblCell.setHorizontalTextPosition(JLabel.RIGHT);
        
        if (isSelected) {
            lblCell.setBackground(AppInfo.PRIMARY_COLOR);
            lblCell.setForeground(Color.WHITE);
        }
        else {
            lblCell.setBackground(Color.WHITE);
            lblCell.setForeground(Color.BLACK);
        }

        ImageIcon icon = getIcon(song);
        lblCell.setIcon(icon);
        lblCell.setVisible(true);
        
        lblCell.updateUI();
        
        return lblCell;
    }
    
}
