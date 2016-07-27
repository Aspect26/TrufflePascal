package cz.cuni.mff.d3s.trupple.tests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

public abstract class UnitTest {

	protected final ByteArrayOutputStream output = new ByteArrayOutputStream();
	protected final ByteArrayOutputStream error = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(output));
		System.setErr(new PrintStream(error));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	protected void test(String sourceCode, String expectedOutput) {
		test(sourceCode, "UnnamedTest", expectedOutput);
	}

	protected void test(String sourceCode, String codeDescription, String expectedOutput) {
		test(sourceCode, new ArrayList<>(), codeDescription, expectedOutput);
	}

	protected void test(String sourceCode, List<String> imports, String codeDecription, String expectedOutput) {
		PascalLanguage.startFromCodes(sourceCode, imports, codeDecription);
		assertTrue(output.toString().equals(expectedOutput));
	}
}
