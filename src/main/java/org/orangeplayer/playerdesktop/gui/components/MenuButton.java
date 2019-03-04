/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.RIGHT;

/**
 *
 * @author martin
 */
public class MenuButton extends JButton {

    public MenuButton() {
        super();
        defaultConfig();
        configAligments();
    }

    public MenuButton(String text) {
        super(text);
        defaultConfig();
        configAligments();
    }
    
    public MenuButton(String text, JComponent parent) {
        super(text);
        defaultConfig();
        configAligments();
        setBackground(parent.getBackground());
    }

    public MenuButton(String text, Icon icon) {
        super(text, icon);
        defaultConfig();
        configAligments();
    }
    
    private void defaultConfig() {
        setFocusPainted(false);
        setBorderPainted(false);
        setRequestFocusEnabled(false);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("Sans Serif", Font.BOLD, 14));
        setAlignmentX(0);
        setAlignmentY(0);
        setHorizontalAlignment(LEADING);
        setVerticalAlignment(CENTER);
        setVisible(true);
    }
    
    protected void configAligments() {
        setHorizontalTextPosition(RIGHT);
        setVerticalTextPosition(CENTER);
    }
}
