package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.orCode.model.result.InKindRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 出库清单 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
public interface OutstockListingService extends IService<OutstockListing> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-09-15
     */
    void add(OutstockListingParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-09-15
     */
    void delete(OutstockListingParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-09-15
     */
    void update(OutstockListingParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-09-15
     */
    OutstockListingResult findBySpec(OutstockListingParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-09-15
     */
    List<OutstockListingResult> findListBySpec(OutstockListingParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-09-15
     */
    PageInfo<OutstockListingResult> findPageBySpec(OutstockListingParam param);


    List<OutstockListingResult> getDetailsByOrderId(Long id);

    OutstockListing getEntity(OutstockListingParam param);
}
