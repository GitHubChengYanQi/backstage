package cn.atsoft.dasheng.codeRule.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.codeRule.entity.RestCodeRule;
import cn.atsoft.dasheng.codeRule.model.params.RestCodeRuleParam;
import cn.atsoft.dasheng.codeRule.model.result.RestCodeRuleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 编码规则 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
public interface RestCodeRuleService extends IService<RestCodeRule> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(RestCodeRuleParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-22
     */
    void delete(RestCodeRuleParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-22
     */
    void update(RestCodeRuleParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    RestCodeRuleResult findBySpec(RestCodeRuleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    List<RestCodeRuleResult> findListBySpec(RestCodeRuleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    PageInfo<RestCodeRuleResult> findPageBySpec(RestCodeRuleParam param);

    /**
     * 自动生成编码
     *
     * @param ids
     * @return
     */
    String backCoding(Long ids);

    String backCoding(Long ids,Long spuId);

    /**
     * 通过模块获取编码
     * @param module
     * @return
     */
    String getCodingByModule(Long module);

    String defaultEncoding();

    String encoding(int module);
}
