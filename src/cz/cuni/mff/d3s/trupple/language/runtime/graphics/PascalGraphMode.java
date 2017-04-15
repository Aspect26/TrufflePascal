package cz.cuni.mff.d3s.trupple.language.runtime.graphics;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry.Pixel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class PascalGraphMode {

    private static PascalGraphFrame frame;
    private static boolean frameOpened = false;

    public static long init() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        try {
            SwingUtilities.invokeAndWait(PascalGraphMode::openFrame);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new PascalRuntimeException("Could not initialize graphics");
        }

        return 0;
    }

    private static void openFrame() {
        frame = new PascalGraphFrame();
        frame.setVisible(true);
        frame.setBackground();
        frameOpened = true;
    }

    public static long close() {
        frame.dispose();
        frameOpened = false;
        return 0;
    }

    public static long drawPixel(int x, int y, int colorNumber) {
        if (frame == null) {
            return -1;
        }
        Color color = new Color(colorNumber);
        Pixel pixel = new Pixel(x, y, color);
        return frame.putGeometry(pixel);
    }

    public static boolean keyPressed() {
        return frameOpened && frame.keyPressed();
    }

    public static char readKey() {
        if (!frameOpened) {
            return '\0';
        }

        return frame.readKey();
    }

}
