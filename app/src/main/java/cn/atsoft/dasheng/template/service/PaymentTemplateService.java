package cn.atsoft.dasheng.template.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplate;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 付款模板 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
public interface PaymentTemplateService extends IService<PaymentTemplate> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-24
     */
    void add(PaymentTemplateParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-24
     */
    void delete(PaymentTemplateParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-24
     */
    void update(PaymentTemplateParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
    PaymentTemplateResult findBySpec(PaymentTemplateParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
    List<PaymentTemplateResult> findListBySpec(PaymentTemplateParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
     PageInfo<PaymentTemplateResult> findPageBySpec(PaymentTemplateParam param);

    PaymentTemplateResult detail(Long id);
}
