package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class GotoTest extends JUnitTest {

    @Test
    public void gotoFirstOptionOfStandard() {
        String code="program main;\n"+
                "label 42;\n"+
                "var i:integer;\n"+
                "\n"+
                "begin\n"+
                " i := 0;\n"+
                " 42: if i < 10 then begin\n"+
                " i := i + 1;\n"+
                " goto 42;\n"+
                " end;\n"+
                "\n"+
                " write(i);\n"+
                "end.";
        String output = "10";
        this.test(code, output);
    }

}
