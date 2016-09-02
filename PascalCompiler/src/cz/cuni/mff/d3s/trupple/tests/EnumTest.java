package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class EnumTest extends JUnitTest {

	@Test
	public void simpleEnum() {
		String code = 
				"type Color = (red,green,blue);"+
				"var c:Color;"+
				"begin "+
				"c:=red;"+
				"write('asd');"+
				"end.";

		String output = "asd";
		test(code, output);
	}
	
	@Test
	public void assignEnum() {
		String code = 
				"type Color = (red,green,blue);"+
				"var b,c:Color;"+
				"begin "+
				"c:=red;"+
				"b:=c;"+
				"write('asd');"+
				"end.";

		String output = "asd";
		test(code, output);
	}
	
	@Test
	public void switchEnum() {
		String code = 
				"type Color = (red,green,blue);"+
				"var b:Color;"+
				"begin "+
				"b:=green; "+
				"case b of "+
				" red: write('1651'); "+
				" green: write('Luke Skywalker'); "+
				" blue: write('onomatopoje'); "+
				"end;"+
				"end.";

		String output = "Luke Skywalker";
		test(code, output);
	}
}