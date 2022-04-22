package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.Excel.pojo.ContractLabel;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
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
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.DateTime;
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

        if (param.getGenerateContract() == 1) {   //创建合同
            Contract contract = contractService.orderAddContract(entity.getOrderId(), param.getContractParam(), param, orderType);
            entity.setContractId(contract.getContractId());
            if (ToolUtil.isNotEmpty(contract.getContractId())) {
                entity.setContractId(contract.getContractId());
            }
        }

        param.getPaymentParam().setOrderId(entity.getOrderId());
        supplyService.OrdersBackfill(param.getSellerId(), param.getDetailParams());  //回填
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
//            activitiProcessTaskParam.setTaskName(user.getName() + "发起的" + source + "申请");
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
        ContractResult detail = contractService.detail(contractId);

        OrderResult orderResult = this.getDetail(detail.getSourceId());

        Map<String, Object> map = new HashMap<>();

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
                    if (ToolUtil.isNotEmpty(orderResult.getAcontacts()) && ToolUtil.isNotEmpty(orderResult.getAcontacts().getContactsName())) {
                        map.put(ContractEnum.pickUpMan.getDetail(), orderResult.getAcontacts().getContactsName());
                    } else {
                        map.put(ContractEnum.pickUpMan.getDetail(), orderResult.getAcontacts().getContactsName());
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
                        map.put(ContractEnum.ABankAccount.getDetail(), orderResult.getPartyABankAccount());
                    } else {
                        map.put(ContractEnum.ABankAccount.getDetail(), "");
                    }
                    break;
                case BBankAccount:
                    map.put(ContractEnum.BBankAccount.getDetail(), orderResult.getPartyBBankAccount());
                    break;
                case TotalAmountInFigures:
                    map.put(ContractEnum.TotalAmountInFigures.getDetail(), orderResult.getAllMoney());
                    break;
                case TotalAmountInWords:
                    int money = Math.toIntExact(orderResult.getAllMoney());
                    String format = NumberChineseFormatter.format(money, true, true);
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
                    map.put(ContractEnum.AEin.getDetail(), "");
                    break;
                case BEin:
                    map.put(ContractEnum.BEin.getDetail(), "");
                    break;
                case DeliveryAddress:
                    map.put(ContractEnum.DeliveryAddress.getDetail(), "");
                    break;
                case TotalNumber:
                    map.put(ContractEnum.TotalNumber.getDetail(), orderResult.getTotalNumber());
                    break;
                case ALegalMan:
                    map.put(ContractEnum.ALegalMan.getDetail(), "");
                    break;
                case BLegalMan:
                    map.put(ContractEnum.BLegalMan.getDetail(), "");
                    break;
                case pickUpManPhone:
                    map.put(ContractEnum.pickUpManPhone.getDetail(),orderResult.getAContactsPhone() );
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
        long allMoney = 0L;
        long totalNumber = 0L;
        for (OrderDetailResult detail : details) {
            totalNumber = totalNumber + detail.getPurchaseNumber();
            long money = detail.getOnePrice() * detail.getPurchaseNumber();
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

        Bank Abank = bankService.getById(result.getPartyABankId()); //甲方银行
        Bank Bbank = bankService.getById(result.getPartyBBankId());//乙方银行
        result.setAbank(Abank);
        result.setBbank(Bbank);

        Adress Aadress = adressService.getById(result.getPartyAAdressId());//甲方地址
        Adress Badress = adressService.getById(result.getPartyBAdressId());//乙方地址
        result.setAadress(Aadress);
        result.setBadress(Badress);

        Phone Aphone = phoneService.getById(result.getPartyAPhone());//甲方电话；
        Phone Bphone = phoneService.getById(result.getPartyBPhone());//乙方电话
        result.setAphone(Aphone);
        result.setBphone(Bphone);

        Customer Acustomer = customerService.getById(result.getBuyerId());//甲方;
        Customer Bcustomer = customerService.getById(result.getSellerId());//乙方

        result.setAcustomer(Acustomer);
        result.setBcustomer(Bcustomer);

        Phone AcompanyPhone = ToolUtil.isEmpty(result.getPartyACompanyPhone()) ? new Phone() : phoneService.getById(result.getPartyACompanyPhone());  //甲方公司电话
        Phone BcompanyPhone = ToolUtil.isEmpty(result.getPartyBCompanyPhone()) ? new Phone() : phoneService.getById(result.getPartyBCompanyPhone());  //乙方公司电话

        if (ToolUtil.isNotEmpty(AcompanyPhone) &&ToolUtil.isNotEmpty(AcompanyPhone.getPhoneNumber())) {
            result.setACompanyPhone(AcompanyPhone.getPhoneNumber());
        }

        if (ToolUtil.isNotEmpty(BcompanyPhone) && ToolUtil.isNotEmpty(BcompanyPhone.getPhoneNumber())) {
            result.setBCompanyPhone(BcompanyPhone.getPhoneNumber());
        }

        Adress adress = ToolUtil.isEmpty(result.getAdressId()) ? new Adress() : adressService.getById(result.getAdressId());

        result.setAdress(adress);
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
        for (OrderResult datum : data) {
            customerIds.add(datum.getBuyerId());
            customerIds.add(datum.getSellerId());
            userIds.add(datum.getCreateUser());
        }

        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (OrderResult datum : data) {

            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(datum.getBuyerId())) {
                    datum.setAcustomer(customer);
                }
                if (customer.getCustomerId().equals(datum.getSellerId())) {
                    datum.setBcustomer(customer);
                }
            }

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
