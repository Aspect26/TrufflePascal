package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class SetTypeTest extends JUnitTest {

    @Test
    public void setTypeVariableDeclarations() {
        String code="program main;\n"+
                "\n"+
                "var s1: set of char;\n"+
                " s2: set of 1..3;\n"+
                " s3: set of (club, diamond, heart, spade);\n"+
                "\n"+
                "begin\n"+
                " write(\'Speak the words of wisdom!\');\n"+
                "end.";
        String output="Speak the words of wisdom!";
        this.test(code, output);
    }

    @Test
    public void setTypeDefinitions() {
        String code="program main;\n"+
                "\n"+
                "type\n"+
                " Days = (mon, tue, wed, thu, fri, sat, sun);\n"+
                " Letters = set of char;\n"+
                " DaySet = set of days;\n"+
                " studentAge = set of 13..20;\n"+
                "\n"+
                "var s1: studentAge;\n"+
                "\n"+
                "begin\n"+
                " write(\'Unicorn invasion of dundee!\');\n"+
                "end.";
        String output="Unicorn invasion of dundee!";
        this.test(code, output);
    }
}
