package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsBindMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsDeptBindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.StorehousePositionsDeptBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.model.request.ProcurementDetailSkuTotal;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * 库位绑定物料表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
@Service
public class StorehousePositionsBindServiceImpl extends ServiceImpl<StorehousePositionsBindMapper, StorehousePositionsBind> implements StorehousePositionsBindService {
    @Autowired
    private StorehousePositionsService positionsService;

    @Autowired
    private StorehouseService storehouseService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StorehousePositionsDeptBindService storehousePositionsDeptBindService;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Override
    public StorehousePositionsBind add(StorehousePositionsBindParam param) {
        List<StorehousePositions> positions = positionsService.query().eq("pid", param.getPositionId()).list();
        if (ToolUtil.isNotEmpty(positions)) {
            throw new ServiceException(500, "当前库位不是最下级");
        }
        StorehousePositionsBind positionsBind = this.query().eq("position_id", param.getPositionId()).eq("sku_id", param.getSkuId()).one();
        if (ToolUtil.isNotEmpty(positionsBind)) {
            throw new ServiceException(500, "以绑定");
        }
        StorehousePositionsBind entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void bindBatch(StorehousePositionsBindParam param) {
        List<StorehousePositionsBind> binds = new ArrayList<>();
        for (Long skuId : param.getSkuIds()) {
            StorehousePositionsBind bind = new StorehousePositionsBind();
            bind.setSkuId(skuId);
            bind.setBrandId(param.getBrandId());
            bind.setPositionId(param.getPositionId());
            binds.add(bind);
        }
        this.saveBatch(binds);
    }

    @Override
    public void SpuAddBind(StorehousePositionsBindParam param) {

        List<StorehousePositions> positions = positionsService.query().eq("pid", param.getPositionId()).eq("display", 1).list();
        if (ToolUtil.isNotEmpty(positions)) {
            throw new ServiceException(500, "当前库位不是最下级");
        }
        List<StorehousePositionsBind> storehousePositionsBinds = new ArrayList<>();

        List<Sku> skus = skuService.query().eq("spu_id", param.getSpuId()).eq("display", 1).list();
        List<StorehousePositionsBind> positionsBinds = this.query().eq("position_id", param.getPositionId()).eq("display", 1).list();
        for (Sku sku : skus) {
            if (positionsBinds.stream().noneMatch(i -> i.getPositionId().equals(param.getPositionId()) && i.getSkuId().equals(sku.getSkuId()))) {
                StorehousePositionsBind bind = new StorehousePositionsBind();
                bind.setSkuId(sku.getSkuId());
                bind.setPositionId(param.getPositionId());
                storehousePositionsBinds.add(bind);
            }
        }
        this.saveBatch(storehousePositionsBinds);
    }

    @Override
    public void delete(StorehousePositionsBindParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehousePositionsBindParam param) {
        StorehousePositionsBind oldEntity = getOldEntity(param);
        StorehousePositionsBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public List<StorehousePositionsResult> treeView(List<Long> skuIds) {
        List<StorehousePositionsResult> topResults = new ArrayList<>();
        List<SkuResult> skuResultList = skuService.formatSkuResult(skuIds);

        List<Long> positionIds = new ArrayList<>();
        List<StockDetails> stockDetails = stockDetailsService.query().in("sku_id", skuIds).list();
        for (StockDetails stockDetail : stockDetails) {
            positionIds.add(stockDetail.getStorehousePositionsId());
        }

        List<StockDetails> totalList = new ArrayList<>();

        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setStorehousePositionsId(a.getStorehousePositionsId());
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                    }}).ifPresent(totalList::add);
                }
        );

        Map<Long, List<SkuResult>> skuMap = new HashMap<>();
        for (StockDetails details : totalList) {
            for (SkuResult skuResult : skuResultList) {
                if (details.getSkuId().equals(skuResult.getSkuId())) {
                    List<SkuResult> results = skuMap.get(details.getStorehousePositionsId());
                    if (ToolUtil.isEmpty(results)) {
                        results = new ArrayList<>();
                        results.add(skuResult);
                        skuMap.put(details.getStorehousePositionsId(), results);
                        break;
                    } else {
                        results.add(skuResult);
                        skuMap.put(details.getStorehousePositionsId(), results);
                        break;
                    }
                }
            }
        }

        /**
         *  查出所有库位放进map 里
         */
        List<StorehousePositions> positions = positionsService.list();
        List<StorehousePositionsResult> allResult = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());

        Map<Long, StorehousePositionsResult> allMap = new HashMap<>();
        for (StorehousePositionsResult storehousePositionsResult : allResult) {
            allMap.put(storehousePositionsResult.getStorehousePositionsId(), storehousePositionsResult);
        }

        /**
         * 查出需要的库位 进行比对
         */
        List<StorehousePositions> storehousePositions = positionIds.size() == 0 ? new ArrayList<>() : positionsService.query().in("storehouse_positions_id", positionIds).orderByAsc("sort").list();
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(storehousePositions, StorehousePositionsResult.class, new CopyOptions());


        for (StorehousePositionsResult positionsResult : positionsResults) {
            StorehousePositionsResult supper = allMap.get(positionsResult.getPid());

            List<SkuResult> skuResults = skuMap.get(positionsResult.getStorehousePositionsId());
            positionsResult.setSkuResults(skuResults);

            if (ToolUtil.isNotEmpty(supper)) {


                //找到最下级的直属上级
                List<StorehousePositionsResult> results = supper.getStorehousePositionsResults();
                if (ToolUtil.isEmpty(results)) {
                    results = new ArrayList<>();
                }
                results.add(positionsResult);
                supper.setStorehousePositionsResults(results);
                StorehousePositionsResult parent = new StorehousePositionsResult();//直属上级 找到所有上级
                StorehousePositionsResult result = getSupper(supper, allResult, parent, skuResults);
                if (topResults.stream().noneMatch(i -> i.getStorehousePositionsId().equals(result.getStorehousePositionsId()))) {
                    topResults.add(result);
                }
            } else {
                topResults.add(positionsResult);
            }
        }
        return topResults;
    }

    /**
     * 返回上级
     *
     * @param result
     * @param results
     * @return
     */
    private StorehousePositionsResult getSupper(StorehousePositionsResult now, List<StorehousePositionsResult> results, StorehousePositionsResult result, List<SkuResult> skuResults) {

        List<SkuResult> skuResultList = now.getSkuResults();
        if (ToolUtil.isEmpty(skuResultList)) {
            skuResultList = new ArrayList<>();
        }
        skuResultList.addAll(skuResults);
        now.setSkuResults(skuResultList);

        if (!now.getPid().equals(0L)) {
            for (StorehousePositionsResult storehousePositionsResult : results) {

                if (now.getPid().equals(storehousePositionsResult.getStorehousePositionsId())) {
                    List<StorehousePositionsResult> positionsResults = storehousePositionsResult.getStorehousePositionsResults();
                    if (ToolUtil.isEmpty(positionsResults)) {
                        positionsResults = new ArrayList<>();
                    }
                    positionsResults.add(now);
                    storehousePositionsResult.setStorehousePositionsResults(positionsResults);
                    result = getSupper(storehousePositionsResult, results, result, skuResults);
                }
            }
        } else {
            result = now;
        }
        return result;
    }

    @Override
    public StorehousePositionsBindResult findBySpec(StorehousePositionsBindParam param) {
        return null;
    }

    @Override
    public List<StorehousePositionsBindResult> findListBySpec(StorehousePositionsBindParam param) {
        return null;
    }

    @Override
    public PageInfo<StorehousePositionsBindResult> findPageBySpec(StorehousePositionsBindParam param) {
        Page<StorehousePositionsBindResult> pageContext = getPageContext();
        IPage<StorehousePositionsBindResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    public void format(List<StorehousePositionsBindResult> param) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();

        for (StorehousePositionsBindResult bindResult : param) {
            skuIds.add(bindResult.getSkuId());
            positionIds.add(bindResult.getPositionId());
        }

        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        List<StorehousePositions> storehousePositions = positionIds.size() == 0 ? new ArrayList<>() : positionsService.listByIds(positionIds);
        List<StorehousePositionsResult> storehousePositionsResults = new ArrayList<>();
        for (StorehousePositions storehousePosition : storehousePositions) {
            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
            storehousePositionsResults.add(storehousePositionsResult);
        }

        for (StorehousePositionsBindResult storehousePositionsBindResult : param) {
            for (SkuResult skuResult : skuResults) {
                if (storehousePositionsBindResult.getSkuId().equals(skuResult.getSkuId())) {
                    storehousePositionsBindResult.setSkuResult(skuResult);
                }
            }
            for (StorehousePositionsResult storehousePositionResult : storehousePositionsResults) {
                if (storehousePositionsBindResult.getPositionId().equals(storehousePositionResult.getStorehousePositionsId())) {
                    storehousePositionsBindResult.setStorehousePositionsResult(storehousePositionResult);
                }
            }
        }

    }

    @Override
    public List<StorehousePositionsResult> sku2position(Long skuId) {
        List<StorehousePositionsBind> positionsBinds = this.query().eq("sku_id", skuId).eq("display", 1).list();

        List<Storehouse> storehouseList = storehouseService.list();

        List<Long> positionIds = new ArrayList<>();
        for (StorehousePositionsBind positionsBind : positionsBinds) {
            positionIds.add(positionsBind.getPositionId());
        }
        List<StorehousePositionsDeptBindResult> bindByPositionIds = positionIds.size() > 0 ? storehousePositionsDeptBindService.getBindByPositionIds(positionIds) : new ArrayList<>();
        List<StorehousePositionsResult> results = new ArrayList<>();

        positionIds.clear();
        Long deptId = LoginContextHolder.getContext().getUser().getDeptId();
        for (StorehousePositionsDeptBindResult bindByPositionId : bindByPositionIds) {
            if (bindByPositionId.getDeptIds().stream().anyMatch(i -> i.equals(deptId))) {
                positionIds.add(bindByPositionId.getStorehousePositionsId());
            }
        }
        List<StorehousePositions> storehousePositions = positionIds.size() > 0 ? positionsService.listByIds(positionIds) : new ArrayList<>();
        for (StorehousePositions storehousePosition : storehousePositions) {
            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
            for (Storehouse storehouse : storehouseList) {
                if (storehouse.getStorehouseId().equals(storehousePosition.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    storehousePositionsResult.setStorehouseResult(storehouseResult);
                }
            }
            results.add(storehousePositionsResult);
        }

        return results;
    }


    private Serializable getKey(StorehousePositionsBindParam param) {
        return param.getBindId();
    }

    private Page<StorehousePositionsBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositionsBind getOldEntity(StorehousePositionsBindParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositionsBind getEntity(StorehousePositionsBindParam param) {
        StorehousePositionsBind entity = new StorehousePositionsBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
