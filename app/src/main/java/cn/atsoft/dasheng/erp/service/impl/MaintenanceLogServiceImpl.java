package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
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
        MaintenanceLog entity = getEntity(param);
        this.save(entity);
    }


    public void needMaintenance(List<Long> ids){
//        List<MaintenanceDetail> maintenanceDetails = maintenanceDetailService.query().in("maintenance_id", ids).eq("display", 1).eq("status", 0).list();
        List<Maintenance> maintenances = maintenanceService.listByIds(ids);
        List<StockDetails> stockDetails = new ArrayList<>();
        for (Maintenance maintenance : maintenances) {
            stockDetails.addAll(maintenanceService.needMaintenanceByRequirement(maintenance));
        }
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        stockDetails = stockDetails.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(StockDetails::getStockItemId))), ArrayList::new));
        List<Long> storeHousePositionsIds = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            storeHousePositionsIds.add(stockDetail.getStorehousePositionsId());
        }
        for (StockDetails stockDetail : stockDetails) {
            skuIds.add(stockDetail.getSkuId());
            if (ToolUtil.isNotEmpty(stockDetail.getBrandId())) {
                brandIds.add(stockDetail.getBrandId());
            }
        }
        List<Brand> brands =brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);
        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);

        storeHousePositionsIds = storeHousePositionsIds.stream().distinct().collect(Collectors.toList());
        List<StorehousePositionsResult> positionsResults = storehousePositionsService.getDetails(storeHousePositionsIds);
        






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
