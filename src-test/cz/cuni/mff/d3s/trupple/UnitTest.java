package cz.cuni.mff.d3s.trupple;

import org.junit.Test;
import java.util.Collections;

public class UnitTest extends JUnitTest {

    private String import_math="UNIT math;\n"+
            "\n"+
            "INTERFACE\n"+
            "\n"+
            " function add(a,b:integer): integer;\n"+
            " function sub(a,b:integer): integer;\n"+
            " function getCalls: integer;\n"+
            "\n"+
            " type definitionSet = (int, r, complex);\n"+
            "\n"+
            "IMPLEMENTATION\n"+
            "\n"+
            " var callsCount: integer;\n"+
            "\n"+
            " procedure increaseCallsCount;\n"+
            " begin\n"+
            " callsCount := callsCount + 1;\n"+
            " end;\n"+
            "\n"+
            " function add(a,b:integer): integer;\n"+
            " begin\n"+
            " increaseCallsCount;\n"+
            " add := a + b;\n"+
            " end;\n"+
            "\n"+
            " function sub(a,b:integer): integer;\n"+
            " begin\n"+
            " increaseCallsCount;\n"+
            " sub := a - b;\n"+
            " end;\n"+
            "\n"+
            " function getCalls: integer;\n"+
            " begin\n"+
            " getCalls := callsCount;\n"+
            " end;\n"+
            "\n"+
            "END.";

    @Test
    public void simpleTest() {
        String source = "program main;\n"+
                "\n"+
                "uses math;\n"+
                "var i:integer;\n"+
                " d:definitionSet;\n"+
                " callscount: integer;\n"+
                "\n"+
                "begin\n"+
                " callscount := -5;\n"+
                " write(add(2,3));\n"+
                " write(sub(2,8));\n"+
                " write(getCalls);\n"+
                " write(callscount);\n"+
                "end.";
        String output = "5-62-5";

        this.test(source, Collections.singletonList(this.import_math), output, true);
    }
}
