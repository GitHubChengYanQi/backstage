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
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;

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
    private CodingRulesService codingRulesService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private DocumentStatusService statusService;

    @Override
    public void add(AnomalyOrderParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "15").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置入库异常单据自动生成编码规则");
            }
        }

        AnomalyOrder entity = getEntity(param);
        this.save(entity);
        List<Anomaly> anomalies = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (AnomalyParam anomalyParam : param.getAnomalyParams()) {
            ids.add(anomalyParam.getAnomalyId());
            Anomaly anomaly = new Anomaly();
            anomaly.setStatus(99);
            ToolUtil.copyProperties(anomalyParam, anomaly);
            anomaly.setOrderId(entity.getOrderId());
            anomalies.add(anomaly);
        }
        anomalyService.updateBatchById(anomalies);    //更新异常单据状态
        /**
         * 更新购物车状态
         */
        ShopCart shopCart = new ShopCart();
        shopCart.setStatus(99);
        shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
            in("form_id", ids);
        }});

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
        submit(entity);
    }

    private void bind(Long inkindId, Long detailId) {
        AnomalyBind anomalyBind = new AnomalyBind();
        anomalyBind.setInkindId(inkindId);
        anomalyBind.setDetailId(detailId);
        bindService.save(anomalyBind);
    }

    public void submit(AnomalyOrder entity) {
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ProcessType.INSTOCKERROR.getType()).eq("status", 99).eq("module", "verifyError").one();
        //    发起审批流程
        if (ToolUtil.isNotEmpty(activitiProcess)) {

            this.power(activitiProcess);//检查创建权限
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的入库异常 ");
            activitiProcessTaskParam.setQTaskId(entity.getOrderId());
            activitiProcessTaskParam.setUserId(entity.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getOrderId());
            activitiProcessTaskParam.setType(ProcessType.INSTOCKERROR.getType());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1);

        } else {
            throw new ServiceException(500, "请先设置流程");
        }
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

    @Override
    public void delete(AnomalyOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public AnomalyOrderResult detail(Long id) {
        AnomalyOrder anomalyOrder = this.getById(id);
        AnomalyOrderResult result = new AnomalyOrderResult();
        ToolUtil.copyProperties(anomalyOrder, result);
        List<Anomaly> anomalies = anomalyService.lambdaQuery().eq(Anomaly::getOrderId, result.getOrderId()).eq(Anomaly::getDisplay, 1).list();
        List<AnomalyResult> anomalyResults = BeanUtil.copyToList(anomalies, AnomalyResult.class, new CopyOptions());
        anomalyService.format(anomalyResults);

        List<DocumentsStatusResult> results = statusService.resultsByIds(new ArrayList<Long>() {{
            add(result.getStatus());
        }});
        if (ToolUtil.isNotEmpty(results)) {
            result.setStatusName(results.get(0).getName());
        }
        result.setAnomalyResults(anomalyResults);

        return result;
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
