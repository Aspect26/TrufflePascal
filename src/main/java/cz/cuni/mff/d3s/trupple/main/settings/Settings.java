package cz.cuni.mff.d3s.trupple.main.settings;

import cz.cuni.mff.d3s.trupple.main.settings.handlers.StandardOptionHandler;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.kohsuke.args4j.spi.StringOptionHandler;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    @Option(name="-I", handler=StringArrayOptionHandler.class, usage="specifies directories, where unit files are located")
    private List<String> includeDirectories = new ArrayList<>();

    @Option(name="-std", handler= StandardOptionHandler.class, usage="sets the standard to be used")
    private String standard;

    @Option(name="-g", usage="sets extended goto support")
    private boolean extendedGotoSupport = false;

    @Option(name="-s", handler=StringOptionHandler.class, usage="sets the source to be evaluated")
    private String source = null;

    public Settings() {
        this.includeDirectories.add("./builtinunits");
    }

    public String getSourcePath() {
        return source;
    }

    public boolean usesTPExtension() {
        return StandardOptionHandler.isTurbo(this.standard);
    }

    public boolean usesExtendedGoto() {
        return this.extendedGotoSupport;
    }

    public List<String> getIncludeDirectories() {
        return this.includeDirectories;
    }
}