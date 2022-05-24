package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.mapper.ApplyDetailsMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ApplyDetailsResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;

import cn.atsoft.dasheng.sys.core.constant.dictmap.MenuDict;
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
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-15
 */
@Service
public class ApplyDetailsServiceImpl extends ServiceImpl<ApplyDetailsMapper, ApplyDetails> implements ApplyDetailsService {
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuService skuService;

    @Override
    public void add(ApplyDetailsParam param) {
        ApplyDetails entity = getEntity(param);
        this.save(entity);
    }


    @Override
    public void delete(ApplyDetailsParam param) {
        ApplyDetails applyDetails = new ApplyDetails();
        applyDetails.setDisplay(0);
        QueryWrapper<ApplyDetails> applyDetailsQueryWrapper = new QueryWrapper<>();
        applyDetailsQueryWrapper.in("outstock_apply_details_id", param.getOutstockApplyDetailsId());
        this.update(applyDetails, applyDetailsQueryWrapper);
//        this.removeById(getKey(param));
    }


    @Override
    public void update(ApplyDetailsParam param) {
        ApplyDetails oldEntity = getOldEntity(param);
        ApplyDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ApplyDetailsResult findBySpec(ApplyDetailsParam param) {
        return null;
    }

    @Override
    public List<ApplyDetailsResult> findListBySpec(ApplyDetailsParam param) {
        return null;
    }

    @Override
    public PageInfo<ApplyDetailsResult> findPageBySpec(ApplyDetailsParam param) {
        Page<ApplyDetailsResult> pageContext = getPageContext();
        IPage<ApplyDetailsResult> page = this.baseMapper.customPageList(pageContext, param);

        List<Long> brandIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (ApplyDetailsResult record : page.getRecords()) {
            brandIds.add(record.getBrandId());
            skuIds.add(record.getSkuId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId, brandIds);
        List<Brand> brandList = brandIds.size() > 0 ? brandService.list(brandQueryWrapper) : new ArrayList<>();

        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).eq("display", 1).list();

        for (ApplyDetailsResult record : page.getRecords()) {
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


        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ApplyDetailsParam param) {
        return param.getOutstockApplyDetailsId();
    }

    private Page<ApplyDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ApplyDetails getOldEntity(ApplyDetailsParam param) {
        return this.getById(getKey(param));
    }

    private ApplyDetails getEntity(ApplyDetailsParam param) {
        ApplyDetails entity = new ApplyDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
