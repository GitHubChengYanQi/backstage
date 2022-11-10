package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.IExcelEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SkuExcelExport extends IExcelExportServiceImpl implements IExcelEntity {

    private Map<String, String> sortColum = new HashMap<String, String>() {{
        put("standard", "物料编码");
        put("spu", "产品名称");
        put("spuClass", "物料分类");
        put("model", "型号");
        put("partNo", "零件号");
        put("nationalStandard", "国家标准");
        put("specifications", "规格");
        put("sku", "物料描述");
        put("unitId", "单位");
        put("color", "表色");
        put("level", "级别");
        put("skuSize", "尺寸");
        put("weight", "重量");
        put("packaging", "包装方式");
        put("materialId", "材质");
        put("viewFrame", "图幅");
        put("maintenancePeriod", "养护周期");
        put("spuCoding", "产品码");
        put("batch", "二维码生成方式");
        put("remarks", "备注");

    }};

    public SkuExcelExport() {
        sortColum(sortColum);
    }
}
