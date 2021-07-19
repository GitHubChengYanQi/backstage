package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Quotation;
import cn.atsoft.dasheng.app.model.params.QuotationParam;
import cn.atsoft.dasheng.app.model.result.QuotationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报价表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
public interface QuotationService extends IService<Quotation> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void add(QuotationParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void delete(QuotationParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-07-19
     */
    void update(QuotationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
    QuotationResult findBySpec(QuotationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<QuotationResult> findListBySpec(QuotationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-07-19
     */
     PageInfo<QuotationResult> findPageBySpec(QuotationParam param);

}
