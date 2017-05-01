package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class SubroutineAsArgumentTest extends JUnitTest {

    @Test
    public void simpleTest() {
        String source = "program subroutinepass;\n"+
                "\n"+
                "procedure call(function f(i:integer): integer; j: integer);\n"+
                "begin\n"+
                " write(f(j));\n"+
                "end;\n"+
                "\n"+
                "function factorial(i:integer): integer;\n"+
                "begin\n"+
                " if i < 2 then factorial := 1\n"+
                " else factorial := i * factorial(i - 1);\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " call(factorial, 7);\n"+
                "end.";

        String output = "5040";
        this.test(source, output);
    }
}
