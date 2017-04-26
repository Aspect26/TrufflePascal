package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.FixedPascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalOrdinal;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

import java.util.List;

public class ArrayDescriptor implements TypeDescriptor {

    private final List<OrdinalDescriptor> dimensions;
    private final TypeDescriptor finalReturnTypeDescriptor;

    public ArrayDescriptor(List<OrdinalDescriptor> dimensions, TypeDescriptor returnTypeDescriptor) {
        this.dimensions = dimensions;
        this.finalReturnTypeDescriptor = returnTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        Object[] data = this.createArrayData();
        return new FixedPascalArray(this.createOrdinal(dimensions.get(0)), data);
    }

    public TypeDescriptor getOneStepInnerDescriptor() {
        if (dimensions.size() == 1) {
            return this.finalReturnTypeDescriptor;
        } else {
            return new ArrayDescriptor(dimensions.subList(1, dimensions.size()), finalReturnTypeDescriptor);
        }
    }

    private PascalOrdinal createOrdinal(OrdinalDescriptor descriptor) {
        if (descriptor instanceof OrdinalDescriptor.RangeDescriptor || descriptor instanceof LongDescriptor) {
            return new PascalOrdinal.RangePascalOrdinal(descriptor.getSize(), descriptor.getFirstIndex());
        } else if (descriptor instanceof BooleanDescriptor) {
            return PascalOrdinal.booleanPascalOrdinal;
        } else if (descriptor instanceof CharDescriptor) {
            return PascalOrdinal.charPascalOrdinal;
        } else if (descriptor instanceof  EnumTypeDescriptor) {
            return new PascalOrdinal.EnumPascalOrdinal((EnumTypeDescriptor) descriptor);
        } else {
            return null;
        }
    }

    private Object[] createArrayData() {
        OrdinalDescriptor myDimension = this.dimensions.get(0);
        Object[] data = new Object[myDimension.getSize()];
        TypeDescriptor innerDescriptor = this.createInnerDescriptor();

        for (int i = 0; i < data.length; ++i) {
            data[i] = innerDescriptor.getDefaultValue();
        }

        return data;
    }

    private TypeDescriptor createInnerDescriptor() {
        if (this.dimensions.size() > 1) {
            return this.createInnerArrayDescriptor();
        } else {
            return this.finalReturnTypeDescriptor;
        }
    }

    private TypeDescriptor createInnerArrayDescriptor() {
        List<OrdinalDescriptor> innerDimensions = this.dimensions.subList(1, this.dimensions.size());
        return new ArrayDescriptor(innerDimensions, this.finalReturnTypeDescriptor);
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return false;
    }

}
