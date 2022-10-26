package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.entity.MaintenanceLog;
import cn.atsoft.dasheng.erp.entity.MaintenanceLogDetail;
import cn.atsoft.dasheng.erp.mapper.MaintenanceLogMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 养护记录 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@Service
public class MaintenanceLogServiceImpl extends ServiceImpl<MaintenanceLogMapper, MaintenanceLog> implements MaintenanceLogService {

    @Autowired
    private MaintenanceDetailService maintenanceDetailService;
    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private cn.atsoft.dasheng.erp.service.MaintenanceLogDetailService MaintenanceLogDetailService;

    @Autowired
    private InkindService inkindService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;


    @Autowired
    private MaintenanceCycleService maintenanceCycleService;

    @Autowired
    private UserService userService;

    @Autowired
    private StepsService stepsService;

    @Autowired
    private MaintenanceLogDetailService maintenanceLogDetailService;

    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private AnnouncementsService announcementsService;

    @Autowired
    private SkuHandleRecordService skuHandleRecordService;

    @Autowired
    private ActivitiProcessTaskService taskService;

    @Override
    public void add(MaintenanceLogParam param) {

        String code = RandomUtil.randomString(5);
        param.setCoding(code);


        if (ToolUtil.isEmpty(param.getMaintenanceLogDetailParams())) {
            throw new ServiceException(500, "请选择养护完成的物料与数量");
        }
        List<StockDetails> stockDetails = new ArrayList<>();
        List<MaintenanceDetail> maintenanceDetails = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getMaintenanceId())) {
            Maintenance maintenance = maintenanceService.getById(param.getMaintenanceId());
            maintenanceDetails = maintenanceDetailService.query().eq("maintenance_id", param.getMaintenanceId()).eq("status", 0).eq("display", 1).list();
            stockDetails = maintenanceService.needMaintenanceByRequirement(maintenance);
//            maintenanceDetailService.updateBatchById(maintenanceDetails);
            /**
             * 判断任务是否完成
             */
            if (maintenanceDetails.stream().allMatch(i -> i.getStatus() == 99) || ToolUtil.isEmpty(maintenanceDetails)) {
                maintenanceService.updateStatus(param.getMaintenanceId());
            }

        } else {
            MaintenanceAndDetail taskAndDetailByTime = maintenanceService.findTaskAndDetailByTime();
            List<Maintenance> maintenances = taskAndDetailByTime.getMaintenances();
            maintenanceDetails.addAll(taskAndDetailByTime.getMaintenanceDetails());
            for (Maintenance maintenance : maintenances) {
                stockDetails.addAll(maintenanceService.needMaintenanceByRequirement(maintenance));
            }
            /**
             * 判断任务是否完成
             */
            if (maintenanceDetails.stream().allMatch(i -> i.getStatus() == 99) || ToolUtil.isEmpty(maintenanceDetails)) {
                maintenanceService.updateStatus(param.getMaintenanceId());
            }
        }

        /**
         * 处理数据 并添加保养记录
         */
        List<MaintenanceLogDetail> logs = new ArrayList<>();
        maintenanceLogDetailService.processingData(param, stockDetails, maintenanceDetails, logs);
        maintenanceDetailService.updateBatchById(maintenanceDetails);
        MaintenanceLog entity = getEntity(param);
        this.save(entity);
        String origin = getOrigin.newThemeAndOrigin("maintenanceLog", entity.getMaintenanceLogId(), "maintenance", entity.getMaintenanceId());
        entity.setOrigin(origin);
        this.updateById(entity);

        Map<String, MaintenanceLogDetail> map = new HashMap<>();
        for (MaintenanceLogDetail maintenanceLogDetail : logs) {
            maintenanceLogDetail.setMaintenanceLogId(entity.getMaintenanceLogId());
            map.put(maintenanceLogDetail.getSkuId() + "," + maintenanceLogDetail.getBrandId(), maintenanceLogDetail);
        }
        ActivitiProcessTask task = ToolUtil.isEmpty(param.getMaintenanceId()) ? null : taskService.query().eq("form_id", param.getMaintenanceId()).one();
        /**
         * 添加物料操作记录
         */
        Long positionsId = param.getMaintenanceLogDetailParams().get(0).getStorehousePositionsId();
        for (String s : map.keySet()) {
            MaintenanceLogDetail logDetail = map.get(s);
            skuHandleRecordService.addRecord(logDetail.getSkuId(), logDetail.getBrandId(), positionsId, null, "MAINTENANCE", task,Long.valueOf(logDetail.getNumber()),null,null);
        }


        maintenanceLogDetailService.saveBatch(logs);
        if (maintenanceDetails.stream().allMatch(i -> i.getStatus() == 99) || ToolUtil.isEmpty(maintenanceDetails)) {
            maintenanceService.updateStatus(param.getMaintenanceId());
        }
        shopCartService.addDynamic(param.getMaintenanceId(), maintenanceDetails.get(0).getSkuId(), "对 " + skuService.skuMessage(maintenanceDetails.get(0).getSkuId()) + " 进行了养护");

//        List<MaintenanceDetail> details = new ArrayList<>();
//        if (ToolUtil.isNotEmpty(param.getMaintenanceId())){
//            details = maintenanceDetailService.query().eq("maintenances_id", param.getMaintenanceId()).eq("status", 0).list();
//
//        }else {
//            List<Maintenance> maintenances = maintenanceService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).ne("status", 99).eq("display", 1).list();
//            List<Long> maintenancesIds = new ArrayList<>();
//            for (Maintenance maintenance : maintenances) {
//                maintenancesIds.add(maintenance.getMaintenanceId());
//            }
//            details = maintenancesIds.size() == 0 ? new ArrayList<>() : maintenanceDetailService.query().in("maintenances_id", maintenancesIds).eq("status", 0).list();
//
//        }
//        List<MaintenanceLogDetail> logs = new ArrayList<>();
//        for (MaintenanceLogDetailParam MaintenanceLogDetailParam : param.getMaintenanceLogDetailParams()) {
//            Long maintenanceId = MaintenanceLogDetailParam.getMaintenanceId();
//            Long brandId = MaintenanceLogDetailParam.getBrandId();
//            Long storehousePositionsId = MaintenanceLogDetailParam.getStorehousePositionsId();
//            Long skuId = MaintenanceLogDetailParam.getSkuId();
//            Integer number = MaintenanceLogDetailParam.getNumber();
//            List<Long> maintenanceIds = new ArrayList<>();
////            List<MaintenanceDetail> details = maintenanceDetailService.query().eq("brand_id ", MaintenanceLogDetailParam.getBrandId()).eq("storehouse_positions_id", MaintenanceLogDetailParam.getStorehousePositionsId()).eq("sku_id", MaintenanceLogDetailParam.getSkuId()).eq("display",1).eq("status",0).list();
//            for (MaintenanceDetail detail : details) {
//                //防止错误数据产生
//                if (detail.getNumber()<=detail.getDoneNumber()){
//                    detail.setStatus(99);
//                }
//                if (number>0){
//                    if (detail.getSkuId().equals(skuId) && detail.getBrandId().equals(brandId) && detail.getStorehousePositionsId().equals(storehousePositionsId) && !detail.getStatus().equals(99)){
//
//                        if (number - (detail.getNumber()-detail.getDoneNumber()) >=0) {
//                            detail.setStatus(99);
//                            detail.setDoneNumber(detail.getNumber());
//                            MaintenanceLogDetail entity = new MaintenanceLogDetail();
//                            ToolUtil.copyProperties(detail,entity);
//                            entity.setMaintenanceId(maintenanceId);
//                            logs.add(entity);
//                            maintenanceIds.add(detail.getMaintenanceId());
//                        }else {
//                            detail.setDoneNumber(number + detail.getDoneNumber());
//                            MaintenanceLogDetail entity = new MaintenanceLogDetail();
//                            ToolUtil.copyProperties(detail,entity);
//                            entity.setNumber(detail.getNumber() - (number*-1));
//                            entity.setMaintenanceId(maintenanceId);
//                            logs.add(entity);
//                        }
//                        number -=(detail.getNumber()-detail.getDoneNumber());
//                    }
//                }
//            }


    }

    @Override
    public MaintenanceLogResult detail(Long id) {
        MaintenanceLogResult result = BeanUtil.copyProperties(this.getById(id), MaintenanceLogResult.class);
        List<MaintenanceLogDetailResult> logDetails = BeanUtil.copyToList(maintenanceLogDetailService.query().eq("maintenance_log_id", id).list(), MaintenanceLogDetailResult.class);
        maintenanceLogDetailService.format(logDetails);
        if (ToolUtil.isNotEmpty(result.getOrigin())) {
            result.setThemeAndOrigin(getOrigin.getOrigin(JSON.parseObject(result.getOrigin(), ThemeAndOrigin.class)));
        }

        if (ToolUtil.isNotEmpty(result.getNoticeIds())) {
            List<Long> collect = Arrays.asList(result.getNoticeIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<AnnouncementsResult> announcementsResults = BeanUtil.copyToList(announcementsService.listByIds(collect), AnnouncementsResult.class);
            result.setAnnouncementsResults(announcementsResults);
        }

        result.setCreateUserResult(BeanUtil.copyProperties(userService.getById(result.getCreateUser()), UserResult.class));
        result.setDetailResults(logDetails);
        return result;
    }

    @Override
    public void delete(MaintenanceLogParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceLogParam param) {
        MaintenanceLog oldEntity = getOldEntity(param);
        MaintenanceLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaintenanceLogResult findBySpec(MaintenanceLogParam param) {
        return null;
    }

    @Override
    public List<MaintenanceLogResult> findListBySpec(MaintenanceLogParam param) {
        return null;
    }

    @Override
    public PageInfo<MaintenanceLogResult> findPageBySpec(MaintenanceLogParam param) {
        Page<MaintenanceLogResult> pageContext = getPageContext();
        IPage<MaintenanceLogResult> page = this.baseMapper.leftJoinList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceLogParam param) {
        return param.getMaintenanceLogId();
    }

    private Page<MaintenanceLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private MaintenanceLog getOldEntity(MaintenanceLogParam param) {
        return this.getById(getKey(param));
    }

    private MaintenanceLog getEntity(MaintenanceLogParam param) {
        MaintenanceLog entity = new MaintenanceLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


    private void format(List<MaintenanceLogResult> data) {
        List<Long> inkindIds = new ArrayList<>();
        for (MaintenanceLogResult datum : data) {
            inkindIds.add(datum.getInkindId());
        }
        List<UserResult> userResultsByIds =data.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(data.stream().map(MaintenanceLogResult::getCreateUser).collect(Collectors.toList()));
        List<InkindResult> inKinds = inkindService.getInKinds(inkindIds);
        List<MaintenanceLogDetail> details =data.size() == 0 ? new ArrayList<>() : maintenanceLogDetailService.lambdaQuery().in(MaintenanceLogDetail::getMaintenanceLogId, data.stream().map(MaintenanceLogResult::getMaintenanceLogId).collect(Collectors.toList())).list();
        List<MaintenanceLogDetailResult> maintenanceLogDetailResults = BeanUtil.copyToList(details, MaintenanceLogDetailResult.class);
        maintenanceLogDetailService.format(maintenanceLogDetailResults);
        for (MaintenanceLogResult datum : data) {
            for (InkindResult inKind : inKinds) {
                if (ToolUtil.isNotEmpty(datum.getInkindId()) && datum.getInkindId().equals(inKind.getInkindId())) {
                    datum.setInkindResult(inKind);
                    break;
                }
            }
            List<MaintenanceLogDetailResult> detailResults = new ArrayList<>();
            for (MaintenanceLogDetailResult detail : maintenanceLogDetailResults) {
                if (detail.getMaintenanceLogId().equals(datum.getMaintenanceLogId())){
                    detailResults.add(detail);
                }
            }
            for (UserResult userResultsById : userResultsByIds) {
                if (datum.getCreateUser().equals(userResultsById.getUserId())){
                    datum.setCreateUserResult(userResultsById);
                    break;
                }
            }
            datum.setDetailResults(detailResults);


        }
    }
}
