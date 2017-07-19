package cz.cuni.mff.d3s.trupple.language.runtime.graphics.geometry;

import java.awt.*;

/**
 * Interface for each drawable geometry object inside {@link cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphFrame}.
 */
public interface Geometry {

    /**
     * Paints itself to the provided graphics object.
     */
    void paint(Graphics graphics);

}
