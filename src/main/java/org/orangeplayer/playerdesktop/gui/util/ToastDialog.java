/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
 * @author martin
 */
public class ToastDialog extends JDialog {

    private int miliseconds;

    public ToastDialog(String toastString, int time) {
        if (!UIManager.getLookAndFeel().getClass().equals(MetalLookAndFeel.class))
            ComponentUtil.setLookAndFeel(MetalLookAndFeel.class);
        this.miliseconds = time;
        
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel("");
        toastLabel.setText(toastString);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(Color.WHITE);

        setBounds(100, 100, toastLabel.getPreferredSize().width + 20, 31);

        setAlwaysOnTop(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int y = dim.height / 2 - getSize().height / 2;
        int half = y / 2;
        setLocation(dim.width / 2 - getSize().width / 2, y + half);
        panel.add(toastLabel);
        setVisible(false);
        
        SwingUtilities.updateComponentTreeUI(this);
        ComponentUtil.configApplicationLookAndFeel();
        pack();
    }
    
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(miliseconds);
                    dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
}
