package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class IfTest extends JUnitTest {

	@Test
	public void ifTrueTest() {
		test("program main; begin if true then write(1); end.", "1");
	}

	@Test
	public void ifFalseTest() {
		test("program main; begin if false then write('lol'); end.", "");
	}

	@Test
	public void elseTest() {
		test("program main; begin if false then write(42) else write(41); end.", "41");
	}

	@Test
	public void ifTrueBlockTest() {
		test("program main; begin if true then begin write(1); write(2); end; end.", "12");
	}

	@Test
	public void elseBlockTest() {
		test("program main; begin if false then begin write(1); end else begin write(2); write(3); end; end.", "23");
	}
}
