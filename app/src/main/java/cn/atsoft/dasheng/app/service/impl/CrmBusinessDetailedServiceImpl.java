package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ErpPackageTableService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CrmBusinessDetailedMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.app.service.CrmBusinessDetailedService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 商机明细表 服务实现类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Service
public class CrmBusinessDetailedServiceImpl extends ServiceImpl<CrmBusinessDetailedMapper, CrmBusinessDetailed> implements CrmBusinessDetailedService {
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private ErpPackageTableService erpPackageTableService;
    @Autowired
    private BrandService brandService;


    @Override
    public void add(CrmBusinessDetailedParam param) {
        QueryWrapper<CrmBusinessDetailed> queryWrapper = new QueryWrapper<>();
        CrmBusinessDetailed one = this.lambdaQuery().in(CrmBusinessDetailed::getBusinessId, param.getBusinessId()).and(i -> i.in(CrmBusinessDetailed::getItemId, param.getItemId())).one();
        List<CrmBusinessDetailed> list = new ArrayList<>();
        if (ToolUtil.isNotEmpty(one)) {
            CrmBusinessDetailed update = new CrmBusinessDetailed();
            update.setId(one.getId());
            update.setBrandId(one.getBrandId());
            update.setBusinessId(one.getBusinessId());
            update.setQuantity(one.getQuantity() + 1);
            update.setItemId(one.getItemId());
            this.updateById(update);
        } else {
            CrmBusinessDetailed save = new CrmBusinessDetailed();
            save.setBusinessId(param.getBusinessId());
            save.setBrandId(param.getBrandId());
            save.setQuantity(param.getQuantity());
            save.setItemId(param.getItemId());
            save.setSalePrice(param.getSalePrice());
            save.setTotalPrice(param.getTotalPrice());
            this.save(save);
        }

    }

    Map<Long, CrmBusinessDetailed> addMap;
    Map<Long, CrmBusinessDetailed> updateMap;
    Map<Long, CrmBusinessDetailed> map;

    @Override
    public void addAll(BusinessDetailedParam param) {
        map = new HashMap<>();
        if (ToolUtil.isNotEmpty(param.getBusinessDetailedParam())) {
            List<CrmBusinessDetailed> updateOrAdd = new ArrayList<>();
            for (CrmBusinessDetailedParam detailedParam : param.getBusinessDetailedParam()) {
                if (detailedParam.getQuantity() == 0) {
                    throw new ServiceException(500, "注意产品数量");
                }
                map = judge(param.getBusinessId(), detailedParam.getItemId(), detailedParam.getBrandId(), detailedParam.getQuantity(), detailedParam.getSalePrice());


            }
            for (Map.Entry<Long, CrmBusinessDetailed> longCrmBusinessDetailedEntry : map.entrySet()) {
                CrmBusinessDetailed value = longCrmBusinessDetailedEntry.getValue();
                updateOrAdd.add(value);
            }

            this.saveOrUpdateBatch(updateOrAdd);
        }
    }

    Map<Long, CrmBusinessDetailed> judge(Long businessIds, Long itemIds, Long brandIds, int number, int money) {
        List<CrmBusinessDetailed> businessDetaileds = this.lambdaQuery().in(CrmBusinessDetailed::getBusinessId, businessIds)
                .list();
        //判断当前商机详情是否有这个商品  没有直接添加
        if (ToolUtil.isEmpty(businessDetaileds)) {
            CrmBusinessDetailed businessDetailedByMap = new CrmBusinessDetailed();
            businessDetailedByMap.setBusinessId(businessIds);
            businessDetailedByMap.setQuantity(number);
            businessDetailedByMap.setBrandId(brandIds);
            businessDetailedByMap.setItemId(itemIds);
            businessDetailedByMap.setSalePrice(money);
            businessDetailedByMap.setTotalPrice(money * number);
            businessDetailedByMap.setDisplay(1);
            map.put(itemIds + brandIds, businessDetailedByMap);
            return map;
        }

        //是否存入相同物品 如果有直接获取之前的数据 进行累加
//        CrmBusinessDetailed crmBusinessDetailed = map.get(itemIds + brandIds);
//        if (ToolUtil.isNotEmpty(crmBusinessDetailed)) {
//            int i = crmBusinessDetailed.getQuantity() + number;
//            crmBusinessDetailed.setQuantity(i);
//            map.put(itemIds + brandIds, crmBusinessDetailed);
//        }


        //判断商机详情是否有粗存在物品  有的直接叠加数量
        for (CrmBusinessDetailed businessDetailed : businessDetaileds) {
            if (businessDetailed.getItemId().equals(itemIds) && businessDetailed.getBrandId().equals(brandIds)) {
                int i = businessDetailed.getQuantity() + number;
                int newMoney = i * money;
                businessDetailed.setSalePrice(money);
                businessDetailed.setTotalPrice(newMoney);
                businessDetailed.setQuantity(i);
                businessDetailed.setDisplay(1);
                map.put(businessDetailed.getItemId() + businessDetailed.getBrandId(), businessDetailed);
                break;
            }

        }

        //通过map判段这个商品是否存在  没有直接添加
        CrmBusinessDetailed BusinessDetailed = map.get(itemIds + brandIds);
        if (ToolUtil.isEmpty(BusinessDetailed)) {
            CrmBusinessDetailed businessDetailed = new CrmBusinessDetailed();
            businessDetailed.setBusinessId(businessIds);
            businessDetailed.setQuantity(number);
            businessDetailed.setBrandId(brandIds);
            businessDetailed.setItemId(itemIds);
            businessDetailed.setSalePrice(money);
            businessDetailed.setTotalPrice(money * number);
            businessDetailed.setDisplay(1);
            map.put(itemIds + brandIds, businessDetailed);

        }

        return map;
    }


    Map<Long, CrmBusinessDetailed> superposition(Long businessId, Long itemId) {
        CrmBusinessDetailed packageTable = addMap.get(businessId + itemId);
        if (addMap.containsKey(businessId + itemId)) {
            int l = packageTable.getQuantity() + 1;
            packageTable.setQuantity(l);
            addMap.put(businessId + itemId, packageTable);
        }
        if (ToolUtil.isEmpty(packageTable)) {
            CrmBusinessDetailed packageTable1 = new CrmBusinessDetailed();
            packageTable1.setBusinessId(businessId);
            packageTable1.setItemId(itemId);
            packageTable1.setQuantity(1);
            addMap.put(businessId + itemId, packageTable1);
        }
        return addMap;
    }


    Boolean addBusinessDetial(Long itemId, Long businessId) {
        Boolean a = true;
        List<CrmBusinessDetailed> list = this.lambdaQuery().list();
        for (CrmBusinessDetailed crmBusinessDetailed : list) {
            if (crmBusinessDetailed.getBusinessId().equals(businessId) && crmBusinessDetailed.getItemId().equals(itemId)) {
                a = false;
                break;
            }
        }
        if (ToolUtil.isEmpty(list)) {
            return true;
        }
        return a;
    }


    @Override
    public void addAllPackages(CrmBusinessDetailedParam param) {
        Map<Long, ErpPackageTable> map = new HashMap<>();
        List<ErpPackageTable> erpPackageTables = erpPackageTableService.lambdaQuery()
                .in(ErpPackageTable::getPackageId, param.getPackagesIds())
                .list();
        //通过map去掉重复套餐数据
        for (ErpPackageTable erpPackageTable : erpPackageTables) {
            Long itemId = erpPackageTable.getItemId();
            Long brandId = erpPackageTable.getBrandId();
            ErpPackageTable packageTable = map.get(itemId + brandId);
            if (map.containsKey(itemId + brandId)) {
                packageTable.setQuantity(packageTable.getQuantity() + erpPackageTable.getQuantity());
                map.put(itemId + brandId, packageTable);
            } else {
                map.put(itemId + brandId, erpPackageTable);
            }
        }
        //套餐数据
        List<ErpPackageTable> packageTableList = new ArrayList<>();
        for (Map.Entry<Long, ErpPackageTable> longErpPackageTableEntry : map.entrySet()) {
            ErpPackageTable value = longErpPackageTableEntry.getValue();
            packageTableList.add(value);
        }


        List<CrmBusinessDetailed> updataBusinessDetailed = new ArrayList<>();
        List<CrmBusinessDetailed> addBusinessDetailed = new ArrayList<>();
        //判断套餐数据是否与商机详情数据相同
        for (ErpPackageTable erpPackageTable : packageTableList) {
            CrmBusinessDetailed judge = judge(param.getBusinessId(), erpPackageTable.getItemId(), erpPackageTable.getBrandId());
            // 如果相同 直接叠加
            if (ToolUtil.isNotEmpty(judge)) {
                judge.setQuantity(Math.toIntExact(judge.getQuantity() + erpPackageTable.getQuantity()));
                int totalPrice = judge.getQuantity() * judge.getSalePrice();
                judge.setTotalPrice(totalPrice);
                judge.setDisplay(1);
                updataBusinessDetailed.add(judge);
            } else {
                // 不同直接加一条数据
                CrmBusinessDetailed crmBusinessDetailed = new CrmBusinessDetailed();
                crmBusinessDetailed.setBusinessId(param.getBusinessId());
                crmBusinessDetailed.setItemId(erpPackageTable.getItemId());
                crmBusinessDetailed.setBrandId(erpPackageTable.getBrandId());
                crmBusinessDetailed.setQuantity(Math.toIntExact(erpPackageTable.getQuantity()));
                crmBusinessDetailed.setSalePrice(Math.toIntExact(erpPackageTable.getSalePrice()));
                crmBusinessDetailed.setDisplay(1);
                int totalPrice = Math.toIntExact(erpPackageTable.getQuantity() * erpPackageTable.getSalePrice());
                crmBusinessDetailed.setTotalPrice(totalPrice);
                addBusinessDetailed.add(crmBusinessDetailed);
            }
        }
        this.saveBatch(addBusinessDetailed);
        this.updateBatchById(updataBusinessDetailed);
    }

    CrmBusinessDetailed judge(Long businessId, Long itemId, Long brandId) {
        //查询当前商机详情
        CrmBusinessDetailed crmBusinessDetaileds = this.lambdaQuery()
                .in(CrmBusinessDetailed::getBusinessId, businessId)
                .eq(CrmBusinessDetailed::getItemId, itemId)
                .eq(CrmBusinessDetailed::getBrandId, brandId)
                .one();
        if (ToolUtil.isNotEmpty(crmBusinessDetaileds)) {
            return crmBusinessDetaileds;
        }

        return null;
    }

    @Override
    public void delete(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed byId = this.getById(param.getId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "删除目标不存在");
        }
        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed oldEntity = getOldEntity(param);
        CrmBusinessDetailed newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessDetailedResult findBySpec(CrmBusinessDetailedParam param) {
        return null;
    }

    @Override
    public List<CrmBusinessDetailedResult> findListBySpec(CrmBusinessDetailedParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(CrmBusinessDetailedParam param, DataScope dataScope ) {
        Page<CrmBusinessDetailedResult> pageContext = getPageContext();
        IPage<CrmBusinessDetailedResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);

        List<Long> detailIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (CrmBusinessDetailedResult record : page.getRecords()) {
            detailIds.add(record.getItemId());
            brandIds.add(record.getBrandId());
        }
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("item_id", detailIds);
        List<Items> list = detailIds.size() == 0 ? new ArrayList<>() : itemsService.list(queryWrapper);

        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.in("brand_id", brandIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);

        for (CrmBusinessDetailedResult record : page.getRecords()) {
            for (Items items : list) {
                if (items.getItemId().equals(record.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }
            for (Brand brands : brandList) {
                if (brands.getBrandId().equals(record.getBrandId())) {
                    BrandResult brandsResult = new BrandResult();
                    ToolUtil.copyProperties(brands, brandsResult);
                    record.setBrandResult(brandsResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessDetailedParam param) {
        return param.getId();
    }

    private Page<CrmBusinessDetailedResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessDetailed getOldEntity(CrmBusinessDetailedParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessDetailed getEntity(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed entity = new CrmBusinessDetailed();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
