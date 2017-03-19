package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class PointersTest extends JUnitTest {

    @Test
    public void simpleAssignmentAndDereferenceTest() {
        String code = "program main;\n"+
                "\n"+
                "var pint: ^integer;\n"+
                "\n"+
                "begin\n"+
                " new(pint);\n"+
                " pint^ := 5;\n"+
                " write(pint^);\n"+
                "end.";

        test(code, "5");
    }

    @Test
    public void simplePointerAssignmentAndDereferenceTest() {
        String code = "program main;\n"+
                "\n"+
                "var pint, pint2: ^integer;\n"+
                "\n"+
                "begin\n"+
                " new(pint);\n"+
                " pint^ := 5;\n"+
                " pint2 := pint;\n"+
                " write(pint2^);\n"+
                "end.";

        test(code, "5");
    }

    @Test
    public void PointerAssignmentInSubroutineTest() {
        String code = "program main;\n"+
                "\n"+
                "type pinteger = ^integer;\n"+
                "\n"+
                "var pint: pinteger;\n"+
                "\n"+
                "procedure p(pointer: pinteger);\n"+
                "begin\n"+
                " pointer^ := 8;\n"+
                "end;\n"+
                "\n"+
                "begin\n"+
                " new(pint);\n"+
                " pint^ := 5;\n"+
                " p(pint);\n"+
                " write(pint^);\n"+
                "end.";

        test(code, "8");
    }

    @Test
    public void MultipleDereferenceTest() {
        String code = "program main;\n"+
                "\n"+
                "type pchar = ^char;\n"+
                " ppchar = ^pchar;\n"+
                " pppchar = ^ppchar;\n"+
                "\n"+
                "var p: pppchar;\n"+
                "\n"+
                "begin\n"+
                " new(p);\n"+
                " new(p^);\n"+
                " new(p^^);\n"+
                " p^^^:=\'h\';\n"+
                " write(p^^^);\n"+
                " dispose(p^^);\n"+
                " dispose(p^);\n"+
                " dispose(p);\n"+
                "end.";

        test(code, "h");
    }

    @Test
    public void withArraysAndRecordTest() {
        String code = "program main;\n"+
                "\n"+
                "type pint = ^integer;\n"+
                "\n"+
                " pArrWrapper = record\n"+
                " data: array[1..20] of pint;\n"+
                " end;\n"+
                "\n"+
                "var a: array[680..695] of pArrWrapper;\n"+
                " i,j: integer;\n"+
                "\n"+
                "begin\n"+
                " for i:=680 to 695 do begin\n"+
                " for j:=1 to 20 do begin\n"+
                " new(a[i].data[j]);\n"+
                " a[i].data[j]^ := i*j mod 10;\n"+
                " end;\n"+
                " end;\n"+
                "\n"+
                " for i:=680 to 695 do begin\n"+
                " for j:=1 to 20 do begin\n"+
                " write(a[i].data[j]^);\n"+
                " dispose(a[i].data[j]);\n"+
                " end;\n"+
                " end;\n"+
                "end.";

        test(code, "00000000000000000000123456789012345678902468024680246802468036925814703692581470482604826048260482605050505050505050505062840628406284062840741852963074185296308642086420864208642098765432109876543210000000000000000000001234567890123456789024680246802468024680369258147036925814704826048260482604826050505050505050505050");
    }

}
