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
        this.test(code, output, true);
    }

    @Test
    public void onlyReadReferenceVariableTest() {
        String code="program main;\n"+
                "var i:integer;\n"+
                "\n"+
                "procedure p(var j:integer);\n"+
                "begin\n"+
                " write(i);\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " i:=42;\n"+
                " p(i);\n"+
                "end.";
        String output="42";
        this.test(code, output, true);
    }

    @Test
    public void passIntegerReference() {
        String code="program main;\n"+
                "\n"+
                "var i:integer;\n"+
                "\n"+
                "procedure p(var j:integer);\n"+
                "begin\n"+
                " j := 5318008;\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " i := 2;\n"+
                " p(i);\n"+
                " write(i);\n"+
                "end.";
        String output="5318008";
        this.test(code, output, true);
    }

    @Test
    public void passSameVariableAsMultipleReferences() {
        String code="program main;\n"+
                "\n"+
                "var i:integer;\n"+
                "\n"+
                "procedure p(var a,b:integer);\n"+
                "begin\n"+
                " a := 3;\n"+
                " b := 5;\n"+
                " a := 8;\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " i := 2;\n"+
                " p(i, i);\n"+
                " write(i);\n"+
                "end.\n";
        String output="8";
        this.test(code, output, true);
    }

    @Test
    public void passReferencesAndNonReferences() {
        String code="program main;\n"+
                "\n"+
                "var i:integer;\n"+
                " c:char;\n"+
                "\n"+
                "procedure p(var ir:integer; cv:char; var cr:char; iv:integer);\n"+
                "begin\n"+
                " ir := 42;\n"+
                " iv := 43;\n"+
                " cr := \'a\';\n"+
                " cv := \'b\';\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " i := 2;\n"+
                " c := \'f\';\n"+
                " p(i, c, c, i);\n"+
                " write(i, c);\n"+
                "end.";
        String output="42a";
        this.test(code, output, true);
    }
}
