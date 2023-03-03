package cn.atsoft.dasheng.form.service.impl;

import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.model.*;
import cn.atsoft.dasheng.form.model.enums.FormFieldEnum;
import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.FormFieldKeyAndValue;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.AuditType;
import cn.atsoft.dasheng.form.pojo.StepsType;
import cn.atsoft.dasheng.form.service.ModelService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    public ModelProcessDao getByModel(String model) {
        for (ModelProcessDao modelProcessDao : list()) {
            if (modelProcessDao.getModel().equals(model)) {
                return modelProcessDao;
            }
        }
        return new ModelProcessDao();
    }

    @Override
    public List<GetFormByModelResult> getFormByModel(String model) {
        ProcessInterface bean = SpringContextHolder.getBean(model + "ProcessFormConfig");
        List<GetFormByModelResult> result = new ArrayList<>();
        for (AuditType value : AuditType.values()) {
                result.add(new GetFormByModelResult() {{
                    setKey(value);
                    setTitle(value.getName());
                    setFields(bean.getProcessForm(value));
                }});

        }
        return result;
    }

//    public List<FormField> getAllFormByModel(ModelEnum model, AuditType key) {
//        ProcessInterface bean = SpringContextHolder.getBean(model + "ProcessFormConfig");
//        List<FormField> result = new ArrayList<>();
//        getAllChildren(bean.getProcessForm(key), result);
//        return result;
//    }



    @Override
    public boolean checkProcessData(ActivitiStepsParam param, String  model) throws IllegalAccessException {

        List<GetFormByModelResult> formByModel = getFormByModel(model);



        List<FormFieldParam> roleList = param.getRoleList();


        //        获取联动数据
        List<FormField> checkField = new ArrayList<>();
        for (GetFormByModelResult formFieldResult : formByModel) {
            if (param.getAuditType().equals(formFieldResult.getKey())){
                for (FormField formField : formFieldResult.getFields()) {


                    if (ToolUtil.isNotEmpty(roleList)) {
                        for (FormFieldParam formFieldParam : roleList) {
                            if (ToolUtil.isEmpty(formField.getLinks()) || (ToolUtil.isNotEmpty(formField.getLinks()) && formField.getLinks().stream().anyMatch(i -> i.getField().equals(formFieldParam.getName()) && i.getValue().equals(formFieldParam.getValue())))) {
                                checkField.add(formField);
                                break;
                            }
                        }
                    }
                }
            }
        }
//
//
        for (FormField formField : checkField) {
            for (FormFieldParam formFieldParam : roleList) {
                if (formField.getName().equals(formFieldParam.getName())) {
                    if (ToolUtil.isNotEmpty(formField.getDataSource().getValues())) {
//                        判断值
                        if (formField.getDataSource().getValues().stream().anyMatch(i -> i.getValue().equals(formFieldParam.getValue()))) {

                        } else {
                            throw new ServiceException(500, "传入字段缺少值或值错误:" + formField.getTitle());
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
