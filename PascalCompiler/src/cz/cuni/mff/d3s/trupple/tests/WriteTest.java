package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class WriteTest extends UnitTest {

	@Test
	public void simple() {
		this.test("begin write('Hello World!'); end.", "SimpleWriteTest", "Hello World!");
	}

	@Test
	public void integerVariable() {
		this.test("var a:integer; begin a:=42; write(a); end.", "IntegerWriteTest", "42");
	}

	@Test
	public void charVariable() {
		this.test("var c:char; begin c:='P'; write(c); end.", "CharWriteTest", "P");
	}

	@Test
	public void multipleArguments() {
		this.test("var c:char; a:integer; begin c:='P'; a:=42; write(a,c); end.", "42P");
	}

	@Test
	public void writeln() {
		String text = "Metal in blood, rock in heart";
		this.test("begin writeln('" + text + "'); end.", text + "\n");
	}
}