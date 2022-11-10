package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.Excel.service.IExcelEntity;
import lombok.Data;

@Data
public class SpuExcel implements IExcelEntity {
    private String spuName;
    private String spuCoding;
    private String spuClass;
    private String line;
    private String unit;
    private String specifications;
    private String error;
}
