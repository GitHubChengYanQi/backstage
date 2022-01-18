package cn.atsoft.dasheng.Excel.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data

public class SkuExcelItem {


    private String standard;


    private String spuClass;


    private String classItem;


    private String spuName;


    private String unit;


    private String isNotBatch;


    private List<String> attributes;


    private Integer line;

}
