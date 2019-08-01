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

	private static String winContractFilePath;

	private static String linuxContractFilePath;

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
	public void setLinuxPayFilePath(String linuxPayFilePath) {
		PathUtil.linuxPayFilePath = linuxPayFilePath;
	}

	@Value("${win.contractFile.path}")
	public void setWinContractFilePath(String winContractFilePath) {
		PathUtil.winContractFilePath = winContractFilePath;
	}

	@Value("${linux.contractFile.path}")
	public void setLinuxContractFilePath(String linuxContractFilePath) {
		PathUtil.linuxContractFilePath = linuxContractFilePath;
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

	public static String getContractFilePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().contains("win")) {
			basePath = winContractFilePath;
		} else {
			basePath = linuxContractFilePath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

}
