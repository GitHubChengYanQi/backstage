package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.fieldAuthority.entity.FieldAuthority;
import cn.atsoft.dasheng.fieldAuthority.model.request.FieldRole;

import cn.atsoft.dasheng.model.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


import java.awt.print.PrinterException;
import java.lang.reflect.Field;
import java.util.*;


@Aspect
public class DocumentFieldAopService {

    //        @Pointcut(value = "@annotation(cn.atsoft.dasheng.appBase.aop.DocumentFieldAop)")
    @Pointcut("execution(* cn.atsoft.dasheng..*.*Controller.*(..))")
    public void field() {
    }

    @Before("field()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
//        Object[] args = joinPoint.getArgs();
//        for (Object obj : args) {
//            filterField(obj);
//        }
    }

    @AfterReturning(returning = "obj", pointcut = "field()")
    public void doAfterReturning(Object obj) throws Throwable {
        filterField(obj);
    }

    private void filterField(Object obj) throws IllegalAccessException {
//        if (obj.getClass().getSimpleName().equals("PageInfo")) {
//
//        } else
        if (ToolUtil.isNotEmpty(obj)) {
            if (obj instanceof List) {
                for (Object datum : (List) obj) {
                    this.filterField(datum);
                }
            }  else if (obj instanceof Map) {
                Iterator it = ((Map) obj).entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object value = entry.getValue();
                    if (value instanceof Object) {
                        this.filterField(value);
                    }
                }
            } else if (obj instanceof PageInfo) {
                    PageInfo pageInfo = (PageInfo) obj;
                    List data = pageInfo.getData();
                    for (Object datum : data) {
                        this.filterField(datum);
                    }
                }else {
                Class objClass = (Class) obj.getClass();
                Field[] fs = objClass.getDeclaredFields();
                for (Field f : fs) {
                    f.setAccessible(true);
                    type(f.getType().getSimpleName());
                    if (type(f.getType().getSimpleName())) {
                        Object o = f.get(obj);
                        if(ToolUtil.isNotEmpty(o)){
                            this.filterField(o);
                        }
                    }
                }
            }
            this.setNull(obj);
        }
    }
    private Boolean type(String type){
        switch (type){
            case "long":
            case "Long":
            case "List":
            case "String":
            case "Boolean":
            case "boolean":
            case "int":
            case "integer":
            case "Integer":
            case "ArrayList":
            case "array":
            case "byte":
            case "Byte":
            case "Date":
            case "DateTime":
            case "Map":
            case "map":
                return false;
            default:
                return true;

        }

    }

    private void setNull(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        String simpleName = obj.getClass().getSimpleName();
        for (Field field : fields) {
            field.setAccessible(true);
            DocumentFieldAop documentFieldAop = field.getAnnotation(DocumentFieldAop.class);
            if (ToolUtil.isNotEmpty(documentFieldAop) && documentFieldAop.value()) {
                boolean hidden = true;
                LoginUser loginUser = LoginContextHolder.getContext().getUser();
                List<Long> roleList = loginUser.getRoleList();
                if (roleList.stream().anyMatch(i -> i.equals(1L))) {
                    hidden = false;
                } else {
                    for (Long roleId : roleList) {
                        FieldAuthority fieldAuthority = new FieldAuthority();
                        List<FieldRole> fieldRoles = JSON.parseArray(fieldAuthority.getDetail(), FieldRole.class);
                        if (ToolUtil.isEmpty(fieldRoles)) {
                            fieldRoles = new ArrayList<>();
                        }
                        fieldRoles.add(new FieldRole() {{
                            setFieldName("skuName");
                            setRoleAuthorities(new ArrayList<FieldRole.RoleAuthority>() {{
                                add(new FieldRole.RoleAuthority() {{
                                    setChmod(7);
                                    setRoleId(12L);
                                }});
                            }});
                        }});
                        fieldRoles.add(new FieldRole() {{
                            setFieldName("name");
                            setRoleAuthorities(new ArrayList<FieldRole.RoleAuthority>() {{
                                add(new FieldRole.RoleAuthority() {{
                                    setChmod(1);
                                    setRoleId(15L);
                                }});
                            }});
                        }});

                        for (FieldRole fieldRole : fieldRoles) {
                            if (fieldRole.getFieldName().equals(field.getName())) {
                                for (FieldRole.RoleAuthority roleAuthority : fieldRole.getRoleAuthorities()) {
                                    if (roleAuthority.getRoleId().equals(roleId)) {
                                        switch (roleAuthority.getChmod()) {
                                            case 7:
                                            case 4:
                                            case 2:
                                            case 1:
                                                hidden = false;
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                
                }

                if (hidden) {
                    field.set(obj, null);
                }
            }
        }
    }

    private void checkAuthority(List<Long> roleList, Field field, String filedName, Boolean hidden) {
        for (Long roleId : roleList) {
            FieldAuthority fieldAuthority = new FieldAuthority();
            List<FieldRole> fieldRoles = JSON.parseArray(fieldAuthority.getDetail(), FieldRole.class);
            if (ToolUtil.isEmpty(fieldRoles)) {
                fieldRoles = new ArrayList<>();
            }
            fieldRoles.add(new FieldRole() {{
                setFieldName("skuName");
                setRoleAuthorities(new ArrayList<FieldRole.RoleAuthority>() {{
                    add(new FieldRole.RoleAuthority() {{
                        setChmod(7);
                        setRoleId(12L);
                    }});
                }});
            }});
            fieldRoles.add(new FieldRole() {{
                setFieldName("name");
                setRoleAuthorities(new ArrayList<FieldRole.RoleAuthority>() {{
                    add(new FieldRole.RoleAuthority() {{
                        setChmod(1);
                        setRoleId(15L);
                    }});
                }});
            }});

            for (FieldRole fieldRole : fieldRoles) {
                if (filedName.equals(fieldAuthority.getModelNames()) && fieldRole.getFieldName().equals(field.getName())) {
                    for (FieldRole.RoleAuthority roleAuthority : fieldRole.getRoleAuthorities()) {
                        if (roleAuthority.getRoleId().equals(roleId)) {
                            switch (roleAuthority.getChmod()) {
                                case 7:
                                case 4:
                                case 2:
                                case 1:
                                    hidden = true;
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
