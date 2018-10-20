package com.syncApp.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.syncApp.pojo.FileData;
import com.syncApp.pojo.IPAddress;
import com.syncApp.pojo.Request;
import com.syncApp.pojo.Response;
import com.syncApp.properties.AppProperties;
import com.syncApp.util.Print;
import com.syncApp.util.SocketUtil;

public class FileTransfer {
	private Socket socket = null;

	public int sendFileToClient(File file, IPAddress ip) {

		// check file lock on server

		FileData fileData = new FileData();
		DataInputStream diStream = null;
		ObjectOutputStream outputStream = null;
		Response response = new Response();
		SocketUtil sock;

		try {
			if (connect(ip)) {
				diStream = new DataInputStream(new FileInputStream(file));
				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];
				int read = 0;
				int numRead = 0;
				while (read < fileBytes.length
						&& (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}
				fileData.setDirectory(file.getPath().replace(AppProperties.SOURCE_DIRECTORY.replace("\\\\", "\\"), "")
						.replace(ip.getUser(), ""));
				fileData.setFilename(file.getName());
				fileData.setFileData(fileBytes);
				fileData.setStatus(1);

				sock = new SocketUtil(socket);

				Request request = new Request();
				request.setCommand("SEND_FILE");
				request.setRequestData(fileData);

				response = sock.sendToServer(request);
				response.setResCode(1);
				response.setMessage("File " + file.getName() + "send to " + ip.getIp() + ip.getPort());
			}
		} catch (Exception e) {
			response.setResCode(-1);
			response.setMessage("Could not send file to client " + ip.getIp() + ip.getPort());
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


	public boolean connect(IPAddress ip) {
		try {
			socket = new Socket(ip.getIp(), ip.getPort());
			return true;
		} catch (IOException e) {
			Print.print("Could not connect to "+ip.getDeviceId());
		}
		return false;
	}
}
