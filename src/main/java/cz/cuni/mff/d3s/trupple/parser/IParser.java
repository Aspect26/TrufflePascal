package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

public interface IParser {

    void SemErr(String str);

    void Parse(Source source);

    void reset();

    boolean hadErrors();

    void setExtendedGoto(boolean extendedGoto);

    boolean isUsingTPExtension();

    RootNode getRootNode();

}
