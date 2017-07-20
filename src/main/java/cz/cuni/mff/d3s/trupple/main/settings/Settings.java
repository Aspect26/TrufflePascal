package cz.cuni.mff.d3s.trupple.main.settings;

import cz.cuni.mff.d3s.trupple.main.settings.handlers.StandardOptionHandler;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.kohsuke.args4j.spi.StringOptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing definitions of Trupple's arguments and options using args4j library.
 */
public class Settings {

    @Option(name="-I", handler=StringArrayOptionHandler.class, usage="specifies directories, where unit files are located")
    private List<String> includeDirectories = new ArrayList<>();

    @Option(name="-std", handler= StandardOptionHandler.class, usage="sets the standard to be used")
    private String standard;

    @Option(name="-j", usage="sets extended goto support")
    private boolean extendedGotoSupport = false;

    @Argument
    private List<String> arguments = new ArrayList<>();

    public Settings() {
        this.includeDirectories.add("./builtinunits");
    }

    /**
     * Gets value of required source path arguments.
     */
    public String getSourcePath() {
        if (this.arguments.size() == 0) {
            throw new IllegalArgumentException("No source file specified.");
        } else if (this.arguments.size() > 1) {
            throw new IllegalArgumentException("Only one source file can be specified.");
        }
        return this.arguments.get(0);
    }

    /**
     * Checks whether the option for setting Turbo Pascal's extensions was set.
     */
    public boolean usesTPExtension() {
        return StandardOptionHandler.isTurbo(this.standard);
    }

    /**
     * Checks whether the option for setting extended goto support was set.
     */
    public boolean usesExtendedGoto() {
        return this.extendedGotoSupport;
    }

    /**
     * Gets the value of include directories option.
     */
    public List<String> getIncludeDirectories() {
        return this.includeDirectories;
    }

}