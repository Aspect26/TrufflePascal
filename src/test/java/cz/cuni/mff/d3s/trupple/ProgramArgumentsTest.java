package cz.cuni.mff.d3s.trupple;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
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
                "var input, output: text; c:char;\n"+
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

    @Test
    public void stringProgramArgumentsTest() {
        String arg1 = "I've got a jar of dirt!";
        String arg2 = "Did everyone see that? Because I will not be doing it again.";
        String arg3 = "Not all treasure’s silver and gold, mate.";

        String source = "program copy(str1, str2, str3);\n"+
                "var str1, str2, str3: string;\n"+
                "\n"+
                "begin\n"+
                " writeln(str1);\n"+
                " writeln(str2);\n"+
                " writeln(str3);\n"+
                "end.";

        String output = String.format(arg1 + "%n" + arg2 + "%n" + arg3 + "%n");

        this.test(source, Collections.emptyList(), output, true, false,
                new String[] {arg1, arg2, arg3});
    }

    @Test
    public void primitiveArgumentsTest() {
        int i = 443;
        long l = 56565656565656L;
        double r = Math.E;
        boolean b = true;

        String source = "program intArgTest(i, r, l, b);\n"+
                "var i: integer; r: real; l: longint; b: boolean;\n"+
                "begin\n"+
                " write(i,\',\',r,\',\',l,\',\',b);\n"+
                "end.";

        String output = i + "," + r + "," + l + "," + b;
        this.test(source, Collections.emptyList(), output, false, false, new Object[] {
                i, r, l, b
        });
    }

    @Test
    @Ignore
    public void intArrayArgumentTest() {
        int[][] data = {
                {3, 1, 2},
                {8, 9, 5},
                {7, 4, 6},
        };
        String source = "program intArgTest(data);\n"+
                "var data: array[1..3, 0..2] of integer; i,j: integer;\n"+
                "begin\n"+
                " for i:=1 to 3 do begin\n"+
                " for j:=0 to 2 do begin\n"+
                " write(data[i][j]);\n"+
                " end;\n"+
                " writeln;\n"+
                " end;\n"+
                "end.";

        String output = String.format(data[0][0] + data[0][1] + data[0][2] + "%n" + data[1][0] + data[1][1] + data[1][2]
                + "%n" + data[2][0] + data[2][1] + data[2][2] + "%n");

        this.test(source, Collections.emptyList(), output, false, false, new Object[]{ data });
    }

}
