package cn.atsoft.dasheng.Excel.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class SkuExcelItem {


    private String 成品码;


    private String 分类;


    private String 产品;


    private String 型号;


    private String 单位;

    private String 是否批量;


    private List<String> attributes;

}
