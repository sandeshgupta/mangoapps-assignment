package com.syncApp;

import java.io.File;
import java.net.Socket;

import com.syncApp.file.FileUtil;
import com.syncApp.pojo.FileData;
import com.syncApp.pojo.Request;
import com.syncApp.pojo.Response;
import com.syncApp.properties.AppProperties;
import com.syncApp.util.Print;
import com.syncApp.util.SocketUtil;

public class ServerProcessor extends Thread {
	private Socket socket;

	public ServerProcessor(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {

		System.out.println("Accepted connection from client : " + socket);

		try {

			SocketUtil socketUtil = new SocketUtil(socket);
			Request request = socketUtil.readFromClient();
			Response response = new Response();

			if (request.getCommand().equals("SEND_FILE")) {
				FileData filedata = (FileData) request.getRequestData();
				String outputFile = AppProperties.SOURCE_DIRECTORY.replace("\\\\", "\\") + "\\" + filedata.getDirectory();
				try {
					// create temp file on local

					File tempFile = new File(outputFile + ".tmp");

					if (FileUtil.createFile(tempFile, filedata)) {
						// Delete old file
						FileUtil.deleteFile(new File(outputFile));

						// Rename temp file
						FileUtil.renameFile(tempFile, new File(outputFile));

						// send files to other devices of same client
						// FileProcessor.sendFileToAllClients(request, outputFile, socket);

						response.setResCode(1);
					} else {
						response.setResCode(-1);
						Print.print("Could not create file " + tempFile);
					}
					
				} catch (Exception e) {
					response.setResCode(-1);
					// Delete file tmp

					// send error message to uploader
				} finally {
					socketUtil.sendToClient(response);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Print.print("Something went wrong");
		}
	}

}
