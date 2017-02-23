package cz.cuni.mff.d3s.trupple.language.parser;

public class FormalParameter {
    public FormalParameter(String id, String type, boolean isReference){
        this.type = type;
        this.identifier = id;
        this.isReference = isReference;
    }

    public String type;
    public String identifier;
    public boolean isReference;
}