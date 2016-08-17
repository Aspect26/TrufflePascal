package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class ArithmeticTest extends JUnitTest {

	@Test
	public void plusTest() {
		test("begin write(42+26); end.", "68");
	}

	@Test
	public void plusFloatTest() {
		test("begin write(2e2 + 3e3); end.", "3200.0");
	}

	@Test
	public void minusTest() {
		test("begin write(42-26); end.", "16");
	}

	@Test
	public void minusFloatTest() {
		test("begin write(3e3 - 200e-2); end.", "2998.0");
	}

	@Test
	public void multiplyTest() {
		test("begin write(165*32); end.", "5280");
	}

	@Test
	public void divisionTest() {
		test("begin write(64 div 8); end.", "8");
	}

	@Test
	public void divisionFloatTest() {
		test("begin write(64/5); end.", "12.8");
	}

	@Test
	public void moduloTest() {
		test("begin write(50 mod 3); end.", "2");
	}
}
