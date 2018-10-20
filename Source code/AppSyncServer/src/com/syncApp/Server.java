package com.syncApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.syncApp.properties.AppProperties;
import com.syncApp.properties.ClientProperties;
import com.syncApp.properties.FileProperties;
import com.syncApp.util.Print;

public class Server {

	public static void main(String args[]) {

		if (args.length != 2) {
			terminateApplication(1, "Please provide <filepath> and <filesystem_source_directory> as argument");
		}

		AppProperties.SOURCE_DIRECTORY = args[1];
		loadProperties(args[0]);

		ServerSocket servsock = null;
		Socket socket = null;
		try {
			servsock = new ServerSocket(AppProperties.SERVER_PORT);
			Print.print("Server started...");
			while (true) {

				socket = servsock.accept();
				ServerProcessor processor = new ServerProcessor(socket);
				processor.start();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (servsock != null)
					servsock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void loadProperties(String path) {
		AppProperties.loadProperty(path);
		ClientProperties.loadProperty(path);
		FileProperties.loadProperty(path);
	}

	public static void terminateApplication(int failureCode, String message) {
		Print.print(message);
		System.exit(failureCode);
	}

}