package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import course_generator.utils.Utils;

/**
 * Tests for the {@link Utils} class.
 */
public class UtilsTests {

	/**
	 * 
	 */
	@Test
	public void testEquals() {
		double expectedSpeed = 5.7; // mph
		double actualSpeed = Utils.PaceToSpeed("10:30");
		assertEquals(expectedSpeed, actualSpeed, 0.1);
	}

}