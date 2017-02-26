package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.trupple.language.parser.IParser;
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
		setInput(input);
		test(sourceCode, expectedOutput);
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
		setUpStreams();
		// TODO: solve this duplicity with task 27
        IParser parser = (useTPExtension)? new cz.cuni.mff.d3s.trupple.language.parser.tp.Parser() : new cz.cuni.mff.d3s.trupple.language.parser.wirth.Parser();
		PascalLanguage.startFromCodes(sourceCode, imports, parser);
		assertEquals(expectedOutput, output.toString() + error.toString());
	}
}
