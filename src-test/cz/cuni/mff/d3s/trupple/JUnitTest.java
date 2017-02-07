package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

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
		test(sourceCode, "UnnamedTest", expectedOutput);
	}

	protected void test(String sourceCode, String codeDescription, String expectedOutput) {
		test(sourceCode, new ArrayList<>(), codeDescription, expectedOutput);
	}

	protected void test(String sourceCode, List<String> imports, String codeDescription, String expectedOutput) {
		setUpStreams();
		PascalLanguage.startFromCodes(sourceCode, imports, codeDescription);
		assertEquals(expectedOutput, output.toString() + error.toString());
	}
}
