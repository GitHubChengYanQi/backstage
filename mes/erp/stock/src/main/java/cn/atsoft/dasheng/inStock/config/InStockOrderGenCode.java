package cn.atsoft.dasheng.inStock.config;

import cn.atsoft.dasheng.codeRule.model.GenCodeInterface;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.atsoft.dasheng.codeRule.model.enums.CodingRuleFiledEnum.skuClass;

@Service("InStockOrderGenCode")

public class InStockOrderGenCode implements GenCodeInterface {
    @Autowired
    private RestCategoryService categoryService;
    @Autowired
    private RestCodeRuleService codeRuleService;
    @Autowired
    private RestStorehouseService restStorehouseService;

    @Override
    public String genCode(String modelName, Object data) {
        String codingByModule = codeRuleService.getCodingByModule(modelName);
        if (ToolUtil.isNotEmpty(data)) {
            Long storehouseId = (Long) data;
            RestStorehouse storehouse = restStorehouseService.getById(storehouseId);
            if(ToolUtil.isNotEmpty(storehouse.getCoding())){
                codingByModule =  codingByModule.replace("${storehouse}",storehouse.getCoding());
            }else {
                codingByModule = codingByModule.replace("${storehouse}", "");
            }
        }
        return codingByModule;

    }
}
