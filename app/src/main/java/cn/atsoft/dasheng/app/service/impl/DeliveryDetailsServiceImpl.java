package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.DeliveryDetailsMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private ItemsService itemsService;
    @Autowired
    private BrandService brandService;

    @Override
    public DeliveryDetails add(DeliveryDetailsParam param) {
        DeliveryDetails entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(DeliveryDetailsParam param) {
        this.removeById(getKey(param));
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
    public PageInfo<DeliveryDetailsResult> findPageBySpec(DeliveryDetailsParam param) {
        Page<DeliveryDetailsResult> pageContext = getPageContext();
        IPage<DeliveryDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
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
        getItems(results);
        getBrands(results);
        return results;
    }


    @Override
    public DeliveryDetailsResult format(List<DeliveryDetailsResult> data) {
        List<Long> dids = new ArrayList<>();
        List<Long> Iids = new ArrayList<>();


        for (DeliveryDetailsResult record : data) {
            dids.add(record.getDeliveryId());
            Iids.add(record.getItemId());

        }
        QueryWrapper<Delivery> deliveryQueryWrapper = new QueryWrapper<>();
        deliveryQueryWrapper.in("delivery_id", dids);
        List<Delivery> deliveryList = dids.size() == 0 ? new ArrayList<>() : deliveryService.list(deliveryQueryWrapper);


        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("item_id", Iids);
        List<Items> itemsList = Iids.size() == 0 ? new ArrayList<>() : itemsService.list(itemsQueryWrapper);


        for (DeliveryDetailsResult record : data) {
            for (Delivery delivery : deliveryList) {
                if (record.getDeliveryId().equals(delivery.getDeliveryId())) {
                    DeliveryResult deliveryResult = new DeliveryResult();
                    ToolUtil.copyProperties(delivery, deliveryResult);
                    record.setDeliveryResult(deliveryResult);
                    break;
                }
            }
            for (Items items : itemsList) {
                if (items.getItemId().equals(record.getItemId())) {
                    //获取产品质保期
                    int shelfLife = items.getShelfLife();
                    //发货时间
                    String time = String.valueOf(record.getCreateTime());
                    Date date = DateUtil.parse(time);

                    //产品到期日期
                    Date day = DateUtil.offsetDay(date, shelfLife);

                    //获取当前时间
                    Date nowtime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = formatter.format(nowtime);
                    Date parse = DateUtil.parse(format);

                    //剩余保修日期
                    long between = DateUtil.between(parse, day, DateUnit.DAY);
                    DeliveryDetailsParam deliveryDetailsParam = new DeliveryDetailsParam();
                    ToolUtil.copyProperties(record, deliveryDetailsParam);
                    if (parse.before(day)) {
                        deliveryDetailsParam.setQualityType("保修内");
                        this.update(deliveryDetailsParam);
                    } else {
                        deliveryDetailsParam.setQualityType("保修外");
                        this.update(deliveryDetailsParam);
                    }
                }

                if (items.getItemId().equals(record.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }


        }

        return data.size() == 0 ? null : data.get(0);
    }

    ;

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

    public void getItems(List<DeliveryDetailsResult> data) {
        List<Long> ids = new ArrayList<>();
        for (DeliveryDetailsResult datum : data) {
            ids.add(datum.getItemId());
        }
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("item_id", ids);
        List<Items> items = itemsService.list(itemsQueryWrapper);
        for (DeliveryDetailsResult datum : data) {
            for (Items item : items) {
                if (item.getItemId().equals(datum.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(item, itemsResult);
                    datum.setDetailesItems(itemsResult);
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
        List<Brand> brands = brandService.list(brandQueryWrapper);
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
