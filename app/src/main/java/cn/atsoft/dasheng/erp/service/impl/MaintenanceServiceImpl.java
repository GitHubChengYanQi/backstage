package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.MaintenanceActionEnum;
import cn.atsoft.dasheng.action.Enum.OutStockActionEnum;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.MaintenanceMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
    private SpuService spuService;

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

    @Override
    @Transactional
    public Maintenance add(MaintenanceParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "16").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置出库库单据自动生成编码规则");
            }
        }

        // 根据2个查询维度  写出两个查询方法  (条件查询维度 对应 查询出实物),(物料维度直接通过sku查询出库存实物  进行养护) （）实物 或 sku
        Maintenance entity = getEntity(param);
        entity.setMaintenanceName(LoginContextHolder.getContext().getUser().getName() + "创建的养护任务");
        this.save(entity);

        List<StockDetails> stockDetails = this.needMaintenanceByRequirement(entity);
        List<MaintenanceDetail> details = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            MaintenanceDetail detail = new MaintenanceDetail();
            ToolUtil.copyProperties(stockDetail, detail);
            detail.setMaintenanceId(entity.getMaintenanceId());
            details.add(detail);
        }
        maintenanceDetailService.saveBatch(details);
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
        } else {
            throw new ServiceException(500, "请创建质检流程！");
        }
        return entity;
    }

    @Override
    public List<StockDetails> needMaintenanceByRequirement(Maintenance param) {
        List<Long> skuIds = new ArrayList<>();
        List<Sku> skuList = new ArrayList<>();
        /**
         * 从材质条件筛选出sku
         * 从sku可获取sku的养护周期
         * 去log表查询 排除不需要养护的实物
         */
        if (ToolUtil.isNotEmpty(param.getMaterialId())) {
            List<Long> spuIds = new ArrayList<>();
            List<Spu> spuList = spuService.query().eq("meterial_id", param.getMaterialId()).eq("display", 1).list();
            for (Spu spu : spuList) {
                spuIds.add(spu.getSpuId());
            }
            skuList = spuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("spu_id", spuIds).eq("display", 1).list();
            for (Sku sku : skuList) {
                skuIds.add(sku.getSkuId());
            }
        }
        //查询出不需要养护的实物
        List<Long> notNeedMaintenanceInkindIds = new ArrayList<>();
        List<MaintenanceLogResult> logResults = maintenanceLogService.findListBySpec(new MaintenanceLogParam() {{
            if (ToolUtil.isNotEmpty(param.getBrandId())) {
                setBrandId(param.getBrandId());
            }
            setSkuIds(skuIds);
        }});
        for (MaintenanceLogResult logResult : logResults) {
            for (Sku sku : skuList) {
                if (logResult.getSkuId().equals(sku.getSkuId()) && ToolUtil.isNotEmpty(sku.getMaintenancePeriod())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(logResult.getCreateTime());
                    calendar.add(Calendar.DATE, (sku.getMaintenancePeriod() - param.getNearMaintenance()));
                    String maintenance = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
                    String now = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
                    if (!maintenance.equals(now)) {
                        notNeedMaintenanceInkindIds.add(logResult.getInkindId());
                    }
                }
            }
        }
        //根据此条件去库存查询需要养护的实物
        return stockDetailsService.maintenanceQuerry(new StockDetailsParam() {{
            setSkuIds(skuIds);
            setNotNeedMaintenanceInkindIds(notNeedMaintenanceInkindIds);
            if (ToolUtil.isNotEmpty(param.getBrandId())) {
                setBrandId(param.getBrandId());
            }
            if (ToolUtil.isNotEmpty(param.getStorehousePositionsId())) {
                setStorehousePositionsId(param.getStorehousePositionsId());
            }
        }});

    }

    @Override
    public List<Maintenance> findTaskByTime() {
        return this.query().eq("display", 1).ne("status", 99).apply(" now() between start_time and end_time").list();
    }

    //任务开始
    public void startMaintenance(Maintenance maintenance) {
        if (ToolUtil.isNotEmpty(maintenance) && maintenance.getStatus().equals(0)) {
            if (maintenance.getStatus().equals(0)) {
                List<Maintenance> maintenances = this.findTaskByTime();
                if (maintenances.stream().anyMatch(i -> i.getMaintenanceId().equals(maintenance.getMaintenanceId()))) {
                    maintenance.setStatus(98);
                    updateDetail(maintenance);
                }
            }

        } else {
            List<Maintenance> maintenances = this.findTaskByTime();
            for (Maintenance entity : maintenances) {
                if (entity.getStatus().equals(0)) {
                    entity.setStatus(98);

                    updateDetail(entity);
                }
            }
            this.updateBatchById(maintenances);
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
        List<Long> ids = new ArrayList<>();
        for (MaintenanceResult maintenanceResult : param) {
            ids.add(maintenanceResult.getMaintenanceId());
        }
        List<MaintenanceDetail> maintenanceDetails = ids.size() == 0 ? new ArrayList<>() : maintenanceDetailService.query().in("maintenance_id", ids).list();

        for (MaintenanceResult maintenanceResult : param) {
            List<Long> storehousePositionsIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            Integer num = 0;
            for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
                if (maintenanceDetail.getMaintenanceId().equals(maintenanceResult.getMaintenanceId())) {
                    storehousePositionsIds.add(maintenanceDetail.getStorehousePositionsId());
                    skuIds.add(maintenanceDetail.getSkuId());
                    num += maintenanceDetail.getNumber();
                }
            }
            skuIds = skuIds.stream().distinct().collect(Collectors.toList());
            storehousePositionsIds = storehousePositionsIds.stream().distinct().collect(Collectors.toList());
            maintenanceResult.setNumberCount(num);
            maintenanceResult.setSkuCount(skuIds.size());
            maintenanceResult.setPositionCount(storehousePositionsIds.size());
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
            brandResults.add(new BrandResult(){{
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
        startMaintenance(maintenance);
        MaintenanceResult maintenanceResult = new MaintenanceResult();
        ToolUtil.copyProperties(maintenance, maintenanceResult);
        List<StorehousePositionsResult> details = this.getDetails(id);
        maintenanceResult.setDetailResultsByPositions(details);
        return maintenanceResult;
    }

    /**
     * 刷新更正需要保养的子表信息  以库存为准
     *
     * @param maintenance
     */
    public void updateDetail(Maintenance maintenance) {
        List<StockDetails> stockDetails = this.needMaintenanceByRequirement(maintenance);
        List<MaintenanceLog> maintenanceLogs = maintenanceLogService.query().eq("maintenance_id", maintenance.getMaintenanceId()).list();
        List<MaintenanceDetail> maintenanceDetails = maintenanceDetailService.query().eq("status", 0).eq("display", 1).eq("maintenance_id", maintenance.getMaintenanceId()).list();
        for (MaintenanceDetail maintenanceDetail : maintenanceDetails) {
            for (StockDetails stockDetail : stockDetails) {
                //对批量实物 库存更正
                if (stockDetail.getInkindId().equals(maintenanceDetail.getInkindId()) && Math.toIntExact(stockDetail.getNumber()) != maintenanceDetail.getNumber()) {
                    maintenanceDetail.setNumber(Math.toIntExact(stockDetail.getNumber()));
                }
            }
            //如果物料在期间被出库  则 删除掉这条数据
            if (stockDetails.stream().noneMatch(i -> i.getInkindId().equals(maintenanceDetail.getInkindId())) && maintenanceLogs.stream().noneMatch(i -> i.getInkindId().equals(maintenanceDetail.getInkindId()))) {
                maintenanceDetail.setDisplay(0);
            }
        }
        for (StockDetails stockDetail : stockDetails) {
            if (maintenanceDetails.stream().noneMatch(i -> i.getInkindId().equals(stockDetail.getInkindId()))) {
                MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                ToolUtil.copyProperties(stockDetail, maintenanceDetail);
                maintenanceDetail.setMaintenanceId(maintenance.getMaintenanceId());
                maintenanceDetails.add(maintenanceDetail);
            }
        }
        maintenanceDetailService.saveOrUpdateBatch(maintenanceDetails);
    }


    @Override
    public void updateStatus(Long id) {
        /**
         *    更新表单状态
         */
        Maintenance entity = new Maintenance();
        entity.setMaintenanceId(id);

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
