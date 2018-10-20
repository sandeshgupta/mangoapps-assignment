package com.syncApp.pojo;

import java.io.Serializable;

public class FileData implements Serializable {
	//http://www.coderpanda.com/java-socket-programming-transferring-directory-through-socket-in-java/
	public FileData() {
	}

	private static final long serialVersionUID = 1L;
	private String directory;
	private String filename;
	private long fileSize;
	private byte[] fileData;
	private int status;

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

}