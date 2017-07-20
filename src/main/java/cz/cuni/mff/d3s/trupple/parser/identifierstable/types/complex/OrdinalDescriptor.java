package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.OrdinalConstantDescriptor;

/**
 * Interface for all ordinal type descriptors. It contains additional information about the number of the values of the
 * ordinal, first integral index and type descriptor of the values of the ordinal.
 */
public interface OrdinalDescriptor extends TypeDescriptor {

    /**
     * Descriptor for the range types. It contains additional information about the index of the first and last index.
     */
    class RangeDescriptor implements OrdinalDescriptor {

        private final OrdinalConstantDescriptor lowerBound;
        private final OrdinalConstantDescriptor upperBound;

        /**
         * Default constructor.
         * @param lowerBound the first index
         * @param upperBound the last index
         */
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

    /**
     * Gets the integral index of the first value in the ordinal.
     * @return
     */
    int getFirstIndex();

    /**
     * Gets the number of the values inside this ordinal.
     */
    int getSize();

    /**
     * Checks whether this ordinal contains the specified value.
     * @param value the value
     */
    boolean containsValue(Object value);

    /**
     * Gets the type descriptor of the values contained in the ordinal.
     */
    TypeDescriptor getInnerTypeDescriptor();

}
