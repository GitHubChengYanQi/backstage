package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.entity.ItemData;
import cn.atsoft.dasheng.crm.mapper.DataMapper;
import cn.atsoft.dasheng.crm.model.params.DataParam;
import cn.atsoft.dasheng.crm.model.params.ItemDataParam;
import cn.atsoft.dasheng.crm.model.result.DataClassificationResult;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import cn.atsoft.dasheng.crm.service.DataClassificationService;
import cn.atsoft.dasheng.crm.service.DataService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.ItemDataService;
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
 * 资料 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
@Service
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements DataService {
    @Autowired
    private ItemDataService itemDataService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private DataClassificationService dataClassificationService;

    @Override
    public void add(DataParam param) {
        param.setDataClassificationId(param.getDataclassId().get(0));
        Data entity = getEntity(param);
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getItemId())) {
            List<ItemData> itemDatas = new ArrayList<>();
            for (Long aLong : param.getItemId()) {
                ItemData itemData = new ItemData();
                itemData.setDataId(entity.getDataId());
                itemData.setItemId(aLong);
                itemDatas.add(itemData);
            }
            if (ToolUtil.isNotEmpty(itemDatas)) {
                itemDataService.saveBatch(itemDatas);
            }
        }
    }

    @Override
    public void delete(DataParam param) {
        this.removeById(getKey(param));
        if (ToolUtil.isNotEmpty(param.getDataId())) {
            List<ItemData> itemDataList = itemDataService.lambdaQuery().in(ItemData::getDataId, param.getDataId()).list();
            if (ToolUtil.isNotEmpty(itemDataList)) {
                for (ItemData itemData : itemDataList) {
                    ItemDataParam itemDataParam = new ItemDataParam();
                    ToolUtil.copyProperties(itemData, itemDataParam);
                    itemDataService.delete(itemDataParam);
                }
            }
        }


    }

    @Override
    public void update(DataParam param) {
        Data oldEntity = getOldEntity(param);
        Data newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        if (ToolUtil.isNotEmpty(param.getItemId())) {
            List<Long> ids = new ArrayList<>();
            for (Long aLong : param.getItemId()) {
                ids.add(aLong);
            }
            if (ToolUtil.isNotEmpty(ids)) {
                itemDataService.update().notIn("item_id", ids).and(i -> i.eq("data_id", param.getDataId())).remove();
//                List<ItemData> itemDataList = itemDataService.lambdaQuery().notIn(ItemData::getItemId, ids).and(i -> i.eq(ItemData::getDataId, param.getDataId())).list();
//                if (ToolUtil.isNotEmpty(itemDataList)) {
//                    List<Long> itemDataIds = new ArrayList<>();
//                    for (ItemData itemData : itemDataList) {
////                        ItemDataParam itemDataParam = new ItemDataParam();
////                        ToolUtil.copyProperties(itemData, itemDataParam);
////                        itemDataService.delete(itemDataParam);
//                        itemDataIds.add(itemData.getItemsDataId());
//
//                    }
//                    itemDataService.removeByIds(itemDataIds);
//                }
                List<ItemData> itemData = itemDataService.lambdaQuery().eq(ItemData::getDataId, param.getDataId()).list();

                for (ItemData itemDatum : itemData) {
                    for (Long id : ids) {
                        if (itemDatum.getItemId().equals(id)) {
                            ids.remove(id);
                            break;
                        }

                    }
                }
                List<ItemData> itemDatas = new ArrayList<>();
                for (Long id : ids) {
                    ItemData itemDatad = new ItemData();
                    itemDatad.setDataId(param.getDataId());
                    itemDatad.setItemId(id);
                    itemDatas.add(itemDatad);

                }
                itemDataService.saveBatch(itemDatas);

            }


        }
    }

    @Override
    public DataResult findBySpec(DataParam param) {

        return null;
    }

    @Override
    public List<DataResult> findListBySpec(DataParam param) {
        return null;
    }

    @Override
    public PageInfo<DataResult> findPageBySpec(DataScope dataScope, DataParam param) {
        Page<DataResult> pageContext = getPageContext();
        IPage<DataResult> page = this.baseMapper.customPageList(dataScope, pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public DataResult detail(DataParam param) {
        Data data = this.getById(param.getDataId());
        DataResult dataResult = new DataResult();
        ToolUtil.copyProperties(data, dataResult);
        List<ItemData> itemDataList = itemDataService.lambdaQuery().in(ItemData::getDataId, dataResult.getDataId()).list();
        List<Long> itemIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(itemDataList)) {
            for (ItemData itemData : itemDataList) {
                itemIds.add(itemData.getItemId());
            }
        }
        if (ToolUtil.isNotEmpty(itemIds)) {
            List<Items> itemsList = itemsService.lambdaQuery().in(Items::getItemId, itemIds).list();
            List<ItemsResult> itemsResults = new ArrayList<>();
            for (Long itemId : itemIds) {
                for (Items items : itemsList) {
                    if (itemId.equals(items.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        itemsResults.add(itemsResult);
                    }
                    dataResult.setItemId(itemsResults);
                }
            }
        }

        return dataResult;
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Data data = new Data();
        data.setDisplay(0);
        QueryWrapper<Data> dataQueryWrapper = new QueryWrapper<>();
        dataQueryWrapper.in("data_id", ids);
        this.update(data, dataQueryWrapper);
    }

    private Serializable getKey(DataParam param) {
        return param.getDataId();
    }

    private Page<DataResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Data getOldEntity(DataParam param) {
        return this.getById(getKey(param));
    }

    private Data getEntity(DataParam param) {
        Data entity = new Data();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<DataResult> data) {
        List<Long> ids = new ArrayList<>();
        List<Long> dataClassIds = new ArrayList<>();
        for (DataResult datum : data) {
            ids.add(datum.getDataId());
            dataClassIds.add(datum.getDataClassificationId());
        }

        List<DataClassification> dataClassifications = dataClassIds.size() == 0 ? new ArrayList<>() : dataClassificationService.lambdaQuery().in(DataClassification::getDataClassificationId, dataClassIds).list();
        if (ToolUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                List<Long> itemIds = new ArrayList<>();
                List<ItemData> itemDataList = itemDataService.lambdaQuery().in(ItemData::getDataId, id).list();
                if (ToolUtil.isNotEmpty(itemDataList)) {
                    for (ItemData itemData : itemDataList) {
                        itemIds.add(itemData.getItemId());
                    }
                }
                if (ToolUtil.isNotEmpty(itemIds)) {
                    List<Items> itemsList = itemsService.lambdaQuery().in(Items::getItemId, itemIds).list();
                    if (ToolUtil.isNotEmpty(itemsList)) {
                        Integer count = this.lambdaQuery().count();
                        for (DataResult datum : data) {
                            datum.setCount(count);
                            if (datum.getDataId().equals(id)) {
                                List<ItemsResult> itemsResults = new ArrayList<>();
                                for (Items items : itemsList) {
                                    ItemsResult itemsResult = new ItemsResult();
                                    ToolUtil.copyProperties(items, itemsResult);
                                    itemsResults.add(itemsResult);
                                }
                                datum.setItemId(itemsResults);
                            }
                            for (DataClassification dataClassification : dataClassifications) {
                                if (dataClassification.getDataClassificationId().equals(datum.getDataClassificationId())) {
                                    DataClassificationResult dataClassificationResult = new DataClassificationResult();
                                    ToolUtil.copyProperties(dataClassification, dataClassificationResult);
                                    datum.setDataClassificationResult(dataClassificationResult);
                                    break;
                                }
                            }


                        }
                    }
                }

            }


        }

    }
}
