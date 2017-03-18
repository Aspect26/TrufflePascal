package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.NilPointerDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.compound.*;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant.*;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.primitive.PrimitiveDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class InitializationNodeGenerator {

    private final IdentifiersTable identifiersTable;

    InitializationNodeGenerator(IdentifiersTable identifiersTable) {
        this.identifiersTable = identifiersTable;
    }

    List<StatementNode> generate() throws LexicalException{
        List<StatementNode> initializationNodes = new ArrayList<>();

        for (Map.Entry<String, TypeDescriptor> entry : this.identifiersTable.getAll().entrySet()) {
            String identifier = entry.getKey();
            TypeDescriptor typeDescriptor = entry.getValue();

            StatementNode initializationNode = createInitializationNode(identifier, typeDescriptor);
            if (initializationNode != null)
                initializationNodes.add(initializationNode);
            // TODO: inform variable not initialized
        }

        return initializationNodes;
    }

    private StatementNode createInitializationNode(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        FrameSlot frameSlot = this.identifiersTable.getFrameSlot(identifier);

        if (typeDescriptor instanceof PrimitiveDescriptor) {
            return createPrimitiveValue(frameSlot, typeDescriptor);
        } else if (typeDescriptor instanceof ConstantDescriptor) {
            return createConstantValue(frameSlot, typeDescriptor);
        } else if (typeDescriptor instanceof ArrayDescriptor) {
            return createArrayValue(frameSlot, (ArrayDescriptor)typeDescriptor);
        } else if (typeDescriptor instanceof EnumTypeDescriptor) {
            return createEnumValue(frameSlot, (EnumTypeDescriptor)typeDescriptor);
        } else if (typeDescriptor instanceof EnumValueDescriptor) {
            // variables of enum types are EnumValue objects at runtime but also the identifiers that create
            //   the enum type are EnumValue objects at run time
            return createEnumValue(frameSlot, ((EnumValueDescriptor)typeDescriptor).getEnumTypeDescriptor(), identifier);
        } else if (typeDescriptor instanceof SetDescriptor) {
            return createSetValue(frameSlot);
        } else if (typeDescriptor instanceof RecordDescriptor) {
            return createRecordValueAndInnerValues(frameSlot, (RecordDescriptor)typeDescriptor);
        } else if (typeDescriptor instanceof NilPointerDescriptor) {
            return createNilPointerValue(frameSlot);
        } else if (typeDescriptor instanceof PointerDescriptor) {
            return createPointerValue(frameSlot, (PointerDescriptor) typeDescriptor);
        }

        return null;
    }

    private StatementNode createPrimitiveValue(FrameSlot frameSlot, TypeDescriptor typeDescriptor) {
        switch (typeDescriptor.getSlotKind()) {
            case Boolean: return InitializationNodeFactory.create(frameSlot, false);
            case Long: return InitializationNodeFactory.create(frameSlot, 0);
            case Double: return InitializationNodeFactory.create(frameSlot, 0.0d);
            case Byte: return InitializationNodeFactory.create(frameSlot, '0');
            default: return null;
        }
    }

    private StatementNode createConstantValue(FrameSlot frameSlot, TypeDescriptor typeDescriptor) {
        if (typeDescriptor instanceof LongConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, (long) ((LongConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof RealConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, (double) ((RealConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof CharConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, (char) ((CharConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof StringConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, ((StringConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof BooleanConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, (boolean) ((BooleanConstantDescriptor)typeDescriptor).getValue());
        else
            return null;
    }

    private StatementNode createEnumValue(FrameSlot frameSlot, EnumTypeDescriptor enumTypeDescriptor) {
        return InitializationNodeFactory.create(frameSlot, new EnumValue(enumTypeDescriptor, enumTypeDescriptor.getDefaultValue()));
    }

    private StatementNode createEnumValue(FrameSlot frameSlot, EnumTypeDescriptor enumTypeDescriptor, String value) {
        return InitializationNodeFactory.create(frameSlot, new EnumValue(enumTypeDescriptor, value));
    }

    private StatementNode createArrayValue(FrameSlot frameSlot, ArrayDescriptor arrayDescriptor) throws LexicalException {
        return InitializationNodeFactory.create(frameSlot, arrayDescriptor.getDefaultValue());
    }

    private StatementNode createSetValue(FrameSlot frameSlot) throws LexicalException {
        return InitializationNodeFactory.create(frameSlot, new SetTypeValue());
    }

    private StatementNode createRecordValueAndInnerValues(FrameSlot frameSlot, RecordDescriptor descriptor) throws LexicalException {
        RecordValue record = (RecordValue) descriptor.getDefaultValue();
        return InitializationNodeFactory.create(frameSlot, record);
    }

    private StatementNode createNilPointerValue(FrameSlot frameSlot) throws LexicalException {
        return InitializationNodeFactory.create(frameSlot, PointerValue.NIL);
    }

    private StatementNode createPointerValue(FrameSlot frameSlot, PointerDescriptor descriptor) throws LexicalException {
        return InitializationNodeFactory.create(frameSlot, new PointerValue(descriptor));
    }

}
