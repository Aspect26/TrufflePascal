package cz.cuni.mff.d3s.trupple.parser.exceptions;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

public class SegmentationFaultException extends PascalRuntimeException {

    public SegmentationFaultException() {
        super("Segmentation fault.");
    }
}
