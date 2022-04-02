package cn.atsoft.dasheng.Excel.pojo;

import cn.hutool.poi.excel.sax.handler.RowHandler;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class Bom {
    private Integer line;
    private String strand;
    private String spuName;
    private String spc;
    private double num;
    private String unit;
    private String skuName;
    private String spuClass;
    private String batch;
}
