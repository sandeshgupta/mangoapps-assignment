package com.syncApp.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.syncApp.Client;
import com.syncApp.properties.AppProperties;
import com.syncApp.util.EncryptionUtil;
import com.syncApp.util.Print;

public class FileProcessor {
	public static void optionUploadFile() {
		//Print.print("Uploadfile() : Enter");

		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));

		try {

			Print.print("Please enter the file path/folder name relative to source path");
			String filePath = AppProperties.SOURCE_DIRECTORY + buff.readLine();
			File file = new File(filePath);

			if (file != null && file.exists()) {
				if (file.isDirectory()) {
					processDirectory(file);
				} else if (file.isFile()) {
					processFile(file);
				}
			} else {
				Print.print("Invalid file/directory");
			}

		} catch (IOException e) {
			e.printStackTrace();
			Print.print("File access failed");
			Client.terminateApplication(1, "File access failed");
		}

		//Print.print("Uploadfile() : Exit");

	}

	private static void processFile(File file) {
		if (FileUtil.validateFile(file)) {
			// start processing the file
			uploadFile(file);
		} else {
			Print.print("Invalid file");
		}

	}

	private static void processDirectory(File file) {
		if (FileUtil.validateDirectory(file)) {
			// start recursively processing the directory
			File[] files = file.listFiles();
			for (File tempFile : files) {
				if (tempFile.isDirectory()) {
					processDirectory(tempFile);
				} else {
					processFile(tempFile);
				}
			}
		} else {
			Print.print("Invalid directory");
		}

	}

	private static void uploadFile(File file) {
		// You can reduce the file size by zipping at this stage

		// check hash of server file

		// SHA-1 checksum
		MessageDigest shaDigest;
		String shaChecksum = null;
		try {
			shaDigest = MessageDigest.getInstance("SHA-1");
			shaChecksum = EncryptionUtil.getFileChecksum(shaDigest, file);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Print.print("Checksum generation failed");
			Client.terminateApplication(1, "Checksum generation failed");
		} catch (IOException e) {
			e.printStackTrace();
			Print.print("File access failed");
			Client.terminateApplication(1, "File access failed");
		}
		//Print.print("Checksum at client side" + shaChecksum);

		FileTransfer ftp = new FileTransfer();
		String hashOfServerFile = ftp.getCheckSumfileOnServer(file);
		//Print.print("Checksum at server side" + hashOfServerFile);
		// checksum is same
		if (hashOfServerFile!=null &&  hashOfServerFile.equals(shaChecksum)) {
			Print.print("File " + file.getName() + " is up to date");
		} else {
			if (ftp.sendFile(file) == 1) {
				Print.print("File " + file.getName() + " sent successfully");
			} else {
				Print.print("File " + file.getName() + " transfer failed");
			}
		}
	}

	public static void syncAllFilesFromServer() {
		FileTransfer.getAllFilesFromServer();
	}

}
