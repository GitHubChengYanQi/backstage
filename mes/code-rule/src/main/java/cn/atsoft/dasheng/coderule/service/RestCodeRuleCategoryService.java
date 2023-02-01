package cn.atsoft.dasheng.coderule.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodeRuleCategory;
import cn.atsoft.dasheng.coderule.model.params.RestCodeRuleCategoryParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodeRuleCategoryResult;
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
public interface RestCodeRuleCategoryService extends IService<RestCodeRuleCategory> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(RestCodeRuleCategoryParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-22
     */
    void delete(RestCodeRuleCategoryParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-22
     */
    void update(RestCodeRuleCategoryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    RestCodeRuleCategoryResult findBySpec(RestCodeRuleCategoryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    List<RestCodeRuleCategoryResult> findListBySpec(RestCodeRuleCategoryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
     PageInfo<RestCodeRuleCategoryResult> findPageBySpec(RestCodeRuleCategoryParam param);

}
