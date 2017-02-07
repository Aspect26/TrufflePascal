package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class GlobalVariablesTest extends JUnitTest {
	
	@Test
	public void simple() {
		String code="program main;\n"+
			"var b:boolean;\n"+
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
	
	@Test
	public void changeGlobalVariableInProcedure() {
		String code="program main; \n"+
				"var i:integer;\n"+
				"\n"+
				"procedure change;\n"+
				" begin\n"+
				" i:=4653;\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" i:=26;\n"+
				" write(i);\n"+
				" change;\n"+
				" write(i);\n"+
				"end.";
		String output = "264653";
		this.test(code, output);
	}
	
	@Test
	public void localHidesGlobal() {
		String code="program main; \n"+
				"var i:integer;\n"+
				"\n"+
				"procedure dontchange;\n"+
				" var i:integer;\n"+
				" begin\n"+
				" i:=4653;\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" i:=26;\n"+
				" write(i);\n"+
				" dontchange;\n"+
				" write(i);\n"+
				"end.";
		String output = "2626";
		this.test(code, output);
	}
	
	@Test
	public void nestedFunctions() {
		String code="program main; \n"+
				"procedure A;\n"+
				" var i:integer;\n"+
				"\n"+
				" procedure B;\n"+
				" begin\n"+
				" i:=65;\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" i:=4653;\n"+
				" write(i);\n"+
				" B;\n"+
				" write(i);\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" A;\n"+
				"end.";
		String output="465365";
		this.test(code, output);
	}
	
	@Test 
	public void nestedWithLocalHidesGlobal() {
		String code="program main; \n"+
				"var i:integer;\n"+
				"\n"+
				"procedure A;\n"+
				" var i:integer;\n"+
				"\n"+
				" procedure B;\n"+
				" begin\n"+
				" i:=65;\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" i:=4653;\n"+
				" write(i);\n"+
				" B;\n"+
				" write(i);\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" i:=-23;\n"+
				" write(i);\n"+
				" A;\n"+
				" write(i);\n"+
				"end.";
		String output = "-23465365-23";
		this.test(code, output);
	}
}
