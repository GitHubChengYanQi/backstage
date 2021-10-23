package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRulesClassification;
import cn.atsoft.dasheng.erp.model.params.CodingRulesClassificationParam;
import cn.atsoft.dasheng.erp.model.result.CodingRulesClassificationResult;
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
public interface CodingRulesClassificationService extends IService<CodingRulesClassification> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(CodingRulesClassificationParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-22
     */
    void delete(CodingRulesClassificationParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-22
     */
    void update(CodingRulesClassificationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    CodingRulesClassificationResult findBySpec(CodingRulesClassificationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    List<CodingRulesClassificationResult> findListBySpec(CodingRulesClassificationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
     PageInfo<CodingRulesClassificationResult> findPageBySpec(CodingRulesClassificationParam param);

}
