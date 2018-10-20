package com.syncApp.lock;

import java.util.HashMap;

import com.syncApp.util.Print;

public class LockManager {

	private HashMap<String, String> fileLockMap = new HashMap<>();

	private LockManager() {
	}

	private static class SingletonHelper {
		private static final LockManager INSTANCE = new LockManager();
	}

	public static LockManager getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public String isFileLock(String filename) {
		return fileLockMap.get(filename);
	}

	public boolean applyLock(String filename, String user) {
		if (isFileLock(filename) == null) {
			synchronized (this) {
				if (isFileLock(filename) == null) {
					fileLockMap.put(filename, user);
					Print.print(filename + " locked by " + user);
					return true;
				}
			}
		}

		Print.print(filename + " NOT locked by " + user);
		return false;
	}
	
	public void releaseLock(String filename) {
		fileLockMap.remove(filename);
	}
}