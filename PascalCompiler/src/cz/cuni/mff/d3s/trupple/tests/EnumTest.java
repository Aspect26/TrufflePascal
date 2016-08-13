package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class EnumTest extends UnitTest {

	@Test
	public void simpleEnum() {
		String code = 
				"type Color = (red,green,blue);"+
				"var c:Color;"+
				"begin"+
				"c:=red;"+
				"end.";

		String output = "";
		test(code, output);
	}
	
	@Test
	public void assignEnum() {
		String code = 
				"type Color = (red,green,blue);"+
				"var b,c:Color;"+
				"begin"+
				"c:=red;"+
				"b:=c"+
				"end.";

		String output = "";
		test(code, output);
	}
}