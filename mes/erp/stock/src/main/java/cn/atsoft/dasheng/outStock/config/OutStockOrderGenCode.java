package cn.atsoft.dasheng.outStock.config;

import cn.atsoft.dasheng.codeRule.model.GenCodeInterface;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.atsoft.dasheng.codeRule.model.enums.CodingRuleFiledEnum.skuClass;
@Service

public class OutStockOrderGenCode implements GenCodeInterface {
    @Autowired
    private RestCategoryService categoryService;
    @Autowired
    private RestCodeRuleService codeRuleService;

    @Override
    public String genCode(String modelName,Object data) {
        return codeRuleService.getCodingByModule(modelName);
    }
}
