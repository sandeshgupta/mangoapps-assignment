package com.syncApp.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.syncApp.pojo.FileData;
import com.syncApp.pojo.Request;
import com.syncApp.pojo.Response;
import com.syncApp.properties.AppProperties;
import com.syncApp.util.LockUtil;
import com.syncApp.util.SocketUtil;

public class FileTransfer {
	private Socket socket = null;

	public int sendFile(File file) {

		// check file lock on server

		FileData fileData = new FileData();
		DataInputStream diStream = null;
		ObjectOutputStream outputStream = null;
		Response response = new Response();
		SocketUtil sock;

		try {

			if (LockUtil.getFileLockOnServer(file)) {
				diStream = new DataInputStream(new FileInputStream(file));
				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];
				int read = 0;
				int numRead = 0;
				while (read < fileBytes.length
						&& (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}
				fileData.setDirectory(file.getPath().replace(AppProperties.SOURCE_DIRECTORY.replace("\\\\", "\\"), ""));
				fileData.setFilename(file.getName());
				fileData.setFileData(fileBytes);
				fileData.setStatus(1);
				System.out.println(file.getPath()+" "+AppProperties.SOURCE_DIRECTORY+" "+fileData.getDirectory() +" " +fileData.getFilename());

				sock = new SocketUtil();

				Request request = new Request();
				request.setCommand("SEND_FILE");
				request.setRequestData(fileData);
				request.setUserId(AppProperties.USER_ID);

				response = sock.sendToServer(request);
			} else {
				response.setResCode(-1);
				response.setMessage("Could not apply lock on file " + file.getName());
			}

		} catch (Exception e) {
			fileData.setStatus(-1);
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				if (socket != null)
					socket.close();
				if (diStream != null)
					diStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return response.getResCode();
	}

	public String getCheckSumfileOnServer(File file) {
		String hash = null;
		SocketUtil sock = new SocketUtil();

		Request request = new Request();
		request.setCommand("GET_CHECKSUM");
		request.setRequestData(file.getPath().replace(AppProperties.SOURCE_DIRECTORY.replace("\\\\", "\\"), ""));
		request.setUserId(AppProperties.USER_ID);

		Response response = sock.sendToServer(request);
		hash = (String) response.getData();
		return hash;
	}

	public static void getAllFilesFromServer() {
		DataInputStream diStream = null;
		ObjectOutputStream outputStream = null;
		SocketUtil sock;

		try {
			sock = new SocketUtil();

			Request request = new Request();
			request.setCommand("SYNC_FILES_FROM_SERVER");
			request.setUserId(AppProperties.USER_ID);

			sock.sendToServer(request);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
				if (diStream != null)
					diStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
