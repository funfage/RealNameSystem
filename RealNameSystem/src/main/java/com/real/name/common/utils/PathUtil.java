package com.real.name.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
	private static String separator = System.getProperty("file.separator");

	private static String winImagePath;

	private static String linuxImagePath;

	private static String winPayFilePath;

	private static String linuxPayFilePath;

	@Value("${win.image.base.path}")
	public void setWinPath(String winImagePath) {
		PathUtil.winImagePath = winImagePath;
	}

	@Value("${linux.image.base.path}")
	public void setLinuxPath(String linuxImagePath) {
		PathUtil.linuxImagePath = linuxImagePath;
	}

	@Value("${win.payFile.path}")
	public void setWinPayFilePath(String winPayFilePath) {
		PathUtil.winPayFilePath = winPayFilePath;
	}

	@Value("${linux.payFile.path}")
	public void setLinuxImagePath(String linuxPayFilePath) {
		PathUtil.linuxPayFilePath = linuxPayFilePath;
	}

	public PathUtil() {
	}

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().contains("win")) {
			basePath = winImagePath;
		} else {
			basePath = linuxImagePath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

	public static String getPayFileBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().contains("win")) {
			basePath = winPayFilePath;
		} else {
			basePath = linuxPayFilePath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

}
