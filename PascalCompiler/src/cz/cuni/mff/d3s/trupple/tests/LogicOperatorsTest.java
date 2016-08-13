package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class LogicOperatorsTest extends UnitTest {

	@Test
	public void greaterThanTest() {
		String code;
		code = "begin write(2>3); end.";
		test(code, "false");

		code = "begin write(3>2); end.";
		test(code, "true");

		code = "begin write(3>3); end.";
		test(code, "false");
	}

	@Test
	public void greaterEqualsThanTest() {
		String code;
		code = "begin write(2>=3); end.";
		test(code, "false");

		code = "begin write(3>=2); end.";
		test(code, "true");

		code = "begin write(3>=3); end.";
		test(code, "true");
	}

	@Test
	public void lesserEqualsThanTest() {
		String code;
		code = "begin write(2<=3); end.";
		test(code, "true");

		code = "begin write(3<=2); end.";
		test(code, "false");

		code = "begin write(3<=3); end.";
		test(code, "true");
	}

	@Test
	public void lesserThanTest() {
		String code;
		code = "begin write(2<3); end.";
		test(code, "true");

		code = "begin write(3<2); end.";
		test(code, "false");

		code = "begin write(3<3); end.";
		test(code, "false");
	}

	@Test
	public void andTest() {
		String code;
		code = "begin write(true and true); end.";
		test(code, "true");

		code = "begin write(false and true); end.";
		test(code, "false");
	}

	@Test
	public void orTest() {
		String code;
		code = "begin write(true or true); end.";
		test(code, "true");

		code = "begin write(true or false); end.";
		test(code, "true");

		code = "begin write(false or false); end.";
		test(code, "false");
	}
}