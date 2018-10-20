package com.syncApp.util;

import java.io.File;

import com.syncApp.pojo.Request;
import com.syncApp.pojo.Response;
import com.syncApp.properties.AppProperties;

public class LockUtil {

	public static boolean getFileLockOnServer(File file) {
		SocketUtil sock = new SocketUtil();

		Request request = new Request();
		request.setCommand("GET_FILE_LOCK");
		request.setRequestData(file.getPath().replace(AppProperties.SOURCE_DIRECTORY.replace("\\\\", "\\"), ""));
		request.setUserId(AppProperties.USER_ID);

		Response response = sock.sendToServer(request);

		return (Boolean) response.getData();
	}

}
