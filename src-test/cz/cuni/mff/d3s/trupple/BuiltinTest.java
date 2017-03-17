package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class BuiltinTest extends JUnitTest {

    @Test
    public void arithmeticOperationsTest() {
        String code = "program main;\n"+
                "\n"+
                "begin\n"+
                " write(abs(-8));\n"+
                " write(abs(6.16) > 6.15 and abs(-6.16) < 6.17);\n"+
                " write(sqr(2));\n"+
                " write(sin(1) > 0.83 and sin(1) < 0.85);\n"+
                " write(cos(1) > 0.53 and cos(1) < 0.55);\n"+
                " write(exp(ln(6)) > 5.98 and exp(ln(6)) < 6.02);\n"+
                " write(ln(exp(1)) > 0.98 and ln(exp(1)) < 1.02);\n"+
                " write(sqrt(64) > 7.98 and sqrt(64) < 8.02);\n"+
                " write(arctan(90) > 1.55 and arctan(90) < 1.57);\n"+
                "end.";

        String output = "8true4truetruetruetruetruetrue";
        test(code, output);
    }

}
