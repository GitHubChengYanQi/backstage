package cn.atsoft.dasheng.supplier.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.supplier.entity.SupplierBrand;
import cn.atsoft.dasheng.supplier.mapper.SupplierBrandMapper;
import cn.atsoft.dasheng.supplier.model.params.SupplierBrandParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBrandResult;
import cn.atsoft.dasheng.supplier.service.SupplierBrandService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 供应商品牌绑定 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Service
public class SupplierBrandServiceImpl extends ServiceImpl<SupplierBrandMapper, SupplierBrand> implements SupplierBrandService {
    @Autowired
    private BrandService brandService;

    @Override
    public void add(SupplierBrandParam param) {
        SupplierBrand entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SupplierBrandParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SupplierBrandParam param) {
        SupplierBrand oldEntity = getOldEntity(param);
        SupplierBrand newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    /**
     * 返回供应商的所有品牌
     *
     * @param customerResults
     */
    @Override
    public void getBrand(List<CustomerResult> customerResults) {
        List<Long> customerIds = new ArrayList<>();
        for (CustomerResult customerResult : customerResults) {
            customerIds.add(customerResult.getCustomerId());
        }
        List<SupplierBrand> supplierBrands = customerIds.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(SupplierBrand::getCustomerId, customerIds).list();

        List<Long> brandIds = new ArrayList<>();
        for (SupplierBrand supplierBrand : supplierBrands) {
            brandIds.add(supplierBrand.getBrandId());
        }
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        for (SupplierBrand supplierBrand : supplierBrands) {
            for (CustomerResult customerResult : customerResults) {
                if (supplierBrand.getCustomerId().equals(customerResult.getCustomerId())) {
                    judge(customerResult, supplierBrand.getBrandId(), brandResults);
                }
            }
        }
    }

    /**
     * 供应商  品牌组合
     *
     * @param customerResult
     * @param brandId
     * @param brandResults
     */
    private void judge(CustomerResult customerResult, Long brandId, List<BrandResult> brandResults) {
        List<BrandResult> brandResultList = new ArrayList<>();
        for (BrandResult brandResult : brandResults) {

            if (brandId.equals(brandResult.getBrandId())) {
                brandResultList.add(brandResult);
            }
        }
        customerResult.setBrandResults(brandResultList);
    }


    @Override
    public SupplierBrandResult findBySpec(SupplierBrandParam param) {
        return null;
    }

    @Override
    public List<SupplierBrandResult> findListBySpec(SupplierBrandParam param) {
        return null;
    }

    @Override
    public PageInfo<SupplierBrandResult> findPageBySpec(SupplierBrandParam param) {
        Page<SupplierBrandResult> pageContext = getPageContext();
        IPage<SupplierBrandResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SupplierBrandParam param) {
        return param.getSupplierBrandId();
    }

    private Page<SupplierBrandResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SupplierBrand getOldEntity(SupplierBrandParam param) {
        return this.getById(getKey(param));
    }

    private SupplierBrand getEntity(SupplierBrandParam param) {
        SupplierBrand entity = new SupplierBrand();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
