package cz.cuni.mff.d3s.trupple.language.parser.exceptions;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;

public class SegmentationFaultException extends PascalRuntimeException {

    public SegmentationFaultException() {
        super("Segmentation fault.");
    }
}
