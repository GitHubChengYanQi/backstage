package cn.atsoft.dasheng.coderule.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.coderule.model.params.RestCodingRulesCategoryParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodingRulesCategoryResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 编码规则分类 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
public interface RestCodingRulesCategoryService {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(RestCodeRulesCategory param);


}
