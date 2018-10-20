package com.syncApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.syncApp.file.FileProcessor;
import com.syncApp.properties.AppProperties;
import com.syncApp.properties.FileProperties;
import com.syncApp.util.PasswordUtility;
import com.syncApp.util.Print;

public class Client {

	public static void main(String args[]) {
		try {

			if (args.length != 4) {
				terminateApplication(1, "Please provide <username>, <password>, <property-path> and <filesystem_source_directory> as arguments");
			}

			AppProperties.SOURCE_DIRECTORY = args[3];
			loadProperties(args[2]);
			
			boolean isUserValid = PasswordUtility.checkUserValid(args[0], args[1]);

			if (isUserValid) {
				AppProperties.USER_ID = args[0];

				// check for hash of the file stored in users directory

				// Sync the files from server to client
			} else {
				terminateApplication(1, "Incorrect username/password");
			}

			// Start a server to listen for files
			Thread t1 = new Thread(new Runnable() {

				@Override
				public void run() {
					Server.startServer(args);
				}
			});
			t1.start();

			Print.print("Welcome to AppSync");
			
			// Sync all files from server
			Print.print("Syncing files from server..");
			//FileProcessor.syncAllFilesFromServer();
			Thread.sleep(500);
			Print.print("Sync completed!");

			// Ask user to upload file

			while (true) {
				Print.print("Choose one of the following options:");
				Print.print("1. Upload file");
				Print.print("2. Exit");

				BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
				try {
					Integer userInput = Integer.parseInt(buff.readLine());

					switch (userInput) {
					case 1:
						FileProcessor.optionUploadFile();
						break;
					case 2:
						terminateApplication(1, "Exited successfully");
						break;
					default:
						Print.print("Please enter valid option");
						break;
					}
				} catch (NumberFormatException e) {
					Print.print("Please enter valid option");
				}

			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private static void loadProperties(String path) {
		AppProperties.loadProperty(path);
		FileProperties.loadProperty(path);
	}


	public static void terminateApplication(int failureCode, String message) {
		Print.print(message);
		System.exit(failureCode);
	}

}