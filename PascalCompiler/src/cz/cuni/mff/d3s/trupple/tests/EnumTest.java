package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class EnumTest extends JUnitTest {

	@Test
	public void simpleEnum() {
		String code = 
				"var c:(red,green,blue);"+
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
				"var b,c:(red,green,blue);"+
				"begin "+
				"c:=red;"+
				"b:=c;"+
				"write('asd');"+
				"end.";

		String output = "asd";
		test(code, output);
	}

	@Test
	public void ifEnumTest() {
		String code="var c1, c2:(red,green,blue,alpha);\n"+
				"\n"+
				"begin\n"+
				" c1 := red;\n"+
				" c2 := c1;\n"+
				" if c2 = red then\n"+
				" write(\'The power that we are dealing with here is immesurable!\');\n"+
				"end.\n"+
				"";

		String output = "The power that we are dealing with here is immesurable!";
		test(code, output);
	}

	@Test
	public void switchEnum() {
        String code="var b:(red,green,blue);\n"+
                "\n"+
                "begin\n"+
                " b:=green;\n"+
                " case b of\n"+
                " red: write(\'1651\');\n"+
                " green: write(\'Luke Skywalker\');\n"+
                " blue: write(\'onomatopoje\');\n"+
                " end;\n"+
                "end.";

		String output = "Luke Skywalker";
		test(code, output);
	}
}