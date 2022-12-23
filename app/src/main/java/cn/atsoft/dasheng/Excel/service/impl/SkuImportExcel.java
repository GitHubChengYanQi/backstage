package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
public class SkuImportExcel extends IExcelImportServiceImpl<SkuExcelItem>{
    public SkuImportExcel() {
        columList(new HashMap<String, String>() {{
            put("物料编码", "standard");
            put("产品名称", "classItem");
            put("物料分类", "spuClass");
            put("零件号", "partNo");
            put("国家标准", "nationalStandard");
            put("规格", "specifications");
            put("物料描述", "attributeAndValue");
            put("单位", "unit");
            put("表色", "color");
            put("型号", "model");
            put("热处理", "heatTreatment");
            put("级别", "level");
            put("尺寸", "skuSize");
            put("重量", "weight");
            put("包装方式", "packaging");
            put("材质", "materialId");
            put("图幅", "viewFrame");
            put("养护周期", "maintenancePeriod");
            put("产品码", "spuCoding");
            put("二维码生成方式", "isNotBatch");
            put("备注", "remarks");
        }});
    }
}
