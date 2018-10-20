package com.syncApp.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import com.syncApp.pojo.IPAddress;
import com.syncApp.util.Print;

public class ClientProperties {
	public static HashMap<String, ArrayList<IPAddress>> userDetails = new HashMap<>();

	public static void loadProperty(String path) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			path = path + "\\ClientDetails.properties";
			System.out.println(path);
			input = new FileInputStream(path);
			prop.load(input);

			ArrayList<IPAddress> temp = new ArrayList<>();
			for (String key : prop.stringPropertyNames()) {
				temp = new ArrayList<>();
				String devices[] = prop.getProperty(key).split(";");

				for (int i = 0; i < devices.length; i++) {
					IPAddress ip = new IPAddress();
					String[] clientData = devices[i].split(":");
					ip.setIp(clientData[0]);
					ip.setPort(Integer.parseInt(clientData[1]));
					ip.setUser(key);
					ip.setDeviceId(clientData[2]);
					temp.add(ip);
				}

				userDetails.put(key, temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Print.print("Failed to load property file ClientDetails.properties");
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
