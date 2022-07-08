package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.InstockErrorActionEnum;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AnomalyOrderMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyOrderParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
import cn.atsoft.dasheng.erp.model.result.AnomalyOrderResult;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;
import static cn.atsoft.dasheng.message.enmu.AuditEnum.CHECK_ACTION;

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
    @Autowired
    private AnomalyDetailService anomalyDetailService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private ActivitiProcessLogService processLogService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private InventoryDetailService inventoryDetailService;
    @Autowired
    private ProductionPickListsService pickListsService;
    @Autowired
    private InstockListService instockListService;

    @Override
    @Transactional
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
            if (ToolUtil.isEmpty(anomalyParam.getAnomalyId())) {
                throw new ServiceException(500, "缺少 异常id");
            }
            ids.add(anomalyParam.getAnomalyId());
            Anomaly anomaly = new Anomaly();
            anomaly.setStatus(98);
            ToolUtil.copyProperties(anomalyParam, anomaly);
            anomaly.setOrderId(entity.getOrderId());
            anomalies.add(anomaly);
        }
        //判断是否提交过
        List<Anomaly> anomalyList = anomalyService.listByIds(ids);
        for (Anomaly anomaly : anomalyList) {
            if (anomaly.getStatus() == 98) {
                throw new ServiceException(500, "请勿重新提交异常");
            }
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
            if (ToolUtil.isNotEmpty(inkind)) {
                switch (inkind.getSource()) {
                    case "inErrorBatch":
                        inkind.setNumber(detail.getNumber());
                        inkind.setSource(AnomalyType.InstockError.name());
                        inkind.setSourceId(detail.getDetailId());
                        inkindService.updateById(inkind);
                        bind(inkind.getInkindId(), detail.getDetailId());
                        break;
                    case "inErrorSingle":
                        for (long i = 0; i < detail.getNumber(); i++) {
                            Inkind newInkind = new Inkind();
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
        submit(entity);
        shopCartService.addDynamic(param.getInstockOrderId(), "提交了异常描述");
        shopCartService.addDynamic(entity.getOrderId(), "提交了异常");
    }

    private void bind(Long inkindId, Long detailId) {
        AnomalyBind anomalyBind = new AnomalyBind();
        anomalyBind.setInkindId(inkindId);
        anomalyBind.setDetailId(detailId);
        bindService.save(anomalyBind);
    }

    private void submit(AnomalyOrder entity) {

        String module = "";
        String message = "";
        switch (entity.getType()) {
            case "instock":
                module = "INSTOCKERROR";
                message = "入库";
                break;
            case "Stocktaking":
                module = "StocktakingError";
                message = "盘点";
                break;
        }

        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ProcessType.ERROR.getType()).eq("status", 99).eq("module", module).eq("display", 1).one();
        //    发起审批流程
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            this.power(activitiProcess);//检查创建权限
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的" + message + "异常 ");
            activitiProcessTaskParam.setQTaskId(entity.getOrderId());
            activitiProcessTaskParam.setUserId(entity.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getOrderId());
            activitiProcessTaskParam.setType(ProcessType.ERROR.getType());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());

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

    /**
     * 盘点异常打单据添加
     *
     * @param param
     */
    @Override
    public void addByInventory(AnomalyOrderParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "15").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置异常单据自动生成编码规则");
            }
        }

        AnomalyOrder entity = getEntity(param);
        this.save(entity);
        List<Anomaly> anomalies = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (AnomalyParam anomalyParam : param.getAnomalyParams()) {
            ids.add(anomalyParam.getAnomalyId());
            Anomaly anomaly = new Anomaly();
            anomaly.setStatus(98);
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


        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ProcessType.ERROR.getType()).eq("status", 99).eq("module", "StocktakingError").one();
        //    发起审批流程
        if (ToolUtil.isNotEmpty(activitiProcess)) {

            this.power(activitiProcess);//检查创建权限
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的盘点异常 ");
            activitiProcessTaskParam.setQTaskId(entity.getOrderId());
            activitiProcessTaskParam.setUserId(entity.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getOrderId());
            activitiProcessTaskParam.setType(ProcessType.ERROR.getType());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());

        } else {
            throw new ServiceException(500, "请先设置流程");
        }

    }

    /**
     * 提交
     *
     * @param orderParam
     */
    @Override
    @Transactional
    public void submit(AnomalyOrderParam orderParam) {

        if (ToolUtil.isEmpty(orderParam.getOrderId())) {
            throw new ServiceException(500, "请确定异常单据id");
        }

        AnomalyOrder anomalyOrder = this.getById(orderParam.getOrderId());
        if (ToolUtil.isEmpty(anomalyOrder)) {
            throw new ServiceException(500, "单据不存在");
        }
        if (anomalyOrder.getComplete() != 0) {
            throw new ServiceException(500, "当前单据已被处理");
        }
        List<Anomaly> anomalies = anomalyService.lambdaQuery().eq(Anomaly::getOrderId, orderParam.getOrderId()).eq(Anomaly::getStatus, 99).eq(Anomaly::getDisplay, 1).list();
        List<AnomalyResult> anomalyResults = BeanUtil.copyToList(anomalies, AnomalyResult.class, new CopyOptions());
        anomalyService.format(anomalyResults);

        switch (anomalyOrder.getType()) {
            case "instock":
                inStock(anomalyResults);
                break;
            case "Stocktaking":
                stocktaking(anomalyResults, orderParam.getOrderId());
                break;
        }
        /**
         * 更新状态
         */
        anomalyOrder.setComplete(99);
        this.updateById(anomalyOrder);
        updateStatus(orderParam);
    }

    /**
     * 盘点异常
     */
    private void stocktaking(List<AnomalyResult> anomalyResults, Long orderId) {

        ProductionPickListsParam param = new ProductionPickListsParam();
        param.setSource("StocktakingErrorOutStock");
        param.setSourceId(orderId);
        param.setUserId(LoginContextHolder.getContext().getUserId());

        List<ProductionPickListsDetailParam> pickListsDetailParams = new ArrayList<>();
        for (AnomalyResult anomalyResult : anomalyResults) {
            for (AnomalyDetailResult detail : anomalyResult.getDetails()) {
                if (detail.getStauts() == 2) {  //报损 创建出库单

                    InventoryDetail inventoryDetail = inventoryDetailService.query()  //找到盘点异常物料 进行出库
                            .eq("inventory_id", anomalyResult.getFormId())
                            .eq("inkind_id", detail.getInkindId()).one();

                    ProductionPickListsDetailParam detailParam = new ProductionPickListsDetailParam();
                    detailParam.setBrandId(inventoryDetail.getBrandId());
                    detailParam.setSkuId(inventoryDetail.getSkuId());
                    detailParam.setNumber(Math.toIntExact(inventoryDetail.getRealNumber()));
                    detailParam.setStorehousePositionsId(inventoryDetail.getPositionId());
                    detailParam.setReceivedNumber(Math.toIntExact(inventoryDetail.getRealNumber()));
                    pickListsDetailParams.add(detailParam);
                }
            }

            param.setPickListsDetailParams(pickListsDetailParams);
        }

        pickListsService.add(param);
    }

    /**
     * 入库异常
     *
     * @param anomalyResults
     */
    private void inStock(List<AnomalyResult> anomalyResults) {


        for (AnomalyResult anomalyResult : anomalyResults) {
            handle(anomalyResult);
        }
        for (AnomalyResult anomaly : anomalyResults) {
            long errorNum = 0;
            boolean t = false;
            for (AnomalyDetailResult detail : anomaly.getDetails()) {
                if (detail.getStauts() == -1) {
                    errorNum = errorNum + detail.getNumber();
                    t = true;
                }

            }
            if (t) {
                /**
                 * 添加异常记录
                 */
                InstockLogDetail instockLogDetail = new InstockLogDetail();
                instockLogDetail.setInstockOrderId(anomaly.getFormId());
                instockLogDetail.setSkuId(anomaly.getSkuId());
                instockLogDetail.setType("error");
                instockLogDetail.setBrandId(anomaly.getBrandId());
                instockLogDetail.setCustomerId(anomaly.getCustomerId());
                instockLogDetail.setNumber(errorNum);
                instockLogDetailService.save(instockLogDetail);
            }
        }
    }


    /**
     * 处理
     *
     * @param result
     */
    private void handle(AnomalyResult result) {

        if (ToolUtil.isNotEmpty(result.getInstockNumber()) && result.getInstockNumber() > 0) {   //允许入库 并添加 入库购物车
            addShopCart(result);

            String skuMessage = skuService.skuMessage(result.getSkuId());
            shopCartService.addDynamic(result.getFormId(), "异常物料" + skuMessage + "允许入库");
        }
        for (AnomalyDetailResult detail : result.getDetails()) {
            //终止入库
            if (detail.getStauts() == -1) {
                stopInStock(result, detail);
//                stopInStock(detail.getDetailId());
            }
        }

    }

    /**
     * 判断单据 状态
     */
    private void updateStatus(AnomalyOrderParam param) {
        List<Anomaly> anomalies = anomalyService.lambdaQuery().eq(Anomaly::getOrderId, param.getOrderId()).eq(Anomaly::getDisplay, 1).list();
        boolean t = true;
        for (Anomaly anomaly : anomalies) {
            if (anomaly.getStatus() != 99) {
                t = false;
            }
        }

        if (t) {
            /**
             * 消息队列完成动作
             */
            processLogService.checkAction(param.getOrderId(), "ERROR", param.getActionId(), LoginContextHolder.getContext().getUserId());
//            messageProducer.auditMessageDo(new AuditEntity() {{
//                setAuditType(CHECK_ACTION);
//                setMessageType(AuditMessageType.AUDIT);
//                setFormId(param.getOrderId());
//                setMaxTimes(2);
//                setTimes(1);
//                setForm("INSTOCKERROR");
//                setActionId(param.getActionId());
//            }});
        }
    }

    /**
     * 允许入库并添加购物车
     *
     * @param result
     */
    private void addShopCart(AnomalyResult result) {
        ShopCart shopCart = new ShopCart();
        shopCart.setSkuId(result.getSkuId());
        shopCart.setBrandId(result.getBrandId());
        shopCart.setCustomerId(result.getCustomerId());
        shopCart.setType("instockByAnomaly");
        shopCart.setFormId(result.getAnomalyId());
        shopCart.setNumber(result.getInstockNumber());
        shopCartService.save(shopCart);
    }

    /**
     * 终止入库
     *
     * @param id
     */
    private void stopInStock(Long id) {
        AnomalyDetail anomalyDetail = new AnomalyDetail();
        anomalyDetail.setStauts(-1L);
        anomalyDetail.setAnomalyId(id);
        anomalyDetailService.updateById(anomalyDetail);
    }

    /**
     * 终止入库
     * 拆分入库清单物料
     *
     * @param
     */
    private void stopInStock(AnomalyResult anomalyResult, AnomalyDetailResult detailResult) {
        Long inStockListId = anomalyResult.getSourceId();
        InstockList instockList = instockListService.getById(inStockListId);
        InstockList newInStockList = new InstockList();
        ToolUtil.copyProperties(instockList, newInStockList);

        if (detailResult.getNumber() > anomalyResult.getRealNumber()) {
            throw new ServiceException(500, "异常数量超过入库清单数量");
        }

        newInStockList.setInstockListId(null);
        newInStockList.setStatus(50L);
        newInStockList.setNumber(50L);
        newInStockList.setNumber(detailResult.getNumber());
        newInStockList.setRealNumber(detailResult.getNumber());
        instockListService.save(newInStockList);

        instockList.setStatus(1L);
        instockList.setRealNumber(anomalyResult.getInstockNumber());
        instockListService.updateById(instockList);


    }

    @Override
    public void delete(AnomalyOrderParam param) {
        AnomalyOrder entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }


    @Override
    public AnomalyOrderResult detail(Long id) {

        AnomalyOrder anomalyOrder = this.getById(id);
        AnomalyOrderResult result = new AnomalyOrderResult();
        ToolUtil.copyProperties(anomalyOrder, result);
        List<Anomaly> anomalies = anomalyService.lambdaQuery().eq(Anomaly::getOrderId, result.getOrderId()).eq(Anomaly::getDisplay, 1).list();
        List<AnomalyResult> anomalyResults = BeanUtil.copyToList(anomalies, AnomalyResult.class, new CopyOptions());
        anomalyService.format(anomalyResults);

        InstockOrder instockOrder = instockOrderService.getById(result.getInstockOrderId());
        if (ToolUtil.isNotEmpty(instockOrder)) {
            Long createUser = instockOrder.getCreateUser();
            User user = userService.getById(createUser);
            result.setMasterUser(user);
        }


        result.setInstockOrder(instockOrder);
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
    public void updateStatus(ActivitiProcessTask processTask) {
        AnomalyOrder anomalyOrder = this.getById(processTask.getFormId());
        anomalyOrder.setStatus(InstockErrorActionEnum.done.getStatus());
        this.updateById(anomalyOrder);
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

    @Override
    public void format(List<AnomalyOrderResult> data) {

        List<Long> orderIds = new ArrayList<>();
        for (AnomalyOrderResult datum : data) {
            orderIds.add(datum.getOrderId());
        }

        List<Anomaly> anomalies = orderIds.size() == 0 ? new ArrayList<>() : anomalyService.query().in("order_id", orderIds).eq("display", 1).list();
        List<AnomalyResult> anomalyResults = BeanUtil.copyToList(anomalies, AnomalyResult.class, new CopyOptions());
        anomalyService.format(anomalyResults);


        for (AnomalyOrderResult datum : data) {

            List<AnomalyResult> anomalyResultList = new ArrayList<>();

            for (AnomalyResult anomalyResult : anomalyResults) {
                if (datum.getOrderId().equals(anomalyResult.getOrderId())) {
                    anomalyResultList.add(anomalyResult);
                }
            }
            datum.setAnomalyResults(anomalyResultList);
        }

    }
}
