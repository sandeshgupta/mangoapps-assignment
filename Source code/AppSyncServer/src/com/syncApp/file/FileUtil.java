package com.syncApp.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

import com.syncApp.pojo.FileData;
import com.syncApp.properties.FileProperties;
import com.syncApp.util.Print;

public class FileUtil {

	public static boolean validateFile(File file) {
		if (file == null) {
			Print.print("Invalid file ");
			return false;
		}

		if (!file.exists()) {
			Print.print("File " + file.getName() + " does not exist in mentioned path");
			return false;
		}

		// check file extension
		if (!validateFileExtension(file)) {
			Print.print("Invalid file extension" + file.getName());
			return false;
		}

		// check file size
		if (!validateFileSize(file)) {
			Print.print(file.getName() + ": Only file of " + (FileProperties.MAX_FILE_SIZE_ALLOWED / 1024 / 1024)
					+ " MB allowed.");
			return false;
		}

		return true;

	}

	private static boolean validateFileSize(File file) {
		if (file.length() > FileProperties.MAX_FILE_SIZE_ALLOWED) {
			return false;
		}
		return true;

	}

	private static boolean validateFileExtension(File file) {
		String fileExtension = getFileExtension(file);
		if (fileExtension == null || "".equals(fileExtension)
				|| FileProperties.FILES_NOT_ALLOWED.contains(fileExtension)) {
			return false;
		}
		return true;

	}

	private static String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	public static boolean validateDirectory(File file) {

		// check size of whole directory
		if (file == null) {
			Print.print("Invalid directory ");
			return false;
		}

		if (!file.exists()) {
			Print.print("Directory " + file.getName() + " does not exist in mentioned path");
			return false;
		}

		// check file size
		if (validateDirectorySize(file)) {
			Print.print(file.getName() + " Only file of " + (FileProperties.MAX_FILE_SIZE_ALLOWED / 1024 / 1024)
					+ " MB allowed.");
			return false;
		}

		return true;
	}

	private static boolean validateDirectorySize(File file) {
		if (getDirectorySize(file.toPath()) > FileProperties.MAX_FILE_SIZE_ALLOWED) {
			return false;
		}
		return true;

	}

	public static long getDirectorySize(Path path) {

		final AtomicLong size = new AtomicLong(0);

		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

					size.addAndGet(attrs.size());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {

					Print.print("skipped: " + file + " (" + exc + ")");
					// Skip folders that can't be traversed
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

					if (exc != null)
						Print.print("had trouble traversing: " + dir + " (" + exc + ")");
					// Ignore errors traversing a folder
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
		}

		return size.get();
	}

	public static boolean deleteFile(File file) {
		try {
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static boolean renameFile(File fileSrc, File fileDest) {
		try {
			fileSrc.renameTo(fileDest);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean createFile(File file, FileData filedata) {
		FileOutputStream fileOutputStream;
		File parentFile ;
		try {
			
			parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(filedata.getFileData());
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("File " + file.getName() + " successfully created");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
		}
		return true;
	}

}
