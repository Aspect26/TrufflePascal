package cz.cuni.mff.d3s.trupple;

import com.oracle.truffle.api.vm.PolyglotEngine;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class ProgramOutputTest extends JUnitTest {

    @Test
    public void noReturnTest() {
        String source = "program none; begin end.";
        this.testResultValue(source, 0);
    }

    @Test
    public void exitCodeReturnTest() {
        String source = "program none; begin halt(28); end.";
        this.testResultValue(source, 28);
    }

    private void testResultValue(String sourceCode, int expectedValue) {
        PolyglotEngine engine = this.givenExecutionEngine();
        setTPSupport();
        int resultValue = thenEvaluateSource(engine, sourceCode);
        verifyResult(resultValue, expectedValue);
    }

    private PolyglotEngine givenExecutionEngine() {
        return PolyglotEngine.newBuilder().setOut(System.out).setErr(System.err).build();
    }

    private void setTPSupport() {
        PascalLanguage.INSTANCE.reset(true, false);
    }

    private int thenEvaluateSource(PolyglotEngine engine, String soureCode) {
        PolyglotEngine.Value mainFunction = engine.eval(this.createSource(soureCode)).execute();
        PolyglotEngine.Value returnValue = mainFunction.execute();
        return returnValue.as(int.class);
    }

    private void verifyResult(int resultValue, int expectedValue) {
        assertEquals(resultValue, expectedValue);
    }

}
