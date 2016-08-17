package cz.cuni.mff.d3s.trupple.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
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

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);

		output = null;
		error = null;
	}

	protected void test(String sourceCode, String expectedOutput) {
		test(sourceCode, "UnnamedTest", expectedOutput);
	}

	protected void test(String sourceCode, String codeDescription, String expectedOutput) {
		test(sourceCode, new ArrayList<>(), codeDescription, expectedOutput);
	}

	protected void test(String sourceCode, List<String> imports, String codeDecription, String expectedOutput) {
		setUpStreams();
		PascalLanguage.startFromCodes(sourceCode, imports, codeDecription);
		assertEquals(output.toString(), expectedOutput);
	}
}
