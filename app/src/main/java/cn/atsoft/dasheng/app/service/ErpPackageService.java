package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackage;
import cn.atsoft.dasheng.app.model.params.ErpPackageParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐表	 服务类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
public interface ErpPackageService extends IService<ErpPackage> {

    /**
     * 新增
     *
     * @author qr
     * @Date 2021-08-04
     */
    Long add(ErpPackageParam param);

    /**
     * 删除
     *
     * @author qr
     * @Date 2021-08-04
     */
    void delete(ErpPackageParam param);
    /**
     * 批量删除
     */
    void batchDelete(List<Long> packageIds);
    /**
     * 更新
     *
     * @author qr
     * @Date 2021-08-04
     */
    void update(ErpPackageParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
    ErpPackageResult findBySpec(ErpPackageParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<ErpPackageResult> findListBySpec(ErpPackageParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
     PageInfo findPageBySpec(ErpPackageParam param, DataScope dataScope );

}
