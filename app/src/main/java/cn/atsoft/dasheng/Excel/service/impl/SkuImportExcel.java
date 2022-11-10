package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
public class SkuImportExcel extends IExcelImportServiceImpl<SkuExcelItem>{
    public SkuImportExcel() {
        columList(new HashMap<String, String>() {{
            put("物料编码", "standard");
            put("物料分类", "spuClass");
            put("产品名称", "classItem");
            put("型号", "model");
            put("单位", "unit");
            put("零件号", "partNo");
            put("是否批量", "batch");
            put("规格", "specifications");
            put("国家标准", "nationalStandard");
            put("产品码", "spuCoding");
            put("材质", "materialId");
            put("级别", "level");
            put("热处理", "heatTreatment");
            put("重量", "weight");
            put("尺寸", "skuSize");
            put("表色", "color");
            put("包装方式", "packaging");
            put("品牌", "brandIds");
            put("备注", "remarks");
            put("养护周期", "maintenancePeriod");
            put("图幅", "viewFrame");
        }});
    }
}
