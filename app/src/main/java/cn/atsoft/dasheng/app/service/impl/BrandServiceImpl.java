package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.mapper.BrandMapper;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.SkuBrandBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuBrandBindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Autowired
    private PartsService partsService;

    @Autowired
    private SkuBrandBindService skuBrandBindService;

    @Autowired
    private SkuService skuService;


    @Override
    public Long add(BrandParam param) {
        Integer brandName = this.query().eq("brand_name", param.getBrandName()).eq("display",1).count();
        if (brandName > 0) {
            throw new ServiceException(500, "名字已重复");
        }
        Brand entity = getEntity(param);

        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getSkuIds())){
            skuBrandBindService.addBatchByBrand(new SkuBrandBindParam(){{
                setBrandId(entity.getBrandId());
                setSkuIds(param.getSkuIds());
            }});
        }
        return entity.getBrandId();
    }

    @Override
    public void delete(BrandParam param) {
        Brand byId = this.getById(param.getBrandId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "所删除目标不存在");
        } else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(BrandParam param) {
        Integer brandName = this.query().eq("brand_name", param.getBrandName()).count();
        if (brandName > 1) {
            throw new ServiceException(500, "名字以重复");
        }
        if (ToolUtil.isNotEmpty(param.getSkuIds())){
            List<SkuBrandBind> brandBinds = skuBrandBindService.query().eq("brand_id", param.getBrandId()).list();
            for (SkuBrandBind brandBind : brandBinds) {
                brandBind.setDisplay(0);
            }
            skuBrandBindService.updateBatchById(brandBinds);
            skuBrandBindService.addBatchByBrand(new SkuBrandBindParam(){{
                setBrandId(param.getBrandId());
                setSkuIds(param.getSkuIds());
            }});
        }
        Brand oldEntity = getOldEntity(param);
        Brand newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BrandResult findBySpec(BrandParam param) {
        return null;
    }

    @Override
    public List<BrandResult> findListBySpec(BrandParam param) {
        return null;
    }

    @Override
    public PageInfo<BrandResult> findPageBySpec(BrandParam param, DataScope dataScope) {
        Page<BrandResult> pageContext = getPageContext();
        IPage<BrandResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<BrandResult> getBrandResults(List<Long> brandIds) {
        List<BrandResult> brandResults = new ArrayList<>();
        if (ToolUtil.isEmpty(brandIds)) {
            return brandResults;
        }
        List<Brand> brands = this.query().in("brand_id", brandIds).eq("display", 1).list();

//        List<BrandResult> list = Collections.singletonList((BrandResult) brands.stream().skip(0).collect(Collectors.toList()));


        if (ToolUtil.isEmpty(brands)) {
            return brandResults;
        }
        for (Brand brand : brands) {
            BrandResult brandResult = new BrandResult();
            ToolUtil.copyProperties(brand, brandResult);
            brandResults.add(brandResult);
        }
        return brandResults;
    }

    @Override
    public BrandResult getBrandResult(Long id) {
        Brand brand = this.getById(id);
        BrandResult brandResult = new BrandResult();
        ToolUtil.copyProperties(brand, brandResult);
        return brandResult;
    }

    private Serializable getKey(BrandParam param) {
        return param.getBrandId();
    }

    private Page<BrandResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("brandName");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Brand getOldEntity(BrandParam param) {
        return this.getById(getKey(param));
    }

    private Brand getEntity(BrandParam param) {
        Brand entity = new Brand();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public void format(List<BrandResult> data) {
        List<Long> brandIs = new ArrayList<>();
        for (BrandResult datum : data) {
            brandIs.add(datum.getBrandId());
        }
        List<SkuBrandBind> skuBrandBinds =brandIs.size()==0? new ArrayList<>(): skuBrandBindService.query().in("brand_id", brandIs).eq("display", 1).list();
        List<Long> skuIds = new ArrayList<>();
        for (SkuBrandBind skuBrandBind : skuBrandBinds) {
            skuIds.add(skuBrandBind.getSkuId());
        }
        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
//        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
//        partsQueryWrapper.in("brand_id", brandIs);
//        List<Parts> list = partsService.list(partsQueryWrapper);
//        partsService.getByIds(brandIs);
//
//        for (BrandResult datum : data) {
//            for (Parts parts : list) {
//                if (datum.getBrandId().equals(parts.getBrandId())) {
//                    PartsResult partsResult =new PartsResult();
//                    ToolUtil.copyProperties(parts,partsResult);
//                     datum.setPartsResult(partsResult);
//                     break;
//                }
//            }
//        }
        for (BrandResult datum : data) {
            skuIds = new ArrayList<>();
            List<SkuResult> results = new ArrayList<>();
            for (SkuBrandBind skuBrandBind : skuBrandBinds) {
                for (SkuResult skuResult : skuResults) {
                    if(datum.getBrandId().equals(skuBrandBind.getSkuBrandBind()) && skuBrandBind.getSkuId().equals(skuResult.getSkuId())){
                        skuIds.add(skuBrandBind.getSkuId());
                        results.add(skuResult);
                    }
                }
            }
            datum.setSkuIds(skuIds);
            datum.setSkuResults(results);
        }
    }
}
