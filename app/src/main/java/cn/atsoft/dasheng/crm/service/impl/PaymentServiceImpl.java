package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Payment;
import cn.atsoft.dasheng.crm.mapper.PaymentMapper;
import cn.atsoft.dasheng.crm.model.params.PaymentParam;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import cn.atsoft.dasheng.crm.service.PaymentDetailService;
import cn.atsoft.dasheng.crm.service.PaymentService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 付款信息表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    @Autowired
    private PaymentDetailService detailService;


    @Override
    public void add(PaymentParam param, String type) {

        Payment entity = getEntity(param);
        switch (type) {
            case "采购":
                param.setType(1);
                break;
            case "销售":
                param.setType(2);
                break;
        }
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
            detailService.addList(entity.getPaymentId(), param.getDetailParams());
        }
    }

    @Override
    public void delete(PaymentParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PaymentParam param) {
        Payment oldEntity = getOldEntity(param);
        Payment newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PaymentResult findBySpec(PaymentParam param) {
        return null;
    }

    @Override
    public List<PaymentResult> findListBySpec(PaymentParam param) {
        return null;
    }

    @Override
    public PageInfo<PaymentResult> findPageBySpec(PaymentParam param) {
        Page<PaymentResult> pageContext = getPageContext();
        IPage<PaymentResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PaymentParam param) {
        return param.getPaymentId();
    }

    private Page<PaymentResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Payment getOldEntity(PaymentParam param) {
        return this.getById(getKey(param));
    }

    private Payment getEntity(PaymentParam param) {
        Payment entity = new Payment();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 详情
     *
     * @param orderId
     * @return
     */
    @Override
    public PaymentResult getDetail(Long orderId) {
        if (ToolUtil.isEmpty(orderId)) {
            return null;
        }
        Payment payment = this.query().eq("order_id", orderId).one();
        if (ToolUtil.isEmpty(payment)) {
            return null;
        }
        PaymentResult paymentResult = new PaymentResult();
        ToolUtil.copyProperties(payment, paymentResult);

        List<PaymentDetailResult> serviceResults = detailService.getResults(paymentResult.getPaymentId());
        paymentResult.setDetailResults(serviceResults);

        return paymentResult;
    }
}
