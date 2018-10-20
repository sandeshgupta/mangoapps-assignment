package com.syncApp.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.syncApp.util.Print;

public class AppProperties {
	public static int NO_OF_FILE_RETRY_ATTEMPT;
	public static String SERVER_IP;
	public static int SERVER_PORT;
	public static String SOURCE_DIRECTORY;
	public static String DEVICE_ID;

	public static void loadProperty(String path) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			path = path+"\\ApplicationConfig.properties";
			System.out.println(path);
			input = new FileInputStream(path);
			prop.load(input);

			NO_OF_FILE_RETRY_ATTEMPT = Integer.parseInt(prop.get("NO_OF_FILE_RETRY_ATTEMPT").toString());
			SERVER_IP = prop.getProperty("SERVER_IP");
			SERVER_PORT = Integer.parseInt(prop.getProperty("SERVER_PORT").toString());
			//SOURCE_DIRECTORY = prop.getProperty("SOURCE_DIRECTORY");
			DEVICE_ID = prop.getProperty("DEVICE_ID");

		} catch (Exception e) {
			e.printStackTrace();
			Print.print("Failed to load property file ApplicationConfig.properties");
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
