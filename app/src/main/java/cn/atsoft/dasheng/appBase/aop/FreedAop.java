package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.app.controller.CustomerController;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.config.FreedTemplateProperties;
import cn.atsoft.dasheng.appBase.service.FreedTemplateService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.core.util.HttpContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
public class FreedAop {

    @Autowired
    private CustomerDynamicService customerDynamicService;

    @Autowired
    private BusinessDynamicService businessDynamicService;

    @Autowired
    private FreedTemplateService freedTemplateService;


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
         * 联系人状态
         */
        if (target instanceof ContactsService) {
            FreedTemplateProperties.Contacts contacts = freedTemplateService.getConfig().getContacts();
            String content = "";
            Contacts contactsparam = (Contacts) result;
            customerDynamicParam.setCustomerId(contactsparam.getCustomerId());
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
            CustomerParam customer = (CustomerParam) args[0];
            String content = "";
            switch (methodName) {
                case "add":
                    content = customerpro.getAdd().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    customerDynamicParam.setCustomerId(customer.getCustomerId());
                    break;
                case "update":
                    content = customerpro.getEdit().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    customerDynamicParam.setCustomerId(customer.getCustomerId());
                    break;
                case "delete":
                    content = customerpro.getDelete().replace("[操作人]", user.getName());
                    customerDynamicParam.setContent(content);
                    customerDynamicParam.setCustomerId(customer.getCustomerId());
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
            customerDynamicParam.setCustomerId(adressResult.getCustomerId());
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
            customerDynamicParam.setCustomerId(contractparam.getPartyA());
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
            ErpOrder erpOrderparam = (ErpOrder) result;
            customerDynamicParam.setCustomerId(erpOrderparam.getCustomerId());
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
