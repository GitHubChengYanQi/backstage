package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.entity.MaintenanceLog;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.mapper.MaintenanceLogMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.orCode.model.result.StoreHousePositionsRequest;
import cn.atsoft.dasheng.production.model.request.StockSkuTotal;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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


    @Override
    public void add(MaintenanceLogParam param){


        List<MaintenanceDetail> details = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getMaintenanceId())){
            details = maintenanceDetailService.query().eq("maintenances_id", param.getMaintenanceId()).eq("status", 0).list();

        }else {
            List<Maintenance> maintenances = maintenanceService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).ne("status", 99).eq("display", 1).list();
            List<Long> maintenancesIds = new ArrayList<>();
            for (Maintenance maintenance : maintenances) {
                maintenancesIds.add(maintenance.getMaintenanceId());
            }
            details = maintenancesIds.size() == 0 ? new ArrayList<>() : maintenanceDetailService.query().in("maintenances_id", maintenancesIds).eq("status", 0).list();

        }
        List<MaintenanceLog> logs = new ArrayList<>();
        for (MaintenanceLogParam maintenanceLogParam : param.getMaintenanceLogParams()) {
            Long maintenanceId = maintenanceLogParam.getMaintenanceId();
            Long brandId = maintenanceLogParam.getBrandId();
            Long storehousePositionsId = maintenanceLogParam.getStorehousePositionsId();
            Long skuId = maintenanceLogParam.getSkuId();
            Integer number = maintenanceLogParam.getNumber();
            List<Long> maintenanceIds = new ArrayList<>();
//            List<MaintenanceDetail> details = maintenanceDetailService.query().eq("brand_id ", maintenanceLogParam.getBrandId()).eq("storehouse_positions_id", maintenanceLogParam.getStorehousePositionsId()).eq("sku_id", maintenanceLogParam.getSkuId()).eq("display",1).eq("status",0).list();
            for (MaintenanceDetail detail : details) {
                //防止错误数据产生
                if (detail.getNumber()<=detail.getDoneNumber()){
                    detail.setStatus(99);
                }
                if (number>0){
                    if (detail.getSkuId().equals(skuId) && detail.getBrandId().equals(brandId) && detail.getStorehousePositionsId().equals(storehousePositionsId) && !detail.getStatus().equals(99)){

                        if (number - (detail.getNumber()-detail.getDoneNumber()) >=0) {
                            detail.setStatus(99);
                            detail.setDoneNumber(detail.getNumber());
                            MaintenanceLog entity = new MaintenanceLog();
                            ToolUtil.copyProperties(detail,entity);
                            entity.setMaintenanceId(maintenanceId);
                            logs.add(entity);
                            maintenanceIds.add(detail.getMaintenanceId());
                        }else {
                            detail.setDoneNumber(number + detail.getDoneNumber());
                            MaintenanceLog entity = new MaintenanceLog();
                            ToolUtil.copyProperties(detail,entity);
                            entity.setNumber(detail.getNumber() - (number*-1));
                            entity.setMaintenanceId(maintenanceId);
                            logs.add(entity);
                        }
                        number -=(detail.getNumber()-detail.getDoneNumber());

                    }
                }

            }
            /**
             * 判断任务是否完成
             */
            if (details.stream().allMatch(i->i.getStatus() == 99 ) || ToolUtil.isEmpty(details)){
                for (Long id : maintenanceIds) {
                    maintenanceService. updateStatus(id);
                }
            }else {
                Boolean statusFlag = true;
                for (Long id : maintenanceIds) {
                    for (MaintenanceDetail detail : details) {
                        if (id.equals(detail.getMaintenanceDetailId()) && !detail.getStatus().equals(99)){
                            statusFlag = false;
                        }
                    }
                    if (statusFlag){
                        maintenanceService. updateStatus(id);
                    }
                }
            }



            maintenanceDetailService.updateBatchById(details);
        }
        this.saveBatch(logs);
    }





    @Override
    public void delete(MaintenanceLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceLogParam param){
        MaintenanceLog oldEntity = getOldEntity(param);
        MaintenanceLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaintenanceLogResult findBySpec(MaintenanceLogParam param){
        return null;
    }

    @Override
    public List<MaintenanceLogResult> findListBySpec(MaintenanceLogParam param){
        return this.baseMapper.customList(param);
    }

    @Override
    public PageInfo<MaintenanceLogResult> findPageBySpec(MaintenanceLogParam param){
        Page<MaintenanceLogResult> pageContext = getPageContext();
        IPage<MaintenanceLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceLogParam param){
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
