package com.xyz.utils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

import static cn.afterturn.easypoi.util.PoiCellUtil.getCellValue;

public class ExcelReadUtils {

    /**
     * 读取excel文件：列值可为空
     */
    public static List<List<List<String>>> getData(XSSFWorkbook wb, Integer ignoreRows, Integer sheetNum) {
        if (sheetNum == null) {
            sheetNum = wb.getNumberOfSheets();
        }
        List<List<List<String>>> lists = new ArrayList<>();
        //for循环：取前N个表,下标从0开始
        for (int i = 0; i < sheetNum; i++) {
            XSSFSheet sheetI = wb.getSheetAt(i);
            List<List<String>> list = new ArrayList<>();
            int cellSize = sheetI.getRow(0).getLastCellNum();//列数
            //第N+1行开始，可以通过传参，从第N+1行开始取
            for (int rowIndex = ignoreRows; rowIndex <= sheetI.getLastRowNum(); rowIndex++) {
                XSSFRow row = sheetI.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                List<String> rowList = new ArrayList<>();
                //在每行中的每一列，从下标0开始，一直取到所有
                for (int a = 0; a < cellSize; a++) {
                    String cellValue = getCellValue(row.getCell(a));
                    rowList.add(cellValue);
                }
                list.add(rowList);
            }
            lists.add(list);
        }
        return lists;
    }

}

