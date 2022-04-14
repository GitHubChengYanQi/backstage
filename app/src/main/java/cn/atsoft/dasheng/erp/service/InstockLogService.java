package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockLog;
import cn.atsoft.dasheng.erp.model.params.InstockLogParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库记录表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
public interface InstockLogService extends IService<InstockLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    void add(InstockLogParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    void delete(InstockLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    void update(InstockLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    InstockLogResult findBySpec(InstockLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    List<InstockLogResult> findListBySpec(InstockLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
     PageInfo<InstockLogResult> findPageBySpec(InstockLogParam param);

    List<InstockLogResult> listByInstockOrderId(Long id);
}
