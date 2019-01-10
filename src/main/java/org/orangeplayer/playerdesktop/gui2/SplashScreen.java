/*
 * Created by JFormDesigner on Tue Jan 08 14:45:57 CLST 2019
 */

package org.orangeplayer.playerdesktop.gui2;

import javax.swing.*;
import java.awt.*;

/**
 * @author Martin Pizarro
 */
public class SplashScreen extends JDialog {
    public SplashScreen(Frame owner) {
        super(owner);
        initComponents();
    }

    public SplashScreen(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Martin Pizarro
        lblIcon = new JLabel();

        //======== this ========
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setBackground(Color.white);
        Container contentPane = getContentPane();

        //---- lblIcon ----
        lblIcon.setBackground(Color.white);
        lblIcon.setIcon(new ImageIcon(getClass().getResource("/icons/logo.png")));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setOpaque(true);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, 346, GroupLayout.PREFERRED_SIZE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Martin Pizarro
    private JLabel lblIcon;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
