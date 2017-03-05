package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant.OrdinalConstantDescriptor;

public interface OrdinalDescriptor extends TypeDescriptor {

    abstract class OrdinalDescriptorAbstract implements OrdinalDescriptor {

        @Override
        public FrameSlotKind getSlotKind() {
            return FrameSlotKind.Object;
        }

        @Override
        public Object getDefaultValue() {
            return null;
        }

    }

    class RangeDescriptor extends OrdinalDescriptorAbstract {

        private final OrdinalConstantDescriptor lowerBound;
        private final OrdinalConstantDescriptor upperBound;

        public RangeDescriptor(OrdinalConstantDescriptor lowerBound, OrdinalConstantDescriptor upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public int getFirstIndex() {
            return this.lowerBound.getOrdinalValue();
        }

        public int getSize() {
            return this.upperBound.getOrdinalValue() - this.lowerBound.getOrdinalValue() + 1;
        }

    }

    int getFirstIndex();

    int getSize();
}
