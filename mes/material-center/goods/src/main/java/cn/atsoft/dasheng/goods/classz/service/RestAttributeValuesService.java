package cn.atsoft.dasheng.goods.classz.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeValuesResult;
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
public interface RestAttributeValuesService extends IService<RestAttributeValues> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-18
     */
    Long add(RestAttributeValuesParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-18
     */
    void delete(RestAttributeValuesParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-18
     */
    void update(RestAttributeValuesParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    RestAttributeValuesResult findBySpec(RestAttributeValuesParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
    List<RestAttributeValuesResult> findListBySpec(RestAttributeValuesParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-18
     */
     PageInfo<RestAttributeValuesResult> findPageBySpec(RestAttributeValuesParam param);

     List<RestAttributeValues> restAttributeValuesByAttributeId(List<Long> attributeId);

}
