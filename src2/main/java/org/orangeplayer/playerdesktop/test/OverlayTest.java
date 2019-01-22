/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 *
 * @author martin
 */
public class OverlayTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Overlay Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        LayoutManager overlay = new OverlayLayout(panel);
        panel.setLayout(overlay);

        JButton button = new JButton("Small");
        button.setMaximumSize(new Dimension(250, 250));
        button.setBackground(Color.white);
        panel.add(button);
        
        JButton smallBtn = button;

        button = new JButton("Medium");
        button.setMaximumSize(new Dimension(500, 500));
        button.setBackground(Color.gray);
        panel.add(button);

        button = new JButton("Large");
        button.setMaximumSize(new Dimension(1000, 1000));
        button.setBackground(Color.black);
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);

        frame.setSize(1000, 1000);
        frame.setVisible(true);
        
        smallBtn.setSize(800, 800);
        smallBtn.setBackground(Color.RED);
        panel.updateUI();
    }
}
