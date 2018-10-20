package com.syncApp.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.syncApp.pojo.Request;
import com.syncApp.pojo.Response;
import com.syncApp.properties.AppProperties;

public class SocketUtil {

	Socket socket = null;

	public SocketUtil() {
	}

	public SocketUtil(Socket socket) {
		this.socket = socket;
	}

	public void connect() {
		try {
			socket = new Socket(AppProperties.SERVER_IP, AppProperties.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Response sendToServer(Request request) {
		Response response = new Response();
		ObjectOutputStream outputStream;
		ObjectInputStream inputStream;
		try {
			
			if (socket == null) {
				connect();
			}
			
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(request);
			outputStream.flush();

			inputStream = new ObjectInputStream(socket.getInputStream());
			try {
				response = (Response) inputStream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			response.setResCode(-1);
			response.setData("Something went wrong!");
			e.printStackTrace();
		}
		return response;

	}

	public Request readFromClient() {
		Request request = new Request();

		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			try {
				request = (Request) inputStream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;

	}
	
	public void sendToClient(Response response) {
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(response);
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
