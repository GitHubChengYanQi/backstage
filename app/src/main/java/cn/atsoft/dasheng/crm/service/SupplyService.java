package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.model.params.SupplyParam;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商供应物料 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
public interface SupplyService extends IService<Supply> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-20
     */
    void add(SupplyParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-20
     */
    void delete(SupplyParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-20
     */
    void update(SupplyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    SupplyResult findBySpec(SupplyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    List<SupplyResult> findListBySpec(SupplyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    PageInfo<SupplyResult> findPageBySpec(SupplyParam param);


    void addList(List<SupplyParam> supplyParams, Long customerId);

    List<SupplyResult> detail(Long customerId);

    List<SupplyResult> getListByCustomerId(Long customerId);



}
