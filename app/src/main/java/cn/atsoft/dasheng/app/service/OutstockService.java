package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 出库表 服务类
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
public interface OutstockService extends IService<Outstock> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-07-17
     * @return
     */
    Long add(OutstockParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-07-17
     */
    void delete(OutstockParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-07-17
     */
    void update(OutstockParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
    OutstockResult findBySpec(OutstockParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
    List<OutstockResult> findListBySpec(OutstockParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
     PageInfo findPageBySpec(OutstockParam param, DataScope dataScope );

    OutstockResult detail (Long id);



}
