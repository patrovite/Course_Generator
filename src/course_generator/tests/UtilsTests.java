package course_generator.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
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

		// {@see course_generator.utils.Utils#SpeedCurrentUnitsToMeters(double,
		// CgSettings)}
		expectedSpeed = 9.656064; // 9.656064 km/h
		CgSettings settings = new CgSettings();
		settings.Unit = CgConst.UNIT_MILES_FEET;
		settings.isPace = true;
		actualSpeed = Utils.SpeedCurrentUnitsToMeters(10.0, settings); // 10 min/mile
		assertEquals(expectedSpeed, actualSpeed, 0.2);

		expectedSpeed = 6.2; // 6.2 min/mile
		settings.Unit = CgConst.UNIT_MILES_FEET;
		settings.isPace = true;
		actualSpeed = Utils.SpeedMeterToCurrentUnits(16, settings); // 16 km/h
		assertEquals(expectedSpeed, actualSpeed, 0.2);

		expectedSpeed = 12.0; // 12 km/h
		settings.Unit = CgConst.UNIT_METER;
		settings.isPace = false;
		actualSpeed = Utils.SpeedMeterToCurrentUnits(12, settings); // 12 km/h
		assertEquals(expectedSpeed, actualSpeed, 0.2);

		expectedSpeed = 4.36; // 4 min 36 sec / km
		settings.Unit = CgConst.UNIT_METER;
		settings.isPace = true;
		actualSpeed = Utils.SpeedMeterToCurrentUnits(13, settings); // 13 km/h
		assertEquals(expectedSpeed, actualSpeed, 0.2);
	}

}