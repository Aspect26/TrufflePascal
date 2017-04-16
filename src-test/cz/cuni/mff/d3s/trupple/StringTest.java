package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class StringTest extends JUnitTest {

    @Test
    public void stringSimpleTest() {
        test("program main; begin write('For the Lich King!'); end.", "For the Lich King!");
    }

    @Test
    public void stringDoubleQuotesTest() {
        test("program main; begin write('Don''t Panic!'); end.", "Don't Panic!");
    }

    @Test
    public void stringDoubleQuotes2Test() {
        test("program main; begin write(''''); end.", "'");
    }

    @Test
    public void stringDoubleQuotes3Test() {
        test("program main; begin write('''So it begin.'''); end.", "'So it begin.'");
    }

    @Test
    public void stringDoubleQuotes4Test() {
        test("program main; begin write('N''Zoth', ' is an old god.'); end.", "N'Zoth is an old god.");
    }

}
