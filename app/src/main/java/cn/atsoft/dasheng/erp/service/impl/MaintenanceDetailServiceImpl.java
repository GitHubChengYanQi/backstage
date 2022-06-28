package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.mapper.MaintenanceDetailMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.request.MaintenanceMirageRequest;
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

        List<StockDetails> totalList = new ArrayList<>();
        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId() + '_' + item.getStorehousePositionsId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<Long> positionsIds = new ArrayList<>();
        for (StockDetails details : totalList) {
            positionsIds.add(details.getStorehousePositionsId());
            skuIds.add(details.getSkuId());
            brandIds.add(details.getBrandId());
        }
        List<SkuSimpleResult> skuSimpleResultList = skuService.simpleFormatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);


        positionsIds = positionsIds.stream().distinct().collect(Collectors.toList());
        List<StorehousePositionsResult> positionDetail = storehousePositionsService.getDetails(positionsIds);
        for (StorehousePositionsResult storehousePositionsResult : positionDetail) {
            MaintenanceMirageRequest mirageRequest = new MaintenanceMirageRequest();
            mirageRequest.setStorehousePositionsResult(storehousePositionsResult);

            for (StockDetails details : totalList) {
                if (details.getStorehousePositionsId().equals(storehousePositionsResult.getStorehousePositionsId())){
                    for (SkuSimpleResult skuSimpleResult : skuSimpleResultList) {
                        if (skuSimpleResult.getSkuId().equals(details.getSkuId()));
                    }
                }
            }

        }



















        return "wo qu ni ma de ";

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
