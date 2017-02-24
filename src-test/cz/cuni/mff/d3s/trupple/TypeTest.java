package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class TypeTest extends JUnitTest {

    @Test
    public void simpleTestEnum() {
        String code="program main; \n"+
                "type color=(r,g,b);\n"+
                "var c1:color;\n"+
                "\n"+
                "begin\n"+
                " c1:=b;\n"+
                " if c1=b then write(\'Save the rebellion! Save the dream!\');\n"+
                "end.\n"+
                "";

        String output = "Save the rebellion! Save the dream!";
        this.test(code, output);
    }

    @Test
    public void simpleTestArray() {
        String code="program main; \n"+
                "type a=array[1..5] of integer;\n"+
                "var a1:a;\n"+
                "\n"+
                "begin\n"+
                " a1[3] := 8;\n"+
                " write(a1[3]);\n"+
                "end.\n"+
                "";

        String output = "8";
        this.test(code, output);
    }

    @Test
    public void simpleTypeToTypeAssignment() {
        String code="program main; \n"+
                "type Color=(r,g,b);\n"+
                "     Color1=Color;\n"+
                "\n"+
                "var c:Color;\n"+
                "    c1:Color1;\n"+
                "\n"+
                "begin\n"+
                " c:=g;\n"+
                " c1:=g;\n"+
                "\n"+
                " if c=c1 then\n"+
                " write(\'Be careful not to choke on your aspirations, director!\');\n"+
                "end.\n"+
                "";

        String output = "Be careful not to choke on your aspirations, director!";
        this.test(code, output);
    }
}
