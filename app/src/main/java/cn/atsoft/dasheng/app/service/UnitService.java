package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.UnitParam;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
public interface UnitService extends IService<Unit> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Long add(UnitParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void delete(UnitParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void update(UnitParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    UnitResult findBySpec(UnitParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<UnitResult> findListBySpec(UnitParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
     PageInfo findPageBySpec(UnitParam param, DataScope dataScope );

}
