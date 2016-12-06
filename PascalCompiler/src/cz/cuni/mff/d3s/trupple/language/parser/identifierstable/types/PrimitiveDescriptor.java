package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class PrimitiveDescriptor extends TypeDescriptor {

    private static PrimitiveDescriptor longPrimitiveSingleton = new PrimitiveDescriptor(FrameSlotKind.Long);

    private static PrimitiveDescriptor floatPrimitiveSingleton = new PrimitiveDescriptor(FrameSlotKind.Double);

    private static PrimitiveDescriptor charPrimitiveSingleton = new PrimitiveDescriptor(FrameSlotKind.Byte);

    private static PrimitiveDescriptor booleanPrimitiveSingleton = new PrimitiveDescriptor(FrameSlotKind.Boolean);

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

    private final FrameSlotKind slotKind;

    private PrimitiveDescriptor(FrameSlotKind slotKind) {
        this.slotKind = slotKind;
    }

    public FrameSlotKind getSlotKind() {
        return this.slotKind;
    }
}