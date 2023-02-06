package cn.atsoft.dasheng.form.service.impl;

import cn.atsoft.dasheng.form.model.ModelServiceDao;
import cn.atsoft.dasheng.form.service.ModelService;

import java.util.ArrayList;
import java.util.List;

public class ModelServiceImpl implements ModelService {
    List<ModelServiceDao> registerList = new ArrayList<>();
    /**
     * 注册模块
     * @return
     */
    @Override
    public void register(ModelServiceDao param){
        this.registerList.add(param);
    }
}
