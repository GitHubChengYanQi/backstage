package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ErpPackageTableService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CrmBusinessDetailedMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.app.service.CrmBusinessDetailedService;
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
                int oldMoney = businessDetailed.getSalePrice();
                int newMoney = number * money;
                businessDetailed.setSalePrice(oldMoney + newMoney);
                businessDetailed.setQuantity(i);
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
        List<Long> itemIds = new ArrayList<>();

        QueryWrapper<ErpPackageTable> queryWrapper = new QueryWrapper<>();
        List<ErpPackageTable> list = erpPackageTableService.lambdaQuery().in(ErpPackageTable::getPackageId, param.getPackagesIds()).list();

//        List<ErpPackageTable> list = erpPackageTableService.list(queryWrapper);


        for (ErpPackageTable erpPackageTable : list) {
            itemIds.add(erpPackageTable.getItemId());
        }
        param.setItemIds(itemIds);
//        addAll(param);
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
    public PageInfo<CrmBusinessDetailedResult> findPageBySpec(CrmBusinessDetailedParam param) {
        Page<CrmBusinessDetailedResult> pageContext = getPageContext();
        IPage<CrmBusinessDetailedResult> page = this.baseMapper.customPageList(pageContext, param);

        List<Long> detailIds = new ArrayList<>();
        for (CrmBusinessDetailedResult record : page.getRecords()) {
            detailIds.add(record.getItemId());
        }
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("item_id", detailIds);
        List<Items> list = detailIds.size() == 0 ? new ArrayList<>() : itemsService.list(queryWrapper);
        for (CrmBusinessDetailedResult record : page.getRecords()) {
            for (Items items : list) {
                if (items.getItemId().equals(record.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
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
