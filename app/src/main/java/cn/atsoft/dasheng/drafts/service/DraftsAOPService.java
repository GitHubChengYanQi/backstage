package cn.atsoft.dasheng.drafts.service;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.drafts.model.params.DraftsParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
@Component
@Aspect
public class DraftsAOPService {
    @Autowired
    private DraftsService draftsService;
//    @Around("@annotation(DraftsAOP)")
//    @Around("execution(* cn.atsoft.dasheng.erp.controller.*.*(..))")
//@PostConstruct
public void recordSysLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            removeDrafts(point);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void removeDrafts(ProceedingJoinPoint point) throws NoSuchMethodException, ClassNotFoundException {
//获取拦截的方法名
        String classType = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        // 参数值
        Object[] args = point.getArgs();
        Class<?>[] classes = new Class[args.length];
//        for (int k = 0; k < args.length; k++) {
//            if (!args[k].getClass().isPrimitive()) {
//                // 获取的是封装类型而不是基础类型
//                String result = args[k].getClass().getName();
////                Class s = map.get(result);
//                classes[k] = s == null ? args[k].getClass() : s;
//            }
//        }
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
        Method method = Class.forName(classType).getMethod(methodName, classes);
        // 参数名
        String[] parameterNames = pnd.getParameterNames(method);
        // 通过map封装参数和参数值
        for (int i = 0; i < parameterNames.length; i++) {
            if(parameterNames[i].equals("draftsId") && ToolUtil.isNotEmpty(args[i])){
                Long arg = (Long) args[i];
                draftsService.delete(new DraftsParam(){{
                    setDraftsId(arg);
                }});
            }
            System.out.println("参数名："+parameterNames[i]+"\n参数值"+args[i]);
        }
    }
//    private static HashMap<String, Class> map = new HashMap<String, Class>() {
//        {
//            put("java.lang.Integer", int.class);
//            put("java.lang.Double", double.class);
//            put("java.lang.Float", float.class);
//            put("java.lang.Long", Long.class);
//            put("java.lang.Short", short.class);
//            put("java.lang.Boolean", boolean.class);
//            put("java.lang.Char", char.class);
//        }
//
//    }
}
