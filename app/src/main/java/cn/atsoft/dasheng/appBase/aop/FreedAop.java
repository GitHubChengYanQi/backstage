package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.params.CustomerDynamicParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.config.FreedTemplateProperties;
import cn.atsoft.dasheng.appBase.service.FreedTemplateService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.crm.service.TrackMessageService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.portal.repairDynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairDynamic.service.RepairDynamicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class FreedAop {

    @Autowired
    private CustomerDynamicService customerDynamicService;

    @Autowired
    private BusinessDynamicService businessDynamicService;

    @Autowired
    private FreedTemplateService freedTemplateService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ErpOrderService erpOrderService;
    @Autowired
    private RepairDynamicService repairDynamicService;
    @Autowired
    private CrmBusinessService crmBusinessService;
    @Autowired
    private CompetitorService competitorService;
    @Autowired
    private TrackMessageService trackMessageService;

    @Pointcut(value = "@annotation(cn.atsoft.dasheng.base.log.BussinessLog)")
    public void cutService() {
    }

    @Around("cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        //获取拦截的方法名
        Signature sig = point.getSignature();
        Object[] args = point.getArgs();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();

        //先执行业务
        Object result = point.proceed();
        /**
         * 如果是客户的就往客户动态里增加数据
         * 根据 methodName名称 决定是什么操作
         */
        LoginUser user = LoginContextHolder.getContext().getUser();
        CustomerDynamicParam customerDynamicParam = new CustomerDynamicParam();
        BusinessDynamicParam businessDynamicParam = new BusinessDynamicParam();


/**
 * 订单状态
 */
        if (target instanceof ErpOrderService) {
            FreedTemplateProperties.Contacts contacts = freedTemplateService.getConfig().getContacts();
            String content = "";
            ErpOrder erpOrder = (ErpOrder) result;
            QueryWrapper<ErpOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("order_id", erpOrder.getOrderId());
            List<ErpOrder> erpOrderList = erpOrderService.list(queryWrapper);
            for (ErpOrder order : erpOrderList) {
                customerDynamicParam.setCustomerId(order.getPartyAContactsId());
            }

            switch (methodName) {
                case "add":
                    content = contacts.getAdd().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
                case "update":
                    content = contacts.getEdit().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
                case "delete":
                    content = contacts.getDelete().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }

        /**
         * 联系人状态
         */
        if (target instanceof ContactsService) {
            FreedTemplateProperties.Contacts contacts = freedTemplateService.getConfig().getContacts();
            String content = "";
            Contacts contactsparam = (Contacts) result;
            QueryWrapper<Contacts> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("contacts_id", contactsparam.getContactsId());
            List<Contacts> contactsList = contactsService.list(queryWrapper);
//            for (Contacts contacts1 : contactsList) {
//                contacts1.getCustomerId();
//                customerDynamicParam.setCustomerId(contacts1.getCustomerId());
//            }

            switch (methodName) {
                case "add":
                    content = contacts.getAdd().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
                case "update":
                    content = contacts.getEdit().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
                case "delete":
                    content = contacts.getDelete().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }
        /**
         * 客户状态
         */
        if (target instanceof CustomerService) {
            FreedTemplateProperties.Customer customerpro = freedTemplateService.getConfig().getCustomer();
            Customer customer = (Customer) result;
            customerDynamicParam.setCustomerId(customer.getCustomerId());
            String content = "";
            switch (methodName) {
                case "add":
                    content = customerpro.getAdd().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
                case "update":
                    content = customerpro.getEdit().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
                case "delete":
                    content = customerpro.getDelete().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);

                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }
        /**
         * 地址状态
         */
        if (target instanceof AdressService) {
            FreedTemplateProperties.Adress adress = freedTemplateService.getConfig().getAdress();
            String content = "";
            Adress adressResult = (Adress) result;
            QueryWrapper<Adress> adressQueryWrapper = new QueryWrapper<>();
            adressQueryWrapper.in("adress_id", adressResult.getAdressId());
            List<Adress> adressList = adressService.list(adressQueryWrapper);
            for (Adress adress1 : adressList) {
                customerDynamicParam.setCustomerId(adress1.getCustomerId());
            }

            switch (methodName) {
                case "add":
                    content = adress.getAdd().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "update":
                    content = adress.getEdit().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "delete":
                    content = adress.getDelete().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }
        /**
         * 商机状态
         */
        if (target instanceof CrmBusinessService) {

            FreedTemplateProperties.CrmBusiness crmbusiness = freedTemplateService.getConfig().getCrmbusiness();
            CrmBusiness crmBusiness = (CrmBusiness) result;
            businessDynamicParam.setBusinessId(crmBusiness.getBusinessId());
            String content = "";
            switch (methodName) {
                case "add":
                    content = crmbusiness.getAdd().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);

                    break;
                case "update":
                    content = crmbusiness.getEdit().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);

                    break;
                case "delete":
                    content = crmbusiness.getDelete().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);

                    break;
            }
            businessDynamicService.add(businessDynamicParam);
        }
        //跟踪动态
        if (target instanceof TrackMessageService) {
            FreedTemplateProperties.TrackMessage trackMessage = freedTemplateService.getConfig().getTrackMessage();
            TrackMessage trackMessageResult = (TrackMessage) result;
            if (ToolUtil.isEmpty(trackMessageResult.getBusinessId())) {
                throw new ServiceException(500, "请确认当前项目");
            }
            businessDynamicParam.setBusinessId(trackMessageResult.getBusinessId());
            String content = "";
            switch (methodName) {
                case "add":
                    content = trackMessage.getAdd().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);
                    break;
            }
            businessDynamicService.add(businessDynamicParam);
        }


/**
 * 合同状态
 */
        if (target instanceof ContractService) {
            FreedTemplateProperties.Contract contract = freedTemplateService.getConfig().getContract();
            Contract contractparam = (Contract) result;
            QueryWrapper<Contract> contractsQueryWrapper = new QueryWrapper<>();
            contractsQueryWrapper.in("contract_id", contractparam.getContractId());
            List<Contract> contractList = contractService.list(contractsQueryWrapper);
            for (Contract contract1 : contractList) {
                customerDynamicParam.setCustomerId(contract1.getPartyA());
            }

            String content = "";
            switch (methodName) {
                case "add":
                    content = contract.getAdd().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "update":
                    content = contract.getEdit().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "delete":
                    content = contract.getDelete().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }

        /**
         * 订单状态
         */
        if (target instanceof CrmBusinessService) {

            FreedTemplateProperties.ErpOrder erpOrder = freedTemplateService.getConfig().getErpOrder();
            CrmBusiness crmBusinessparam = (CrmBusiness) result;
            customerDynamicParam.setCustomerId(crmBusinessparam.getCustomerId());
            String content = "";
            switch (methodName) {
                case "add":
                    content = erpOrder.getAdd().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);

                    break;
                case "update":
                    content = erpOrder.getEdit().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);

                    break;
                case "delete":
                    content = erpOrder.getDelete().replace("[操作人]", user.getName());
                    businessDynamicParam.setContent(content);

                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }
        if (target instanceof RepairService) {
            FreedTemplateProperties.Repair repairs = freedTemplateService.getConfig().getRepair();
            String content = "";
            Repair repair = (Repair) result;
            RepairDynamicParam repairDynamicParam = new RepairDynamicParam();

            switch (methodName) {
                case "add":
                    content = repairs.getAdd().replace("[操作人]", user.getName());
                    repairDynamicParam.setContent(content);
                    repairDynamicParam.setRepairId(repair.getRepairId());

                    break;
                case "update":
                    content = repairs.getEdit().replace("[操作人]", user.getName());
                    repairDynamicParam.setContent(content);
                    repairDynamicParam.setRepairId(repair.getRepairId());

                    break;
                case "delete":
                    content = repairs.getDelete().replace("[操作人]", user.getName());
                    repairDynamicParam.setContent(content);


                    break;
            }

            repairDynamicService.add(repairDynamicParam);
        }


        return result;
    }
}
