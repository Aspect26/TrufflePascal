package cz.cuni.mff.d3s.trupple.parser.utils;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

/**
 * Structure representing a subroutine formal parameter. It contains the identifier of the parameter, its type descriptor,
 * flag whether it is a reference passed parameter and descriptor of the subroutine to which it belongs.
 */
public class FormalParameter {

    public FormalParameter(String identifier, TypeDescriptor type, boolean isReference, SubroutineDescriptor subroutine) {
        this.type = type;
        this.identifier = identifier;
        this.isReference = isReference;

        if (subroutine != null) {
            this.isSubroutine = true;
            this.subroutine = subroutine;
        }
    }

    public FormalParameter(String identifier, TypeDescriptor type, boolean isReference) {
        this(identifier, type, isReference, null);
    }

    public TypeDescriptor type;
    public String identifier;
    public boolean isReference;
    public boolean isSubroutine;
    public SubroutineDescriptor subroutine;

}