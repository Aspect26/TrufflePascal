package cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry;

import java.awt.*;

public class Pixel implements Geometry {

    private final int x;
    private final int y;
    private final Color color;

    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(color);
        graphics.drawLine(x, y, x, y);
    }

}
