/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.orangedesktop.scale;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author martin
 */
public class ScaleData implements Serializable {
    public final double pointX;
    public final double songX;

    public ScaleData(double pointX, double songX) {
        this.pointX = pointX;
        this.songX = songX;
    }

    public double getRectsScale() {
        return songX / pointX;
    }
    
    public double scalePoint(Point point) {
        final double scale = getRectsScale();
        final double scaleX = point.getX() * scale;
        return scaleX;
    }
    
}
