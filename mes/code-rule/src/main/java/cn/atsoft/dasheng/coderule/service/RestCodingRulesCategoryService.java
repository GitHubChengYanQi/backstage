package cn.atsoft.dasheng.coderule.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodingRulesCategory;
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
public interface RestCodingRulesCategoryService extends IService<RestCodingRulesCategory> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(RestCodingRulesCategoryParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-22
     */
    void delete(RestCodingRulesCategoryParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-22
     */
    void update(RestCodingRulesCategoryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    RestCodingRulesCategoryResult findBySpec(RestCodingRulesCategoryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    List<RestCodingRulesCategoryResult> findListBySpec(RestCodingRulesCategoryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
     PageInfo<RestCodingRulesCategoryResult> findPageBySpec(RestCodingRulesCategoryParam param);

}
