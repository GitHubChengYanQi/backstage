package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.MaintenanceLogMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 养护记录 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@Service
public class MaintenanceLogServiceImpl extends ServiceImpl<MaintenanceLogMapper, MaintenanceLog> implements MaintenanceLogService {
    @Autowired
    private MaintenanceDetailService maintenanceDetailService;
    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MaintenanceLogService maintenanceLogService;

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
    private InkindService inkindService;
    @Autowired
    private MaintenanceCycleService maintenanceCycleService;
    @Autowired
    private UserService userService;


    @Override
    public void add(MaintenanceLogParam param) {
        if (ToolUtil.isEmpty(param.getMaintenanceLogParams())) {
            throw new ServiceException(500, "请选择养护完成的物料与数量");
        }
        List<StockDetails> stockDetails = new ArrayList<>();
        List<MaintenanceDetail> maintenanceDetails = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getMaintenanceId())) {
            Maintenance maintenance = maintenanceService.getById(param.getMaintenanceId());
            maintenanceDetails = maintenanceDetailService.query().eq("maintenance_id", param.getMaintenanceId()).eq("status", 0).eq("display", 1).list();
            stockDetails = maintenanceService.needMaintenanceByRequirement(maintenance);
            maintenanceDetailService.updateBatchById(maintenanceDetails);
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
        List<MaintenanceLog> logs = new ArrayList<>();
        processingData(param, stockDetails, maintenanceDetails, logs);
        maintenanceDetailService.updateBatchById(maintenanceDetails);
        this.saveBatch(logs);
        if (maintenanceDetails.stream().allMatch(i -> i.getStatus() == 99) || ToolUtil.isEmpty(maintenanceDetails)) {
            maintenanceService.updateStatus(param.getMaintenanceId());
        }


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
//        List<MaintenanceLog> logs = new ArrayList<>();
//        for (MaintenanceLogParam maintenanceLogParam : param.getMaintenanceLogParams()) {
//            Long maintenanceId = maintenanceLogParam.getMaintenanceId();
//            Long brandId = maintenanceLogParam.getBrandId();
//            Long storehousePositionsId = maintenanceLogParam.getStorehousePositionsId();
//            Long skuId = maintenanceLogParam.getSkuId();
//            Integer number = maintenanceLogParam.getNumber();
//            List<Long> maintenanceIds = new ArrayList<>();
////            List<MaintenanceDetail> details = maintenanceDetailService.query().eq("brand_id ", maintenanceLogParam.getBrandId()).eq("storehouse_positions_id", maintenanceLogParam.getStorehousePositionsId()).eq("sku_id", maintenanceLogParam.getSkuId()).eq("display",1).eq("status",0).list();
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
//                            MaintenanceLog entity = new MaintenanceLog();
//                            ToolUtil.copyProperties(detail,entity);
//                            entity.setMaintenanceId(maintenanceId);
//                            logs.add(entity);
//                            maintenanceIds.add(detail.getMaintenanceId());
//                        }else {
//                            detail.setDoneNumber(number + detail.getDoneNumber());
//                            MaintenanceLog entity = new MaintenanceLog();
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

    public void processingData(MaintenanceLogParam param, List<StockDetails> stockDetails, List<MaintenanceDetail> maintenanceDetails, List<MaintenanceLog> logs) {
        for (MaintenanceLogParam maintenanceLogParam : param.getMaintenanceLogParams()) {
            List<StockDetails> need = new ArrayList<>();
            for (StockDetails stockDetail : stockDetails) {
                if (maintenanceLogParam.getSkuId().equals(stockDetail.getSkuId()) && maintenanceLogParam.getBrandId().equals(stockDetail.getBrandId()) && maintenanceLogParam.getStorehousePositionsId().equals(stockDetail.getStorehousePositionsId())) {
                    need.add(stockDetail);
                }
            }
            List<Long> inkindIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            int num = maintenanceLogParam.getNumber();
            if (num > 0) {
                for (StockDetails details : need) {
                    inkindIds.add(details.getInkindId());
                    num -= details.getNumber();
                    MaintenanceLog log = new MaintenanceLog();
                    log.setNumber(Math.toIntExact(details.getNumber()));
                    log.setBrandId(details.getBrandId());
                    log.setSkuId(details.getSkuId());
                    log.setInkindId(details.getInkindId());
                    log.setEnclosure(maintenanceLogParam.getEnclosure());
                    logs.add(log);
                }
            }
            List<Inkind> inkinds = inkindIds.size() == 0 ? new ArrayList<>() : inkindService.listByIds(inkindIds);
            for (Inkind inkind : inkinds) {
                skuIds.add(inkind.getSkuId());
            }
            List<MaintenanceCycle> cycles = skuIds.size() == 0 ? new ArrayList<>() : maintenanceCycleService.query().in("sku_id", skuIds).eq("display", 1).list();
            //实物添加下次养护时间
            for (Inkind inkind : inkinds) {
                for (MaintenanceCycle cycle : cycles) {
                    if (inkind.getSkuId().equals(cycle.getSkuId())) {
                        Calendar calendar = Calendar.getInstance();
                        if (ToolUtil.isNotEmpty(inkind.getLastMaintenanceTime())) {
                            calendar.setTime(inkind.getLastMaintenanceTime());
                        } else {
                            calendar.setTime(new Date());
                        }
                        calendar.add(Calendar.DATE, cycle.getMaintenancePeriod());
                        inkind.setLastMaintenanceTime(calendar.getTime());
                    }
                }
            }
            for (MaintenanceDetail detail : maintenanceDetails) {
                if (detail.getSkuId().equals(maintenanceLogParam.getSkuId()) && detail.getStorehousePositionsId().equals(maintenanceLogParam.getStorehousePositionsId()) && detail.getBrandId().equals(maintenanceLogParam.getBrandId())) {
                    detail.setDoneNumber(detail.getDoneNumber() + maintenanceLogParam.getNumber());
                    if (detail.getDoneNumber() >= detail.getNumber()) {
                        detail.setStatus(99);
                    }
                }
            }
        }
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
        return this.baseMapper.customList(param);
    }

    @Override
    public List<MaintenanceLogResult> lastLogByInkindIds(List<Long> inkindIds) {
        List<MaintenanceLog> maintenanceLogs = inkindIds.size() == 0 ? new ArrayList<>() : this.query().in("inkind_id", inkindIds).eq("display", 1).groupBy("inkind_id").orderByDesc("create_time").list();
        List<MaintenanceLogResult> maintenanceLogResults = BeanUtil.copyToList(maintenanceLogs, MaintenanceLogResult.class);
        this.format(maintenanceLogResults);
        return maintenanceLogResults;
    }

    private void format(List<MaintenanceLogResult> data){
        List<Long> userIds = new ArrayList<>();
        for (MaintenanceLogResult datum : data) {
            userIds.add(datum.getCreateUser());
        }
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        for (MaintenanceLogResult datum : data) {
            for (UserResult userResult : userResultsByIds) {
                if (datum.getCreateUser().equals(userResult.getUserId())){
                    datum.setUserResult(userResult);
                }
            }
        }
    }
    @Override
    public PageInfo<MaintenanceLogResult> findPageBySpec(MaintenanceLogParam param) {
        Page<MaintenanceLogResult> pageContext = getPageContext();
        IPage<MaintenanceLogResult> page = this.baseMapper.customPageList(pageContext, param);
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

}
