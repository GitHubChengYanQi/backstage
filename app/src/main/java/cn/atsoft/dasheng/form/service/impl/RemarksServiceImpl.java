package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.mapper.RemarksMapper;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.RemarksResult;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * log备注 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
@Service
public class RemarksServiceImpl extends ServiceImpl<RemarksMapper, Remarks> implements RemarksService {
    @Autowired
    private ActivitiProcessLogService logService;
    @Autowired
    private ActivitiAuditService auditService;


    @Override
    public void add(Long logId, String note) {
        Remarks remarks = new Remarks();
        remarks.setLogId(logId);
        remarks.setContent(note);
        this.save(remarks);
    }

    @Override
    public void delete(RemarksParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(RemarksParam param) {
        Remarks oldEntity = getOldEntity(param);
        Remarks newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RemarksResult findBySpec(RemarksParam param) {
        return null;
    }

    @Override
    public List<RemarksResult> findListBySpec(RemarksParam param) {
        return null;
    }

    @Override
    public PageInfo<RemarksResult> findPageBySpec(RemarksParam param) {
        Page<RemarksResult> pageContext = getPageContext();
        IPage<RemarksResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RemarksParam param) {
        return param.getRemarksId();
    }

    private Page<RemarksResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Remarks getOldEntity(RemarksParam param) {
        return this.getById(getKey(param));
    }

    private Remarks getEntity(RemarksParam param) {
        Remarks entity = new Remarks();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void addNote(Long taskId, String note) {

        List<ActivitiProcessLog> logs = logService.listByTaskId(taskId);

        List<Long> stepIds = new ArrayList<>();
        for (ActivitiProcessLog activitiProcessLog : logs) {
            stepIds.add(activitiProcessLog.getSetpsId());
        }
        List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepIds);
        }});
        List<ActivitiProcessLog> audit = logService.getAudit(taskId);
        for (ActivitiProcessLog activitiProcessLog : audit) {
            ActivitiAudit activitiAudit = logService.getRule(activitiAudits, activitiProcessLog.getSetpsId());

            if (activitiAudit.getType().equals("process")) {
                Remarks one = this.query().eq("log_id", activitiProcessLog.getLogId()).one();

                if (ToolUtil.isNotEmpty(one)) {
                    throw new ServiceException(500, "请勿重复添加备注");
                }
                Remarks remarks = new Remarks();
                remarks.setLogId(activitiProcessLog.getLogId());
                remarks.setContent(note);
                this.save(remarks);
            }
        }
    }
}
