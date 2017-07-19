package cz.cuni.mff.d3s.trupple.language.runtime.graphics;

import cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

/**
 * The GUI frame which contains a panel component into which all graphic content is drawn.
 */
public class PascalGraphFrame extends JFrame {

    /**
     * Key listener for the drawing panel.
     */
    private class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) { }

        /**
         * If a key is pressed, push it into the frame's key buffer.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            keyBuffer.add(e.getKeyChar());
        }

        @Override
        public void keyReleased(KeyEvent e) { }
    }

    /**
     * List of all geometric object that should be drawn
     */
    private final List<Geometry> geometries = new ArrayList<>();

    /**
     * Lock used when working with the list of geometries from different threads
     */
    private final Object drawingLock = new Object();

    /**
     * Panel onto which all the content is drawn
     */
    private final JPanel printingPanel = new JPanel();

    /**
     * A key buffer. Stores pressed keys and releases them when they are processed
     */
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

    void putGeometry(Geometry geometry) {
        synchronized (drawingLock) {
            this.geometries.add(geometry);
            this.repaint();
        }
    }

    /**
     * Checks whether there is a not processed key in the key buffer.
     */
    boolean keyPressed() {
        return !this.keyBuffer.isEmpty();
    }

    /**
     * Reads one ket from the key buffer. If it is empty, it waits for a key to be pressed,
     */
    char readKey() {
        while (this.keyBuffer.isEmpty()) ;
        return this.keyBuffer.poll();
    }

}
