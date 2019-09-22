/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.gui.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author martin
 */
public class BlurredImage extends JPanel {

    public static void paint(JLabel lbl, URL imgUrl) {
        try {
            Graphics g = lbl.getGraphics();
            BufferedImage myImage = ImageIO.read(imgUrl);

            BufferedImage filteredImage = new BufferedImage(myImage.getWidth(null), myImage
                    .getHeight(null), BufferedImage.TYPE_BYTE_GRAY);

            Graphics g1 = filteredImage.getGraphics();
            g1.drawImage(myImage, 400, 200, null);

            float[] blurKernel = new float[9];
            
            for (int i = 0; i < blurKernel.length; i++) {
                blurKernel[i] = 1 / 9f;
            }

            BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
            myImage = blur.filter(myImage, null);
            g1.dispose();

            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(myImage, null, 3, 3);
            lbl.update(g2d);
            lbl.updateUI();
        } catch (Exception e) {
            System.err.println("BlurredException: "+e.toString());
        }
    }
}
