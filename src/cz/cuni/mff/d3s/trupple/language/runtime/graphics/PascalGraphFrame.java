package cz.cuni.mff.d3s.trupple.language.runtime.graphics;

import cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry.Geometry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PascalGraphFrame extends JFrame {

    private final List<Geometry> geometries;
    private final Object lock = new Object();
    private final JPanel printingPanel = new JPanel();

    PascalGraphFrame() {
        super("Trupple graphics mode");
        geometries = new ArrayList<>();
        this.setSize(640, 480);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(printingPanel);
    }

    @Override
    public void paint(Graphics graphics) {
        synchronized (lock) {
            for (Geometry geometry : this.geometries) {
                geometry.paint(printingPanel.getGraphics());
            }
        }
    }

    void setBackground() {
        this.printingPanel.setBackground(Color.black);
    }

    long putGeometry(Geometry geometry) {
        synchronized (lock) {
            this.geometries.add(geometry);
            this.repaint();
        }
        return 0;
    }

}
