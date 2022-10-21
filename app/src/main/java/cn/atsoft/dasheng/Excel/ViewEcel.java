package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.service.InstockViewExcel;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/viewExcel")
public class ViewEcel {

    @Autowired
    private InstockViewExcel instockViewExcel;

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ApiOperation("导出")
    public void export(HttpServletResponse response) throws IOException {
        instockViewExcel.excel(response,new DataStatisticsViewParam());
    }



}
