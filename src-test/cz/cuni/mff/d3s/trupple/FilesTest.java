package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class FilesTest extends JUnitTest {

    @Test
    public void simpleStoreLoadIntTest(){
        String code = "program main;\n"+
                "\n"+
                "var f: file of integer;\n"+
                " i: integer;\n"+
                "\n"+
                "begin\n"+
                " assign(f,\'test.out\');\n"+
                " rewrite(f);\n"+
                " write(f, 42);\n"+
                "\n"+
                " reset(f);\n"+
                " read(f, i);\n"+
                " write(i);\n"+
                "end.";

        test(code, "42", true);
        // TODO: cleanup the created file
    }

    @Test
    public void simpleStoreLoadMultipleCharsTest(){
        String code = "program main;\n"+
                "\n"+
                "var f: file of char;\n"+
                " c: char;\n"+
                " i: integer;\n"+
                "\n"+
                "begin\n"+
                " assign(f,\'test.out\');\n"+
                " rewrite(f);\n"+
                " write(f, 'H');\n"+
                " write(f, 'a');\n"+
                " write(f, 'r');\n"+
                " write(f, 'd');\n"+
                " write(f, 'W');\n"+
                " write(f, 'i');\n"+
                " write(f, 'r');\n"+
                " write(f, 'e');\n"+
                " write(f, 'd');\n"+
                "\n"+
                " reset(f);\n"+
                " for i:=1 to 9 do begin\n"+
                "  read(f, c);\n"+
                "  write(c);\n"+
                " end;\n"+
                "end.";

        test(code, "HardWired", true);
        // TODO: cleanup the created file
    }

}
