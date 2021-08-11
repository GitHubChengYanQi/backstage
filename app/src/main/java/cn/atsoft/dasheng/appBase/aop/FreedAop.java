package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.app.controller.CustomerController;
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
        if (target instanceof ContactsService) {
            FreedTemplateProperties.Contacts contacts = freedTemplateService.getConfig().getContacts();
            String content = "";
            switch (methodName){
                case "add":
                    content = contacts.getAdd().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "update":
                    content = contacts.getEdit().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "delete":
                    content = contacts.getDelete().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }
        if (target instanceof CustomerService) {
            FreedTemplateProperties.Customer customer = freedTemplateService.getConfig().getCustomer();
            String content = "";
            switch (methodName){
                case "add":
                    content = String.format(customer. getAdd(),user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "update":
                    content = customer.getEdit().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "delete":
                    content = customer.getDelete().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
            }
             customerDynamicService.add(customerDynamicParam);
        }

        if (target instanceof AdressService) {
            FreedTemplateProperties.Adress adress = freedTemplateService.getConfig().getAdress();

            String content = "";
            CustomerParam customerParam = (CustomerParam) args[0];

            switch (methodName){
                case "add":
                    content = adress.getAdd().replace("[操作人]",user.getName());
//                    content = String.format(customer. getAdd(),user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "update":
                    content = adress.getEdit().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
                case "delete":
                    content = adress.getDelete().replace("[操作人]",user.getName());
                    customerDynamicParam.setContent(content);
                    break;
            }
            customerDynamicService.add(customerDynamicParam);
        }
        if (target instanceof CrmBusinessService) {

            FreedTemplateProperties.CrmBusiness crmbusiness=freedTemplateService.getConfig().getCrmbusiness();
            CrmBusinessParam crmBusinessParam = (CrmBusinessParam) args[0];
            BusinessDynamicParam businessDynamicParam = new BusinessDynamicParam();
            String content = "";
            switch (methodName){
                case "add":
                    content = crmbusiness.getAdd().replace("[操作人]",user.getName());
                    businessDynamicParam.setContent(content);
                    businessDynamicParam.setBusinessId(crmBusinessParam.getBusinessId());
                    break;
                case "update":
                    content = crmbusiness.getEdit().replace("[操作人]",user.getName());
                    businessDynamicParam.setContent(content);
                    businessDynamicParam.setBusinessId(crmBusinessParam.getBusinessId());
                    break;
                case "delete":
                    content = crmbusiness.getDelete().replace("[操作人]",user.getName());
                    businessDynamicParam.setContent(content);
                    businessDynamicParam.setBusinessId(crmBusinessParam.getBusinessId());
                    break;
            }
            businessDynamicService.add(businessDynamicParam);
        }
        return result;
    }
}
