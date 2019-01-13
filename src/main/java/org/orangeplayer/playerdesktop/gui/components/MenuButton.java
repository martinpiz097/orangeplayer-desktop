/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.border.LineBorder;

/**
 *
 * @author martin
 */
public class MenuButton extends JButton {

    public MenuButton() {
        super();
        configButton();
    }

    public MenuButton(String text) {
        super(text);
        configButton();
    }

    public MenuButton(String text, Icon icon) {
        super(text, icon);
        configButton();
    }
    
    private void configButton() {
        setBorder(new LineBorder(Color.WHITE));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("Sans Serif", Font.BOLD, 14));
        setMargin(new Insets(0, 0, 0, 0));
        setAlignmentX(0);
        setAlignmentY(0);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setHorizontalTextPosition(RIGHT);
        setVerticalTextPosition(CENTER);
        setVisible(true);
    }
}
