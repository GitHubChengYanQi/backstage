package cn.atsoft.dasheng.goods.config;


import cn.atsoft.dasheng.codeRule.model.RestCode;
import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration

public class GoodsConfig {

    @Autowired
    private RestCodeRuleCategoryService restCodeRuleCategoryService;

    @Bean
    public void goodsRule() {
        RestCodeRulesCategory skuRule = new RestCodeRulesCategory() {{
            setModule("sku");
            setName("物料");
            ArrayList<RestCode> skuRoleList = new ArrayList<RestCode>() {{
                add(new RestCode() {{
                    setLabel("分类码");
                    setValue("${skuClass}");
                }});
                add(new RestCode() {{
                    setLabel("产品码");
                    setValue("${spuCoding}");
                }});
            }};
//            skuRoleList.addAll(generalRoule);
            setRuleList(skuRoleList);
        }};
        restCodeRuleCategoryService.add(skuRule);

    }
}
