package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class ReferencePassingTest extends JUnitTest {

    @Test
    public void onlyPassTest() {
        String code="program main;\n"+
                "var i:integer;\n"+
                "\n"+
                "procedure p(var j:integer);\n"+
                "begin\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " p(i);\n"+
                " write('Is this the real life?');\n"+
                "end.";
        String output="Is this the real life?";
        this.test(code, output);
    }
}
