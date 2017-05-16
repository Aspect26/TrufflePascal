package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class VariableAccessTest extends JUnitTest {

    @Test
    public void complexAccessTest() {
        String source = "program complexAccess;\n"+
                "\n"+
                "const index = 7;\n"+
                "\n"+
                "type pint = ^integer;\n"+
                " iArr = array[1..10] of pint;\n"+
                " arrWrap = record a: iArr; end;\n"+
                "\n"+
                "function getComplex: arrWrap;\n"+
                "var result: arrWrap;\n"+
                "begin\n"+
                " new(result.a[index]);\n"+
                " result.a[index]^ := 147;\n"+
                " getComplex := result;\n"+
                "end;\n"+
                "\n"+
                "function getComplexP(value: integer): arrWrap;\n"+
                "var result: arrWrap;\n"+
                "begin\n"+
                " new(result.a[index]);\n"+
                " result.a[index]^ := value;\n"+
                " getComplexP := result;\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " write(getComplex.a[index]^);\n"+
                " write(getComplexP(getComplex.a[index]^).a[index]^);\n"+
                "end.";
        String output = "147147";
        this.test(source, output);
    }

}
