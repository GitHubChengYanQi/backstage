package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.entity.InventoryStock;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.InventoryStockMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.params.InventoryStockParam;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import cn.atsoft.dasheng.erp.model.result.InventoryStockResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.pojo.SkuBind;
import cn.atsoft.dasheng.erp.pojo.SkuBindParam;
import cn.atsoft.dasheng.erp.service.InventoryService;
import cn.atsoft.dasheng.erp.service.InventoryStockService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 库存盘点处理 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-07-15
 */
@Service
public class InventoryStockServiceImpl extends ServiceImpl<InventoryStockMapper, InventoryStock> implements InventoryStockService {

    @Autowired
    private SkuService skuService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StorehousePositionsService positionsService;


    @Override
    public void add(InventoryStockParam param) {
        InventoryStock entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InventoryStockParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InventoryStockParam param) {
        InventoryStock oldEntity = getOldEntity(param);
        InventoryStock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public void addList(List<InventoryDetailParam> detailParams) {
        List<InventoryStock> all = new ArrayList<>();
        for (InventoryDetailParam detailParam : detailParams) {

            List<SkuBind> skuBinds = inventoryService.getSkuBinds(detailParam);  //从物料绑定取
            for (SkuBind skuBind : skuBinds) {
                if (ToolUtil.isNotEmpty(skuBind.getPositionId()) &&
                        all.stream().noneMatch(i -> i.getSkuId().equals(skuBind.getSkuId())
                                && i.getBrandId().equals(skuBind.getBrandId())
                                && i.getPositionId().equals(skuBind.getPositionId())
                        )) {
                    InventoryStock inventoryStock = new InventoryStock();
                    inventoryStock.setSkuId(skuBind.getSkuId());
                    inventoryStock.setBrandId(skuBind.getBrandId());
                    inventoryStock.setPositionId(skuBind.getPositionId());
                    inventoryStock.setInventoryId(detailParam.getInventoryId());
                    inventoryStock.setDetailId(detailParam.getDetailId());
                    all.add(inventoryStock);
                }
            }
//----------------------------------------------------------------------------------------------------------------------
            List<InventoryStock> condition = inventoryService.condition(detailParam);   //从库存取
            for (InventoryStock inventoryStock : condition) {
                if (all.stream().noneMatch(i -> i.getSkuId().equals(inventoryStock.getSkuId())
                        && i.getBrandId().equals(inventoryStock.getBrandId())
                        && i.getPositionId().equals(inventoryStock.getPositionId())
                )) {
                    all.add(inventoryStock);
                }
            }
        }
        if (all.size() == 0) {
            throw new ServiceException(500, "没有可盘点的物料");
        }
        this.saveBatch(all);
    }

    public List<InventoryStockResult> details(Long inventoryId) {

        List<InventoryStock> inventoryStocks = this.query().eq("inventory_id", inventoryId).eq("display", 1).list();
        List<InventoryStockResult> inventoryStockResults = BeanUtil.copyToList(inventoryStocks, InventoryStockResult.class);
        this.format(inventoryStockResults);
        return inventoryStockResults;
    }

    @Override
    public InventoryStockResult findBySpec(InventoryStockParam param) {
        return null;
    }


    @Override
    public void updateInventoryStatus(AnomalyParam param, int status) {
        QueryWrapper<InventoryStock> queryWrapper = new QueryWrapper<>();
        /**
         * 同一时间段   统一修改
         */
        List<Long> inventoryIds = new ArrayList<>();
        List<InventoryResult> inventoryResults = inventoryService.listByTime();
        if (ToolUtil.isNotEmpty(inventoryResults)) {
            for (InventoryResult inventoryResult : inventoryResults) {
                inventoryIds.add(inventoryResult.getInventoryTaskId());
            }
        }
        inventoryIds.add(param.getFormId());
        queryWrapper.in("inventory_id", inventoryIds);
        queryWrapper.eq("sku_id", param.getSkuId());
        queryWrapper.eq("brand_id", param.getBrandId());
        queryWrapper.eq("position_id", param.getPositionId());

        List<InventoryStock> inventoryStocks = this.list(queryWrapper);

        for (InventoryStock inventoryStock : inventoryStocks) {    //保留之前记录
            if (inventoryStock.getLockStatus() == 99) {
                throw new ServiceException(500, "当前状态不可更改");
            }
            inventoryStock.setStatus(status);
            inventoryStock.setAnomalyId(param.getAnomalyId());
            inventoryStock.setLockStatus(0);
            inventoryStock.setDisplay(0);
        }

        List<InventoryStock> stockList = BeanUtil.copyToList(inventoryStocks, InventoryStock.class, new CopyOptions());
        for (InventoryStock inventoryStock : stockList) {
            inventoryStock.setDetailId(null);
            inventoryStock.setDisplay(1);
            if (status == 1) {   //正常物料    清楚异常id
                inventoryStock.setAnomalyId(0L);
            }
        }

        param.setType(param.getAnomalyType().toString());
        this.updateBatchById(inventoryStocks);
        this.saveBatch(stockList);
    }


    @Override
    public List<InventoryStockResult> findListBySpec(InventoryStockParam param) {
        return null;
    }

    @Override
    public PageInfo<InventoryStockResult> findPageBySpec(InventoryStockParam param) {
        Page<InventoryStockResult> pageContext = getPageContext();
        IPage<InventoryStockResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryStockParam param) {
        return param.getInventoryStockId();
    }

    private Page<InventoryStockResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InventoryStock getOldEntity(InventoryStockParam param) {
        return this.getById(getKey(param));
    }

    private InventoryStock getEntity(InventoryStockParam param) {
        InventoryStock entity = new InventoryStock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InventoryStockResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();


        for (InventoryStockResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            positionIds.add(datum.getPositionId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositionsResult> positionsResults = positionsService.details(positionIds);

        for (InventoryStockResult datum : data) {

            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && brandResult.getBrandId().equals(datum.getBrandId())) {
                    datum.setBrandResult(brandResult);
                    break;
                }
            }


            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(positionsResult.getStorehousePositionsId())) {
                    datum.setPositionsResult(positionsResult);
                    break;
                }
            }

        }

    }
}
