package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.InStockActionEnum;
import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.erp.controller.AnnouncementsController;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.InstockOrderMapper;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.pojo.InStockByOrderParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.pojo.ActionStatus;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.message.service.AuditMessageService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.rest.model.params.MobileUrl;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

import static cn.atsoft.dasheng.message.enmu.AuditEnum.CHECK_ACTION;

/**
 * <p>
 * 入库单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class InstockOrderServiceImpl extends ServiceImpl<InstockOrderMapper, InstockOrder> implements InstockOrderService {

    @Autowired
    private UserService userService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockSendTemplate instockSendTemplate;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private OrCodeBindService bindService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StorehousePositionsBindService positionsBindService;
    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private StepsService stepsService;

    @Autowired
    private SkuService skuService;
    @Autowired
    private InstockLogService instockLogService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private DocumentStatusService documentStatusService;

    @Autowired
    private DocumentsActionService documentsActionService;

    @Autowired
    private AnnouncementsService announcementsService;
    @Autowired
    private RemarksService remarksService;

    @Autowired
    private MediaService mediaService;
    @Autowired
    private ShopCartService cartService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private AnomalyDetailService anomalyDetailService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private ShopCartService shopCartService;


    @Override
    @Transactional
    public void add(InstockOrderParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "1").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置入库单据自动生成编码规则");
            }
        }


        //防止添加重复数据
//        List<Long> judge = new ArrayList<>();
//        for (InstockRequest instockRequest : param.getInstockRequest()) {
//            Long skuId = instockRequest.getSkuId();
//            judge.add(skuId);
//        }
//        long count = judge.stream().distinct().count();
//        if (param.getInstockRequest().size() > count) {
//            throw new ServiceException(500, "请勿重复添加");
//        }
        /**
         * 附件
         */
        if (ToolUtil.isNotEmpty(param.getMediaIds())) {
            String mediaJson = JSON.toJSONString(param.getMediaIds());
            param.setEnclosure(mediaJson);
        }

        /**
         * 注意事项
         */
        if (ToolUtil.isNotEmpty(param.getNoticeIds())) {
            String json = JSON.toJSONString(param.getNoticeIds());
            param.setNoticeId(json);
            messageProducer.microService(new MicroServiceEntity() {{
                setType(MicroServiceType.Announcements);
                setObject(param.getNoticeIds());
                setOperationType(OperationType.ORDER);
                setMaxTimes(2);
                setTimes(1);
            }});

        }

        InstockOrder entity = getEntity(param);
        this.save(entity);

        List<Long> skuIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getListParams())) {
            for (InstockListParam instockRequest : param.getListParams()) {
                skuIds.add(instockRequest.getSkuId());
            }
//            List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.listByIds(skuIds);

            for (InstockListParam instockRequest : param.getListParams()) {
                if (ToolUtil.isNotEmpty(instockRequest)) {
                    if (ToolUtil.isEmpty(instockRequest.getCartId())) {
                        throw new ServiceException(500, "缺少购物车id");
                    }
                    ShopCart shopCart = shopCartService.getById(instockRequest.getCartId());
                    if (ToolUtil.isEmpty(shopCart)) {
                        throw new ServiceException(500, "购物车不存在");
                    }
                    if (shopCart.getStatus() == 99) {
                        throw new ServiceException(500, "购物车已被操作");
                    }
//                        for (Sku sku : skus) {
//                        if (ToolUtil.isEmpty(sku.getQualityPlanId()) && sku.getSkuId().equals(instockRequest.getSkuId())) {
                    InstockList instockList = new InstockList();
                    instockList.setSkuId(instockRequest.getSkuId());
                    if (instockRequest.getNumber() < 0) {
                        throw new ServiceException(500, "不可以出现负数");
                    }
                    instockList.setNumber(instockRequest.getNumber());
                    instockList.setRealNumber(instockRequest.getNumber());
                    instockList.setInstockOrderId(entity.getInstockOrderId());
                    instockList.setInstockNumber(instockRequest.getNumber());
                    instockList.setBrandId(instockRequest.getBrandId());
                    if (ToolUtil.isEmpty(instockRequest.getCustomerId())) {
                        throw new ServiceException(500, "请添加供应商");
                    }
                    instockList.setCustomerId(instockRequest.getCustomerId());
                    instockList.setLotNumber(instockRequest.getLotNumber());
                    instockList.setSerialNumber(instockRequest.getSerialNumber());
                    instockList.setReceivedDate(instockRequest.getReceivedDate());
                    instockList.setManufactureDate(instockRequest.getManufactureDate());
                    instockList.setEffectiveDate(instockRequest.getEffectiveDate());
                    instockList.setStoreHouseId(param.getStoreHouseId());
                    if (ToolUtil.isNotEmpty(instockRequest.getSellingPrice())) {
                        instockList.setSellingPrice(instockRequest.getSellingPrice());
                    }
                    instockListService.save(instockList);
                    instockRequest.setInstockListId(instockList.getInstockListId());

//                        }
//                    }
                }
            }


            //更新主题与来源
            List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
            StringBuffer stringBuffer = new StringBuffer();
            if (ToolUtil.isEmpty(entity.getTheme())) {

                if (ToolUtil.isNotEmpty(param.getCustomerName())) {
                    stringBuffer.append(param.getCustomerName());
                }
                for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                    stringBuffer.append(skuSimpleResult.getSkuName());
                    if (skuSimpleResults.size() > 1) {
                        stringBuffer.append("等");
                        break;
                    }
                }
                stringBuffer.append("的入库单");
                entity.setTheme(stringBuffer.toString());
            }

            entity.setOrigin(getOrigin.newThemeAndOrigin("instockOrder", entity.getInstockOrderId(), entity.getSource(), entity.getSourceId()));
            this.updateById(entity);

            //发起审批流程
            if (ToolUtil.isEmpty(param.getModule())) {
                param.setModule("");
            }
            ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ReceiptsEnum.INSTOCK.name()).eq("status", 99).eq("module", param.getModule()).one();

            if (ToolUtil.isEmpty(activitiProcess)) {
                throw new ServiceException(500, "请先设置入库审批流程");
            }

            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "的入库申请");
            activitiProcessTaskParam.setQTaskId(entity.getInstockOrderId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getInstockOrderId());
            activitiProcessTaskParam.setType(ReceiptsEnum.INSTOCK.name());
            if (param.getDirectInStock()) {
                activitiProcessTaskParam.setStatus(99);
            }
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            if (param.getDirectInStock()) {
                messageProducer.auditMessageDo(new AuditEntity() {{
                    setMessageType(AuditMessageType.COMPLETE);
                    setActivitiProcess(activitiProcess);
                    setTaskId(taskId);
                    setTimes(0);
                    setMaxTimes(1);
                }});
            } else {   // 直接成功
                messageProducer.auditMessageDo(new AuditEntity() {{
                    setMessageType(AuditMessageType.CREATE_TASK);
                    setActivitiProcess(activitiProcess);
                    setTaskId(taskId);
                    setLoginUserId(LoginContextHolder.getContext().getUserId());
                    setTimes(0);
                    setMaxTimes(1);
                }});
            }


            /**
             * 清空购物车
             */
            ShopCart shopCart = new ShopCart();
            shopCart.setDisplay(0);
            shopCart.setStatus(99);
            shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
                eq("type", param.getShopCardType());
                eq("create_user", LoginContextHolder.getContext().getUserId());
            }});


            /**
             * 判断是否直接入库
             * 不直接走审批
             *
             */
            if (param.getDirectInStock()) {
                param.setInstockOrderId(entity.getInstockOrderId());
                inStock(param);//直接入库
                entity.setState(99);
                this.updateById(entity);
            }


            /**
             * 添加动态记录
             */
            RemarksParam remarksParam = new RemarksParam();
            remarksParam.setTaskId(taskId);
            remarksParam.setType("dynamic");
            remarksParam.setContent(LoginContextHolder.getContext().getUser().getName() + "发起了入库申请");
            messageProducer.remarksServiceDo(new RemarksEntity() {{
                setOperationType(OperationType.ADD);
                setRemarksParam(remarksParam);
            }});
//                activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
//                activitiProcessLogService.autoAudit(taskId, 1);
            /**
             * 指定人推送
             */
            if (ToolUtil.isNotEmpty(param.getUserIds())) {
                remarksService.pushPeople(param.getUserIds(), taskId, "你有一条被@的消息");
            }


            /**
             * 内部调用创建质检
             */
//            this.createQualityTask(param, skus);


        }
    }


    /**
     * 直接入库 不走审批
     */
    public void directInStock(InstockOrderParam param) {


    }

    /**
     * 判断入库流程发起人
     *
     * @return
     */
    @Override
    public boolean judgeLoginUser() {
        return activitiProcessService.judgePerson(ReceiptsEnum.INSTOCK.name(), "createInstock");
    }

    /**
     * 添加入库记录
     *
     * @param param
     */
    @Override
    public void addRecord(InstockOrderParam param) {
        if (ToolUtil.isNotEmpty(param)) {

            InstockOrder entity = new InstockOrder();
            ToolUtil.copyProperties(param, entity);
            entity.setDisplay(0);
            this.save(entity);

            List<InstockList> instockLists = BeanUtil.copyToList(param.getListParams(), InstockList.class, new CopyOptions());
            for (InstockList instockList : instockLists) {
                instockList.setInstockOrderId(entity.getInstockOrderId());
            }

            instockListService.saveBatch(instockLists);
        }

    }


    public void createQualityTask(InstockOrderParam param, List<Sku> skus) {
        QualityTaskParam qualityTaskParam = new QualityTaskParam();
        qualityTaskParam.setType("入场");
        List<QualityTaskDetailParam> qualityTaskDetailParams = new ArrayList<>();
        for (InstockListParam instockRequest : param.getListParams()) {
            if (ToolUtil.isNotEmpty(instockRequest)) {
                for (Sku sku : skus) {
                    if (ToolUtil.isNotEmpty(sku.getQualityPlanId()) || sku.getSkuId().equals(instockRequest.getSkuId())) {
                        QualityTaskDetailParam qualityTaskDetailParam = new QualityTaskDetailParam();
                        qualityTaskDetailParam.setQualityPlanId(sku.getQualityPlanId());
                        qualityTaskDetailParam.setQualityPlanId(sku.getQualityPlanId());
                        qualityTaskDetailParam.setNumber(Math.toIntExact(instockRequest.getNumber()));
                        qualityTaskDetailParam.setSkuId(instockRequest.getSkuId());
                        qualityTaskDetailParam.setBrandId(instockRequest.getBrandId());
                        qualityTaskDetailParams.add(qualityTaskDetailParam);
                        break;
                    }
                }
            }
        }
        qualityTaskParam.setDetails(qualityTaskDetailParams);
        qualityTaskParam.setMicroUserId(LoginContextHolder.getContext().getUserId());
        MicroServiceEntity serviceEntity = new MicroServiceEntity();
        serviceEntity.setType(MicroServiceType.QUALITY_TASK);
        serviceEntity.setOperationType(OperationType.ADD);
        String jsonString = JSON.toJSONString(qualityTaskParam);
        serviceEntity.setObject(jsonString);
        serviceEntity.setMaxTimes(2);
        serviceEntity.setTimes(0);
        messageProducer.microService(serviceEntity);


    }

    @Override
    public void checkNumberTrue(Long id, Integer status, Long actionId) {
        if (status != 98) {
            throw new ServiceException(500, "您传入的状态不正确");
        } else {
            messageProducer.auditMessageDo(new AuditEntity() {{
                setAuditType(CHECK_ACTION);
                setMessageType(AuditMessageType.AUDIT);
                setFormId(id);
                setForm("instock");
                setActionId(actionId);
            }});
        }

    }

    @Override
    public void checkNumberFalse(Long id, Integer status) {
        InstockOrder instockOrder = this.getById(id);
        if (status != 49) {
            throw new ServiceException(500, "您传入的状态不正确");
        } else {
            instockOrder.setState(status);
            this.updateById(instockOrder);
        }
    }


    private void currentNode() {
//        ActivitiSteps activitiSteps = getSteps(allSteps, activitiProcessLog.getSetpsId());
    }


    @Override

    public void delete(InstockOrderParam param) {
        this.removeById(getKey(param));
    }

    /**
     * 自由入库
     *
     * @param param
     */
    @Override
    @Transactional
    public void update(InstockOrderParam param) {
    }

    @Override
    public InstockOrderResult findBySpec(InstockOrderParam param) {
        return null;
    }

    @Override
    public List<InstockOrderResult> findListBySpec(InstockOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockOrderResult> findPageBySpec(InstockOrderParam param) {
        Page<InstockOrderResult> pageContext = getPageContext();
        IPage<InstockOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 通过质检创建入库单
     *
     * @param instockParams
     */
    @Override
    @Transactional
    public void addByQuality(InstockParams instockParams) {
        //生成入库编码
        String coding = backCoding(instockParams.getCoding(), instockParams.getStoreHouseId());
        //查询实物
        List<Inkind> inkinds = inkindService.query().in("inkind_id", instockParams.getInkinds()).list();
        //创建入库单
        InstockOrder instockOrder = new InstockOrder();
        instockOrder.setCoding(coding);
        instockOrder.setUserId(instockParams.getUserId());
        instockOrder.setStoreHouseId(instockParams.getStoreHouseId());
        this.save(instockOrder);
        //通过实物id查询sku和brand  创建入库清单
        List<InstockList> instockLists = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            inkind.setSource("质检");
            inkind.setType("0");
            //创建入库清单
            InstockList instockList = new InstockList();
            instockList.setSkuId(inkind.getSkuId());
            instockList.setBrandId(inkind.getBrandId());
            instockList.setNumber(inkind.getNumber());
            instockList.setInstockNumber(inkind.getNumber());
            instockList.setInstockOrderId(instockOrder.getInstockOrderId());
            instockList.setStoreHouseId(instockParams.getStoreHouseId());
            instockLists.add(instockList);
        }
        instockListService.saveBatch(instockLists);
        inkindService.updateBatchById(inkinds);
        //推送消息
        BusinessTrack businessTrack = new BusinessTrack();
        businessTrack.setType("代办");
        businessTrack.setMessage("入库");
        businessTrack.setUserId(instockParams.getUserId());
        businessTrack.setNote("有物料需要入库");
        DateTime data = new DateTime();
        businessTrack.setTime(data);
        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(instockOrder.getInstockOrderId());
        backCodeRequest.setSource("instock");
        Long aLong = orCodeService.backCode(backCodeRequest);
        String url = instockParams.getUrl().replace("codeId", aLong.toString());
        instockSendTemplate.setBusinessTrack(businessTrack);
        instockSendTemplate.setUrl(url);
        instockSendTemplate.sendTemplate();
    }

    /**
     * 通过物料自由入库
     *
     * @param freeInStockParam
     */
    @Override
    @Transactional
    public void freeInstock(FreeInStockParam freeInStockParam) {

        //判断库位绑定
        List<StorehousePositionsBind> positionsBinds = positionsBindService.query().eq("position_id", freeInStockParam.getPositionsId()).list();
        List<Inkind> inkinds = inkindService.listByIds(freeInStockParam.getInkindIds());
        Map<Long, Long> positionsMap = new HashMap<>();
        Map<Long, Long> houseMap = new HashMap<>();
        for (Inkind inkind : inkinds) {
            positionsMap.put(inkind.getInkindId(), freeInStockParam.getPositionsId());
            houseMap.put(inkind.getInkindId(), freeInStockParam.getStoreHouseId());
        }
        instock(inkinds, positionsMap, positionsBinds, houseMap);  //入库
    }

    @Override
    public void formatDetail(InstockOrderResult orderResult) {

        List<InstockLogResult> logResults = instockLogService.listByInstockOrderId(orderResult.getInstockOrderId());
        orderResult.setLogResults(logResults);

        List<Long> userIds = new ArrayList<>();
        userIds.add(orderResult.getUserId());
        userIds.add(orderResult.getCreateUser());
        userIds.add(orderResult.getUpdateUser());
        userIds.add(orderResult.getStockUserId());
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        for (User user : users) {
            if (ToolUtil.isNotEmpty(orderResult.getUserId())) {
                if (orderResult.getUserId().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    orderResult.setUserResult(userResult);
                }
                if (ToolUtil.isNotEmpty(orderResult.getCreateUser()) && orderResult.getCreateUser().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();

                    ToolUtil.copyProperties(user, userResult);
                    orderResult.setCreateUserResult(userResult);
                }
                if (ToolUtil.isNotEmpty(orderResult.getStockUserId()) && orderResult.getStockUserId().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    orderResult.setStockUserResult(userResult);
                }
                if (ToolUtil.isNotEmpty(orderResult.getUpdateUser()) && orderResult.getUpdateUser().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    orderResult.setUpdateUserResult(userResult);
                }
            }

        }
        if (ToolUtil.isNotEmpty(orderResult)) {
            List<InstockList> instockLists = instockListService.query().eq("instock_order_id", orderResult.getInstockOrderId()).list();
            List<InstockListResult> listResults = BeanUtil.copyToList(instockLists, InstockListResult.class, new CopyOptions());

            long enoughNumber = 0L;
            long realNumber = 0L;
            for (InstockListResult listResult : listResults) {
                enoughNumber = enoughNumber + listResult.getNumber();
                realNumber = realNumber + listResult.getRealNumber();
            }
            orderResult.setEnoughNumber(enoughNumber);
            orderResult.setRealNumber(realNumber);
            orderResult.setNotNumber(enoughNumber - realNumber);


            listResults.removeIf(i -> i.getRealNumber() == 0);
            instockListService.format(listResults);
            orderResult.setInstockListResults(listResults);
            List<Long> skuIds = new ArrayList<>();
            for (InstockList instockList : instockLists) {
                skuIds.add(instockList.getSkuId());
            }
            List<StorehousePositionsResult> results = positionsBindService.bindTreeView(skuIds);  //返回树形结构
            orderResult.setBindTreeView(results);
        }
    }

    @Override
    public void formatResult(InstockOrderResult result) {
        DocumentsStatusResult statusResult = documentStatusService.detail(result.getStatus());
        result.setStatusResult(statusResult);
    }


    @Override
    @Transactional
    public List<Long> inStock(InstockOrderParam param) {
        List<Long> inkindIds = new ArrayList<>();

        for (InstockListParam listParam : param.getListParams()) {
            listParam.setInstockOrderId(param.getInstockOrderId());
            if (ToolUtil.isNotEmpty(listParam.getInkindIds())) {   //直接入库
                handle(listParam, listParam.getInkindIds());
            } else {   //创建实物入库
                if (listParam.getBatch()) {   //批量
                    Long inKind = createInKind(listParam);
                    handle(listParam, inKind);
                    inkindIds.add(inKind);
                } else {
                    Long i = listParam.getNumber();
                    for (long aLong = 0; aLong < i; aLong++) {    //单个入库
                        listParam.setNumber(1L);
                        Long inKind = createInKind(listParam);
                        handle(listParam, inKind);
                        inkindIds.add(inKind);
                    }
                    listParam.setNumber(i);
                }
            }
            updateStatus(listParam);

            /**
             * 添加入库记录
             */
            InstockLogDetail instockLogDetail = new InstockLogDetail();
            instockLogDetail.setInstockOrderId(param.getInstockOrderId());
            instockLogDetail.setSkuId(listParam.getSkuId());
            instockLogDetail.setType("normal");
            instockLogDetail.setBrandId(listParam.getBrandId());
            instockLogDetail.setCustomerId(listParam.getCustomerId());
            instockLogDetail.setStorehousePositionsId(listParam.getStorehousePositionsId());
            instockLogDetail.setNumber(listParam.getNumber());
            instockLogDetailService.save(instockLogDetail);
        }
        /**
         * 添加动态
         */
        Long taskId = activitiProcessTaskService.getTaskIdByFormId(param.getInstockOrderId());
        RemarksParam remarksParam = new RemarksParam();
        remarksParam.setTaskId(taskId);
        remarksParam.setType("dynamic");
        remarksParam.setCreateUser(LoginContextHolder.getContext().getUserId());
        remarksParam.setContent(LoginContextHolder.getContext().getUser().getName() + "操作了入库");
        messageProducer.remarksServiceDo(new RemarksEntity() {{
            setOperationType(OperationType.ADD);
            setRemarksParam(remarksParam);
        }});

        /**
         * 更新单据状态
         */
        boolean b = instockOrderComplete(param.getInstockOrderId());
        if (b) {
            /**
             * 消息队列完成动作
             */

            activitiProcessLogService.checkAction(param.getInstockOrderId(), "INSTOCK", param.getActionId(), LoginContextHolder.getContext().getUserId());
//            messageProducer.auditMessageDo(new AuditEntity() {{
//                setAuditType(CHECK_ACTION);
//                setMessageType(AuditMessageType.AUDIT);
//                setFormId(param.getInstockOrderId());
//                setForm("INSTOCK");
//                setMaxTimes(2);
//                setTimes(1);
//                setActionId(param.getActionId());
//            }});
            InstockOrder order = this.getById(param.getInstockOrderId());
            remarksParam.setContent("单据:" + order.getCoding() + "完成了入库");
            remarksParam.setType("dynamic");
            messageProducer.remarksServiceDo(new RemarksEntity() {{
                setOperationType(OperationType.ADD);
                setRemarksParam(remarksParam);
            }});

        }

        return inkindIds;
    }

    /**
     * 更新清单 和 购物车
     *
     * @param listParam
     */
    private void updateStatus(InstockListParam listParam) {

        if (ToolUtil.isEmpty(listParam.getInstockListId())) {
            throw new ServiceException(500, "参数错误");
        }
        /**
         * 异常可入库
         */
        if (ToolUtil.isNotEmpty(listParam.getType()) && listParam.getType().equals("instockByAnomaly")) {
            Anomaly anomaly = anomalyService.getById(listParam.getInstockListId());
            listParam.setInstockListId(anomaly.getSourceId());
        }

        InstockList instockList = instockListService.getById(listParam.getInstockListId());
        if (ToolUtil.isEmpty(instockList)) {
            throw new ServiceException(500, "参数不正确");
        }
        instockList.setRealNumber(instockList.getRealNumber() - listParam.getNumber());
        if (instockList.getRealNumber() < 0) {
            throw new ServiceException(500, "当前入库数量与单据数量不符");
        }
        if (instockList.getRealNumber() == 0) {
            instockList.setStatus(99L);
        }
        instockListService.updateById(instockList);

        if (ToolUtil.isNotEmpty(listParam.getCartId())) {
            ShopCart shopCart = cartService.getById(listParam.getCartId());
            if (shopCart.getNumber() != 0) {
                shopCart.setNumber(shopCart.getNumber() - listParam.getNumber());
                if (shopCart.getNumber() < 0) {
                    throw new ServiceException(500, "购物车数量不正确");
                }
            }else {
                shopCart.setStatus(99);
            }
            cartService.updateById(shopCart);
        }

    }

    /**
     * 创建实物 绑定二维码
     *
     * @param param
     * @return
     */
    private Long createInKind(InstockListParam param) {
        Inkind inkind = new Inkind();
        inkind.setNumber(param.getNumber());
        inkind.setSkuId(param.getSkuId());
        inkind.setCustomerId(param.getCustomerId());
        inkind.setSource("入库");
        inkind.setSourceId(param.getInstockListId());
        inkind.setType("1");
        inkind.setBrandId(param.getBrandId());
        inkindService.save(inkind);

        OrCode orCode = new OrCode();
        orCode.setState(1);
        orCode.setType("item");
        orCodeService.save(orCode);

        OrCodeBindParam bindParam = new OrCodeBindParam();
        bindParam.setOrCodeId(orCode.getOrCodeId());
        bindParam.setFormId(inkind.getInkindId());
        bindParam.setSource("item");
        orCodeBindService.add(bindParam);

        return inkind.getInkindId();
    }

    /**
     * 入库操作
     *
     * @param param
     * @param inkindId
     */
    private void handle(InstockListParam param, Long inkindId) {


        StockDetails stockDetails = new StockDetails();
        stockDetails.setSkuId(param.getSkuId());
        stockDetails.setBrandId(param.getBrandId());
        stockDetails.setCustomerId(param.getCustomerId());
        stockDetails.setInkindId(inkindId);
        stockDetails.setStorehousePositionsId(param.getStorehousePositionsId());
        stockDetails.setNumber(param.getNumber());

        if (ToolUtil.isEmpty(param.getStorehousePositionsId())) {
            throw new ServiceException(500, "存在物料缺少库位信息");
        }
        StorehousePositions storehousePositions = positionsService.getById(param.getStorehousePositionsId());
        stockDetails.setStorehouseId(storehousePositions.getStorehouseId());
        stockDetailsService.save(stockDetails);

    }

    private void handle(InstockListParam param, List<Long> inkindIds) {

        List<StockDetails> stockDetailList = new ArrayList<>();
        for (Long inkindId : inkindIds) {
            StockDetails stockDetails = new StockDetails();
            stockDetails.setSkuId(param.getSkuId());
            stockDetails.setBrandId(param.getBrandId());
            stockDetails.setCustomerId(param.getCustomerId());
            stockDetails.setInkindId(inkindId);
            stockDetails.setStorehousePositionsId(param.getStorehousePositionsId());
            stockDetails.setNumber(param.getNumber());
            stockDetails.setStorehouseId(param.getStoreHouseId());
            stockDetailList.add(stockDetails);
        }

        stockDetailsService.saveBatch(stockDetailList);
    }


    /**
     * 通过入库单入库
     */
    @Override
    public boolean inStockByOrder(InStockByOrderParam param) {
        List<InStockByOrderParam.SkuParam> skuParams = param.getSkuParams();


        List<Long> positionIds = new ArrayList<>();
        List<Long> instockListIds = new ArrayList<>();


        for (InStockByOrderParam.SkuParam skuParam : skuParams) {
            instockListIds.add(skuParam.getInStockListId());
            positionIds.add(skuParam.getPositionId());
        }

        List<InstockList> instockLists = instockListIds.size() == 0 ? new ArrayList<>() : instockListService.listByIds(instockListIds);
        Map<Long, Long> houseByPositionId = positionsService.getHouseByPositionId(positionIds);
        Map<Long, InstockList> instockListMap = new HashMap<>();

        if (ToolUtil.isEmpty(instockLists)) {
            throw new ServiceException(500, "缺少入库单");
        }
        InstockOrder instockOrder = this.getById(instockLists.get(0).getInstockOrderId());
        InstockLog instockLog = new InstockLog();
        instockLog.setInstockOrderId(instockOrder.getInstockOrderId());
        instockLog.setRemark(param.getRemark());
        instockLog.setInstockTime(param.getInstockTime());

        instockLogService.save(instockLog);

        for (InstockList instockList : instockLists) {
            instockListMap.put(instockList.getInstockListId(), instockList);
        }


        /**
         * 入库操作
         */
        List<StockDetails> stockDetailsList = new ArrayList<>();
        List<InstockLogDetail> instockLogDetails = new ArrayList<>();

        for (InStockByOrderParam.SkuParam skuParam : skuParams) {
            List<Inkind> inkinds = skuParam.getInkindIds().size() == 0 ? new ArrayList<>() : inkindService.listByIds(skuParam.getInkindIds());

            if (ToolUtil.isNotEmpty(skuParam.getStockNumber()) && skuParam.getStockNumber() > 0) {   //盘点
                List<StockDetails> stockDetails = stockDetailsService.query().eq("sku_id", skuParam.getSkuId()).eq("storehouse_positions_id", skuParam.getPositionId()).list();
                for (int i = 0; i < stockDetails.size(); i++) {
                    if (i == 0) {
                        StockDetails details = stockDetails.get(0);
                        details.setNumber(skuParam.getStockNumber());
                    } else {
                        StockDetails details = stockDetails.get(i);
                        details.setNumber(0L);
                    }
                }
                stockDetailsService.updateBatchById(stockDetails);
            }


            long number = 0L;
            for (Inkind inKind : inkinds) {
                //修改清单数量
                InstockList instockList = instockListMap.get(skuParam.getInStockListId());
                instockList.setRealNumber(instockList.getRealNumber() - inKind.getNumber());
                if (instockList.getRealNumber() < 0) {
                    throw new ServiceException(500, "当前入库数量与单据数量不符");
                }
                instockLists.add(instockList);

                StockDetails stockDetails = new StockDetails();
                stockDetails.setNumber(inKind.getNumber());
                stockDetails.setStorehousePositionsId(skuParam.getPositionId());
                stockDetails.setInkindId(inKind.getInkindId());
                stockDetails.setStorehouseId(houseByPositionId.get(skuParam.getPositionId()));
                stockDetails.setCustomerId(inKind.getCustomerId() == null ? null : inKind.getCustomerId());
                stockDetails.setBrandId(inKind.getBrandId() == null ? null : inKind.getBrandId());
                stockDetails.setSkuId(inKind.getSkuId());
                stockDetailsList.add(stockDetails);
                inKind.setType("1");

                number = number + inKind.getNumber();
            }
            InstockLogDetail instockLogDetail = new InstockLogDetail();
            instockLogDetail.setSkuId(skuParam.getSkuId());
            instockLogDetail.setNumber(number);
            instockLogDetail.setInstockOrderId(instockOrder.getInstockOrderId());
            instockLogDetail.setInstockLogId(instockLog.getInstockLogId());
            instockLogDetails.add(instockLogDetail);

            inkindService.updateBatchById(inkinds);
        }

        instockLogDetailService.saveBatch(instockLogDetails);
        instockListService.updateBatchById(instockLists);
        stockDetailsService.saveBatch(stockDetailsList);

        boolean orderComplete = instockOrderComplete(instockOrder.getInstockOrderId());
        if (orderComplete) {
            /**
             * 消息队列完成动作
             */
            messageProducer.auditMessageDo(new AuditEntity() {{
                setAuditType(CHECK_ACTION);
                setMessageType(AuditMessageType.AUDIT);
                setFormId(param.getInstockOrderId());
                setForm("instock");
                setActionId(param.getActionId());
            }});
        }
        return orderComplete;
    }


    /**
     * 入库单完成状态
     *
     * @param orderId
     */
    @Override
    public boolean instockOrderComplete(Long orderId) {
        if (ToolUtil.isEmpty(orderId)) {
            return false;
        }

        List<InstockList> instockLists = instockListService.query().eq("instock_order_id", orderId).list();
        boolean t = true;

        for (InstockList instockList : instockLists) {
            if (instockList.getRealNumber() != 0) {
                t = false;
                break;
            }
        }
        if (t) {
            InstockOrder order = this.getById(orderId);
            order.setState(99);
            this.updateById(order);

        }
        return t;
    }

    /**
     * 通过库位自由入库
     */
    @Override
    @Transactional
    public void freeInStockByPositions(FreeInStockParam stockParam) {

        List<FreeInStockParam.PositionsInStock> inStocks = stockParam.getInStocks();
        List<Long> inkindIds = new ArrayList<>();
        Map<Long, Long> positionsMap = new HashMap<>();
        Map<Long, Long> houseMap = new HashMap<>();

        for (FreeInStockParam.PositionsInStock inStock : inStocks) {  //先取实物
            inkindIds.add(inStock.getInkind());
            positionsMap.put(inStock.getInkind(), inStock.getPositionsId());  //实物对应的库位
            houseMap.put(inStock.getInkind(), inStock.getStoreHouseId());
        }

        List<Long> positionsIds = new ArrayList<Long>() {{
            for (FreeInStockParam.PositionsInStock inStock : inStocks) {
                add(inStock.getPositionsId());
            }
        }};

        List<StorehousePositionsBind> positionsBinds = positionsBindService.query().in("position_id", positionsIds).list();

        List<Inkind> inkinds = inkindService.listByIds(inkindIds);

        instock(inkinds, positionsMap, positionsBinds, houseMap);  //入库
    }

    /**
     * 入库操作逻辑
     *
     * @param inkinds   实物
     * @param positions 库位
     * @param binds     绑定关系
     * @param houseId   仓库
     */
    private void instock(List<Inkind> inkinds, Map<Long, Long> positions, List<StorehousePositionsBind> binds, Map<Long, Long> houseId) {

        List<StockDetails> stockDetailsList = new ArrayList<>();
        List<Long> inkindIds = new ArrayList<>();

        for (Inkind inkind : inkinds) {  //获取二维码
            inkindIds.add(inkind.getInkindId());
        }
        List<OrCodeBind> codeBinds = bindService.query().in("form_id", inkindIds).list();
        Map<Long, Long> qrMap = new HashMap<>();

        for (OrCodeBind bind : codeBinds) {
            for (Inkind inkind : inkinds) {
                if (bind.getFormId().equals(inkind.getInkindId())) {
                    qrMap.put(inkind.getInkindId(), bind.getOrCodeId());
                    break;
                }
            }
        }


        InstockLog instockLog = new InstockLog();   //添加入库记录
        instockLog.setInstockTime(new DateTime());
        instockLogService.save(instockLog);

        List<InstockLogDetail> instockLogDetails = new ArrayList<>();

        List<InstockListParam> instockListParams = new ArrayList<>();
        for (Inkind inkind : inkinds) {
//            if (judgePosition(binds, inkind)) {
//                throw new ServiceException(500, "入库的物料 未和库位绑定");
//            }
            InstockListParam instockListParam = new InstockListParam();
            instockListParam.setNumber(inkind.getNumber());
            instockListParam.setSkuId(inkind.getSkuId());
            instockListParams.add(instockListParam);

            StockDetails stockDetails = new StockDetails();
            stockDetails.setNumber(inkind.getNumber());
            stockDetails.setStorehousePositionsId(positions.get(inkind.getInkindId()));
            stockDetails.setQrCodeId(qrMap.get(inkind.getInkindId()));
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setStorehouseId(houseId.get(inkind.getInkindId()));
            stockDetails.setCustomerId(inkind.getCustomerId());
            stockDetails.setBrandId(inkind.getBrandId());
            stockDetails.setSkuId(inkind.getSkuId());
            stockDetailsList.add(stockDetails);
            inkind.setType("1");

            InstockLogDetail logDetail = new InstockLogDetail();
            logDetail.setInstockLogId(instockLog.getInstockLogId());
            logDetail.setNumber(inkind.getNumber());
            logDetail.setInkindId(inkind.getInkindId());
            logDetail.setStorehousePositionsId(positions.get(inkind.getInkindId()));
            logDetail.setStorehouseId(houseId.get(inkind.getInkindId()));
            logDetail.setSkuId(inkind.getSkuId());
            instockLogDetails.add(logDetail);

        }
        instockLogDetailService.saveBatch(instockLogDetails);
        stockDetailsService.saveBatch(stockDetailsList);
        inkindService.updateBatchById(inkinds);


        addInStockRecord(instockListParams, "自由入库记录");  //添加记录
    }

    /**
     * 添加入库记录
     *
     * @param instockListParams
     */
    @Override
    public void addInStockRecord(List<InstockListParam> instockListParams, String source) {
        InstockOrderParam param = new InstockOrderParam();  //往入库单中添加记录
        param.setSource(source);
        param.setState(60);
        param.setDisplay(0);
        param.setCreateUser(LoginContextHolder.getContext().getUserId());
        param.setListParams(instockListParams);

        MicroServiceEntity microServiceEntity = new MicroServiceEntity(); //添加入库记录
        microServiceEntity.setOperationType(OperationType.SAVE);
        microServiceEntity.setType(MicroServiceType.INSTOCKORDER);
        microServiceEntity.setObject(param);
        microServiceEntity.setMaxTimes(2);
        microServiceEntity.setTimes(0);
        messageProducer.microService(microServiceEntity);
    }

    /**
     * 判断实物有无库存
     *
     * @param inkind
     * @param stocks
     * @return
     */
    @Override
    public Stock judgeStockExist(Inkind inkind, List<Stock> stocks) {

        for (Stock stock : stocks) {
            if (stock.getSkuId().equals(inkind.getSkuId()) && stock.getBrandId().equals(inkind.getBrandId())) {
                return stock;
            }
        }
        return null;
    }

    /**
     * 判断物料 供应商 品牌 是否绑定
     *
     * @param inkind
     * @param supplies
     * @return
     */
    @Override
    public boolean judgeSkuBind(Inkind inkind, List<Supply> supplies) {
        try {
            for (Supply supply : supplies) {
                if (inkind.getSkuId().equals(supply.getSkuId()) && inkind.getBrandId().equals(supply.getBrandId()) && supply.getCustomerId().equals(inkind.getCustomerId())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 当前物料和库位是否绑定
     *
     * @param positionsBinds
     * @param inkind
     * @return
     */
    @Override
    public Boolean judgePosition(List<StorehousePositionsBind> positionsBinds, Inkind inkind) {

        for (StorehousePositionsBind positionsBind : positionsBinds) {
            if (positionsBind.getSkuId().equals(inkind.getSkuId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateStatus(ActivitiProcessTask processTask) {

        InstockOrder order = this.getById(processTask.getFormId());
        if (order.getState() != 50) {
            DocumentsAction action = documentsActionService.query().eq("form_type", ReceiptsEnum.INSTOCK.name()).eq("action", InStockActionEnum.verify.name()).eq("display", 1).one();
            messageProducer.auditMessageDo(new AuditEntity() {{
                setAuditType(CHECK_ACTION);
                setMessageType(AuditMessageType.AUDIT);
                setFormId(order.getInstockOrderId());
                setForm(ReceiptsEnum.INSTOCK.name());
                setActionId(action.getDocumentsActionId());
            }});

        }

    }

    @Override
    public void updateCreateInstockStatus(ActivitiProcessTask processTask) {
        InstockOrder order = this.getById(processTask.getFormId());
        List<InstockList> instockLists = instockListService.lambdaQuery().eq(InstockList::getInstockOrderId, order.getInstockOrderId()).eq(InstockList::getDisplay, 1).list();

        boolean t = true;
        for (InstockList instockList : instockLists) {
            if (instockList.getRealNumber() != 0) {
                t = false;
            }
        }
        if (t) {
            order.setStatus(InStockActionEnum.done.getStatus());
            this.updateById(order);
            processTask.setStatus(99);
            activitiProcessTaskService.updateById(processTask);
        }

    }

    @Override
    public void updateRefuseStatus(ActivitiProcessTask processTask) {
        InstockOrder order = this.getById(processTask.getFormId());
        order.setState(50);
        this.updateById(order);
    }

    @Override
    public void updateCreateInstockRefuseStatus(ActivitiProcessTask processTask) {
        InstockOrder order = this.getById(processTask.getFormId());
        order.setState(-1);
        this.updateById(order);
    }


    private Serializable getKey(InstockOrderParam param) {
        return param.getInstockOrderId();
    }

    private Page<InstockOrderResult> getPageContext() {
        List<String> fields = new ArrayList<>();
        fields.add("storeHouseId");
        fields.add("createTime");
        fields.add("userId");
        return PageFactory.defaultPage(fields);
    }

    /**
     * 入库生成编码
     *
     * @param coding
     * @return
     */
    String backCoding(Long coding, Long storeHouseId) {
        String cod = null;
        //添加编码
        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", coding).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", storeHouseId).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                String replace = "";
                if (ToolUtil.isNotEmpty(storehouse.getCoding())) {
                    replace = backCoding.replace("${storehouse}", storehouse.getCoding());
                } else {
                    replace = backCoding.replace("${storehouse}", "");
                }
                cod = replace;
            }
        }
        return cod;
    }

    private InstockOrder getOldEntity(InstockOrderParam param) {
        return this.getById(getKey(param));
    }

    private InstockOrder getEntity(InstockOrderParam param) {
        InstockOrder entity = new InstockOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void setList(List<InstockOrderResult> data) {

        List<Long> orderIds = new ArrayList<>();
        for (InstockOrderResult datum : data) {
            orderIds.add(datum.getInstockOrderId());
        }
        List<InstockList> instockListList = orderIds.size() == 0 ? new ArrayList<>() : instockListService.query().in("instock_order_id", orderIds).list();
        List<InstockListResult> instockListResults = BeanUtil.copyToList(instockListList, InstockListResult.class, new CopyOptions());
        format(data);

        instockListService.format(instockListResults);

        for (InstockOrderResult datum : data) {
            List<InstockListResult> results = new ArrayList<>();
            for (InstockListResult instockListResult : instockListResults) {
                if (instockListResult.getInstockOrderId().equals(datum.getInstockOrderId())) {
                    results.add(instockListResult);
                }
            }
            datum.setInstockListResults(results);
        }
    }


    @Override
    public void format(List<InstockOrderResult> data) {
        List<Long> userIds = new ArrayList<>();
        List<Long> storeIds = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        List<Long> statusIds = new ArrayList<>();
        List<Long> noticeIds = new ArrayList<>();
        List<Long> mediaIds = new ArrayList<>();

        for (InstockOrderResult datum : data) {
            if (ToolUtil.isNotEmpty(datum.getNoticeId())) {
                List<Long> noticeId = JSON.parseArray(datum.getNoticeId(), Long.class);
                noticeIds.addAll(noticeId);
                datum.setNoticeIds(noticeId);
            }
            if (ToolUtil.isNotEmpty(datum.getEnclosure())) {
                List<Long> mediaId = JSON.parseArray(datum.getEnclosure(), Long.class);
                datum.setMediaIds(mediaId);
                mediaIds.addAll(mediaId);
            }
            userIds.add(datum.getUserId());
            if (ToolUtil.isNotEmpty(datum.getStockUserId())) {
                userIds.add(datum.getStockUserId());
            }
            storeIds.add(datum.getStoreHouseId());
            orderIds.add(datum.getInstockOrderId());
            if (ToolUtil.isNotEmpty(datum.getStatus())) {
                statusIds.add(datum.getStatus());
            }
        }

        List<Announcements> announcements = noticeIds.size() == 0 ? new ArrayList<>() : announcementsService.listByIds(noticeIds);
        List<DocumentsStatusResult> documentsStatusResults = documentStatusService.resultsByIds(statusIds);
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();
        List<InstockListResult> instockListList = instockListService.getListByOrderIds(orderIds);

        for (InstockOrderResult datum : data) {

            long enoughNumber = 0L;
            long realNumber = 0L;
            List<InstockListResult> instockListResults = new ArrayList<>();

            for (InstockListResult instockList : instockListList) {
                if (datum.getInstockOrderId().equals(instockList.getInstockOrderId())) {
                    instockListResults.add(instockList);

                    enoughNumber = ToolUtil.isEmpty(instockList.getRealNumber()) ? 0 : enoughNumber + instockList.getNumber();
                    realNumber = ToolUtil.isEmpty(instockList.getRealNumber()) ? 0 : realNumber + instockList.getRealNumber();
                }
            }
            datum.setInstockListResults(instockListResults);
            datum.setEnoughNumber(enoughNumber);
            datum.setRealNumber(realNumber);
            datum.setNotNumber(enoughNumber - realNumber);

            for (DocumentsStatusResult documentsStatusResult : documentsStatusResults) {
                if (ToolUtil.isNotEmpty(datum.getStatus()) && datum.getStatus().equals(documentsStatusResult.getDocumentsStatusId())) {
                    datum.setStatusResult(documentsStatusResult);
                    break;
                }
            }


            for (User user : users) {
                if (ToolUtil.isNotEmpty(datum.getUserId())) {
                    if (datum.getUserId().equals(user.getUserId())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        datum.setUserResult(userResult);
                    }
                    if (ToolUtil.isNotEmpty(datum.getStockUserId()) && datum.getStockUserId().equals(user.getUserId())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        datum.setStockUserResult(userResult);
                    }
                }

            }
            for (Storehouse storehouse : storehouses) {
                if (storehouse.getStorehouseId().equals(datum.getStoreHouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }

            List<String> url = new ArrayList<>();
            for (Long mediaId : mediaIds) {
                String mediaUrl = mediaService.getMediaUrl(mediaId, 0L);
                url.add(mediaUrl);
            }
            datum.setUrl(url);

            List<Announcements> announcementsList = new ArrayList<>();
            for (Announcements announcement : announcements) {
                if (ToolUtil.isNotEmpty(datum.getNoticeIds()) && datum.getNoticeIds().stream().anyMatch(i -> i.equals(announcement.getNoticeId()))) {
                    announcementsList.add(announcement);
                }
            }
            datum.setAnnouncementsList(announcementsList);
        }
    }
}
