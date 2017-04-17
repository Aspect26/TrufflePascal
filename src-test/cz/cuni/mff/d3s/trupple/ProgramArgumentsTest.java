package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class ProgramArgumentsTest extends JUnitTest {

    @Test
    public void copyFileFromProgramArgumentsTest() {

        PrintWriter writer;
        try {
            writer = new PrintWriter("in.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.print("Could not create test file");
            return;
        }

        String input = String.format("I'm one with the Force. The Force is with me.%nI'm one with the Force. The Force is with me.%nI'm one with the Force. The Force is with me.");
        writer.print(input);
        writer.close();

        String source = "program copy(input, output);\n"+
                "var input, output: file of char; c:char;\n"+
                "\n"+
                "begin\n"+
                " reset(input);\n"+
                " rewrite(output);\n"+
                " while not eof(input) do begin\n"+
                " read(input, c);\n"+
                " write(output, c);\n"+
                " end;\n"+
                "end.";

        this.test(source, Collections.emptyList(), "", true, false,
                new String[] {"in.txt", "out.txt"});

        try {
            assertEquals(input, new String(Files.readAllBytes(Paths.get("out.txt"))));
        } catch (IOException e) {
            System.out.print("Could not read output test file");
            return;
        }
        this.cleanupFile("in.txt");
        this.cleanupFile("out.txt");

    }

}
