package cn.atsoft.dasheng.appBase.aop;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.form.model.result.DocumentsOperationResult;
import cn.atsoft.dasheng.form.model.result.DocumentsPermissionsResult;
import cn.atsoft.dasheng.form.pojo.CanDo;
import cn.atsoft.dasheng.form.service.DocumentsPermissionsService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.lang.reflect.Field;
import java.util.*;


@Aspect
public class DocumentFieldAopService {

    @Autowired
    private DocumentsPermissionsService documentsPermissionsService;
    private List<DocumentsPermissionsResult> details = new ArrayList<>();
    String formType = null;

    //        @Pointcut(value = "@annotation(cn.atsoft.dasheng.appBase.aop.DocumentFieldAop)")
    @Pointcut("execution(* cn.atsoft.dasheng..*.*Controller.*(..)),!execution(* cn.atsoft.dasheng.sys.*.*Controller.*(..))")
    public void field() {
    }

    @Before("field()")
    public void doBefore(JoinPoint joinPoint) throws IllegalAccessException {
        details = documentsPermissionsService.getAllPermissions();
        formType = joinPoint.getTarget().getClass().getSimpleName();


        Integer type = 0;
        switch (joinPoint.getSignature().getName()) {
            case "edit":
            case "add":
            case "delete":
                type = 1;
                break;
        }
        if (type == 1) {
            Object[] args = joinPoint.getArgs();
            for (Object obj : args) {
                filterField(obj, type);
            }
        }


    }

    @AfterReturning(returning = "obj", pointcut = "field()")
//    @After("field()")
    public void doAfterReturning(Object obj) throws Throwable {
        details = documentsPermissionsService.getAllPermissions();
        filterField(obj, 2);

    }

    private void filterField(Object obj, Integer type) throws IllegalAccessException {

        if (ToolUtil.isNotEmpty(obj)) {
            if (obj instanceof List) {
                for (Object datum : (List) obj) {
                    this.filterField(datum, type);
                }
            } else if (obj instanceof Map) {
                Iterator it = ((Map) obj).entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object value = entry.getValue();
                    if (value instanceof Object) {
                        this.filterField(value, type);
                    }
                }
            } else if (obj instanceof PageInfo) {
                PageInfo pageInfo = (PageInfo) obj;
                List data = pageInfo.getData();
                if (ToolUtil.isNotEmpty(data)) {
                    for (Object datum : data) {
                        this.filterField(datum, type);
                    }
                }
            } else {
                Class objClass = obj.getClass();
                Field[] fs = objClass.getDeclaredFields();
                objClass.getGenericSuperclass();
                for (Field f : fs) {
                    f.getType();
                    f.setAccessible(true);
                    Object o = f.get(obj);

                    if (type(f)) {
                        if (ToolUtil.isNotEmpty(o)) {
                            this.filterField(o, type);
                        }
                    }
                }
            }
            this.setNull(obj, type);
        }
    }

    private Boolean type(Field field) {
        Class<?>[] interfaces = field.getClass().getInterfaces();
        return Arrays.stream(interfaces).anyMatch(i->i.getSimpleName().equals("Serializable"));
        //        switch (type) {
//            case "long":
//            case "Long":
//            case "String":
//            case "Boolean":
//            case "boolean":
//            case "int":
//            case "Integer":
//            case "array":
//            case "byte":
//            case "Byte":
//            case "Date":
//            case "DateTime":
//                return false;
//            default:
//                return true;
//
//        }

    }

    private void setNull(Object obj, Integer type) throws IllegalAccessException {
        String formTypeStr = this.formatFormType(formType);
        if (ToolUtil.isNotEmpty(formTypeStr)) {
            Field[] fields = obj.getClass().getDeclaredFields();
            String simpleName = obj.getClass().getSimpleName();
            for (Field field : fields) {
                field.setAccessible(true);
                FieldPermission fieldPermission = field.getAnnotation(FieldPermission.class);
                if (ToolUtil.isNotEmpty(fieldPermission) && fieldPermission.value()) {
                    boolean hidden = true;
                    LoginUser loginUser = LoginContextHolder.getContext().getUser();
                    List<Long> roleList = loginUser.getRoleList();
                    if (roleList.stream().anyMatch(i -> i.equals(1L))) {
                        hidden = false;
                    } else {
                        for (DocumentsPermissionsResult detail : details) {
                            if (detail.getFiledName().equals(field.getName()) && detail.getFormType().equals(formTypeStr)) {
                                for (Long roleId : roleList) {
                                    for (DocumentsOperationResult operationResult : detail.getOperationResults()) {
                                        if (operationResult.getRoleId().equals(roleId)) {
                                            switch (type) {
                                                case 1:
                                                    //增删改接传入返回参数校验
                                                    for (CanDo canDo : operationResult.getCanDos()) {
                                                        if (canDo.getDoName().equals("edit") && canDo.getIsCan()) {
                                                            hidden = false;
                                                            break;
                                                        }
                                                    }

                                                    break;
                                                case 2:
                                                    //其他接口传入返回校验
                                                    for (CanDo canDo : operationResult.getCanDos()) {
                                                        if (canDo.getDoName().equals("see") && canDo.getIsCan()) {
                                                            hidden = false;
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                default:
                                                    hidden = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
//
//                    for (Object fieldRole : fieldRoles) {
//                        if (fieldRole.getFieldName().equals(field.getName())) {
//                            for (FieldRole.RoleAuthority roleAuthority : fieldRole.getRoleAuthorities()) {
//                                if (roleAuthority.getRoleId().equals(roleId)) {
//                                    switch (type) {
//                                        case 1:
//                                            //增删改接传入返回参数校验
//                                            switch (roleAuthority.getChmod()) {
//                                                case 2:
//                                                    hidden = false;
//                                                    break;
//                                            }
//                                            break;
//                                        case 0:
//                                            //其他接口传入返回校验
//                                            switch (roleAuthority.getChmod()) {
//                                                case 2:
//                                                case 1:
//                                                    hidden = false;
//                                                    break;
//                                            }
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//                    }


                    }

                    if (hidden) {
                        field.set(obj, null);
                    }
                }
            }
        }
    }


    private String formatFormType(String formType) {
        if (ToolUtil.isNotEmpty(this.formType)) {
            switch (this.formType) {
                case "PurchaseAskController":
                    return "PURCHASE";
                case "ProductionPickListsController":
                    return "PICKLISTS";
                case "ProductionPickListsCartController":
                    return "PICKLISTSCART";
                case "ProductionPickListsDetailController":
                    return "PICKLISTSDETAIL";
                case "SkuController":
                    return "SKU";
                default:
                    return null;
            }
        }
        return null;
    }
}
