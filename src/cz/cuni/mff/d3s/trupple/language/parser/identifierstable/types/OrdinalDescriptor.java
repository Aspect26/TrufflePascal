package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public abstract class OrdinalDescriptor extends TypeDescriptor {

    public static class RangeDescriptor extends OrdinalDescriptor {

        final private int firstIndex;
        final private int size;

        public RangeDescriptor(int firstIndex, int size) {
            this.firstIndex = firstIndex;
            this.size = size;
        }

        public int getFirstIndex() {
            return this.firstIndex;
        }

        public int getSize() {
            return this.size;
        }

    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    public abstract int getFirstIndex();

    public abstract int getSize();
}
