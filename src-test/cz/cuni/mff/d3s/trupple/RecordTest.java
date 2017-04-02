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

    @Test
    public void withStatementTest(){
        String code = "program main;\n"+
                "\n"+
                "type r = record\n"+
                " i,j: integer;\n"+
                " end;\n"+
                "\n"+
                "var rinstance: r;\n"+
                "\n"+
                "begin\n"+
                " with rinstance do begin\n"+
                " i := 8;\n"+
                " j := 42;\n"+
                " write(i);\n"+
                " write(j);\n"+
                " end;\n"+
                "\n"+
                " write(rinstance.i);\n"+
                " write(rinstance.j);\n"+
                "end.";

        test(code, "842842");
    }

    @Test
    public void innerWithStatementTest(){
        String code = "program main;\n"+
                "\n"+
                "type inner = record\n"+
                " c: char;\n"+
                " end;\n"+
                "\n"+
                " r = record\n"+
                " j: integer;\n"+
                " i: inner;\n"+
                " end;\n"+
                "\n"+
                "var rinstance: r;\n"+
                "\n"+
                "begin\n"+
                " with rinstance do begin\n"+
                " j := 42;\n"+
                " with i do begin\n"+
                " c := \'J\';\n"+
                " write(c);\n"+
                " end;\n"+
                " write(j);\n"+
                " write(i.c);\n"+
                " end;\n"+
                "\n"+
                " write(rinstance.j);\n"+
                " write(rinstance.i.c);\n"+
                "end.";

        test(code, "J42J42J");
    }

    @Test
    public void withStatementWithIdentifiersListTest(){
        String code = "program main;\n"+
                "\n"+
                "type inner = record\n"+
                " c: char;\n"+
                " end;\n"+
                "\n"+
                " r = record\n"+
                " i: inner;\n"+
                " end;\n"+
                "\n"+
                "var rinstance: r;\n"+
                "\n"+
                "begin\n"+
                " with rinstance, i do begin\n"+
                " c := \'J\';\n"+
                " write(c);\n"+
                " end;\n"+
                "\n"+
                " write(rinstance.i.c);\n"+
                "end.\n"+
                "\n";

        test(code, "JJ");
    }
}
