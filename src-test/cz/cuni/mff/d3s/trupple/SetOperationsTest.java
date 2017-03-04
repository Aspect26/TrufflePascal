package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class SetOperationsTest extends JUnitTest {

    @Test
    public void equalsTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2: set of 1..10;\n"+
                "\n"+
                "begin\n"+
                " s1 := [3,5,9,2];\n"+
                " s2 := s1;\n"+
                " write(s2 = s1);\n"+
                " s1 := [1,2,3];\n"+
                " s2 := [1,2,3];\n"+
                " write(s1 = s2);\n"+
                " s1 := [5,6,8,7];\n"+
                " write(s1 = s2);\n"+
                "end.";
        String output="truetruefalse";
        this.test(code, output);
    }

    @Test
    public void notEqualsTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2: set of 1..10;\n"+
                "\n"+
                "begin\n"+
                " s1 := [3,5,9,2];\n"+
                " s2 := s1;\n"+
                " write(s2 <> s1);\n"+
                " s1 := [1,2,3];\n"+
                " s2 := [1,2,3];\n"+
                " write(s1 <> s2);\n"+
                " s1 := [5,6,8,7];\n"+
                " write(s1 <> s2);\n"+
                "end.";
        String output="falsefalsetrue";
        this.test(code, output);
    }

    @Test
    public void lessThanTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2: set of 5..13;\n"+
                "\n"+
                "begin\n"+
                " s1 := [8,5,13];\n"+
                " s2 := [10];\n"+
                " write(s1 < s2);\n"+
                " write(s2 < s1);\n"+
                "end.";
        String output="falsetrue";
        this.test(code, output);
    }

    @Test
    public void lessThanOrEqualTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2: set of 5..13;\n"+
                "\n"+
                "begin\n"+
                " s1 := [8,5,13];\n"+
                " s2 := [10];\n"+
                " write(s1 <= s2);\n"+
                " write(s2 <= s1);\n"+
                " s2 := [10,13,9];\n"+
                " write(s1<=s2);\n"+
                " write(s2<=s1);\n"+
                "end.";
        String output="falsetruetruetrue";
        this.test(code, output);
    }

    @Test
    public void integerInTest() {
        String code="program main;\n"+
                "\n"+
                "var s1: set of 5..13;\n"+
                "\n"+
                "begin\n"+
                " s1 := [8,5,13];\n"+
                " write(7 in s1);\n"+
                " write(8 in s1);\n"+
                "end.";
        String output="falsetrue";
        this.test(code, output);
    }

    @Test
    public void enumInTest() {
        String code="program main;\n"+
                "\n"+
                "var c: set of (r,g,b,a);\n"+
                "\n"+
                "begin\n"+
                " c := [r,a];\n"+
                " write(r in c);\n"+
                " write(b in c);\n"+
                "end.";
        String output="truefalse";
        this.test(code, output);
    }

    @Test
    public void differenceTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2, s3: set of 1..5;\n"+
                "begin\n"+
                " s1 := [1,2,3,4,5];\n"+
                " s2 := [1,3,5];\n"+
                " s3 := s1 - s2;\n"+
                " write(1 in s3);\n"+
                " write(2 in s3);\n"+
                " write(3 in s3);\n"+
                " write(4 in s3);\n"+
                " write(5 in s3);\n"+
                "end.";
        String output="falsetruefalsetruefalse";
        this.test(code, output);
    }

    @Test
    public void intersectionTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2, s3: set of 1..8;\n"+
                "begin\n"+
                " s1 := [1,2,3,4,5];\n"+
                " s2 := [1,3,5];\n"+
                " s3 := s1 * s2;\n"+
                " write(1 in s3);\n"+
                " write(2 in s3);\n"+
                " write(3 in s3);\n"+
                " write(4 in s3);\n"+
                " write(5 in s3);\n"+
                " write(6 in s3);\n"+
                "end.";
        String output="truefalsetruefalsetruefalse";
        this.test(code, output);
    }

    @Test
    public void unionTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2, s3: set of 1..5;\n"+
                "begin\n"+
                " s1 := [1];\n"+
                " s2 := [3,4];\n"+
                " s3 := s1 + s2;\n"+
                " write(1 in s3);\n"+
                " write(2 in s3);\n"+
                " write(3 in s3);\n"+
                " write(4 in s3);\n"+
                " write(5 in s3);\n"+
                "end.";
        String output="truefalsetruetruefalse";
        this.test(code, output);
    }

    @Test
    public void symmetricDifferenceTest() {
        String code="program main;\n"+
                "\n"+
                "var s1, s2, s3: set of 1..5;\n"+
                "begin\n"+
                " s1 := [1,2,4];\n"+
                " s2 := [3,4];\n"+
                " s3 := s1 >< s2;\n"+
                " write(1 in s3);\n"+
                " write(2 in s3);\n"+
                " write(3 in s3);\n"+
                " write(4 in s3);\n"+
                " write(5 in s3);\n"+
                "end.";
        String output="truetruetruefalsefalse";
        this.test(code, output, true);
    }
}
