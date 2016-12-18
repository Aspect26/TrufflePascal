package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public abstract class PrimitiveDescriptor extends OrdinalDescriptor {

    public static class LongDescriptor extends PrimitiveDescriptor {
        @Override
        public FrameSlotKind getSlotKind() {
            return FrameSlotKind.Long;
        }

        @Override
        public boolean isVariable() {
            return true;
        }

        @Override
        public Object getDefaultValue() {
            return 0;
        }

        @Override
        public int getSize() {
            return Integer.SIZE;
        }

        @Override
        public int getFirstIndex() {
            return Integer.MIN_VALUE;
        }
    }

    public static class RealDescriptor extends PrimitiveDescriptor {
        @Override
        public FrameSlotKind getSlotKind() {
            return FrameSlotKind.Double;
        }

        @Override
        public boolean isVariable() {
            return true;
        }

        @Override
        public Object getDefaultValue() {
            return 0.0d;
        }

        // TODO: what to do with this?
        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public int getFirstIndex() {
            return 0;
        }
    }

    public static class CharDescriptor extends PrimitiveDescriptor {
        @Override
        public FrameSlotKind getSlotKind() {
            return FrameSlotKind.Byte;
        }

        @Override
        public boolean isVariable() {
            return true;
        }

        @Override
        public Object getDefaultValue() {
            return '0';
        }

        @Override
        public int getSize() {
            return Character.SIZE;
        }

        @Override
        public int getFirstIndex() {
            return Character.MIN_VALUE;
        }
    }

    public static class BooleanDescriptor extends PrimitiveDescriptor {
        @Override
        public FrameSlotKind getSlotKind() {
            return FrameSlotKind.Boolean;
        }

        @Override
        public boolean isVariable() {
            return true;
        }

        @Override
        public Object getDefaultValue() {
            return false;
        }

        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public int getFirstIndex() {
            return 0;
        }
    }

    private static PrimitiveDescriptor longPrimitiveSingleton = new LongDescriptor();

    private static PrimitiveDescriptor floatPrimitiveSingleton = new RealDescriptor();

    private static PrimitiveDescriptor charPrimitiveSingleton = new CharDescriptor();

    private static PrimitiveDescriptor booleanPrimitiveSingleton = new BooleanDescriptor();

    public static PrimitiveDescriptor longDescriptor() {
        return longPrimitiveSingleton;
    }

    public static PrimitiveDescriptor floatDescriptor() {
        return floatPrimitiveSingleton;
    }

    public static PrimitiveDescriptor charDescriptor() {
        return charPrimitiveSingleton;
    }

    public static PrimitiveDescriptor booleanDescriptor() {
        return booleanPrimitiveSingleton;
    }
}