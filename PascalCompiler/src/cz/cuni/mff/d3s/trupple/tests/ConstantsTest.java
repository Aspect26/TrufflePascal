package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class ConstantsTest extends JUnitTest {

	@Test
	public void simple() {
		String code="const i=42;\n"+
				"const d=12.5;\n"+
				"const c=\'a\';\n"+
				"const b=TrUe;\n"+
				"\n"+
				"begin\n"+
				" write(i,d,b,c);\n"+
				"end.";
		String output = "4212.5truea";
		this.test(code, output);
	}
}
