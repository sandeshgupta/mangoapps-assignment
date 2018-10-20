package com.syncApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.syncApp.properties.AppProperties;
import com.syncApp.util.Print;

public class Server {

	public static void startServer(String args[]) {
		ServerSocket servsock = null;
		Socket socket = null;
		try {
			servsock = new ServerSocket(AppProperties.LISTEN_PORT);
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

	public static void terminateApplication(int failureCode, String message) {
		Print.print(message);
		System.exit(failureCode);
	}

}