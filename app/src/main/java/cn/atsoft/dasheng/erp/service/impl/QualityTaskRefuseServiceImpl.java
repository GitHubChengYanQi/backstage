package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.entity.QualityTaskRefuse;
import cn.atsoft.dasheng.erp.mapper.QualityTaskRefuseMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskRefuseParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskRefuseResult;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskRefuseService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 质检任务拒检 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-14
 */
@Service
public class QualityTaskRefuseServiceImpl extends ServiceImpl<QualityTaskRefuseMapper, QualityTaskRefuse> implements QualityTaskRefuseService {

    @Autowired
    private QualityTaskDetailService taskDetailService;
    @Autowired
    private QualityTaskService taskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Override
    public void add(QualityTaskRefuseParam param) {
        QualityTaskRefuse entity = getEntity(param);
        this.save(entity);

    }

    @Override
    public void delete(QualityTaskRefuseParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityTaskRefuseParam param) {
        QualityTaskRefuse oldEntity = getOldEntity(param);
        QualityTaskRefuse newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityTaskRefuseResult findBySpec(QualityTaskRefuseParam param) {
        return null;
    }

    @Override
    public List<QualityTaskRefuseResult> findListBySpec(QualityTaskRefuseParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityTaskRefuseResult> findPageBySpec(QualityTaskRefuseParam param) {
        Page<QualityTaskRefuseResult> pageContext = getPageContext();
        IPage<QualityTaskRefuseResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 主任务拒检
     *
     * @param param
     */
    @Override
    @Transactional
    public void refuse(QualityTaskRefuseParam param) {
        //TODO 拒绝检查
        List<QualityTaskRefuse> refuses = new ArrayList<>();
        List<QualityTaskDetail> details = new ArrayList<>();
        for (QualityTaskDetailParam detailParam : param.getDetailParams()) {
            QualityTaskRefuse refuse = new QualityTaskRefuse();
            QualityTaskDetail taskDetail = new QualityTaskDetail();
            refuse.setQualityTaskId(param.getQualityTaskId());
            refuse.setBrandId(detailParam.getBrandId());
            refuse.setSkuId(detailParam.getSkuId());
            refuse.setNote(param.getNote());
            refuse.setQualityTaskDetailId(detailParam.getQualityTaskDetailId());
            refuse.setNumber(Long.valueOf(detailParam.getNewNumber()));
            detailParam.setRemaining(detailParam.getRemaining() - detailParam.getNewNumber());

            ToolUtil.copyProperties(detailParam, taskDetail);
            details.add(taskDetail);
            refuses.add(refuse);
        }
        this.saveBatch(refuses);
        taskDetailService.updateBatchById(details);

        //判断是否全拒绝
        List<QualityTaskDetailResult> results = taskDetailService.getTaskDetailResults(param.getQualityTaskId());
        boolean a = true;
        for (QualityTaskDetailResult result : results) {
            if (result.getRemaining() != 0) {
                a = false;
            }
        }
        if (a) {
            //判断有没有子任务
            List<QualityTask> tasks = taskService.query().eq("parent_id", param.getQualityTaskId()).list();
            if (ToolUtil.isEmpty(tasks)) {
                QualityTask qualityTask = new QualityTask();
                qualityTask.setState(-1);
                taskService.update(qualityTask, new QueryWrapper<QualityTask>() {{
                    eq("quality_task_id", param.getQualityTaskId());
                }});
                //TODO  全部拒绝推送
                ActivitiProcessTask processTask = activitiProcessTaskService.getByFormId(param.getQualityTaskId());
                activitiProcessLogService.autoAudit(processTask.getProcessTaskId(),0);
            }
        }
    }

    @Override
    public List<QualityTaskRefuseResult> getRefuseByDetailId(Long detailId) {
        List<QualityTaskRefuseResult> refuseResults = new ArrayList<>();

        List<QualityTaskRefuse> taskRefuses = this.query().eq("quality_task_detail_id", detailId).list();

        for (QualityTaskRefuse taskRefus : taskRefuses) {
            QualityTaskRefuseResult taskRefuseResult = new QualityTaskRefuseResult();
            ToolUtil.copyProperties(taskRefus, taskRefuseResult);
            refuseResults.add(taskRefuseResult);
        }
        return refuseResults;
    }

    private Serializable getKey(QualityTaskRefuseParam param) {
        return param.getRefuseId();
    }

    private Page<QualityTaskRefuseResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityTaskRefuse getOldEntity(QualityTaskRefuseParam param) {
        return this.getById(getKey(param));
    }

    private QualityTaskRefuse getEntity(QualityTaskRefuseParam param) {
        QualityTaskRefuse entity = new QualityTaskRefuse();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
