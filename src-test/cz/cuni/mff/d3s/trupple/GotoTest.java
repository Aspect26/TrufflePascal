package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

import java.util.ArrayList;

public class GotoTest extends JUnitTest {

    @Test
    public void firstCaseOfGotoFromStandard() {
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

    @Test
    public void secondCaseOfGotoFromStandard() {
        String code="program main;\n"+
                "label 1,2;\n"+
                "var i:integer;\n"+
                "\n"+
                "begin\n"+
                " i := 0;\n"+
                " 1: i:= i + 1;\n"+
                " write(\'j\');\n"+
                " if i < 10 then\n"+
                "  goto 1\n"+
                " else\n"+
                "  goto 2;\n"+
                " 2: write(i);\n"+
                "end.";
        String output = "jjjjjjjjjj10";
        this.test(code, new ArrayList<>(), output, false, true);
    }

    @Test
    public void thirdCaseOfGotoFromStandard() {
        String code="program main;\n"+
                "label 1,2;\n"+
                "var i:integer;\n"+
                "\n"+
                "begin\n"+
                " i := 0;\n"+
                " 1: i:= i + 1;\n"+
                " write(\'j\');\n"+
                " if i < 10 then\n"+
                "  while true do begin\n"+
                "   goto 1;\n"+
                "  end\n"+
                " else\n"+
                "  while true do begin\n"+
                "   goto 2;\n"+
                "  end;\n"+
                " 2: write(i);\n"+
                "end.";
        String output = "jjjjjjjjjj10";
        this.test(code, new ArrayList<>(), output, false, true);
    }

}
