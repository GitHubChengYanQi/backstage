package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.DeliveryDetailsMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-20
 */
@Service
public class DeliveryDetailsServiceImpl extends ServiceImpl<DeliveryDetailsMapper, DeliveryDetails> implements DeliveryDetailsService {
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuService skuService;

    @Override
    public DeliveryDetails add(DeliveryDetailsParam param) {
        DeliveryDetails entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(DeliveryDetailsParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(DeliveryDetailsParam param) {
        DeliveryDetails oldEntity = getOldEntity(param);
        DeliveryDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeliveryDetailsResult findBySpec(DeliveryDetailsParam param) {

        return null;
    }

    @Override
    public List<DeliveryDetailsResult> findListBySpec(DeliveryDetailsParam param) {
        List<DeliveryDetailsResult> deliveryDetailsResults = this.baseMapper.customList(param);
        format(deliveryDetailsResults);
        return deliveryDetailsResults;
    }

    @Override
    public PageInfo<DeliveryDetailsResult> findPageBySpec(DeliveryDetailsParam param, DataScope dataScope) {
        Page<DeliveryDetailsResult> pageContext = getPageContext();
        IPage<DeliveryDetailsResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<DeliveryDetailsResult> getByIds(List<Long> ids) {
        QueryWrapper<DeliveryDetails> detailsQueryWrapper = new QueryWrapper<>();
        detailsQueryWrapper.in("delivery_details_id", ids);
        List<DeliveryDetails> deliveryDetails = this.list(detailsQueryWrapper);
        List<DeliveryDetailsResult> results = new ArrayList<>();
        for (DeliveryDetails deliveryDetail : deliveryDetails) {
            DeliveryDetailsResult deliveryDetailsResult = new DeliveryDetailsResult();
            ToolUtil.copyProperties(deliveryDetail, deliveryDetailsResult);
            results.add(deliveryDetailsResult);
        }
        getSkus(results);
        getBrands(results);
        return results;
    }


    @Override
    public DeliveryDetailsResult format(List<DeliveryDetailsResult> data) {
        List<Long> dids = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();


        for (DeliveryDetailsResult record : data) {
            dids.add(record.getDeliveryId());
            brandIds.add(record.getBrandId());
            skuIds.add(record.getSkuId());

        }

        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(brandIds)) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        QueryWrapper<Delivery> deliveryQueryWrapper = new QueryWrapper<>();
        deliveryQueryWrapper.in("delivery_id", dids);
        List<Delivery> deliveryList = dids.size() == 0 ? new ArrayList<>() : deliveryService.list(deliveryQueryWrapper);

        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();


        for (DeliveryDetailsResult record : data) {
            for (Delivery delivery : deliveryList) {
                if (record.getDeliveryId().equals(delivery.getDeliveryId())) {
                    DeliveryResult deliveryResult = new DeliveryResult();
                    ToolUtil.copyProperties(delivery, deliveryResult);
                    record.setDeliveryResult(deliveryResult);
                    break;
                }
            }

            if (ToolUtil.isNotEmpty(record.getSkuId())) {
                Sku sku = skuService.getById(record.getSkuId());
                record.setSku(sku);
            }


            if (ToolUtil.isNotEmpty(brandList)) {
                for (Brand brand : brandList) {
                    if (brand.getBrandId().equals(record.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        record.setBrandResult(brandResult);
                        break;
                    }
                }
            }
            List<BackSku> backSkus = skuService.backSku(record.getSkuId());

            SpuResult spuResult = skuService.backSpu(record.getSkuId());

            record.setBackSkus(backSkus);
            if (ToolUtil.isNotEmpty(spuResult)) {
                record.setSpuResult(spuResult);
            }


//            for (Sku sku : skus) {
//                if (record.getSkuId() != null && sku.getSkuId().equals(record.getSkuId())) {
//                    //获取产品质保期
////                    int shelfLife = items.getShelfLife();
//                    int shelfLife = 1;
//                    //发货时间
//                    String time = String.valueOf(record.getCreateTime());
//                    Date date = DateUtil.parse(time);
//
//                    //产品到期日期
//                    Date day = DateUtil.offsetDay(date, shelfLife);
//
//                    //获取当前时间
//                    Date nowtime = new Date();
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String format = formatter.format(nowtime);
//                    Date parse = DateUtil.parse(format);
//
//                    //剩余保修日期
//                    long between = DateUtil.between(parse, day, DateUnit.DAY);
//                    DeliveryDetailsParam deliveryDetailsParam = new DeliveryDetailsParam();
//                    ToolUtil.copyProperties(record, deliveryDetailsParam);
//                    if (parse.before(day)) {
//                        deliveryDetailsParam.setQualityType("保修内");
//                        this.update(deliveryDetailsParam);
//                    } else {
//                        deliveryDetailsParam.setQualityType("保修外");
//                        this.update(deliveryDetailsParam);
//                    }
//                }
//
//                if (sku.getSkuId().equals(record.getSkuId())) {
//                    SkuResult skuResult = new SkuResult();
//                    ToolUtil.copyProperties(sku, skuResult);
//                    record.setSkuResult(skuResult);
//                    break;
//                }
//            }


        }

        return data.size() == 0 ? null : data.get(0);
    }



    private Serializable getKey(DeliveryDetailsParam param) {
        return param.getDeliveryDetailsId();
    }

    private Page<DeliveryDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DeliveryDetails getOldEntity(DeliveryDetailsParam param) {
        return this.getById(getKey(param));
    }

    private DeliveryDetails getEntity(DeliveryDetailsParam param) {
        DeliveryDetails entity = new DeliveryDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void getSkus(List<DeliveryDetailsResult> data) {
        List<Long> skuIds = new ArrayList<>();
        for (DeliveryDetailsResult datum : data) {
            skuIds.add(datum.getSkuId());
        }
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();
        for (DeliveryDetailsResult datum : data) {
            for (Sku sku : skus) {
                if (datum.getSkuId() != null && sku.getSkuId().equals(datum.getSkuId())) {
                    SkuResult skuResult = new SkuResult();
                    ToolUtil.copyProperties(sku, skuResult);
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
        }

    }

    public void getBrands(List<DeliveryDetailsResult> data) {
        List<Long> ids = new ArrayList<>();
        for (DeliveryDetailsResult datum : data) {
            ids.add(datum.getBrandId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.in("brand_id", ids);
        List<Brand> brands = ids.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);
        for (DeliveryDetailsResult datum : data) {
            for (Brand brand : brands) {
                if (brand.getBrandId().equals(datum.getBrandId())) {
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);
                    datum.setDetailsBrand(brandResult);
                    break;
                }
            }
        }
    }
}
