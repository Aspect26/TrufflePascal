package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	    this.test(sourceCode, imports, expectedOutput, useTPExtension, extendedGotoSupport, new String[0]);
	}

	protected void test(String sourceCode, List<String> imports, String expectedOutput, boolean useTPExtension,
                        boolean extendedGotoSupport, String[] arguments) {
        List<String> importsWithBuiltin = new ArrayList<>();
        String stringsUnit = getStringsUnit();
        if (stringsUnit != null) {
            importsWithBuiltin.add(stringsUnit);
        }
        importsWithBuiltin.addAll(imports);
        setUpStreams();
        PascalLanguage.startFromCodes(sourceCode, arguments, importsWithBuiltin, useTPExtension, extendedGotoSupport);
        assertEquals(expectedOutput, output.toString() + error.toString());
    }

	private String getStringsUnit() {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get("./builtinunits/strings.pas"));
        } catch (IOException e) {
            System.err.print("Could not read strings unit");
            return null;
        }
        return new String(encoded);
    }

    void cleanupFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Could not cleanup test file: " + filePath);
        }
    }

}
