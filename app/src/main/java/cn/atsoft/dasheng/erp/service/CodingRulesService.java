package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.model.params.CodingRulesParam;
import cn.atsoft.dasheng.erp.model.result.CodingRulesResult;
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
public interface CodingRulesService extends IService<CodingRules> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(CodingRulesParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-22
     */
    void delete(CodingRulesParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-22
     */
    void update(CodingRulesParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    CodingRulesResult findBySpec(CodingRulesParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    List<CodingRulesResult> findListBySpec(CodingRulesParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    PageInfo<CodingRulesResult> findPageBySpec(CodingRulesParam param);

    /**
     * 自动生成编码
     *
     * @param ids
     * @return
     */
    String backCoding(Long ids);

    String backSkuCoding(Long ids,Long spuId);

    /**
     * 通过模块获取编码
     * @param module
     * @return
     */
    String getCodingByModule(Long module);

    String defaultEncoding();

    String encoding(int module);
}
