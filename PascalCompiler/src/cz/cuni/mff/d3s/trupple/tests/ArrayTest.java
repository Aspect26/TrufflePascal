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
				"var a:array[(r,g,b)] of boolean;" +
				"begin" + 
				" write(a[r]);" +
				" write(a[b]); " +
				"end.";
		
		test(code, "falsefalse");
	}
	
	@Test
	public void assignmentEnumTest(){
		String code =
				"var a:array[(red,green,blue)] of integer;"+

				"begin"+
				" a[blue] := 5;"+
				" a[green] := 8;"+
				" write(a[blue]);"+
				" write(a[green]);"+
				"end.";
		
		test(code, "58");
	}

	@Test
	public void verySimpleMultidimensionalTest() {
		String s="var multi: array[1..5,3..6,8..15] of integer;\n"+
				"\n"+
				"begin\n"+
				" multi[1,6,10] := 14122017;\n"+
				" write(multi[1,6,10]);\n"+
				"end.";
		String result = "14122017";

		test(s, result);
	}

	@Test
	public void simpleMultidimensionalTest(){
		String s="var multi: array[1..5,char,boolean,8..15] of integer;\n"+
				"\n"+
				"begin\n"+
				" multi[1,\'3\',true,8] := 326545;\n"+
				" write(multi[1,\'3\',true,8]);\n"+
				"end.";
		String result = "326545";
		
		test(s, result);
	}
	
	@Test
	public void lessSimpleMultidimensionalTest(){
		String s="var arr: array[1..3,10..15,0..6] of integer;\n"+
				"var i,j,k:integer;\n"+
				"\n"+
				"begin\n"+
				" for i:=1 to 3 do\n"+
				" for j:=10 to 15 do\n"+
				" for k:=6 downto 0 do\n"+
				" arr[i,j,k]:=i+j+k;\n"+
				"\n"+
				" for i:=1 to 3 do\n"+
				" for j:=10 to 15 do\n"+
				" for k:=6 downto 0 do\n"+
				" write(arr[i,j,k],\',\');\n"+
				"end.";
		String result = "17,16,15,14,13,12,11,18,17,16,15,14,13,12,19,18,17,16,15,14,13,20,19,18,17,16,15,14,21,20,19,18,17,16,15,22,21,20,19,18,17,16,18,17,16,15,14,13,12,19,18,17,16,15,14,13,20,19,18,17,16,15,14,21,20,19,18,17,16,15,22,21,20,19,18,17,16,23,22,21,20,19,18,17,19,18,17,16,15,14,13,20,19,18,17,16,15,14,21,20,19,18,17,16,15,22,21,20,19,18,17,16,23,22,21,20,19,18,17,24,23,22,21,20,19,18,";
		
		test(s, result);
	}
	
	@Test
	public void differentMultidimensionalArrayDefinitionsTest(){
		String s="var arr1:array [Boolean] of array [1..10] of array [1..3] of real;\n"+
				" arr2:array [Boolean] of array [1..10, 1..3] of real;\n"+
				" arr3:array [Boolean, 1..10, 1..3] of real;\n"+
				" arr4:array [Boolean, 1..10] of array [1..3] of real;\n"+
				"\n"+
				"begin\n"+
				" arr1[true, 8, 2] := 2.5;\n"+
				" arr2[true, 8, 2] := 2.5;\n"+
				" arr3[true, 8, 2] := 2.5;\n"+
				" arr4[true, 8, 2] := 2.5;\n"+
				"\n"+
				" write(arr1[true, 8, 2]);\n"+
				" write(arr2[true, 8, 2]);\n"+
				" write(arr3[true, 8, 2]);\n"+
				" write(arr4[true, 8, 2]);\n"+
				"end.";
		String result = "2.52.52.52.5";
		
		test(s, result);
	}
}
