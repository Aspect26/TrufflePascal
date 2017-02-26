package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class LongerProgramsTest extends JUnitTest {

    @Test
    public void setOperations() {
        String code = "program setColors;\n"+
                "type\n"+
                " color = (red, blue, yellow, green, white, black, orange);\n"+
                " colors = set of color;\n"+
                "var\n"+
                " c : colors;\n"+
                "\n"+
                "procedure displayColors(c : colors);\n"+
                "var currentColor: color;\n"+
                "begin\n"+
                " write(\'[\');\n"+
                "\n"+
                " if red in c then write(\'red, \');\n"+
                " if blue in c then write(\'blue, \');\n"+
                " if yellow in c then write(\'yellow, \');\n"+
                " if green in c then write(\'green, \');\n"+
                " if white in c then write(\'white, \');\n"+
                " if black in c then write(\'black, \');\n"+
                " if orange in c then write(\'orange, \');\n"+
                "\n"+
                " write(\']\');\n"+
                "end;\n"+
                "\n"+
                "\n"+
                "begin\n"+
                " c:= [red, blue, yellow, green, white, black, orange];\n"+
                " displayColors(c);\n"+
                "\n"+
                " c:=[red, blue]+[yellow, green];\n"+
                " displayColors(c);\n"+
                "\n"+
                " c:=[red, blue, yellow, green, white, black, orange] - [green, white];\n"+
                " displayColors(c);\n"+
                "\n"+
                " c:= [red, blue, yellow, green, white, black, orange] * [green, white];\n"+
                " displayColors(c);\n"+
                "end.\n"+
                "";
        String output = "[red, blue, yellow, green, white, black, orange, ][red, blue, yellow, green, ][red, blue, yellow, black, orange, ][green, white, ]";
        this.test(code, output);
    }
}
