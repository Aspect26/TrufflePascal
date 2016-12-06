package cz.cuni.mff.d3s.trupple.language.parser;

public class FormalParameter {
    public FormalParameter(String id, String type, boolean isOutput){
        this.type = type;
        this.identifier = id;
        this.isOutput = isOutput;
    }

    public String type;
    public String identifier;
    public boolean isOutput;
}