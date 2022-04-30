package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.drafts.service.DraftsService;
import cn.atsoft.dasheng.sys.core.log.LogManager;
import cn.atsoft.dasheng.sys.core.log.factory.LogTaskFactory;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.context.request.RequestContextHolder.*;

@Component
@Aspect
public class LogAOP {

    @Autowired
    private DraftsService draftsService;
    @Pointcut("execution(* cn.atsoft.dasheng..*.*Controller.*(..))")
    public void webLog() {

    }

    @Around("webLog()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            doBefore(point);
        } catch (Exception e) {
//            log.error("日志记录出错!", e);
        }

        return result;
    }


    public void doBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        LoginUser user = LoginContextHolder.getContext().getUser();
        if (null == user) {
            return;
        }


        System.out.println(joinPoint.getTarget());
        System.out.println(request.getMethod());
        System.out.println(joinPoint.getSignature().getName());
        System.out.println(joinPoint.getSignature().getDeclaringTypeName());
        System.out.println(JSON.toJSONString(joinPoint.getArgs()));

        LogManager.me().executeLog(LogTaskFactory.bussinessLog(user.getId(), null,joinPoint.getSignature().getDeclaringTypeName() , joinPoint.getSignature().getName(), JSON.toJSONString(joinPoint.getArgs())));
    }
//

}
