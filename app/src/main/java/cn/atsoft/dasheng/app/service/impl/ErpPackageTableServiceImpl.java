package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.mapper.ErpPackageTableMapper;
import cn.atsoft.dasheng.app.model.params.ErpPackageTableParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageTableResult;
import cn.atsoft.dasheng.app.service.ErpPackageTableService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 套餐分表 服务实现类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Service
public class ErpPackageTableServiceImpl extends ServiceImpl<ErpPackageTableMapper, ErpPackageTable> implements ErpPackageTableService {
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private BrandService brandService;


    @Override
    public void add(ErpPackageTableParam param) {
        ErpPackageTable entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ErpPackageTableParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(ErpPackageTableParam param) {
        ErpPackageTable oldEntity = getOldEntity(param);
        ErpPackageTable newEntity = getEntity(param);
        newEntity.setTotalPrice(newEntity.getQuantity() * newEntity.getSalePrice());
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ErpPackageTableResult findBySpec(ErpPackageTableParam param) {
        return null;
    }

    @Override
    public List<ErpPackageTableResult> findListBySpec(ErpPackageTableParam param) {
        return null;
    }

    @Override
    public PageInfo<ErpPackageTableResult> findPageBySpec(ErpPackageTableParam param, DataScope dataScope) {
        Page<ErpPackageTableResult> pageContext = getPageContext();
        IPage<ErpPackageTableResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);

    }


    private Map<Long, ErpPackageTable> map;

    @Override
    public void batchAdd(BusinessDetailedParam param) {
        map = new HashMap<>();
        if (ToolUtil.isNotEmpty(param.getBusinessDetailedParam())) {
            List<ErpPackageTable> updateOrAdd = new ArrayList<>();
            for (CrmBusinessDetailedParam detailedParam : param.getBusinessDetailedParam()) {
                map = judge(param.getPackageId(), detailedParam.getItemId(), detailedParam.getBrandId(), detailedParam.getQuantity(), detailedParam.getSalePrice());


            }
            for (Map.Entry<Long, ErpPackageTable> longErpPackageTableEntry : map.entrySet()) {
                ErpPackageTable value = longErpPackageTableEntry.getValue();
                updateOrAdd.add(value);

            }
            this.saveOrUpdateBatch(updateOrAdd);
        }
    }

    Map<Long, ErpPackageTable> judge(Long packageId, Long itemIds, Long brandIds, int number, int money) {
        List<ErpPackageTable> erpPackageTables = this.lambdaQuery().in(ErpPackageTable::getPackageId, packageId)
                .list();
        //判断当前商机详情是否有这个商品  没有直接添加
        if (ToolUtil.isEmpty(erpPackageTables)) {
            ErpPackageTable erpPackageTableByMap = new ErpPackageTable();
            erpPackageTableByMap.setPackageId(packageId);
            erpPackageTableByMap.setQuantity((long) number);
            erpPackageTableByMap.setBrandId(brandIds);
            erpPackageTableByMap.setItemId(itemIds);
            erpPackageTableByMap.setSalePrice(Long.valueOf(money));
            erpPackageTableByMap.setTotalPrice(Long.valueOf(money*number));
            map.put(itemIds + brandIds, erpPackageTableByMap);
            return map;
        }


        for (ErpPackageTable erpPackageTable : erpPackageTables) {
            if (erpPackageTable.getItemId().equals(itemIds) && erpPackageTable.getBrandId().equals(brandIds)) {
                Long i = erpPackageTable.getQuantity() + number;
                long l = erpPackageTable.getQuantity() + number;
                int newMoney = Math.toIntExact(l * money);
                erpPackageTable.setTotalPrice(Long.valueOf(newMoney));
                erpPackageTable.setQuantity(i);
                map.put(erpPackageTable.getItemId() + erpPackageTable.getBrandId(), erpPackageTable);
                break;
            }
        }


        //通过map判段这个商品是否存在  没有直接添加
        ErpPackageTable erpPackageTable = map.get(itemIds + brandIds);
        if (ToolUtil.isEmpty(erpPackageTable)) {
            ErpPackageTable erpPackageTableByMap = new ErpPackageTable();
            erpPackageTableByMap.setPackageId(packageId);
            erpPackageTableByMap.setQuantity((long) number);
            erpPackageTableByMap.setBrandId(brandIds);
            erpPackageTableByMap.setItemId(itemIds);
            erpPackageTableByMap.setSalePrice(Long.valueOf(money));
            erpPackageTableByMap.setTotalPrice(Long.valueOf(money*number));
            map.put(itemIds + brandIds, erpPackageTableByMap);

        }

        return map;
    }





    private Serializable getKey(ErpPackageTableParam param) {
        return param.getId();
    }

    private Page<ErpPackageTableResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpPackageTable getOldEntity(ErpPackageTableParam param) {
        return this.getById(getKey(param));
    }

    private ErpPackageTable getEntity(ErpPackageTableParam param) {
        ErpPackageTable entity = new ErpPackageTable();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ErpPackageTableResult> data) {
        List<Long> itemIds = new ArrayList<>();
        List<Long> brandeIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(data)) {
            for (ErpPackageTableResult record : data) {
                itemIds.add(record.getItemId());
                brandeIds.add(record.getBrandId());
            }

            QueryWrapper<Items> ItemsQueryWrapper = new QueryWrapper<>();
            if (ToolUtil.isNotEmpty(itemIds)) {
                ItemsQueryWrapper.in("item_id", itemIds);
            }
            List<Items> ItemsList = itemIds.size() == 0 ? new ArrayList<>() : itemsService.list(ItemsQueryWrapper);

            QueryWrapper<Brand> BrandQueryWrapper = new QueryWrapper<>();
            if (ToolUtil.isNotEmpty(brandeIds)) {
                BrandQueryWrapper.in("brand_id", brandeIds);
            }
            List<Brand> BrandList = brandeIds.size() == 0 ? new ArrayList<>() : brandService.list(BrandQueryWrapper);
            for (ErpPackageTableResult record : data) {
                for (Items items : ItemsList) {
                    if (items.getItemId().equals(record.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        record.setItemsResult(itemsResult);
                        break;
                    }
                }
                for (Brand brand : BrandList) {
                    if (brand.getBrandId().equals(record.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        record.setBrandResult(brandResult);
                        break;
                    }
                }
            }
        }
    }
}
