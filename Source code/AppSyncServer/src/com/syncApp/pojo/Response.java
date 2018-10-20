package com.syncApp.pojo;

import java.io.Serializable;

public class Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int resCode;
	private String command;
	private Object responseData;
	private String message;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object getData() {
		return responseData;
	}

	public void setData(Object responseData) {
		this.responseData = responseData;
	}

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int resCode) {
		this.resCode = resCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
