package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.mapper.TemplateMapper;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 合同模板 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {




    @Override
    public Long add(TemplateParam param) {
        Template entity = getEntity(param);
        this.save(entity);
        return entity.getTemplateId();
    }

    @Override
    public void delete(TemplateParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(TemplateParam param) {
        Template oldEntity = getOldEntity(param);
        Template newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TemplateResult findBySpec(TemplateParam param) {
        return null;
    }

    @Override
    public List<TemplateResult> findListBySpec(TemplateParam param) {
        return null;
    }

    @Override
    public PageInfo<TemplateResult> findPageBySpec(TemplateParam param, DataScope dataScope) {
        Page<TemplateResult> pageContext = getPageContext();
        IPage<TemplateResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Template template = new Template();
        template.setDisplay(0);
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("template_id");
        this.update(template, queryWrapper);
    }

    private Serializable getKey(TemplateParam param) {
        return param.getTemplateId();
    }

    private Page<TemplateResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Template getOldEntity(TemplateParam param) {
        return this.getById(getKey(param));
    }

    private Template getEntity(TemplateParam param) {
        Template entity = new Template();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
