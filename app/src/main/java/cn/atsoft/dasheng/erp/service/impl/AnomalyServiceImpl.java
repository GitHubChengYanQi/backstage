package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.InStockActionEnum;
import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.mapper.AnomalyMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.service.AnomalyDetailService;
import cn.atsoft.dasheng.erp.service.AnomalyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;
import static cn.atsoft.dasheng.message.enmu.AuditEnum.CHECK_ACTION;


/**
 * <p>
 * 异常单据 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
@Service
public class AnomalyServiceImpl extends ServiceImpl<AnomalyMapper, Anomaly> implements AnomalyService {

    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private AnomalyDetailService detailService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private DocumentsActionService documentsActionService;
    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private GetOrigin getOrigin;

    @Transactional
    @Override
    public void add(AnomalyParam param) {

        switch (param.getAnomalyType()) {
            case InstockError:     //判断入库单
                InstockOrder order = instockOrderService.getById(param.getFormId());
                if (ToolUtil.isEmpty(order)) {
                    throw new ServiceException(500, "入库单不存在");
                }
                param.setType(param.getAnomalyType().toString());
                break;
        }
        Anomaly entity = getEntity(param);
        this.save(entity);
        /**
         * 来源
         */
        if (ToolUtil.isNotEmpty(param.getSource()) && ToolUtil.isNotEmpty(param.getSourceId())) {
            String origin = getOrigin.newThemeAndOrigin("anomaly", entity.getAnomalyId(), entity.getSource(), entity.getSourceId());
            entity.setOrigin(origin);
            this.updateById(entity);
        }

//        List<AnomalyDetail> details = new ArrayList<>();
//        for (AnomalyDetailParam detailParam : param.getDetailParams()) {
//            AnomalyDetail detail = new AnomalyDetail();
//            ToolUtil.copyProperties(detailParam, detail);
//            detail.setAnomalyId(entity.getAnomalyId());
//
//            if (param.getAnomalyType() == AnomalyType.InstockError) {
//                InstockList instockList = instockListService.getById(detailParam.getInstockListId());
//                instockList.setNumber(detailParam.getRealNumber());
//                instockList.setRealNumber(detailParam.getRealNumber());
//                instockListService.updateById(instockList);
//            }
//            details.add(detail);
//        }
//        detailService.saveBatch(details);


        //     发起审批流程
//        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "instock").eq("status", 99).eq("module", "instockError").one();
//        if (ToolUtil.isNotEmpty(activitiProcess)) {
//            this.power(activitiProcess);//检查创建权限
//            LoginUser user = LoginContextHolder.getContext().getUser();
//            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
//            activitiProcessTaskParam.setTaskName(user.getName() + "发起的入库异常 ");
//            activitiProcessTaskParam.setQTaskId(entity.getFormId());
//            activitiProcessTaskParam.setUserId(param.getCreateUser());
//            activitiProcessTaskParam.setFormId(entity.getFormId());
//            activitiProcessTaskParam.setType("instockError");
//            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
//            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
//            //添加小铃铛
//            wxCpSendTemplate.setSource("processTask");
//            wxCpSendTemplate.setSourceId(taskId);
//            //添加log
//            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
//            activitiProcessLogService.autoAudit(taskId, 1);
//        } else {
//            /**
//             * 没有异常流程直接算完成
//             */
//            InstockOrder instockOrder = instockOrderService.getById(param.getFormId());
//            instockOrderService.updateById(instockOrder);
//            DocumentsAction action = documentsActionService.query().eq("form_type", ReceiptsEnum.INSTOCK.name()).eq("action", InStockActionEnum.verify.name()).eq("display", 1).one();
//            messageProducer.auditMessageDo(new AuditEntity() {{
//                setAuditType(CHECK_ACTION);
//                setFormId(instockOrder.getInstockOrderId());
//                setForm(ReceiptsEnum.INSTOCK.name());
//                setActionId(action.getDocumentsActionId());
//            }});
//        }
    }


    private void power(ActivitiProcess activitiProcess) {
        ActivitiSteps startSteps = stepsService.query().eq("process_id", activitiProcess.getProcessId()).eq("type", START).one();
        if (ToolUtil.isNotEmpty(startSteps)) {
            ActivitiAudit audit = auditService.query().eq("setps_id", startSteps.getSetpsId()).one();
            if (!stepsService.checkUser(audit.getRule())) {
                throw new ServiceException(500, "您没有权限创建任务");
            }
        }
    }


    public void addDetails(AnomalyParam param) {

    }

    @Override
    public void delete(AnomalyParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AnomalyParam param) {
        Anomaly oldEntity = getOldEntity(param);
        Anomaly newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AnomalyResult findBySpec(AnomalyParam param) {
        return null;
    }

    @Override
    public List<AnomalyResult> findListBySpec(AnomalyParam param) {
        return null;
    }

    @Override
    public PageInfo<AnomalyResult> findPageBySpec(AnomalyParam param) {
        Page<AnomalyResult> pageContext = getPageContext();
        IPage<AnomalyResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyParam param) {
        return param.getAnomalyId();
    }

    private Page<AnomalyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Anomaly getOldEntity(AnomalyParam param) {
        return this.getById(getKey(param));
    }

    private Anomaly getEntity(AnomalyParam param) {
        Anomaly entity = new Anomaly();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void detailFormat(AnomalyResult result) {

        if (result.getType().equals("InstockError")) {
            InstockOrder instockOrder = instockOrderService.getById(result.getFormId());
            InstockOrderResult orderResult = new InstockOrderResult();
            ToolUtil.copyProperties(instockOrder, orderResult);
            instockOrderService.formatDetail(orderResult);
            result.setOrderResult(orderResult);
        }

        //返回附件图片等
        if (ToolUtil.isNotEmpty(result.getEnclosure())) {
            List<Long> filedIds = Arrays.asList(result.getEnclosure().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<String> filedUrls = new ArrayList<>();
            for (Long filedId : filedIds) {
                String mediaUrl = mediaService.getMediaUrl(filedId, 0L);
                filedUrls.add(mediaUrl);
            }
            result.setFiledUrls(filedUrls);
        }
    }

}
