package cz.cuni.mff.d3s.trupple.main.settings.handlers;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public class StandardOptionHandler extends OptionHandler<String> {

    private static final String STANDARD_WIRTH = "wirth";
    private static final String STANDARD_TP = "turbo";
    private final CmdLineParser parser;

    protected StandardOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super String> setter) {
        super(parser, option, setter);
        this.parser = parser;
    }

    @Override
    public int parseArguments(Parameters parameters) throws CmdLineException {
        String value = parameters.getParameter(0);
        if (!value.equals(STANDARD_TP) && !value.equals(STANDARD_WIRTH)) {
            throw new CmdLineException(parser, "Unknown value provided to the option. Expected one of the values: " + STANDARD_TP + "," + STANDARD_WIRTH, new IllegalArgumentException());
        }
        setter.addValue(value);
        return 1;
    }

    @Override
    public String getDefaultMetaVariable() {
        return STANDARD_TP;
    }

    public static boolean isTurbo(String value) {
        return value.equals(STANDARD_TP);
    }
}
