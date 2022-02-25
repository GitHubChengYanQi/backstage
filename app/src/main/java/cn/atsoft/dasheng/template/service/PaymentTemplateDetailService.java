package cn.atsoft.dasheng.template.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplateDetail;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateDetailParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 付款模板详情 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
public interface PaymentTemplateDetailService extends IService<PaymentTemplateDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-24
     */
    void add(PaymentTemplateDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-24
     */
    void delete(PaymentTemplateDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-24
     */
    void update(PaymentTemplateDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
    PaymentTemplateDetailResult findBySpec(PaymentTemplateDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
    List<PaymentTemplateDetailResult> findListBySpec(PaymentTemplateDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
     PageInfo<PaymentTemplateDetailResult> findPageBySpec(PaymentTemplateDetailParam param);

    List<PaymentTemplateDetailResult> getDetails(Long id);

    void addList(Long id, List<PaymentTemplateDetailParam> params);
}
