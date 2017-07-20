package cz.cuni.mff.d3s.trupple.language.runtime.graphics;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry.Pixel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Representation of Turbo Pascal's graph mode. It contains a {@link PascalGraphFrame}, which is a GUI windows into which
 * all the graphic content is drawn.
 */
public class PascalGraphMode {

    private static PascalGraphFrame frame;
    private static boolean frameOpened = false;

    public static void init() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        try {
            SwingUtilities.invokeAndWait(PascalGraphMode::openFrame);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new PascalRuntimeException("Could not initialize graphics");
        }
    }

    private static void openFrame() {
        frame = new PascalGraphFrame();
        frame.setVisible(true);
        frame.setBackground();
        frameOpened = true;
    }

    public static void close() {
        frame.dispose();
        frameOpened = false;
    }

    public static void drawPixel(int x, int y, int colorNumber) {
        if (frame == null) {
            return;
        }
        Color color = new Color(colorNumber);
        Pixel pixel = new Pixel(x, y, color);
        frame.putGeometry(pixel);
    }

    /**
     * Checks whether a key was pressed and not processed already.
     */
    public static boolean keyPressed() {
        return frameOpened && frame.keyPressed();
    }

    /**
     * Reads key from the key buffer,
     */
    public static char readKey() {
        if (!frameOpened) {
            return '\0';
        }

        return frame.readKey();
    }

}
