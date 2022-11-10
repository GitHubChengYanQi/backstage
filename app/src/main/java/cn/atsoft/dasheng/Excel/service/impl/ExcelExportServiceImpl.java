package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.ExcelExportService;
import cn.atsoft.dasheng.Excel.service.excelEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelExportServiceImpl implements ExcelExportService<excelEntity> {

    List<excelEntity> data = new ArrayList<>();

    private Map<String,String> sortColum;

    @Override
    public XSSFWorkbook exportExcel(List<String> columNames) {

        Map<String, String> sortColum = this.sortColum;

        XSSFWorkbook workbook = new XSSFWorkbook();

        int headerIndex = 0;
        //创建sheet
        XSSFSheet sheet = createSheet(workbook, "sheet1");
        sheet.setDefaultColumnWidth(20);

        /**
         * 创建表头
         */
        XSSFRow headRow = sheet.createRow(0);

        for (String columKey : sortColum.keySet()) {
            if (columNames.stream().anyMatch(i->i.equals(columKey))){
              //创建一个单元格
              XSSFCell cell = headRow.createCell(headerIndex);
              //将内容对象的文字内容写入到单元格中
              cell.setCellValue(sortColum.get(columKey));
              cell.setCellStyle(headerStyle(workbook));
              headerIndex+=1;
            }
        }
        /**
         * 创建数据
         */
        int index = 1;
        for (excelEntity datum : data) {
            //创建行
            XSSFRow dataRow = sheet.createRow(index);
            int cellIndex = 0 ;
            for (String columKey : sortColum.keySet()) {
                if (columNames.stream().anyMatch(i->i.equals(columKey))){

                    Field[] fields = datum.getClass().getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getName().equals(columKey)) {
                            XSSFCell cell = dataRow.createCell(cellIndex);
                            Object value = null ;
                            try{
                                value = field.get(datum);
                            }catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            cell.setCellValue(value == null ? "" : value.toString());
                            cellIndex+=1;
                        }
                    }

                }
            }
            index+=1;
        }





        return workbook;

    }

    @Override
    public void setData(List<excelEntity> data) {
        this.data = data;
    }

    private XSSFCellStyle headerStyle(XSSFWorkbook workbook) {
        //头样式
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        headerStyle.setAlignment(HorizontalAlignment.CENTER);//居左对齐
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        return headerStyle;
    }
    private XSSFSheet createSheet(XSSFWorkbook workbook, String name) {
        XSSFSheet sheet = workbook.createSheet(name);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        cellStyle.setAlignment(HorizontalAlignment.LEFT);//居左对齐
        cellStyle.setWrapText(true);//自动换行

        //设置边框样式
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return sheet;
    }

    @Override
    public void sortColum(Map<String, String> sortColum) {
        this.sortColum = sortColum;
    }
}
