package cn.atsoft.dasheng.Excel.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface ExcelImportService<T> {

    XSSFWorkbook importExcel(List<String> columNames);

    void getData(List<T> data);

    void sortColum(Map<String, String> sortColum);
}
