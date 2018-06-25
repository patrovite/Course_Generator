package course_generator.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import course_generator.utils.Utils;

/**
 * Tests for the {@link Utils} class.
 */
public class UtilsTests {

	/**
	 * Testing the Utils functions
	 */
	@Test
	public void testEquals() {

		// {@see course_generator.utils.Utils#PaceToSpeed(String)}
		double expectedSpeed = 5.7; // mph
		double actualSpeed = Utils.PaceToSpeed("10:30");
		assertEquals(expectedSpeed, actualSpeed, 0.1);
	}

}