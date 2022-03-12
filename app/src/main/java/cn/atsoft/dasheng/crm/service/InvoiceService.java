package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.model.params.InvoiceParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商开票 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
public interface InvoiceService extends IService<Invoice> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-20
     */
    Invoice add(InvoiceParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-20
     */
    void delete(InvoiceParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-20
     */
    void update(InvoiceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    InvoiceResult findBySpec(InvoiceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    List<InvoiceResult> findListBySpec(InvoiceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    PageInfo<InvoiceResult> findPageBySpec(InvoiceParam param);

    List<InvoiceResult> getDetails(List<Long> ids);

    List<InvoiceResult> getDetailsByCustomerIds(List<Long> ids);
}
