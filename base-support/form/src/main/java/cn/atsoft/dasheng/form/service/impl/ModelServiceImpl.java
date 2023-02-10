package cn.atsoft.dasheng.form.service.impl;

import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.model.FormField;
import cn.atsoft.dasheng.form.model.FormFieldEnum;
import cn.atsoft.dasheng.form.model.ModelProcessDao;
import cn.atsoft.dasheng.form.model.ProcessInterface;
import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.StepsType;
import cn.atsoft.dasheng.form.service.ModelService;
import cn.atsoft.dasheng.form.service.ReflectUtils;
import cn.atsoft.dasheng.model.exception.ServiceException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Service;
import sun.reflect.misc.FieldUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {
    List<ModelProcessDao> registerList = new ArrayList<>();

    /**
     * 注册模块
     *
     * @return
     */
    @Override
    public void register(ModelProcessDao param) {
        this.registerList.add(param);
    }

    /**
     * 获取列表
     *
     * @return
     */
    @Override
    public List<ModelProcessDao> list() {
        return registerList;

    }

    /**
     * 通过模块获取数据
     *
     * @return
     */
    @Override
    public ModelProcessDao getByModel(ModelEnum model) {
        for (ModelProcessDao modelProcessDao : list()) {
            if (modelProcessDao.getModel().equals(model)) {
                return modelProcessDao;
            }
        }
        return new ModelProcessDao();
    }

    @Override
    public List<FormField> getFormByModel(ModelEnum model) {
        ProcessInterface bean = SpringContextHolder.getBean(model.name() + "ProcessFormConfig");

        return bean.getProcessForm();
    }

    public List<FormField> getAllFormByModel(ModelEnum model) {
        ProcessInterface bean = SpringContextHolder.getBean(model.name() + "ProcessFormConfig");
        List<FormField> result = new ArrayList<>();
        getAllChildren(bean.getProcessForm(), result);
        return result;
    }

    public void getAllChildren(List<FormField> data, List<FormField> result) {
//        result.addAll(data);
//        for (FormField datum : data) {
//            if(ToolUtil.isNotEmpty(datum.getChildren())){
//                this.getAllChildren(datum.getChildren(),result);
//            }
//        }
    }
    private void getAllFields (Field[] declaredFields,Class<?> selfClass,List<Field> formByModel){

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
//            type(declaredField)&&!declaredField.getName().equals("clazz")&&!declaredField.getName().equals("Class") && !declaredField.getType().equals(selfClass) && Arrays.stream(declaredField.getType().getInterfaces()).anyMatch(i->i.getSimpleName().equals("Serializable"))
            if (type(declaredField) &&!declaredField.getType().equals(selfClass)){
                try{
                    Field[] declaredFields1 = declaredField.getClass().getDeclaredFields();
                    getAllFields(declaredFields1,selfClass,formByModel);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (!type(declaredField)){
                formByModel.add(declaredField);
            }

        }
    }
    @Override
    public boolean checkProcessData(ActivitiStepsParam param, ModelEnum model) throws IllegalAccessException {

        List<FormField> formByModel = getFormByModel(model);
        List<Field> needCheckField = new ArrayList<>();
        Field[] declaredFields1 = FieldUtils.getAllFields(param.getClass());
        Field[] declaredFields = ReflectUtils.getAllField(param.getClass());
        getAllFields(declaredFields,param.getClass(),needCheckField);
//        Field[] declaredFields = param.getClass().getDeclaredFields();
        for (FormField formField : formByModel) {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);if (formField.getField().equals(declaredField.getName())) {
                    if (ToolUtil.isEmpty(declaredField.get(param))){
                        throw new ServiceException(500, "缺少提交数据");
                    }else {
                        needCheckField.add(declaredField);
                        break;
                    }
                }
            }
        }
//        获取联动数据
        List<FormField> checkField = new ArrayList<>();
        for (FormField formField : formByModel) {
            for (Field declaredField : needCheckField) {
                    if (ToolUtil.isEmpty(formField.getLinks())||(ToolUtil.isNotEmpty(formField.getLinks()) && formField.getLinks().stream().anyMatch(i -> {
                        try {
                            return i.getKey().equals(declaredField.getName()) && i.getValue().equals(declaredField.get(param));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }))) {
                        checkField.add(formField);
                    }
            }
        }


        for (FormField formField : checkField) {
            for (Field declaredField : needCheckField) {
                if (formField.getField().equals(declaredField.getName())) {
                    if (ToolUtil.isNotEmpty(formField.getDataSource().getValues())) {
//                        判断值
                        if (formField.getDataSource().getValues().stream().anyMatch(i -> {
                            try {
                                return i.getValue().equals(declaredField.get(param));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })) {
                            //TODO 校验值
                            if (formField.getType().equals(FormFieldEnum.Select)) {

                            }
                        } else {
                            throw new ServiceException(500, "传入字段缺少值或值错误:" + formField.getName());
                        }
                    }
                }
            }
        }
//        checkProcessDataDetail(param, allFormByModel);
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            checkProcessData(param.getChildNode(), model);
        } else if (ToolUtil.isNotEmpty(param.getConditionNodeList())) {
            for (ActivitiStepsParam activitiStepsParam : param.getConditionNodeList()) {
                checkProcessData(activitiStepsParam, model);
            }
        }
        return true;

    }

    private boolean checkProcessDataDetail(ActivitiStepsParam param, List<FormField> allFormByModel) {
        AuditRule auditRule = param.getAuditRule();
        if (!param.getType().equals(StepsType.BRANCH.getType())) {
            for (FormField formField : allFormByModel) {
                switch (formField.getType()) {
                    case SelectUser:
                        if (ToolUtil.isNotEmpty(auditRule.getRules())) {

                        } else {
                            throw new ServiceException(500, "请选择人员范围");
                        }
                        break;
                    case AuditOperation:
                        if (ToolUtil.isEmpty(auditRule.getNodeApprovalType())) {
                            throw new ServiceException(500, "请选择审批操作");
                        }
                        break;
                    case Select:
                        if (ToolUtil.isEmpty(formField.getDataSource())) {
//                            formField.get
                        }
                }
            }
        }


        return true;
    }

    private Boolean type(Field field) {
        Class<?>[] interfaces = field.getClass().getInterfaces();
//        return Arrays.stream(interfaces).anyMatch(i->i.getSimpleName().equals("Serializable"));
                switch (field.getType().getSimpleName()) {
            case "long":
            case "Long":
            case "String":
            case "Boolean":
            case "boolean":
            case "int":
            case "Field":
            case "Map":
            case "FieldRepository":
            case "FieldAccessor":
            case "Integer":
            case "clazz":
            case "Class":
            case "array":
            case "byte[]":
            case "Byte":
            case "Byte[]":
            case "Date":
            case "DateTime":
                return false;
            default:
                return true;

        }

    }
}
