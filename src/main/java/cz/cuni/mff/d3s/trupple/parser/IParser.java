package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

/**
 * Interface for both our parsers.
 */
public interface IParser {

    /**
     * Coco/R generated function. It is used to inform parser about semantic errors.
     */
    void SemErr(String str);

    /**
     * Coco/R generated function for parsing input. The signature was changes in parsers' frame files so that it
     * receives source argument.
     *
     * @param source source to be parsed
     */
    void Parse(Source source);

    /**
     * Resets the parser but not the {@link NodeFactory} instance it holds. This way it can be used multiple times with
     * storing current state (mainly identifiers table). This is useful for parsing unit files before parsing the actual
     * source.
     */
    void reset();

    /**
     * Returns whether there were any errors throughout the parsing of the last source.
     */
    boolean hadErrors();

    /**
     * Sets support for extended goto. If this option is turned on, the parser will generate {@link cz.cuni.mff.d3s.trupple.language.nodes.statement.ExtendedBlockNode}s
     * instead of {@link cz.cuni.mff.d3s.trupple.language.nodes.statement.BlockNode}s.
     */
    void setExtendedGoto(boolean extendedGoto);

    /**
     * Returns true if the support for Turbo Pascal extensions is turned on.
     */
    boolean isUsingTPExtension();

    /**
     * Reutnrs the root node of the last parsed source.
     */
    RootNode getRootNode();

}
