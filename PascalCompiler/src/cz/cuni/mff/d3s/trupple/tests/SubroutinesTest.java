package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class SubroutinesTest extends JUnitTest {

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
	public void simpleArgumentsTest() {
		String code="procedure w(a:integer);\n"+
				"begin\n"+
				" a:=a+1;\n"+
				" write(a);\n"+
				"end;\n"+
				"\n"+
				"var b:integer;\n"+
				"begin\n"+
				" b:=5;\n"+
				" w(b);\n"+
				" write(b);\n"+
				"end.";
		String output="65";
		this.test(code, output);
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
	public void procedureCallNoParentheses() {
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
	public void functionCallNoParentheses() {
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
	
	@Test
	public void forwardTest() {
		String code="procedure b; forward;\n"+
				"\n"+
				"procedure a;\n"+
				" begin\n"+
				" b;\n"+
				" end;\n"+
				"\n"+
				"procedure b;\n"+
				" begin;\n"+
				" write(\'You are not prepared!\');\n"+
				" end;\n"+
				"\n"+
				"begin\n"+
				" a();\n"+
				"end.";
		String output = "You are not prepared!";
		this.test(code, output);
	}
}
