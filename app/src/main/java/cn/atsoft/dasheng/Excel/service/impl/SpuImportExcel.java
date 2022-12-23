package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.pojo.SpuExcel;
import cn.atsoft.dasheng.Excel.service.IExcelEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Service
public class SpuImportExcel extends IExcelImportServiceImpl implements IExcelEntity {

    public SpuImportExcel() {
        columList(new HashMap<String, String>() {{
            put("产品编码", "spuCoding");
            put("产品分类", "spuClass");
            put("产品名称", "spuName");
            put("单位", "unit");
            put("型号", "specifications");
        }});
    }
}
