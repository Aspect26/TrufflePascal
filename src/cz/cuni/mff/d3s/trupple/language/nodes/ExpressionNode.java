package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import cz.cuni.mff.d3s.trupple.language.PascalTypes;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;

@TypeSystemReference(PascalTypes.class)
@NodeInfo(description = "Abstract class for all nodes that return value")
public abstract class ExpressionNode extends StatementNode {

	public abstract Object executeGeneric(VirtualFrame frame);

	@Override
	public void executeVoid(VirtualFrame virtualFrame) {
		executeGeneric(virtualFrame);
	}

	public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
		return PascalTypesGen.expectBoolean(executeGeneric(frame));
	}

	public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
		return PascalTypesGen.expectLong(executeGeneric(frame));
	}

	public char executeChar(VirtualFrame frame) throws UnexpectedResultException {
		return PascalTypesGen.expectCharacter(executeGeneric(frame));
	}

	public EnumValue executeEnum(VirtualFrame frame) throws UnexpectedResultException {
	    return PascalTypesGen.expectEnumValue(executeGeneric(frame));
    }

	/**
	 * guard functions
	 */
	// TODO: refactor these 3 functions below -> too much duplicits
	protected boolean isSet(VirtualFrame frame, FrameSlot frameSlot) {
		frame = this.getFrameContainingSlot(frame, frameSlot);
		if(frame == null) {
			return false;
		}
		try {
			Object obj = frame.getObject(frameSlot);
			return obj instanceof SetTypeValue;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}

	protected boolean isPascalArray(VirtualFrame frame, FrameSlot frameSlot) {
		frame = this.getFrameContainingSlot(frame, frameSlot);
		if(frame == null) {
			return false;
		}
		try {
			Object obj = frame.getObject(frameSlot);
			return obj instanceof PascalArray;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}

	protected boolean isEnum(VirtualFrame frame, FrameSlot frameSlot) {
		frame = this.getFrameContainingSlot(frame, frameSlot);
		if(frame == null) {
			return false;
		}
		try {
			Object obj = frame.getObject(frameSlot);
			return obj instanceof EnumValue;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}

	protected boolean isLongKind(VirtualFrame frame, FrameSlot frameSlot) {
		return isKind(frame, FrameSlotKind.Long, frameSlot);
	}

	protected boolean isLongKindOrLongReference(VirtualFrame frame, FrameSlot frameSlot) {
	    return isKind(frame, FrameSlotKind.Long, frameSlot) || isReferenceKind(frame, FrameSlotKind.Long, frameSlot);
    }

	protected boolean isBoolKind(VirtualFrame frame, FrameSlot frameSlot) {
		return isKind(frame, FrameSlotKind.Boolean, frameSlot);
	}

    protected boolean isBoolKindOrBoolReference(VirtualFrame frame, FrameSlot frameSlot) {
        return isKind(frame, FrameSlotKind.Boolean, frameSlot) || isReferenceKind(frame, FrameSlotKind.Boolean, frameSlot);
    }

	protected boolean isCharKind(VirtualFrame frame, FrameSlot frameSlot) {
		return isKind(frame, FrameSlotKind.Byte, frameSlot);
	}

    protected boolean isCharKindOrCharReference(VirtualFrame frame, FrameSlot frameSlot) {
        return isKind(frame, FrameSlotKind.Byte, frameSlot) || isReferenceKind(frame, FrameSlotKind.Byte, frameSlot);
    }

	protected boolean isDoubleKind(VirtualFrame frame, FrameSlot frameSlot) {
		return isKind(frame, FrameSlotKind.Double, frameSlot);
	}

    protected boolean isDoubleKindOrDoubleReference(VirtualFrame frame, FrameSlot frameSlot) {
        return isKind(frame, FrameSlotKind.Double, frameSlot) || isReferenceKind(frame, FrameSlotKind.Double, frameSlot);
    }

    protected boolean isObjectKindOrObjectReference(VirtualFrame frame, FrameSlot frameSlot) {
        return isKind(frame, FrameSlotKind.Object, frameSlot) || isReferenceKind(frame, FrameSlotKind.Object, frameSlot);
    }

	private boolean isReferenceKind(VirtualFrame frame, FrameSlotKind kind, FrameSlot frameSlot) {
        frame = getFrameContainingSlot(frame, frameSlot);
        if (frame == null)
            return false;

        Reference reference = this.tryGetReference(frame, frameSlot);
        return reference != null && reference.getFrameSlot().getKind().equals(kind);
    }

    private boolean isKind(VirtualFrame frame, FrameSlotKind kind, FrameSlot frameSlot) {
		frame = getFrameContainingSlot(frame, frameSlot);
		if (frame == null)
			return false;

		Reference reference = this.tryGetReference(frame, frameSlot);
		if (reference == null) {
			return frameSlot.getKind() == kind;
		}
		else {
			return reference.getFrameSlot().getKind() == kind;
		}
	}

}
