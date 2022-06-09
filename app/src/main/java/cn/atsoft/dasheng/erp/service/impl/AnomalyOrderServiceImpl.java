package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AnomalyOrderMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyOrderParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyOrderResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-06-09
 */
@Service
public class AnomalyOrderServiceImpl extends ServiceImpl<AnomalyOrderMapper, AnomalyOrder> implements AnomalyOrderService {

    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private AnomalyDetailService detailService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private AnomalyBindService bindService;

    @Override
    public void add(AnomalyOrderParam param) {


        AnomalyOrder entity = getEntity(param);
        this.save(entity);

        List<Anomaly> anomalies = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        for (AnomalyParam anomalyParam : param.getAnomalyParams()) {
            ids.add(anomalyParam.getAnomalyId());
            Anomaly anomaly = new Anomaly();
            ToolUtil.copyProperties(anomalyParam, anomaly);
            anomaly.setOrderId(entity.getOrderId());
            anomalies.add(anomaly);
        }
        anomalyService.updateBatchById(anomalies);

        /**
         * 创建实物并绑定
         */
        List<AnomalyDetail> details = ids.size() == 0 ? new ArrayList<>() : detailService.query().in("anomaly_id", ids).eq("display", 1).list();
        for (AnomalyDetail detail : details) {

            Inkind inkind = inkindService.getById(detail.getInkindId());
            Inkind newInkind = new Inkind();

            switch (inkind.getSource()) {
                case "inErrorBatch":
                    inkind.setNumber(detail.getNumber());
                    inkindService.updateById(inkind);
                    bind(inkind.getInkindId(), detail.getDetailId());
                    break;
                case "inErrorSingle":
                    for (long i = 0; i < detail.getNumber(); i++) {
                        newInkind.setNumber(1L);
                        newInkind.setCustomerId(inkind.getCustomerId());
                        newInkind.setBrandId(inkind.getBrandId());
                        newInkind.setSkuId(inkind.getSkuId());
                        newInkind.setPid(inkind.getInkindId());
                        newInkind.setSource(AnomalyType.InstockError.name());
                        newInkind.setSourceId(detail.getDetailId());
                        inkindService.save(newInkind);
                        bind(newInkind.getInkindId(), detail.getDetailId());
                    }
                    break;
            }


        }


    }


    private void bind(Long inkindId, Long detailId) {
        AnomalyBind anomalyBind = new AnomalyBind();
        anomalyBind.setInkindId(inkindId);
        anomalyBind.setDetailId(detailId);
        bindService.save(anomalyBind);
    }

    public void submit(AnomalyOrderParam param) {

        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "instock").eq("status", 99).eq("module", "instockError").one();
        //     发起审批流程
//        if (ToolUtil.isNotEmpty(activitiProcess)) {
//            for (AnomalyParam paramParam : param.getParams()) {
//                this.power(activitiProcess);//检查创建权限
//                LoginUser user = LoginContextHolder.getContext().getUser();
//                ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
//                activitiProcessTaskParam.setTaskName(user.getName() + "发起的入库异常 ");
//                activitiProcessTaskParam.setQTaskId(paramParam.getFormId());
//                activitiProcessTaskParam.setUserId(paramParam.getCreateUser());
//                activitiProcessTaskParam.setFormId(paramParam.getFormId());
//                activitiProcessTaskParam.setType("instockError");
//                activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
//                Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
//                //添加小铃铛
//                wxCpSendTemplate.setSource("processTask");
//                wxCpSendTemplate.setSourceId(taskId);
//                //添加log
//                activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
//                activitiProcessLogService.autoAudit(taskId, 1);
//            }
//        }
//        else {
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

    @Override
    public void delete(AnomalyOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AnomalyOrderParam param) {
        AnomalyOrder oldEntity = getOldEntity(param);
        AnomalyOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AnomalyOrderResult findBySpec(AnomalyOrderParam param) {
        return null;
    }

    @Override
    public List<AnomalyOrderResult> findListBySpec(AnomalyOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<AnomalyOrderResult> findPageBySpec(AnomalyOrderParam param) {
        Page<AnomalyOrderResult> pageContext = getPageContext();
        IPage<AnomalyOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyOrderParam param) {
        return param.getOrderId();
    }

    private Page<AnomalyOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AnomalyOrder getOldEntity(AnomalyOrderParam param) {
        return this.getById(getKey(param));
    }

    private AnomalyOrder getEntity(AnomalyOrderParam param) {
        AnomalyOrder entity = new AnomalyOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
