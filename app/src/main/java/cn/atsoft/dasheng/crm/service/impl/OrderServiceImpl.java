package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.Excel.ContractExcel;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.crm.entity.*;
import cn.atsoft.dasheng.crm.mapper.OrderMapper;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.crm.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileUpload;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileUploadRequest;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpSpaceCreateData;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpSpaceCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private PaymentRecordService paymentRecordService;
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
    @Autowired
    private InvoiceBillService invoiceBillService;


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
        String coding = entity.getCoding();
        String theme = entity.getTheme();

        LambdaQueryChainWrapper<Order> orderLambdaQueryChainWrapper1 = this.lambdaQuery();
        orderLambdaQueryChainWrapper1.eq(Order::getCoding, coding);
        orderLambdaQueryChainWrapper1.eq(Order::getDisplay, 1);
        if (ToolUtil.isNotEmpty(param.getTenantId())) {
            orderLambdaQueryChainWrapper1.eq(Order::getTenantId, param.getTenantId());
        }else {
            orderLambdaQueryChainWrapper1.eq(Order::getTenantId, LoginContextHolder.getContext().getTenantId());
        }

        int codingCount = ToolUtil.isEmpty(coding) ? 0 : orderLambdaQueryChainWrapper1.count();
        LambdaQueryChainWrapper<Order> orderLambdaQueryChainWrapper = this.lambdaQuery();
        orderLambdaQueryChainWrapper.eq(Order::getTheme, theme).eq(Order::getDisplay, 1);
        if (ToolUtil.isNotEmpty(param.getTenantId())) {
            orderLambdaQueryChainWrapper.eq(Order::getTenantId, param.getTenantId());
        }else {
            orderLambdaQueryChainWrapper.eq(Order::getTenantId, LoginContextHolder.getContext().getTenantId());
        }
        int themeCount = ToolUtil.isEmpty(theme) ? 0 : orderLambdaQueryChainWrapper.count();

        if (codingCount > 0) {
            throw new ServiceException(500, "编码不可重复");
        }
        if (themeCount > 0) {
            throw new ServiceException(500, "主题不可重复");
        }


        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getGenerateContract()) && param.getGenerateContract() == 1) {   //创建合同
            Contract contract = contractService.orderAddContract(entity.getOrderId(), param.getContractParam(), param, orderType);
            entity.setContractId(contract.getContractId());
            if (ToolUtil.isNotEmpty(contract.getContractId())) {
                entity.setContractId(contract.getContractId());
            }
        }

        param.getPaymentParam().setOrderId(entity.getOrderId());
        supplyService.OrdersBackFill(param.getSellerId(), param.getDetailParams());  //回填
        long totalAmount = detailService.addList(entity.getOrderId(), param.getSellerId(), param.getDetailParams());    //返回添加所有物料总价
        if (ToolUtil.isNotEmpty(param.getPaymentParam()) && ToolUtil.isNotEmpty(param.getPaymentParam().getFloatingAmount())) {
            totalAmount = totalAmount + param.getPaymentParam().getFloatingAmount();
        }
        entity.setTotalAmount(totalAmount);  //订单总金额
        param.getPaymentParam().setTotalAmount(BigDecimal.valueOf(totalAmount).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN).doubleValue());
        BigDecimal subtract = BigDecimal.valueOf(totalAmount).subtract(BigDecimal.valueOf(param.getPaymentParam().getFloatingAmount()));
        param.getPaymentParam().setMoney(subtract.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN).doubleValue());
        paymentService.add(param.getPaymentParam(), orderType);
        this.updateById(entity);
        return entity;

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
    public Map<String, Object> getAllFile(OrderParam param) {
        Long orderId = param.getOrderId();
        List<PaymentRecord> paymentRecords = paymentRecordService.lambdaQuery().eq(PaymentRecord::getOrderId, orderId).isNotNull(PaymentRecord::getField).list();

        List<InvoiceBill> invoice = invoiceBillService.lambdaQuery().eq(InvoiceBill::getOrderId, orderId).isNotNull(InvoiceBill::getEnclosureId).list();

        Order order = this.getById(orderId);

        Map<String, Object> result = new HashMap<>();

        List<String> payment = paymentRecords.stream().map(PaymentRecord::getField).collect(Collectors.toList());

        List<String> invoiceFile = invoice.stream().map(InvoiceBill::getEnclosureId).collect(Collectors.toList());

        result.put("invoiceBillFiles", StringUtils.join(invoiceFile, ","));
        result.put("paymentRecordFiles", StringUtils.join(payment, ","));
        result.put("orderFiles", ToolUtil.isEmpty(order.getFileId()) ? "" : order.getFileId());
        return result;
    }

    @Override
    public OrderResult findBySpec(OrderParam param) {
        return null;
    }

    @Override
    public List<OrderResult> findListBySpec(OrderParam param, DataScope dataScope) {
        List<OrderResult> orderResults = this.baseMapper.customList(param,dataScope);
        format(orderResults);
        return orderResults;
    }

    @Override
    public PageInfo<OrderResult> findPageBySpec(OrderParam param, DataScope dataScope) {
        Page<OrderResult> pageContext = getPageContext();
        IPage<OrderResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderParam param) {
        return param.getOrderId();
    }

    private Page<OrderResult> getPageContext() {
        return PageFactory.defaultPage(new ArrayList<String>() {{
            add("coding");
            add("inStockRate");
            add("paymentRate");
            add("createTime");
            add("invoiceBillRate");
        }});
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
                        if (ToolUtil.isNotEmpty(invoice)) {
                            map.put(ContractEnum.ABankAccount.getDetail(), invoice.getBankAccount());
                        }
                    } else {
                        map.put(ContractEnum.ABankAccount.getDetail(), "");
                    }
                    break;
                case BBankAccount:
                    if (ToolUtil.isNotEmpty(orderResult.getPartyBBankAccount())) {
                        Invoice invoice = invoiceService.getById(orderResult.getPartyBBankAccount());
                        if (ToolUtil.isNotEmpty(invoice)){
                            map.put(ContractEnum.BBankAccount.getDetail(), invoice.getBankAccount());
                        }
                    } else {
                        map.put(ContractEnum.BBankAccount.getDetail(), "");
                    }
                    break;
                case TotalAmountInFigures:
                    map.put(ContractEnum.TotalAmountInFigures.getDetail(), sign + ContractExcel.priceReplace(orderResult.getAllMoney()));
                    break;
                case TotalAmountInWords:
                    double money = orderResult.getAllMoney();
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
                    if (ToolUtil.isNotEmpty(orderResult.getDeliverer())) {
                        QueryWrapper<Phone> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(Phone::getContactsId, orderResult.getDeliverer().getContactsId());
                        List<Phone> list = phoneService.list(queryWrapper);
                        if (ToolUtil.isEmpty(list.get(0)) || ToolUtil.isEmpty(list.get(0).getPhoneNumber())) {
                            map.put(ContractEnum.pickUpManPhone.getDetail(), "");
                        } else {
                            map.put(ContractEnum.pickUpManPhone.getDetail(), list.get(0).getPhoneNumber());
                        }
                    } else {
                        map.put(ContractEnum.pickUpManPhone.getDetail(), "");
                    }
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
                case DeliveryDate:
                    if (orderResult.getDeliveryDate() == null) {

                    }
                    map.put(ContractEnum.DeliveryDate.getDetail(), DateUtil.format(orderResult.getDeliveryDate(), "yyyy年MM月dd日 "));
                    break;
                case floatingAmount:
                    Double floatingAmount = paymentResult.getFloatingAmount();
                    if (ToolUtil.isEmpty(floatingAmount)) {
                        map.put(ContractEnum.floatingAmount.getDetail(), "");
                    } else {
                        map.put(ContractEnum.floatingAmount.getDetail(), floatingAmount);
                    }
                    break;
                case totalAmount:
                    Double totalAmount = paymentResult.getTotalAmount();
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
        if (ToolUtil.isEmpty(order)) {
            throw new ServiceException(500, "数据不存在");
        }
        OrderResult orderResult = new OrderResult();
        ToolUtil.copyProperties(order, orderResult);
        List<OrderDetailResult> details = new ArrayList<>();

        PaymentResult paymentResult = paymentService.getDetail(orderResult.getOrderId());
        if (ToolUtil.isNotEmpty(paymentResult) && ToolUtil.isNotEmpty(paymentResult.getOrderId())) {
            details = detailService.getDetails(paymentResult.getOrderId());
        }
        int inStockNum = 0;
        int allNum = 0;
        for (OrderDetailResult detail : details) {
            inStockNum += detail.getInStockNumber();
            allNum += detail.getPurchaseNumber();
        }

        try {
            orderResult.setInStockRate(BigDecimal.valueOf(inStockNum).divide(BigDecimal.valueOf(allNum), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(100)).intValue());
        } catch (Exception e) {

        }

        double allMoney = 0;
        int totalNumber = 0;
        if (ToolUtil.isNotEmpty(order.getTotalAmount())) {  //兼容之前老订单  没有总价格 
            allMoney = order.getTotalAmount();
        }
        for (OrderDetailResult detail : details) {
            totalNumber += detail.getPurchaseNumber();
        }

        orderResult.setTotalNumber(totalNumber);
        orderResult.setAllMoney(allMoney);
        orderResult.setDetailResults(details);
        orderResult.setPaymentResult(paymentResult);

        detailFormat(orderResult);
        return orderResult;

    }

    private void detailFormat(OrderResult result) {
        Contacts Acontacts = contactsService.getById(result.getPartyAContactsId());//甲方委托人
        Contacts Bcontacts = contactsService.getById(result.getPartyBContactsId());//乙方联系人
        /**
         * 银行账户
         */
        if (ToolUtil.isNotEmpty(result.getPartyABankAccount())) {
            Invoice aInvoice = invoiceService.getById(result.getPartyABankAccount());
            result.setPartyABankAccount(aInvoice.getBankAccount());
        }
        if (ToolUtil.isNotEmpty(result.getPartyBBankAccount())) {
            Invoice bInvoice = invoiceService.getById(result.getPartyBBankAccount());
            result.setPartyBBankAccount(bInvoice.getBankAccount());
        }


        if (ToolUtil.isNotEmpty(Acontacts) && ToolUtil.isNotEmpty(Acontacts.getPhone())) {  //甲方代表电话
            Phone phone = phoneService.getById(Acontacts.getPhone());
            if (ToolUtil.isNotEmpty(phone)) {
                result.setAContactsPhone(phone.getPhoneNumber());
            }
        }

        if (ToolUtil.isNotEmpty(Bcontacts) && ToolUtil.isNotEmpty(Bcontacts.getPhone())) {  //乙方代表电话
            Phone phone = phoneService.getById(Bcontacts.getPhone());
            if (ToolUtil.isNotEmpty(phone)) {
                result.setBContactsPhone(phone.getPhoneNumber());
            }
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

        Contract contract = ToolUtil.isEmpty(result.getContractId()) ? new Contract() : contractService.getById(result.getContractId());
        result.setContract(contract);

        Contacts contacts = contactsService.getById(result.getUserId());//交货人
        result.setDeliverer(contacts);
        List<PaymentRecord> paymentRecords = paymentRecordService.listByOrderIds(new ArrayList<Long>() {{
            add(result.getOrderId());
        }});
        long paymentSum = 0;
        for (PaymentRecord paymentRecord : paymentRecords) {
            paymentSum += paymentRecord.getPaymentAmount();
        }


        try {
            result.setPaymentRate(BigDecimal.valueOf(paymentSum).divide(BigDecimal.valueOf(100),2,RoundingMode.DOWN).divide(BigDecimal.valueOf(result.getTotalAmount()), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(100)).intValue());
        } catch (Exception e) {

        }

        result.setUser(userService.getUserResultsByIds(new ArrayList<Long>() {{
            add(result.getCreateUser());
        }}).get(0));

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
        /**
         * 取出orderDetail 循环取出 orderId
         */
        OrderDetailParam orderDetailParam = new OrderDetailParam();
        orderDetailParam.setType(2);
        List<OrderDetailResult> orderDetailResults = detailService.getOrderDettailProductionIsNull(orderDetailParam);


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
        List<OrderResult> orderResultList = BeanUtil.copyToList(this.listByIds(orderDetailResults.stream().map(OrderDetailResult::getOrderId).distinct().collect(Collectors.toList())), OrderResult.class);
        //序列化 order表
        formatOrders(orderResultList);
        for (OrderDetailResult orderDetailResult : orderDetailResults) {
            for (OrderResult orderResult : orderResultList) {
                if (orderDetailResult.getOrderId().equals(orderResult.getOrderId())) {
                    orderDetailResult.setOrderResult(orderResult);
                    break;
                }
            }
        }
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

    private void formatOrders(List<OrderResult> data) {
        List<Long> customerIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        List<Long> contractIds = new ArrayList<>();
        for (OrderResult datum : data) {
            customerIds.add(datum.getBuyerId());
            customerIds.add(datum.getSellerId());
            userIds.add(datum.getCreateUser());
            orderIds.add(datum.getOrderId());
            contractIds.add(datum.getContractId());
        }

        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        List<Contract> contractList = contractIds.size() == 0 ? new ArrayList<>() : contractService.listByIds(contractIds);
        List<PaymentRecord> paymentRecords = paymentRecordService.listByOrderIds(orderIds);
        List<PaymentRecordResult> paymentRecordResultList = BeanUtil.copyToList(paymentRecords, PaymentRecordResult.class);


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
            if (ToolUtil.isNotEmpty(datum.getContractId())) {
                for (Contract contract : contractList) {
                    if (datum.getContractId().equals(contract.getContractId())) {
                        datum.setContract(contract);
                        break;
                    }
                }
            }
            List<PaymentRecordResult> paymentList = new ArrayList<>();
            for (PaymentRecordResult paymentRecordResult : paymentRecordResultList) {
                if (paymentRecordResult.getOrderId().equals(datum.getOrderId())) {
                    paymentList.add(paymentRecordResult);
                }
            }
            datum.setPaymentRecordResults(paymentList);
        }
    }

    @Override
    public void format(List<OrderResult> data) {
        this.formatOrders(data);
        List<OrderDetail> orderDetails = data.size() == 0 ? new ArrayList<>() : detailService.query().in("order_id", data.stream().map(OrderResult::getOrderId).distinct().collect(Collectors.toList())).eq("display", 1).list();
        List<OrderDetailResult> detailResults = BeanUtil.copyToList(orderDetails, OrderDetailResult.class, new CopyOptions());
//        detailService.format(detailResults);

        for (OrderResult datum : data) {
            List<OrderDetailResult> orderDetailResults = new ArrayList<>();
            for (OrderDetailResult detailResult : detailResults) {
                if (detailResult.getOrderId().equals(datum.getOrderId())) {
                    orderDetailResults.add(detailResult);
                }
            }
            datum.setDetailResults(orderDetailResults);
        }


    }

    @Override
    public void checkStatus(Long orderId) {
        Order order = this.getById(orderId);
        if (ToolUtil.isEmpty(order)) {
            throw new ServiceException(500, "参数错误");
        }
        List<OrderDetail> orderDetailList = detailService.lambdaQuery().eq(OrderDetail::getOrderId, order.getOrderId()).eq(OrderDetail::getDisplay, 1).list();
        int doneNum = 0;
        for (OrderDetail orderDetail : orderDetailList) {
            if (orderDetail.getPurchaseNumber() <= orderDetail.getInStockNumber()) {
                doneNum++;
            }
        }
        if (doneNum == orderDetailList.size()) {
            order.setStatus(99);
            this.updateById(order);
        }
    }

    @Override
    public String wxUpload(OrderParam param) {
        try {
            Order order = this.getById(param.getOrderId());
            WxCpService wxCpService = SpringContextHolder.getBean(WxCpService.class);

            WxCpFileUploadRequest wxCpFileUploadRequest = new WxCpFileUploadRequest("RenYiTaiYu", "spaceId", "fatherId", "fileName", "base64");
            WxCpFileUpload wxCpFileUpload = wxCpService.getWxCpClient().getOaWeDriveService().fileUpload(wxCpFileUploadRequest);
            return wxCpFileUpload.getFileId();
        } catch (WxErrorException e) {
//           throw new RuntimeException(e);
        }
        return "false";
    }

    @Override
    public String createWeDirvSpace(OrderParam param) {
        try {
            WxCpService wxCpService = SpringContextHolder.getBean(WxCpService.class);
//        WxCpFileUploadRequest wxCpFileUploadRequest = new WxCpFileUploadRequest("RenYiTaiYu", "spaceId", "fatherId", "fileName", "base64");
//        WxCpFileUpload wxCpFileUpload = wxCpService.getWxCpClient().getOaWeDriveService().fileUpload(wxCpFileUploadRequest);
            List<WxCpDepart> list = wxCpService.getWxCpClient().getDepartmentService().list(null);
            String userId = "RenYaiTiYu";
            String spaceName = "浑河云-订单";
//        配置
            List<WxCpSpaceCreateRequest.AuthInfo> infoList = new ArrayList<>();
            for (WxCpDepart wxCpDepart : list) {
//               WxCpSpaceCreateRequest.AuthInfo authInfoDepartment = new WxCpSpaceCreateRequest.AuthInfo();
//               authInfoDepartment.setAuth(4);
//               authInfoDepartment.setType(2);
//               authInfoDepartment.setDepartmentId(Math.toIntExact(wxCpDepart.getId()));
                infoList.add(new WxCpSpaceCreateRequest.AuthInfo() {{
                    setAuth(4);
                    setType(2);
                    setDepartmentId(Math.toIntExact(wxCpDepart.getId()));
                }});

            }


//        WxCpSpaceCreateRequest.AuthInfo authInfoUser = new WxCpSpaceCreateRequest.AuthInfo();
//        authInfoUser.setAuth(7);
//        authInfoUser.setType(1);
//        authInfoUser.setUserId("RenYiTaiYu");

//        infoList.add(authInfoUser);
            WxCpSpaceCreateRequest wxCpSpaceCreateRequest = new WxCpSpaceCreateRequest(userId, spaceName, infoList);
            WxCpSpaceCreateData wxCpSpaceCreateData = wxCpService.getWxCpClient().getOaWeDriveService().spaceCreate(wxCpSpaceCreateRequest);
            return JSON.toJSONString(wxCpSpaceCreateData);
        } catch (WxErrorException e) {

        }
        return "false";
    }
}
