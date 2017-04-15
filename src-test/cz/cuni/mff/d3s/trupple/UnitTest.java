package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UnitTest extends JUnitTest {

    private String UNIT_MATH ="UNIT math;\n"+
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

    private String UNIT_COMPLEX = "UNIT complex;\n"+
            "\n"+
            "uses math;\n"+
            "\n"+
            "INTERFACE\n"+
            "\n"+
            "type complexInteger = record\n"+
            " r,i: integer;\n"+
            "end;\n"+
            "\n"+
            "function addComplex(a,b: complexInteger): complexInteger;\n"+
            "\n"+
            "IMPLEMENTATION\n"+
            "\n"+
            "function addComplex(a,b: complexInteger): complexInteger;\n"+
            "var result: complexInteger;\n"+
            "begin\n"+
            " result.r := add(a.r, b.r);\n"+
            " result.i := add(a.i, b.i);\n"+
            " addComplex := result;\n"+
            "end;\n"+
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

        this.test(source, Collections.singletonList(this.UNIT_MATH), output, true);
    }

    @Test
    public void recursiveUnitWithRecordTest() {
        String source = "program recursiveUnitTest;\n"+
                "uses complex;\n"+
                "var a,b,c:complexInteger;\n"+
                "\n"+
                "begin\n"+
                " a.r := 2;\n"+
                " b.r := 3;\n"+
                " c := addComplex(a,b);\n"+
                " write(c.r);\n"+
                " write(getCalls);\n"+
                "end.";
        String output = "52";

        this.test(source, Arrays.asList(UNIT_MATH, UNIT_COMPLEX), output, true);
    }

}
