package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.PaymentRecord;
import cn.atsoft.dasheng.crm.model.params.PaymentRecordParam;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 付款记录 服务类
 * </p>
 *
 * @author song
 * @since 2022-03-01
 */
public interface PaymentRecordService extends IService<PaymentRecord> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-03-01
     */
    PaymentRecord add(PaymentRecordParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-03-01
     */
    void delete(PaymentRecordParam param);

    /**
     * 状态作废
     */
    void obsolete(PaymentRecordParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-03-01
     */
    void update(PaymentRecordParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-03-01
     */
    PaymentRecordResult findBySpec(PaymentRecordParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-03-01
     */
    List<PaymentRecordResult> findListBySpec(PaymentRecordParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-03-01
     */
     PageInfo<PaymentRecordResult> findPageBySpec(PaymentRecordParam param);

    void format(List<PaymentRecordResult> results);
}
