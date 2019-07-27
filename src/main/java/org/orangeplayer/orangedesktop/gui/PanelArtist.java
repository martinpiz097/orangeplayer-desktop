/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.gui;

import java.awt.Color;
import java.awt.Dimension;
import org.orangeplayer.orangedesktop.sys.Artist;
import javax.swing.*;
import org.orangeplayer.orangedesktop.sys.ImageUtil;

/**
 *
 * @author martin
 */
public class PanelArtist extends javax.swing.JPanel {

    private final Artist artist;
    
    public PanelArtist(Artist artist) {
        initComponents();
        this.artist = artist;
        System.out.println("LblCoverSize: "+lblCover.getSize());
        System.out.println("LblCoverPreferedSize: "+lblCover.getPreferredSize());
        lblCover.setIcon(ImageUtil.resizeIcon(getCover(), new Dimension(256, 256)));
        //lblCover.setIcon(ImageUtil.resizeIcon(getCover(), lblCover.getSize()));
        lblTitle.setText(artist.getName());
    }
    
    
    private ImageIcon getCover() {
        if (artist.hasCover()) {
            return new ImageIcon(artist.getCover());
        }
        else {
            return new ImageIcon(getClass().getResource("/img/cover.png"));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCover = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(204, 204, 204)));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        lblCover.setBackground(new java.awt.Color(255, 255, 255));
        lblCover.setFont(new java.awt.Font("Droid Sans", 1, 12)); // NOI18N
        lblCover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCover.setOpaque(true);

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Droid Sans", 1, 12)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblCover, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        JOptionPane.showMessageDialog(null, "ComponentShown");
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}