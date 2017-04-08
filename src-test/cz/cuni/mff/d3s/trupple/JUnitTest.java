package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import org.junit.Ignore;

@Ignore
public abstract class JUnitTest {

	protected ByteArrayOutputStream output;
	protected ByteArrayOutputStream error;

	@Before
	public void setUpStreams() {
		output = new ByteArrayOutputStream();
		error = new ByteArrayOutputStream();

		System.setOut(new PrintStream(output));
		System.setErr(new PrintStream(error));
	}
	
	private void setInput(String input) {
		System.setIn(new ByteArrayInputStream(input.getBytes()));
	}

	protected void testWithInput(String sourceCode, String input, String expectedOutput) {
		this.testWithInput(sourceCode, input, expectedOutput, false);
	}

	protected void testWithInput(String sourceCode, String input, String expectedOutput, boolean useTpExtension) {
		setInput(input);
		test(sourceCode, expectedOutput, useTpExtension);
	}

	protected void test(String sourceCode, String expectedOutput) {
		test(sourceCode, expectedOutput, false);
	}

	protected void test(String sourceCode, String expectedOutput, boolean useTPExtension) {
		test(sourceCode, new ArrayList<>(), expectedOutput, useTPExtension);
	}

    protected void test(String sourceCode, List<String> imports, String expectedOutput) {
	    test(sourceCode, imports, expectedOutput, false);
    }

    protected void test(String sourceCode, List<String> imports, String expectedOutput, boolean useTPExtension) {
	    this.test(sourceCode, imports, expectedOutput, useTPExtension, false);
    }

	protected void test(String sourceCode, List<String> imports, String expectedOutput, boolean useTPExtension,
                        boolean extendedGotoSupport) {
		setUpStreams();
		PascalLanguage.startFromCodes(sourceCode, imports, useTPExtension, extendedGotoSupport);
		assertEquals(expectedOutput, output.toString() + error.toString());
	}
}
