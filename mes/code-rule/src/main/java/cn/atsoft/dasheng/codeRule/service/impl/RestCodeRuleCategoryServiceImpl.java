package cn.atsoft.dasheng.codeRule.service.impl;


import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 编码规则分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Service
public class RestCodeRuleCategoryServiceImpl implements RestCodeRuleCategoryService {

    private List<RestCodeRulesCategory> categoryList = new ArrayList<>();

    @Override
    public void add(RestCodeRulesCategory param){
        this.categoryList.add(param);
    }

    @Override
    public List<RestCodeRulesCategory> get() {
        return this.categoryList;
    }

}
