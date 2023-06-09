package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.InStockActionEnum;
import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.InventoryCorrectionParam;
import cn.atsoft.dasheng.app.model.request.InstockView;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.enums.StockLogDetailSourceEnum;
import cn.atsoft.dasheng.erp.enums.StockLogTypeEnum;
import cn.atsoft.dasheng.erp.mapper.InstockOrderMapper;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.request.InstockViewDetail;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.pojo.InStockByOrderParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.message.enmu.AuditEnum.CHECK_ACTION;
import static org.apache.http.client.methods.RequestBuilder.put;

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
    private SupplyService supplyService;
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
    private ActivitiProcessLogV1Service activitiProcessLogService;
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
    @Autowired
    private InstockHandleService instockHandleService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private ProductionPickListsService pickListsService;
    @Autowired
    private AllocationService allocationService;
    @Autowired
    private AllocationDetailService allocationDetailService;
    @Autowired
    private AllocationCartService allocationCartService;
    @Autowired
    private InstockReceiptService instockReceiptService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SkuHandleRecordService skuHandleRecordService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private StockLogService stockLogService;
    @Autowired
    private StockLogDetailService stockLogDetailService;

    @Autowired
    private SerialNumberService serialNumberService;


    @Override
    @Transactional
    public InstockOrder add(InstockOrderParam param) {

        List<OrderDetail> orderDetails = new ArrayList<>();
        Map<Long, Long> arrivalNumber = new HashMap<>();

        if (ToolUtil.isEmpty(param.getListParams())) {
            throw new ServiceException(500, "入库物料不能为空");
        } else if (ToolUtil.isNotEmpty(param.getOrderId())) {
            Order order = orderService.getById(param.getOrderId());
            if (ToolUtil.isEmpty(order)) {
                throw new ServiceException(500, "订单错误");
            }
            List<InstockListParam> listParams = param.getListParams();
            List<Long> orderDetailIds = new ArrayList<>();
            for (InstockListParam listParam : listParams) {
                orderDetailIds.add(listParam.getOrderDetailId());
                arrivalNumber.put(listParam.getOrderDetailId(), listParam.getNumber());
            }

            orderDetails = orderDetailService.list(new QueryWrapper<OrderDetail>() {{
                in("detail_id", orderDetailIds);
                eq("order_id", param.getOrderId());
            }});

            if (orderDetailIds.size() != orderDetails.size()) {
                throw new ServiceException(500, "返回行数不同");
            }
            /**
             * 通过详情Id，到详情表中取出详情数据。
             * 对比条数：id数量，与返回行数不同为有错误。抛出异常。只有相等的时候才是正常的。
             */

        }

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "1").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                param.setCoding(codingRulesService.genSerial());
            }
        }


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
        String origin = this.getOrigin.newThemeAndOrigin("INSTOCK", entity.getInstockOrderId(), ToolUtil.isEmpty(param.getSource()) ? null : param.getSource(), ToolUtil.isEmpty(param.getSourceId()) ? null : param.getSourceId());
        entity.setOrigin(origin);
        entity.setInstockType(entity.getInstockType());
        this.updateById(entity);


        /**
         * 被调用类型  判断购物车和一些条件
         */
        boolean t = true;
        if (ToolUtil.isNotEmpty(param.getType())) {
            switch (param.getType()) {
                case "resubmit":
                case "调拨入库":
                    t = false;
                    break;
                case "purchaseOrder":
                    t = false;
                    break;
            }
        }


        List<Long> skuIds = new ArrayList<>();
        for (InstockListParam instockRequest : param.getListParams()) {
            skuIds.add(instockRequest.getSkuId());
        }
        for (InstockListParam instockRequest : param.getListParams()) {

            if (ToolUtil.isNotEmpty(instockRequest)) {
                if (t) {    //是否判断购物车
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
                    if (ToolUtil.isEmpty(instockRequest.getCustomerId())) {
                        throw new ServiceException(500, "请添加供应商");
                    }
                }

                InstockList instockList = new InstockList();
                instockList.setSkuId(instockRequest.getSkuId());
                if (instockRequest.getNumber() < 0) {
                    throw new ServiceException(500, "不可以出现负数");
                }
                instockList.setNumber(instockRequest.getNumber());
                instockList.setRealNumber(instockRequest.getNumber());
                instockList.setInstockOrderId(entity.getInstockOrderId());
                instockList.setInstockNumber(0L);
                instockList.setDetailId(instockRequest.getOrderDetailId());
                instockList.setBrandId(instockRequest.getBrandId());
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

        entity.setOrigin(getOrigin.newThemeAndOrigin("instockOrder", entity.getInstockOrderId(), ToolUtil.isEmpty(entity.getSource()) ? null : entity.getSource(), ToolUtil.isEmpty(entity.getSourceId()) ? null : entity.getSourceId()));
        this.updateById(entity);

        //发起审批流程
        if (ToolUtil.isEmpty(param.getModule())) {
            param.setModule("createInstock");
        }
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ReceiptsEnum.INSTOCK.name()).eq("status", 99).eq("module", param.getModule()).eq("display", 1).one();

        if (ToolUtil.isEmpty(activitiProcess)) {
            throw new ServiceException(500, "请先设置入库审批流程");
        }

        LoginUser user = LoginContextHolder.getContext().getUser();
        ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
        activitiProcessTaskParam.setTaskName(user.getName() + "发起的入库申请");
        activitiProcessTaskParam.setQTaskId(entity.getInstockOrderId());
        activitiProcessTaskParam.setTheme(param.getTheme());
        activitiProcessTaskParam.setFormId(entity.getInstockOrderId());
        activitiProcessTaskParam.setType(ReceiptsEnum.INSTOCK.name());
        activitiProcessTaskParam.setRemark(entity.getRemark());
        activitiProcessTaskParam.setNoticeId(announcementsService.toList(entity.getNoticeId()));
        activitiProcessTaskParam.setTheme(param.getTheme());
        if (ToolUtil.isNotEmpty(entity.getSource()) && ToolUtil.isNotEmpty(entity.getSourceId())) {
            activitiProcessTaskParam.setSource(entity.getSource());
            activitiProcessTaskParam.setSourceId(entity.getSourceId());
        }
        if (ToolUtil.isNotEmpty(entity.getSource()) && entity.getSource().equals("processTask") && ToolUtil.isNotEmpty(entity.getSourceId())) {
            activitiProcessTaskParam.setPid(param.getPid());
        }
        if (param.getDirectInStock()) {
            activitiProcessTaskParam.setStatus(99);
        }
        activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
        if (ToolUtil.isNotEmpty(entity.getTheme())) {
            activitiProcessTaskParam.setTheme(entity.getTheme());
        }
        Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
        entity.setTaskId(taskId);
        this.updateById(entity);
        //添加log
        if (param.getDirectInStock()) {   // 直接入库
//                messageProducer.auditMessageDo(new AuditEntity() {{
//                    setMessageType(AuditMessageType.COMPLETE);
//                    setActivitiProcess(activitiProcess);
//                    setTaskId(taskId);
//                    setTimes(0);
//                    setMaxTimes(1);
//                }});
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId, 1, LoginContextHolder.getContext().getUserId());
            entity.setStatus(99L);
        } else {
//                messageProducer.auditMessageDo(new AuditEntity() {{
//                    setMessageType(AuditMessageType.CREATE_TASK);
//                    setActivitiProcess(activitiProcess);
//                    setTaskId(taskId);
//                    setLoginUserId(LoginContextHolder.getContext().getUserId());
//                    setTimes(0);
//                    setMaxTimes(0);
//                }});
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());
            this.updateById(entity);
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


        if (ToolUtil.isNotEmpty(param.getUserIds())) {
            /**
             * 评论
             */
            RemarksParam remarksParam = new RemarksParam();
            remarksParam.setTaskId(taskId);
            remarksParam.setType("remark");
            StringBuffer userIdStr = new StringBuffer();
            for (Long userId : param.getUserIds()) {
                userIdStr.append(userId).append(",");
            }
            String userStrtoString = userIdStr.toString();
            if (userIdStr.length() > 1) {
                userStrtoString = userStrtoString.substring(0, userStrtoString.length() - 1);
            }
            remarksParam.setUserIds(userStrtoString);
            remarksParam.setContent(param.getRemark());
            remarksService.addByMQ(remarksParam);
        }

        if (ToolUtil.isNotEmpty(param.getType()) && param.getType().equals("purchaseOrder") && ToolUtil.isNotEmpty(param.getOrderId())) {
            /**
             * 循环更新到货数量字段。
             */
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setArrivalNumber((int) (orderDetail.getArrivalNumber() + arrivalNumber.get(orderDetail.getDetailId())));
                orderDetailService.updateById(orderDetail);
            }

        }
        return entity;
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
    public void checkNumberTrue(Long id, Integer status, String actionType) {
        if (status != 98) {
            throw new ServiceException(500, "您传入的状态不正确");
        } else {
            messageProducer.auditMessageDo(new AuditEntity() {{
                setAuditType(CHECK_ACTION);
                setMessageType(AuditMessageType.AUDIT);
                setFormId(id);
                setForm("instock");
                setActionId(actionType);
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
        List<InstockOrderResult> instockOrderResults = this.baseMapper.customList(param);
        this.format(instockOrderResults);
        return instockOrderResults;
    }

    @Override
    public PageInfo findPageBySpec(InstockOrderParam param, DataScope dataScope) {
        Page<InstockOrderResult> pageContext = getPageContext();
        IPage<InstockOrderResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
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

        List<InstockHandleResult> instockHandleResults = instockHandleService.detailByInStockOrder(result.getInstockOrderId());
        result.setHandleResults(instockHandleResults);
    }


    @Override
    @Transactional
    public List<Long> inStock(InstockOrderParam param) {

        boolean operat = activitiProcessLogService.canOperat(ProcessType.INSTOCK.name(), ProcessModuleEnum.createInstock.name(), InStockActionEnum.performInstock.name());
        if (!operat) {
            throw new ServiceException(500, "你没有入库权限");
        }
        inventoryService.staticState();  //静态盘点判断

        //查询任务id
        ActivitiProcessTask task = activitiProcessTaskService.query().eq("form_id", param.getInstockOrderId()).one();
        List<InstockLogDetail> instockLogDetails = new ArrayList<>();
        List<InstockHandle> instockHandles = new ArrayList<>();
        long skuId = 0;

        for (InstockListParam listParam : param.getListParams()) {
            skuId = listParam.getSkuId();
            InstockHandle instockHandle = new InstockHandle();    //添加入庫处理结果
            ToolUtil.copyProperties(listParam, instockHandle);

            listParam.setInstockOrderId(param.getInstockOrderId());
            Integer number = stockDetailsService.getNumberByStock(listParam.getSkuId(), null, null);//入库前的库存数
            if (ToolUtil.isNotEmpty(listParam.getInkindIds())) {   //直接入库
                handle(listParam, listParam.getInkindIds());
            } else {   //创建实物入库
                if (listParam.getBatch()) {   //批量
                    Long inKind = createInKind(listParam);
                    listParam.setInkind(inKind);
                    handle(listParam, inKind);  //入库处理
                    InstockLogDetail instockLogDetail = addLog(param, listParam, inKind, number);  //添加记录
                    instockLogDetails.add(instockLogDetail);
                    //添加物料的操作记录
                    skuHandleRecordService.addRecord(listParam.getSkuId(), listParam.getBrandId(), listParam.getStorehousePositionsId(), listParam.getCustomerId(), "INSTOCK", task, listParam.getNumber(), Long.valueOf(number), listParam.getNumber() + number);
                } else {
                    batchInStock(param, listParam, instockLogDetails);
                    //添加物料的操作记录
                    skuHandleRecordService.addRecord(listParam.getSkuId(), listParam.getBrandId(), listParam.getStorehousePositionsId(), listParam.getCustomerId(), "INSTOCK", task, listParam.getNumber(), Long.valueOf(number), listParam.getNumber() + number);
                }
            }
            updateStatus(listParam);
        }

        /**
         * 生成入库单
         */
        instockReceiptService.addReceipt(param, instockLogDetails);
        /**
         *
         * 添加入库处理结果
         */
        instockHandleService.saveBatch(instockHandles);
        /**
         * 添加入库记录
         */
        instockLogDetailService.saveBatch(instockLogDetails);
        /**
         * 添加动态
         */
        String skuMessage = skuService.skuMessage(skuId);
        shopCartService.addDynamic(param.getInstockOrderId(), null, "将" + skuMessage + "入库");
        /**
         * 更新单据状态
         */
        boolean b = instockOrderComplete(param.getInstockOrderId());
        if (b) {
            /**
             * 消息队列完成动作
             */

            activitiProcessLogService.checkAction(param.getInstockOrderId(), "INSTOCK", param.getActionType(), LoginContextHolder.getContext().getUserId());
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
            shopCartService.addDynamic(param.getInstockOrderId(), null, "完成任务");

        }
        InstockOrder instockOrder = this.getById(param.getInstockOrderId());
        if (ToolUtil.isNotEmpty(instockOrder.getOrderId())) {
            orderService.checkStatus(instockOrder.getOrderId());
        }
        return null;
    }
    @Override
    public void instock(List<InventoryCorrectionParam> params){
        List<Long> skuIds = params.stream().map(InventoryCorrectionParam::getSkuId).distinct().collect(Collectors.toList());
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.listByIds(skuIds);

        for (InventoryCorrectionParam param : params) {
            Long skuId = 0L;
            int bantch = 0;
            for (Sku sku : skuList) {
                if(param.getSkuId().equals(sku.getSkuId())){
                    skuId = sku.getSkuId();
                    bantch = sku.getBatch();
                }
            }
            if (skuId.equals(0L)){
                throw new ServiceException(500,"未找到物料信息 请验证物料id");
            }
            InstockListParam listParam = new InstockListParam();
            listParam.setSkuId(skuId);
            if (ToolUtil.isEmpty(param.getCustomerId())){
                Long customerId = supplyService.getanyOneCustomerIdBySkuIdAndBrandId(param.getSkuId(), param.getBrandId());
                listParam.setCustomerId( ToolUtil.isEmpty(customerId)?null:customerId);
            }else {
                listParam.setCustomerId( param.getCustomerId());
            }
            listParam.setBrandId(param.getBrandId());
            listParam.setStorehousePositionsId(param.getPositionId());
            listParam.setNumber(Long.valueOf(param.getNumber()));
            if (bantch == 1) {   //批量
                Long inKind = createInKind(listParam);
                listParam.setInkind(inKind);
                handle(listParam, inKind);  //入库处理
                //添加物料的操作记录
                skuHandleRecordService.addRecord(listParam.getSkuId(), listParam.getBrandId(), listParam.getStorehousePositionsId(), listParam.getCustomerId(), "InventoryCorrection", null, listParam.getNumber(), Long.valueOf(param.getNumber()), listParam.getNumber() + param.getNumber());
            } else {
                instockBatchSku(listParam);
                //添加物料的操作记录
                skuHandleRecordService.addRecord(listParam.getSkuId(), listParam.getBrandId(), listParam.getStorehousePositionsId(), listParam.getCustomerId(), "InventoryCorrection", null, listParam.getNumber(), Long.valueOf(param.getNumber()), listParam.getNumber() + param.getNumber());
            }
        }
    }


    /**
     * 单个物料批量添加
     *
     * @param param
     * @param listParam
     * @param instockLogDetails
     */
    private void batchInStock(InstockOrderParam param, InstockListParam listParam, List<InstockLogDetail> instockLogDetails) {
        Integer numberByStock = stockDetailsService.getNumberByStock(listParam.getSkuId(), listParam.getBrandId(), null);  //入库之前库存数

        this.instockBatchSku(listParam);







        InstockLogDetail instockLogDetail = addLog(param, listParam, null, numberByStock);
        instockLogDetails.add(instockLogDetail);
    }

    private void instockBatchSku(InstockListParam listParam){
        Integer numberByStock = stockDetailsService.getNumberByStock(listParam.getSkuId(), listParam.getBrandId(), null);  //入库之前库存数

        Long number = listParam.getNumber();
        StockLog stockLog = new StockLog();
        stockLog.setType(StockLogTypeEnum.increase.getValue());
        stockLog.setSkuId(listParam.getSkuId());
        stockLog.setBeforeNumber(numberByStock);
        stockLog.setNumber(Math.toIntExact(number));
        stockLog.setAfterNumber((int) (numberByStock+number));
        stockLog.setStorehouseId(listParam.getStoreHouseId());
        stockLog.setStorehousePositionsId(listParam.getStorehousePositionsId());
        stockLogService.save(stockLog);

        List<OrCode> orCodes = new ArrayList<>();
        List<Inkind> inkinds = new ArrayList<>();   //先创建实物
        List<StockLogDetail> stockLogDetails = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            Inkind inkind = new Inkind();
            inkind.setNumber(1L);
            inkind.setSkuId(listParam.getSkuId());
            inkind.setCustomerId(listParam.getCustomerId());
            inkind.setSource("入库");
            inkind.setSourceId(listParam.getInstockListId());
            inkind.setType("1");
            inkind.setBrandId(listParam.getBrandId());
            inkind.setPositionId(listParam.getStorehousePositionsId());   //勿动
            inkinds.add(inkind);

            OrCode orCode = new OrCode();    //创建二维码
            orCode.setState(1);
            orCode.setType("item");
            orCodes.add(orCode);
        }

        if (ToolUtil.isEmpty(listParam.getStorehousePositionsId())) {
            throw new ServiceException(500, "库位不能为空");
        }
        inkindService.saveBatch(inkinds);
        for (Inkind inkind : inkinds) {
            StockLogDetail stockLogDetail = new StockLogDetail();
            stockLogDetail.setNumber(1);
            stockLogDetail.setBeforeNumber(0);
            stockLogDetail.setAfterNumber(1);
            stockLogDetail.setInkindId(inkind.getInkindId());
            stockLogDetail.setSource(StockLogDetailSourceEnum.instock.getValue());
            stockLogDetail.setStockLogId(stockLog.getStockLogId());
            stockLogDetails.add(stockLogDetail);
        }
        orCodeService.saveBatch(orCodes);






        StorehousePositions storehousePositions = positionsService.getById(listParam.getStorehousePositionsId());

        List<OrCodeBind> binds = new ArrayList<>();
        List<StockDetails> stockDetailList = new ArrayList<>();
        for (int i = 0; i < number; i++) {

            OrCode orCode = orCodes.get(i);
            Inkind inkind = inkinds.get(i);

            OrCodeBind bind = new OrCodeBind();   //添加绑定
            bind.setOrCodeId(orCode.getOrCodeId());
            bind.setFormId(inkind.getInkindId());
            bind.setSource("item");
            binds.add(bind);

            StockDetails stockDetails = new StockDetails();
            stockDetails.setSkuId(listParam.getSkuId());
            stockDetails.setBrandId(listParam.getBrandId());
            stockDetails.setCustomerId(listParam.getCustomerId());
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setStorehousePositionsId(listParam.getStorehousePositionsId());
            stockDetails.setStorehouseId(storehousePositions.getStorehouseId());
            stockDetails.setNumber(inkind.getNumber());
            stockDetailList.add(stockDetails);

        }
        stockLogDetailService.saveBatch(stockLogDetails);

        stockDetailsService.saveBatch(stockDetailList);
        orCodeBindService.saveBatch(binds);
    }

    /**
     * 添加入库记录
     */
    private InstockLogDetail addLog(InstockOrderParam param, InstockListParam listParam, Long inkindId, Integer stockNum) {
        InstockLogDetail instockLogDetail = new InstockLogDetail();
        instockLogDetail.setInstockOrderId(param.getInstockOrderId());
        instockLogDetail.setSkuId(listParam.getSkuId());
        instockLogDetail.setType("normal");
        instockLogDetail.setSource("instock");
        instockLogDetail.setReceiptId(param.getInstockOrderId());
        instockLogDetail.setSourceId(param.getInstockOrderId());
        instockLogDetail.setInkindId(inkindId);
        instockLogDetail.setRealNumber(stockNum + listParam.getNumber());
        instockLogDetail.setCurrentNumber(Long.valueOf(stockNum));  //原库存
        instockLogDetail.setBrandId(listParam.getBrandId());
        instockLogDetail.setCustomerId(listParam.getCustomerId());
        instockLogDetail.setStorehousePositionsId(listParam.getStorehousePositionsId());
        instockLogDetail.setNumber(listParam.getNumber());
        return instockLogDetail;
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
        instockList.setInstockNumber(instockList.getInstockNumber() + listParam.getNumber());
        if (ToolUtil.isNotEmpty(instockList.getDetailId())) {
            OrderDetail orderDetail = orderDetailService.getById(instockList.getDetailId());
            orderDetail.setInStockNumber(orderDetail.getInStockNumber()+Math.toIntExact(instockList.getInstockNumber()));
            orderDetailService.updateById(orderDetail);
        }
        if (instockList.getRealNumber() < 0) {
            throw new ServiceException(500, "当前入库数量与单据数量不符");
        }

        instockListService.updateById(instockList);

        if (ToolUtil.isNotEmpty(listParam.getCartId())) {
            ShopCart shopCart = cartService.getById(listParam.getCartId());

            if (shopCart.getNumber() != 0) {
                long number = shopCart.getNumber() - listParam.getNumber();
                if (number < 0) {
                    throw new ServiceException(500, "购物车数量不足");
                } else if (number == 0) {
                    shopCart.setStatus(99);
                }
                shopCart.setNumber(number);   //购物车数量 不足出库
            } else {
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

        if (ToolUtil.isEmpty(param.getStorehousePositionsId())) {
            throw new ServiceException(500, "库位不能为空");
        }

        Inkind inkind = new Inkind();
        inkind.setNumber(param.getNumber());
        inkind.setSkuId(param.getSkuId());
        inkind.setCustomerId(param.getCustomerId());
        inkind.setSource("入库");
        inkind.setLastMaintenanceTime(new DateTime());
        inkind.setSourceId(param.getInstockListId());
        inkind.setPositionId(param.getStorehousePositionsId());
        inkind.setType("1");
        inkind.setLastMaintenanceTime(new DateTime());
        inkind.setPositionId(param.getStorehousePositionsId());   //别动
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

    /**
     * 批量入库操作
     */
    private void batchHandle(List<InstockListParam> params) {
        List<StockDetails> stockDetailsList = new ArrayList<>();
        for (InstockListParam param : params) {
            StockDetails stockDetails = new StockDetails();
            stockDetails.setSkuId(param.getSkuId());
            stockDetails.setBrandId(param.getBrandId());
            stockDetails.setCustomerId(param.getCustomerId());
            stockDetails.setInkindId(param.getInkindId());
            stockDetails.setStorehousePositionsId(param.getStorehousePositionsId());
            stockDetails.setNumber(param.getNumber());

            if (ToolUtil.isEmpty(param.getStorehousePositionsId())) {
                throw new ServiceException(500, "存在物料缺少库位信息");
            }
            StorehousePositions storehousePositions = positionsService.getById(param.getStorehousePositionsId());
            stockDetails.setStorehouseId(storehousePositions.getStorehouseId());

            stockDetailsList.add(stockDetails);
        }
        stockDetailsService.saveBatch(stockDetailsList);
    }

    private void handle(InstockListParam param, List<Long> inkindIds) {

        List<StockDetails> stockDetailList = new ArrayList<>();
        for (Long inkindId : inkindIds) {
            Integer count = stockDetailsService.query().eq("inkind_id", inkindId).eq("display", 1).count();
            if (count > 0) {
                throw new ServiceException(500, "当前实物已在库存");
            }
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


    @Override
    public List<InstockOrderResult> getDetails(List<Long> orderIds) {
        if (ToolUtil.isEmpty(orderIds)) {
            return new ArrayList<>();
        }
        List<InstockOrder> instockOrders = this.listByIds(orderIds);
        if (ToolUtil.isEmpty(instockOrders)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(instockOrders, InstockOrderResult.class);
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
                setActionId(param.getActionType());
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

        List<InstockList> instockLists = instockListService.query().ne("status", 50)
                .eq("display", 1)
                .eq("instock_order_id", orderId).list();
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
     * 关联单据
     *
     * @return
     */
    @Override
    public Object document(Long id, String type) {
        switch (type) {
            case "INSTOCK":
            case "Stocktaking":
                List<Anomaly> anomalies = anomalyService.query().eq("form_id", id).eq("display", 1).list();
                List<Long> anomalyOrderIds = new ArrayList<>();
                for (Anomaly anomaly : anomalies) {
                    anomalyOrderIds.add(anomaly.getOrderId());
                }
                List<ActivitiProcessTask> processTasks = anomalyOrderIds.size() == 0 ? new ArrayList<>() : activitiProcessTaskService.
                        query().in("form_id", anomalyOrderIds).list();
                List<ActivitiProcessTaskResult> results = BeanUtil.copyToList(processTasks, ActivitiProcessTaskResult.class);
                activitiProcessTaskService.format(results);
//                for (ActivitiProcessTaskResult result : results) {
//                    User user = result.getUser();
//                    if (ToolUtil.isNotEmpty(user)) {
//                        String imgUrl = stepsService.imgUrl(user.getUserId().toString());
//                        user.setAvatar(imgUrl);
//                    }
//                    result.setUser(user);
//                }
                return results;

            case "ERROR":
                List<Anomaly> anomalyList = anomalyService.query().eq("order_id", id).eq("display", 1).list();
                List<Long> orderIds = new ArrayList<>();
                for (Anomaly anomaly : anomalyList) {
                    orderIds.add(anomaly.getFormId());
                }

                ProductionPickLists pickLists = pickListsService.query().eq("source_id", id).eq("display", 1).one();
                if (ToolUtil.isNotEmpty(pickLists)) {
                    orderIds.add(pickLists.getPickListsId());
                }

                List<ActivitiProcessTask> processTaskList = orderIds.size() == 0 ? new ArrayList<>() : activitiProcessTaskService.query().in("form_id", orderIds).list();
                List<ActivitiProcessTaskResult> taskResults = BeanUtil.copyToList(processTaskList, ActivitiProcessTaskResult.class);
                activitiProcessTaskService.format(taskResults);
//                for (ActivitiProcessTaskResult result : taskResults) {
//                    User user = result.getUser();
//                    if (ToolUtil.isNotEmpty(user)) {
//                        String imgUrl = stepsService.imgUrl(user.getUserId().toString());
//                        user.setAvatar(imgUrl);
//                    }
//                    result.setUser(user);
//                }
                return taskResults;
        }

        return null;
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
                setActionId(ReceiptsEnum.INSTOCK.name());
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
        if (ToolUtil.isNotEmpty(order)) {
            order.setState(50);
            this.updateById(order);
        }
    }

    @Override
    public void updateCreateInstockRefuseStatus(ActivitiProcessTask processTask) {
        InstockOrder order = this.getById(processTask.getFormId());
        order.setState(Math.toIntExact(InStockActionEnum.refuse.getStatus()));
        order.setStatus(InStockActionEnum.refuse.getStatus());
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

            long applyNum = 0L;
            long inStockNum = 0;
            int positionNum = 0;
            int skuNum = 0;
            for (InstockListResult instockListResult : instockListResults) {
                if (instockListResult.getInstockOrderId().equals(datum.getInstockOrderId())) {
                    skuNum = skuNum + 1;
                    positionNum = positionNum + instockListResult.getPositionNum();
                    applyNum = applyNum + instockListResult.getNumber();
                    inStockNum = inStockNum + instockListResult.getInstockNumber();
                    results.add(instockListResult);
                }
            }

            if (datum.getStatus() == 99) {   //单据完成 进度条直接完成
                datum.setInStockNum(applyNum);
            } else {
                datum.setInStockNum(inStockNum);
            }
            datum.setApplyNum(applyNum);
            datum.setInstockListResults(results);
            datum.setPositionNum(positionNum);
            datum.setSkuNum(skuNum);
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
        List<Long> formIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();

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
            customerIds.add(datum.getCustomerId());
        }


        List<Announcements> announcements = noticeIds.size() == 0 ? new ArrayList<>() : announcementsService.listByIds(noticeIds);
        List<DocumentsStatusResult> documentsStatusResults = documentStatusService.resultsByIds(statusIds);
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();
        List<InstockListResult> instockListList = instockListService.getListByOrderIds(orderIds);

        for (InstockListResult instockListResult : instockListList) {
            formIds.add(instockListResult.getInstockListId());
        }


        List<Anomaly> anomalyList = instockListList.size() == 0 ? new ArrayList<>() : anomalyService.query().in("source_id", formIds).eq("display", 1).list();
        for (Anomaly anomaly : anomalyList) {
            formIds.add(anomaly.getAnomalyId());
        }

        List<ShopCart> shopCarts = formIds.size() == 0 ? new ArrayList<>() : shopCartService.query().in("form_id", formIds).eq("display", 1).eq("status", 0).list();
        List<CustomerResult> results = customerService.getResults(customerIds);

        for (InstockOrderResult datum : data) {

            long enoughNumber = 0L;
            long realNumber = 0L;
            int waitInStockNum = 0;
            int instockErrorNum = 0;
            List<InstockListResult> instockListResults = new ArrayList<>();

            for (CustomerResult result : results) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(result.getCustomerId())) {
                    datum.setCustomerResult(result);
                    break;
                }
            }

            for (InstockListResult instockList : instockListList) {
                for (Anomaly anomaly : anomalyList) {
                    if (ToolUtil.isNotEmpty(anomaly.getSourceId()) && anomaly.getSourceId().equals(instockList.getInstockListId())) {
                        instockList.setAnomalyId(anomaly.getAnomalyId());
                    }
                }
                if (datum.getInstockOrderId().equals(instockList.getInstockOrderId())) {
                    instockListResults.add(instockList);
                    enoughNumber = ToolUtil.isEmpty(instockList.getRealNumber()) ? 0 : enoughNumber + instockList.getNumber();
                    realNumber = ToolUtil.isEmpty(instockList.getRealNumber()) ? 0 : realNumber + instockList.getRealNumber();

                    for (ShopCart shopCart : shopCarts) {
                        if (shopCart.getFormId().equals(instockList.getInstockListId()) ||
                                (ToolUtil.isNotEmpty(instockList.getAnomalyId()) && shopCart.getFormId().equals(instockList.getAnomalyId()))) {
                            switch (shopCart.getType()) {
                                case "waitInStock":
                                case "instockByAnomaly":
                                    waitInStockNum = waitInStockNum + 1;
                                    break;
                                case "InstockError":
                                    instockErrorNum = instockErrorNum + 1;
                                    break;
                            }
                        }
                    }
                }
            }
            datum.setInstockErrorNum(instockErrorNum);
            datum.setWaitInStockNum(waitInStockNum);
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

            /**
             * 可入库状态
             */
            boolean canPut = false;
            for (InstockListResult instockListResult : instockListList) {
                if (instockListResult.getInstockOrderId().equals(datum.getInstockOrderId())) {
                    if (instockListResult.getStatus() == 0) {    //是否滑动到购物车
                        canPut = true;
                        break;
                    } else {
                        if (instockErrorNum > 0) {
                            canPut = true;
                            break;
                        } else if (waitInStockNum > 0) {
                            canPut = true;
                            break;
                        }
                    }
                }
            }
            datum.setCanPut(canPut);


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

    /**
     * 审批流完成检查上级单据如果是调拨 进入此方法
     * 完成上级调拨任务
     *
     * @param processTask
     */
    @Override
    public void checkAllocationDone(ActivitiProcessTask processTask) {
        if (ToolUtil.isNotEmpty(processTask.getSource()) && ToolUtil.isNotEmpty(processTask.getSourceId()) && processTask.getSource().equals("ALLOCATION")) {
            Allocation allocation = allocationService.getById(processTask.getSourceId());
            List<InstockList> instockLists = instockListService.query().eq("instock_order_id", processTask.getFormId()).list();
            List<AllocationCart> allocationCarts = allocationCartService.query().eq("display", 1).eq("type", "carry").eq("allocation_id", allocation.getAllocationId()).list();
            List<AllocationDetail> allocationDetails = allocationDetailService.query().eq("display", 1).eq("allocation_id", allocation.getAllocationId()).list();
//           if (allocation.getType().equals("")&&allocation.getAllocationType().equals(1)){
//               for (InstockList instockList : instockLists) {
//                   int number = Math.toIntExact(instockList.getNumber());
//                   for (AllocationCart cart : allocationCarts) {
//                       if (number > 0) {
//                           if (cart.getStatus().equals(98) && ToolUtil.isEmpty(cart.getStorehousePositionsId()) && instockList.getBrandId().equals(cart.getBrandId()) && instockList.getSkuId().equals(cart.getSkuId()) && instockList.getStoreHouseId().equals(cart.getStorehouseId())) {
//                               int lastNumber = number;
//                               number = number - (cart.getNumber() - cart.getDoneNumber());
//                               if (number >= 0) {
//                                   cart.setDoneNumber(cart.getNumber());
//                                   cart.setStatus(99);
//                               } else {
//                                   cart.setDoneNumber(cart.getDoneNumber() + lastNumber);
//                               }
//                           }
//                       }
//
//                   }
//               }
//           }else
//               if(allocation.getType().equals("allocation")&&allocation.getAllocationType().equals(2)){
            for (InstockList instockList : instockLists) {
                int number = Math.toIntExact(instockList.getNumber());
                for (AllocationCart cart : allocationCarts) {
                    if (number > 0) {
                        if (cart.getStatus().equals(98) && cart.getInstockOrderId().equals(instockList.getInstockOrderId()) && !cart.getStorehouseId().equals(allocation.getStorehouseId()) && instockList.getSkuId().equals(cart.getSkuId())) {
                            int lastNumber = number;
                            number = number - (cart.getNumber() - cart.getDoneNumber());
                            if (number >= 0) {
                                cart.setDoneNumber(cart.getNumber());
                                cart.setStatus(99);
                            } else {
                                cart.setDoneNumber(cart.getDoneNumber() + lastNumber);
                            }
                        }
                    }

                }
            }
//           }
            int detailCount = 0;
            int cartCount = 0;

            for (AllocationCart allocationCart : allocationCarts) {
                cartCount += allocationCart.getNumber();
            }
            for (AllocationDetail allocationDetail : allocationDetails) {
                detailCount += allocationDetail.getNumber();
            }
            if (allocationCarts.stream().allMatch(i -> i.getStatus().equals(99)) && detailCount == cartCount && detailCount > 0) {
                for (AllocationDetail allocationDetail : allocationDetails) {
                    allocationDetail.setStatus(99);
                }
                this.allocationService.checkCarry(allocation.getAllocationId());
            }
            allocationCartService.updateBatchById(allocationCarts);
        }
    }


    /**
     * 入库统计
     *
     * @param param
     * @return
     */
    @Override
    public Page<InstockView> instockView(DataStatisticsViewParam param) {
        Page<InstockView> longPageInfo = customerService.customIdListFromInStockOrder(param);
        List<CustomerResult> customerResults = longPageInfo.getRecords().size() == 0 ? new ArrayList<>() : customerService.getResults(longPageInfo.getRecords().stream().map(InstockView::getCustomerId).collect(Collectors.toList()));


        LambdaQueryChainWrapper<InstockOrder> instockOrderLambdaQueryChainWrapper = this.lambdaQuery().in(InstockOrder::getCustomerId, longPageInfo.getRecords().stream().map(InstockView::getCustomerId).collect(Collectors.toList()));
        if (param.getBeginTime() != null && param.getEndTime() != null) {
            instockOrderLambdaQueryChainWrapper.between(InstockOrder::getCreateTime, DateUtil.format(param.getBeginTime(), "yyyy-MM-dd"), DateUtil.format(param.getEndTime(), "yyyy-MM-dd"));
        }
        List<InstockOrder> list = longPageInfo.getRecords().size() == 0 ? new ArrayList<>() : instockOrderLambdaQueryChainWrapper.list();
        List<InstockListResult> instockLists = list.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(instockListService.lambdaQuery().in(InstockList::getInstockOrderId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), InstockListResult.class);
        instockListService.format(instockLists);
        List<InstockLogDetailResult> logDetails = list.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(instockLogDetailService.lambdaQuery().in(InstockLogDetail::getInstockOrderId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), InstockLogDetailResult.class);
        instockLogDetailService.format(logDetails);
        List<AnomalyResult> instockErrors = list.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(anomalyService.lambdaQuery().eq(Anomaly::getType, "InstockError").in(Anomaly::getFormId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), AnomalyResult.class);
        anomalyService.format(instockErrors);


        for (InstockView record : longPageInfo.getRecords()) {
            for (CustomerResult customerResult : customerResults) {
                if (record.getCustomerId().equals(customerResult.getCustomerId())) {
                    record.setCustomerName(customerResult.getCustomerName());
                }
            }

            List<InstockLogDetailResult> logs = new ArrayList<>();
            List<InstockListResult> lists = new ArrayList<>();
            List<AnomalyResult> errorList = new ArrayList<>();
            for (InstockOrder instockOrder : list) {
                if (record.getCustomerId().equals(instockOrder.getCustomerId())) {
                    for (InstockLogDetailResult detail : logDetails) {
                        if (detail.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            logs.add(detail);
                        }
                    }

                    for (InstockListResult instockList : instockLists) {
                        if (instockList.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            lists.add(instockList);
                        }
                    }
                    for (AnomalyResult instockError : instockErrors) {
                        if (instockError.getFormId().equals(instockOrder.getInstockOrderId())) {
                            errorList.add(instockError);
                        }
                    }
                }

            }
            record.setInstockLists(lists);
            record.setInstockLogDetails(logs);
            record.setErrorList(errorList);
            List<InstockListResult> instockListResults = BeanUtil.copyToList(lists, InstockListResult.class);
            instockListResults.removeIf(i -> i.getInstockNumber() == null || i.getInstockNumber() == 0);
            Integer logSkuCount = instockListResults.stream().map(InstockListResult::getSkuId).distinct().collect(Collectors.toList()).size();
            Integer detailSkuCount = lists.stream().map(InstockListResult::getSkuId).distinct().collect(Collectors.toList()).size();
            int logNumberCount = 0;
//            for (InstockLogDetailResult detailResult : logs) {
//                logNumberCount += detailResult.getNumber();
//            }
            int detailNumberCount = 0;
            for (InstockListResult listResult : lists) {
                detailNumberCount += listResult.getNumber();
                if (listResult.getInstockNumber() != null) {
                    logNumberCount += listResult.getInstockNumber();
                }
            }
            errorList.removeIf(i -> i.getErrorNumber() == 0 || i.getErrorNumber() == null);
            int errorSkuCount = errorList.stream().map(AnomalyResult::getSkuId).collect(Collectors.toList()).size();
            int errorNumberCount = 0;
            for (AnomalyResult instockError : errorList) {
                errorNumberCount += instockError.getErrorNumber();
            }


            record.setErrorNumberCount(errorNumberCount);
            record.setErrorSkuCount(errorSkuCount);
            record.setDetailSkuCount(detailSkuCount);
            record.setDetailNumberCount(detailNumberCount);
            record.setLogSkuCount(logSkuCount);
            record.setLogNumberCount(logNumberCount);
        }
        return longPageInfo;
    }

    @Override
    public List<InstockView> instockViewExcel(DataStatisticsViewParam param) {
        LambdaQueryChainWrapper<InstockOrder> instockOrderLambdaQueryChainWrapper = this.lambdaQuery().eq(InstockOrder::getDisplay, 1).groupBy(InstockOrder::getCustomerId);
        if (param.getBeginTime() != null && param.getEndTime() != null) {
            instockOrderLambdaQueryChainWrapper.between(InstockOrder::getCreateTime, DateUtil.format(param.getBeginTime(), "yyyy-MM-dd"), DateUtil.format(param.getEndTime(), "yyyy-MM-dd"));
        }
        List<InstockOrder> list = instockOrderLambdaQueryChainWrapper.list();
        List<Long> customerIds = new ArrayList<>();
        for (InstockOrder instockOrder : list) {
            if (ToolUtil.isNotEmpty(instockOrder.getCustomerId())) {
                customerIds.add(instockOrder.getCustomerId());
            }
        }
        //用customerIds返向查询出库单
        List<InstockOrder> instockOrders = customerIds.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(InstockOrder::getCustomerId, customerIds).eq(InstockOrder::getDisplay, 1).list();
        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<InstockView> results = new ArrayList<>();
        for (Long customerId : customerIds) {
            InstockView result = new InstockView();
            result.setCustomerId(customerId);
            results.add(result);
        }

        /**
         * 处理详细数据
         */
//        List<InstockList> instockLists =instockOrders.size() == 0 ? new ArrayList<>() : instockListService.lambdaQuery().in(InstockList::getInstockOrderId, instockOrders.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list();
        List<InstockListResult> instockLists = instockOrders.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(instockListService.lambdaQuery().in(InstockList::getInstockOrderId, instockOrders.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), InstockListResult.class);
        instockListService.format(instockLists);
        List<InstockLogDetailResult> logDetails = list.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(instockLogDetailService.lambdaQuery().in(InstockLogDetail::getInstockOrderId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), InstockLogDetailResult.class);
        instockLogDetailService.format(logDetails);
        List<AnomalyResult> instockErrors = BeanUtil.copyToList(anomalyService.lambdaQuery().eq(Anomaly::getType, "InstockError").in(Anomaly::getFormId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), AnomalyResult.class);
        anomalyService.format(instockErrors);
        List<Long> skuIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (InstockView result : results) {
            for (Customer customer : customers) {
                if (result.getCustomerId().equals(customer.getCustomerId())) {
                    result.setCustomerName(customer.getCustomerName());
                }
            }
            List<InstockOrder> instockOrderList = new ArrayList<>();
            List<InstockLogDetailResult> detailResults = new ArrayList<>();
            List<InstockListResult> lists = new ArrayList<>();
            List<AnomalyResult> errorList = new ArrayList<>();
            for (InstockOrder instockOrder : instockOrders) {
                if (result.getCustomerId().equals(instockOrder.getCustomerId())) {
                    instockOrderList.add(instockOrder);

                    if (result.getCustomerId().equals(instockOrder.getCustomerId())) {
                        for (InstockLogDetailResult detail : logDetails) {
                            if (detail.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                                detailResults.add(detail);
                                skuIds.add(detail.getSkuId());
                                userIds.add(detail.getCreateUser());
                            }
                        }

                        for (InstockListResult instockList : instockLists) {
                            if (instockList.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                                lists.add(instockList);
                                skuIds.add(instockList.getSkuId());
                                userIds.add(instockList.getCreateUser());
                            }
                        }
                        for (AnomalyResult instockError : instockErrors) {
                            if (instockError.getFormId().equals(instockOrder.getInstockOrderId())) {
                                errorList.add(instockError);
                                skuIds.add(instockError.getSkuId());
                                userIds.add(instockError.getCreateUser());
                            }
                        }
                    }
                }
            }
            result.setInstockOrders(instockOrderList);
            result.setInstockLists(lists);
            result.setInstockLogDetails(detailResults);
            result.setErrorList(errorList);
            Integer logSkuCount = detailResults.stream().map(InstockLogDetailResult::getSkuId).distinct().collect(Collectors.toList()).size();
            Integer detailSkuCount = lists.stream().map(InstockListResult::getSkuId).distinct().collect(Collectors.toList()).size();
            int logNumberCount = 0;
            for (InstockLogDetailResult detailResult : detailResults) {
                logNumberCount += detailResult.getNumber();
            }
            int detailNumberCount = 0;
            for (InstockListResult listResult : lists) {
                detailNumberCount += listResult.getNumber();
            }
            int errorSkuCount = instockErrors.stream().map(AnomalyResult::getSkuId).collect(Collectors.toList()).size();
            int errorNumberCount = 0;
            for (AnomalyResult instockError : instockErrors) {
                errorNumberCount += instockError.getErrorNumber();
            }
            result.setErrorNumberCount(errorNumberCount);
            result.setErrorSkuCount(errorSkuCount);
            result.setDetailSkuCount(detailSkuCount);
            result.setDetailNumberCount(detailNumberCount);
            result.setLogSkuCount(logSkuCount);
            result.setLogNumberCount(logNumberCount);
        }
        formatViewDetails(results);
        return results;
    }

    //format
    private void formatViewDetails(List<InstockView> data) {
        for (InstockView datum : data) {
            formatViewDetail(datum);
        }

    }

    private void formatViewDetail(InstockView data) {
        List<InstockViewDetail> results = new ArrayList<>();

        for (InstockOrder instockOrder : data.getInstockOrders()) {

            for (InstockListResult instockList : data.getInstockLists()) {

                if (instockOrder.getInstockOrderId().equals(instockList.getInstockOrderId())) {

                    InstockViewDetail result = new InstockViewDetail();
                    ToolUtil.copyProperties(result, instockList);
                    results.add(result);
                }
            }
        }
        data.setInstockViewDetails(results);
    }

    @Override
    public List<InstockViewDetail> viewDetail(DataStatisticsViewParam param) {
        LambdaQueryChainWrapper<InstockOrder> instockOrderLambdaQueryChainWrapper = this.lambdaQuery().eq(InstockOrder::getDisplay, 1);
        if (param.getBeginTime() != null && param.getEndTime() != null) {
            instockOrderLambdaQueryChainWrapper.between(InstockOrder::getCreateTime, DateUtil.format(param.getBeginTime(), "yyyy-MM-dd"), DateUtil.format(param.getEndTime(), "yyyy-MM-dd"));
        }
        if (ToolUtil.isNotEmpty(param.getCustomerId())){
            instockOrderLambdaQueryChainWrapper.eq(InstockOrder::getCustomerId, param.getCustomerId());
        }
        List<InstockOrder> instockOrders = instockOrderLambdaQueryChainWrapper.list();

        List<InstockListResult> instockLists = instockOrders.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(instockListService.lambdaQuery().in(InstockList::getInstockOrderId, instockOrders.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), InstockListResult.class);
        instockListService.format(instockLists);

        List<AnomalyResult> instockErrors = instockOrders.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(anomalyService.lambdaQuery().eq(Anomaly::getType, "InstockError").in(Anomaly::getFormId, instockOrders.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), AnomalyResult.class);
        anomalyService.format(instockErrors);


        List<InstockListResult> instockListsTotal = new ArrayList<>();
        instockLists.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new InstockListResult() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        if (BeanUtil.isNotEmpty(a.getSkuResult())) {
                            setSkuResult(a.getSkuResult());
                        }
                        if (BeanUtil.isNotEmpty(a.getBrandResult())) {
                            setBrandResult(a.getBrandResult());
                        }
                        setInstockNumber(a.getInstockNumber() + b.getInstockNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                    }}).ifPresent(instockListsTotal::add);
                }
        );

        List<AnomalyResult> instockErrorsTotal = new ArrayList<>();
        instockErrors.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new AnomalyResult() {{
                        setSkuId(a.getSkuId());
                        setErrorNumber(a.getErrorNumber() + b.getErrorNumber());
                        if (BeanUtil.isNotEmpty(a.getSkuResult())) {
                            setSkuResult(a.getSkuResult());
                        }
                        if (BeanUtil.isNotEmpty(a.getBrand())) {
                            setBrand(a.getBrand());
                        }
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                    }}).ifPresent(instockErrorsTotal::add);
                }
        );


        List<InstockViewDetail> instockViewDetails = new ArrayList<>();

        for (InstockListResult instockListResult : instockListsTotal) {
            InstockViewDetail detail = new InstockViewDetail();
            detail.setBrandId(instockListResult.getBrandId());
            detail.setListNum(Math.toIntExact(instockListResult.getNumber()));
            if (ToolUtil.isNotEmpty(instockListResult.getBrandId())) {
                detail.setBrandResult(instockListResult.getBrandResult());
            }
            detail.setLogNum(Math.toIntExact(instockListResult.getInstockNumber()));
            detail.setListCount(Math.toIntExact(instockListResult.getNumber()));
            if (ToolUtil.isNotEmpty(instockListResult.getSkuResult())) {
                detail.setSkuResult(BeanUtil.copyProperties(instockListResult.getSkuResult(), SkuSimpleResult.class));
            }

//            for (InstockLogDetailResult instockLogDetailResult : logDetailsTotal) {
//                if(instockLogDetailResult.getSkuId().equals(instockListResult.getSkuId()) && instockListResult.getBrandId().equals(instockLogDetailResult.getBrandId())){
//
//                }
//            }
            for (AnomalyResult anomalyResult : instockErrorsTotal) {
                if (anomalyResult.getSkuId().equals(instockListResult.getSkuId()) && anomalyResult.getBrandId().equals(instockListResult.getBrandId())) {
                    detail.setErrorNum(Math.toIntExact(anomalyResult.getErrorNumber()));
                }
            }
            instockViewDetails.add(detail);
        }
        return instockViewDetails;
    }

    @Override
    public InstockView viewTotail(DataStatisticsViewParam param) {
//        this.baseMapper.customPageList()
        return null;
    }

}
