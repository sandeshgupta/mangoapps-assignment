package com.syncApp.file;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.syncApp.pojo.IPAddress;
import com.syncApp.pojo.Request;
import com.syncApp.properties.ClientProperties;
import com.syncApp.util.EncryptionUtil;
import com.syncApp.util.Print;

public class FileProcessor {

	public static String getCheckSumOfFile(File file) {
		if (FileUtil.validateFile(file)) {
			MessageDigest shaDigest;
			String shaChecksum = null;
			try {
				shaDigest = MessageDigest.getInstance("SHA-1");
				shaChecksum = EncryptionUtil.getFileChecksum(shaDigest, file);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				Print.print("Checksum generation failed");
				// Server.terminateApplication(1, "Checksum generation failed");
			} catch (IOException e) {
				e.printStackTrace();
				Print.print("File access failed");
				// Server.terminateApplication(1, "File access failed");
			}
			Print.print("Checksum at server side" + shaChecksum);
			return shaChecksum;
		} else {
			return null;
		}
	}

	public static void sendFileToAllClients(Request request, String outputFile, Socket socket) {
		ArrayList<IPAddress> userDetails = ClientProperties.userDetails.get(request.getUserId());
		ExecutorService executor = Executors.newFixedThreadPool(userDetails.size() - 1);
		List<Future<?>> futures = new ArrayList<Future<?>>();
		File outputfileObj = new File(outputFile);

		for (int i = 0; i < userDetails.size(); i++) {
			// Dont send to device which uploaded the file
			IPAddress ip = userDetails.get(i);

			if (request.getDeviceId().equals(ip.getDeviceId())) {
				continue;
			}

			Future<?> future = executor.submit(new Runnable() {
				@Override
				public void run() {
					FileTransfer fileTransfer = new FileTransfer();
					fileTransfer.sendFileToClient(outputfileObj, ip);
				}
			});

			futures.add(future);

		}

		// A) Await all runnables to be done (blocking)
		for (Future<?> future : futures) {
			try {

				future.get();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
