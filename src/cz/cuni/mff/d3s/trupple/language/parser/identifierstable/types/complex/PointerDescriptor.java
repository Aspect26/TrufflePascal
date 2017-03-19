package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.NoBinaryRepresentationException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

public class PointerDescriptor implements TypeDescriptor {

    private TypeDescriptor innerType;
    private String innerTypeIdentifier;
    private boolean innerTypeInitialized;

    public PointerDescriptor(TypeDescriptor innerType) {
        this.innerType = innerType;
        this.innerTypeInitialized = true;
    }

    public PointerDescriptor(String innerTypeIdentifier) {
        this.innerTypeIdentifier = innerTypeIdentifier;
        this.innerTypeInitialized = false;
    }

    // TODO: refactor <- parent class should not know about its subclasses
    /**
     * This c'tor is only used for NilPointerDescriptor
     */
    protected PointerDescriptor() {
        this.innerTypeInitialized = true;
        this.innerType = null;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new PointerValue(innerType);
    }

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        throw new NoBinaryRepresentationException();
    }


    public String getInnerTypeIdentifier() {
        return this.innerTypeIdentifier;
    }

    public void setInnerType(TypeDescriptor innerType) {
        this.innerType = innerType;
        this.innerTypeInitialized = true;
    }

    public boolean isInnerTypeInitialized() {
        return this.innerTypeInitialized;
    }

}
