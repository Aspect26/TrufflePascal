package cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PascalGraphFrame extends JFrame {

    private final List<Geometry> geometries;
    private final Object lock = new Object();

    public PascalGraphFrame() {
        super("Trupple graphics mode");
        geometries = new ArrayList<>();
        this.setSize(640, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void paint(Graphics graphics) {
        synchronized (lock) {
            for (Geometry geometry : this.geometries) {
                geometry.paint(graphics);
            }
        }
    }

    public long putGeometry(Geometry geometry) {
        synchronized (lock) {
            this.geometries.add(geometry);
            this.repaint();
        }
        return 0;
    }

}
