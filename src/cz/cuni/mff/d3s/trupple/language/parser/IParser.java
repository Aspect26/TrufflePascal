package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

public interface IParser {

    void SemErr(String str);

    void Parse(Source source);

    boolean hadErrors();

    RootNode getRootNode();
}
