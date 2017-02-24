package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class ConstantsTest extends JUnitTest {

	@Test
	public void simple() {
		String code="program main; \n"+
				"const i=42;\n"+
				"      d=12.5;\n"+
				"      c=\'a\';\n"+
				"      b=TrUe;\n"+
				"\n"+
				"begin\n"+
				" write(i,d,b,c);\n"+
				"end.";
		String output = "4212.5truea";
		this.test(code, output);
	}

	// NOTE: not supported in Wirth's standard
    /*
	@Test
	public void composedConstants() {
		String code="const ea=53;\n"+
				"const eb=5;\n"+
				"const ec=eb * eb * 0.0 + ea + 8 * 9 +8 *8 *8 +8 +8;\n"+
				"const APPLICATIONNAME=\'Super \' + \'massive \' + \'op \' + \'\' + \'compiler.\';\n"+
				"const b=true or false or not true and false or not not true;\n"+
				"\n"+
				"begin\n"+
				" write(APPLICATIONNAME,ec,b);\n"+
				"end.";
		String output = "Super massive op compiler.653.0true";
		this.test(code, output);
	}
	*/
}
