package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class ArrayTest extends UnitTest {
	
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
}
