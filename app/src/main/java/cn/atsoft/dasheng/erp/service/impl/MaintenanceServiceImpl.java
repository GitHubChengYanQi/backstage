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
    private MaterialService materialService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private PartsService partsService;

    @Autowired
    private StockDetailsService stockDetailsService;


    @Autowired
    private DocumentsActionService actionService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private DocumentStatusService statusService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private CodingRulesService codingRulesService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Autowired
    private MaintenanceCycleService maintenanceCycleService;

    @Autowired
    private SpuClassificationService spuClassificationService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnouncementsService announcementsService;

    @Autowired
    private StepsService stepsService;
    @Autowired
    private InkindService inkindService;

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
                messageProducer.remarksServiceDo(new RemarksEntity() {{
                    setOperationType(OperationType.ADD);
                    setRemarksParam(remarksParam);
                }});
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
        List<MaintenanceAndInventorySelectParam> maintenanceAndInventorySelectParams = JSON.parseArray(param.getSelectParams(), MaintenanceAndInventorySelectParam.class);
        for (MaintenanceAndInventorySelectParam maintenanceAndInventorySelectParam : maintenanceAndInventorySelectParams) {
            InventoryDetailParam inventoryDetailParam = new InventoryDetailParam();
            inventoryDetailParam.setBrandIds(maintenanceAndInventorySelectParam.getBrandIds());
            inventoryDetailParam.setBomIds(maintenanceAndInventorySelectParam.getPartsIds());
            inventoryDetailParam.setPositionIds(maintenanceAndInventorySelectParam.getStorehousePositionsIds());
            inventoryDetailParam.setClassIds(maintenanceAndInventorySelectParam.getSpuClassificationIds());
            inventoryDetailParam.setSkuIds(maintenanceAndInventorySelectParam.getSkuIds());
            inventoryDetailParam.setSpuIds(maintenanceAndInventorySelectParam.getSpuIds());
            List<InventoryStock> condition = inventoryService.condition(inventoryDetailParam);
            for (InventoryStock inventoryDetail : condition) {
                inkindIds.add(inventoryDetail.getInkindId());
                skuIds.add(inventoryDetail.getSkuId());
            }
        }
        inkindIds = inkindIds.stream().distinct().collect(Collectors.toList());


        List<MaintenanceCycle> maintenanceCycles = skuIds.size() == 0 ? new ArrayList<>() : maintenanceCycleService.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Inkind> inkinds = inkindIds.size() == 0 ? new ArrayList<>() : inkindService.listByIds(inkindIds);
        if (ToolUtil.isNotEmpty(param.getNearMaintenance())) {
            for (Inkind inkind : inkinds) {
                for (MaintenanceCycle maintenanceCycle : maintenanceCycles) {
                    if (inkind.getSkuId().equals(maintenanceCycle.getSkuId()) && ToolUtil.isNotEmpty(inkind.getLastMaintenanceTime()) && param.getNearMaintenance().getTime() < inkind.getLastMaintenanceTime().getTime()) {
                        inkindIds.remove(inkind.getInkindId());
                    }
                }
            }
        }

        //根据此条件去库存查询需要养护的实物
        return inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).list();

    }

    @Override
    public List<Maintenance> findTaskByTime() {
        Long userId = LoginContextHolder.getContext().getUserId();
        List<Maintenance> list = this.query().eq("display", 1).eq("user_id", userId).ne("status", 99).apply(" now() between start_time and end_time").list();
        list.addAll(this.query().eq("display", 1).eq("user_id", userId).eq("status", 98).list());
        List<Maintenance> totalList = new ArrayList<>();
        list.parallelStream().collect(Collectors.groupingBy(item -> item.getMaintenanceId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> a).ifPresent(totalList::add);
                }
        );


        return totalList;
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
        if (ToolUtil.isNotEmpty(maintenanceParam) && maintenanceParam.getStatus().equals(0)) {
            List<Maintenance> maintenances = this.findTaskByTime();
            for (Maintenance maintenance : maintenances) {
                if (maintenance.getMaintenanceId().equals(maintenanceParam.getMaintenanceId()) && updateDetail(maintenance) && maintenance.getStatus().equals(0)) {
                    maintenanceParam.setStatus(99);
                } else {
                    maintenanceParam.setStatus(98);
                }
            }

        } else { //如果是从合并任务查看界面进入  则筛选出 符合条件的任务  更改这些任务状态为开始  并更新物料列表
            List<Maintenance> maintenances = this.findTaskByTime();
            for (Maintenance entity : maintenances) {
                if (entity.getStatus().equals(0)) {
                    entity.setStatus(98);
                    if (updateDetail(entity)) {
                        entity.setStatus(99);
                    }
                }
            }
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
    public PageInfo<MaintenanceResult> findPageBySpec(MaintenanceParam param) {
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
        List<Long> materialIds = new ArrayList<>();

        List<Long> brandIds = new ArrayList<>();

        List<Long> positionsIds = new ArrayList<>();

        List<Long> bomIds = new ArrayList<>();

        List<Long> spuClassificationIds = new ArrayList<>();

        List<Long> ids = new ArrayList<>();

        for (MaintenanceResult maintenanceResult : param) {
            ids.add(maintenanceResult.getMaintenanceId());
            if (ToolUtil.isNotEmpty(maintenanceResult.getSelectParams())) {
                List<MaintenanceAndInventorySelectParam> selectParams = JSON.parseArray(maintenanceResult.getSelectParams(), MaintenanceAndInventorySelectParam.class);
                for (MaintenanceAndInventorySelectParam selectParam : selectParams) {
                    if (ToolUtil.isNotEmpty(selectParam.getBrandIds())) {
                        brandIds.addAll(selectParam.getBrandIds());
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getMaterialIds())) {
                        materialIds.addAll(selectParam.getMaterialIds());
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getStorehousePositionsIds())) {
                        positionsIds.addAll(selectParam.getStorehousePositionsIds());
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getPartsIds())) {
                        bomIds.addAll(selectParam.getPartsIds());
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getSpuClassificationIds())) {
                        spuClassificationIds.addAll(selectParam.getSpuClassificationIds());
                    }
                }
            }
        }
        List<SkuSimpleResult> skuSimpleResults = bomIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(bomIds);
        List<StorehousePositionsResult> positionsResults = positionsIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.getDetails(positionsIds);
        List<MaterialResult> materials = materialIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(materialService.listByIds(materialIds), MaterialResult.class);
        List<SpuClassificationResult> spuClassifications = spuClassificationIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(spuClassificationService.listByIds(spuClassificationIds), SpuClassificationResult.class);
        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);


        List<MaintenanceDetail> maintenanceDetails = ids.size() == 0 ? new ArrayList<>() : maintenanceDetailService.query().in("maintenance_id", ids).list();

        for (MaintenanceResult maintenanceResult : param) {
            if (ToolUtil.isNotEmpty(maintenanceResult.getSelectParams())) {
                List<SkuSimpleResult> skuSimpleResults1 = new ArrayList<>();
                List<StorehousePositionsResult> positionsResults1 = new ArrayList<>();
                List<MaterialResult> materials1 = new ArrayList<>();
                List<SpuClassificationResult> spuClassificationsice1 = new ArrayList<>();
                List<BrandResult> brandResults1 = new ArrayList<>();
                List<MaintenanceAndInventorySelectParam> selectParams = JSON.parseArray(maintenanceResult.getSelectParams(), MaintenanceAndInventorySelectParam.class);
                for (MaintenanceAndInventorySelectParam selectParam : selectParams) {
                    if (ToolUtil.isNotEmpty(selectParam.getBrandIds())) {
                        for (Long brandId : selectParam.getBrandIds()) {
                            for (BrandResult brandResult : brandResults) {
                                if (brandResult.getBrandId().equals(brandId)) {
                                    brandResults1.add(brandResult);
                                }
                            }
                        }
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getMaterialIds())) {
                        for (Long materialId : selectParam.getMaterialIds()) {
                            for (MaterialResult material : materials) {
                                if (material.getMaterialId().equals(materialId)) {
                                    materials1.add(material);
                                }
                            }
                        }

                    }
                    if (ToolUtil.isNotEmpty(selectParam.getStorehousePositionsIds())) {
                        for (Long positionId : selectParam.getStorehousePositionsIds()) {
                            for (StorehousePositionsResult positionsResult : positionsResults) {
                                if (positionsResult.getStorehousePositionsId().equals(positionId)) {
                                    positionsResults1.add(positionsResult);
                                }
                            }
                        }
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getPartsIds())) {
                        for (Long bomId : selectParam.getPartsIds()) {
                            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                                if (skuSimpleResult.getSkuId().equals(bomId)) {
                                    skuSimpleResults1.add(skuSimpleResult);
                                }
                            }
                        }
                    }
                    if (ToolUtil.isNotEmpty(selectParam.getSpuClassificationIds())) {
                        for (Long spuClassId : selectParam.getSpuClassificationIds()) {
                            for (SpuClassificationResult spuClassification : spuClassifications) {
                                if (spuClassification.getSpuClassificationId().equals(spuClassId)) {
                                    spuClassificationsice1.add(spuClassification);
                                }
                            }
                        }
                    }
                    selectParam.setBrandResults(brandResults1);
                    selectParam.setMaterialResults(materials1);
                    selectParam.setPartsResults(skuSimpleResults1);
                    selectParam.setSpuClassificationResults(spuClassificationsice1);
                    selectParam.setStorehousePositionsResults(positionsResults1);
                }
                maintenanceResult.setSelectParamResults(selectParams);
            }


            List<Long> storehousePositionsIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            Integer num = 0;
            Integer doneNumber = 0;
            for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
                if (maintenanceDetail.getMaintenanceId().equals(maintenanceResult.getMaintenanceId())) {
                    storehousePositionsIds.add(maintenanceDetail.getStorehousePositionsId());
                    skuIds.add(maintenanceDetail.getSkuId());
                    num += maintenanceDetail.getNumber();
                    doneNumber += maintenanceDetail.getDoneNumber();
                }
            }
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
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        for (UserResult userResultsById : userResultsByIds) {
            if (userResultsById.getUserId().equals(maintenanceResult.getUserId())) {
                userResultsById.setAvatar(stepsService.imgUrl(userResultsById.getUserId().toString()));
                maintenanceResult.setUserResult(userResultsById);
            }
            if (userResultsById.getUserId().equals(maintenanceResult.getCreateUser())) {
                userResultsById.setAvatar(stepsService.imgUrl(userResultsById.getUserId().toString()));
                maintenanceResult.setCreateUserResult(userResultsById);
            }
        }

        List<Long> materialIds = new ArrayList<>();

        List<Long> brandIds = new ArrayList<>();

        List<Long> positionsIds = new ArrayList<>();

        List<Long> bomIds = new ArrayList<>();

        List<Long> spuClassificationIds = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<Long> spuIds = new ArrayList<>();

        ids.add(maintenanceResult.getMaintenanceId());
        if (ToolUtil.isNotEmpty(maintenanceResult.getSelectParams())) {
            List<MaintenanceAndInventorySelectParam> selectParams = JSON.parseArray(maintenanceResult.getSelectParams(), MaintenanceAndInventorySelectParam.class);
            for (MaintenanceAndInventorySelectParam selectParam : selectParams) {
                if (ToolUtil.isNotEmpty(selectParam.getBrandIds())) {
                    brandIds.addAll(selectParam.getBrandIds());
                }
                if (ToolUtil.isNotEmpty(selectParam.getMaterialIds())) {
                    materialIds.addAll(selectParam.getMaterialIds());
                }
                if (ToolUtil.isNotEmpty(selectParam.getStorehousePositionsIds())) {
                    positionsIds.addAll(selectParam.getStorehousePositionsIds());
                }
                if (ToolUtil.isNotEmpty(selectParam.getSkuIds())){
                    bomIds.add(selectParam.getSkuId());
                }
                if (ToolUtil.isNotEmpty(selectParam.getPartsIds())) {
                    bomIds.addAll(selectParam.getPartsIds());
                }
                if (ToolUtil.isNotEmpty(selectParam.getSpuClassificationIds())) {
                    spuClassificationIds.addAll(selectParam.getSpuClassificationIds());
                }
                if (ToolUtil.isNotEmpty(selectParam.getSkuIds())) {
                    bomIds.addAll(selectParam.getSkuIds());
                }
                if (ToolUtil.isNotEmpty(selectParam.getSpuIds())) {
                    spuIds.addAll(selectParam.getSpuIds());
                }
            }
        }


        List<SkuSimpleResult> skuSimpleResults = bomIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(bomIds);
        List<StorehousePositionsResult> positionsResults = positionsIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.getDetails(positionsIds);
        List<MaterialResult> materials = materialIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(materialService.listByIds(materialIds), MaterialResult.class);
        List<SpuClassificationResult> spuClassifications = spuClassificationIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(spuClassificationService.listByIds(spuClassificationIds), SpuClassificationResult.class);
        List<BrandResult> brandResultsList = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        List<SpuResult> spuResults = spuIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(spuService.listByIds(spuIds), SpuResult.class);
        if (ToolUtil.isNotEmpty(maintenanceResult.getSelectParams())) {
            List<MaintenanceAndInventorySelectParam> selectParams = JSON.parseArray(maintenanceResult.getSelectParams(), MaintenanceAndInventorySelectParam.class);
            for (MaintenanceAndInventorySelectParam selectParam : selectParams) {
                List<SkuSimpleResult> bomResults = new ArrayList<>();
                List<StorehousePositionsResult> positionsResults1 = new ArrayList<>();
                List<MaterialResult> materialsResults = new ArrayList<>();
                List<SpuClassificationResult> spuClassificationResults = new ArrayList<>();
                List<BrandResult> brandResults = new ArrayList<>();
                List<SkuSimpleResult> skuResults = new ArrayList<>();
                List<SpuResult> spuResults1 = new ArrayList<>();
                if (ToolUtil.isNotEmpty(selectParam.getBrandIds())) {
                    for (Long brandId : selectParam.getBrandIds()) {
                        for (BrandResult brandResult : brandResultsList) {
                            if (brandResult.getBrandId().equals(brandId)) {
                                brandResults.add(brandResult);
                            }
                        }
                    }
                }
                if (ToolUtil.isNotEmpty(selectParam.getSkuIds())){
                    for (SkuSimpleResult skuResult : skuResults) {
                        if (skuResult.getSkuId().equals(selectParam.getSkuId())){
                            selectParam.setSkuResult(skuResult);
                            break;
                        }
                    }
                }
                if (ToolUtil.isNotEmpty(selectParam.getMaterialIds())) {
                    for (Long materialId : selectParam.getMaterialIds()) {
                        for (MaterialResult material : materials) {
                            if (material.getMaterialId().equals(materialId)) {
                                materialsResults.add(material);
                            }
                        }
                    }

                }
                if (ToolUtil.isNotEmpty(selectParam.getSpuIds())) {
                    for (Long spuId : selectParam.getSpuIds()) {
                        for (SpuResult spu : spuResults) {
                            if (spu.getSpuId().equals(spuId)) {
                                spuResults1.add(spu);
                            }
                        }
                    }

                }
                if (ToolUtil.isNotEmpty(selectParam.getStorehousePositionsIds())) {
                    for (Long positionId : selectParam.getStorehousePositionsIds()) {
                        for (StorehousePositionsResult positionsResult : positionsResults) {
                            if (positionsResult.getStorehousePositionsId().equals(positionId)) {
                                positionsResults1.add(positionsResult);
                            }
                        }
                    }
                }
                if (ToolUtil.isNotEmpty(selectParam.getPartsIds())) {
                    for (Long bomId : selectParam.getPartsIds()) {
                        for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                            if (skuSimpleResult.getSkuId().equals(bomId)) {
                                bomResults.add(skuSimpleResult);
                            }
                        }
                    }
                }
                if (ToolUtil.isNotEmpty(selectParam.getSkuIds())) {
                    for (Long skuId : selectParam.getSkuIds()) {
                        for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                            if (skuSimpleResult.getSkuId().equals(skuId)) {
                                skuResults.add(skuSimpleResult);
                            }
                        }
                    }
                }
                if (ToolUtil.isNotEmpty(selectParam.getSpuClassificationIds())) {
                    for (Long spuClassId : selectParam.getSpuClassificationIds()) {
                        for (SpuClassificationResult spuClassification : spuClassifications) {
                            if (spuClassification.getSpuClassificationId().equals(spuClassId)) {
                                spuClassificationResults.add(spuClassification);
                            }
                        }
                    }
                }
                selectParam.setBrandResults(brandResults);
                selectParam.setMaterialResults(materialsResults);
                selectParam.setPartsResults(bomResults);
                selectParam.setSpuClassificationResults(spuClassificationResults);
                selectParam.setStorehousePositionsResults(positionsResults1);
                selectParam.setSpuResults(spuResults1);
                selectParam.setSkuResults(skuResults);
            }
            maintenanceResult.setSelectParamResults(selectParams);
            maintenanceResult.setStatusName(statusMap.get((long) maintenanceResult.getStatus()));
            List<StorehousePositionsResult> details = this.getDetails(id);
            if (details.size() == 0) {
                this.updateStatus(maintenanceResult.getMaintenanceId());
            }
            List<MaintenanceDetail> maintenanceDetails = maintenanceDetailService.query().eq("display", 1).eq("maintenance_id", id).list();
            List<Long> storehousePositionsIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            Integer num = 0;
            Integer doneNumber = 0;
            for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
                if (maintenanceDetail.getMaintenanceId().equals(maintenanceResult.getMaintenanceId())) {
                    storehousePositionsIds.add(maintenanceDetail.getStorehousePositionsId());
                    skuIds.add(maintenanceDetail.getSkuId());
                    num += maintenanceDetail.getNumber();
                    doneNumber += maintenanceDetail.getDoneNumber();
                }
            }
            skuIds = skuIds.stream().distinct().collect(Collectors.toList());
            storehousePositionsIds = storehousePositionsIds.stream().distinct().collect(Collectors.toList());
            maintenanceResult.setNumberCount(num);
            maintenanceResult.setSkuCount(skuIds.size());
            maintenanceResult.setPositionCount(storehousePositionsIds.size());
            maintenanceResult.setDoneNumberCount(doneNumber);
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
    public Boolean updateDetail(Maintenance maintenance) {
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
                            maintenanceDetail.setStatus(0);
                        }
                        maintenanceDetail.setNumber(Math.toIntExact(details.getNumber()));
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
                maintenanceDetail.setDisplay(0);
            }
        }
        maintenanceDetailService.saveOrUpdateBatch(maintenanceDetails);
        if (maintenanceDetails.stream().allMatch(i -> i.getDisplay().equals(0))) {
            maintenance.setStatus(99);
            this.updateById(maintenance);
            return true;
        } else {
            maintenance.setStatus(98);
            this.updateById(maintenance);
            return false;
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
