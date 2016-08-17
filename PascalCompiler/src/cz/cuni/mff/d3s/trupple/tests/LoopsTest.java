package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class LoopsTest extends JUnitTest{
	@Test
	public void forToTest() {
		String code = "var i,j:integer;\n" + 
				"\n" + 
				"begin\n" + 
				" j:=0;\n" + 
				" for i:=5 to 10 do\n" + 
				" begin\n" + 
				"  j:=j+1;\n" + 
				" end;\n" + 
				"\n" + 
				" write(j);\n" + 
				"end.";
		
		test(code, "6");
	}
	
	@Test
	public void forDowntoTest() {
		String code = "var i,j:integer;\n" + 
				"\n" + 
				"begin\n" + 
				" j:=0;\n" + 
				" for i:=568 downto 527 do\n" + 
				" begin\n" + 
				"  j:=j+1;\n" + 
				" end;\n" + 
				"\n" + 
				" write(j);\n" + 
				"end.";
		
		test(code, "42");
	}
	
	@Test
	public void whileTest(){
		String code ="var j:integer;\n" + 
				"\n" + 
				"begin\n" + 
				" j:=0;\n" + 
				" \n" + 
				" while false do\n" + 
				"  j:=j+1;\n" + 
				"\n" + 
				" while j<20 do\n" + 
				" begin\n" + 
				"  j:=j+1;\n" + 
				" end;\n" + 
				"\n" + 
				" write(j);\n" + 
				"end.";
		
		test(code, "20");
	}
	
	@Test
	public void repeatTest(){
		String code ="var j:integer;\n" + 
				"\n" + 
				"begin\n" + 
				" j:=0;\n" + 
				" \n" + 
				" repeat\n" + 
				"  j:=j+1;\n" + 
				" until j=5;\n" + 
				"\n" + 
				" repeat\n" + 
				"  j:=j-1;\n" + 
				" until j=-25;\n" + 
				"\n" + 
				" write(j);\n" + 
				"end.";
		
		test(code, "-25");
	}
	
	@Test
	public void whileBreakTest(){
		String code = "var j:integer;\n" + 
				"\n" + 
				"begin\n" + 
				" j:=0;\n" + 
				" \n" + 
				" while true do\n" + 
				" begin\n" + 
				"  j:=j+1;\n" + 
				"  if j=18 then break;\n" + 
				" end;\n" + 
				"\n" + 
				" write(j);\n" + 
				"end.";
		
		test(code, "18");
	}
}
