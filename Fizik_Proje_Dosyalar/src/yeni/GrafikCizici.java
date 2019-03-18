/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yeni;

/**
 *
 * @author salih
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class GrafikCizici extends JPanel {

    String deneyCesidi;
    double[] data;

    GrafikCizici(double[]data) {
        this.data = data;
    } 
    
    final int PAD = 20;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
        double xInc = (double) (w - 2 * PAD) / (data.length - 1);
        double scale = (double) (h - 2 * PAD) / getMax();
        // Mark data points.
        g2.setPaint(Color.red);

        for (int i = 0; i < data.length; i++) {
            double x = PAD + i * xInc;
            double y = h - PAD - scale * data[i];
            g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
        }
    }

    private double getMax() {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }
}
