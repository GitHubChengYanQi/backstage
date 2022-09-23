package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.MaintenanceActionEnum;
import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.MaintenanceMapper;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceAndInventorySelectParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.message.enmu.OperationType.SAVEDETAILS;

/**
 * <p>
 * 养护申请主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@Service
public class MaintenanceServiceImpl extends ServiceImpl<MaintenanceMapper, Maintenance> implements MaintenanceService {
    @Autowired
    private MaintenanceDetailService maintenanceDetailService;

    @Autowired
    private MaintenanceLogService maintenanceLogService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockDetailsService stockDetailsService;


    @Autowired
    private DocumentsActionService actionService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessLogV1Service activitiProcessLogService;

    @Autowired
    private CodingRulesService codingRulesService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Autowired
    private MaintenanceCycleService maintenanceCycleService;


    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnouncementsService announcementsService;

    @Autowired
    private StepsService stepsService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private RemarksService remarksService;
    @Autowired
    private MaintenanceLogDetailService logDetailService;
    @Autowired
    private ProductionPickListsCartService pickListsCartService;
    @Autowired
    private ShopCartService shopCartService;

    @Override

    public Maintenance add(MaintenanceParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "16").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置养护单据自动生成编码规则");
            }
        }

        // 根据2个查询维度  写出两个查询方法  (条件查询维度 对应 查询出实物),(物料维度直接通过sku查询出库存实物  进行养护) （）实物 或 sku
        Maintenance entity = getEntity(param);
        String selectParams = JSON.toJSONString(param.getSelectParams());
        entity.setSelectParams(selectParams);
        entity.setMaintenanceName(LoginContextHolder.getContext().getUser().getName() + "创建的养护任务");
        this.save(entity);
        this.saveDetails(entity);
//        messageProducer.microService(new MicroServiceEntity() {{
//            setObject(entity);
//            setOperationType(SAVEDETAILS);
//            setType(MicroServiceType.MAINTENANCE);
//            setMaxTimes(2);
//            setTimes(0);
//        }});


        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "MAINTENANCE").eq("status", 99).eq("module", "reMaintenance").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            activitiProcessTaskService.checkStartUser(activitiProcess.getProcessId());
            auditService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            String name = LoginContextHolder.getContext().getUser().getName();
            activitiProcessTaskParam.setTaskName(name + "养护申请 ");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setFormId(entity.getMaintenanceId());
            activitiProcessTaskParam.setType("MAINTENANCE");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
            ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);

            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());
            shopCartService.addDynamic(entity.getMaintenanceId(), null, "发起了养护申请");
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
            throw new ServiceException(500, "请创建质检流程！");
        }
        return entity;
    }

    @Override
    public void saveDetails(Maintenance entity) {
        List<StockDetails> stockDetails = this.needMaintenanceByRequirement(entity);

        List<StockDetails> detailTotalList = new ArrayList<>();

        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()) + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setNumber(a.getNumber() + b.getNumber());
                        setSkuId(a.getSkuId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                        setBrandId(a.getBrandId());
                        setStorehouseId(a.getStorehouseId());
                    }}).ifPresent(detailTotalList::add);
                }
        );


        List<MaintenanceDetail> details = new ArrayList<>();
        for (StockDetails stockDetail : detailTotalList) {
            MaintenanceDetail detail = new MaintenanceDetail();
            ToolUtil.copyProperties(stockDetail, detail);
            detail.setMaintenanceId(entity.getMaintenanceId());
            details.add(detail);
        }
        if (details.size() == 0) {
            throw new ServiceException(500, "当前条件未找到需要养护物料");
        }
        maintenanceDetailService.saveBatch(details);
    }

    @Override
    public List<StockDetails> needMaintenanceByRequirement(Maintenance param) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> inkindIds = new ArrayList<>();
        List<StockDetails> stockDetails = new ArrayList<>();
        List<MaintenanceAndInventorySelectParam> maintenanceAndInventorySelectParams = JSON.parseArray(param.getSelectParams(), MaintenanceAndInventorySelectParam.class);
        for (MaintenanceAndInventorySelectParam maintenanceAndInventorySelectParam : maintenanceAndInventorySelectParams) {
            InventoryDetailParam inventoryDetailParam = new InventoryDetailParam();
            inventoryDetailParam.setBrandIds(maintenanceAndInventorySelectParam.getBrandIds());
            inventoryDetailParam.setBomIds(maintenanceAndInventorySelectParam.getPartsIds());
            inventoryDetailParam.setPositionIds(maintenanceAndInventorySelectParam.getStorehousePositionsIds());
            inventoryDetailParam.setClassIds(maintenanceAndInventorySelectParam.getSpuClassificationIds());
            inventoryDetailParam.setSkuIds(maintenanceAndInventorySelectParam.getSkuIds());
            inventoryDetailParam.setSpuIds(maintenanceAndInventorySelectParam.getSpuIds());
            inventoryDetailParam.setInkindIds(maintenanceAndInventorySelectParam.getInkindIds());
            List<InventoryStock> condition = inventoryService.condition(inventoryDetailParam);

            for (InventoryStock inventoryStock : condition) {
                inkindIds.add(inventoryStock.getInkindId());
            }
            if (ToolUtil.isNotEmpty(maintenanceAndInventorySelectParam.getInkindIds())) {
                inkindIds.addAll(maintenanceAndInventorySelectParam.getInkindIds());
            }
            for (InventoryStock inventoryDetail : condition) {
                skuIds.add(inventoryDetail.getSkuId());
            }
        }
        inkindIds = inkindIds.stream().distinct().collect(Collectors.toList());
        List<Long> lockedInkindIds = pickListsCartService.getLockedInkindIds();
        inkindIds.removeAll(lockedInkindIds);

        stockDetails = inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
        List<MaintenanceCycle> maintenanceCycles = skuIds.size() == 0 ? new ArrayList<>() : maintenanceCycleService.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Inkind> inkinds = inkindIds.size() == 0 ? new ArrayList<>() : inkindService.listByIds(inkindIds);
        List<MaintenanceLogDetail> logDetails =skuIds.size() == 0 ? new ArrayList<>() : logDetailService.query().in("sku_id", skuIds).list();
        /**
         * 查询养护记录   被养护过的实物计算总数  如果此实物养护记录数量小于库存数量  证明此实物未被全部养护完（针对批量物料）
         */
        for (StockDetails stockDetail : stockDetails) {
            for (MaintenanceLogDetail logDetail : logDetails) {
                if (stockDetail.getInkindId().equals(logDetail.getInkindId())) {
                    stockDetail.setNumber(stockDetail.getNumber() - logDetail.getNumber());
                }
            }
        }
        stockDetails.removeIf(i -> i.getNumber() <= 0);


        /**
         * 如果实物没在此任务中养护过 并且下次养护时间大于当前时间
         * 则证明此物料不是所需要养护的物料
         * 如果在此任务中有过养护记录 证明批量物料养护  养护时更新了实物下次养护时间但是此批量实物未必全部养护完成
         */
        if (ToolUtil.isNotEmpty(param.getNearMaintenance())) {
            for (Inkind inkind : inkinds) {
                for (MaintenanceCycle maintenanceCycle : maintenanceCycles) {
                    if (inkind.getSkuId().equals(maintenanceCycle.getSkuId()) && ToolUtil.isNotEmpty(inkind.getLastMaintenanceTime()) && param.getNearMaintenance().getTime() < inkind.getLastMaintenanceTime().getTime() && logDetails.stream().noneMatch(i -> i.getInkindId().equals(inkind.getInkindId()))) {
                        stockDetails.removeIf(i -> i.getInkindId().equals(inkind.getInkindId()));

                    }
                }
            }
        }
        return stockDetails;
        //根据此条件去库存查询需要养护的实物

    }

    @Override
    public List<Maintenance> findTaskByTime() {
        Long userId = LoginContextHolder.getContext().getUserId();
        List<Maintenance> list = this.query().eq("display", 1).eq("user_id", userId).ne("status", 99).apply(" now() >  start_time ").list();
        list.addAll(this.query().eq("display", 1).eq("user_id", userId).eq("status", 98).list());
        list = list.stream().distinct().collect(Collectors.toList());
        return list;
    }

    @Override
    public MaintenanceAndDetail findTaskAndDetailByTime() {
        List<Maintenance> taskByTime = findTaskByTime();
        List<Long> ids = new ArrayList<>();
        for (Maintenance maintenance : taskByTime) {
            ids.add(maintenance.getMaintenanceId());
        }
        List<MaintenanceDetail> details = ids.size() == 0 ? new ArrayList<>() : maintenanceDetailService.query().eq("status", 0).eq("display", 1).in("maintenance_id", ids).list();
        MaintenanceAndDetail result = new MaintenanceAndDetail();
        result.setMaintenances(taskByTime);
        result.setMaintenanceDetails(details);
        return result;
    }

    //任务开始
    @Override
    public void startMaintenance(Maintenance maintenanceParam) {
        //如果从单个任务进入详情   则只更新该任务的养护物料列表
        if (ToolUtil.isNotEmpty(maintenanceParam) && maintenanceParam.getStatus().equals(0) && maintenanceParam.getStartTime().getTime() < new Date().getTime()) {
            updateDetail(maintenanceParam);
        } else if (ToolUtil.isNotEmpty(maintenanceParam) && maintenanceParam.getStatus().equals(98)) {
            updateDetail(maintenanceParam);

        }

    }

    @Override
    public void delete(MaintenanceParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceParam param) {
        Maintenance oldEntity = getOldEntity(param);
        Maintenance newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    private void checkDetails(Maintenance maintenance) {
        //任务为开始状态时 从新计算实物  然后  在任务进行中时再入库的实物不参加本次 养护
        MaintenanceDetail remove = new MaintenanceDetail();
        remove.setDisplay(0);
        QueryWrapper<MaintenanceDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("maintenance_id", maintenance.getMaintenanceId());
        this.maintenanceDetailService.update(remove, queryWrapper);

        //从新计算需要养护物品
        List<StockDetails> stockDetails = this.needMaintenanceByRequirement(maintenance);
        List<MaintenanceDetail> details = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            MaintenanceDetail detail = new MaintenanceDetail();
            ToolUtil.copyProperties(stockDetail, detail);
            detail.setMaintenanceId(maintenance.getMaintenanceId());
            details.add(detail);
        }
        maintenanceDetailService.saveBatch(details);
    }

    @Override
    public MaintenanceResult findBySpec(MaintenanceParam param) {
        return null;
    }

    @Override
    public List<MaintenanceResult> findListBySpec(MaintenanceParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(MaintenanceParam param) {
        Page<MaintenanceResult> pageContext = getPageContext();
        IPage<MaintenanceResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceParam param) {
        return param.getMaintenanceId();
    }

    @Override
    public void format(List<MaintenanceResult> param) {
        List<Long> skuIds = new ArrayList<>();

        List<Long> ids = new ArrayList<>();

        List<Long> userIds = new ArrayList<>();

        for (MaintenanceResult maintenanceResult : param) {
            ids.add(maintenanceResult.getMaintenanceId());
            if (ToolUtil.isNotEmpty(maintenanceResult.getSelectParams())) {
                List<MaintenanceAndInventorySelectParam> selectParams = JSON.parseArray(maintenanceResult.getSelectParams(), MaintenanceAndInventorySelectParam.class);
                for (MaintenanceAndInventorySelectParam selectParam : selectParams) {
                    skuIds.add(selectParam.getSkuId());
                }
            }
            userIds.add(maintenanceResult.getUserId());
        }
        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);

        List<UserResult> userResults = userService.getUserResultsByIds(userIds);

        List<MaintenanceDetail> maintenanceDetails = ids.size() == 0 ? new ArrayList<>() : maintenanceDetailService.query().in("maintenance_id", ids).list();

        for (MaintenanceResult maintenanceResult : param) {
            for (UserResult userResult : userResults) {
                if (maintenanceResult.getUserId().equals(userResult.getUserId())) {
                    userResult.setAvatar(stepsService.imgUrl(userResult.getUserId().toString()));
                    maintenanceResult.setUserResult(userResult);
                }
                if (userResult.getUserId().equals(maintenanceResult.getCreateUser())) {
                    userResult.setAvatar(stepsService.imgUrl(userResult.getUserId().toString()));
                    maintenanceResult.setCreateUserResult(userResult);
                }
            }
            if (ToolUtil.isNotEmpty(maintenanceResult.getSelectParams())) {

                List<MaintenanceAndInventorySelectParam> selectParams = JSON.parseArray(maintenanceResult.getSelectParams(), MaintenanceAndInventorySelectParam.class);
                for (MaintenanceAndInventorySelectParam selectParam : selectParams) {
                    for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                        if (skuSimpleResult.getSkuId().equals(selectParam.getSkuId())) {
                            selectParam.setSkuResult(skuSimpleResult);
                        }
                    }
                }
                maintenanceResult.setSelectParamResults(selectParams);
            }

            List<Long> storehousePositionsIds = new ArrayList<>();
            skuIds = new ArrayList<>();
            Integer num = 0;
            Integer doneNumber = 0;
            List<SkuSimpleResult> skuSimpleResultList = new ArrayList<>();

            for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
                if (maintenanceDetail.getMaintenanceId().equals(maintenanceResult.getMaintenanceId())) {
                    storehousePositionsIds.add(maintenanceDetail.getStorehousePositionsId());
                    skuIds.add(maintenanceDetail.getSkuId());
                    num += maintenanceDetail.getNumber();
                    doneNumber += maintenanceDetail.getDoneNumber();
                    for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                        if( skuSimpleResultList.size() <2 && maintenanceDetail.getSkuId().equals(skuSimpleResult.getSkuId())){
                                skuSimpleResultList.add(skuSimpleResult);
                        }
                    }

                }
            }
            maintenanceResult.setSkuResults(skuSimpleResultList);
            skuIds = skuIds.stream().distinct().collect(Collectors.toList());
            storehousePositionsIds = storehousePositionsIds.stream().distinct().collect(Collectors.toList());
            maintenanceResult.setNumberCount(num);
            maintenanceResult.setSkuCount(skuIds.size());
            maintenanceResult.setPositionCount(storehousePositionsIds.size());
            maintenanceResult.setDoneNumberCount(doneNumber);
        }
    }

    private Page<MaintenanceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Maintenance getOldEntity(MaintenanceParam param) {
        return this.getById(getKey(param));
    }

    @Override
    public List<MaintenanceResult> resultsByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }
        List<Maintenance> maintenances = this.listByIds(ids);
        List<MaintenanceResult> maintenanceResults = BeanUtil.copyToList(maintenances, MaintenanceResult.class);
        this.format(maintenanceResults);
        return maintenanceResults;
    }

    private Maintenance getEntity(MaintenanceParam param) {
        Maintenance entity = new Maintenance();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<StorehousePositionsResult> getDetails(Long maintenanceId) {
        if (ToolUtil.isNotEmpty(maintenanceId)) {
            List<MaintenanceDetail> details = maintenanceDetailService.query().eq("display", 1).eq("status", 0).eq("maintenance_id", maintenanceId).list();
            for (MaintenanceDetail detail : details) {
                detail.setNumber(detail.getNumber() - detail.getDoneNumber());
            }
            List<MaintenanceDetail> totalList = new ArrayList<>();
            if (ToolUtil.isNotEmpty(details)) {
                details.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()) + '_' + item.getStorehousePositionsId() + "_" + item.getMaintenanceId(), Collectors.toList())).forEach(
                        (id, transfer) -> {
                            transfer.stream().reduce((a, b) -> new MaintenanceDetail() {{
                                setSkuId(a.getSkuId());
                                setNumber(a.getNumber() + b.getNumber());
                                setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                                setStorehousePositionsId(a.getStorehousePositionsId());
                                setMaintenanceId(a.getMaintenanceId());
                            }}).ifPresent(totalList::add);
                        }
                );
            }
            /**
             * 获取库位
             */
            List<Long> skuIds = new ArrayList<>();
            List<Long> positionIds = new ArrayList<>();
            List<Long> brandIds = new ArrayList<>();
            for (MaintenanceDetail detail : totalList) {
                positionIds.add(detail.getStorehousePositionsId());
                skuIds.add(detail.getSkuId());
                if (ToolUtil.isNotEmpty(detail.getBrandId())) {
                    brandIds.add(detail.getBrandId());
                }
            }
            List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);

            List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
            brandResults.add(new BrandResult() {{
                setBrandId(0L);
                setBrandName("无品牌");
            }});
            positionIds = positionIds.stream().distinct().collect(Collectors.toList());
            List<StorehousePositionsResult> storehousePositions = positionIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.getDetails(positionIds);
            for (StorehousePositionsResult storehousePosition : storehousePositions) {
                List<MaintenanceDetail> positionTotalList = new ArrayList<>();
                for (MaintenanceDetail detail : totalList) {
                    if (detail.getStorehousePositionsId().equals(storehousePosition.getStorehousePositionsId())) {
                        positionTotalList.add(detail);
                    }
                }
                List<SkuSimpleResult> positionSkuResult = new ArrayList<>();
                for (MaintenanceDetail detail : positionTotalList) {
                    for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                        if (detail.getSkuId().equals(skuSimpleResult.getSkuId()) && positionSkuResult.stream().noneMatch(i -> i.getSkuId().equals(skuSimpleResult.getSkuId()))) {
                            positionSkuResult.add(skuSimpleResult);
                        }
                    }
                }
                List<Map<String, Object>> skus = new ArrayList<>();
                for (SkuSimpleResult skuSimpleResult : positionSkuResult) {
                    Map<String, Object> skuMap = BeanUtil.beanToMap(skuSimpleResult);
                    List<Map<String, Object>> brands = new ArrayList<>();
                    for (MaintenanceDetail detail : positionTotalList) {
                        if (ToolUtil.isEmpty(detail.getBrandId())) {
                            detail.setBrandId(0L);
                        }
                        if (detail.getStorehousePositionsId().equals(storehousePosition.getStorehousePositionsId()) && detail.getSkuId().equals(skuSimpleResult.getSkuId())) {
                            skuMap.put("maintenanceId", detail.getMaintenanceId());
                            for (BrandResult brandResult : brandResults) {
                                if (detail.getBrandId().equals(brandResult.getBrandId())) {
                                    Map<String, Object> brandMap = new HashMap<>();
                                    brandMap.put("brandName", brandResult.getBrandName());
                                    brandMap.put("brandId", brandResult.getBrandId());
                                    brandMap.put("number", detail.getNumber());
                                    brands.add(brandMap);
                                }
                            }
                        }
                    }
                    skuMap.put("brandResults", brands);
                    skus.add(skuMap);
                }

                storehousePosition.setObject(skus);
            }
            return storehousePositions;
        }
        return null;
    }

    //自动判断是否满足  时间条件   自动开始任务
    @Override
    public MaintenanceResult detail(Long id) {

        Maintenance maintenance = this.getById(id);
        if (ToolUtil.isNotEmpty(maintenance)) {
            startMaintenance(maintenance);
        }
        Map<Long, String> statusMap = new HashMap<>();
        statusMap.put(0L, "开始");
        statusMap.put(99L, "完成");
        statusMap.put(50L, "拒绝");
        MaintenanceResult maintenanceResult = new MaintenanceResult();
        ToolUtil.copyProperties(maintenance, maintenanceResult);
        this.format(new ArrayList<MaintenanceResult>() {{
            add(maintenanceResult);
        }});
        List<Long> userIds = new ArrayList<>();
        userIds.add(maintenanceResult.getCreateUser());
        userIds.add(maintenanceResult.getUserId());
        if (ToolUtil.isNotEmpty(maintenanceResult.getEnclosure())) {
            List<String> enclosureUrl = new ArrayList<>();
            List<Long> enclosureIds = Arrays.asList(maintenanceResult.getEnclosure().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            for (Long enclosureId : enclosureIds) {
                enclosureUrl.add(mediaService.getMediaUrl(enclosureId, 0L));
            }
            maintenanceResult.setEnclosureUrl(enclosureUrl);
        }
        maintenanceResult.setStatusName(statusMap.get((long) maintenanceResult.getStatus()));
        List<StorehousePositionsResult> details = this.getDetails(id);
        if (details.size() == 0) {
            this.updateStatus(maintenanceResult.getMaintenanceId());
        }
        if (ToolUtil.isNotEmpty(maintenanceResult.getNotice())) {
            List<Long> collect = Arrays.asList(maintenanceResult.getNotice().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<AnnouncementsResult> announcementsResults = BeanUtil.copyToList(announcementsService.listByIds(collect), AnnouncementsResult.class);
            maintenanceResult.setAnnouncementsResults(announcementsResults);
        }

        return maintenanceResult;
    }

    /**
     * 刷新更正需要保养的子表信息  以库存为准
     *
     * @param maintenance
     */
    @Override
    public void updateDetail(Maintenance maintenance) {
        List<StockDetails> stockDetails = this.needMaintenanceByRequirement(maintenance);
        List<StockDetails> detailTotalList = new ArrayList<>();

        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()) + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setNumber(a.getNumber() + b.getNumber());
                        setSkuId(a.getSkuId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                        setBrandId(a.getBrandId());
                        setStorehouseId(a.getStorehouseId());
                    }}).ifPresent(detailTotalList::add);
                }
        );

        List<MaintenanceDetail> maintenanceDetails = maintenanceDetailService.query().eq("display", 1).eq("maintenance_id", maintenance.getMaintenanceId()).list();
        for (StockDetails details : detailTotalList) {
            for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
                if (details.getBrandId().equals(maintenanceDetail.getBrandId()) && details.getSkuId().equals(maintenanceDetail.getSkuId()) && details.getStorehousePositionsId().equals(maintenanceDetail.getStorehousePositionsId())) {
                    if (maintenanceDetail.getDisplay() == 1 && maintenanceDetail.getNumber() != Math.toIntExact(details.getNumber())) {
                        if (maintenanceDetail.getNumber() < details.getNumber()) {
                            maintenanceDetail.setStatus(99);
                            maintenanceDetail.setDoneNumber(maintenanceDetail.getNumber());
                        } else {
                            maintenanceDetail.setDoneNumber((int) (maintenanceDetail.getNumber()-details.getNumber()));

                        }
                    }
                }
            }

        }

        for (StockDetails stockDetail : stockDetails) {
            if (maintenanceDetails.stream().noneMatch(i -> i.getSkuId().equals(stockDetail.getSkuId()) && i.getBrandId().equals(stockDetail.getBrandId()) && i.getStorehousePositionsId().equals(stockDetail.getStorehousePositionsId()))) {
                MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                ToolUtil.copyProperties(stockDetail, maintenanceDetail);
                maintenanceDetail.setMaintenanceId(maintenance.getMaintenanceId());
                maintenanceDetails.add(maintenanceDetail);
            }
        }
        for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
            if (stockDetails.stream().noneMatch(i -> i.getSkuId().equals(maintenanceDetail.getSkuId()) && i.getBrandId().equals(maintenanceDetail.getBrandId()) && i.getStorehousePositionsId().equals(maintenanceDetail.getStorehousePositionsId()))) {
                maintenanceDetail.setDoneNumber(maintenanceDetail.getNumber());
                maintenanceDetail.setStatus(99);
            }
        }
        maintenanceDetailService.saveOrUpdateBatch(maintenanceDetails);
        if (maintenanceDetails.stream().allMatch(i -> i.getStatus().equals(99))) {
            this.updateStatus(maintenance.getMaintenanceId());
        } else {
            maintenance.setStatus(98);
            this.updateById(maintenance);
        }

    }


    @Override
    public void updateStatus(Long id) {
        /**
         *    更新表单状态
         */
        Maintenance entity = new Maintenance();
        entity.setMaintenanceId(id);
        entity.setStatus(99);
        this.updateById(entity);

        /**
         * 如果有任务则更新任务单据动作状态
         */
        ActivitiProcessTask task = activitiProcessTaskService.query().eq("type", "MAINTENANCE").eq("form_id", id).one();
        if (ToolUtil.isNotEmpty(task)) {
            DocumentsAction action = actionService.query().eq("action", MaintenanceActionEnum.maintenanceing.name()).eq("display", 1).one();
            activitiProcessLogService.checkAction(task.getFormId(), task.getType(), action.getDocumentsActionId(), LoginContextHolder.getContext().getUserId());
        }

    }

}
