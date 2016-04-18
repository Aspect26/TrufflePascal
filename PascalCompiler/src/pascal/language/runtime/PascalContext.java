package pascal.language.runtime;

import java.io.BufferedReader;
import java.io.PrintWriter;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.source.Source;

import pascal.language.parser.Parser;

public class PascalContext extends ExecutionContext {
	private final BufferedReader input;
    private final PrintWriter output;
    private final TruffleLanguage.Env env;
    
    public PascalContext(){
    	this(null, null, null, false);
    }
    
	public PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintWriter output) {
        this(env, input, output, true);
    }

	private PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintWriter output, boolean installBuiltins) {
        this.input = input;
        this.output = output;
        this.env = env;
    }
	
	public void evalSource(Source source){
		Parser.parsePascal(this, source);
	}
	
	/**
	 * Returns the default input
	 */
	public BufferedReader getInput() {
        return input;
    }
	
	/**
	 * Returns the default output
	 */
	public PrintWriter getOutput() {
        return output;
    }
}
