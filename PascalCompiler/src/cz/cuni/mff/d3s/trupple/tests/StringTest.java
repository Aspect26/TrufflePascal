package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class StringTest extends JUnitTest {

    @Test
    public void stringSimpleTest() {
        test("begin write('For the Lich King!'); end.", "For the Lich King!");
    }

    @Test
    public void stringDoubleQuotes() {
        test("begin write('Don''t Panic!'); end.", "Don't Panic!");
    }

    @Test
    public void stringDoubleQuotes2() {
        test("begin write(''''); end.", "'");
    }

    @Test
    public void stringDoubleQuotes3() {
        test("begin write('''So it begin.'''); end.", "'So it begin.'");
    }
}
