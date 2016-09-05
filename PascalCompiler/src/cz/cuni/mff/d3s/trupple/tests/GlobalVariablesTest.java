package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class GlobalVariablesTest extends JUnitTest {
	
	@Test
	public void simple() {
		String code="var b:boolean;\n"+
			"\n"+
			"procedure p;\n"+
			" begin \n"+
			" write(b);\n"+
			" end;\n"+
			"\n"+
			"begin\n"+
			" b:=true;\n"+
			" p;\n"+
			"end.";
		String output = "true";
		this.test(code, output);
	}
}
