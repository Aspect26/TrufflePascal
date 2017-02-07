package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

import java.util.Arrays;

public class UnitTest extends JUnitTest {

    private String import_math="UNIT math;\n"+
            "\n"+
            "INTERFACE\n"+
            "\n"+
            " function add(a,b:integer): integer;\n"+
            " function sub(a,b:integer): integer;\n"+
            "\n"+
            "\n"+
            "IMPLEMENTATION\n"+
            "\n"+
            " function add(a,b:integer): integer;\n"+
            " begin\n"+
            " add := a + b;\n"+
            " end;\n"+
            "\n"+
            " function sub(a,b:integer): integer;\n"+
            " begin\n"+
            " sub := add(a, -b);\n"+
            " end;\n"+
            "\n"+
            "\n"+
            "END.\n"+
            "";

    @Test
    public void simpleTest() {
        String source = "program main; \n"+
                "uses math;\n"+
                "\n"+
                "begin\n"+
                " write(add(3,5));\n"+
                " write(sub(3,5));\n"+
                "end.\n";
        String output = "8-2";

        this.test(source, Arrays.asList(this.import_math), "math_import_test", output);
    }
}
