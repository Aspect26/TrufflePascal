package cz.cuni.mff.d3s.trupple.language.parser.identifierstable;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customtypes.EnumType;
import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.language.parser.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifiersTable {

    /** Map of all identifiers: e.g.: variable names, function names, types names, ... */
    private Map<String, TypeDescriptor> identifiersMap;

    /** Map of type identifiers: e.g.: integer, boolean, enums, records, ... */
    private Map<String, TypeDescriptor> typeDescriptors;

    public IdentifiersTable() {
        this.identifiersMap = new HashMap<>();

        this.typeDescriptors = new HashMap<>();
        addBuiltinTypes();
    }

    private void addBuiltinTypes() {
        typeDescriptors.put("integer", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("shortint", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("longint", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("byte", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("word", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("single", PrimitiveDescriptor.floatDescriptor());
        typeDescriptors.put("real", PrimitiveDescriptor.floatDescriptor());
        typeDescriptors.put("double", PrimitiveDescriptor.floatDescriptor());
        typeDescriptors.put("boolean", PrimitiveDescriptor.booleanDescriptor());
        typeDescriptors.put("char", PrimitiveDescriptor.charDescriptor());
    }

    public void addVariable(String typeName, String identifier) throws LexicalException {
        TypeDescriptor typeDescriptor = typeDescriptors.get(typeName);
        this.registerNewIdentifier(identifier, typeDescriptor);

        if (typeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new LexicalException("Unknown type: " + typeName);
        }
    }

    public void addArrayVariable(String identifier, List<IOrdinalType> ordinalDimenstions, String returnType) throws LexicalException {
        TypeDescriptor returnTypeDescriptor = typeDescriptors.get(returnType);
        TypeDescriptor typeDescriptor = new ArrayDescriptor(ordinalDimenstions, returnTypeDescriptor);

        this.registerNewIdentifier(identifier, typeDescriptor);

        if (returnTypeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new LexicalException("Unknown type: " + returnType);
        }
    }

    public void addEnumType(String name, List<String> identifiers) throws LexicalException {
        if(this.typeDescriptors.containsKey(name))
            throw new LexicalException("The type is already defined: " + name + ".");

        TypeDescriptor typeDescriptor = new EnumDescriptor(identifiers);
        this.registerNewIdentifier(name, typeDescriptor);
    }

    public void addProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        TypeDescriptor typeDescriptor = new ProcedureDescriptor(formalParameters);
        this.registerNewIdentifier(identifier, typeDescriptor);
    }

    public void addFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnType) throws LexicalException {
        TypeDescriptor returnTypeDescriptor = typeDescriptors.get(returnType);
        TypeDescriptor typeDescriptor = new FunctionDescriptor(formalParameters, returnTypeDescriptor);
        this.registerNewIdentifier(identifier, typeDescriptor);

        if (returnTypeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new LexicalException("Unknown return type: " + returnType);
        }
    }

    private void registerNewIdentifier(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        if (this.identifiersMap.containsKey(identifier)){
            throw new LexicalException("Duplicate identifier: " + identifier + ".");
        } else {
            this.identifiersMap.put(identifier, typeDescriptor);
        }
    }
}
