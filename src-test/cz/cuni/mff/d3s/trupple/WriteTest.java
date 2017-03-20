package cz.cuni.mff.d3s.trupple;

import org.junit.Test;

public class WriteTest extends JUnitTest {

	@Test
	public void simpleTest() {
		this.test("program main; begin write('Hello World!'); end.", "Hello World!");
	}

	@Test
	public void integerVariableTest() {
		this.test("program main; var a:integer; begin a:=42; write(a); end.", "42");
	}

	@Test
	public void charVariableTest() {
		this.test("program main; var c:char; begin c:='P'; write(c); end.", "P");
	}

	@Test
	public void multipleArgumentsTest() {
		this.test("program main; var c:char; a:integer; begin c:='P'; a:=42; write(a,c); end.", "42P");
	}

	@Test
	public void writelnTest() {
		String text = "Metal in blood, rock in heart";
		this.test("program main; begin writeln('" + text + "'); end.", text + "\n");
	}
}
