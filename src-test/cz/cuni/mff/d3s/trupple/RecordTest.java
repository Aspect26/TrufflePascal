package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class RecordTest extends JUnitTest {

    @Test
    public void recordWithInnerRecordAndArrayTest(){
        String code = "program main;\n"+
                "\n"+
                "type arrayInner = record\n"+
                " b: boolean;\n"+
                " end;\n"+
                "\n"+
                " inner = record\n"+
                " i: integer;\n"+
                " a: array[1..10] of integer;\n"+
                " ai: array[10..15] of arrayInner;\n"+
                " end;\n"+
                "\n"+
                " rec = record\n"+
                " inception: inner;\n"+
                " end;\n"+
                "\n"+
                "var r: rec;\n"+
                " r2: inner;\n"+
                "\n"+
                "begin\n"+
                " r2.i := 5;\n"+
                " r.inception.i := 8;\n"+
                " r.inception.a[3] := 10;\n"+
                " r.inception.ai[13].b := true;\n"+
                " write(r2.i);\n"+
                " write(r.inception.i);\n"+
                " write(r.inception.a[3]);\n"+
                " write(r.inception.ai[13].b);\n"+
                "end.";

        test(code, "5810true");
    }

}
