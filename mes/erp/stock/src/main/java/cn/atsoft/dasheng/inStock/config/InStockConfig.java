package cn.atsoft.dasheng.inStock.config;


import cn.atsoft.dasheng.codeRule.model.RestCode;
import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration

public class InStockConfig {

    @Autowired
    private RestCodeRuleCategoryService restCodeRuleCategoryService;

    @Bean
    public void inStockOrderRule() {
        RestCodeRulesCategory skuRule = new RestCodeRulesCategory() {{
            setModule("inStockOrder");
            setName("入库单");
            ArrayList<RestCode> skuRoleList = new ArrayList<RestCode>() {{
                add(new RestCode() {{
                    setLabel("仓库码");
                    setValue("${storehouse}");
                }});
            }};
//            skuRoleList.addAll(generalRoule);
            setRuleList(skuRoleList);
        }};
        restCodeRuleCategoryService.add(skuRule);

    }
}
