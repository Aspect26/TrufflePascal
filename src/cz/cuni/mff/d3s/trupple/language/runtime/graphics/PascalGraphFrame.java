package cz.cuni.mff.d3s.trupple.language.runtime.graphics;

import cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class PascalGraphFrame extends JFrame {

    private class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) { }

        @Override
        public void keyPressed(KeyEvent e) {
            keyBuffer.add(e.getKeyChar());
        }

        @Override
        public void keyReleased(KeyEvent e) { }
    }

    private final List<Geometry> geometries = new ArrayList<>();
    private final Object drawingLock = new Object();
    private final JPanel printingPanel = new JPanel();
    private final Queue<Character> keyBuffer = new LinkedList<>();

    PascalGraphFrame() {
        super("Trupple graphics mode");

        this.setSize(640, 480);
        this.setResizable(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.printingPanel.setFocusable(true);
        this.printingPanel.addKeyListener(new MyKeyListener());
        this.add(printingPanel);
        this.printingPanel.requestFocusInWindow();
    }

    @Override
    public void paint(Graphics graphics) {
        synchronized (drawingLock) {
            for (Geometry geometry : this.geometries) {
                geometry.paint(printingPanel.getGraphics());
            }
        }
    }

    void setBackground() {
        this.printingPanel.setBackground(Color.black);
    }

    long putGeometry(Geometry geometry) {
        synchronized (drawingLock) {
            this.geometries.add(geometry);
            this.repaint();
        }
        return 0;
    }

    boolean keyPressed() {
        return !this.keyBuffer.isEmpty();
    }

    char readKey() {
        while (this.keyBuffer.isEmpty()){}
        return this.keyBuffer.poll();
    }

}
