package com.real.name.others;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ExcelTest {

    public static void main(String[] args) throws Exception {
        testFontStyle();
    }

    private static void testCreateCell() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("cell types");
        XSSFRow row = spreadsheet.createRow((short) 2);
        row.createCell(0).setCellValue("Type of Cell");
        row.createCell(1).setCellValue("cell value");
        row = spreadsheet.createRow((short) 3);
        row.createCell(0).setCellValue("set cell type BLANK");
        row.createCell(1);
        row = spreadsheet.createRow((short) 4);
        row.createCell(0).setCellValue("set cell type BOOLEAN");
        row.createCell(1).setCellValue(true);
        row = spreadsheet.createRow((short) 5);
        row.createCell(0).setCellValue("set cell type ERROR");
        row.createCell(1).setCellValue(CellType.ERROR.getCode());
        row = spreadsheet.createRow((short) 6);
        row.createCell(0).setCellValue("set cell type date");
        row.createCell(1).setCellValue(new Date());
        row = spreadsheet.createRow((short) 7);
        row.createCell(0).setCellValue("set cell type numeric" );
        row.createCell(1).setCellValue(20);
        row = spreadsheet.createRow((short) 8);
        row.createCell(0).setCellValue("set cell type string");
        row.createCell(1).setCellValue("A String");
        FileOutputStream out = new FileOutputStream(new File("E:/excel/typesofcells.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("typesofcells.xlsx written successfully");
    }

    private static void testCellStyle() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("cellstyle");
        XSSFRow row = spreadsheet.createRow((short) 1);
        //设置短单位的高度
        row.setHeight((short) 800);
        XSSFCell cell = row.createCell((short) 1);
        cell.setCellValue("test of merging");
        //MEARGING CELLS
        //this statement for merging cells
        spreadsheet.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)
                1, //last row (0-based)
                1, //first column (0-based)
                4 //last column (0-based)
        ));
        //CELL Alignment
        row = spreadsheet.createRow(5);
        cell = row.createCell(0);
        row.setHeight((short) 800);
        // Top Left alignment
        XSSFCellStyle style1 = workbook.createCellStyle();
        spreadsheet.setColumnWidth(0, 8000);
        style1.setAlignment(HorizontalAlignment.LEFT);
        style1.setVerticalAlignment(VerticalAlignment.TOP);
        cell.setCellValue("Top Left");
        cell.setCellStyle(style1);
        row = spreadsheet.createRow(6);
        cell = row.createCell(1);
        row.setHeight((short) 800);
        // Center Align Cell Contents
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellValue("Center Aligned");
        cell.setCellStyle(style2);
        row = spreadsheet.createRow(7);
        cell = row.createCell(2);
        row.setHeight((short) 800);
        // Bottom Right alignment
        XSSFCellStyle style3 = workbook.createCellStyle();
        style3.setAlignment(HorizontalAlignment.RIGHT);
        style3.setVerticalAlignment(VerticalAlignment.BOTTOM);
        cell.setCellValue("Bottom Right");
        cell.setCellStyle(style3);
        row = spreadsheet.createRow(8);
        cell = row.createCell(3);
        // Justified Alignment
        XSSFCellStyle style4 = workbook.createCellStyle();
        style4.setAlignment(HorizontalAlignment.JUSTIFY);
        style4.setVerticalAlignment(VerticalAlignment.JUSTIFY);
        cell.setCellValue("Contents are Justified in Alignment");
        cell.setCellStyle(style4);
        //CELL BORDER
        row = spreadsheet.createRow((short) 10);
        row.setHeight((short) 800);
        cell = row.createCell((short) 1);
        cell.setCellValue("BORDER");
        XSSFCellStyle style5 = workbook.createCellStyle();
        style5.setBorderBottom(BorderStyle.THICK);
        style5.setBottomBorderColor(IndexedColors.BLUE.getIndex());
        style5.setBorderLeft(BorderStyle.DOUBLE);
        style5.setLeftBorderColor(IndexedColors.GREEN.getIndex());
        style5.setBorderRight(BorderStyle.HAIR);
        style5.setRightBorderColor(IndexedColors.RED.getIndex());
        style5.setBorderTop(BorderStyle.DASH_DOT);
        style5.setTopBorderColor(IndexedColors.CORAL.getIndex());
        cell.setCellStyle(style5);
        //Fill Colors
        //background color
        row = spreadsheet.createRow((short) 10 );
        cell = row.createCell((short) 1);
        XSSFCellStyle style6 = workbook.createCellStyle();
        style6.setFillBackgroundColor(HSSFColor.LEMON_CHIFFON.index);
        style6.setFillPattern(FillPatternType.LESS_DOTS);
        style6.setAlignment(HorizontalAlignment.LEFT);
        spreadsheet.setColumnWidth(1,8000);
        cell.setCellValue("FILL BACKGROUNG/FILL PATTERN");
        cell.setCellStyle(style6);
        //Foreground color
        row = spreadsheet.createRow((short) 12);
        cell = row.createCell((short) 1);
        XSSFCellStyle style7=workbook.createCellStyle();
        style7.setFillForegroundColor(HSSFColor.BLUE.index);
        style7.setFillPattern(FillPatternType.LESS_DOTS);
        style7.setAlignment(HorizontalAlignment.LEFT);
        cell.setCellValue("FILL FOREGROUND/FILL PATTERN");
        cell.setCellStyle(style7);
        FileOutputStream out = new FileOutputStream(new File("E:/excel/cellstyle.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("cellstyle.xlsx written successfully");
    }

    public static void testFontStyle() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Fontstyle");
        XSSFRow row = spreadsheet.createRow(2);
        //Create a new font and alter it.
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 30);
        font.setFontName("IMPACT");
        font.setItalic(true);
        font.setColor(HSSFColor.BRIGHT_GREEN.index);
        //Set font into style
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        // Create a cell with a value and set style to it.
        XSSFCell cell = row.createCell(1);
        cell.setCellValue("Font Style");
        cell.setCellStyle(style);
        FileOutputStream out = new FileOutputStream(new File("E:/excel/fontstyle.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("fontstyle.xlsx written successfully");
    }


}

