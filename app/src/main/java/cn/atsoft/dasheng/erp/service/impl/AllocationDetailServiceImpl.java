package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.mapper.AllocationDetailMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.params.AllocationParam;
import cn.atsoft.dasheng.erp.model.request.AllocationDetailParamJson;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.AllocationCartService;
import cn.atsoft.dasheng.erp.service.AllocationDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
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
 * @since 2022-07-13
 */
@Service
public class AllocationDetailServiceImpl extends ServiceImpl<AllocationDetailMapper, AllocationDetail> implements AllocationDetailService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Autowired
    private StorehouseService storehouseService;

    @Autowired
    private AllocationCartService allocationCartService;


    @Override
    public void add(AllocationDetailParam param) {
        AllocationDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void add(AllocationParam param) {
        List<AllocationDetail> entityList = new ArrayList<>();
        List<AllocationCart> cartList = new ArrayList<>();
        if (ToolUtil.isEmpty(param.getJsonParam())) {
            throw new ServiceException(500, "请选择所需物料及库位后提交");
        }
        /**
         * 需求目标位置
         */
        if (ToolUtil.isNotEmpty(param.getJsonParam().getSkuAndNumbers())) {
            for (AllocationDetailParam storehouseAndPosition : param.getJsonParam().getSkuAndNumbers()) {
                AllocationDetail entity = this.getEntity(storehouseAndPosition);
                entity.setAllocationId(param.getAllocationId());
                entityList.add(entity);
            }
            this.saveBatch(entityList);
        }
        /**
         * 期望目标位置
         */
        if (ToolUtil.isNotEmpty(param.getJsonParam().getStorehouseAndPositions())) {
            for (AllocationCartParam storehouseAndPosition : param.getJsonParam().getStorehouseAndPositions()) {
                AllocationCart cart = BeanUtil.copyProperties(storehouseAndPosition, AllocationCart.class);
                cart.setAllocationId(param.getAllocationId());
                cart.setType("hope");
                cartList.add(cart);
            }
            allocationCartService.saveBatch(cartList);

        }
    }

    @Override
    public void delete(AllocationDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationDetailParam param) {
        AllocationDetail oldEntity = getOldEntity(param);
        AllocationDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationDetailResult findBySpec(AllocationDetailParam param) {
        return null;
    }

    @Override
    public List<AllocationDetailResult> findListBySpec(AllocationDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<AllocationDetailResult> findPageBySpec(AllocationDetailParam param) {
        Page<AllocationDetailResult> pageContext = getPageContext();
        IPage<AllocationDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<AllocationDetailResult> resultsByAllocationId(Long allocationId) {
        List<AllocationDetail> allocationDetails = this.query().eq("allocation_id", allocationId).eq("display", 1).list();
        List<AllocationDetailResult> results = BeanUtil.copyToList(allocationDetails, AllocationDetailResult.class);
        this.format(results);
        return results;
    }

    public void format(List<AllocationDetailResult> results) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> storehouseId = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        for (AllocationDetailResult result : results) {
            if (ToolUtil.isNotEmpty(result.getParams())) {
                AllocationDetailParamJson allocationDetailParamJson = JSON.parseObject(result.getParams(), AllocationDetailParamJson.class);
//                for (AllocationDetailParamJson.SkuAndNumber skuAndNumber : allocationDetailParamJson.getSkuAndNumbers()) {
//
//                }
            }

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
        for (AllocationDetailResult result : results) {
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

    private Serializable getKey(AllocationDetailParam param) {
        return param.getAllocationDetailId();
    }

    private Page<AllocationDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AllocationDetail getOldEntity(AllocationDetailParam param) {
        return this.getById(getKey(param));
    }

    private AllocationDetail getEntity(AllocationDetailParam param) {
        AllocationDetail entity = new AllocationDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public List<AllocationDetailResult> listByAllocationIdAndSkuId(Long allocationId, Long skuId){
        List<AllocationDetail> allocationDetails = this.query().eq("allocation_id", allocationId).eq("sku_id", skuId).eq("display", 1).list();
        List<AllocationDetailResult> allocationResults = BeanUtil.copyToList(allocationDetails, AllocationDetailResult.class);
        this.format(allocationResults);
        return allocationResults;

    }

}
