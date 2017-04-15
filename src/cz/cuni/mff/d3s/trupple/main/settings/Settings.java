package cz.cuni.mff.d3s.trupple.main.settings;

import cz.cuni.mff.d3s.trupple.main.settings.handlers.ImportsOptionHandler;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private static final String STANDARD_WIRTH = "wirth";
    private static final String STANDARD_TP = "turbo";

    @Option(name="-v", usage="makes compiler verbose")
    public boolean verbose;

    @Option(name="-I", handler=ImportsOptionHandler.class, usage="specifies directories, where unit files are located")
    public List<String> imports = new ArrayList<>();

    @Option(name="-std", usage="sets the standard to be used")
    public String standard = STANDARD_WIRTH;

    @Option(name="-g", usage="sets extended goto support")
    public boolean extendedGotoSupport;

    @Argument
    public List<String> arguments = new ArrayList<>();

    public Settings() {
        this.imports.add("builtinunits");
    }

    public String getSourcePath() {
        return this.arguments.get(0);
    }

    public String[] getArguments() {
        return this.arguments.subList(1, this.arguments.size()).toArray(new String[this.arguments.size() - 1]);
    }

    public static boolean isStandard(String value) {
        return value.equals(STANDARD_TP) || value.equals(STANDARD_WIRTH);
    }

    public boolean isTPExtensionSet() {
        return this.standard.equals(STANDARD_TP);
    }
}