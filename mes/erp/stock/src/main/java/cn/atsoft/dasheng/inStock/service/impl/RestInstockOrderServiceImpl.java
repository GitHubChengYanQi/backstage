package cn.atsoft.dasheng.inStock.service.impl;


import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.entity.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.atsoft.dasheng.form.service.DocumentsActionService;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.inStock.entity.RestInstockOrder;
import cn.atsoft.dasheng.inStock.entity.RestInstockOrderDetail;
import cn.atsoft.dasheng.inStock.mapper.RestInstockOrderMapper;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderDetailParam;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderParam;
import cn.atsoft.dasheng.inStock.model.result.OrderResult;
import cn.atsoft.dasheng.inStock.service.RestInstockOrderDetailService;
import cn.atsoft.dasheng.inStock.service.RestInstockOrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.params.QrCodeBindParam;
import cn.atsoft.dasheng.service.IErpBase;
import cn.atsoft.dasheng.service.QrCodeBindService;
import cn.atsoft.dasheng.service.QrCodeService;
import cn.atsoft.dasheng.service.RestTraceabilityService;
import cn.atsoft.dasheng.skuHandleRecord.service.RestSkuHandleRecordService;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import cn.atsoft.dasheng.stockDetail.service.RestStockDetailsService;
import cn.atsoft.dasheng.stockLog.entity.RestStockLog;
import cn.atsoft.dasheng.stockLog.entity.RestStockLogDetail;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogParam;
import cn.atsoft.dasheng.stockLog.service.RestStockLogService;
import cn.atsoft.dasheng.storehousePosition.entity.RestStorehousePositions;
import cn.atsoft.dasheng.storehousePosition.service.RestStorehousePositionsService;
import cn.atsoft.dasheng.storehousePositionBind.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.storehousePositionBind.service.RestStorehousePositionsBindService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 入库单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class RestInstockOrderServiceImpl extends ServiceImpl<RestInstockOrderMapper, RestInstockOrder> implements RestInstockOrderService {

    @Autowired
    private UserService userService;
    //    @Autowired
//    private StorehouseService storehouseService;
//    @Autowired
//    private InstockListService instockListService;
//    @Autowired
//    private InstockSendTemplate instockSendTemplate;
//    @Autowired
//    private OrCodeService orCodeService;
//    @Autowired
//    private CodingRulesService codingRulesService;
//    @Autowired
//    private InkindService inkindService;
//    @Autowired
//    private StockService stockService;
//    @Autowired
//    private StockDetailsService stockDetailsService;
//    @Autowired
//    private OrCodeBindService bindService;
//    @Autowired
//    private WxCpSendTemplate wxCpSendTemplate;
//    @Autowired
//    private GetOrigin getOrigin;
//    @Autowired
//    private StorehousePositionsService positionsService;
//    @Autowired
//    private StorehousePositionsBindService positionsBindService;
//    @Autowired
//    private MessageProducer messageProducer;
//
//    @Autowired
//    private StepsService stepsService;
//
//    @Autowired
//    private SkuService skuService;
//    @Autowired
//    private InstockLogService instockLogService;
//    @Autowired
//    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessFormLogService activitiProcessLogService;

    @Autowired
    private DocumentStatusService documentStatusService;

    @Autowired
    private DocumentsActionService documentsActionService;

    @Autowired
    private RestCodeRuleService codeRuleService;

    @Autowired
    private RestTraceabilityService inkindService;
    @Autowired
    private RestStockDetailsService stockDetailsService;
    @Autowired
    private RestSkuService skuService;
    @Autowired
    private RestInstockOrderDetailService detailService;
    @Autowired
    private QrCodeBindService qrCodeBindService;
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private RestStorehousePositionsBindService positionsBindService;
    @Autowired
    private RestStockLogService stockLogService;
    @Autowired
    private RestStorehousePositionsService positionsService;

    @Autowired
    private RestSkuHandleRecordService skuHandleRecordService;

//
//    @Autowired
//    private IErpBase erpBase;

    @Override
    public List<OrderResult> showOrderList() {
//        return this.baseMapper.customList();
        return null;
    }


    @Override

    public Long autoInStock(RestInstockOrderParam param) {
        if (ToolUtil.isEmpty( param.getDetailParams())) {
            throw new ServiceException(500,"请选择物料进行入库");
        }
        param.getDetailParams().removeIf(i->i.getNumber()<=0);

        /**
         * TODO 创建入库单 创建出库任务 并且完成任务
         */
        IErpBase orderDetailService = SpringContextHolder.getBean("RestOrderDetailService");
        IErpBase orderService = SpringContextHolder.getBean("RestOrderService");

        RestOrder order = orderService.getOrderById(param.getOrderId());
        if (ToolUtil.isEmpty(order)) {
            throw new ServiceException(500, "未找到订单");
        }
        List<RestInstockOrder> instockOrderList = this.lambdaQuery().eq(RestInstockOrder::getSource, "order").eq(RestInstockOrder::getSourceId, order.getOrderId()).list();
        if (instockOrderList.size()>0){
            throw new ServiceException(500,"此订单已存在入库单,不可直接入库");
        }


        List<Long> orderDetailIdList = param.getDetailParams().stream().map(RestInstockOrderDetailParam::getDetailId).distinct().collect(Collectors.toList());
        List<RestOrderDetail> orderDetails = orderDetailService.getDetailListByOrderDetailIds(orderDetailIdList);

        List<Long> skuIds = orderDetails.stream().map(RestOrderDetail::getSkuId).distinct().collect(Collectors.toList());

        List<StorehousePositionsBind> storehousePositionsBinds = positionsBindService.lambdaQuery().in(StorehousePositionsBind::getSkuId, skuIds).eq(StorehousePositionsBind::getDisplay, 1).list();
        if (storehousePositionsBinds.size() == 0) {
            throw new ServiceException(500, "未查询到绑定库位");
        }
        List<RestSku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.listByIds(skuIds);
        for (RestSku restSku : skuList) {
            if (storehousePositionsBinds.stream().noneMatch(i -> i.getSkuId().equals(restSku.getSkuId()))) {
                throw new ServiceException(500, restSku.getStandard() + "未查询到绑定库位");
            }
        }

        //创建INSTOCK_ORDER 数据
        RestInstockOrder entity = new RestInstockOrder();
        entity.setCoding(codeRuleService.backCoding("InStockOrder", null));
        entity.setCustomerId(order.getSellerId());
        if (ToolUtil.isNotEmpty(order.getRemark())) {
            entity.setRemark(order.getRemark());
        }
        entity.setOrderId(order.getOrderId());
        entity.setSource("order");
        entity.setSourceId(order.getOrderId());
        entity.setStatus(99L);
        this.save(entity);


        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "InStock").eq("status", 99).eq("module", "createInstock").one();
        if (ToolUtil.isEmpty(activitiProcess)) {
            throw new ServiceException(500, "请先创建入库流程");
        }
        /**
         *  创建任务
         */
        Long taskId = activitiProcessTaskService.addV2(new ActivitiProcessTaskParam() {{
            setProcessId(activitiProcess.getProcessId());
            setTaskName(LoginContextHolder.getContext().getUser().getName() + "发起的一键入库");
            setStatus(99);
            setSource("inStock");
            setType("INSTOCK");
            setSourceId(entity.getInstockOrderId());
            setFormId(entity.getInstockOrderId());
            if (ToolUtil.isNotEmpty(order.getTheme())){
                setTheme(order.getTheme());
            }
            setUserId(LoginContextHolder.getContext().getUserId());
        }});
        entity.setTaskId(taskId);
        this.updateById(entity);


        /**
         * 创建入库单子表信息
         */

        List<RestInstockOrderDetail> details = new ArrayList<>();
        for (RestInstockOrderDetailParam detailParam : param.getDetailParams()) {
            for (RestOrderDetail orderDetail : orderDetails) {
                if (detailParam.getDetailId().equals(orderDetail.getDetailId())){
                    details.add(new RestInstockOrderDetail() {{
                        setInstockOrderId(entity.getInstockOrderId());
                        setBrandId(orderDetail.getBrandId());
                        setCustomerId(order.getSellerId());
                        setSkuId(orderDetail.getSkuId());
                        setCustomerId(orderDetail.getCustomerId());
                        setInstockNumber(detailParam.getNumber());
                        setNumber(detailParam.getNumber());
                    }});
                    break;
                }
            }
        }


        detailService.saveBatch(details);



        /**
         * TODO 创建实物  入库
         *
         *
         *
         *
         *
         */

        List<RestTraceability> saveInkind = new ArrayList<>();
        for (RestInstockOrderDetail detail : details) {
            for (RestSku sku : skuList) {
                if (detail.getSkuId().equals(sku.getSkuId())) {

                    List<RestStockLogDetail> logDetails = new ArrayList<>();
                    for (StorehousePositionsBind storehousePositionsBind : storehousePositionsBinds) {
                        if (storehousePositionsBind.getSkuId().equals(sku.getSkuId())) {
                            skuHandleRecordService.addRecord(sku.getSkuId(),detail.getBrandId(),storehousePositionsBind.getPositionId(),detail.getCustomerId(),taskId,detail.getNumber(),"INSTOCK");

                            if (sku.getBatch().equals(1)) {
                                RestTraceability inkind = new RestTraceability();
                                inkind.setNumber(detail.getNumber());
                                inkind.setSkuId(detail.getSkuId());
                                inkind.setBrandId(detail.getBrandId());
                                inkind.setCustomerId(detail.getCustomerId());
                                inkind.setSource("入库");
                                inkind.setLastMaintenanceTime(new DateTime());
                                inkind.setSourceId(detail.getInstockListId());
                                inkind.setPositionId(storehousePositionsBind.getPositionId());
                                inkind.setType("1");
                                inkind.setLastMaintenanceTime(new DateTime());
                                inkind.setNumber(detail.getNumber());

                                inkindService.save(inkind);

                                QrCode orCode = new QrCode();
                                orCode.setState(1);
                                orCode.setType("item");
                                qrCodeService.save(orCode);

                                QrCodeBind bindParam = new QrCodeBind();
                                bindParam.setOrCodeId(orCode.getOrCodeId());
                                bindParam.setFormId(inkind.getInkindId());
                                bindParam.setSource("item");
                                qrCodeBindService.save(bindParam);
                                saveInkind.add(inkind);


                            } else {
                                for (Long i = 0L; i < detail.getNumber(); i++) {
                                    RestTraceability inkind = new RestTraceability();
                                    inkind.setNumber(1L);
                                    inkind.setSkuId(detail.getSkuId());
                                    inkind.setBrandId(detail.getBrandId());
                                    inkind.setCustomerId(detail.getCustomerId());
                                    inkind.setSource("入库");
                                    inkind.setLastMaintenanceTime(new DateTime());
                                    inkind.setSourceId(detail.getInstockListId());
                                    inkind.setPositionId(storehousePositionsBind.getPositionId());
                                    inkind.setType("1");
                                    inkind.setLastMaintenanceTime(new DateTime());
                                    inkind.setNumber(1L);
                                    inkindService.save(inkind);
                                    QrCode orCode = new QrCode();
                                    orCode.setState(1);
                                    orCode.setType("item");
                                    qrCodeService.save(orCode);
                                    QrCodeBind bindParam = new QrCodeBind();
                                    bindParam.setOrCodeId(orCode.getOrCodeId());
                                    bindParam.setFormId(inkind.getInkindId());
                                    bindParam.setSource("item");
                                    qrCodeBindService.save(bindParam);
                                    saveInkind.add(inkind);

                                }
                            }

                            break;
                        }
                    }
                }
            }
        }
//


        List<RestStockDetails> stockDetailsList = new ArrayList<>();
        List<Long> positionIds = storehousePositionsBinds.stream().map(StorehousePositionsBind::getPositionId).distinct().collect(Collectors.toList());
        List<RestStorehousePositions> restStorehousePositions = positionsService.listByIds(positionIds);
        /**
         * 入库  并添加入库记录
         */
        for (RestTraceability restTraceability : saveInkind) {

            RestStockDetails stockDetails = new RestStockDetails();
            stockDetails.setNumber(restTraceability.getNumber());
            stockDetails.setSkuId(restTraceability.getSkuId());
            stockDetails.setBrandId(restTraceability.getBrandId());
            stockDetails.setCustomerId(restTraceability.getCustomerId());
            for (RestStorehousePositions restStorehousePosition : restStorehousePositions) {
                if (restStorehousePosition.getStorehousePositionsId().equals(restTraceability.getPositionId())){
                    stockDetails.setStorehousePositionsId(restTraceability.getPositionId());
                    stockDetails.setStorehouseId(restStorehousePosition.getStorehouseId());
                }
            }
            stockDetails.setInkindId(restTraceability.getInkindId());
            stockDetailsList.add(stockDetails);
        }

        stockDetailsService.saveBatch(stockDetailsList);
        stockLogService.addBatch(stockDetailsList,"increase","inStock");


        for (RestOrderDetail orderDetail : orderDetails) {
            for (RestInstockOrderDetailParam detailParam : param.getDetailParams()) {
                if (detailParam.getDetailId().equals(orderDetail.getDetailId())){
                    orderDetail.setArrivalNumber((int) (orderDetail.getArrivalNumber()+detailParam.getNumber()));
                    orderDetail.setInStockNumber((int) (orderDetail.getInStockNumber()+detailParam.getNumber()));
                }
            }
        }
        orderDetailService.updateDetailList(orderDetails);
        if (ToolUtil.isNotEmpty(param.getOrderId())) {
            orderService.checkStatus(param.getOrderId());
        }
        return taskId;

    }



}
