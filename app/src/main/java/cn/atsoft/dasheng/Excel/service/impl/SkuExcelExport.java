package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.excelEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SkuExcelExport extends ExcelExportServiceImpl implements excelEntity {

    private Map<String, String> sortColum = new HashMap<String, String>() {{
        put("standard", "物料编码");
        put("spu", "产品名称");
        put("spuClass", "物料分类");
        put("unitId", "单位");
        put("model", "型号");
        put("partNo", "零件号");
        put("nationalStandard", "国家标准");
        put("specifications", "规格");
        put("spuCoding", "产品码");
        put("sku", "物料描述");
        put("materialId", "材质");
        put("level", "级别");
        put("heatTreatment", "热处理");
        put("weight", "重量");
        put("skuSize", "尺寸");
        put("color", "表色");
        put("packaging", "包装方式");
        put("brandIds", "品牌");
        put("remarks", "备注");
        put("maintenancePeriod", "养护周期");
        put("batch", "二维码生成方式");
        put("viewFrame", "图幅");

    }};

    public SkuExcelExport() {
        sortColum(sortColum);
    }
}
