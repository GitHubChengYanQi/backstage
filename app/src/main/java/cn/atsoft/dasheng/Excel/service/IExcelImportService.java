package cn.atsoft.dasheng.Excel.service;

import cn.hutool.poi.excel.ExcelReader;

import java.util.List;
import java.util.Map;

public interface IExcelImportService<T> {

    List<T> getData();
    void importExcel(ExcelReader excelReader);

    void columList(Map<String, String> columList);
}
