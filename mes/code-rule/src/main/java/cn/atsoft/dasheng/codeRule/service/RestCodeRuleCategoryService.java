package cn.atsoft.dasheng.codeRule.service;

import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;

import java.util.List;

/**
 * <p>
 * 编码规则分类 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
public interface RestCodeRuleCategoryService {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(RestCodeRulesCategory param);

    List<RestCodeRulesCategory> get();


}
