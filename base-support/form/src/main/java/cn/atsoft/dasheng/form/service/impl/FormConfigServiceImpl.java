package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormConfig;
import cn.atsoft.dasheng.form.mapper.FormConfigMapper;
import cn.atsoft.dasheng.form.model.params.FormConfigParam;
import cn.atsoft.dasheng.form.model.result.FormConfigResult;
import  cn.atsoft.dasheng.form.service.FormConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自定义表单 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class FormConfigServiceImpl extends ServiceImpl<FormConfigMapper, FormConfig> implements FormConfigService {

    @Override
    public void add(FormConfigParam param){
        FormConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FormConfigParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(FormConfigParam param){
        FormConfig oldEntity = getOldEntity(param);
        FormConfig newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public FormConfigResult findBySpec(FormConfigParam param){
        return null;
    }

    @Override
    public List<FormConfigResult> findListBySpec(FormConfigParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(FormConfigParam param){
        Page<FormConfigResult> pageContext = getPageContext();
        IPage<FormConfigResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(FormConfigParam param){
        return param.getFormId();
    }

    private Page<FormConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FormConfig getOldEntity(FormConfigParam param) {
        return this.getById(getKey(param));
    }

    private FormConfig getEntity(FormConfigParam param) {
        FormConfig entity = new FormConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
