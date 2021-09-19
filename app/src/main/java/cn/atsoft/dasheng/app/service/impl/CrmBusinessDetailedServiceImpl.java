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

    @Override
    public void addAll(CrmBusinessDetailedParam param) {
        List<CrmBusinessDetailed> list = new ArrayList<>();
        for (Long itemId : param.getItemIds()) {
            CrmBusinessDetailed newEntity = new CrmBusinessDetailed();
            newEntity.setBusinessId(param.getBusinessId());
            newEntity.setItemId(itemId);
            newEntity.setSalePrice(0);
            newEntity.setQuantity(0);
            newEntity.setTotalPrice(0);
            list.add(newEntity);
        }
        this.saveBatch(list);

    }

    @Override
    public void addAllPackages(CrmBusinessDetailedParam param) {

        //newEntryList{CrmBusinessDerailed实体对象(多个)}
        Map<Long, CrmBusinessDetailed> map = new HashMap<>();
        List<CrmBusinessDetailed> crmBusinessDetailedList = new ArrayList<>();
        //查出当前商机详情的物品
        List<CrmBusinessDetailed> crmBusinessDetaileds = this.lambdaQuery()
                .in(CrmBusinessDetailed::getBusinessId, param.getBusinessId())
                .list();
        //查询当前
        if (ToolUtil.isNotEmpty(crmBusinessDetaileds)) {
            List<CrmBusinessDetailed> updateDetailedList = new ArrayList<>();
            List<CrmBusinessDetailed> detailedList = new ArrayList<>();
            for (CrmBusinessDetailed crmBusinessDetailed : crmBusinessDetaileds) {
                ErpPackageTable erpPackageTable = judgeItem(param.getPackagesIds(), crmBusinessDetailed.getItemId());
            }


            this.saveBatch(detailedList);
            this.updateBatchById(updateDetailedList);
        }
//        if (ToolUtil.isNotEmpty(crmBusinessDetaileds)) {
//            for (CrmBusinessDetailed crmBusinessDetailed : crmBusinessDetaileds) {
//                Long detailedItemId = crmBusinessDetailed.getItemId();
//                int detailedQuantity = crmBusinessDetailed.getQuantity();
//                if (ToolUtil.isNotEmpty(param.getPackagesIds())) {
//                    for (Long packagesId : param.getPackagesIds()) {
//                        //通过packgeid查出套餐详细数据
//                        List<ErpPackageTable> erpPackageTables = erpPackageTableService.lambdaQuery()
//                                .in(ErpPackageTable::getPackageId, packagesId)
//                                .list();
//                        for (ErpPackageTable erpPackageTable : erpPackageTables) {
//                            Long tableItemIds = erpPackageTable.getItemId();
//                            Long taboeQuantity = erpPackageTable.getQuantity();
//                            if (tableItemIds.equals(detailedItemId)) {
//                                crmBusinessDetailed.setQuantity(Math.toIntExact(detailedQuantity + taboeQuantity));
//                                CrmBusinessDetailedParam crmBusinessDetailedParam = new CrmBusinessDetailedParam();
//                                ToolUtil.copyProperties(crmBusinessDetailed, crmBusinessDetailedParam);
//                                this.update(crmBusinessDetailedParam);
//                            } else {
//                                CrmBusinessDetailed crmBusinessDetailed1 = new CrmBusinessDetailed();
//                                crmBusinessDetailed1.setBusinessId(param.getBusinessId());
//                                crmBusinessDetailed1.setItemId(tableItemIds);
//                                crmBusinessDetailed1.setQuantity(Math.toIntExact(taboeQuantity));
//                                this.save(crmBusinessDetailed1);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        if (ToolUtil.isEmpty(crmBusinessDetaileds)) {
            List<ErpPackageTable> erpPackageTables = erpPackageTableService.lambdaQuery()
                    .in(ErpPackageTable::getPackageId, param.getPackagesIds())
                    .list();
            for (ErpPackageTable erpPackageTable : erpPackageTables) {
                CrmBusinessDetailed crmBusinessDetailed = new CrmBusinessDetailed();
                crmBusinessDetailed.setItemId(erpPackageTable.getItemId());
                crmBusinessDetailed.setQuantity(Math.toIntExact(erpPackageTable.getQuantity()));
                crmBusinessDetailed.setBusinessId(param.getBusinessId());
                crmBusinessDetailedList.add(crmBusinessDetailed);
            }
            this.saveBatch(crmBusinessDetailedList);
        }


    }

    ErpPackageTable judgeItem(List<Long> packageIds, Long item) {
        for (Long packageId : packageIds) {
            List<ErpPackageTable> erpPackageTables = erpPackageTableService.lambdaQuery()
                    .eq(ErpPackageTable::getPackageId, packageId)
                    .list();
            for (ErpPackageTable erpPackageTable : erpPackageTables) {
                if (erpPackageTable.getItemId().equals(item)) {
                    return erpPackageTable;
                }
            }
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
