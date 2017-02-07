package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class LogicNegationTest extends JUnitTest {

	@Test
	public void simple() {
		String code;
		code = "program main; begin write(not true); end.";
		test(code, "false");

		code = "program main; begin write(not false); end.";
		test(code, "true");

		code = "program main; begin write(not not true); end.";
		test(code, "true");
	}

	@Test
	public void simple2() {
		String code;
		code = "program main; begin write(not 2>=3); end.";
		test(code, "true");

		code = "program main; begin write(not 3>=2); end.";
		test(code, "false");
	}
	
	@Test
	public void simple3() {
		String code;
		code = "program main; begin write(not true or not false); end.";
		test(code, "true");

		code = "program main; begin write(true and not false); end.";
		test(code, "true");
	}

	@Test
	public void withVariables() {
		String code;
		code = "program main; var b:boolean; begin b:=true; write(not b); end.";
		test(code, "false");

		code = "program main; var b:boolean; begin b:=true; write(not b or true); end.";
		test(code, "true");

		code = "program main; var b:boolean; begin b:=true; write(not not not not not b); end.";
		test(code, "false");
	}

	@Test
	public void withFunction() {
		String code;
		code = "program main; function b:boolean; begin b:=false; end; begin write(not b()); end.";
		test(code, "true");
	}
}
