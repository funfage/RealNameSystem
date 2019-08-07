package com.real.name.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HSSFExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(HSSFExcelUtils.class);

    /**
     * 导出excel表格
     */
    public static HSSFWorkbook exportHSSFExcel(List<List<String>> head, List<List<String>> body) {
        //创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建表
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        //创建行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        BorderStyle borderStyle = BorderStyle.valueOf((short) 1);
        setBorderStyle(cellStyle, borderStyle);
        cellStyle.setFont(setFontStyle(workbook, "黑体", (short) 14));
        //设置水平对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        sheet.createFreezePane(0, 1, 0, 1);
        //设置表头
        /*for (int i = 0; i < head.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(cellStyle);
        }*/
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2, borderStyle);
        cellStyle2.setFont(setFontStyle(workbook, "宋体", (short) 12));
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        //设置表格内容
        for (int i = 0; i < body.size(); i++) {
            row = sheet.createRow(i + 1);
            List<String> paramList = body.get(i);
            for (int p = 0; p < paramList.size(); p++) {
                cell = row.createCell(p);
                cell.setCellValue(paramList.get(p));
                cell.setCellStyle(cellStyle2);
            }
        }
        for (int i = 0, isize = head.size(); i < isize; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    /**
     * 文件输出
     * @param workbook 填充好的workbook
     * @param path     存放的位置
     * @author LiuYang
     */
    public static void outFile(HSSFWorkbook workbook, String path, HttpServletResponse response) {
        SimpleDateFormat fdate = new SimpleDateFormat("yyyyMMdd-HH点mm分");
        path = path.substring(0, path.lastIndexOf(".")) + fdate.format(new Date()) + path.substring(path.lastIndexOf("."));
        OutputStream os = null;
        File file = null;
        try {
            file = new File(path);
            String filename = file.getName();
            os = new FileOutputStream(file);
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            workbook.write(os);
        } catch (IOException e) {
            logger.error("");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("");
            }
        }
    }

    /**
     * 设置字体样式
     *
     * @param workbook 工作簿
     * @param name     字体类型
     * @param height   字体大小
     * @return HSSFFont
     * @author LiuYang
     */
    private static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(height);
        font.setFontName(name);
        return font;
    }

    /**
     * 设置单元格样式
     *
     * @param cellStyle 工作簿
     * @param border    border样式
     * @author LiuYang
     */
    private static void setBorderStyle(HSSFCellStyle cellStyle, BorderStyle border) {
        cellStyle.setBorderBottom(border); // 下边框
        cellStyle.setBorderLeft(border);// 左边框
        cellStyle.setBorderTop(border);// 上边框
        cellStyle.setBorderRight(border);// 右边框
    }

}
