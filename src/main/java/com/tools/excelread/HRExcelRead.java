package com.tools.excelread;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 对Excel做读、写操作的类
 * @author huangrong
 */
public class HRExcelRead {
    private static Logger logger = Logger.getLogger(HRExcelRead.class);
    private static HSSFWorkbook workbook = null;

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
     * 往Excel中写入数据
     *
     * @param filePath  文件名
     * @param sheetName sheet名字
     * @param cell      列
     * @param row       行
     * @param content   写入数据
     */
    public static void writeInfoToExcelByCell(String filePath, String sheetName,
                                              int cell, int row, String content) {
        //判断文件是否存在
//        if (false) {
//            ExcelUtil.createExcel1(filePath);
//        }
        //创建一个文件
        File file = new File(filePath);
        try {
            //创建一个工作薄
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet;
        try {
            //设置sheet名，0指第1张Sheet
            sheet = workbook.getSheet(sheetName);
        } catch (Exception e) {
            sheet = workbook.createSheet();
        }

        Row row1 = sheet.getRow(row);
        Cell cell1 = row1.getCell(cell);

//        cell1.getCellType();
        cell1.setCellValue(content);

//        Cell cellValue = sheet.getRow(row).getCell(cell);
//        cellValue.setCellValue(content);

        //写入excel
        saveFile(filePath);
    }


    /**
     * 设置单元格背景颜色
     *
     * @param filePath  文件路径
     * @param sheetName sheet名字
     * @param cell      要设置颜色的列
     * @param row       要设置颜色的行
     * @param color     要设置的颜色
     */
    public static void setCellBackgroundColor(String filePath, String sheetName, int cell,
                                              int row, int color)  {
        File file = new File(filePath);
        try {
            //创建一个工作薄
             workbook= new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = workbook.getSheet(sheetName);

        Row row1 = sheet.getRow(row);
        Cell cell1 = row1.getCell(cell);
//        XSSFCell cellValue = sheet.getRow(row).getCell(cell);
        CellStyle old = cell1.getCellStyle();
        CellStyle temp = workbook.createCellStyle();
        temp.cloneStyleFrom(old); // 拷贝旧的样式

        switch (color) {
            case 0:
                // 红色
                temp.setFillForegroundColor(IndexedColors.RED.getIndex());
                break;
            case 1:
                // 绿色
                temp.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
                break;
            case 2:
                // 灰色
                temp.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                break;
            case 3:
                // 黄色
                temp.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                break;
            case 4:
                // 白色
                temp.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                break;
            default:
                logger.error("设定颜色参数 (color) 错误...");
                System.out.println("设定颜色参数 (color) 错误...");
                break;
        }

        temp.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // XSSFFont font = book.createFont();
        // font.setFontHeightInPoints((short)9); // 字体大小
        // font.setFontName("宋体");
        // temp.setFont(font);
        cell1.setCellStyle(temp);
        saveFile(filePath);

    }


    /**
     * 将更改后的workbook写入excelFilePath位置
     * <p/>
     * <li>如果该文件不存在，则进行创建新文件</li>
     * <li>如果该文件已经存在，则进行覆盖</li>
     */
    public static void saveFile(String excelFilePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
