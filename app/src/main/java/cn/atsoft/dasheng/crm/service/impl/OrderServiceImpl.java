package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.Excel.ContractExcel;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.*;
import cn.atsoft.dasheng.crm.mapper.OrderMapper;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.crm.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private BankService bankService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InvoiceService invoiceService;

    @Override
    @Transactional
    public Order add(OrderParam param) {
        Order entity = getEntity(param);

        String orderType = null;
        switch (param.getType()) {
            case 1:
                orderType = "采购";
                break;
            case 2:
                orderType = "销售";
                break;
        }


        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getGenerateContract())&&param.getGenerateContract() == 1) {   //创建合同
            Contract contract = contractService.orderAddContract(entity.getOrderId(), param.getContractParam(), param, orderType);
            entity.setContractId(contract.getContractId());
            if (ToolUtil.isNotEmpty(contract.getContractId())) {
                entity.setContractId(contract.getContractId());
            }
        }

        param.getPaymentParam().setOrderId(entity.getOrderId());
        supplyService.OrdersBackFill(param.getSellerId(), param.getDetailParams());  //回填
        detailService.addList(entity.getOrderId(), param.getSellerId(), param.getDetailParams());
        paymentService.add(param.getPaymentParam(), orderType);
        this.updateById(entity);
        return entity;


        //发起审批流程
//        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", param.getProcessType()).eq("status", 99).eq("module", "purchaseAsk").one();
//        if (ToolUtil.isNotEmpty(activitiProcess)) {
//            qualityTaskSers);//检查创建权限
////            LoginUser user = LoginContextHolder.getCvice.power(activitiProcesontext().getUser();
//            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
//            activitiProcessTaskParam.setTaskName(user.getName() + "提交的" + source + "申请");
////            activitiProcessTaskParam.setQTaskId(entity.getOrderId());
//            activitiProcessTaskParam.setUserId(param.getCreateUser());
//            activitiProcessTaskParam.setFormId(entity.getOrderId());
//            activitiProcessTaskParam.setType(type);
//            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
//            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
//            //添加小铃铛
//            wxCpSendTemplate.setSource("processTask");
//            wxCpSendTemplate.setSourceId(taskId);
//            //添加log
//            activitiProcessLogService.addLogJudgeBranch(activitiProcess.getProcessId(), taskId, entity.getOrderId(), type);
//            activitiProcessLogService.autoAudit(taskId, 1);
//        } else {
////            entity.setStatus(99);
////            this.updateById(entity);
//        }
    }

    @Override
    public void delete(OrderParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));

    }

    //    @Permission
    @Override
    public void updateContract(Long orderId, ContractParam contractParam) {
        Order order = this.getById(orderId);
        OrderParam param = new OrderParam();
        ToolUtil.copyProperties(order, param);

        String orderType = null;
        switch (param.getType()) {
            case 1:
                orderType = "采购";
                break;
            case 2:
                orderType = "销售";
                break;
        }

        Contract contract = contractService.orderAddContract(order.getOrderId(), contractParam, param, orderType);
        order.setContractId(contract.getContractId());
        if (ToolUtil.isNotEmpty(contract.getContractId())) {
            order.setContractId(contract.getContractId());
        }

        this.updateById(order);
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
        List<OrderResult> orderResults = this.baseMapper.customList(param);
        format(orderResults);
        return orderResults;
    }

    @Override
    public PageInfo<OrderResult> findPageBySpec(OrderParam param) {
        Page<OrderResult> pageContext = getPageContext();
        IPage<OrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
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
    public Map<String, Object> mapFormat(Long contractId) {
        ContractResult detail = contractService.detail(contractId);                         //合同表
        OrderResult orderResult = this.getDetail(detail.getSourceId());                     //订单表
        PaymentResult paymentResult = paymentService.getDetail(orderResult.getOrderId());  //付款主表
        Map<String, Object> map = new HashMap<>();
        String sign = detailService.getSign(orderResult.getOrderId());

        for (ContractEnum label : ContractEnum.values()) {
            switch (label) {
                case AContacts: //"需方委托代表":
                    if (ToolUtil.isNotEmpty(detail.getPartyAContacts()) && ToolUtil.isNotEmpty(detail.getPartyAContacts().getContactsName())) {
                        map.put(ContractEnum.AContacts.getDetail(), detail.getPartyAContacts().getContactsName());
                    } else {
                        map.put(ContractEnum.AContacts.getDetail(), "");
                    }
                    break;
                case BContacts: //供方委托代表
                    if (ToolUtil.isNotEmpty(detail.getPartyBContacts()) && ToolUtil.isNotEmpty(detail.getPartyBContacts().getContactsName())) {
                        map.put(ContractEnum.BContacts.getDetail(), detail.getPartyBContacts().getContactsName());
                    } else {
                        map.put(ContractEnum.BContacts.getDetail(), "");
                    }
                    break;
                case ACustomerAdress: //需方公司地址
                    if (ToolUtil.isNotEmpty(detail.getPartyAAdress()) && ToolUtil.isNotEmpty(detail.getPartyAAdress().getLocation())) {
                        map.put(ContractEnum.ACustomerAdress.getDetail(), detail.getPartyAAdress().getLocation());
                    } else {
                        map.put(ContractEnum.ACustomerAdress.getDetail(), "");
                    }
                    break;
                case BCustomerAdress:
                    if (ToolUtil.isNotEmpty(detail.getPartyBAdress()) && ToolUtil.isNotEmpty(detail.getPartyBAdress().getLocation())) {
                        map.put(ContractEnum.BCustomerAdress.getDetail(), detail.getPartyBAdress().getLocation());
                    } else {
                        map.put(ContractEnum.BCustomerAdress.getDetail(), "");
                    }
                    break;
                case ACustomerPhone:
                    if (ToolUtil.isNotEmpty(detail.getPhoneA()) && ToolUtil.isNotEmpty(detail.getPhoneA().getPhone())) {
                        map.put(ContractEnum.ACustomerPhone.getDetail(), detail.getPhoneA().getPhone());
                    } else {
                        map.put(ContractEnum.ACustomerPhone.getDetail(), "");
                    }
                    break;
                case BCustomerPhone:
                    if (ToolUtil.isNotEmpty(detail.getPhoneB()) && ToolUtil.isNotEmpty(detail.getPhoneB().getPhone())) {
                        map.put(ContractEnum.BCustomerPhone.getDetail(), detail.getPhoneB().getPhone());
                    } else {
                        map.put(ContractEnum.BCustomerPhone.getDetail(), "");
                    }
                    break;

                case extractPlace:
                    if (ToolUtil.isNotEmpty(orderResult.getAdress()) && ToolUtil.isNotEmpty(orderResult.getAdress().getLocation())) {
                        map.put(ContractEnum.extractPlace.getDetail(), orderResult.getAdress().getLocation());
                    } else {
                        map.put(ContractEnum.extractPlace.getDetail(), "");
                    }
                    break;
                case pickUpMan:
                    if (ToolUtil.isNotEmpty(orderResult.getDeliverer())) {
                        map.put(ContractEnum.pickUpMan.getDetail(), orderResult.getDeliverer().getContactsName());
                    } else {
                        map.put(ContractEnum.pickUpMan.getDetail(), "");
                    }
                    break;
                case ACustomerName:
                    if (ToolUtil.isNotEmpty(detail.getPartA()) && ToolUtil.isNotEmpty(detail.getPartA().getCustomerName())) {
                        map.put(ContractEnum.ACustomerName.getDetail(), detail.getPartA().getCustomerName());
                    } else {
                        map.put(ContractEnum.ACustomerName.getDetail(), "");
                    }
                    break;
                case BCustomerName:
                    if (ToolUtil.isNotEmpty(detail.getPartB()) && ToolUtil.isNotEmpty(detail.getPartB().getCustomerName())) {
                        map.put(ContractEnum.BCustomerName.getDetail(), detail.getPartB().getCustomerName());
                    } else {
                        map.put(ContractEnum.BCustomerName.getDetail(), "");
                    }
                    break;
                case ABank:
                    if (ToolUtil.isNotEmpty(orderResult.getAbank()) && ToolUtil.isNotEmpty(orderResult.getAbank().getBankName())) {
                        map.put(ContractEnum.ABank.getDetail(), orderResult.getAbank().getBankName());
                    } else {
                        map.put(ContractEnum.ABank.getDetail(), "");
                    }
                    break;
                case BBank:
                    if (ToolUtil.isNotEmpty(orderResult.getBbank()) && ToolUtil.isNotEmpty(orderResult.getBbank().getBankName())) {
                        map.put(ContractEnum.BBank.getDetail(), orderResult.getBbank().getBankName());
                    } else {
                        map.put(ContractEnum.BBank.getDetail(), "");
                    }
                    break;
                case ABankAccount:
                    if (ToolUtil.isNotEmpty(orderResult.getPartyABankAccount())) {
                        Invoice invoice = invoiceService.getById(orderResult.getPartyABankAccount());
                        map.put(ContractEnum.ABankAccount.getDetail(), invoice.getBankAccount());
                    } else {
                        map.put(ContractEnum.ABankAccount.getDetail(), "");
                    }
                    break;
                case BBankAccount:
                    if (ToolUtil.isNotEmpty(orderResult.getPartyBBankAccount())) {
                        Invoice invoice = invoiceService.getById(orderResult.getPartyBBankAccount());
                        map.put(ContractEnum.BBankAccount.getDetail(), invoice.getBankAccount());
                    } else {
                        map.put(ContractEnum.BBankAccount.getDetail(), "");
                    }
                    break;
                case TotalAmountInFigures:
                    map.put(ContractEnum.TotalAmountInFigures.getDetail(), sign + ContractExcel.priceReplace(orderResult.getAllMoney()));
                    break;
                case TotalAmountInWords:
                    int money = Math.toIntExact(orderResult.getAllMoney());
                    String format = NumberChineseFormatter.format(money, true, true);
                    format = format.replace("元", "圆");
                    map.put(ContractEnum.TotalAmountInWords.getDetail(), format);
                case ABankNo:
                    map.put(ContractEnum.ABankNo.getDetail(), orderResult.getPartyABankNo());
                    break;
                case BBankNo:
                    map.put(ContractEnum.BBankNo.getDetail(), orderResult.getPartyBBankNo());
                    break;
                case AFax:
                    if (ToolUtil.isNotEmpty(orderResult.getPartyAFax())) {
                        map.put(ContractEnum.AFax.getDetail(), orderResult.getPartyAFax());
                    } else {
                        map.put(ContractEnum.AFax.getDetail(), "");
                    }
                    break;
                case BFax:
                    if (ToolUtil.isNotEmpty(orderResult.getPartyBFax())) {
                        map.put(ContractEnum.BFax.getDetail(), orderResult.getPartyBFax());
                    } else {
                        map.put(ContractEnum.BFax.getDetail(), "");
                    }
                    break;
                case APhone:
                    map.put(ContractEnum.APhone.getDetail(), orderResult.getAContactsPhone());
                    break;
                case BPhone:
                    map.put(ContractEnum.BPhone.getDetail(), orderResult.getBContactsPhone());
                    break;
                case AEmail:
                    map.put(ContractEnum.AEmail.getDetail(), orderResult.getPartyAZipcode());
                    break;
                case BEmail:
                    map.put(ContractEnum.BEmail.getDetail(), orderResult.getPartyBZipcode());
                    break;
                case AEin:
                    if (ToolUtil.isEmpty(orderResult.getAcustomer().getUtscc())) {
                        map.put(ContractEnum.AEin.getDetail(), "");
                    } else {
                        map.put(ContractEnum.AEin.getDetail(), orderResult.getAcustomer().getUtscc());
                    }
                    break;
                case BEin:
                    if (ToolUtil.isEmpty(orderResult.getBcustomer().getUtscc())) {
                        map.put(ContractEnum.BEin.getDetail(), "");
                    } else {
                        map.put(ContractEnum.BEin.getDetail(), orderResult.getBcustomer().getUtscc());
                    }
                    break;
                case DeliveryAddress:
                    if (ToolUtil.isEmpty(orderResult.getAdress()) || ToolUtil.isEmpty(orderResult.getAdress().getLocation())) {
                        map.put(ContractEnum.DeliveryAddress.getDetail(), "");
                    } else {
                        map.put(ContractEnum.DeliveryAddress.getDetail(), orderResult.getAdress().getLocation());
                    }
                    break;
                case TotalNumber:
                    map.put(ContractEnum.TotalNumber.getDetail(), orderResult.getTotalNumber());
                    break;
                case ALegalMan:
                    if (ToolUtil.isEmpty(orderResult.getAcustomer().getLegal())) {
                        orderResult.getAcustomer().setLegal("");
                    }
                    map.put(ContractEnum.ALegalMan.getDetail(), orderResult.getAcustomer().getLegal());
                    break;
                case BLegalMan:
                    if (ToolUtil.isEmpty(orderResult.getBcustomer().getLegal())) {
                        orderResult.getBcustomer().setLegal("");
                    }
                    map.put(ContractEnum.BLegalMan.getDetail(), orderResult.getBcustomer().getLegal());
                    break;
                case pickUpManPhone:
                    map.put(ContractEnum.pickUpManPhone.getDetail(), orderResult.getAContactsPhone());
                    break;
                case contractCoding:
                    if (ToolUtil.isEmpty(detail.getCoding())) {
                        detail.setCoding("");
                    }
                    map.put(ContractEnum.contractCoding.getDetail(), detail.getCoding());
                    break;
//                case ASite:
//                    map.put(ContractEnum.ASite.getDetail(), "");
//                    break;
                case ATime:
                    String str = DateUtil.format(orderResult.getCreateTime(), "yyyy年MM月dd日");
                    map.put(ContractEnum.ATime.getDetail(), str);
                    break;
                case mailingAddress:
                    map.put(ContractEnum.mailingAddress.getDetail(), "");
                    break;
                case AZipCode:
                    if (orderResult.getAcustomer().getZipCode() == null) {
                        orderResult.getAcustomer().setZipCode("");
                    }
                    map.put(ContractEnum.AZipCode.getDetail(), orderResult.getAcustomer().getZipCode());
                    break;
                case BZipCode:
                    if (orderResult.getBcustomer().getZipCode() == null) {
                        orderResult.getBcustomer().setZipCode("");
                    }
                    map.put(ContractEnum.BZipCode.getDetail(), orderResult.getBcustomer().getZipCode());
                    break;
                case DeliveryCycle:
                    if (orderResult.getLeadTime() == null) {
                        orderResult.setLeadTime("");
                    }
                    map.put(ContractEnum.DeliveryCycle.getDetail(), orderResult.getLeadTime());
                    break;
                case floatingAmount:
                    Integer floatingAmount = paymentResult.getFloatingAmount();
                    if (ToolUtil.isEmpty(floatingAmount)) {
                        map.put(ContractEnum.floatingAmount.getDetail(), "");
                    } else {
                        map.put(ContractEnum.floatingAmount.getDetail(), floatingAmount);
                    }
                    break;
                case totalAmount:
                    Integer totalAmount = paymentResult.getTotalAmount();
                    if (ToolUtil.isEmpty(totalAmount)) {
                        map.put(ContractEnum.totalAmount.getDetail(), "");
                    } else {
                        map.put(ContractEnum.totalAmount.getDetail(), totalAmount);
                    }
                    break;
                case paperType:
                    Integer paperType = paymentResult.getPaperType();
                    if (ToolUtil.isEmpty(paperType)) {
                        map.put(ContractEnum.paperType.getDetail(), "");
                    } else {
                        String type = "";
                        switch (paperType) {
                            case 0:
                                type = "普票";
                                break;
                            case 1:
                                type = "专票";
                                break;
                        }
                        map.put(ContractEnum.paperType.getDetail(), type);
                    }
                    break;
                case rate:
                    Long rate = paymentResult.getRate();
                    if (ToolUtil.isEmpty(rate)) {
                        map.put(ContractEnum.rate.getDetail(), "");
                    } else {
                        map.put(ContractEnum.rate.getDetail(), rate);
                    }
                    break;
            }
        }

        return map;
    }


    @Override
    public OrderResult getDetail(Long id) {
        Order order = this.getById(id);
        OrderResult orderResult = new OrderResult();
        ToolUtil.copyProperties(order, orderResult);
        List<OrderDetailResult> details = new ArrayList<>();

        PaymentResult paymentResult = paymentService.getDetail(orderResult.getOrderId());
        if (ToolUtil.isNotEmpty(paymentResult) && ToolUtil.isNotEmpty(paymentResult.getOrderId())) {
            details = detailService.getDetails(paymentResult.getOrderId());
        }
        int allMoney = 0;
        int totalNumber = 0;
        for (OrderDetailResult detail : details) {
            totalNumber = totalNumber + detail.getPurchaseNumber();
            int money = detail.getOnePrice() * detail.getPurchaseNumber();
            allMoney = allMoney + money;
        }
        orderResult.setTotalNumber(totalNumber);
        orderResult.setAllMoney(allMoney);
        orderResult.setDetailResults(details);
        orderResult.setPaymentResult(paymentResult);

        detailFormat(orderResult);
        return orderResult;

    }

    private void detailFormat(OrderResult result) {
        Contacts Acontacts = contactsService.getById(result.getPartyAClientId());//甲方委托人
        Contacts Bcontacts = contactsService.getById(result.getPartyBClientId());//乙方联系人


        if (ToolUtil.isNotEmpty(Acontacts) && ToolUtil.isNotEmpty(Acontacts.getPhone())) {  //甲方代表电话
            Phone phone = phoneService.getById(Acontacts.getPhone());
            if (ToolUtil.isNotEmpty(phone)) {
                result.setAContactsPhone(phone.getPhoneNumber());
            }
        } else {
            Acontacts = new Contacts();
        }

        if (ToolUtil.isNotEmpty(Bcontacts) && ToolUtil.isNotEmpty(Bcontacts.getPhone())) {  //乙方代表电话
            Phone phone = phoneService.getById(Bcontacts.getPhone());
            if (ToolUtil.isNotEmpty(phone)) {
                result.setBContactsPhone(phone.getPhoneNumber());
            }
        } else {
            Bcontacts = new Contacts();
        }


        result.setAcontacts(Acontacts);
        result.setBcontacts(Bcontacts);

        Bank Abank = ToolUtil.isEmpty(result.getPartyABankId()) ? null : bankService.getById(result.getPartyABankId()); //甲方银行
        Bank Bbank = ToolUtil.isEmpty(result.getPartyBBankId()) ? null : bankService.getById(result.getPartyBBankId());//乙方银行
        result.setAbank(Abank);
        result.setBbank(Bbank);

        Adress Aadress = ToolUtil.isEmpty(result.getPartyAAdressId()) ? null : adressService.getById(result.getPartyAAdressId());//甲方地址
        Adress Badress = ToolUtil.isEmpty(result.getPartyBAdressId()) ? null : adressService.getById(result.getPartyBAdressId());//乙方地址
        result.setAadress(Aadress);
        result.setBadress(Badress);

        Phone Aphone = ToolUtil.isEmpty(result.getPartyAPhone()) ? null : phoneService.getById(result.getPartyAPhone());//甲方电话；
        Phone Bphone = ToolUtil.isEmpty(result.getPartyBPhone()) ? null : phoneService.getById(result.getPartyBPhone());//乙方电话

        if (ToolUtil.isNotEmpty(Aphone)) {
            result.setAphone(Aphone);
            result.setAContactsPhone(Aphone.getPhoneNumber());
        }
        if (ToolUtil.isNotEmpty(Bphone)) {
            result.setBphone(Bphone);
            result.setBContactsPhone(Bphone.getPhoneNumber());
        }


        Customer Acustomer = customerService.getById(result.getBuyerId());//甲方;
        Customer Bcustomer = customerService.getById(result.getSellerId());//乙方

        result.setAcustomer(Acustomer);
        result.setBcustomer(Bcustomer);

        Phone AcompanyPhone = ToolUtil.isEmpty(result.getPartyACompanyPhone()) ? new Phone() : phoneService.getById(result.getPartyACompanyPhone());  //甲方公司电话
        Phone BcompanyPhone = ToolUtil.isEmpty(result.getPartyBCompanyPhone()) ? new Phone() : phoneService.getById(result.getPartyBCompanyPhone());  //乙方公司电话

        if (ToolUtil.isNotEmpty(AcompanyPhone) && ToolUtil.isNotEmpty(AcompanyPhone.getPhoneNumber())) {
            result.setACompanyPhone(AcompanyPhone.getPhoneNumber());
        }
        if (ToolUtil.isNotEmpty(BcompanyPhone) && ToolUtil.isNotEmpty(BcompanyPhone.getPhoneNumber())) {
            result.setBCompanyPhone(BcompanyPhone.getPhoneNumber());
        }

        Adress adress = ToolUtil.isEmpty(result.getAdressId()) ? new Adress() : adressService.getById(result.getAdressId());  //交货地址
        result.setAdress(adress);

        Contacts contacts = contactsService.getById(result.getUserId());//交货人
        result.setDeliverer(contacts);

    }

    @Override
    public List<OrderResult> pendingProductionPlanByContracts(OrderParam orderParam) {
        List<Long> orderIds = new ArrayList<>();
        /**
         * 取出orderDetail 循环取出 orderId
         */
        OrderDetailParam orderDetailParam = new OrderDetailParam();
        orderDetailParam.setType(2);
        List<OrderDetailResult> orderDetailResults = detailService.getOrderDettailProductionIsNull(orderDetailParam);
        for (OrderDetailResult orderDetailResult : orderDetailResults) {
            orderIds.add(orderDetailResult.getOrderId());
        }

        /**
         * 查询Order
         */
        List<Order> orders = orderIds.size() == 0 ? new ArrayList<>() : this.query().in("order_Id", orderIds.stream().distinct().collect(Collectors.toList())).eq("type", 2).list();
        List<OrderResult> orderResults = new ArrayList<>();
        for (Order order : orders) {
            OrderResult orderResult = new OrderResult();
            ToolUtil.copyProperties(order, orderResult);
            orderResults.add(orderResult);
        }
        detailService.format(orderDetailResults);
        /**
         * 匹配order与orderDetail
         */
        for (OrderResult orderResult : orderResults) {
            orderIds.add(orderResult.getOrderId());
            List<OrderDetailResult> results = new ArrayList<>();
            for (OrderDetailResult orderDetailResult : orderDetailResults) {
                if (orderResult.getOrderId().equals(orderDetailResult.getOrderId())) {
                    results.add(orderDetailResult);
                }
            }
            orderResult.setDetailResults(results);
        }
        format(orderResults);
        return orderResults;
    }

    @Override
    public Set<ContractDetailSetRequest> pendingProductionPlan(OrderParam orderParam) {
        List<Long> orderIds = new ArrayList<>();
        /**
         * 取出orderDetail 循环取出 orderId
         */
        OrderDetailParam orderDetailParam = new OrderDetailParam();
        orderDetailParam.setType(2);
        List<OrderDetailResult> orderDetailResults = detailService.getOrderDettailProductionIsNull(orderDetailParam);
        for (OrderDetailResult orderDetailResult : orderDetailResults) {
            orderIds.add(orderDetailResult.getOrderId());
        }

        /*
         * 返回合并后的数据
         */
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        Set<ContractDetailSetRequest> contractDetailSet = new HashSet<>();
        for (OrderDetailResult orderDetail : orderDetailResults) {
            ContractDetailSetRequest request = new ContractDetailSetRequest();
            ToolUtil.copyProperties(orderDetail, request);
            skuIds.add(request.getSkuId());
            brandIds.add(request.getBrandId());
            contractDetailSet.add(request);
        }

        /*
          查询sku
         */
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);


        contractDetailSet = new HashSet<ContractDetailSetRequest>(contractDetailSet.stream().collect(Collectors.toMap(ContractDetailSetRequest::getSkuId, a -> a, (o1, o2) -> {
            if (ToolUtil.isEmpty(o1.getQuantity())) {
                o1.setQuantity(0L);
            }
            if (ToolUtil.isEmpty(o2.getQuantity())) {
                o2.setQuantity(0L);
            }
            o1.setQuantity(o1.getQuantity() + o2.getQuantity());
            return o1;

        })).values());


        this.detailService.format(orderDetailResults);


        for (ContractDetailSetRequest request : contractDetailSet) {
            Long quantity = 0L;
            List<OrderDetailResult> results = new ArrayList<>();
            for (OrderDetailResult orderDetailResult : orderDetailResults) {
                if (ToolUtil.isNotEmpty(orderDetailResult.getSkuId()) && ToolUtil.isNotEmpty(request.getSkuId()) && orderDetailResult.getSkuId().equals(request.getSkuId())) {
                    quantity += orderDetailResult.getPreordeNumber(); //fix 预购数量与采购数量
                    results.add(orderDetailResult);
                    request.setSkuId(orderDetailResult.getSkuId());
                    request.setBrandId(orderDetailResult.getBrandId());
                    request.setCustomerId(orderDetailResult.getCustomerId());
                }

            }
            request.setChildren(results);
            request.setQuantity(quantity);
            for (SkuResult skuResult : skuResults) {
                if (request.getSkuId().equals(skuResult.getSkuId())) {
                    request.setSkuResult(skuResult);
                }
            }
            BrandResult brandResult = new BrandResult();
            for (Brand brand : brands) {
                if (request.getBrandId().equals(brand.getBrandId())) {
                    ToolUtil.copyProperties(brand, brandResult);
                    request.setBrandResult(brandResult);
                }
            }
        }

        return contractDetailSet;
    }

    private void format(List<OrderResult> data) {
        List<Long> customerIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        for (OrderResult datum : data) {
            customerIds.add(datum.getBuyerId());
            customerIds.add(datum.getSellerId());
            userIds.add(datum.getCreateUser());
            orderIds.add(datum.getOrderId());
        }

        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        List<OrderDetail> orderDetails = orderIds.size() == 0 ? new ArrayList<>() : detailService.query().in("order_id", orderIds).eq("display", 1).list();
        List<OrderDetailResult> detailResults = BeanUtil.copyToList(orderDetails, OrderDetailResult.class, new CopyOptions());
        detailService.format(detailResults);


        for (OrderResult datum : data) {
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(datum.getBuyerId())) {
                    datum.setAcustomer(customer);
                }
                if (customer.getCustomerId().equals(datum.getSellerId())) {
                    datum.setBcustomer(customer);
                }
            }

            List<OrderDetailResult> orderDetailResults = new ArrayList<>();
            for (OrderDetailResult detailResult : detailResults) {
                if (detailResult.getOrderId().equals(datum.getOrderId())) {
                    orderDetailResults.add(detailResult);
                }
            }
            datum.setDetailResults(orderDetailResults);


            for (User user : userList) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUser(userResult);
                    break;
                }
            }
        }


    }
}
