package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
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
        CrmBusinessDetailed entity = getEntity(param);
        this.save(entity);
    }

    Map<Long, CrmBusinessDetailed> addMap;
    Map<Long, CrmBusinessDetailed> updateMap;
    @Override
    public void addAll(CrmBusinessDetailedParam param) {



//        List<CrmBusinessDetailed> list = new ArrayList<>();
//        for (Long itemId : param.getItemIds()) {
//            CrmBusinessDetailed newEntity = new CrmBusinessDetailed();
//            newEntity.setBusinessId(param.getBusinessId());
//            newEntity.setItemId(itemId);
//            newEntity.setSalePrice(0);
//            newEntity.setQuantity(0);
//            newEntity.setTotalPrice(0);
//            list.add(newEntity);
//        }
//        this.saveBatch(list);


        addMap = new HashMap<>();
        updateMap = new HashMap<>();
        List<CrmBusinessDetailed> updateList = new ArrayList<>();
        List<CrmBusinessDetailed> addList = new ArrayList<>();
        int l = 0;
        Map<Long, CrmBusinessDetailed> tableMap = new HashMap();
        for (Long itemId : param.getItemIds()) {
            CrmBusinessDetailed crmBusinessDetailed = this.lambdaQuery().eq(CrmBusinessDetailed::getItemId, itemId).and(i -> i.eq(CrmBusinessDetailed::getBusinessId, param.getBusinessId())).one();
            if (ToolUtil.isNotEmpty(crmBusinessDetailed)) {
                if (crmBusinessDetailed.getBusinessId().equals(param.getBusinessId()) && crmBusinessDetailed.getItemId().equals(itemId)) {
                    CrmBusinessDetailed detailTable = updateMap.get(param.getBusinessId() + itemId);
                    if (ToolUtil.isEmpty(detailTable)) {
                        l = crmBusinessDetailed.getQuantity() + 1;
                        crmBusinessDetailed.setQuantity(l);
                        updateMap.put(param.getBusinessId() + itemId, crmBusinessDetailed);
                    } else {
                        l = l + 1;
                        crmBusinessDetailed.setQuantity(l);
                        updateMap.put(param.getBusinessId() + itemId, crmBusinessDetailed);
                    }

                }
            }
            Boolean table = addBusinessDetial(itemId, param.getBusinessId());
            if (table) {
                tableMap = superposition(param.getBusinessId(), itemId);
            }
        }
        //通过map取出相同数据批量修改
        Set<Map.Entry<Long, CrmBusinessDetailed>> entriesUpdate = updateMap.entrySet();
        for (Map.Entry<Long, CrmBusinessDetailed> longErpPackageTableEntry : entriesUpdate) {
            CrmBusinessDetailed value = longErpPackageTableEntry.getValue();
            updateList.add(value);
        }
        //通过map取出相同数据批量增加
        Set<Map.Entry<Long, CrmBusinessDetailed>> entries = tableMap.entrySet();
        for (Map.Entry<Long, CrmBusinessDetailed> entry : entries) {
            CrmBusinessDetailed entryValue = entry.getValue();
            addList.add(entryValue);
        }
        this.updateBatchById(updateList);
        this.saveBatch(addList);

    }




    Map<Long, CrmBusinessDetailed> superposition(Long businessId, Long itemId) {
        CrmBusinessDetailed packageTable = addMap.get(businessId + itemId);
        if (addMap.containsKey(businessId + itemId)) {
            int l = packageTable.getQuantity() + 1;
            packageTable.setQuantity( l);
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
