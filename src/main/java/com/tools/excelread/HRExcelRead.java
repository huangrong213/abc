package com.tools.excelread;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HRExcelRead {
    private static Logger logger = Logger.getLogger(HRExcelRead.class);

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileChecked(String filePath) {
        Boolean bl = new File(filePath).exists();
        return bl;
    }

    /**
     * 获取WorkBook
     *
     * @param filePath 需要读取的文件路径
     * @return
     */
    public static Workbook getWorkBook(String filePath) {
        Workbook workbook = null;
        try {
            File file = new File(filePath);
            InputStream is = new FileInputStream(file);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);

            } else if (filePath.endsWith(".xlsx")) {
                // workbook = new XSSFWorkbook(is);
                workbook = WorkbookFactory.create(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workbook;
    }


    /**
     * 读取Excel操作
     *
     * @param filePath  Excel文件路径
     * @param sheetName 测试用例存放的sheet名字
     * @return
     */
    public static Object[][] readExcel(String filePath, String sheetName) {
        //判断文件是否存在
        fileChecked(filePath);
        //获取workbook
        Workbook wk = getWorkBook(filePath);
        ArrayList<String> arrkey = new ArrayList<String>();
        //根据sheet名字获取sheet
        Sheet sheet = wk.getSheet(sheetName);
        //获取总行数，加1是因为行号是从0开始读的
        int rowAllNum = sheet.getLastRowNum() + 1;
        //获取总列数,getRow(0),意思是计算第一行有多少列
        int columns = sheet.getRow(0).getPhysicalNumberOfCells();
        HashMap<String, String>[][] map = new HashMap[rowAllNum - 1][1];
        // 对数组中所有元素hashmap进行初始化
        if (rowAllNum > 1) {
            for (int i = 0; i < rowAllNum - 1; i++) {
                map[i][0] = new HashMap();
            }
        } else {
            logger.error("测试的Excel" + filePath + "中没有数据");
        }
        // 获得首行的列名，作为hashmap的key值
        for (int c = 0; c < columns; c++) {
            String cellvalue = getCellValue(sheet, 0, c);
            arrkey.add(cellvalue);
        }
        // 遍历所有的单元格的值添加到hashmap中
        for (int r = 1; r < rowAllNum; r++) {
            for (int c = 0; c < columns; c++) {
                String cellvalue = getCellValue(sheet, r, c);
                map[r - 1][0].put(arrkey.get(c), cellvalue);
            }
        }
        return map;
    }


    /**
     * 往excel中写入数据
     * @param Row 指定行
     * @param cell 指定列
     * @param text 写入内容
     */
    public static void writeExcel(int Row, int cell, String text) {


    }


    /**
     * 通过sheet 行号和列返回值
     *
     * @param sheet   sheet名字
     * @param rowNum  行号
     * @param cellNum 列号
     * @return
     */
    public static String getCellValue(Sheet sheet, int rowNum, int cellNum) {
        Cell cell = sheet.getRow(rowNum).getCell(cellNum);
        //首先强制设置成string类型
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String value = cell.getStringCellValue();
        return value;

    }

    /**
     * 把不同类型的单元格转换成字符串，并返回
     *
     * @param cell
     * @return 当个单元格值
     */
    public static String GetCellValueType(Cell cell) {
        String cellValue = "";
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }

        return cellValue;
    }


}
