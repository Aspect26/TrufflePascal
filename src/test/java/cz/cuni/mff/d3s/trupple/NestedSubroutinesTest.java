package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class NestedSubroutinesTest extends JUnitTest {

	@Test
	public void simpleTest() {
		String code="program main;\n"+
				"procedure a;\n"+
				" var ab:boolean;\n"+
				"\n"+
				" procedure b;\n"+
				" var bb:integer;\n"+
				" begin\n"+
				" write(\'::b\');\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" write(\'::a\');\n"+
				" b;\n"+
				" end;\n"+
				"\n"+
				"begin \n"+
				" write(\'::main\');\n"+
				" a;\n"+
				"end.";
		String output = "::main::a::b";
		this.test(code, output);
	}
	
	@Test
	public void usingGlobalVariableTest() {
		String code="program main;\n"+
				"var b:boolean;\n"+
				"\n"+
				"procedure a;\n"+
				" procedure c;\n"+
				" begin\n"+
				" write(\'c::\');\n"+
				" b:=false;\n"+
				" a;\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" write(\'a::\');\n"+
				" if b then c;\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" b:=true;\n"+
				" write(\'main::\');\n"+
				" a;\n"+
				"end.";
		String output = "main::a::c::a::";
		this.test(code, output);
	}
	
	@Test
	public void simpleNestedFunctionsTest() {
		String s="program main; \n"+
				"function a:integer;\n"+
				"\n"+
				" function b:integer;\n"+
				"\n"+
				" function c:integer;\n"+
				" \n"+
				" function d:integer;\n"+
				" begin\n"+
				" d:=42;\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" c:=d;\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" b:=c;\n"+
				" end;\n"+
				"\n"+
				" begin\n"+
				" a:=b;\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" write(a);\n"+
				"end.";
		String output="42";
		this.test(s, output);
	}
}
