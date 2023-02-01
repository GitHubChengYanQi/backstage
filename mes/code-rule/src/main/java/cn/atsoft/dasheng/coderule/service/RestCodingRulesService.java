package cn.atsoft.dasheng.coderule.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodingRules;
import cn.atsoft.dasheng.coderule.model.params.RestCodingRulesParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodingRulesResult;
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
public interface RestCodingRulesService extends IService<RestCodingRules> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-22
     */
    void add(RestCodingRulesParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-22
     */
    void delete(RestCodingRulesParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-22
     */
    void update(RestCodingRulesParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    RestCodingRulesResult findBySpec(RestCodingRulesParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    List<RestCodingRulesResult> findListBySpec(RestCodingRulesParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-22
     */
    PageInfo<RestCodingRulesResult> findPageBySpec(RestCodingRulesParam param);

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
