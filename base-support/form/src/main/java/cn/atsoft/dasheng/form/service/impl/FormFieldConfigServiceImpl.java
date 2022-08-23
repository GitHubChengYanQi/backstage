package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormFieldConfig;
import cn.atsoft.dasheng.form.mapper.FormFieldConfigMapper;
import cn.atsoft.dasheng.form.model.params.FormFieldConfigParam;
import cn.atsoft.dasheng.form.model.result.FormFieldConfigResult;
import  cn.atsoft.dasheng.form.service.FormFieldConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自定义表单字段设置 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class FormFieldConfigServiceImpl extends ServiceImpl<FormFieldConfigMapper, FormFieldConfig> implements FormFieldConfigService {

    @Override
    public void add(FormFieldConfigParam param){
        FormFieldConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FormFieldConfigParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(FormFieldConfigParam param){
        FormFieldConfig oldEntity = getOldEntity(param);
        FormFieldConfig newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public FormFieldConfigResult findBySpec(FormFieldConfigParam param){
        return null;
    }

    @Override
    public List<FormFieldConfigResult> findListBySpec(FormFieldConfigParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(FormFieldConfigParam param){
        Page<FormFieldConfigResult> pageContext = getPageContext();
        IPage<FormFieldConfigResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(FormFieldConfigParam param){
        return param.getFieldId();
    }

    private Page<FormFieldConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FormFieldConfig getOldEntity(FormFieldConfigParam param) {
        return this.getById(getKey(param));
    }

    private FormFieldConfig getEntity(FormFieldConfigParam param) {
        FormFieldConfig entity = new FormFieldConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
