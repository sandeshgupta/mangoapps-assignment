package com.syncApp;

import java.io.File;
import java.net.Socket;

import com.syncApp.file.FileProcessor;
import com.syncApp.file.FileUtil;
import com.syncApp.lock.LockManager;
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

			if (request != null && request.getCommand() != null) {

				if (request.getCommand().equals("GET_CHECKSUM")) {
					String filename = AppProperties.SOURCE_DIRECTORY + "\\" + request.getUserId() + "\\"
							+ (String) request.getRequestData();

					File file = new File(filename);
					String hash = FileProcessor.getCheckSumOfFile(file);
					response.setCommand("PROVIDE_CHECKSUM");
					response.setData(hash);

					socketUtil.sendToClient(response);
				}

				if (request.getCommand().equals("GET_FILE_LOCK")) {
					String filename = AppProperties.SOURCE_DIRECTORY + "\\" + request.getUserId() + "\\"
							+ (String) request.getRequestData();
					LockManager lockManager = LockManager.getInstance();
					boolean locked = lockManager.applyLock(filename, request.getUserId());
					response.setData(locked);
					response.setCommand("FILE_LOCK_OPERATION");
					socketUtil.sendToClient(response);
				}

				if (request.getCommand().equals("SEND_FILE")) {
					FileData filedata = (FileData) request.getRequestData();
					String outputFile = AppProperties.SOURCE_DIRECTORY + "\\" + request.getUserId() + "\\"
							+ filedata.getDirectory();
					try {
						// create temp file on local

						File tempFile = new File(outputFile + ".tmp");

						if (FileUtil.createFile(tempFile, filedata)) {
							// Delete old file
							FileUtil.deleteFile(new File(outputFile));

							// Rename temp file
							FileUtil.renameFile(tempFile, new File(outputFile));

							// send files to other devices of same client
							FileProcessor.sendFileToAllClients(request, outputFile, socket);

						} else {
							Print.print("Could not create file " + tempFile);
						}

					} catch (Exception e) {
						// Delete file tmp
						e.printStackTrace();
						// send error message to uploader
						response.setResCode(-1);
					} finally {
						// release lock
						LockManager lockManager = LockManager.getInstance();
						lockManager.releaseLock(outputFile);
					}

					response.setResCode(1);
					socketUtil.sendToClient(response);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Print.print("Something went wrong");
		}
	}

}
