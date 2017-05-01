package cz.cuni.mff.d3s.trupple.main.settings;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.kohsuke.args4j.spi.StringOptionHandler;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private static final String STANDARD_WIRTH = "wirth";
    private static final String STANDARD_TP = "turbo";

    @Option(name="-I", handler=StringArrayOptionHandler.class, usage="specifies directories, where unit files are located")
    public List<String> imports = new ArrayList<>();

    @Option(name="-std", usage="sets the standard to be used")
    public String standard = STANDARD_WIRTH;

    @Option(name="-g", usage="sets extended goto support")
    public boolean extendedGotoSupport;

    @Option(name="-s", handler=StringOptionHandler.class, usage="sets the source to be evaluated")
    public String source = null;

    public Settings() {
        this.imports.add("./builtinunits");
    }

    public String getSourcePath() {
        return source;
    }

    public boolean isTPExtensionSet() {
        return this.standard.equals(STANDARD_TP);
    }

}