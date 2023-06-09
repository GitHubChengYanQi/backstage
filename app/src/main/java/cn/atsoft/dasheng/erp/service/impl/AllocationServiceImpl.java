package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.AllocationActionEnum;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AllocationMapper;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.entity.DocumentsStatus;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.pojo.MarkDownTemplateTypeEnum;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.ProcessType.ALLOCATION;

/**
 * <p>
 * 调拨主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@Service
public class AllocationServiceImpl extends ServiceImpl<AllocationMapper, Allocation> implements AllocationService {

    @Autowired
    private AllocationDetailService allocationDetailService;
    @Autowired
    private AllocationCartService allocationCartService;
    @Autowired
    private ProductionPickListsService productionPickListsService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private AllocationLogService allocationLogService;
    @Autowired
    private AllocationLogDetailService allocationLogDetailService;
    @Autowired
    private StorehousePositionsBindService storehousePositionsBindService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogV1Service activitiProcessLogService;
    @Autowired
    private DocumentsActionService documentsActionService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private InkindService inkindService;

    @Autowired
    private RemarksService remarksService;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private StorehouseService storehouseService;

    @Autowired
    private UserService userService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private DocumentStatusService statusService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private InstockListService instockListService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private AnnouncementsService announcementsService;

    @Autowired
    private SerialNumberService serialNumberService;

    @Override
    @Transactional
    public Allocation add(AllocationParam param) {
        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "17").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                param.setCoding(codingRulesService.genSerial());
            }
        }


        Allocation entity = getEntity(param);
        this.save(entity);
        param.setAllocationId(entity.getAllocationId());
        allocationDetailService.add(param);
//        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
//            List<AllocationDetail> allocationDetails = BeanUtil.copyToList(param.getDetailParams(), AllocationDetail.class);
//            for (AllocationDetail allocationDetail : allocationDetails) {
//                allocationDetail.setAllocationId(entity.getAllocationId());
//            }
//            allocationDetailService.saveBatch(allocationDetails);
//        }
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "ALLOCATION").eq("status", 99).eq("module", "allocation").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            activitiProcessTaskService.checkStartUser(activitiProcess.getProcessId());
            auditService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            String name = LoginContextHolder.getContext().getUser().getName();
            activitiProcessTaskParam.setTaskName(name + "调拨申请 ");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setRemark(entity.getNote());
            activitiProcessTaskParam.setFormId(entity.getAllocationId());
            activitiProcessTaskParam.setType("ALLOCATION");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
            ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
            if (ToolUtil.isNotEmpty(param.getReason())) {
                List<Long> collect = Arrays.asList(param.getReason().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<AnnouncementsResult> announcementsResults = BeanUtil.copyToList(announcementsService.listByIds(collect), AnnouncementsResult.class);
                StringBuffer sb = new StringBuffer();
                for (AnnouncementsResult announcementsResult : announcementsResults) {
                    sb.append(announcementsResult.getContent()).append(",");
                }
                if(sb.length()>0){
                    activitiProcessTaskParam.setTheme(sb.substring(0,sb.length()-1).toString());
                }
            }
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            entity.setTaskId(taskId);
            this.updateById(entity);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());
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
        } else {
            throw new ServiceException(500, "请创建调拨流程！");
        }
        /**
         * 删除购物车
         */
        List<ShopCart> shopCarts = shopCartService.query().eq("create_user", LoginContextHolder.getContext().getUserId()).eq("type", "allocation").eq("display", 1).list();
        for (ShopCart shopCart : shopCarts) {
            shopCart.setDisplay(0);
        }
        shopCartService.updateBatchById(shopCarts);
        shopCartService.addDynamic(entity.getAllocationId(), null, "发起了调拨申请");
        return entity;
    }

    @Override
    public void checkCart(Long allocation) {
        DocumentsAction action = documentsActionService.query().eq("action", AllocationActionEnum.assign.name()).eq("display", 1).one();
        activitiProcessLogService.checkAction(allocation, "ALLOCATION", AllocationActionEnum.assign.name(), LoginContextHolder.getContext().getUserId());
    }

    @Override
    public void checkCarry(Long allocation) {
        Allocation entity = this.getById(allocation);
        entity.setStatus(99L);
        this.updateById(entity);
        DocumentsAction action = documentsActionService.query().eq("action", AllocationActionEnum.carryAllocation.name()).eq("display", 1).one();
        activitiProcessLogService.checkAction(allocation, "ALLOCATION", AllocationActionEnum.carryAllocation.name(), LoginContextHolder.getContext().getUserId());
    }

    /**
     * 创建出库单
     *
     * @param
     */
    @Override
    public void createPickListsAndInStockOrder(AllocationParam param, List<AllocationCart> allocationCarts) {
        Allocation allocation = this.getById(param.getAllocationId());
        ActivitiProcessTask processTask = activitiProcessTaskService.query().eq("form_id", allocation.getAllocationId()).eq("type", ALLOCATION.getType()).one();
        if (ToolUtil.isEmpty(processTask.getUserId())) {
            allocation.setUserId(param.getUserId());
            this.updateById(allocation);
            processTask.setUserId(param.getUserId());
            activitiProcessTaskService.updateById(processTask);

        }
        boolean haveTransfer = false;
        if (allocation.getType().equals("allocation")) {
            if (allocation.getAllocationType() == 1) {
                List<Long> stockIds = new ArrayList<>();
                for (AllocationCart allocationCart : allocationCarts) {
                    stockIds.add(allocationCart.getStorehouseId());
                }
                stockIds = stockIds.stream().distinct().collect(Collectors.toList());

                //入库单
//                InstockOrderParam instockOrderParam = new InstockOrderParam();
//                instockOrderParam.setSource("ALLOCATION");
//                instockOrderParam.setSourceId(allocationId);
//                instockOrderParam.setType("调拨入库");
//                instockOrderParam.setStoreHouseId(allocation.getStorehouseId());
//                List<InstockListParam> listParams = new ArrayList<>();
                for (Long stockId : stockIds) {
                    ProductionPickListsParam listsParam = new ProductionPickListsParam();
                    listsParam.setPickListsName(allocation.getAllocationName());
                    listsParam.setUserId(allocation.getUserId());
                    listsParam.setSource("processTask");
                    listsParam.setSourceId(processTask.getProcessTaskId());
                    List<ProductionPickListsDetailParam> details = new ArrayList<>();
                    List<AllocationCart> updateCart = new ArrayList<>();
                    for (AllocationCart allocationCart : allocationCarts) {
                        if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isEmpty(allocationCart.getStorehousePositionsId())) {
                            ProductionPickListsDetailParam listsDetailParam = new ProductionPickListsDetailParam();
                            ToolUtil.copyProperties(allocationCart, listsDetailParam);
                            listsDetailParam.setStatus(0);
                            details.add(listsDetailParam);

                            allocationCart.setStatus(98);
                            updateCart.add(allocationCart);
                        }
                        if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isNotEmpty(allocationCart.getStorehousePositionsId())) {
                            haveTransfer = true;
                        }
                    }
                    if (details.size() > 0) {
                        List<ProductionPickListsDetailParam> pickListsDetailParams = new ArrayList<>();
                        details.parallelStream().collect(Collectors.groupingBy(i -> i.getSkuId() + "_" + i.getBrandId(), Collectors.toList())).forEach(
                                (id, transfer) -> {
                                    transfer.stream().reduce((a, b) -> new ProductionPickListsDetailParam() {{
                                        setSkuId(a.getSkuId());
                                        setBrandId(a.getBrandId());
                                        setNumber(a.getNumber() + b.getNumber());
                                    }}).ifPresent(pickListsDetailParams::add);
                                }
                        );
                        listsParam.setPickListsDetailParams(pickListsDetailParams);
                        ProductionPickLists pickLists = productionPickListsService.add(listsParam);
                        for (AllocationCart allocationCart : updateCart) {
                            //setPickListsId 方便对应生成入库单时查找
                            allocationCart.setPickListsId(pickLists.getPickListsId());
                        }
                        allocationCartService.updateBatchById(updateCart);

                    }
                }
//
            } else if (allocation.getAllocationType() == 2) {
                List<Long> stockIds = new ArrayList<>();
                for (AllocationCart allocationCart : allocationCarts) {
                    stockIds.add(allocationCart.getStorehouseId());
                }
                stockIds = stockIds.stream().distinct().collect(Collectors.toList());
//
                //入库单

                for (Long stockId : stockIds) {
                    ProductionPickListsParam listsParam = new ProductionPickListsParam();
                    listsParam.setPickListsName(allocation.getAllocationName());
                    listsParam.setUserId(allocation.getUserId());
                    listsParam.setSource("processTask");
                    listsParam.setSourceId(processTask.getProcessTaskId());
                    List<ProductionPickListsDetailParam> details = new ArrayList<>();
                    List<AllocationCart> updateCart = new ArrayList<>();
                    for (AllocationCart allocationCart : allocationCarts) {
                        if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isEmpty(allocationCart.getStorehousePositionsId())) {
                            ProductionPickListsDetailParam listsDetailParam = new ProductionPickListsDetailParam();
                            listsDetailParam.setStorehouseId(allocation.getStorehouseId());
                            ToolUtil.copyProperties(allocationCart, listsDetailParam);
                            listsDetailParam.setStatus(0);
                            details.add(listsDetailParam);
                            InstockListParam instockListParam = new InstockListParam();
                            instockListParam.setStoreHouseId(stockId);
                            ToolUtil.copyProperties(allocationCart, instockListParam);
                            instockListParam.setStatus(0L);
                            instockListParam.setCartId(allocationCart.getAllocationCartId());
                            instockListParam.setStoreHouseId(allocation.getStorehouseId());
                            allocationCart.setStatus(98);
                            updateCart.add(allocationCart);

                        }
                        if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isNotEmpty(allocationCart.getStorehousePositionsId())) {
                            haveTransfer = true;
                        }
                    }
                    if (details.size() > 0) {
                        List<ProductionPickListsDetailParam> pickListsDetailParams = new ArrayList<>();
                        details.parallelStream().collect(Collectors.groupingBy(i -> i.getSkuId() + "_" + i.getBrandId(), Collectors.toList())).forEach(
                                (id, transfer) -> {
                                    transfer.stream().reduce((a, b) -> new ProductionPickListsDetailParam() {{
                                        setSkuId(a.getSkuId());
                                        setBrandId(a.getBrandId());
                                        setNumber(a.getNumber() + b.getNumber());
                                    }}).ifPresent(pickListsDetailParams::add);
                                }
                        );
                        listsParam.setPickListsDetailParams(pickListsDetailParams);
                        ProductionPickLists pickLists = productionPickListsService.add(listsParam);
                        for (AllocationCart cart : updateCart) {
                            cart.setPickListsId(pickLists.getPickListsId());
                        }
                        allocationCartService.updateBatchById(updateCart);
                    }


                }

            }

        }

        /**
         * 如果是移库操作  审批通过删除库位绑定
         */
        if (allocation.getType().equals("transfer")) {
            List<Long> skuIds = new ArrayList<>();
            List<Long> positionIds = new ArrayList<>();
            List<Long> detailIds = new ArrayList<>();
            for (AllocationCart allocationCart : allocationCarts) {

                if (ToolUtil.isNotEmpty(allocationCart.getAllocationDetailId())) {
                    detailIds.add(allocationCart.getAllocationDetailId());
                }
            }
            List<AllocationDetail> allocationDetails = detailIds.size() == 0 ? new ArrayList<>() : allocationDetailService.listByIds(detailIds);
            for (AllocationDetail allocationDetail : allocationDetails) {
                skuIds.add(allocationDetail.getSkuId());
                if (ToolUtil.isNotEmpty(allocationDetail.getStorehousePositionsId())) {
                    positionIds.add(allocationDetail.getStorehousePositionsId());
                }
            }
            List<StorehousePositionsBind> list = new ArrayList<>();
            if (skuIds.size() > 0 && positionIds.size() > 0) {
                list = storehousePositionsBindService.query().in("sku_id", skuIds).in("position_id", positionIds).list();
            }


            for (AllocationDetail allocationDetail : allocationDetails) {
                if (ToolUtil.isNotEmpty(list)) {
                    for (StorehousePositionsBind storehousePositionsBind : list) {
                        if (allocationDetail.getSkuId().equals(storehousePositionsBind.getSkuId()) && ToolUtil.isNotEmpty(allocationDetail.getStorehousePositionsId()) && allocationDetail.getStorehousePositionsId().equals(storehousePositionsBind.getPositionId())) {
                            storehousePositionsBind.setDisplay(0);
                        }
                    }

                }
            }
            storehousePositionsBindService.updateBatchById(list);


            List<Long> stockIds = new ArrayList<>();
            for (AllocationCart allocationCart : allocationCarts) {
                stockIds.add(allocationCart.getStorehouseId());
            }
            stockIds = stockIds.stream().distinct().collect(Collectors.toList());

            for (Long stockId : stockIds) {
                ProductionPickListsParam listsParam = new ProductionPickListsParam();
                listsParam.setPickListsName(allocation.getAllocationName());
                listsParam.setUserId(allocation.getUserId());
                listsParam.setSource("processTask");
                listsParam.setSourceId(processTask.getProcessTaskId());
                List<ProductionPickListsDetailParam> details = new ArrayList<>();
                List<AllocationCart> updateCart = new ArrayList<>();
                for (AllocationCart allocationCart : allocationCarts) {
                    if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isEmpty(allocationCart.getStorehousePositionsId())) {
                        ProductionPickListsDetailParam listsDetailParam = new ProductionPickListsDetailParam();
                        ToolUtil.copyProperties(allocationCart, listsDetailParam);
                        listsDetailParam.setStatus(0);
                        details.add(listsDetailParam);

                        allocationCart.setStatus(98);
                        updateCart.add(allocationCart);
                    }
                    if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isNotEmpty(allocationCart.getStorehousePositionsId())) {
                        haveTransfer = true;
                    }
                }
                if (details.size() > 0) {
                    List<ProductionPickListsDetailParam> pickListsDetailParams = new ArrayList<>();
                    details.parallelStream().collect(Collectors.groupingBy(i -> i.getSkuId() + "_" + i.getBrandId(), Collectors.toList())).forEach(
                            (id, transfer) -> {
                                transfer.stream().reduce((a, b) -> new ProductionPickListsDetailParam() {{
                                    setSkuId(a.getSkuId());
                                    setBrandId(a.getBrandId());
                                    setNumber(a.getNumber() + b.getNumber());
                                }}).ifPresent(pickListsDetailParams::add);
                            }
                    );
                    listsParam.setPickListsDetailParams(pickListsDetailParams);
                    ProductionPickLists pickLists = productionPickListsService.add(listsParam);
                    for (AllocationCart allocationCart : updateCart) {
                        //setPickListsId 方便对应生成入库单时查找
                        allocationCart.setPickListsId(pickLists.getPickListsId());
                    }
                    allocationCartService.updateBatchById(updateCart);

                }
            }
        }
        shopCartService.addDynamic(allocation.getAllocationId(), null, "指派了调拨物料");
        if (haveTransfer) {
            wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
                setFunction(MarkDownTemplateTypeEnum.transfer);
                setType(1);
                setItems("需要调拨");
                setUrl("/#/Receipts/ReceiptsDetail?" + "id=" + processTask.getProcessTaskId());
                setSource("processTask");
                setSourceId(processTask.getProcessTaskId());
                setCreateTime(processTask.getCreateTime());
                setCreateUser(processTask.getCreateUser());
                setTaskId(processTask.getProcessTaskId());
                setUserIds(new ArrayList<Long>() {{
                    add(processTask.getUserId());
                }});
            }});
        }

    }


    @Override
    public void format(List<AllocationResult> data) {
        List<Long> allocationIds = new ArrayList<>();
        for (AllocationResult datum : data) {
            allocationIds.add(datum.getAllocationId());
        }
        List<AllocationDetailResult> details = allocationIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(allocationDetailService.query().in("allocation_id", allocationIds).eq("display", 1).list(),AllocationDetailResult.class);
        allocationDetailService.format(details);
        List<AllocationCart> allocationCarts = allocationIds.size() == 0 ? new ArrayList<>() : allocationCartService.query().in("allocation_id", allocationIds).eq("type", "carry").eq("display", 1).list();

        for (AllocationResult datum : data) {
            List<Long> skuIds = new ArrayList<>();
            List<Long> storehousePositionsIds = new ArrayList<>();
            int detailNumber = 0;
            int doneNumber = 0;
            List<AllocationDetailResult> detailResultList = new ArrayList<>();
            for (AllocationDetailResult detail : details) {
                if (datum.getAllocationId().equals(detail.getAllocationId())) {
                    if  (detailResultList.size() < 2){
                        detailResultList.add(detail);
                    }
                    skuIds.add(detail.getSkuId());
                    detailNumber += detail.getNumber();
                    if (ToolUtil.isNotEmpty(detail.getStorehousePositionsId())) {
                        storehousePositionsIds.add(detail.getStorehousePositionsId());
                    }
                }

            }
            datum.setDetailResults(detailResultList);
            for (AllocationCart allocationCart : allocationCarts) {
                if (allocationCart.getAllocationId().equals(datum.getAllocationId())) {
                    doneNumber += allocationCart.getDoneNumber();
                }
            }
            datum.setSkuCount((int) skuIds.stream().distinct().count());
            datum.setPositionCount((int) storehousePositionsIds.stream().distinct().count());
            datum.setDetailNumber(detailNumber);
            datum.setDoneNumber(doneNumber);
        }


    }

    @Override
    public void delete(AllocationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public AllocationResult detail(Long allocationId) {

        Allocation allocation = this.getById(allocationId);
        AllocationResult result = BeanUtil.copyProperties(allocation, AllocationResult.class);
        List<AllocationDetailResult> allocationDetailResults = allocationDetailService.resultsByAllocationId(allocationId);
        result.setDetailResults(allocationDetailResults);
        List<AllocationCartResult> allocationCartResults = allocationCartService.resultsByAllocationId(allocationId);
        result.setAllocationCartResults(allocationCartResults);
        result.setStorehouseResult(storehouseService.getDetail(allocation.getStorehouseId()));
        if (ToolUtil.isNotEmpty(result.getUserId())) {
            User user = userService.getById(result.getUserId());
            String imgUrl = stepsService.imgUrl(user.getUserId().toString());
            UserResult userResult = BeanUtil.copyProperties(user, UserResult.class);
            userResult.setAvatar(imgUrl);
            result.setUserResult(userResult);
        }

        Map<Long, String> statusMap = new HashMap<>();
        List<DocumentsStatus> statuses = statusService.list();
        statusMap.put(0L, "开始");
        statusMap.put(49L,"已撤回");
        statusMap.put(49L,"已撤回");
        statusMap.put(99L, "完成");
        statusMap.put(50L, "拒绝");
        for (DocumentsStatus status : statuses) {
            statusMap.put(status.getDocumentsStatusId(), status.getName());
        }
        String statusName = statusMap.get(result.getStatus());
        result.setStatusName(statusName);
        return result;

    }


    @Override
    public void newTransfer(AllocationCartParam param) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> storehousePositionsIds = new ArrayList<>();
        for (AllocationCartParam allocationCartParam : param.getAllocationCartParams()) {
            skuIds.add(allocationCartParam.getSkuId());
            brandIds.add(allocationCartParam.getBrandId());
            storehousePositionsIds.add(allocationCartParam.getStorehousePositionsId());
        }
        if (ToolUtil.isNotEmpty(param.getAllocationId())) {
            //查询全部Cart
            List<AllocationCart> allCarts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).eq("display", 1).eq("type", "carry").list();
            //查询details
            List<AllocationDetail> details = allocationDetailService.query().eq("allocation_id", param.getAllocationId()).eq("display", 1).list();
            List<StockDetails> stockDetails = new ArrayList<>();
            if (skuIds.size() != 0 && brandIds.size() != 0 && storehousePositionsIds.size() != 0) {
                stockDetails = stockDetailsService.query().in("sku_id", skuIds).in("brand_id", brandIds).in("storehouse_positions_id", storehousePositionsIds).eq("display", 1).list();
            }
            if (stockDetails.size() == 0) {
                throw new ServiceException(500, "库存数量不足 无法调用");
            }

            Long allocationId = param.getAllocationId();
            Allocation allocation = this.getById(allocationId);
            /**
             * 查询出库内调拨cart
             */
            List<AllocationCart> allocationCarts = new ArrayList<>();
            if (allocation.getAllocationType().equals(1)) {
                allocationCarts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).in("sku_id", skuIds).in("brand_id", brandIds).in("storehouse_positions_id", storehousePositionsIds).isNull("pick_lists_id").eq("type", "carry").eq("status", 98).eq("display", 1).list();
            } else if (allocation.getAllocationType().equals(2)) {
                allocationCarts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).in("sku_id", skuIds).in("brand_id", brandIds).eq("storehouse_positions_id", param.getToStorehousePositionsId()).isNull("pick_lists_id").eq("type", "carry").eq("status", 98).eq("display", 1).list();
            }
            List<AllocationLogDetail> allocationLogDetails = new ArrayList<>();

            for (AllocationCartParam allocationCartParam : param.getAllocationCartParams()) {
                //TODO 数量防空抛异常
                int number = allocationCartParam.getNumber();
                if (allocationCarts.size() > 0) {
                    for (StockDetails stockDetail : stockDetails) {
                        if (number > 0) {
                            if (ToolUtil.isNotEmpty(allocationCartParam.getInkindId()) && stockDetail.getInkindId().equals(allocationCartParam.getInkindId())) {
                                int kickNum = number;
                                number = Math.toIntExact(number - stockDetail.getNumber());
                                addLogs(number, kickNum, allocation, param, allocationCartParam, stockDetail, allocationLogDetails, allCarts);

                            }
                        }
                    }
                    for (StockDetails stockDetail : stockDetails) {
                        if (number > 0) {
                            if (ToolUtil.isEmpty(allocationCartParam.getInkindId())) {
                                int kickNum = number;
                                number = Math.toIntExact(number - stockDetail.getNumber());
                                addLogs(number, kickNum, allocation, param, allocationCartParam, stockDetail, allocationLogDetails, allCarts);

                            }
                        }
                    }
                }


            }

            allocationCartService.updateBatchById(allCarts);
            int detailCount = 0;
            for (AllocationDetail detail : details) {
                detailCount += detail.getNumber();
            }
            int cartCount = 0;
            for (AllocationCart cart : allCarts) {
                cartCount += cart.getNumber();
            }
            if (allCarts.stream().allMatch(i -> i.getStatus().equals(99)) && detailCount == cartCount && detailCount > 0) {
                for (AllocationDetail allocationDetail : details) {
                    allocationDetail.setStatus(99);
                }
                this.checkCarry(param.getAllocationId());
            }
            stockDetailsService.updateBatchById(stockDetails);
            AllocationLog allocationLog = new AllocationLog();
            allocationLog.setAllocationId(param.getAllocationId());
            String code = RandomUtil.randomString(5);
            allocationLog.setCoding(code);
            allocationLogService.save(allocationLog);
            for (AllocationLogDetail allocationLogDetail : allocationLogDetails) {
                allocationLogDetail.setAllocationLogId(allocationLog.getAllocationLogId());
            }
            allocationLogDetailService.saveBatch(allocationLogDetails);
            shopCartService.addDynamic(param.getAllocationId(), null, "库内调拨了物料");
        }
    }


    private void addLogs(int number, int kickNum, Allocation allocation, AllocationCartParam param, AllocationCartParam allocationCartParam, StockDetails stockDetail, List<AllocationLogDetail> allocationLogDetails, List<AllocationCart> allCarts) {

        if (number >= 0) {
            AllocationLogDetail allocationLogDetail = new AllocationLogDetail();
            allocationLogDetail.setInkindId(stockDetail.getInkindId());
            allocationLogDetail.setStorehousePositionsId(stockDetail.getStorehousePositionsId());
            allocationLogDetail.setToStorehousePositionsId(allocationCartParam.getToStorehousePositionsId());
            allocationLogDetail.setSkuId(stockDetail.getSkuId());
            allocationLogDetail.setBrandId(stockDetail.getBrandId());
            allocationLogDetail.setAllocationId(allocationCartParam.getAllocationId());  //song
            allocationLogDetail.setNumber(Math.toIntExact(stockDetail.getNumber()));
            allocationLogDetails.add(allocationLogDetail);
            stockDetail.setStorehousePositionsId(allocationCartParam.getToStorehousePositionsId());
            stockDetailsService.updateById(stockDetail);
            this.checkCartNumber(allocationCartParam, allCarts, allocation, Math.toIntExact(stockDetail.getNumber()));
        } else {
            //拆分实物创建新的实物
            Inkind inkind = new Inkind();
            stockDetail.setNumber(stockDetail.getNumber() - kickNum);
            ToolUtil.copyProperties(stockDetail, inkind);
            inkind.setInkindId(null);
            if (ToolUtil.isNotEmpty(stockDetail.getCustomerId())) {
                inkind.setCustomerId(stockDetail.getCustomerId());
            }
            inkind.setBrandId(stockDetail.getBrandId());
            inkind.setSource("inkind");
            inkind.setSourceId(stockDetail.getInkindId());
            inkind.setCreateUser(null);
            inkind.setCreateTime(null);
            inkind.setUpdateTime(null);
            inkind.setUpdateUser(null);
            inkindService.save(inkind);
            //添加调拨记录
            AllocationLogDetail allocationLogDetail = new AllocationLogDetail();
            allocationLogDetail.setInkindId(inkind.getInkindId());
            allocationLogDetail.setNumber(kickNum);
            allocationLogDetail.setSkuId(inkind.getSkuId());
            allocationLogDetail.setBrandId(inkind.getBrandId());
            allocationLogDetail.setStorehousePositionsId(allocationCartParam.getStorehousePositionsId());
            allocationLogDetail.setStorehouseId(allocationCartParam.getStorehouseId());
            allocationLogDetail.setToStorehouseId(allocationCartParam.getToStorehouseId());
            allocationLogDetail.setAllocationId(allocationCartParam.getAllocationId());  //song
            allocationLogDetail.setToStorehousePositionsId(allocationCartParam.getToStorehousePositionsId());
            allocationLogDetails.add(allocationLogDetail);
            //因是创建实物  故创建库存
            StockDetails stockDetailEntity = BeanUtil.copyProperties(stockDetail, StockDetails.class);
            stockDetailEntity.setInkindId(inkind.getInkindId());
            stockDetailEntity.setStorehousePositionsId(allocationCartParam.getToStorehousePositionsId());
            stockDetailEntity.setNumber((long) kickNum);
            stockDetailEntity.setUpdateTime(null);
            stockDetailEntity.setUpdateUser(null);
            stockDetailEntity.setCreateTime(null);
            stockDetailEntity.setCreateUser(null);
            stockDetailsService.save(stockDetailEntity);
            stockDetailsService.updateById(stockDetail);
            this.checkCartNumber(allocationCartParam, allCarts, allocation, kickNum);
        }

    }

    /**
     * 库间调拨接口
     * 更改实物库位
     * 保存调拨记录
     *
     * @param param
     */
    @Override
    @Transactional
    public void transferInStorehouse(AllocationCartParam param) {

        Long skuId = param.getSkuId();
        Long brandId = param.getBrandId();

        Long storehousePositionsId = param.getStorehousePositionsId();
        Long toStorehousePositionsId = param.getToStorehousePositionsId();
        List<StockDetails> stockDetails = stockDetailsService.query().eq("sku_id", skuId).eq("brand_id", brandId).eq("storehouse_positions_id", storehousePositionsId).eq("display", 1).list();
        if (stockDetails.size() == 0) {
            throw new ServiceException(500, "库存数量不足 无法调用");
        }
        List<AllocationLogDetail> allocationLogDetails = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getAllocationId())) {
            Allocation allocation = this.getById(param.getAllocationId());
            List<AllocationCart> allCarts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).eq("display", 1).eq("type", "carry").list();
            List<AllocationDetail> details = allocationDetailService.query().eq("allocation_id", param.getAllocationId()).eq("display", 1).list();
            List<AllocationCart> allocationCarts = new ArrayList<>();
            if (allocation.getAllocationType().equals(1)) {
                allocationCarts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).eq("sku_id", skuId).eq("brand_id", brandId).eq("storehouse_positions_id", storehousePositionsId).eq("type", "carry").eq("status", 98).eq("display", 1).list();
            } else if (allocation.getAllocationType().equals(2)) {
                allocationCarts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).eq("sku_id", skuId).eq("brand_id", brandId).eq("storehouse_positions_id", toStorehousePositionsId).eq("type", "carry").eq("status", 98).eq("display", 1).list();
            }

            Integer number = param.getNumber();
            if (allocationCarts.size() > 0) {
                int kickNum = number;
                for (StockDetails stockDetail : stockDetails) {
                    if (number > 0) {
                        number = Math.toIntExact(number - stockDetail.getNumber());
                        if (number >= 0) {
                            AllocationLogDetail allocationLogDetail = new AllocationLogDetail();
                            allocationLogDetail.setInkindId(stockDetail.getInkindId());
                            allocationLogDetail.setStorehousePositionsId(stockDetail.getStorehousePositionsId());
                            allocationLogDetail.setToStorehousePositionsId(param.getToStorehousePositionsId());
                            allocationLogDetail.setSkuId(stockDetail.getSkuId());
                            allocationLogDetail.setBrandId(stockDetail.getBrandId());
                            allocationLogDetail.setAllocationId(param.getAllocationId());  //song
                            allocationLogDetail.setNumber(Math.toIntExact(stockDetail.getNumber()));
                            allocationLogDetails.add(allocationLogDetail);
                            stockDetail.setStorehousePositionsId(param.getToStorehousePositionsId());
                            stockDetailsService.updateById(stockDetail);
                            this.checkCartNumber(param, allCarts, allocation, Math.toIntExact(stockDetail.getNumber()));
                        } else {
                            //拆分实物创建新的实物
                            Inkind inkind = new Inkind();
                            stockDetail.setNumber(stockDetail.getNumber() - kickNum);
                            ToolUtil.copyProperties(stockDetail, inkind);
                            inkind.setInkindId(null);
                            inkind.setSource("inkind");
                            inkind.setSourceId(stockDetail.getInkindId());
                            inkindService.save(inkind);
                            //添加调拨记录
                            AllocationLogDetail allocationLogDetail = new AllocationLogDetail();
                            allocationLogDetail.setInkindId(inkind.getInkindId());
                            allocationLogDetail.setNumber(kickNum);
                            allocationLogDetail.setSkuId(inkind.getSkuId());
                            allocationLogDetail.setBrandId(inkind.getBrandId());
                            allocationLogDetail.setStorehousePositionsId(param.getStorehousePositionsId());
                            allocationLogDetail.setStorehouseId(param.getStorehouseId());
                            allocationLogDetail.setToStorehouseId(param.getToStorehouseId());
                            allocationLogDetail.setAllocationId(param.getAllocationId());  //song
                            allocationLogDetail.setToStorehousePositionsId(param.getToStorehousePositionsId());
                            allocationLogDetails.add(allocationLogDetail);
                            //因是创建实物  故创建库存
                            StockDetails stockDetailEntity = new StockDetails();
                            stockDetailEntity.setInkindId(inkind.getInkindId());
                            stockDetailEntity.setSkuId(param.getSkuId());
                            stockDetailEntity.setStorehouseId(param.getToStorehouseId());
                            stockDetailEntity.setStorehousePositionsId(param.getToStorehousePositionsId());
                            stockDetailEntity.setNumber((long) kickNum);
                            stockDetailEntity.setBrandId(stockDetail.getBrandId());
                            stockDetailsService.save(stockDetailEntity);
                            stockDetailsService.updateById(stockDetail);
                            this.checkCartNumber(param, allCarts, allocation, kickNum);
                        }
                    }
                }
                allocationCartService.updateBatchById(allCarts);


                int detailCount = 0;
                for (AllocationDetail detail : details) {
                    detailCount += detail.getNumber();
                }
                int cartCount = 0;
                for (AllocationCart cart : allCarts) {
                    cartCount += cart.getNumber();
                }
                if (allCarts.stream().allMatch(i -> i.getStatus().equals(99)) && detailCount == cartCount && detailCount > 0) {
                    for (AllocationDetail allocationDetail : details) {
                        allocationDetail.setStatus(99);
                    }
                    this.checkCarry(param.getAllocationId());
                }
            }
        }

        stockDetailsService.updateBatchById(stockDetails);
        AllocationLog allocationLog = new AllocationLog();
        allocationLog.setAllocationId(param.getAllocationId());
        String code = RandomUtil.randomString(5);
        allocationLog.setCoding(code);
        allocationLogService.save(allocationLog);
        for (AllocationLogDetail allocationLogDetail : allocationLogDetails) {
            allocationLogDetail.setAllocationLogId(allocationLog.getAllocationLogId());
        }
        allocationLogDetailService.saveBatch(allocationLogDetails);
        shopCartService.addDynamic(param.getAllocationId(), null, "库内调拨了物料");

    }


    private void checkCartNumber(AllocationCartParam param, List<AllocationCart> allCarts, Allocation allocation, Integer number) {
        /**
         * 处理 carts DoneNumber
         */

        for (AllocationCart cart : allCarts) {
            if (number > 0) {
                if (ToolUtil.isNotEmpty(cart.getStorehousePositionsId())) {
                    if (allocation.getAllocationType().equals(1) && param.getSkuId().equals(cart.getSkuId()) && param.getBrandId().equals(cart.getBrandId()) && param.getStorehousePositionsId().equals(cart.getStorehousePositionsId()) && cart.getStatus().equals(98)) {
                        int lastNumber = number;
                        number = number - (cart.getNumber() - cart.getDoneNumber());
                        if (number >= 0) {
                            cart.setDoneNumber(cart.getNumber());
                            cart.setStatus(99);
                        } else {
                            cart.setDoneNumber(cart.getDoneNumber() + lastNumber);
                            break;
                        }
                    } else if (allocation.getAllocationType().equals(2) && param.getSkuId().equals(cart.getSkuId()) && param.getBrandId().equals(cart.getBrandId()) && param.getToStorehousePositionsId().equals(cart.getStorehousePositionsId()) && cart.getStatus().equals(98)) {
                        int lastNumber = number;
                        number = number - (cart.getNumber() - cart.getDoneNumber());
                        if (number >= 0) {
                            cart.setDoneNumber(cart.getNumber());
                            cart.setStatus(99);
                        } else {
                            cart.setDoneNumber(cart.getDoneNumber() + lastNumber);
                            break;
                        }
                    }
                }
            }

        }
    }

    @Override
    public void update(AllocationParam param) {
        Allocation oldEntity = getOldEntity(param);
        Allocation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationResult findBySpec(AllocationParam param) {
        return null;
    }

    @Override
    public List<AllocationResult> findListBySpec(AllocationParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(AllocationParam param) {
        Page<AllocationResult> pageContext = getPageContext();
        IPage<AllocationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationParam param) {
        return param.getAllocationId();
    }

    private Page<AllocationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Allocation getOldEntity(AllocationParam param) {
        return this.getById(getKey(param));
    }

    private Allocation getEntity(AllocationParam param) {
        Allocation entity = new Allocation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void createOrder(AllocationParam param) {
        if (ToolUtil.isEmpty(param.getUserId())) {
            throw new ServiceException(500, "请填写负责人");
        }
        if (ToolUtil.isEmpty(param.getAllocationId())) {
            throw new ServiceException(500, "请选择单据");
        }
        Allocation allocation = this.getById(param.getAllocationId());

        List<AllocationDetail> details = allocationDetailService.query().eq("allocation_id", param.getAllocationId()).eq("status", 0).list();
        List<AllocationCart> carts = allocationCartService.query().eq("allocation_id", param.getAllocationId()).eq("display", 1).eq("status", 0).eq("type", "carry").list();


        for (AllocationCart cart : carts) {
            cart.setStatus(98);
            int num = cart.getNumber();
            for (AllocationDetail detail : details) {
                if (num > 0) {
                    if (cart.getAllocationDetailId().equals(detail.getAllocationDetailId())) {
                        detail.setCarryNumber(detail.getCarryNumber() + num);
                        if (Objects.equals(detail.getCarryNumber(), detail.getNumber())) {
                            detail.setStatus(98);
                        }
                        num -= (detail.getNumber() - detail.getCarryNumber());

                    }
                }
            }

        }
        allocationDetailService.updateBatchById(details);
        allocationCartService.updateBatchById(carts);
        this.createPickListsAndInStockOrder(param, carts);
        details = allocationDetailService.query().eq("allocation_id", param.getAllocationId()).list();
        if (details.stream().noneMatch(i -> i.getStatus().equals(0))) {
            checkCart(param.getAllocationId());
        }
    }

    @Override
    public void checkCartDone(Long allocationId, List<InstockList> listing) {
        List<AllocationCart> allocationCarts = allocationCartService.query().eq("display", 1).eq("allocation_id", allocationId).list();
        List<AllocationDetail> allocationDetails = allocationDetailService.query().eq("display", 1).eq("allocation_id", allocationId).list();
        int detailCount = 0;
        int cartCount = 0;

        for (AllocationCart allocationCart : allocationCarts) {
            cartCount += allocationCart.getNumber();
        }
        for (AllocationDetail allocationDetail : allocationDetails) {
            detailCount += allocationDetail.getNumber();
        }

        for (InstockList instockList : listing) {
            int number = Math.toIntExact(instockList.getNumber());
            for (AllocationCart allocationCart : allocationCarts) {
                if (number > 0) {
                    if (allocationCart.getStatus().equals(98) && instockList.getInstockOrderId().equals(allocationCart.getInstockOrderId()) && instockList.getSkuId().equals(allocationCart.getSkuId())) {
                        int lastNumber = number;
                        number -= (allocationCart.getNumber() - allocationCart.getDoneNumber());
                        if (number >= 0) {
                            allocationCart.setDoneNumber((int) (allocationCart.getDoneNumber() + instockList.getNumber()));

                        } else {
                            allocationCart.setDoneNumber((allocationCart.getDoneNumber() + lastNumber));
                        }
                    }
                    if (allocationCart.getDoneNumber().equals(allocationCart.getNumber())) {
                        allocationCart.setStatus(99);
                    }
                }
            }

        }

        if (allocationCarts.stream().allMatch(i -> i.getStatus().equals(99)) && detailCount == cartCount && detailCount > 0) {
            for (AllocationDetail allocationDetail : allocationDetails) {
                allocationDetail.setStatus(99);
            }
            this.checkCart(allocationId);
        }
        allocationCartService.updateBatchById(allocationCarts);
        allocationDetailService.updateBatchById(allocationDetails);
    }

    @Override
    public List<InstockListResult> getInstockListResultsByAllocationTask(Long taskId) {
        List<ActivitiProcessTask> childrenTasks = activitiProcessTaskService.query().eq("pid", taskId).list();
        List<Long> formIds = new ArrayList<>();
        for (ActivitiProcessTask childrenTask : childrenTasks) {
            if (childrenTask.getType().equals("INSTOCK")) {
                formIds.add(childrenTask.getFormId());
            }
        }
        List<InstockList> instock_order_id = formIds.size() == 0 ? new ArrayList<>() : instockListService.query().in("instock_order_id", formIds).list();
        List<InstockListResult> instockListResults = BeanUtil.copyToList(instock_order_id, InstockListResult.class);
        instockListService.format(instockListResults);
        return instockListResults;

    }
}
