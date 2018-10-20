package com.syncApp.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.syncApp.util.Print;

public class FileProperties {
	public static Set<String> FILES_NOT_ALLOWED;
	public static long MAX_FILE_SIZE_ALLOWED;

	public static void loadProperty(String path)  {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			path = path + "\\FileConfig.properties";
			System.out.println(path);
			input = new FileInputStream(path);
			prop.load(input);

			MAX_FILE_SIZE_ALLOWED = Long.parseLong(prop.get("MAX_FILE_SIZE_ALLOWED").toString());
			FILES_NOT_ALLOWED = new HashSet<>(Arrays.asList(prop.getProperty("FILES_NOT_ALLOWED").split(",")));

		} catch (Exception e) {
			e.printStackTrace();
			Print.print("Failed to load property file FileConfig.properties");
		} finally {
			try {
				if (input != null)
					input.close();
				if (prop != null)
					prop.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
