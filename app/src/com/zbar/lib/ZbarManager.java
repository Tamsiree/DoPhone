package com.zbar.lib;


/**
 *
 */
public class ZbarManager {

	static {
		try {
			System.loadLibrary("zbar");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);
}
