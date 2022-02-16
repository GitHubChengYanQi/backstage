package cn.atsoft.dasheng.Excel.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ExcelPositionResult {
    private List<PositionBind> errorList;
    private List<PositionBind> successList;
}
