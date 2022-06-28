package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.mapper.MaintenanceMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogResult;
import cn.atsoft.dasheng.erp.model.result.MaintenanceResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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


    @Override
    public void add(MaintenanceParam param){
        // 根据2个查询维度  写出两个查询方法  (条件查询维度 对应 查询出实物),(物料维度直接通过sku查询出库存实物  进行养护) （）实物 或 sku
        Maintenance entity = getEntity(param);
        this.save(entity);
//        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
//            List<MaintenanceDetail> details = new ArrayList<>();
//            for (MaintenanceDetailParam detailParam : param.getDetailParams()) {
//                MaintenanceDetail detail = new MaintenanceDetail();
//                ToolUtil.copyProperties(detailParam,detail);
//                detail.setMaintenanceId(entity.getMaintenanceId());
//                details.add(detail);
//            }
//            maintenanceDetailService.saveBatch(details);
//        }
        List<StockDetails> stockDetails = this.needMaintenanceByRequirement(entity);
        List<MaintenanceDetail> details = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            MaintenanceDetail detail = new MaintenanceDetail();
            ToolUtil.copyProperties(stockDetail,detail);
            detail.setMaintenanceId(entity.getMaintenanceId());
            details.add(detail);
        }
        maintenanceDetailService.saveBatch(details);
    }
    @Override
    public List<StockDetails> needMaintenanceByRequirement(Maintenance param){
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
            skuList =spuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("spu_id", spuIds).eq("display", 1).list();
            for (Sku sku : skuList) {
                skuIds.add(sku.getSkuId());
            }
        }
        //查询出不需要养护的实物
        List<Long> notNeedMaintenanceInkindIds= new ArrayList<>();
        List<MaintenanceLogResult> logResults = maintenanceLogService.findListBySpec(new MaintenanceLogParam() {{
            if (ToolUtil.isNotEmpty(param.getBrandId())) {
                setBrandId(param.getBrandId());
            }
            if (ToolUtil.isNotEmpty(skuIds)) {
                setSkuIds(skuIds);
            }
        }});
        for (MaintenanceLogResult logResult : logResults) {
            for (Sku sku : skuList) {
                if (logResult.getSkuId().equals(sku.getSkuId()) && ToolUtil.isNotEmpty(sku.getMaintenancePeriod())){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(logResult.getCreateTime());
                    calendar.add(Calendar.DATE,(sku.getMaintenancePeriod()-param.getNearMaintenance()));
                    String maintenance = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
                    String now = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
                    if (!maintenance.equals(now)){
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
    public void delete(MaintenanceParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceParam param){
        Maintenance oldEntity = getOldEntity(param);
        Maintenance newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaintenanceResult findBySpec(MaintenanceParam param){
        return null;
    }

    @Override
    public List<MaintenanceResult> findListBySpec(MaintenanceParam param){
        return null;
    }

    @Override
    public PageInfo<MaintenanceResult> findPageBySpec(MaintenanceParam param){
        Page<MaintenanceResult> pageContext = getPageContext();
        IPage<MaintenanceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceParam param){
        return param.getMaintenanceId();
    }

    private Page<MaintenanceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Maintenance getOldEntity(MaintenanceParam param) {
        return this.getById(getKey(param));
    }

    private Maintenance getEntity(MaintenanceParam param) {
        Maintenance entity = new Maintenance();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
