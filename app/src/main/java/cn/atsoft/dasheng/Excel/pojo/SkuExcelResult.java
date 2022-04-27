package cn.atsoft.dasheng.Excel.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SkuExcelResult {
    private List<SkuExcelItem> errorList;
    private Integer successNum;
}
