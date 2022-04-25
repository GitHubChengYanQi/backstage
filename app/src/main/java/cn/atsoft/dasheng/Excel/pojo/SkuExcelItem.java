package cn.atsoft.dasheng.Excel.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
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


    private String skuName;


    private String unit;


    private String specifications; //规格


    private String isNotBatch;

    private String itemRule;


    private Integer line;

    private String describe;  //描述

    private String error;

    private String type;

    private Long errorSkuId;

    private SkuResult simpleResult;
}