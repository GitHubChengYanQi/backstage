package cn.atsoft.dasheng.Excel.service;

import cn.hutool.poi.excel.ExcelReader;

import java.util.List;
import java.util.Map;

public interface IExcelImportService<T> {

    List<T> importExcel(ExcelReader excelReader,Class<T> clazz);

    void columList(Map<String, String> columList);
}
