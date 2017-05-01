package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import org.junit.Before;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import org.junit.Ignore;

@Ignore
public abstract class JUnitTest {

	protected ByteArrayOutputStream output;
	private ByteArrayOutputStream error;
	private PolyglotEngine engine;

	@Before
	public void setUp() {
		output = new ByteArrayOutputStream();
		error = new ByteArrayOutputStream();
        engine = PolyglotEngine.newBuilder().setIn(System.in).setOut(System.out).setErr(error).build();

        assertTrue(engine.getLanguages().containsKey(PascalLanguage.MIME_TYPE));
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
	}

	public void clearOutputs() {
        output = new ByteArrayOutputStream();
        error = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
    }
	
	private void setInput(String input) {
        engine = PolyglotEngine.newBuilder().setIn(new ByteArrayInputStream(input.getBytes())).setOut(System.out).setErr(error).build();
	}

	void testWithInput(String sourceCode, String input, String expectedOutput) {
		this.testWithInput(sourceCode, input, expectedOutput, false);
	}

	void testWithInput(String sourceCode, String input, String expectedOutput, boolean useTpExtension) {
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
	    this.test(sourceCode, imports, expectedOutput, useTPExtension, extendedGotoSupport, new String[0]);
	}

	protected void test(String sourceCode, List<String> imports, String expectedOutput, boolean useTPExtension,
                        boolean extendedGotoSupport, String[] arguments) {
        Source source = this.createSource(sourceCode);
        clearOutputs();
        engine.eval(source);
        assertEquals(expectedOutput, output.toString() + error.toString());
    }

	private Source createSource(String source) {
        return Source.newBuilder(source).
                name("<testCode>").
                mimeType(PascalLanguage.MIME_TYPE).
                build();
    }

    void cleanupFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Could not cleanup test file: " + filePath);
        }
    }

}
