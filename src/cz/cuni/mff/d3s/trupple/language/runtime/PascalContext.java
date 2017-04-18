package cz.cuni.mff.d3s.trupple.language.runtime;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import com.oracle.truffle.api.ExecutionContext;

public final class PascalContext extends ExecutionContext {

    private static PascalContext instance;

    public static PascalContext getInstance() {
        if (instance == null) {
            instance = new PascalContext();
            instance.random = new Random(26270);
        }
        return instance;
    }
	
	private Scanner input;
	private PrintStream output;
    private Random random;

	public Scanner getInput() {
		return input;
	}

	public PrintStream getOutput() {
		return output;
	}

	public void setInput(Scanner input) {
	    this.input = input;
    }

    public void setOutput(PrintStream output) {
	    this.output = output;
    }

    public void randomize() {
        random = new Random();
    }

    public long getRandom(long upperBound) {
        return Math.abs(random.nextLong()) % upperBound;
    }

}
