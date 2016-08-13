package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class SubroutinesTest extends UnitTest {

	@Test
	public void simpleProcedureTest(){
		String code = "procedure Hello;\n" + 
				"begin\n" + 
				" write('Hello World');\n" + 
				"end;\n" + 
				"\n" + 
				"begin\n" + 
				" Hello();\n" + 
				"end.";
		
		test(code, "Hello World");
	}
	
	@Test
	public void simpleFunctionTest(){
		String code ="function simple:integer;\n" + 
				"begin\n" + 
				" simple:=720;\n" + 
				"end;\n" + 
				"\n" + 
				"begin\n" + 
				" write(simple());\n" + 
				"end.";
		
		test(code, "720");
	}
	
	@Test
	public void factorialTest(){
		String code = "function factorial(n:integer):integer;\n" + 
				"begin\n" + 
				" if n<2 then factorial:=1\n" + 
				" else factorial:=n*factorial(n-1);\n" + 
				"end;\n" + 
				"\n" + 
				"begin\n" + 
				" write(factorial(8));\n" + 
				"end.";
		
		test(code, "40320");
	}
	
	@Test
	public void procedureCallNoParentheses(){
		String code = "procedure p;\n" + 
				"begin\n" + 
				"write('Metallica!');" + 
				"end;\n" + 
				"\n" + 
				"begin\n" + 
				" p;\n" + 
				"end.";
		
		test(code, "Metallica!");
	}
	
	@Test
	public void functionCallNoParentheses(){
		String code = "function f:integer;\n" + 
				"begin\n" + 
				"f:=26270;" + 
				"end;\n" + 
				"\n" + 
				"begin\n" + 
				"write(f);\n" + 
				"end.";
		
		test(code, "26270");
	}
	
	// TODO
	/*
	@Test
	public void globalVarTest(){
		String code="var j:integer;\n" + 
				"\n" + 
				"procedure incJ;\n" + 
				"begin\n" + 
				" j:=j+5;\n" + 
				"end;\n" + 
				"\n" + 
				"begin\n" + 
				" j:=7;\n" + 
				" incJ();\n" + 
				" write(j);\n" + 
				"end.";
		
		test(code, "12");
	}
	*/
}
