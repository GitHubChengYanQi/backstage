package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.app.controller.CustomerController;
import cn.atsoft.dasheng.app.service.CustomerDynamicService;
import cn.atsoft.dasheng.appBase.service.FreedTemplateService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class FreedAop {

    @Autowired
    private CustomerDynamicService customerDynamicService;

    @Autowired
    private FreedTemplateService freedTemplateService;

    @Pointcut(value = "@annotation(cn.atsoft.dasheng.base.log.BussinessLog)")
    public void cutService() {
    }

    @Around("cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        //获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();
        /**
         * 如果是客户的就往客户动态里增加数据
         * 根据 methodName名称 决定是什么操作
         */
        if(target instanceof CustomerController){
            // customerDynamicService.add();
        }
        //先执行业务
        Object result = point.proceed();


        return result;
    }
}
