package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuExcelService {

    @Autowired
    private SpuClassificationService spuClassificationService;

    @Autowired
    private SkuService skuService;

    public HSSFSheet dataEffective(HSSFSheet sheet,int size) {
        List<String> className = spuClassificationService.getClassName();
        //获取分类名称
        String[] categoryNames = className.toArray(new String[className.size()]);
        //资产分类有效性
        DVConstraint categoryConstraint = DVConstraint
                .createExplicitListConstraint(categoryNames);//textlist  下拉选项的 数组 如{列表1，列表2，。。。。。}
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList categoryRegions = new CellRangeAddressList(1, size, 1, 1);
        // 数据有效性对象
        HSSFDataValidation categoryDataValidationList = new HSSFDataValidation(categoryRegions, categoryConstraint);
        sheet.addValidationData(categoryDataValidationList);

        return sheet;
    }
}
