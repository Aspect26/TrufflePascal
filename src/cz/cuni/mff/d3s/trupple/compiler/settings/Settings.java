package cz.cuni.mff.d3s.trupple.compiler.settings;

import cz.cuni.mff.d3s.trupple.compiler.settings.handlers.ImportsOptionHandler;
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

    @Argument
    public String sourcePath;

    public String getSourcePath() {
        return this.sourcePath;
    }

    public static boolean isStandard(String value) {
        return value.equals(STANDARD_TP) || value.equals(STANDARD_WIRTH);
    }

    public boolean isTPExtensionSet() {
        return this.standard.equals(STANDARD_TP);
    }
}