package cn.atsoft.dasheng.Excel.service;

import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface InstockViewExcel {
    void excel(DataStatisticsViewParam param) throws IOException;
}
