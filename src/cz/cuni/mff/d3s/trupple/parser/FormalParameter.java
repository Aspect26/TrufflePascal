package cz.cuni.mff.d3s.trupple.parser;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class FormalParameter {

    public FormalParameter(String id, TypeDescriptor type, boolean isReference){
        this.type = type;
        this.identifier = id;
        this.isReference = isReference;
    }

    public TypeDescriptor type;
    public String identifier;
    public boolean isReference;
}