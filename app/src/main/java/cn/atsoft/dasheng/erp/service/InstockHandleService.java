package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockHandle;
import cn.atsoft.dasheng.erp.model.params.InstockHandleParam;
import cn.atsoft.dasheng.erp.model.result.InstockHandleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库操作结果 服务类
 * </p>
 *
 * @author song
 * @since 2022-07-08
 */
public interface InstockHandleService extends IService<InstockHandle> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-07-08
     */
    void add(InstockHandleParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-07-08
     */
    void delete(InstockHandleParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-07-08
     */
    void update(InstockHandleParam param);

    List<InstockHandleResult> detailByInStockOrder(Long instockOrderId);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-07-08
     */
    InstockHandleResult findBySpec(InstockHandleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-07-08
     */
    List<InstockHandleResult> findListBySpec(InstockHandleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-07-08
     */
     PageInfo<InstockHandleResult> findPageBySpec(InstockHandleParam param);

    void format(List<InstockHandleResult> data);
}
