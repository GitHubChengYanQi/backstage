package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.mapper.OrderMapper;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import cn.atsoft.dasheng.crm.service.BankService;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.PaymentService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
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
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private BankService bankService;

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
        String type = null;
        String source = null;
        switch (param.getType()) {
            case 1:
                type = "purchaseAsk";
                source = "采购";
                break;
            case 2:
                type = "销售申请";
                source = "销售申请";
                break;
            default:
        }

        //发起审批流程
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", param.getProcessType()).eq("status", 99).eq("module", "purchaseAsk").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            qualityTaskService.power(activitiProcess);//检查创建权限
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的" + source + "申请");
//            activitiProcessTaskParam.setQTaskId(entity.getOrderId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getOrderId());
            activitiProcessTaskParam.setType(type);
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            activitiProcessLogService.addLogJudgeBranch(activitiProcess.getProcessId(), taskId, entity.getOrderId(), type);
            activitiProcessLogService.autoAudit(taskId, 1);
        } else {
//            entity.setStatus(99);
//            this.updateById(entity);
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

    private void detailFormat(OrderDetailResult result) {
         contactsService.getById(result.)
    }
}
