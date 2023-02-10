package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.form.model.FormField;
import cn.atsoft.dasheng.form.model.ModelProcessDao;
import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;

import java.util.ArrayList;
import java.util.List;

public interface ModelService {
    void register(ModelProcessDao param);
    List<ModelProcessDao> list();

    ModelProcessDao getByModel(ModelEnum model);
    List<FormField> getFormByModel(ModelEnum model);


    boolean checkProcessData(ActivitiStepsParam param, ModelEnum model) throws IllegalAccessException;
}
