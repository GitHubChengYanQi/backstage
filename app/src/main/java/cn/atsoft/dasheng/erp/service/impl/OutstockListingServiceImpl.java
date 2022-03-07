package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.OutstockListingMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.InKindRequest;
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
 * 出库清单 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
@Service
public class OutstockListingServiceImpl extends ServiceImpl<OutstockListingMapper, OutstockListing> implements OutstockListingService {
    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private StorehousePositionsService positionsService;

    @Override
    public void add(OutstockListingParam param) {
        param.setDelivery(param.getNumber());
        OutstockListing entity = getEntity(param);
        this.save(entity);
    }

    @Override

    public void delete(OutstockListingParam param) {
        throw new ServiceException(500, "不可以删除");
    }

    @Override

    public void update(OutstockListingParam param) {
        throw new ServiceException(500, "不可以修改");
    }

    @Override
    public OutstockListingResult findBySpec(OutstockListingParam param) {
        return null;
    }

    @Override
    public List<OutstockListingResult> findListBySpec(OutstockListingParam param) {
        return null;
    }

    @Override
    public PageInfo<OutstockListingResult> findPageBySpec(OutstockListingParam param) {
        Page<OutstockListingResult> pageContext = getPageContext();
        IPage<OutstockListingResult> page = this.baseMapper.customPageList(pageContext, param);

        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 通过订单获取清单
     *
     * @param id
     * @return
     */
    @Override
    public List<OutstockListingResult> getDetailsByOrderId(Long id) {
        List<OutstockListing> outstockListings = this.query().eq("outstock_order_id", id).list();
        List<OutstockListingResult> resultList = BeanUtil.copyToList(outstockListings, OutstockListingResult.class, new CopyOptions());
        List<StorehousePositions> storehousePositions = positionsService.list();
        format(resultList);
        List<StockDetails> details = stockDetailsService.list();

        if (ToolUtil.isNotEmpty(storehousePositions)) {
            for (OutstockListingResult data : resultList) {
                List<StorehousePositionsResult> positionsResults = new ArrayList<>();
                for (StockDetails detail : details) {
                    if (ToolUtil.isNotEmpty(detail.getBrandId()) && data.getSkuId().equals(detail.getSkuId()) && data.getBrandId().equals(detail.getBrandId())) {
                        StorehousePositionsResult positionsResult = positionsService.getDetail(detail.getStorehousePositionsId(), storehousePositions);
                        positionsResult.setNumber(detail.getNumber());
                        positionsResults.add(positionsResult);
                    }
                }
                data.setPositionsResults(positionsResults);
            }
        }


        return resultList;
    }

    private Serializable getKey(OutstockListingParam param) {
        return param.getOutstockListingId();
    }

    private Page<OutstockListingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockListing getOldEntity(OutstockListingParam param) {
        return this.getById(getKey(param));
    }

    @Override
    public OutstockListing getEntity(OutstockListingParam param) {
        OutstockListing entity = new OutstockListing();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<OutstockListingResult> data) {
        List<Long> brandIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (OutstockListingResult record : data) {
            brandIds.add(record.getBrandId());
            skuIds.add(record.getSkuId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId, brandIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();


        for (OutstockListingResult record : data) {
            List<BackSku> backSkus = skuService.backSku(record.getSkuId());
            SpuResult result = skuService.backSpu(record.getSkuId());
            record.setBackSkus(backSkus);
            record.setSpuResult(result);

            if (ToolUtil.isNotEmpty(record.getSkuId())) {
                Sku sku = skuService.getById(record.getSkuId());
                record.setSku(sku);
            }

            for (Brand brand : brandList) {
                if (record.getBrandId() != null && record.getBrandId().equals(brand.getBrandId())) {
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);
                    record.setBrandResult(brandResult);
                    break;
                }
            }
            if (ToolUtil.isNotEmpty(skus)) {
                for (Sku sku : skus) {
                    if (record.getSkuId() != null && sku.getSkuId().equals(record.getSkuId())) {
                        SkuResult skuResult = new SkuResult();
                        ToolUtil.copyProperties(sku, skuResult);
                        record.setSkuResult(skuResult);
                    }
                }
            }
        }
    }

}
