/*
 * Course Generator
 * Copyright (C) 2016 Pierre Delore
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package course_generator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CgLog {
	private static PrintStream logfile = null;
	private static int _maxsize = 10 * 1024 * 1024; // 10Mb
	private static String _filename = "";
	private static String _oldfilename = "";
	private static boolean active = false;

	/**
	 * Create the logging engine. If the size of the log file is greater than the
	 * maximum then "old" file is deleted and the current is renamed to
	 * "filename".old
	 * 
	 * @param filename  Complete path and filename. Filename without extension
	 * @param maxsize   Maximum file size in bytes
	 * @param logToFile true if we want to send the log and the error to a file
	 */
	public CgLog(String filename, int maxsize, boolean logToFile) {
		_maxsize = maxsize;
		_filename = filename;
		_oldfilename = filename + ".old";
		active = logToFile;
		try {
			if (logToFile) {
				logfile = new PrintStream(new FileOutputStream(filename, true));
				System.setOut(logfile);
				System.setErr(logfile);
			}

		} catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		checkFileSize();
	}

	public static void info(String msg) {
		System.out.println(new SimpleDateFormat("yyyyMMdd-HH:mm:ss.S").format(new Date()) + ": (i) : " + msg);
	}

	public static void error(String msg) {
		System.out.println(new SimpleDateFormat("yyyyMMdd-HH:mm:ss.S").format(new Date()) + ": (!) : " + msg);
	}

	public static void checkFileSize() {
		if (active && logfile != null) {
			long l = new File(_filename).length();
			if (l > _maxsize) {
				try {
					// -- If the old file exist then delete it
					if (new File(_oldfilename).exists())
						new File(_filename + ".old").delete();
					// -- Flush and close the file
					logfile.flush();
					logfile.close();
					// -- Rename the file
					new File(_filename).renameTo(new File(_oldfilename));
					// -- Recreate the log file
					logfile = new PrintStream(new FileOutputStream(_filename, true));
					System.setOut(logfile);
					System.setErr(logfile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

}
