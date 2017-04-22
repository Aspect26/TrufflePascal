package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.OrdinalConstantDescriptor;

public interface OrdinalDescriptor extends TypeDescriptor {

    class RangeDescriptor implements OrdinalDescriptor {

        private final OrdinalConstantDescriptor lowerBound;
        private final OrdinalConstantDescriptor upperBound;

        public RangeDescriptor(OrdinalConstantDescriptor lowerBound, OrdinalConstantDescriptor upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public int getFirstIndex() {
            return this.lowerBound.getOrdinalValue();
        }

        @Override
        public int getSize() {
            return this.upperBound.getOrdinalValue() - this.lowerBound.getOrdinalValue() + 1;
        }

        @Override
        public boolean containsValue(Object value) {
            return (value instanceof Integer) && (getFirstIndex() <= (Integer)value  && (Integer) value < getFirstIndex() + getSize());
        }

        @Override
        public TypeDescriptor getInnerTypeDescriptor() {
            return lowerBound.getInnerType();
        }

        @Override
        public FrameSlotKind getSlotKind() {
            return lowerBound.getSlotKind();
        }

        @Override
        public Object getDefaultValue() {
            return this.lowerBound.getDefaultValue();
        }

        @Override
        public boolean convertibleTo(TypeDescriptor type) {
            return false;
        }

    }

    int getFirstIndex();

    int getSize();

    boolean containsValue(Object value);

    TypeDescriptor getInnerTypeDescriptor();

}
