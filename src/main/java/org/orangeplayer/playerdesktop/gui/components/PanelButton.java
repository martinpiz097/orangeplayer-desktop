/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.components;  

import static java.awt.Color.WHITE;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import org.orangeplayer.playerdesktop.R;

/**
 *
 * @author martin
 */
public class PanelButton extends MenuButton {

    public PanelButton() {
    }

    public PanelButton(String text) {
        super(text);
    }

    public PanelButton(String text, JComponent parent) {
        super(text, parent);
    }

    public PanelButton(String text, Icon icon) {
        super(text, icon);
    }
    
    @Override
    protected void configAligments() {
        setBackground(WHITE);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setMargin(new Insets(5, 5, 5, 5));
        setBorder(new LineBorder(R.colors.PRIMARY_COLOR, 10, true));
    }
    
}
