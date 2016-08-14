package cz.cuni.mff.d3s.trupple.compiler;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public class ImportsOptionHandler extends OptionHandler<String> {
	public ImportsOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super String> setter) {
        super(parser, option, setter);
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
    	String dir = null;
    	int index = 0;
    	while(index<params.size()-1){
    		dir = params.getParameter(index++);
    		if(dir.matches("-.*"))
    			break;
    		
    		setter.addValue(dir);
    	}
    	
    	return 1;
    }

    @Override
    public String getDefaultMetaVariable() {
        return null;
    }
}
