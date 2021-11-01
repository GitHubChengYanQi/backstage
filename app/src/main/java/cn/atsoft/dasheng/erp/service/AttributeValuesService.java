package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品属性数据表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface AttributeValuesService extends IService<AttributeValues> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-18
     */
    Long add(AttributeValuesParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-18
     */
    void delete(AttributeValuesParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-18
     */
    void update(AttributeValuesParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    AttributeValuesResult findBySpec(AttributeValuesParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    List<AttributeValuesResult> findListBySpec(AttributeValuesParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
     PageInfo<AttributeValuesResult> findPageBySpec(AttributeValuesParam param);

}
