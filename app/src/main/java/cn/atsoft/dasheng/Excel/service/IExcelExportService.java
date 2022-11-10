package cn.atsoft.dasheng.Excel.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface IExcelExportService<T> {


    XSSFWorkbook exportExcel(List<String> columNames);

    void setData(List<T> data);

    void sortColum(Map<String, String> sortColum);
}
