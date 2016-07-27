package cz.cuni.mff.d3s.trupple.tests;

import java.io.IOException;

import org.junit.Test;

public class WriteTest extends UnitTest {

	@Test
	public void Simple() throws IOException {
		this.test("begin write('Hello World!'); end.", "SimpleWriteTest", "Hello World!");
	}
}
