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
import cn.atsoft.dasheng.erp.mapper.MaintenanceDetailMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * 养护申请子表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@Service
public class MaintenanceDetailServiceImpl extends ServiceImpl<MaintenanceDetailMapper, MaintenanceDetail> implements MaintenanceDetailService {
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
    public void add(MaintenanceDetailParam param) {
        MaintenanceDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MaintenanceDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceDetailParam param) {
        MaintenanceDetail oldEntity = getOldEntity(param);
        MaintenanceDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public List<Map<String, Object>> needMaintenance(List<Long> ids) {
//        List<MaintenanceDetail> maintenanceDetails = maintenanceDetailService.query().in("maintenance_id", ids).eq("display", 1).eq("status", 0).list();
        List<Maintenance> maintenances = maintenanceService.listByIds(ids);
        List<StockDetails> stockDetails = new ArrayList<>();
        for (Maintenance maintenance : maintenances) {
            stockDetails.addAll(maintenanceService.needMaintenanceByRequirement(maintenance));
        }
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        stockDetails = stockDetails.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(StockDetails::getStockItemId))), ArrayList::new));
        List<StockDetailsResult> stockDetailsResults = BeanUtil.copyToList(stockDetails, StockDetailsResult.class, new CopyOptions());
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
        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);
        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);

        storeHousePositionsIds = storeHousePositionsIds.stream().distinct().collect(Collectors.toList());
        List<StorehousePositionsResult> positionsResults = storehousePositionsService.getDetails(storeHousePositionsIds);





       //拼装返回格式u
        List<Map<String, Object>> results = new ArrayList<>();
        for (StorehousePositionsResult positionsResult : positionsResults) {
            Map<String, Object> result = new HashMap<>();
            result.put("positionsResult", positionsResult);
            List<StockDetailsResult> stockDetailsResultList = new ArrayList<>();
            List<Long> skus = new ArrayList<>();
            for (StockDetailsResult stockDetailsResult : stockDetailsResults) {
                if (stockDetailsResult.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
                    stockDetailsResultList.add(stockDetailsResult);
                    skus.add(stockDetailsResult.getSkuId());
                }
            }
            skuIds = skuIds.stream().distinct().collect(Collectors.toList());
            List<SkuSimpleResult> skuSimpleResultList = new ArrayList<>();
            for (Long skuId : skus) {
                for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                    if (skuId.equals(skuSimpleResult.getSkuId())) {
                        skuSimpleResultList.add(skuSimpleResult);
                    }
                }
            }
            List<Map<String, Object>> skuResults = new ArrayList<>();
            for (SkuSimpleResult skuSimpleResult : skuSimpleResultList) {
                Map<String, Object> skuResult = BeanUtil.beanToMap(skuSimpleResult);
                List<Map<String,Object>> brandResults = new ArrayList<>();
                for (StockDetailsResult stockDetailsResult : stockDetailsResultList) {
                    if (skuSimpleResult.getSkuId().equals(stockDetailsResult.getSkuId())){
                        for (Brand brand : brands) {
                            if (stockDetailsResult.getBrandId().equals(brand.getBrandId())){
                                Map<String,Object> brandResult = new HashMap<>();
                                brandResult.put("brandId",brand.getBrandId());
                                brandResult.put("brandName",brand.getBrandName());
                                brandResults.add(brandResult);
                            }
                        }
                    }
                }
                skuResult.put("brandResults",brandResults);
                skuResults.add(skuResult);
            }
            result.put("skuResults",skuResults);
            results.add(result);
        }
        return results;
    }


        @Override
        public MaintenanceDetailResult findBySpec (MaintenanceDetailParam param){
            return null;
        }

        @Override
        public List<MaintenanceDetailResult> findListBySpec (MaintenanceDetailParam param){
            return null;
        }

        @Override
        public PageInfo<MaintenanceDetailResult> findPageBySpec (MaintenanceDetailParam param){
            Page<MaintenanceDetailResult> pageContext = getPageContext();
            IPage<MaintenanceDetailResult> page = this.baseMapper.customPageList(pageContext, param);
            return PageFactory.createPageInfo(page);
        }

        private Serializable getKey (MaintenanceDetailParam param){
            return param.getMaintenanceDetailId();
        }

        private Page<MaintenanceDetailResult> getPageContext () {
            return PageFactory.defaultPage();
        }

        private MaintenanceDetail getOldEntity (MaintenanceDetailParam param){
            return this.getById(getKey(param));
        }

        private MaintenanceDetail getEntity (MaintenanceDetailParam param){
            MaintenanceDetail entity = new MaintenanceDetail();
            ToolUtil.copyProperties(param, entity);
            return entity;
        }

    }
