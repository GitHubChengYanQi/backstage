package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.RulesRelation;
import cn.atsoft.dasheng.erp.model.params.RulesRelationParam;
import cn.atsoft.dasheng.erp.model.result.RulesRelationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 编码规则和模块的对应关系 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
public interface RulesRelationService extends IService<RulesRelation> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-25
     */
    void add(RulesRelationParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-25
     */
    void delete(RulesRelationParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-25
     */
    void update(RulesRelationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
    RulesRelationResult findBySpec(RulesRelationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
    List<RulesRelationResult> findListBySpec(RulesRelationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
     PageInfo<RulesRelationResult> findPageBySpec(RulesRelationParam param);

}
