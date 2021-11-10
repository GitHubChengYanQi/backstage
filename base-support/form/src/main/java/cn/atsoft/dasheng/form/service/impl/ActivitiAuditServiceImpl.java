package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.mapper.ActivitiAuditMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import  cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 审批配置表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiAuditServiceImpl extends ServiceImpl<ActivitiAuditMapper, ActivitiAudit> implements ActivitiAuditService {

    @Override
    public void add(ActivitiAuditParam param){
        ActivitiAudit entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ActivitiAuditParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ActivitiAuditParam param){
        ActivitiAudit oldEntity = getOldEntity(param);
        ActivitiAudit newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ActivitiAuditResult findBySpec(ActivitiAuditParam param){
        return null;
    }

    @Override
    public List<ActivitiAuditResult> findListBySpec(ActivitiAuditParam param){
        return null;
    }

    @Override
    public PageInfo<ActivitiAuditResult> findPageBySpec(ActivitiAuditParam param){
        Page<ActivitiAuditResult> pageContext = getPageContext();
        IPage<ActivitiAuditResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiAuditParam param){
        return param.getAuditId();
    }

    private Page<ActivitiAuditResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiAudit getOldEntity(ActivitiAuditParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiAudit getEntity(ActivitiAuditParam param) {
        ActivitiAudit entity = new ActivitiAudit();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
