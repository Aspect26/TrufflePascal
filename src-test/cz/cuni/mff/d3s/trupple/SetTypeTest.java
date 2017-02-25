package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class SetTypeTest extends JUnitTest {

    @Test
    public void setTypeVariableDeclarationsTest() {
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
    public void setTypeDefinitionsTest() {
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

    @Test
    public void setConstructorAndAssignmentTest() {
        String code="program main;\n"+
                "\n"+
                "var s1: set of 1..10;\n"+
                "\n"+
                "begin\n"+
                " s1 := [3,5,9,2];\n"+
                " write(\'Hail to hoots!\');\n"+
                "end.";
        String output="Hail to hoots!";
        this.test(code, output);
    }

    @Test
    public void setVariablesAsssignment() {
        String code="program main;\n"+
                "\n"+
                "var s1: set of 1..10;\n"+
                " s2: set of 1..10;\n"+
                "\n"+
                "begin\n"+
                " s1 := [3,5,9,2];\n"+
                " s2 := s1;\n"+
                " write(\'The mighty legend of Crail.\');\n"+
                "end.";
        String output="The mighty legend of Crail.";
        this.test(code, output);
    }
}
