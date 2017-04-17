package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        this.cleanupFile("test.out");
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
        this.cleanupFile("test.out");
    }

    @Test
    public void simpleStoreLoadEnumTest(){
        String code = "program main;\n"+
                "\n"+
                "type color = (red, green, blue);\n"+
                "\n"+
                "var f: file of color;\n"+
                " c: color;\n"+
                "\n"+
                "begin\n"+
                " assign(f,\'test.out\');\n"+
                " rewrite(f);\n"+
                " write(f, green);\n"+
                "\n"+
                " reset(f);\n"+
                " read(f, c);\n"+
                " write(c = green);\n"+
                "end.";

        test(code, "true", true);
        this.cleanupFile("test.out");
    }

    @Test
    public void eofFileTest() {
        String code = "program main;\n"+
                "\n"+
                "var f: file of integer; i:integer;\n"+
                "\n"+
                "begin\n"+
                " assign(f,\'out.bin\');\n"+
                " rewrite(f);\n"+
                " write(f, 42);\n"+
                " write(f, 2);\n"+
                " write(f, 28);\n"+
                " write(f, 26);\n"+
                "\n"+
                " reset(f);\n"+
                " while not eof(f) do begin\n"+
                "  read(f, i);\n"+
                "  write(i,0);\n"+
                " end;\n"+
                "end.";

        String output = "42020280260";
        test(code, output, true);
        this.cleanupFile("out.bin");
    }

    @Test
    public void eolFileTest() {
        String code = "program main;\n"+
                "\n"+
                "var f: file of char; c:char;\n"+
                "\n"+
                "begin\n"+
                " assign(f,\'out.txt\');\n"+
                " rewrite(f);\n"+
                " writeln(f, \'Not dead which eternal lie\');\n"+
                " writeln(f, \'Stranger eons death may die\');\n"+
                "\n"+
                " reset(f);\n"+
                " while not eol(f) do begin\n"+
                "  read(f, c);\n"+
                "  write(c);\n"+
                " end;\n"+
                "end.";

        String output = "Not dead which eternal lie";
        test(code, output, true);
        this.cleanupFile("out.txt");
    }

    private void cleanupFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Could not cleanup test file: " + filePath);
        }
    }

}
