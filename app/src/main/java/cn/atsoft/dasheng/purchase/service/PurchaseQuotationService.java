package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseQuotation;
import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;
import cn.atsoft.dasheng.purchase.pojo.QuotationParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购报价表单 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-22
 */
public interface PurchaseQuotationService extends IService<PurchaseQuotation> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    void add(PurchaseQuotationParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    void delete(PurchaseQuotationParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    void update(PurchaseQuotationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    PurchaseQuotationResult findBySpec(PurchaseQuotationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    List<PurchaseQuotationResult> findListBySpec(PurchaseQuotationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    PageInfo<PurchaseQuotationResult> findPageBySpec(PurchaseQuotationParam param);


    void addList(QuotationParam param);

}
