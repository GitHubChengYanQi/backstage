package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.mapper.AllocationCartMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.result.AllocationCartResult;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import  cn.atsoft.dasheng.erp.service.AllocationCartService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.hutool.core.bean.BeanUtil;
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

    @Override
    public void add(AllocationCartParam param){
        AllocationCart entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AllocationCartParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationCartParam param){
        AllocationCart oldEntity = getOldEntity(param);
        AllocationCart newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationCartResult findBySpec(AllocationCartParam param){
        return null;
    }

    @Override
    public List<AllocationCartResult> findListBySpec(AllocationCartParam param){
        return null;
    }

    @Override
    public PageInfo<AllocationCartResult> findPageBySpec(AllocationCartParam param){
        Page<AllocationCartResult> pageContext = getPageContext();
        IPage<AllocationCartResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<AllocationCartResult> resultsByAllocationId(Long allocationId){
        List<AllocationCart> allocationCarts = this.query().eq("allocation_id", allocationId).eq("display", 1).list();
        List<AllocationCartResult> results = BeanUtil.copyToList(allocationCarts, AllocationCartResult.class);
        this.format(results);
        return results;
    }

    public void format(List<AllocationCartResult> results){

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
            if (ToolUtil.isNotEmpty(result.getToStorehousePositionsId())){
                positionIds.add(result.getToStorehousePositionsId());
            }
            if (ToolUtil.isNotEmpty(result.getStorehouseId())) {
                storehouseId.add(result.getStorehouseId());

            }
            if (ToolUtil.isNotEmpty(result.getToStorehousePositionsId())){
                storehouseId.add(result.getToStorehouseId());
            }
        }

        List<StorehouseResult> storehouseResults =storehouseId.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehouseService.listByIds(storehouseId), StorehouseResult.class);
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositionsResult> positionsResults = storehousePositionsService.getDetails(positionIds);
        for (AllocationCartResult result : results) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if(result.getSkuId().equals(skuSimpleResult.getSkuId())){
                    result.setSkuResult(skuSimpleResult);
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (result.getBrandId().equals(brandResult.getBrandId())) {
                    result.setBrandResult(brandResult);
                }
            }

            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (ToolUtil.isNotEmpty(result.getStorehousePositionsId()) && result.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())){
                    result.setPositionsResult(positionsResult);
                }
                if (ToolUtil.isNotEmpty(result.getToStorehousePositionsId()) && result.getToStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())){
                    result.setToPositionsResult(positionsResult);
                }
            }
            for (StorehouseResult storehouseResult : storehouseResults) {
                if (ToolUtil.isNotEmpty(result.getStorehouseId()) && result.getStorehouseId().equals(storehouseResult.getStorehouseId())){
                    result.setStorehouseResult(storehouseResult);
                }
                if (ToolUtil.isNotEmpty(result.getToStorehouseId()) && result.getToStorehouseId().equals(storehouseResult.getStorehouseId())){
                    result.setToStorehouseResult(storehouseResult);
                }
            }
        }
    }

    private Serializable getKey(AllocationCartParam param){
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
