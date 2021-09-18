package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
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
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        this.removeById(getKey(param));
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
    public PageInfo<ErpPackageTableResult> findPageBySpec(ErpPackageTableParam param) {
        Page<ErpPackageTableResult> pageContext = getPageContext();
        IPage<ErpPackageTableResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);

    }

    @Override
    public void batchAdd(ErpPackageTableParam param) {
        List<ErpPackageTable> updateList = new ArrayList<>();
        List<ErpPackageTable> addList = new ArrayList<>();
        for (Long itemId : param.getItemIds()) {
            ErpPackageTable erpPackageTable = this.lambdaQuery().eq(ErpPackageTable::getItemId, itemId).and(i -> i.eq(ErpPackageTable::getPackageId, param.getPackageId())).one();
            if (ToolUtil.isNotEmpty(erpPackageTable)) {
                if (erpPackageTable.getPackageId().equals(param.getPackageId()) && erpPackageTable.getItemId().equals(itemId)) {
                    long l = erpPackageTable.getQuantity() + 1;
                    erpPackageTable.setQuantity(l);
                    updateList.add(erpPackageTable);
                }

            }
            Boolean table = addPackgeTable(itemId, param.getPackageId());
            if (table) {
                ErpPackageTable newEntry = new ErpPackageTable();
                newEntry.setPackageId(param.getPackageId());
                newEntry.setItemId(itemId);
                newEntry.setQuantity(1L);
                addList.add(newEntry);
            }
        }


        this.updateBatchById(updateList);
        this.saveBatch(addList);

    }

    Boolean addPackgeTable(Long itemId, Long packageId) {
        Boolean a = true;
        List<ErpPackageTable> list = this.lambdaQuery().list();
        for (ErpPackageTable erpPackageTable : list) {
            if (erpPackageTable.getPackageId().equals(packageId) && erpPackageTable.getItemId().equals(itemId)) {
                a=false;
                break;
            }
        }
        if (ToolUtil.isEmpty(list)) {
            return  true;
        }
        return a;
    }


//        List<ErpPackageTable> list = new ArrayList<>();
//        List<ErpPackageTable> updateList = new ArrayList<>();


    //查询是否已有物品
//        QueryWrapper<ErpPackageTable> idwrapper = new QueryWrapper<>();
//        idwrapper.lambda().in(ErpPackageTable::getPackageId,param.getPackageId());
//        List<ErpPackageTable> dataList = new ArrayList<>();
//        for (Long itemId : param.getItemIds()) {
//            for (ErpPackageTable erpPackageTable : dataList) {
//                if (erpPackageTable.getItemId().equals(itemId) ){
//                    dataList.remove(erpPackageTable);
//                    if (erpPackageTable.getQuantity() == null){
//                        erpPackageTable.setQuantity(0L);
//                    }
//                    erpPackageTable.setQuantity(erpPackageTable.getQuantity()+1L);
//                    updateList.add(erpPackageTable);
//                }else {
//                    //如果没有则增加
//                    ErpPackageTable newEntry = new ErpPackageTable();
//                    newEntry.setPackageId(param.getPackageId());
//                    newEntry.setItemId(itemId);
//                    newEntry.setQuantity(1L);
//                    list.remove(erpPackageTable);
//                    list.add(newEntry);
//                }
//                break;
//            }
//        }


//        //查询是否已有物品
//        QueryWrapper<ErpPackageTable> idwrapper = new QueryWrapper<>();
//        idwrapper.lambda().in(ErpPackageTable::getPackageId, param.getPackageId());
//        List<ErpPackageTable> dataList = this.baseMapper.selectList(idwrapper);
//        List<Long> packItems = new ArrayList<>();
//        List<Long> items = new ArrayList<>();
//        for (ErpPackageTable data : dataList) {
//            packItems.add(data.getItemId());
//        }
//        for (Long itemId : param.getItemIds()) {
//            items.add(itemId);
//        }
//        packItems.removeAll(items);
//        items.removeAll(packItems);
//        for (Long item : packItems) {
//            // 不相同产品新增一条
//            ErpPackageTable newEntry = new ErpPackageTable();
//            newEntry.setPackageId(param.getPackageId());
//            newEntry.setItemId(item);
//            newEntry.setQuantity(1L);
//            this.save(newEntry);
//        }
//        for (ErpPackageTable data : dataList) {
//            for (Long item : items) {
//                if (data.getItemId().equals(item)) {
//                    if (data.getQuantity() == null) {
//                        data.setQuantity(0L);
//                    }
//                    data.setQuantity(data.getQuantity() + 1L);
////                    updateList.add(data);
//                    this.updateById(data);
//                }
//            }
//        }

//        for(ErpPackageTable data : dataList){
//            for (Long itemId : param.getItemIds()) {
//                // 相同产品则增加数量
//                if (data.getItemId().equals(itemId)) {
//                    if (data.getQuantity() == null){
//                        data.setQuantity(0L);
//                    }
//                    data.setQuantity(data.getQuantity()+1L);
////                    updateList.add(data);
//                    this.updateById(data);
//                }else{
//                    // 判断是否有重复数据
//                    List<ErpPackageTable> dataList1 = this.baseMapper.selectList(idwrapper);
//                    for(ErpPackageTable data1 : dataList1){
//                        if(data1.getItemId().equals(itemId)) {
//                            data1.setQuantity(data.getQuantity()+1L);
//                            this.save(data1);
//                        }else{
//
//                        }
//                    }
//
//                }
//            }
//        }


//        for (Long itemId : param.getItemIds()) {
//            for (ErpPackageTable erpPackageTable : data) {
//                if (erpPackageTable.getItemId().equals(itemId) ){
//                    if (erpPackageTable.getQuantity() == null){
//                        erpPackageTable.setQuantity(0L);
//                    }
//                    erpPackageTable.setQuantity(erpPackageTable.getQuantity()+1L);
//                    update.add(erpPackageTable);
//                }else {
//                    //如果没有则增加
//                    ErpPackageTable newEntry = new ErpPackageTable();
//                    newEntry.setPackageId(param.getPackageId());
//                    newEntry.setItemId(itemId);
//                    newEntry.setQuantity(1L);
//                    list.add(newEntry);
//                }
//                break;
//            }
//        }

//        for (Long itemId : param.getItemIds()) {
//            if (erpPackageTable.getItemId().equals(itemId)) {
//                //如果有 则增加数量+1
//                erpPackageTable.setQuantity(erpPackageTable.getQuantity()+1);
//                update.add(erpPackageTable);
//            }else{
//                //如果没有则增加
//                ErpPackageTable newEntry = new ErpPackageTable();
//                newEntry.setPackageId(param.getPackageId());
//                newEntry.setItemId(itemId);
//                list.add(newEntry);
//            }
//        }

//        for (Long itemId : param.getItemIds()) {
//            ErpPackageTable newEntry = new ErpPackageTable();
//            newEntry.setPackageId(param.getPackageId());
//            newEntry.setItemId(itemId);
//            list.add(newEntry);
//        }


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
