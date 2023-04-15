package cn.atsoft.dasheng.production.card.config;

import cn.atsoft.dasheng.codeRule.model.RestCode;
import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
@Configuration
public class cardConfig {
    @Autowired
    private RestCodeRuleCategoryService restCodeRuleCategoryService;

    @Bean
    public void cardRule() {
        RestCodeRulesCategory cardRule = new RestCodeRulesCategory() {{
            setModule("card");
            setName("生产卡片");
            ArrayList<RestCode> cardRuleList = new ArrayList<RestCode>() {{
//                add(new RestCode() {{
//                    setLabel("分类码");
//                    setValue("${skuClass}");
//                }});
//                add(new RestCode() {{
//                    setLabel("产品码");
//                    setValue("${spuCoding}");
//                }});
            }};
//            skuRoleList.addAll(generalRoule);
            setRuleList(cardRuleList);
        }};
        restCodeRuleCategoryService.add(cardRule);

    }
}
