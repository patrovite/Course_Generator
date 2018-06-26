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

		// {@see course_generator.utils.Utils#SpeedToPaceNumber(double)}
		expectedSpeed = 10.0; // 10 min/mile
		actualSpeed = Utils.SpeedToPaceNumber(6.0); // 6 mph
		assertEquals(expectedSpeed, actualSpeed, 0.1);

		expectedSpeed = 7.42; // 7min42sec/mile
		actualSpeed = Utils.SpeedToPaceNumber(7.7); // 7.7mph
		assertEquals(expectedSpeed, actualSpeed, 0.2);
	}

}