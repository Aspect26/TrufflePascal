package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

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
    public PointerDescriptor() {
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

    public TypeDescriptor getInnerTypeDescriptor() {
        return this.innerType;
    }

    @Override
    public boolean equals(Object compareTo) {
        if (!(compareTo instanceof PointerDescriptor)) {
            return false;
        }
        PointerDescriptor compareToPointer = (PointerDescriptor) compareTo;
        return (innerTypeIdentifier == null && compareToPointer.innerTypeIdentifier == null) ||
                compareToPointer.innerTypeIdentifier.equals(this.innerTypeIdentifier);
    }

    @Override
    public int hashCode() {
        // NOTE: the multiplication here is needed so pointer descriptors won't have same hashes as the inner types
        return (this.innerTypeIdentifier == null)? 0 : this.innerTypeIdentifier.hashCode() * 1245;
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

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        if (!(type instanceof PointerDescriptor)) {
            return false;
        }

        PointerDescriptor convertTo = (PointerDescriptor) type;
        return (this.getInnerTypeDescriptor() == null) || (convertTo.getInnerTypeDescriptor() == null) ||
                (convertTo.getInnerTypeDescriptor() == this.getInnerTypeDescriptor());
    }

}
