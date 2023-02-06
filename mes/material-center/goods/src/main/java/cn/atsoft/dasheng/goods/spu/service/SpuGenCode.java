package cn.atsoft.dasheng.goods.spu.service;

import cn.atsoft.dasheng.codeRule.model.GenCodeInterface;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.classz.service.RestClassService;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuGenCode implements GenCodeInterface {
    @Autowired
    private RestCategoryService categoryService;
    @Autowired
    private RestCodeRuleService codeRuleService;
    @Autowired
    private RestClassService classService;

    @Override
    public String genCode(String modelName,Object data) {

        String codingByModule = codeRuleService.getCodingByModule(modelName);

        return codingByModule;
    }
}
