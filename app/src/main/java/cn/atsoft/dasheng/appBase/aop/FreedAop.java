package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.params.CustomerDynamicParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.config.FreedTemplateProperties;
import cn.atsoft.dasheng.appBase.service.FreedTemplateService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
            for (Contacts contacts1 : contactsList) {
                contacts1.getCustomerId();
                customerDynamicParam.setCustomerId(contacts1.getCustomerId());
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
        return result;
    }
}
