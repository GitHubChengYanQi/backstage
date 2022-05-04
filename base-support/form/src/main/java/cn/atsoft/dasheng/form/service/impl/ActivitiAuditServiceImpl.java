package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiAuditMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Autowired
    private DocumentStatusService documentStatusService;

    @Override
    @Transactional
    public void add(ActivitiAuditParam param) {

        ActivitiAudit entity = getEntity(param);
        this.save(entity);

    }

    @Override
    public List<ActivitiAudit> getListBySteps(List<ActivitiSteps> steps) {
        List<Long> stepsId = new ArrayList<>();
        for (ActivitiSteps step : steps) {
            stepsId.add(step.getSetpsId());
        }
        return this.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepsId);
        }});
    }

    @Override
    public List<ActivitiAudit> getListByStepsId(List<Long> stepsIds) {
        if (ToolUtil.isEmpty(stepsIds)) {
            return new ArrayList<>();
        }
        return this.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepsIds);
        }});
    }

    @Override
    public void delete(ActivitiAuditParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public ActivitiAuditResult getAudit(Long id) {
        ActivitiAudit audit = this.getOne(new QueryWrapper<ActivitiAudit>() {{
            eq("setps_id", id);
        }});
        ActivitiAuditResult activitiAuditResult = new ActivitiAuditResult();
        ToolUtil.copyProperties(audit, activitiAuditResult);
        return activitiAuditResult;
    }

    @Override
    public void update(ActivitiAuditParam param) {
        ActivitiAudit oldEntity = getOldEntity(param);
        ActivitiAudit newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ActivitiAuditResult findBySpec(ActivitiAuditParam param) {
        return null;
    }

    @Override
    public List<ActivitiAuditResult> findListBySpec(ActivitiAuditParam param) {
        return null;
    }

    @Override
    public PageInfo<ActivitiAuditResult> findPageBySpec(ActivitiAuditParam param) {
        Page<ActivitiAuditResult> pageContext = getPageContext();
        IPage<ActivitiAuditResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<ActivitiAuditResult> backAudits(List<Long> ids) {
        List<ActivitiAuditResult> auditResults = new ArrayList<>();
        if (ids.size() > 0) {
            List<ActivitiAudit> audits = this.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", ids);
            }});

            for (ActivitiAudit audit : audits) {
                ActivitiAuditResult auditResult = new ActivitiAuditResult();
                ToolUtil.copyProperties(audit, auditResult);
                DocumentsStatusResult detail = documentStatusService.detail(auditResult.getDocumentsStatusId());
                auditResult.setStatusResult(detail);
                if (ToolUtil.isNotEmpty(auditResult.getAction())) {
                    List<Long> actionIds = JSON.parseArray(auditResult.getAction(), Long.class);
                    auditResult.setActionIds(actionIds);
                }
                auditResults.add(auditResult);
            }
        }

        return auditResults;
    }

    private Serializable getKey(ActivitiAuditParam param) {
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
