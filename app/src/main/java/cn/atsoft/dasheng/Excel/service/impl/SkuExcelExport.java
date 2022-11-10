package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.excelEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class SkuExcelExport extends ExcelExportServiceImpl implements  excelEntity{

    private Map<String,String> sortColum = new HashMap<String,String>(){{
        put("standard","物料编码");
        put("skuMessage","产品名称");
        put("model","型号");
        put("partNo","零件号");
        put("unitName","单位");
        put("specifications","规格");
        put("attributeAndValues","规格参数");
        put("color","颜色");
        put("heatTreatment","热处理");
        put("level","等级");
        put("packaging","包装方式");
        put("viewFrame","图幅");
        put("skuSize","尺寸");
        put("weight","重量");
        put("nationalStandard","国家标准");
    }};

    public SkuExcelExport() {
        sortColum(sortColum);
    }
}
