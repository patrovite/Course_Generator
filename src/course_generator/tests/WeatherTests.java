package course_generator.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.Utils;
import course_generator.weather.JPanelWeather;

/**
 * Tests for the NOAA Weather feature.
 */
public class WeatherTests {

	public static final String IMPORT_FILE_PATH = "/src/course_generator/tests/files/"; //$NON-NLS-1$


	@Test
	public void testEquals() {

		// Load the GPX file

		String tontoGpxfilePath = Utils.GetAppDir() + IMPORT_FILE_PATH + "Move_2019_03_20_08_15_39_Trail+running.gpx"; //$NON-NLS-1$
		String tontoControlfilePath = Utils.GetAppDir() + IMPORT_FILE_PATH + "Tonto.cgx"; //$NON-NLS-1$
		String tontoTestfilePath = Utils.GetAppDir() + IMPORT_FILE_PATH + "TestGenerated-Tonto.cgx"; //$NON-NLS-1$

		String zionGpxfilePath = Utils.GetAppDir() + IMPORT_FILE_PATH + "Zion.gpx"; //$NON-NLS-1$
		String zionControlfilePath = Utils.GetAppDir() + IMPORT_FILE_PATH + "Zion.cgx"; //$NON-NLS-1$
		String zionTestfilePath = Utils.GetAppDir() + IMPORT_FILE_PATH + "TestGenerated-Zion.cgx"; //$NON-NLS-1$

		Assert.assertTrue(VerifyWeather(tontoGpxfilePath, tontoControlfilePath, tontoTestfilePath));

		Assert.assertTrue(VerifyWeather(zionGpxfilePath, zionControlfilePath, zionTestfilePath));
	}


	private boolean VerifyWeather(String testFilePath, String controlFilePath, String testGeneratedFilePath) {
		CgSettings settings = new CgSettings();
		TrackData trackData = new TrackData(settings);
		try {
			trackData.OpenGPX(testFilePath, 0, 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Retrieve the weather data

		JPanelWeather panelWeather = new JPanelWeather(settings, null);
		panelWeather.refresh(trackData, true);

		// We wait that weather retrieval is done
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		trackData.StartTime = new DateTime("2020-01-01T13:00:00Z");
		trackData.SaveCGX(testGeneratedFilePath, 0, trackData.data.size() - 1, false);

		// Compare with the control file
		return CompareAgainstControl(controlFilePath, testGeneratedFilePath);
	}


	/**
	 * Compares a test file against a control file.
	 * 
	 * @param controlDocument
	 *            The control CGX file's content.
	 * @param xmlTestDocument
	 *            The test CGX file's content.
	 * @return True if no differences were found, false otherwise.
	 */
	private static boolean CompareAgainstControl(String controlDocumentfilePath, String testDocumentFilePath) {

		StringBuilder controlDocumentContent = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(controlDocumentfilePath))) {
			stream.forEach(s -> controlDocumentContent.append(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder testDocumentContent = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(testDocumentFilePath))) {
			stream.forEach(s -> testDocumentContent.append(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Diff myDiff = DiffBuilder.compare(Input.fromString(controlDocumentContent.toString()))
				.withTest(Input.fromString(testDocumentContent.toString()))
				.ignoreWhitespace()
				.withNodeFilter(node -> !node.getNodeName().equals("MOONPHASE"))	
				.build();

		return !myDiff.hasDifferences();
	}
}