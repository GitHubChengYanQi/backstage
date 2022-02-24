package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.mapper.OrderMapper;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.PaymentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderDetailService detailService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ContractService contractService;

    @Override
    @Transactional
    public void add(OrderParam param) {
        Order entity = getEntity(param);
        this.save(entity);

        detailService.addList(entity.getOrderId(), param.getDetailParams());

        paymentService.add(param.getPaymentParam());

        if (ToolUtil.isNotEmpty(param.getContractParam())) {
            contractService.orderAddContract(entity.getOrderId(), param.getContractParam());
        }


    }

    @Override
    public void delete(OrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrderParam param) {
        Order oldEntity = getOldEntity(param);
        Order newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderResult findBySpec(OrderParam param) {
        return null;
    }

    @Override
    public List<OrderResult> findListBySpec(OrderParam param) {
        return null;
    }

    @Override
    public PageInfo<OrderResult> findPageBySpec(OrderParam param) {
        Page<OrderResult> pageContext = getPageContext();
        IPage<OrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderParam param) {
        return param.getOrderId();
    }

    private Page<OrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Order getOldEntity(OrderParam param) {
        return this.getById(getKey(param));
    }

    private Order getEntity(OrderParam param) {
        Order entity = new Order();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public OrderResult getDetail(Long id) {
        Order order = this.getById(id);
        OrderResult orderResult = new OrderResult();
        ToolUtil.copyProperties(order, orderResult);

        PaymentResult paymentResult = paymentService.getDetail(orderResult.getOrderId());
        List<OrderDetailResult> details = detailService.getDetails(paymentResult.getOrderId());
        orderResult.setDetailResults(details);
        orderResult.setPaymentResult(paymentResult);
        return orderResult;
    }
}
