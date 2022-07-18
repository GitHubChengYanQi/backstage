package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockHandle;
import cn.atsoft.dasheng.erp.mapper.InstockHandleMapper;
import cn.atsoft.dasheng.erp.model.params.InstockHandleParam;
import cn.atsoft.dasheng.erp.model.result.InstockHandleResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InstockHandleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库操作结果 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-07-08
 */
@Service
public class InstockHandleServiceImpl extends ServiceImpl<InstockHandleMapper, InstockHandle> implements InstockHandleService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StorehousePositionsService positionsService;


    @Override
    public void add(InstockHandleParam param) {
        InstockHandle entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InstockHandleParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockHandleParam param) {
        InstockHandle oldEntity = getOldEntity(param);
        InstockHandle newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockHandleResult findBySpec(InstockHandleParam param) {
        return null;
    }

    @Override
    public List<InstockHandleResult> findListBySpec(InstockHandleParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockHandleResult> findPageBySpec(InstockHandleParam param) {
        Page<InstockHandleResult> pageContext = getPageContext();
        IPage<InstockHandleResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockHandleParam param) {
        return param.getInstockHandleId();
    }

    private Page<InstockHandleResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockHandle getOldEntity(InstockHandleParam param) {
        return this.getById(getKey(param));
    }

    private InstockHandle getEntity(InstockHandleParam param) {
        InstockHandle entity = new InstockHandle();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public void format(List<InstockHandleResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();

        for (InstockHandleResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            customerIds.add(datum.getCustomerId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.lambdaQuery().in(Brand::getBrandId, brandIds).list();
        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<CustomerResult> customerResults = BeanUtil.copyToList(customerList, CustomerResult.class, new CopyOptions());
        Map<Long, List<StorehousePositionsResult>> positionMap = positionsService.getMap(skuIds);

        for (InstockHandleResult datum : data) {
            for (CustomerResult customerResult : customerResults) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(customerResult.getCustomerId())) {
                    datum.setCustomerResult(customerResult);
                    break;
                }
            }


            for (SkuResult sku : skuResults) {
                List<StorehousePositionsResult> positionsResults = positionMap.get(sku.getPositionId());
                sku.setPositionsResult(positionsResults);
                if (datum.getSkuId() != null && sku.getSkuId().equals(datum.getSkuId())) {
                    datum.setSkuResult(sku);
                    break;
                }
            }

            for (Brand brand : brands) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brand.getBrandId())) {
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);
                    datum.setBrandResult(brandResult);
                    break;
                }
            }

        }

    }

}
