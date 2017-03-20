package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

public interface IParser {

    void SemErr(String str);

    void Parse(Source source);

    boolean hadErrors();

    boolean isUsingTPExtension();

    RootNode getRootNode();
}
