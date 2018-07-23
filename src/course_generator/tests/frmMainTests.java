package course_generator.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import course_generator.frmMain;

/**
 * Tests for the {@link frmMain} class.
 */
public class frmMainTests {

	/**
	 * Testing the version comparison function
	 */
	@Test
	public void testVersionsComparison() {

		// {@see course_generator.frmMain#isRemoteVersionNewer(String)}
		boolean isRemoteVersionNewer = true;
		boolean actualResult = frmMain.isRemoteVersionNewer("5.0.0");
		assertEquals(isRemoteVersionNewer, actualResult);

		isRemoteVersionNewer = false;
		actualResult = frmMain.isRemoteVersionNewer("4.0.2");
		assertEquals(isRemoteVersionNewer, actualResult);

		isRemoteVersionNewer = false;
		actualResult = frmMain.isRemoteVersionNewer("4.1.0");
		assertEquals(isRemoteVersionNewer, actualResult);

		isRemoteVersionNewer = false;
		actualResult = frmMain.isRemoteVersionNewer("3.88");
		assertEquals(isRemoteVersionNewer, actualResult);
	}

}