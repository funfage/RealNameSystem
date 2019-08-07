package com.real.name.common.utils;

import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.PersonWorkRecord;
import com.real.name.record.entity.ProjectWorkRecord;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XSSFExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(XSSFExcelUtils.class);

    /**
     * 系统换行符
     */
    private static String lineSeparator = System.lineSeparator();

    /**
     * 详细日期格式化
     */
    private final static SimpleDateFormat detailFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 年月日日期格式化
     */
    private final static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 月日日期格式化
     */
    private final static SimpleDateFormat mdFormat = new SimpleDateFormat("MM-dd");

    /**
     * 时分日期格式化
     */
    private final static SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");

    public static void exportXSSFExcel(ProjectWorkRecord projectWorkRecord, List<String> headNameList, List<String> dayBetween) throws IOException {
        //创建一个空白的工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建一个空白的表
        XSSFSheet spreadSheet = workbook.createSheet("Sheet Name");

        //创建第零行
        XSSFRow row = spreadSheet.createRow((short) 0);
        //创建第零行元素风格
        XSSFCellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setAlignment(HorizontalAlignment.CENTER);
        rowCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //创建第零行字体
        XSSFFont rowFont = workbook.createFont();
        rowFont.setBold(true);
        rowFont.setFontName("等线");
        rowFont.setFontHeightInPoints((short) 18);
        rowCellStyle.setFont(rowFont);
        //设置短单位的高度
        row.setHeight((short) 500);
        //创建一个单元格
        XSSFCell cell = row.createCell(0);
        //设置单元格的值
        cell.setCellValue("筠诚建筑科技劳务实名制考勤数据报表");
        cell.setCellStyle(rowCellStyle);
        //合并七列
        spreadSheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row (0-based)
                0, //first column (0-based)
                6  //last column (0-based))
        ));

        //创建第一行
        XSSFRow row1 = spreadSheet.createRow((short) 1);
        row1.setHeight((short) 400);
        //元素风格
        XSSFCellStyle row1CellStyle = workbook.createCellStyle();
        row1CellStyle.setAlignment(HorizontalAlignment.LEFT);
        row1CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont row1CellFont = workbook.createFont();
        row1CellFont.setFontHeightInPoints((short) 12);
        row1CellFont.setFontName("等线");
        row1CellStyle.setFont(row1CellFont);
        //第一个元素
        XSSFCell row1Cell = row1.createCell(0);
        row1Cell.setCellValue("工程名称：" + projectWorkRecord.getProjectName());
        row1Cell.setCellStyle(row1CellStyle);
        spreadSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
        //第二个元素
        XSSFCell row1Cell3 = row1.createCell(3);
        row1Cell3.setCellValue("报表生成日期：" + detailFormat.format(projectWorkRecord.getCreateTime()));
        row1Cell3.setCellStyle(row1CellStyle);
        spreadSheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));
        //第三个元素
        XSSFCell row1Cell6 = row1.createCell(6);
        row1Cell6.setCellValue("统计时间：" + ymdFormat.format(projectWorkRecord.getStartDate()) + "至" + ymdFormat.format(projectWorkRecord.getEndDate()));
        row1Cell6.setCellStyle(row1CellStyle);
        spreadSheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));

        //创建第二行，头部数据
        XSSFRow row2 = spreadSheet.createRow((short) 2);
        XSSFCellStyle row2CellStyle = workbook.createCellStyle();
        row2CellStyle.setAlignment(HorizontalAlignment.LEFT);
        row2CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont row2CellFont = workbook.createFont();
        row2CellFont.setFontName("等线");
        row2CellFont.setFontHeightInPoints((short) 12);
        row2CellStyle.setFont(row2CellFont);
        row2.setHeight((short) 400);
        //List<PersonWorkRecord> personWorkRecordList = projectWorkRecord.getPersonWorkRecordList();
        //头部数据
        headNameList.addAll(dayBetween);
        for (int i = 0; i < headNameList.size(); i++) {
            //第二行第i列
            XSSFCell row2Celli = row2.createCell(i);
            row2Celli.setCellValue(headNameList.get(i));
            spreadSheet.setColumnWidth(i, 4800);
            row2Celli.setCellStyle(row2CellStyle);
        }
        //内容
        //正常数据输出的格式
        XSSFCellStyle rowiCellStyle = workbook.createCellStyle();
        rowiCellStyle.setAlignment(HorizontalAlignment.LEFT);
        rowiCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont rowiCellFont = workbook.createFont();
        rowiCellFont.setFontName("等线");
        rowiCellFont.setFontHeightInPoints((short) 12);
        rowiCellStyle.setFont(rowiCellFont);
        //异常数据输出的格式
        XSSFCellStyle errorCellStyle = workbook.createCellStyle();
        errorCellStyle.setAlignment(HorizontalAlignment.LEFT);
        errorCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        errorCellStyle.setWrapText(true);
        XSSFFont errorFont = workbook.createFont();
        errorFont.setFontName("等线");
        errorFont.setFontHeightInPoints((short) 12);
        errorFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        errorCellStyle.setFont(errorFont);
        List<PersonWorkRecord> personWorkRecords = projectWorkRecord.getPersonWorkRecordList();
        for (int i = 0; i < personWorkRecords.size(); i++) {
            PersonWorkRecord workRecord = personWorkRecords.get(i);
            XSSFRow rowi = spreadSheet.createRow(i + 3);
            rowi.setHeight((short) 700);
            //姓名
            XSSFCell rowiCell = rowi.createCell(0);
            rowiCell.setCellValue(workRecord.getPersonName());
            rowiCell.setCellStyle(rowiCellStyle);
            //身份证号码
            XSSFCell rowiCell1 = rowi.createCell(1);
            rowiCell1.setCellValue(workRecord.getIdCardNumber());
            rowiCell1.setCellStyle(rowiCellStyle);
            //性别
            XSSFCell rowiCell2 = rowi.createCell(2);
            rowiCell2.setCellValue(workRecord.getGender() == 0 ? "男" : "女");
            rowiCell2.setCellStyle(rowiCellStyle);
            //身份证号码
            XSSFCell rowiCell3 = rowi.createCell(3);
            rowiCell3.setCellValue(workRecord.getSubordinateCompany());
            rowiCell3.setCellStyle(rowiCellStyle);
            //班组名
            XSSFCell rowiCell4 = rowi.createCell(4);
            rowiCell4.setCellValue(workRecord.getTeamName());
            rowiCell4.setCellStyle(rowiCellStyle);
            //工种
            XSSFCell rowiCell5 = rowi.createCell(5);
            rowiCell5.setCellValue(NationalUtils.getWorkType(workRecord.getWorkType()));
            rowiCell5.setCellStyle(rowiCellStyle);
            //出勤天数
            XSSFCell rowiCell6 = rowi.createCell(6);
            rowiCell6.setCellValue(workRecord.getWorkDay());
            rowiCell6.setCellStyle(rowiCellStyle);
            //考勤小时
            XSSFCell rowiCell7 = rowi.createCell(7);
            rowiCell7.setCellValue(workRecord.getWorkHours());
            rowiCell7.setCellStyle(rowiCellStyle);
            //考勤日期
            List<Attendance> attendanceList = workRecord.getAttendanceList();
            for (int j = 0; j < attendanceList.size(); j++) {
                Attendance attendance = attendanceList.get(j);
                Date workTime = attendance.getWorkTime();
                Date startTime = attendance.getStartTime();
                Date endTime = attendance.getEndTime();
                //格式化时间为:mm-dd
                String mdFormat = XSSFExcelUtils.mdFormat.format(workTime);
                //格式化时间为hh:ss
                String startFormat = "";
                if (startTime != null) {
                    startFormat = hmFormat.format(attendance.getStartTime());
                }
                String endFormat = "";
                if (endTime != null) {
                    endFormat = hmFormat.format(attendance.getEndTime());
                }
                for (int h = 0; h < dayBetween.size(); h++) {
                    if (dayBetween.get(h).equals(mdFormat)) {
                        XSSFCell rowiCellj = rowi.createCell(8 + h);
                        if (attendance.getStatus() == -2) {
                            rowiCellj.setCellValue("统计异常!");
                            rowiCellj.setCellStyle(errorCellStyle);
                        } else if (attendance.getStatus() == -1) {
                            rowiCellj.setCellValue("异常!" + lineSeparator + "?-" + endFormat);
                            rowiCellj.setCellStyle(errorCellStyle);
                        } else if (attendance.getStatus() == 0) {
                            rowiCellj.setCellValue("异常!" + lineSeparator + startFormat + "-?");
                            rowiCellj.setCellStyle(errorCellStyle);
                        } else {
                            rowiCellj.setCellValue(startFormat + "-" + endFormat);
                            rowiCellj.setCellStyle(rowiCellStyle);
                        }
                    }
                }
            }
        }
        //生成文件
        File file = new File(PathUtil.getExcelFilePath() + projectWorkRecord.getProjectCode() + ".xlsx");
        // 检测是否存在目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        //文件输出
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        logger.warn("报表生成成功:{}", file.getAbsolutePath());
    }
}
