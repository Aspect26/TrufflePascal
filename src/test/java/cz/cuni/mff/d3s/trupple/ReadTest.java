package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class ReadTest extends JUnitTest {
	
	@Test
	public void simpleReadIntegerTest() {
		String code="program main; var i:integer;\n"+
				"\n"+
				"begin\n"+
				" read(i);\n"+
				" write(i);\n"+
				"end.";
		String input = "15324";
		String output = "15324";
		this.testWithInput(code, input, output);
	}
	
	@Test
	public void correctReadBehaviourTest() {
		String code="program main; var i:integer;\n"+
				" c:char;\n"+
				"\n"+
				"begin\n"+
				" read(i,c);\n"+
				" write(i,c);\n"+
				"end.";
		String input = "    126 c     ";
		String output = "126 ";
		this.testWithInput(code, input, output);
	}

    @Test
    public void multipleReadOnOneLineTest() {
        String code="program main; var i,j:integer;\n"+
                "\n"+
                "begin\n"+
                " read(i,j);\n"+
                " write(i,',',j);\n"+
                "end.";
        String input = "    126   58   ";
        String output = "126,58";
        this.testWithInput(code, input, output);
    }

	@Test
	public void mutipleReadLnOnMultipleLinesTest() {
		String code="program main; var i,j,k:integer;\n"+
				" b:boolean;\n"+
				"\n"+
				"begin\n"+
				" readln(i,j);\n"+
				" readln(k);\n"+
				" write(i,',',j);\n"+
                " write(',',k);\n"+
				"end.";
		String input = String.format("   12    34   58     %n   65 %n");
		String output = "12,34,65";
		this.testWithInput(code, input, output);
	}
	
	@Test
	public void simpleCalculationBasedOnInputTest() {
		String code="program main; Var \n"+
				" Num1, Num2, Sum : Integer;\n"+
				"\n"+
				"Begin\n"+
				"\tReadln(Num1);\n"+
				"\tReadln(Num2);\n"+
				"\tSum := Num1 + Num2; {addition} \n"+
				"\tWrite(Sum);\n"+
				"\tReadln;\n"+
				"End. ";
		String input = String.format("36%n18%n%n");
		String output = "54";
		this.testWithInput(code, input, output);
	}

	@Test
	public void multipleLineEndingTypeTest() {
		String code = "program main;\n"+
				"\n"+
				"var i,j,k,l: integer;\n"+
				"\n"+
				"begin\n"+
				" readln(i,j,k,l);\n"+
				" writeln(i);\n"+
				" writeln(j);\n"+
				" writeln(k);\n"+
				" writeln(l);\n"+
				"end.";
		String input = String.format("315\r\n42\n26\r654%n");
		String output = String.format("315%n42%n26%n654%n");
		this.testWithInput(code, input, output);
	}
}
