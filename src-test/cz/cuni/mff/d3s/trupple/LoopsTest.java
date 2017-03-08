package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class LoopsTest extends JUnitTest{
	@Test
	public void forToTest() {
		String code = "program main; \n"+
				"var i:integer;\n" +
				"\n" + 
				"begin\n" + 
				" for i:=5 to 10 do\n" +
				" begin\n" + 
				"  write(i);\n" +
				" end;\n" + 
				"\n" +
				"end.";
		
		test(code, "5678910");
	}
	
	@Test
	public void forDowntoTest() {
		String code = "program main; \n"+
				"var c:char;\n" +
				"\n" + 
				"begin\n" + 
				" for c:='g' downto 'c' do\n" +
				" begin\n" + 
				"  write(c);\n" +
				" end;\n" + 
				"\n" + 
				"end.";
		
		test(code, "gfedc");
	}

	@Test
	public void forEnumTest() {
		String code = "program main;\n"+
				"type day = (null, mon, tue, wed, thu, fri, sat, sun, unusedValue, unusedValue2);\n"+
				"var goodDays: set of day;\n"+
				" iterator: day;\n"+
				"\n"+
				"begin\n"+
				" goodDays := [wed, fri, sat, sun];\n"+
				"\n"+
				" for iterator := mon to sun do begin\n"+
				" write(iterator in goodDays, ', ');\n"+
				" end;\n"+
				"end.";

		test(code, "false, false, true, false, true, true, true, ");
	}
	
	@Test
	public void whileTest(){
		String code ="program main; \n"+
				"var j:integer;\n" +
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
		String code ="program main; \n"+
				"var j:integer;\n" +
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
	public void whileBreakTestTP(){
		String code = "program main; \n"+
				"var j:integer;\n" +
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
		
		test(code, "18", true);
	}
}
