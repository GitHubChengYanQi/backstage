package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.IExcelImportService;
import cn.hutool.poi.excel.ExcelReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IExcelImportServiceImpl implements IExcelImportService {

    List data = new ArrayList<>();

    Map<String,String> columList = new HashMap<String,String>();

    @Override
    public void importExcel(ExcelReader excelReader) {
        Map<String, String> columList = this.columList;
        //设置标题行的别名Map
        for (String columNameKey : columList.keySet()) {
            excelReader.addHeaderAlias(columNameKey,columList.get(columNameKey));
        }
        data = excelReader.readAll();
    }

    @Override
    public List getData() {
        return data;
    }

    @Override
    public void columList(Map columList) {
        this.columList = columList;
    }
}
