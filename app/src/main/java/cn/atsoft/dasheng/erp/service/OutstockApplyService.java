package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 出库申请 服务类
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
public interface OutstockApplyService extends IService<OutstockApply> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-09-14
     */
    void add(OutstockApplyParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-09-14
     */
    void delete(OutstockApplyParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-09-14
     */
    void update(OutstockApplyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-09-14
     */
    OutstockApplyResult findBySpec(OutstockApplyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-09-14
     */
    List<OutstockApplyResult> findListBySpec(OutstockApplyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-09-14
     */
     PageInfo<OutstockApplyResult> findPageBySpec(OutstockApplyParam param);

}
