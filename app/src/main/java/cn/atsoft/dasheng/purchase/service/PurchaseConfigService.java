package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseConfig;
import cn.atsoft.dasheng.purchase.model.params.PurchaseConfigParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseConfigResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购配置表 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
public interface PurchaseConfigService extends IService<PurchaseConfig> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-21
     */
    void add(PurchaseConfigParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-21
     */
    void delete(PurchaseConfigParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-21
     */
    void update(PurchaseConfigParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    PurchaseConfigResult findBySpec(PurchaseConfigParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    List<PurchaseConfigResult> findListBySpec(PurchaseConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
     PageInfo<PurchaseConfigResult> findPageBySpec(PurchaseConfigParam param);

}
