package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 仓库库位表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class StorehousePositionsServiceImpl extends ServiceImpl<StorehousePositionsMapper, StorehousePositions> implements StorehousePositionsService {
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private SkuService skuService;

    @Override
    public void add(StorehousePositionsParam param) {
        StorehousePositions entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StorehousePositionsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehousePositionsParam param) {
        StorehousePositions oldEntity = getOldEntity(param);
        StorehousePositions newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehousePositionsResult findBySpec(StorehousePositionsParam param) {
        return null;
    }

    @Override
    public List<StorehousePositionsResult> findListBySpec(StorehousePositionsParam param) {
        return null;
    }

    @Override
    public PageInfo<StorehousePositionsResult> findPageBySpec(StorehousePositionsParam param) {
        Page<StorehousePositionsResult> pageContext = getPageContext();
        IPage<StorehousePositionsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public Map<String,Map<String,Object>> takeStock(StorehousePositionsParam param) {
        param.getStorehousePositionsId();
        List<Long> skuIds = new ArrayList<>();
        List<StockDetails> stockDetailsList = stockDetailsService.lambdaQuery().in(StockDetails::getStorehousePositionsId, param.getStorehousePositionsId()).list();
        for (StockDetails stockDetails : stockDetailsList) {
                skuIds.add(stockDetails.getSkuId());

        }

        List<Long> collect = skuIds.stream().distinct().collect(Collectors.toList());
        List<Sku> skuList = skuService.lambdaQuery().in(Sku::getSkuId, collect).list();
        List<SkuResult> skuResults  =new ArrayList<>();
        for (Sku sku : skuList) {

        }
        Map<String,Map<String,Object>> resultMap = new HashMap<>();
        List<List<StockDetails>> listArrayList = new ArrayList<>();
        for (Long skuId : collect) {
            List<StockDetails> stockDetails = new ArrayList<>();
            List<BackSku> backSkus = skuService.backSku(skuId);
            for (StockDetails stockDetail : stockDetailsList) {
                if (skuId.equals(stockDetail.getSkuId())) {
                    Map<String,Object> map = new HashMap<>();
                    ToolUtil.copyProperties(stockDetail,map);
                    map.put("skuValue",backSkus);
                    resultMap.put(stockDetail.getStockItemId().toString(),map);
                }
            }
            listArrayList.add(stockDetails);
        }
        System.out.println(listArrayList);
        return resultMap;
    }

    private Serializable getKey(StorehousePositionsParam param) {
        return param.getStorehousePositionsId();
    }

    private Page<StorehousePositionsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositions getOldEntity(StorehousePositionsParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositions getEntity(StorehousePositionsParam param) {
        StorehousePositions entity = new StorehousePositions();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<StorehousePositionsResult> data) {
        List<Long> storeIds = new ArrayList<>();
        List<Long> pids = new ArrayList<>();
        for (StorehousePositionsResult datum : data) {
            storeIds.add(datum.getStorehouseId());
            pids.add(datum.getPid());
        }
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.query().in("storehouse_id", storeIds).list();

        List<StorehousePositions> positions = pids.size() == 0 ? new ArrayList<>() :
                this.query().in("storehouse_positions_id", pids).list();

        for (StorehousePositionsResult datum : data) {
            if (ToolUtil.isNotEmpty(storehouses)) {
                for (Storehouse storehouse : storehouses) {
                    if (datum.getStorehouseId() != null && storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                    }
                }
            }
            if (ToolUtil.isNotEmpty(positions)) {
                for (StorehousePositions position : positions) {
                    if (position.getStorehousePositionsId().equals(datum.getPid())) {
                        StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                        ToolUtil.copyProperties(datum, storehousePositionsResult);
                        datum.setStorehousePositionsResult(storehousePositionsResult);
                    }
                }
            }
        }
    }
}
