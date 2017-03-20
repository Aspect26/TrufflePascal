package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class StringUnitTest extends JUnitTest {

    @Test
    public void strupperTest(){
        String code = "program main;\n"+
                "uses string;\n"+
                "var a:string;\n"+
                "\n"+
                "begin\n"+
                " a := \'Metallica - 2nd of April 2018 in Prague.\';\n"+
                " write(a);\n"+
                " strupper(a);\n"+
                " write(a);\n"+
                "end.";

        test(code, "Metallica - 2nd of April 2018 in Prague.METALLICA - 2ND OF APRIL 2018 IN PRAGUE.", true);
    }

}
