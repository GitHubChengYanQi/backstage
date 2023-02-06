package cn.atsoft.dasheng.goods.sku.service;

import cn.atsoft.dasheng.codeRule.model.GenCodeInterface;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.atsoft.dasheng.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.atsoft.dasheng.codeRule.model.enums.CodingRuleFiledEnum.skuClass;

@Service
public class SkuGenCode implements GenCodeInterface {
    @Autowired
    private RestCategoryService categoryService;
    @Autowired
    private RestCodeRuleService codeRuleService;

    @Override
    public String genCode(String modelName,Object data) {
        RestSkuParam param = (RestSkuParam) data;

        String codingByModule = codeRuleService.getCodingByModule(modelName);

        RestCategory classification = categoryService.getById(param.getSpuClassificationId());

        if (ToolUtil.isNotEmpty(classification) && classification.getDisplay() != 0) {
            if (ToolUtil.isNotEmpty(classification.getCodingClass())) {
                codingByModule = codingByModule.replace(skuClass.getValue(), classification.getCodingClass());
            } else {
                codingByModule = codingByModule.replace(skuClass.getValue(), "");
            }
        }
        return codingByModule;
    }
}
