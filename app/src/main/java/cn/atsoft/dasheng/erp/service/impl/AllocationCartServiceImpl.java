package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.mapper.AllocationCartMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.result.AllocationCartResult;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.AllocationCartService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.AllocationDetailService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 调拨子表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-25
 */
@Service
public class AllocationCartServiceImpl extends ServiceImpl<AllocationCartMapper, AllocationCart> implements AllocationCartService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private AllocationDetailService allocationDetailService;

    @Override
    public void add(AllocationCartParam param) {

        Long allocationId = param.getAllocationId();
        Long skuId = param.getSkuId();
        Long brandId = param.getBrandId();
        Long storehouseId = param.getStorehouseId();
        List<AllocationDetail> allocationDetails = allocationDetailService.query().eq("allocation_id", allocationId).eq("display", 1).eq("status", 0).list();


        AllocationCart entity = getEntity(param);
        entity.setType("carry");
        this.save(entity);
    }

    @Override
    public void addBatch(AllocationCartParam param) {
        if (ToolUtil.isEmpty(param.getAllocationCartParams())) {
            throw new ServiceException(500, "请填写您要分派的信息");
        }
        List<AllocationDetail> allocationDetails = allocationDetailService.query().eq("display", 1).eq("allocation_id", param.getAllocationId()).eq("display",1).eq("status",0).list();
        List<AllocationCart> allocationCarts = this.query().eq("allocation_id", param.getAllocationId()).eq("display", 1).list();




        List<AllocationCart> totalList = new ArrayList<>();
        allocationCarts.parallelStream().collect(Collectors.groupingBy(AllocationCart::getAllocationDetailId, Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new AllocationCart() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setAllocationDetailId(a.getAllocationDetailId());
                    }}).ifPresent(totalList::add);
                }
        );
        for (AllocationCart allocationCart : totalList) {
            for (AllocationDetail allocationDetail : allocationDetails) {
                if (allocationCart.getAllocationDetailId().equals(allocationDetail.getAllocationDetailId())){
                    allocationDetail.setNumber(allocationDetail.getNumber()-allocationCart.getNumber());
                }
            }
        }
        allocationDetails.removeIf(i->i.getNumber()==0);


        List<AllocationCart> entityList = new ArrayList<>();
        for (AllocationCartParam allocationCartParam : param.getAllocationCartParams()) {
            int number = allocationCartParam.getNumber();
            for (AllocationDetail allocationDetail : allocationDetails) {
                if (number > 0) {
                    if ((allocationDetail.getHaveBrand().equals(1) && allocationDetail.getBrandId().equals(allocationCartParam.getBrandId()) && allocationDetail.getSkuId().equals(allocationCartParam.getSkuId())) || (allocationDetail.getHaveBrand().equals(0) && allocationDetail.getSkuId().equals(allocationCartParam.getSkuId()))) {
                        if (ToolUtil.isNotEmpty(allocationCartParam.getStorehousePositionsId()) && allocationDetail.getStorehousePositionsId().equals(allocationCartParam.getStorehousePositionsId())) {
                            continue;
                        }
                        int lastNumber = number;
                        number -= allocationDetail.getNumber();
                        if (number >= 0) {
                            AllocationCart entity = new AllocationCart();
                            entity.setNumber(Math.toIntExact(allocationDetail.getNumber()));
                            entity.setSkuId(allocationDetail.getSkuId());
                            if (ToolUtil.isNotEmpty(allocationDetail.getBrandId())) {
                                entity.setBrandId(allocationDetail.getBrandId());
                            }
                            if (ToolUtil.isNotEmpty(allocationCartParam.getStorehousePositionsId())) {
                                entity.setStorehousePositionsId(allocationCartParam.getStorehousePositionsId());
                            }
                            entity.setAllocationId(param.getAllocationId());
                            entity.setAllocationDetailId(allocationDetail.getAllocationDetailId());
                            entity.setStorehouseId(allocationCartParam.getStorehouseId());
                            entity.setType("carry");
                            entityList.add(entity);
                        } else {
                            AllocationCart entity = new AllocationCart();
                            entity.setNumber(lastNumber);
                            entity.setSkuId(allocationCartParam.getSkuId());
                            if (ToolUtil.isNotEmpty(allocationDetail.getBrandId())) {
                                entity.setBrandId(allocationDetail.getBrandId());
                            }
                            entity.setAllocationId(param.getAllocationId());
                            entity.setAllocationDetailId(allocationDetail.getAllocationDetailId());
                            entity.setStorehouseId(allocationCartParam.getStorehouseId());
                            entity.setType("carry");
                            if (ToolUtil.isNotEmpty(allocationCartParam.getStorehousePositionsId())) {
                                entity.setStorehousePositionsId(allocationCartParam.getStorehousePositionsId());
                            }
                            entityList.add(entity);
                        }

                    }
                }
            }

        }
        this.saveBatch(entityList);


    }

    @Override
    public void delete(AllocationCartParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationCartParam param) {


        /**
         * 先删除
         */
        List<AllocationCart> list = this.query().eq("allocation_id", param.getAllocationId()).eq("sku_id", param.getSkuId()).eq("type", "carry").eq("display", 1).list();
        for (AllocationCart allocationCart : list) {
            allocationCart.setDisplay(0);
        }
        this.updateBatchById(list);
        /**
         * 后添加
         */
        this.addBatch(param);
    }

    @Override
    public AllocationCartResult findBySpec(AllocationCartParam param) {
        return null;
    }

    @Override
    public List<AllocationCartResult> findListBySpec(AllocationCartParam param) {
        return null;
    }

    @Override
    public PageInfo<AllocationCartResult> findPageBySpec(AllocationCartParam param) {
        Page<AllocationCartResult> pageContext = getPageContext();
        IPage<AllocationCartResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<AllocationCartResult> resultsByAllocationId(Long allocationId) {
        List<AllocationCart> allocationCarts = this.query().eq("allocation_id", allocationId).eq("display", 1).list();
        List<AllocationCartResult> results = BeanUtil.copyToList(allocationCarts, AllocationCartResult.class);
        this.format(results);
        return results;
    }


    public void format(List<AllocationCartResult> results) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> storehouseId = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        for (AllocationCartResult result : results) {
            skuIds.add(result.getSkuId());
            brandIds.add(result.getBrandId());
            if (ToolUtil.isNotEmpty(result.getStorehousePositionsId())) {
                positionIds.add(result.getStorehousePositionsId());

            }
            if (ToolUtil.isNotEmpty(result.getToStorehousePositionsId())) {
                positionIds.add(result.getToStorehousePositionsId());
            }
            if (ToolUtil.isNotEmpty(result.getStorehouseId())) {
                storehouseId.add(result.getStorehouseId());

            }
            if (ToolUtil.isNotEmpty(result.getToStorehousePositionsId())) {
                storehouseId.add(result.getToStorehouseId());
            }
        }

        List<StorehouseResult> storehouseResults = storehouseId.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehouseService.listByIds(storehouseId), StorehouseResult.class);
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositionsResult> positionsResults = positionIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehousePositionsService.listByIds(positionIds), StorehousePositionsResult.class);
        for (AllocationCartResult result : results) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (result.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    result.setSkuResult(skuSimpleResult);
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (result.getBrandId().equals(brandResult.getBrandId())) {
                    result.setBrandResult(brandResult);
                }
            }

            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (ToolUtil.isNotEmpty(result.getStorehousePositionsId()) && result.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
                    result.setPositionsResult(positionsResult);
                }
                if (ToolUtil.isNotEmpty(result.getToStorehousePositionsId()) && result.getToStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
                    result.setToPositionsResult(positionsResult);
                }
            }
            for (StorehouseResult storehouseResult : storehouseResults) {
                if (ToolUtil.isNotEmpty(result.getStorehouseId()) && result.getStorehouseId().equals(storehouseResult.getStorehouseId())) {
                    result.setStorehouseResult(storehouseResult);
                }
                if (ToolUtil.isNotEmpty(result.getToStorehouseId()) && result.getToStorehouseId().equals(storehouseResult.getStorehouseId())) {
                    result.setToStorehouseResult(storehouseResult);
                }
            }
        }
    }

    private Serializable getKey(AllocationCartParam param) {
        return param.getAllocationCartId();
    }

    private Page<AllocationCartResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AllocationCart getOldEntity(AllocationCartParam param) {
        return this.getById(getKey(param));
    }

    private AllocationCart getEntity(AllocationCartParam param) {
        AllocationCart entity = new AllocationCart();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
