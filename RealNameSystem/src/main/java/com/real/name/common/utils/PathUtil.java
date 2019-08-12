package com.real.name.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
	private static String separator = System.getProperty("file.separator");

	/**
	 * 头像路径
	 */
	private static String winImagePath;

	private static String linuxImagePath;

	/**
	 * 薪资路径
	 */
	private static String winPayFilePath;

	private static String linuxPayFilePath;

	/**
	 * 合同路径
	 */
	private static String winContractFilePath;

	private static String linuxContractFilePath;

	/**
	 * 报表输出路径
	 */
	private static String winExcelPath;

	private static String linuxExcelPath;

	/**
	 * 系统名
	 */
	private final static String os = System.getProperty("os.name");

	private final static String win = "win";

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

	@Value("${win.excel.path}")
	public void setWinExcelPath(String winExcelPath) {
		PathUtil.winExcelPath = winExcelPath;
	}

	@Value("${linux.excel.path}")
	public void setLinuxExcelPath(String linuxExcelPath) {
		PathUtil.linuxExcelPath = linuxExcelPath;
	}

	public PathUtil() {
	}

	public static String getImgBasePath() {
		String basePath;
		if (os.toLowerCase().contains(win)) {
			basePath = winImagePath;
		} else {
			basePath = linuxImagePath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

	public static String getPayFileBasePath() {
		String basePath;
		if (os.toLowerCase().contains(win)) {
			basePath = winPayFilePath;
		} else {
			basePath = linuxPayFilePath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

	public static String getContractFilePath() {
		String basePath;
		if (os.toLowerCase().contains(win)) {
			basePath = winContractFilePath;
		} else {
			basePath = linuxContractFilePath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

	public static String getExcelFilePath() {
		String basePath;
		if (os.toLowerCase().contains(win)) {
			basePath = winExcelPath;
		} else {
			basePath = linuxExcelPath;
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

}
