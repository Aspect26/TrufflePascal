package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class LogicOperatorsTest extends JUnitTest {

	@Test
	public void greaterThanTest() {
		String code;
		code = "program main; begin write(2>3); end.";
		test(code, "false");

		code = "program main; begin write(3>2); end.";
		test(code, "true");

		code = "program main; begin write(3>3); end.";
		test(code, "false");
	}

	@Test
	public void greaterEqualsThanTest() {
		String code;
		code = "program main; begin write(2>=3); end.";
		test(code, "false");

		code = "program main; begin write(3>=2); end.";
		test(code, "true");

		code = "program main; begin write(3>=3); end.";
		test(code, "true");
	}

	@Test
	public void lesserEqualsThanTest() {
		String code;
		code = "program main; begin write(2<=3); end.";
		test(code, "true");

		code = "program main; begin write(3<=2); end.";
		test(code, "false");

		code = "program main; begin write(3<=3); end.";
		test(code, "true");
	}

	@Test
	public void lesserThanTest() {
		String code;
		code = "program main; begin write(2<3); end.";
		test(code, "true");

		code = "program main; begin write(3<2); end.";
		test(code, "false");

		code = "program main; begin write(3<3); end.";
		test(code, "false");
	}

	@Test
	public void andTest() {
		String code;
		code = "program main; begin write(true and true); end.";
		test(code, "true");

		code = "program main; begin write(false and true); end.";
		test(code, "false");
	}

	@Test
	public void orTest() {
		String code;
		code = "program main; begin write(true or true); end.";
		test(code, "true");

		code = "program main; begin write(true or false); end.";
		test(code, "true");

		code = "program main; begin write(false or false); end.";
		test(code, "false");
	}
}
