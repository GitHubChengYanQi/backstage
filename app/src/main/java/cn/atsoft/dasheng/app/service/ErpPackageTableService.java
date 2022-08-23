package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.model.params.ErpPackageTableParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageTableResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐分表 服务类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
public interface ErpPackageTableService extends IService<ErpPackageTable> {

    /**
     * 新增
     *
     * @author qr
     * @Date 2021-08-04
     */
    void add(ErpPackageTableParam param);

    /**
     * 删除
     *
     * @author qr
     * @Date 2021-08-04
     */
    void delete(ErpPackageTableParam param);

    /**
     * 更新
     *
     * @author qr
     * @Date 2021-08-04
     */
    void update(ErpPackageTableParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
    ErpPackageTableResult findBySpec(ErpPackageTableParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<ErpPackageTableResult> findListBySpec(ErpPackageTableParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author qr
     * @Date 2021-08-04
     */
     PageInfo findPageBySpec(ErpPackageTableParam param, DataScope dataScope );

     void batchAdd(BusinessDetailedParam param);

}
