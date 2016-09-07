package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class ArrayTest extends JUnitTest {
	
	@Test
	public void verySimple(){
		String code = "var a:array[0..5] of integer;" + 
				"begin " + 
				" write(a[3]); " + 
				"end.";
		
		test(code, "0");
	}
	
	@Test
	public void simple(){
		String code = "var a:array[8..20] of integer;" + 
				"begin" + 
				" write(a[16]);\n" + 
				"end.";
		
		test(code, "0");
	}
	
	@Test
	public void simpleBoolean(){
		String code = "var a:array[boolean] of integer;" + 
				"begin" + 
				" write(a[true]);" +
				" write(a[false]); " +
				"end.";
		
		test(code, "00");
	}
	
	@Test
	public void simpleReturnBoolean(){
		String code = "var a:array[boolean] of boolean;" + 
				"begin" + 
				" write(a[true]);" +
				" write(a[false]); " +
				"end.";
		
		test(code, "falsefalse");
	}
	
	@Test
	public void simpleEnumAccess(){
		String code = 
				"type e = (r,g,b);" +
				"var a:array[e] of boolean;" + 
				"begin" + 
				" write(a[r]);" +
				" write(a[b]); " +
				"end.";
		
		test(code, "falsefalse");
	}
	
	@Test
	public void assignmentTest(){
		String code =
				"type e = (red,green,blue);"+
				"var a:array[e] of integer;"+

				"begin"+
				" a[blue] := 5;"+
				" a[green] := 8;"+
				" write(a[blue]);"+
				" write(a[green]);"+
				"end.";
		
		test(code, "58");
	}
	
	@Test
	public void simpleMultidimensionalTest(){
		String s="var multi: array[1..5,char,boolean,8..15] of integer;\n"+
				"\n"+
				"begin\n"+
				" multi[1,\'3\',true,8] := 326545;\n"+
				" writeln(multi[1,\'3\',true,8]);\n"+
				"end.";
		String result = "326545";
		
		test(s, result);
	}
}
