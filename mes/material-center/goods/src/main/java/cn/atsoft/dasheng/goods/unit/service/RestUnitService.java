package cn.atsoft.dasheng.goods.unit.service;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.model.params.RestUnitParam;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 单位表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
public interface RestUnitService extends IService<RestUnit> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void add(RestUnitParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void delete(RestUnitParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void update(RestUnitParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    RestUnitResult findBySpec(RestUnitParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<RestUnitResult> findListBySpec(RestUnitParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
     PageInfo findPageBySpec(RestUnitParam param, DataScope dataScope );

}
