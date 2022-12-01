package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.InvoiceBill;
import cn.atsoft.dasheng.crm.model.params.InvoiceBillParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceBillResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发票 服务类
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
public interface InvoiceBillService extends IService<InvoiceBill> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-04-22
     */
    InvoiceBill add(InvoiceBillParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-04-22
     */
    void delete(InvoiceBillParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-04-22
     */
    void update(InvoiceBillParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-04-22
     */
    InvoiceBillResult findBySpec(InvoiceBillParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-04-22
     */
    List<InvoiceBillResult> findListBySpec(InvoiceBillParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-04-22
     */
     PageInfo<InvoiceBillResult> findPageBySpec(InvoiceBillParam param);

}
