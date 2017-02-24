package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalOrdinal;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;

import java.awt.*;
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
            return createSetValue(frameSlot, (SetDescriptor)typeDescriptor);
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
            return InitializationNodeFactory.create(frameSlot, ((LongConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof RealConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, ((RealConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof CharConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, ((CharConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof StringConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, ((StringConstantDescriptor)typeDescriptor).getValue());
        else if (typeDescriptor instanceof BooleanConstantDescriptor)
            return InitializationNodeFactory.create(frameSlot, ((BooleanConstantDescriptor)typeDescriptor).getValue());
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
        List<OrdinalDescriptor> dimensions = arrayDescriptor.getDimensionDescriptors();

        PascalOrdinal ordinal = this.createOrdinal(dimensions.get(dimensions.size() - 1));
        Object[] data = createArrayData(arrayDescriptor.getReturnTypeDescriptor(), ordinal.getSize());
        PascalArray currentArray = new PascalArray(this.createOrdinal(dimensions.get(dimensions.size() - 1)), data);

        for (int i = dimensions.size() - 2; i > -1; --i) {
            ordinal = this.createOrdinal(dimensions.get(i));
            Object[] arrayOfArray = new Object[ordinal.getSize()];
            for (int j = 0; j < arrayOfArray.length; ++j) {
                arrayOfArray[j] = currentArray.createDeepCopy();
            }
            currentArray = new PascalArray(ordinal, arrayOfArray);
        }

        return InitializationNodeFactory.create(frameSlot, currentArray);
    }

    private StatementNode createSetValue(FrameSlot frameSlot, SetDescriptor descriptor) throws LexicalException {
        PascalOrdinal ordinal = createOrdinal(descriptor.getBaseTypeDescriptor());
        return InitializationNodeFactory.create(frameSlot, new SetTypeValue(ordinal));
    }

    private PascalOrdinal createOrdinal(OrdinalDescriptor descriptor) throws LexicalException {
        if (descriptor instanceof OrdinalDescriptor.RangeDescriptor || descriptor instanceof PrimitiveDescriptor.LongDescriptor) {
            return new PascalOrdinal.RangePascalOrdinal(descriptor.getSize(), descriptor.getFirstIndex());
        } else if (descriptor instanceof PrimitiveDescriptor.BooleanDescriptor) {
            return PascalOrdinal.booleanPascalOrdinal;
        } else if (descriptor instanceof PrimitiveDescriptor.CharDescriptor) {
            return PascalOrdinal.charPascalOrdinal;
        } else if (descriptor instanceof  EnumTypeDescriptor) {
            return new PascalOrdinal.EnumPascalOrdinal((EnumTypeDescriptor) descriptor);
        } else {
            throw new LexicalException("Not an ordinal type");
        }
    }

    private Object[] createArrayData(TypeDescriptor typeDescriptor, int size) {
        Object[] data = new Object[size];
        for (int i = 0; i < data.length; ++i) {
            data[i] = typeDescriptor.getDefaultValue();
        }

        return data;
    }
}
