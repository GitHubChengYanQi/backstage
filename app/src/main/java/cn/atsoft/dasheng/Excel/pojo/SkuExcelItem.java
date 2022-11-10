package cn.atsoft.dasheng.Excel.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.atsoft.dasheng.Excel.service.IExcelEntity;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data

public class SkuExcelItem implements IExcelEntity {


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

    private String partNo;

    private String nationalStandard;

    private String spuCoding;

    private String materialId;

    private String model;

    private String level;

    private String heatTreatment;

    private Long errorSkuId;

    private String weight;

    private String color;

    private String packaging;

    private String remarks;

    private String attributeAndValue;

    private String maintenancePeriod;

    private String skuSize;

    private String viewFrame;

    private SkuResult simpleResult;

    private Long classId;

    private Long unitId;

    private Integer success;
}