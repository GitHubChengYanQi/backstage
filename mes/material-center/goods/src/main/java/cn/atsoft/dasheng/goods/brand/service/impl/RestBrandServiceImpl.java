package cn.atsoft.dasheng.goods.brand.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.brand.entity.RestBrand;
import cn.atsoft.dasheng.goods.brand.entity.RestSkuBrandBind;
import cn.atsoft.dasheng.goods.brand.mapper.RestBrandMapper;
import cn.atsoft.dasheng.goods.brand.model.params.RestBrandParam;
import cn.atsoft.dasheng.goods.brand.model.params.RestSkuBrandBindParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestBrandResult;
import cn.atsoft.dasheng.goods.brand.model.result.RestSkuBrandBindResult;
import cn.atsoft.dasheng.goods.brand.service.RestBrandService;
import cn.atsoft.dasheng.goods.brand.service.RestSkuBrandBindService;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 品牌表 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class RestBrandServiceImpl extends ServiceImpl<RestBrandMapper, RestBrand> implements RestBrandService {


    @Autowired
    private RestSkuBrandBindService skuBrandBindService;

    @Autowired
    private RestSkuService skuService;


    @Override
    public Long add(RestBrandParam param) {
        Integer brandName = this.query().eq("brand_name", param.getBrandName()).eq("display", 1).count();
        if (brandName > 0) {
            throw new ServiceException(500, "名字已重复");
        }
        RestBrand entity = getEntity(param);

        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getSkuIds())) {
            skuBrandBindService.addBatchByBrand(new RestSkuBrandBindParam() {{
                setBrandId(entity.getBrandId());
                setSkuIds(param.getSkuIds());
            }});
        }
        return entity.getBrandId();
    }

    @Override
    public void delete(RestBrandParam param) {
        RestBrand byId = this.getById(param.getBrandId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "所删除目标不存在");
        } else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(RestBrandParam param) {
        Integer brandName = this.query().eq("brand_name", param.getBrandName()).count();
        if (brandName > 1) {
            throw new ServiceException(500, "名字已重复");
        }
        if (ToolUtil.isNotEmpty(param.getSkuIds())) {
            List<RestSkuBrandBind> brandBinds = skuBrandBindService.query().eq("brand_id", param.getBrandId()).list();
            for (RestSkuBrandBind brandBind : brandBinds) {
                brandBind.setDisplay(0);
            }
            skuBrandBindService.updateBatchById(brandBinds);
            skuBrandBindService.addBatchByBrand(new RestSkuBrandBindParam() {{
                setBrandId(param.getBrandId());
                setSkuIds(param.getSkuIds());
            }});
        }
        RestBrand oldEntity = getOldEntity(param);
        RestBrand newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestBrandResult findBySpec(RestBrandParam param) {
        return null;
    }

    @Override
    public List<RestBrandResult> findListBySpec(RestBrandParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(RestBrandParam param, DataScope dataScope) {
        Page<RestBrandResult> pageContext = getPageContext();
        IPage<RestBrandResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo pureList(RestBrandParam param, DataScope dataScope) {
        Page<RestBrandResult> pageContext = getPageContext();
        IPage<RestBrandResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<RestBrandResult> getBrandResults(List<Long> brandIds) {
        List<RestBrandResult> brandResults = new ArrayList<>();
        if (ToolUtil.isEmpty(brandIds)) {
            return brandResults;
        }
        List<RestBrand> brands = this.query().in("brand_id", brandIds).eq("display", 1).list();

//        List<BrandResult> list = Collections.singletonList((BrandResult) brands.stream().skip(0).collect(Collectors.toList()));


        if (ToolUtil.isEmpty(brands)) {
            return brandResults;
        }
        for (RestBrand brand : brands) {
            RestBrandResult brandResult = new RestBrandResult();
            ToolUtil.copyProperties(brand, brandResult);
            brandResults.add(brandResult);
        }
        return brandResults;
    }

    @Override
    public RestBrandResult getBrandResult(Long id) {
        RestBrand brand = this.getById(id);
        RestBrandResult brandResult = new RestBrandResult();
        if (ToolUtil.isNotEmpty(brand)) {
            ToolUtil.copyProperties(brand, brandResult);
            return brandResult;
        } else {
            return null;
        }

    }

    private Serializable getKey(RestBrandParam param) {
        return param.getBrandId();
    }

    private Page<RestBrandResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("brandName");
        }};
        return PageFactory.defaultPage(fields);
    }

    private RestBrand getOldEntity(RestBrandParam param) {
        return this.getById(getKey(param));
    }

    private RestBrand getEntity(RestBrandParam param) {
        RestBrand entity = new RestBrand();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void format(List<RestBrandResult> data) {
        //brandIds
        List<Long> brandIds = new ArrayList<>();
        for (RestBrandResult datum : data) {
            brandIds.add(datum.getBrandId());
        }

        List<RestSkuBrandBind> skuBrandBinds = brandIds.size() == 0 ? new ArrayList<>() : skuBrandBindService.query().in("brand_id", brandIds).eq("display", 1).list();



    }
}
