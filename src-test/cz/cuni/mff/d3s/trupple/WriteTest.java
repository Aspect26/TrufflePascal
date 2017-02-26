package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class WriteTest extends JUnitTest {

	@Test
	public void simple() {
		this.test("program main; begin write('Hello World!'); end.", "Hello World!");
	}

	@Test
	public void integerVariable() {
		this.test("program main; var a:integer; begin a:=42; write(a); end.", "42");
	}

	@Test
	public void charVariable() {
		this.test("program main; var c:char; begin c:='P'; write(c); end.", "P");
	}

	@Test
	public void multipleArguments() {
		this.test("program main; var c:char; a:integer; begin c:='P'; a:=42; write(a,c); end.", "42P");
	}

	@Test
	public void writeln() {
		String text = "Metal in blood, rock in heart";
		this.test("program main; begin writeln('" + text + "'); end.", text + "\n");
	}
}
