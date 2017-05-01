package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class StringsUnitTest extends JUnitTest {

    @Test
    public void strallocStrCompStrupperTest(){
        String code = "program pcharTest;\n"+
                "uses strings;\n"+
                "var s1, s2: PChar;\n"+
                "\n"+
                "begin\n"+
                " s1 := stralloc(5);\n"+
                " s1 := \'asdf\';\n"+
                "\n"+
                " s2 := stralloc(41);\n"+
                " s2 := \'Metallica - 2nd of April 2018 in Prague.\';\n"+
                "\n"+
                " write(strcomp(s1, s2));\n"+
                " s1 := strupper(s2);\n"+
                " write(s1^);\n"+
                "end.";

        String output = "-36METALLICA - 2ND OF APRIL 2018 IN PRAGUE." + String.valueOf('\0');
        test(code, output, true);
    }

}
